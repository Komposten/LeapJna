/*
 * Copyright 2020-2022 Jakob Hjelm (Komposten)
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
 *      "https://docs.ultraleap.com/tracking-api/group/group___enum.html#_CPPv415eLeapPolicyFlag">LeapC
 *      API - eLeapPolicyFlag</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
public enum eLeapPolicyFlag implements IntFlagEnum<eLeapPolicyFlag>
{
	/** No active policy flags. */
	None(0x00000000),

	/** The policy allowing an application to receive frames in the background. */
	BackgroundFrames(0x00000001),

	/**
	 * The policy specifying whether to automatically stream images from the device.
	 * 
	 * @since Ultraleap Gemini SDK 4.0.0
	 */
	Images(0x00000002),

	/** The policy specifying whether to optimise tracking for head-mounted device. */
	OptimiseHMD(0x00000004),

	/** The policy allowing an application to pause or resume service tracking. */
	AllowPauseResume(0x00000008),

	/**
	 * The policy allowing an application to receive per-frame map points.
	 * 
	 * @since Ultraleap Orion SDK 4.0.0
	 */
	MapPoints(0x00000080),

	/**
	 * The policy specifying whether to optimize tracking for screen-top device.
	 * 
	 * @since LeapJna 1.1.0
	 * @since Ultraleap Gemini SDK 5.0.0
	 */
	OptimizeScreenTop(0x00000100);

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
