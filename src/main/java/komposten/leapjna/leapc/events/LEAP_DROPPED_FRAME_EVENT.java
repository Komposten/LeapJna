/*
 * Copyright 2020 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.events;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.leapc.enums.Enums;
import komposten.leapjna.leapc.enums.eLeapDroppedFrameType;
import komposten.leapjna.leapc.enums.eLeapLogSeverity;


/**
 * A dropped frame event.
 */
@FieldOrder({ "frame_id", "type" })
public class LEAP_DROPPED_FRAME_EVENT extends Structure implements LEAP_EVENT
{
	/**
	 * The type of message. Use {@link #getSeverity()} to get the severity as an
	 * {@link eLeapLogSeverity} value.
	 */
	public long frame_id;

	/**
	 * <p>
	 * The dropped frame type. Use {@link #getType()} to get the type as an
	 * {@link eLeapDroppedFrameType} value.
	 * </p>
	 */
	public int type;

	public LEAP_DROPPED_FRAME_EVENT(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}


	/**
	 * @return The type as an {@link eLeapDroppedFrameType} instead of an <code>int</code>.
	 */
	public eLeapDroppedFrameType getType()
	{
		return Enums.parse(type, eLeapDroppedFrameType.Unknown);
	}
}
