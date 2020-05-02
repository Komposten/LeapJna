/*
 * Copyright 2020 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.enums;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;

import komposten.leapjna.leapc.LeapC;
import komposten.leapjna.leapc.data.LEAP_CONNECTION_MESSAGE;
import komposten.leapjna.leapc.data.LEAP_VARIANT;
import komposten.leapjna.leapc.enums.Enums.IntEnum;


/**
 * The types of event messages resulting from calling
 * {@link LeapC#LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)}.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#ga04f93b375f7c8040178ec5be2bf658ec">LeapC
 *      API - eLeapEventType</a>
 */
public enum eLeapEventType implements IntEnum
{
	/**
	 * Indicates that an unknown event type, i.e. one which doesn't match any of the
	 * constants, was encountered.
	 */
	Unknown(-1),
	
	/**
	 * No event has occurred within the timeout period specified when calling
	 * {@link LeapC#LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)}.
	 */
	None(0x000),

	/** A connection to the Leap Motion service has been established. */
	Connection(0x001),

	/** The connection to the Leap Motion service has been lost. */
	ConnectionLost(0x002),

	/**
	 * <p>
	 * A device has been detected or plugged-in.
	 * </p>
	 * <p>
	 * A device event is dispatched after a connection is established for any devices
	 * already plugged in. (The system currently only supports one streaming device at a
	 * time.)
	 * </p>
	 */
	Device(0x003),

	/**
	 * <p>
	 * A device has failed.
	 * </p>
	 * <p>
	 * Device failure could be caused by hardware failure, USB controller issues, or other
	 * system instability. Note that unplugging a device generates an {@link #DeviceLost}
	 * event message, not a failure message.
	 * </p>
	 */
	DeviceFailure(0x004),

	/**
	 * <p>
	 * A policy change has occurred.
	 * </p>
	 * <p>
	 * This can be due to setting a policy with
	 * {@link LeapC#LeapSetPolicyFlags(Pointer, long, long)} or due to changing or
	 * policy-related config settings, including images_mode. (A user can also change these
	 * policies using the Leap Motion Control Panel.)
	 * </p>
	 */
	Policy(0x005),

	/** A tracking frame. The message contains the tracking data for the frame. */
	Tracking(0x100),

	/**
	 * <p>
	 * The request for an image has failed.
	 * </p>
	 * <p>
	 * The message contains information about the failure. The client application will not
	 * receive the requested image set.
	 * </p>
	 */
	ImageRequestError(0x101),

	/**
	 * <p>
	 * The request for an image is complete.
	 * </p>
	 * <p>
	 * The image data has been completely written to the application-provided buffer.
	 * </p>
	 */
	ImageComplete(0x102),

	/** A system message. */
	LogEvent(0x103),

	/**
	 * <p>
	 * The device connection has been lost.
	 * </p>
	 *
	 * <p>
	 * This event is generally asserted when the device has been detached from the system,
	 * when the connection to the service has been lost, or if the device is closed while
	 * streaming. Generally, any event where the system can conclude no further frames will
	 * be received will result in this message. The DeviceEvent field will be filled with
	 * the id of the formerly attached device.
	 * </p>
	 */
	DeviceLost(0x104),

	/**
	 * The asynchronous response to a call to
	 * {@link LeapC#LeapRequestConfigValue(Pointer, String, LongByReference)}. Contains the
	 * value of requested configuration item.
	 */
	ConfigResponse(0x105),

	/**
	 * The asynchronous response to a call to
	 * {@link LeapC#LeapSaveConfigValue(Pointer, String, LEAP_VARIANT, LongByReference)}.
	 * Reports whether the change succeeded or failed.
	 */
	ConfigChange(0x106),

	/** Notification that a status change has been detected on an attached device */
	DeviceStatusChange(0x107),

	/** */
	DroppedFrame(0x108),

	/** Notification that an unrequested stereo image pair is available */
	Image(0x109),

	/** Notification that point mapping has changed */
	PointMappingChange(0x10A),

	/** An array of system messages. */
	LogEvents(0x10B),

	/** */
	HeadPose(0x10C);

	public final int value;

	private eLeapEventType(int value)
	{
		this.value = value;
	}


	@Override
	public int getValue()
	{
		return value;
	}
}