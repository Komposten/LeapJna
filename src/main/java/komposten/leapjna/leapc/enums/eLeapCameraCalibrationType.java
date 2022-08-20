/*
 * Copyright 2022 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.enums;

import komposten.leapjna.leapc.enums.Enums.IntEnum;


/**
 * Camera calibration types.
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___enum.html#_CPPv426eLeapCameraCalibrationType">LeapC
 *      API - eLeapCameraCalibrationType</a>
 * @since LeapJna 1.2.0
 * @since Ultraleap Gemini SDK 5.6.0
 */
public enum eLeapCameraCalibrationType implements IntEnum
{
  /** Infrared calibration (default). */
	Infrared(0),
	
	/** Visual calibration. */
	Visual(1);

	public final int value;

	private eLeapCameraCalibrationType(int value)
	{
		this.value = value;
	}


	@Override
	public int getValue()
	{
		return value;
	}
}