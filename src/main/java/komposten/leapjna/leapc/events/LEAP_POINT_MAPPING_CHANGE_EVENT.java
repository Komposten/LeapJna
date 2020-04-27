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


/**
 * A notification that a device's point mapping has changed.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___p_o_i_n_t___m_a_p_p_i_n_g___c_h_a_n_g_e___e_v_e_n_t">LeapC
 *      API - LEAP_POINT_MAPPING_CHANGE_EVENT</a>
 */
@FieldOrder({ "frame_id", "timestamp", "nPoints" })
public class LEAP_POINT_MAPPING_CHANGE_EVENT extends Structure implements LEAP_EVENT
{
	/**
	 * The ID of the frame corresponding to the source of the currently tracked points.
	 */
	public long frame_id;

	/**
	 * The timestamp of the frame, in microseconds, referenced against
	 * {@link LeapC#LeapGetNow()}.
	 */
	public long timestamp;

	/**
	 * The number of points being tracked.
	 */
	public int nPoints;

	public LEAP_POINT_MAPPING_CHANGE_EVENT(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}
}
