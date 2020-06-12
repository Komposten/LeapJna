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
import komposten.leapjna.leapc.data.LEAP_CONNECTION_MESSAGE;
import komposten.leapjna.leapc.enums.Enums;
import komposten.leapjna.leapc.enums.eLeapServiceDisposition;


/**
 * Received from {@link LeapC#LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)}
 * when a connection to the Leap Motion service is established.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___c_o_n_n_e_c_t_i_o_n___e_v_e_n_t">LeapC
 *      API - LEAP_CONNECTION_EVENT</a>
 */
@FieldOrder({ "flags" })
public class LEAP_CONNECTION_EVENT extends Structure implements LEAP_EVENT
{
	/**
	 * A combination of {@link eLeapServiceDisposition} flags. Use {@link #getFlags()} to
	 * get the type as a {@link eLeapServiceDisposition} value.
	 */
	public int flags;

	public LEAP_CONNECTION_EVENT(Pointer pointer)
	{
		super(pointer);
		read();
	}


	/**
	 * @return The flags as an {@link eLeapServiceDisposition} instead of an
	 *         <code>int</code>.
	 */
	public eLeapServiceDisposition getFlags()
	{
		return Enums.parse(flags, eLeapServiceDisposition.Unknown);
	}
}
