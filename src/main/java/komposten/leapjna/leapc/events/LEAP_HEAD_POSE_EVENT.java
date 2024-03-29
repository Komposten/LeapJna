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
import komposten.leapjna.leapc.data.LEAP_QUATERNION;
import komposten.leapjna.leapc.data.LEAP_VECTOR;


/**
 * A snapshot, or frame of data, containing the head pose data for a single moment in
 * time.
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/struct/struct_l_e_a_p___h_e_a_d___p_o_s_e___e_v_e_n_t.html#_CPPv420LEAP_HEAD_POSE_EVENT">LeapC
 *      API - LEAP_HEAD_POSE_EVENT</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 4.1.0
 */
@FieldOrder({ "timestamp", "head_position", "head_orientation", "head_linear_velocity",
		"head_angular_velocity" })
public class LEAP_HEAD_POSE_EVENT extends Structure implements LEAP_EVENT
{
	/**
	 * The timestamp for this head pose, in microseconds, referenced against
	 * {@link LeapC#LeapGetNow()}.
	 */
	public long timestamp;

	/**
	 * The position of the user's head. Positional tracking must be enabled.
	 */
	public LEAP_VECTOR head_position;

	/**
	 * The orientation of the user's head. Positional tracking must be enabled.
	 */
	public LEAP_QUATERNION head_orientation;

	/**
	 * The linear velocity of the user's head. Positional tracking must be enabled.
	 */
	public LEAP_VECTOR head_linear_velocity;

	/**
	 * The angular velocity of the user's head. Positional tracking must be enabled.
	 */
	public LEAP_VECTOR head_angular_velocity;

	public LEAP_HEAD_POSE_EVENT()
	{
		super(ALIGN_NONE);
	}


	public LEAP_HEAD_POSE_EVENT(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}
}
