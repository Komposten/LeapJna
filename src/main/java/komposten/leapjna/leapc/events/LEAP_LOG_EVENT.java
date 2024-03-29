/*
 * Copyright 2020-2022 Jakob Hjelm (Komposten)
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

import komposten.leapjna.leapc.LeapC;
import komposten.leapjna.leapc.enums.Enums;
import komposten.leapjna.leapc.enums.eLeapLogSeverity;


/**
 * A system log message.
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv414LEAP_LOG_EVENT">LeapC
 *      API - LEAP_LOG_EVENT</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
@FieldOrder({ "severity", "timestamp", "message" })
public class LEAP_LOG_EVENT extends Structure implements LEAP_EVENT
{
	/**
	 * The type of message. Use {@link #getSeverity()} to get the severity as an
	 * {@link eLeapLogSeverity} value.
	 * 
	 * @since Ultraleap Orion SDK 4.0.0
	 */
	public int severity;

	/**
	 * <p>
	 * The timestamp of the message in microseconds.
	 * </p>
	 * <p>
	 * Compare with the current value of {@link LeapC#LeapGetNow()} and the system clock to
	 * calculate the absolute time of the message.
	 * </p>
	 * 
	 * @since Ultraleap Orion SDK 4.0.0
	 */
	public long timestamp;

	/**
	 * The log message.
	 * 
	 * @since Ultraleap Orion SDK 4.0.0
	 */
	public String message;

	public LEAP_LOG_EVENT(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}


	/**
	 * @return The severity as an {@link eLeapLogSeverity} instead of an <code>int</code>.
	 */
	public eLeapLogSeverity getSeverity()
	{
		return Enums.parse(severity, eLeapLogSeverity.Unknown);
	}
}
