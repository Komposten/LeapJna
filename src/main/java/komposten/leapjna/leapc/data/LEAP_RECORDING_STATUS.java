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

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.leapc.LeapC;
import komposten.leapjna.leapc.enums.Enums;
import komposten.leapjna.leapc.enums.eLeapDeviceStatus;
import komposten.leapjna.leapc.enums.eLeapRecordingFlags;


/**
 * <p>
 * Information about a current {@link LEAP_RECORDING}.
 * </p>
 * <p>
 * Filled in by a call to
 * {@link LeapC#LeapRecordingGetStatus(Pointer, LEAP_RECORDING_STATUS)}.
 * </p>
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv421LEAP_RECORDING_STATUS">LeapC
 *      API - LEAP_RECORDING_STATUS</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.2.0
 */
@FieldOrder({ "mode" })
public class LEAP_RECORDING_STATUS extends Structure
{
	/**
	 * <p>
	 * Some combination of eLeapRecordingFlags indicating the status of the recording.
	 * </p>
	 * <p>
	 * Use {@link #getMode()} to get the mode as an array of {@link eLeapRecordingFlags}
	 * values.
	 * </p>
	 */
	public int mode;

	/**
	 * @return The mode flags as an {@link eLeapDeviceStatus} array instead of an
	 *         <code>int</code>.
	 */
	public eLeapRecordingFlags[] getMode()
	{
		return Enums.parseMask(mode, eLeapRecordingFlags.class);
	}
}
