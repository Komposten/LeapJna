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

import komposten.leapjna.leapc.enums.Enums.IntFlagEnum;

/**
 * The service policy flags.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#gaf9b8fb0f14bd75188519ab4eaedd6a47">LeapC
 *      API - eLeapDeviceStatus</a>
 */
public enum eLeapPolicyFlag implements IntFlagEnum<eLeapPolicyFlag>
{
	/** No active policy flags. */
	None(0x00000000),

	/** The policy allowing an application to receive frames in the background. */
	BackgroundFrames(0x00000001),

	/** The policy specifying whether to automatically stream images from the device. */
	Images(0x00000002),

	/** The policy specifying whether to optimise tracking for head-mounted device. */
	OptimiseHMD(0x00000004),

	/** The policy allowing an application to pause or resume service tracking. */
	AllowPauseResume(0x00000008),

	/** The policy allowing an application to receive per-frame map points. */
	MapPoints(0x00000080);

	public final int value;

	private eLeapPolicyFlag(int value)
	{
		this.value = value;
	}
	
	
	@Override
	public int getValue()
	{
		return value;
	}
	
	
	@Override
	public eLeapPolicyFlag getEmptyMaskConstant()
	{
		return None;
	}


	public static int createMask(eLeapPolicyFlag... flags)
	{
		return Enums.createMask(flags);
	}
}
