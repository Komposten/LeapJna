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

import komposten.leapjna.leapc.LeapC;
import komposten.leapjna.leapc.data.LEAP_RECORDING;
import komposten.leapjna.leapc.data.LEAP_RECORDING_PARAMETERS;
import komposten.leapjna.leapc.data.LEAP_RECORDING_STATUS;
import komposten.leapjna.leapc.enums.Enums.IntFlagEnum;


/**
 * <p>
 * Defines the recording mode provided to the
 * {@link LeapC#LeapRecordingOpen(LEAP_RECORDING, String, LEAP_RECORDING_PARAMETERS)}
 * function.
 * </p>
 * <p>
 * Also used in members of {@link LEAP_RECORDING_PARAMETERS} and
 * {@link LEAP_RECORDING_STATUS}.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#ga7f321cfd29f8d1b74589cde5dfb3a1ed">LeapC
 *      API - eLeapRecordingFlags</a>
 */
public enum eLeapRecordingFlags implements IntFlagEnum<eLeapRecordingFlags>
{
	Error(0x00000000), Reading(0x00000001), Writing(0x00000002), Flushing(
			0x00000004), Compressed(0x00000008);

	public final int value;

	private eLeapRecordingFlags(int value)
	{
		this.value = value;
	}


	@Override
	public int getValue()
	{
		return value;
	}


	@Override
	public eLeapRecordingFlags getEmptyMaskConstant()
	{
		return Error;
	}


	public static int createMask(eLeapRecordingFlags... flags)
	{
		return Enums.createMask(flags);
	}
}