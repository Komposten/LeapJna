/*
 * Copyright 2020-2022 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.example;

import java.util.Arrays;

import com.sun.jna.ptr.LongByReference;

import komposten.leapjna.example.VisualiserListener.LogType;
import komposten.leapjna.leapc.LeapC;
import komposten.leapjna.leapc.data.LEAP_CONNECTION;
import komposten.leapjna.leapc.data.LEAP_CONNECTION_MESSAGE;
import komposten.leapjna.leapc.data.LEAP_DEVICE;
import komposten.leapjna.leapc.data.LEAP_DEVICE_INFO;
import komposten.leapjna.leapc.data.LEAP_RECORDING;
import komposten.leapjna.leapc.data.LEAP_RECORDING_PARAMETERS;
import komposten.leapjna.leapc.data.LEAP_RECORDING_STATUS;
import komposten.leapjna.leapc.enums.eLeapEventType;
import komposten.leapjna.leapc.enums.eLeapPolicyFlag;
import komposten.leapjna.leapc.enums.eLeapRS;
import komposten.leapjna.leapc.enums.eLeapRecordingFlags;
import komposten.leapjna.leapc.events.LEAP_CONFIG_CHANGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_CONFIG_RESPONSE_EVENT;
import komposten.leapjna.leapc.events.LEAP_CONNECTION_EVENT;
import komposten.leapjna.leapc.events.LEAP_DEVICE_EVENT;
import komposten.leapjna.leapc.events.LEAP_DEVICE_FAILURE_EVENT;
import komposten.leapjna.leapc.events.LEAP_DEVICE_STATUS_CHANGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_IMAGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_LOG_EVENT;
import komposten.leapjna.leapc.events.LEAP_LOG_EVENTS;
import komposten.leapjna.leapc.events.LEAP_POLICY_EVENT;
import komposten.leapjna.leapc.events.LEAP_TRACKING_EVENT;


class VisualiserBackend
{
	public enum State
	{
		INITIAL, CONNECTING, POLLING, ERROR, CLOSED
	}


	private static final int FRAME_RATE = 60;
	private static final float FRAME_TIME = 1000f / FRAME_RATE;

	private VisualiserListener listener;

	private LEAP_CONNECTION leapConnection;
	private LEAP_RECORDING recording;

	LongByReference imagesRequestId = new LongByReference();

	private long lastHandledFrame;
	private long lastHandledImage;

	private boolean imagesAllowed;
	private boolean isPaused;


	VisualiserBackend(VisualiserListener listener)
	{
		this.listener = listener;
	}


	public boolean areImagesAllowed()
	{
		return imagesAllowed;
	}


	void start()
	{
		listener.onStateChanged(State.CONNECTING);
		
		leapConnection = new LEAP_CONNECTION();
		eLeapRS result = LeapC.INSTANCE.LeapCreateConnection(null, leapConnection);

		if (result == eLeapRS.Success)
		{
			result = LeapC.INSTANCE.LeapOpenConnection(leapConnection.handle);

			if (result == eLeapRS.Success)
			{
				listener.onStateChanged(State.POLLING);
				doPollLoop();
			}
			else
			{
				listener.onLogMessage(LogType.ERROR,
						"Failed to open a connection to the Ultraleap Tracking Service: %s", result);
			}
		}
		else
		{
			listener.onLogMessage(LogType.ERROR,
					"Failed to create a connection to the Ultraleap Tracking Service: %s", result);
		}

		listener.onStateChanged(State.ERROR);
	}


	private void doPollLoop()
	{
		boolean firstIteration = true;

		eLeapRS previousResult = null;
		while (true)
		{
			LEAP_CONNECTION_MESSAGE message = new LEAP_CONNECTION_MESSAGE();
			eLeapRS result = LeapC.INSTANCE.LeapPollConnection(leapConnection.handle, 30,
					message);

			if (firstIteration)
			{
				// Enable the images and pause policies
				LeapC.INSTANCE.LeapSetPolicyFlags(leapConnection.handle, eLeapPolicyFlag
						.createMask(eLeapPolicyFlag.Images, eLeapPolicyFlag.AllowPauseResume), 0);

				firstIteration = false;
			}
			
			if (result == eLeapRS.Timeout)
			{
				if (previousResult != eLeapRS.Timeout)
				{
					listener.onLogMessage(LogType.ERROR,
							"Timed out while polling for events! The tracking service might be paused.");
					listener.onLogMessage(LogType.SEPARATOR, "");
				}
			}
			else if (result != eLeapRS.Success)
			{
				listener.onLogMessage(LogType.ERROR,
						"Polling failed with result %s for event type %s", result, message.getType());
				listener.onLogMessage(LogType.SEPARATOR, "");
			}

			boolean handled = handleInformationalEvents(message, leapConnection);

			if (!handled)
			{
				if (message.getType() == eLeapEventType.Tracking)
				{
					handleFrame(message.getTrackingEvent());
				}
				else if (message.getType() == eLeapEventType.Image)
				{
					handleImage(message.getImageEvent());
				}
			}
			
			previousResult = result;

			if (Thread.interrupted())
			{
				break;
			}
		}

		listener.onLogMessage(LogType.NORMAL, "Closing connection!");
		listener.onLogMessage(LogType.SEPARATOR, "");

		LeapC.INSTANCE.LeapCloseConnection(leapConnection.handle);
		LeapC.INSTANCE.LeapDestroyConnection(leapConnection.handle);

		listener.onStateChanged(State.CLOSED);
	}


	private boolean handleInformationalEvents(LEAP_CONNECTION_MESSAGE message,
			LEAP_CONNECTION leapConnection)
	{
		boolean handled = true;

		switch (message.getType())
		{
			case Connection :
				handleConnectionEvent(message.getConnectionEvent());
				break;
			case ConnectionLost :
				handleConnectionLostEvent();
				break;
			case Device :
				handleDeviceEvent(message.getDeviceEvent());
				break;
			case DeviceStatusChange :
				handleDeviceStatusChangeEvent(message.getDeviceStatusChangeEvent());
				break;
			case DeviceLost :
				handleDeviceLostEvent(message.getDeviceLostEvent());
				break;
			case DeviceFailure :
				handleDeviceFailureEvent(message.getDeviceFailureEvent());
				break;
			case Policy :
				handlePolicyEvent(message.getPolicyEvent(), leapConnection);
				break;
			case ConfigChange :
				handleConfigChangeEvent(message.getConfigChangeEvent());
				break;
			case ConfigResponse :
				handleConfigResponseEvent(message);
				break;
			case LogEvent :
				handleLogEvent(message.getLogEvent());
				break;
			case LogEvents :
				handleLogEvents(message.getLogEvents());
				break;
			default :
				handled = false;
				break;
		}

		return handled;
	}


	private void handleConnectionEvent(LEAP_CONNECTION_EVENT event)
	{
		listener.onLogMessage(LogType.NORMAL, "Connection flags: %s", event.getFlags());
		listener.onLogMessage(LogType.SEPARATOR, "");
	}


	private void handleConnectionLostEvent()
	{
		listener.onLogMessage(LogType.NORMAL, "Connection lost!");
		listener.onLogMessage(LogType.SEPARATOR, "");
	}


	private void handleDeviceEvent(LEAP_DEVICE_EVENT event)
	{
		eLeapRS result;

		// Print device info.
		listener.onLogMessage(LogType.HEADER, "Device detected");
		listener.onLogMessage(LogType.NORMAL, "  Id: %d", event.device.id);

		// Open the device to retrieve information about it.
		LEAP_DEVICE phDevice = new LEAP_DEVICE();
		result = LeapC.INSTANCE.LeapOpenDevice(event.device, phDevice);

		if (result == eLeapRS.Success)
		{
			// Read the length of the device's serial string
			LEAP_DEVICE_INFO info = new LEAP_DEVICE_INFO();
			result = LeapC.INSTANCE.LeapGetDeviceInfo(phDevice.handle, info);

			if (result == eLeapRS.InsufficientBuffer || result == eLeapRS.Success)
			{
				// Allocate space for the serial and read device info
				info.allocateSerialBuffer(info.serial_length);
				result = LeapC.INSTANCE.LeapGetDeviceInfo(phDevice.handle, info);

				if (result == eLeapRS.Success)
				{
					listener.onLogMessage(LogType.NORMAL, "  Status: %s",
							Arrays.toString(info.getStatus()));
					listener.onLogMessage(LogType.NORMAL, "  Baseline: %d \u00b5m", info.baseline);
					listener.onLogMessage(LogType.NORMAL, "  FoV: %.02f\u00b0 x %.02f\u00b0 (HxV)",
							Math.toDegrees(info.h_fov), Math.toDegrees(info.v_fov));
					listener.onLogMessage(LogType.NORMAL, "  Range: %d \u00b5m", info.range);
					listener.onLogMessage(LogType.NORMAL, "  Serial: %s", info.serial);
					listener.onLogMessage(LogType.NORMAL, "  Product ID: %s (%d)",
							LeapC.INSTANCE.LeapDevicePIDToString(info.pid), info.pid);
					listener.onLogMessage(LogType.NORMAL, "  Capabilities: %s",
							Arrays.toString(info.getCapabilities()));

					// Close the device since we no longer need it.
					LeapC.INSTANCE.LeapCloseDevice(phDevice.handle);
				}
				else
				{
					listener.onLogMessage(LogType.ERROR, "Failed to read device info: %s", result);
				}
			}
			else
			{
				listener.onLogMessage(LogType.ERROR,
						"Failed to read device info to get serial length: %s", result);
			}
		}
		else
		{
			listener.onLogMessage(LogType.ERROR, "Failed to open device: %s", result);
		}

		listener.onLogMessage(LogType.SEPARATOR, "");
	}


	private void handleDeviceStatusChangeEvent(LEAP_DEVICE_STATUS_CHANGE_EVENT event)
	{
		listener.onLogMessage(LogType.NORMAL, "Device status changed: %d | From %s to %s",
				event.device.id, Arrays.toString(event.getLastStatus()),
				Arrays.toString(event.getStatus()));
		listener.onLogMessage(LogType.SEPARATOR, "");
	}


	private void handlePolicyEvent(LEAP_POLICY_EVENT event,
			@SuppressWarnings("unused") LEAP_CONNECTION leapConnection)
	{
		listener.onLogMessage(LogType.NORMAL, "Active policies: %s",
				Arrays.toString(event.getCurrentPolicy()));
		listener.onLogMessage(LogType.SEPARATOR, "");

		// Request the current images_mode setting to check if images were activated
		// through the Ultraleap Tracking control panel.
		// LeapC.INSTANCE.LeapRequestConfigValue(leapConnection.handle,
		// Configurations.Tracking.IMAGES_MODE, imagesRequestId);
		if (Arrays.stream(event.getCurrentPolicy()).anyMatch(p -> p == eLeapPolicyFlag.Images))
		{
			imagesAllowed = true;
		}
	}


	private void handleLogEvent(LEAP_LOG_EVENT event)
	{
		listener.onLogMessage(LogType.NORMAL, "[LOG] %s: %s", event.getSeverity(),
				event.message);
		listener.onLogMessage(LogType.SEPARATOR, "");
	}


	private void handleLogEvents(LEAP_LOG_EVENTS events)
	{
		for (LEAP_LOG_EVENT logEvent : events.getEvents())
		{
			handleLogEvent(logEvent);
		}
	}


	private void handleDeviceFailureEvent(LEAP_DEVICE_FAILURE_EVENT event)
	{
		listener.onLogMessage(LogType.ERROR, "Device failure: %s",
				Arrays.toString(event.getStatus()));
		listener.onLogMessage(LogType.SEPARATOR, "");
	}


	private void handleDeviceLostEvent(LEAP_DEVICE_EVENT event)
	{
		listener.onLogMessage(LogType.NORMAL, "Device was lost:");
		listener.onLogMessage(LogType.NORMAL, "  Id: %d", event.device.id);
		listener.onLogMessage(LogType.NORMAL, "  Status: %d",
				Arrays.toString(event.getStatus()));
		listener.onLogMessage(LogType.SEPARATOR, "");
	}
	
	
	private void handleConfigChangeEvent(LEAP_CONFIG_CHANGE_EVENT event)
	{
		listener.onLogMessage(LogType.NORMAL, "Result of config change request %d: %s", event.requestID,
				event.status);
	}


	private void handleConfigResponseEvent(LEAP_CONNECTION_MESSAGE message)
	{
		LEAP_CONFIG_RESPONSE_EVENT responseEvent = message.getConfigResponseEvent();

		if (responseEvent.requestID == imagesRequestId.getValue())
		{
			imagesAllowed = responseEvent.value.getInt() == 2;
		}
	}


	private void handleFrame(LEAP_TRACKING_EVENT trackingEvent)
	{
		if (System.currentTimeMillis() - lastHandledFrame > FRAME_TIME)
		{
			if (recording != null)
			{
				LeapC.INSTANCE.LeapRecordingWrite(recording.handle, trackingEvent, null);
			}

			listener.onFrame(trackingEvent);

			lastHandledFrame = System.currentTimeMillis();
		}
	}


	private void handleImage(LEAP_IMAGE_EVENT imageEvent)
	{
		if (System.currentTimeMillis() - lastHandledImage > FRAME_TIME)
		{
			listener.onImage(imageEvent);

			lastHandledImage = System.currentTimeMillis();
		}
	}


	public void requestPause()
	{
		eLeapRS result = LeapC.INSTANCE.LeapSetPause(leapConnection.handle, isPaused ? 0 : 1);

		if (result == eLeapRS.Success)
		{
			isPaused = !isPaused;
			listener.onLogMessage(LogType.NORMAL, isPaused ? "Paused" : "Resumed");
		}
		else
		{
			listener.onLogMessage(LogType.ERROR, "Failed to %s: %s",
					(isPaused ? "resume" : "pause"), result);
		}

		listener.onLogMessage(LogType.SEPARATOR, "");
	}


	public void requestRecording()
	{
		if (recording == null)
		{
			recording = new LEAP_RECORDING();
			LEAP_RECORDING_PARAMETERS params = new LEAP_RECORDING_PARAMETERS(
					eLeapRecordingFlags.Writing);
			String filePath = "recording_" + System.currentTimeMillis() + ".lmt";

			eLeapRS result = LeapC.INSTANCE.LeapRecordingOpen(recording, filePath, params);

			if (result != eLeapRS.Success)
			{
				recording = null;
				listener.onLogMessage(LogType.ERROR, "Failed to start recording: %s", result);
			}
			else
			{
				listener.onLogMessage(LogType.NORMAL, "Recording to %s", filePath);
			}
		}
		else
		{
			eLeapRS result = LeapC.INSTANCE.LeapRecordingClose(recording);

			if (result != eLeapRS.Success)
			{
				listener.onLogMessage(LogType.NORMAL, "Failed to close recording: %s", result);
			}
			else
			{
				listener.onLogMessage(LogType.NORMAL, "Stopped recording!");
				recording = null;
			}
		}

		listener.onLogMessage(LogType.SEPARATOR, "");
	}


	public void requestRecordingStatus()
	{
		if (recording != null)
		{
			LEAP_RECORDING_STATUS pStatus = new LEAP_RECORDING_STATUS();
			eLeapRS result = LeapC.INSTANCE.LeapRecordingGetStatus(recording.handle, pStatus);

			if (result != eLeapRS.Success)
			{
				listener.onLogMessage(LogType.ERROR, "Failed to get recording status: %s",
						result);
			}
			else
			{
				listener.onLogMessage(LogType.NORMAL, "Status of current recording: %s",
						Arrays.toString(pStatus.getMode()));
			}
		}
		else
		{
			listener.onLogMessage(LogType.NORMAL, "Status of current recording: Not recording");
		}

		listener.onLogMessage(LogType.SEPARATOR, "");
	}
}
