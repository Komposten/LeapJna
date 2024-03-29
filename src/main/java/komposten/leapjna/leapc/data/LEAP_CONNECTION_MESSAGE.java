/*
 * Copyright 2020-2022 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.data;

import java.util.function.Function;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.leapc.enums.Enums;
import komposten.leapjna.leapc.enums.eLeapEventType;
import komposten.leapjna.leapc.events.LEAP_CONFIG_CHANGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_CONFIG_RESPONSE_EVENT;
import komposten.leapjna.leapc.events.LEAP_CONNECTION_EVENT;
import komposten.leapjna.leapc.events.LEAP_CONNECTION_LOST_EVENT;
import komposten.leapjna.leapc.events.LEAP_DEVICE_EVENT;
import komposten.leapjna.leapc.events.LEAP_DEVICE_FAILURE_EVENT;
import komposten.leapjna.leapc.events.LEAP_DEVICE_STATUS_CHANGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_DROPPED_FRAME_EVENT;
import komposten.leapjna.leapc.events.LEAP_EVENT;
import komposten.leapjna.leapc.events.LEAP_EYE_EVENT;
import komposten.leapjna.leapc.events.LEAP_HEAD_POSE_EVENT;
import komposten.leapjna.leapc.events.LEAP_IMAGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_IMU_EVENT;
import komposten.leapjna.leapc.events.LEAP_LOG_EVENT;
import komposten.leapjna.leapc.events.LEAP_LOG_EVENTS;
import komposten.leapjna.leapc.events.LEAP_POINT_MAPPING_CHANGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_POLICY_EVENT;
import komposten.leapjna.leapc.events.LEAP_TRACKING_EVENT;
import komposten.leapjna.leapc.events.LEAP_TRACKING_MODE_EVENT;


/**
 * Defines a basic message from the LeapC message queue.
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv423LEAP_CONNECTION_MESSAGE">LeapC
 *      API - LEAP_CONNECTION_MESSAGE</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
@SuppressWarnings("deprecation")
@FieldOrder({ "size", "type", "pEvent", "device_id" })
public class LEAP_CONNECTION_MESSAGE extends Structure
{
	/** The size of this message struct. */
	public int size;

	/**
	 * The event type. Use {@link #getType()} to get the type as a {@link eLeapEventType}
	 * value.
	 */
	public int type;

	/**
	 * <p>
	 * A pointer to the event data for the current type of message.
	 * </p>
	 * <p>
	 * Check the event type using {@link #type} or {@link #getType()} and then call the
	 * relevant <code>get***Event()</code> method for that event type to get the event data.
	 * </p>
	 */
	public Pointer pEvent;
	
	/**
	 * <p> 
	 * A unique ID for the attached device that sent this message. A value of 0 indicates that it was
	 * a system-wide message, and not device specific. Use this ID to distinguish messages sent from
	 * multiple attached devices.
	 * </p>
	 * @since LeapJna 1.2.0
	 */
	public int device_id;

	private LEAP_EVENT event;

	public LEAP_CONNECTION_MESSAGE()
	{
		super(ALIGN_NONE);
	}


	public LEAP_CONNECTION_MESSAGE(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}


	@Override
	public void read()
	{
		super.read();
		event = null;
	}


	/**
	 * @return The event type as an {@link eLeapEventType} instead of an <code>int</code>.
	 */
	public eLeapEventType getType()
	{
		return Enums.parse(type, eLeapEventType.Unknown);
	}


	/**
	 * @return The event data as a connection event.
	 * @throws IllegalStateException If this event message is not an
	 *           {@link eLeapEventType#Connection} event.
	 */
	public LEAP_CONNECTION_EVENT getConnectionEvent()
	{
		checkType(eLeapEventType.Connection);
		return getOrCreateEvent(LEAP_CONNECTION_EVENT::new);
	}


	/**
	 * @return The event data as a connection lost event.
	 * @throws IllegalStateException If this event message is not an
	 *           {@link eLeapEventType#ConnectionLost} event.
	 */
	public LEAP_CONNECTION_LOST_EVENT getConnectionLostEvent()
	{
		checkType(eLeapEventType.ConnectionLost);
		return getOrCreateEvent(LEAP_CONNECTION_LOST_EVENT::new);
	}


	/**
	 * @return The event data as a device event.
	 * @throws IllegalStateException If this event message is not an
	 *           {@link eLeapEventType#Device} event.
	 */
	public LEAP_DEVICE_EVENT getDeviceEvent()
	{
		checkType(eLeapEventType.Device);
		return getOrCreateEvent(LEAP_DEVICE_EVENT::new);
	}


	/**
	 * @return The event data as a device event.
	 * @throws IllegalStateException If this event message is not an
	 *           {@link eLeapEventType#DeviceLost} event.
	 */
	public LEAP_DEVICE_EVENT getDeviceLostEvent()
	{
		checkType(eLeapEventType.DeviceLost);
		return getOrCreateEvent(LEAP_DEVICE_EVENT::new);
	}


	/**
	 * @return The event data as a device status change event.
	 * @throws IllegalStateException If this event message is not an
	 *           {@link eLeapEventType#DeviceStatusChange} event.
	 * @since Ultraleap Orion SDK 3.1.3
	 */
	public LEAP_DEVICE_STATUS_CHANGE_EVENT getDeviceStatusChangeEvent()
	{
		checkType(eLeapEventType.DeviceStatusChange);
		return getOrCreateEvent(LEAP_DEVICE_STATUS_CHANGE_EVENT::new);
	}


	/**
	 * @return The event data as a policy event.
	 * @throws IllegalStateException If this event message is not an
	 *           {@link eLeapEventType#Policy} event.
	 */
	public LEAP_POLICY_EVENT getPolicyEvent()
	{
		checkType(eLeapEventType.Policy);
		return getOrCreateEvent(LEAP_POLICY_EVENT::new);
	}


	/**
	 * @return The event data as a device failure event.
	 * @throws IllegalStateException If this event message is not an
	 *           {@link eLeapEventType#DeviceFailure} event.
	 */
	public LEAP_DEVICE_FAILURE_EVENT getDeviceFailureEvent()
	{
		checkType(eLeapEventType.DeviceFailure);
		return getOrCreateEvent(LEAP_DEVICE_FAILURE_EVENT::new);
	}


	/**
	 * @return The event data as a tracking event.
	 * @throws IllegalStateException If this event message is not an
	 *           {@link eLeapEventType#Tracking} event.
	 */
	public LEAP_TRACKING_EVENT getTrackingEvent()
	{
		checkType(eLeapEventType.Tracking);
		return getOrCreateEvent(LEAP_TRACKING_EVENT::new);
	}
	
	
	/**
	 * @return The event data as a tracking mode event.
	 * @throws IllegalStateException If this event message is not an
	 *           {@link eLeapEventType#TrackingMode} event.
	 * @since LeapJna 1.1.0
	 * @since Ultraleap Gemini SDK 5.0.0
	 */
	public LEAP_TRACKING_MODE_EVENT getTrackingModeEvent()
	{
		checkType(eLeapEventType.TrackingMode);
		return getOrCreateEvent(LEAP_TRACKING_MODE_EVENT::new);
	}


	/**
	 * @return The event data as a log event.
	 * @throws IllegalStateException If this event message is not an
	 *           {@link eLeapEventType#LogEvent} event.
	 */
	public LEAP_LOG_EVENT getLogEvent()
	{
		checkType(eLeapEventType.LogEvent);
		return getOrCreateEvent(LEAP_LOG_EVENT::new);
	}


	/**
	 * @return The event data as multiple log events.
	 * @throws IllegalStateException If this event message is not an
	 *           {@link eLeapEventType#LogEvents} event.
	 * @since Ultraleap Orion SDK 4.0.0
	 */
	public LEAP_LOG_EVENTS getLogEvents()
	{
		checkType(eLeapEventType.LogEvents);
		return getOrCreateEvent(LEAP_LOG_EVENTS::new);
	}


	/**
	 * @return The event data as a config response event.
	 * @throws IllegalStateException If this event message is not an
	 *           {@link eLeapEventType#ConfigResponse} event.
	 */
	public LEAP_CONFIG_RESPONSE_EVENT getConfigResponseEvent()
	{
		checkType(eLeapEventType.ConfigResponse);
		return getOrCreateEvent(LEAP_CONFIG_RESPONSE_EVENT::new);
	}


	/**
	 * @return The event data as a config change event.
	 * @throws IllegalStateException If this event message is not an
	 *           {@link eLeapEventType#ConfigChange} event.
	 */
	public LEAP_CONFIG_CHANGE_EVENT getConfigChangeEvent()
	{
		checkType(eLeapEventType.ConfigChange);
		return getOrCreateEvent(LEAP_CONFIG_CHANGE_EVENT::new);
	}


	/**
	 * @return The event data as a dropped frame event.
	 * @throws IllegalStateException If this event message is not an
	 *           {@link eLeapEventType#DroppedFrame} event;
	 */
	public LEAP_DROPPED_FRAME_EVENT getDroppedFrameEvent()
	{
		checkType(eLeapEventType.DroppedFrame);
		return getOrCreateEvent(LEAP_DROPPED_FRAME_EVENT::new);
	}


	/**
	 * @return The event data as a head pose event.
	 * @throws IllegalStateException If this event message is not an
	 *           {@link eLeapEventType#HeadPose} event;
	 * @since Ultraleap Orion SDK 4.1.0
	 */
	public LEAP_HEAD_POSE_EVENT getHeadPoseEvent()
	{
		checkType(eLeapEventType.HeadPose);
		return getOrCreateEvent(LEAP_HEAD_POSE_EVENT::new);
	}


	/**
	 * @return The event data as an eye event.
	 * @throws IllegalStateException If this event message is not an
	 *           {@link eLeapEventType#Eyes} event;
	 * @since LeapJna 1.2.0
	 */
	public LEAP_EYE_EVENT getEyeEvent()
	{
		checkType(eLeapEventType.Eyes);
		return getOrCreateEvent(LEAP_EYE_EVENT::new);
	}
	
	
	/**
	 * @return The event data as an IMU event.
	 * @throws IllegalStateException If this event message is not an
	 *           {@link eLeapEventType#IMU} event;
	 * @since LeapJna 1.2.0
	 */
	public LEAP_IMU_EVENT getIMUEvent()
	{
		checkType(eLeapEventType.IMU);
		return getOrCreateEvent(LEAP_IMU_EVENT::new);
	}


	/**
	 * @return The event data as a point mapping change event.
	 * @throws IllegalStateException If this event message is not an
	 *           {@link eLeapEventType#PointMappingChange} event;
	 * @since Ultraleap Orion SDK 4.0.0
	 */
	public LEAP_POINT_MAPPING_CHANGE_EVENT getPointMappingChangeEvent()
	{
		checkType(eLeapEventType.PointMappingChange);
		return getOrCreateEvent(LEAP_POINT_MAPPING_CHANGE_EVENT::new);
	}


	/**
	 * @return The event data as an image event.
	 * @throws IllegalStateException If this event message is not an
	 * 				   {@link eLeapEventType#Image} event;
	 * @since Ultraleap Orion SDK 4.0.0
	 */
	public LEAP_IMAGE_EVENT getImageEvent()
	{
		checkType(eLeapEventType.Image);
		return getOrCreateEvent(LEAP_IMAGE_EVENT::new);
	}


	private void checkType(eLeapEventType eventType)
	{
		if (type != eventType.value)
		{
			throw new IllegalStateException(
					"Incorrect event type: " + getType() + " != " + eventType);
		}
	}


	@SuppressWarnings("unchecked")
	private <T extends LEAP_EVENT> T getOrCreateEvent(Function<Pointer, T> createFunction)
	{
		if (event == null)
			event = createFunction.apply(pEvent);

		return (T) event;
	}
}