/*
 * Copyright 2020 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.data;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.leapc.enums.Enums;
import komposten.leapjna.leapc.enums.eLeapDeviceStatus;
import komposten.leapjna.leapc.enums.eLeapRecordingFlags;


/**
 * <p>
 * Read/Write mode for opening a {@link LEAP_RECORDING}.
 * </p>
 * <p>
 * Use {@link #handle} when interacting with the Leap API methods.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___r_e_c_o_r_d_i_n_g___p_a_r_a_m_e_t_e_r_s">LeapC
 *      API - LEAP_RECORDING_PARAMETERS</a>
 */
@FieldOrder({ "mode" })
public class LEAP_RECORDING_PARAMETERS extends Structure
{
	/**
	 * <p>
	 * A combination of eLeapRecordingFlags indicating the desired operations.
	 * </p>
	 * <p>
	 * Use {@link #getMode()} to get the mode as an array of {@link eLeapRecordingFlags}
	 * values.
	 * </p>
	 */
	public int mode;

	public LEAP_RECORDING_PARAMETERS()
	{}


	public LEAP_RECORDING_PARAMETERS(eLeapRecordingFlags... flags)
	{
		mode = eLeapRecordingFlags.createMask(flags);
		write();
	}


	/**
	 * @return The mode flags as an {@link eLeapDeviceStatus} array instead of an
	 *         <code>int</code>.
	 */
	public eLeapRecordingFlags[] getMode()
	{
		return Enums.parseMask(mode, eLeapRecordingFlags.class);
	}


	public static class ByValue extends LEAP_RECORDING_PARAMETERS
			implements Structure.ByValue
	{
		public ByValue(eLeapRecordingFlags... flags)
		{
			super(flags);
		}
	}
}
