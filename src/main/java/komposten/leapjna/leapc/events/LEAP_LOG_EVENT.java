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

import komposten.leapjna.leapc.LeapC;
import komposten.leapjna.leapc.enums.Enums;
import komposten.leapjna.leapc.enums.eLeapLogSeverity;


/**
 * A system log message.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___l_o_g___e_v_e_n_t">LeapC
 *      API - LEAP_LOG_EVENT</a>
 */
@FieldOrder({ "severity", "timestamp", "message" })
public class LEAP_LOG_EVENT extends Structure implements LEAP_EVENT
{
	/**
	 * The type of message. Use {@link #getSeverity()} to get the severity as an
	 * {@link eLeapLogSeverity} value.
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
	 */
	public long timestamp;

	/**
	 * The log message.
	 */
	public String message;

	private eLeapLogSeverity severityE;

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
		if (severityE == null)
		{
			severityE = Enums.parse(severity, eLeapLogSeverity.Unknown);
		}
		return severityE;
	}
}
