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

import komposten.leapjna.leapc.enums.Enums.IntEnum;


/**
 * Camera perspective types.
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___enum.html#_CPPv420eLeapPerspectiveType">LeapC
 *      API - eLeapPerspectiveType</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
public enum eLeapPerspectiveType implements IntEnum
{
	/** An unknown or invalid type. */
	Invalid(0),

	/** A canonically left image. */
	Stereo_left(1),

	/** A canonically right image. */
	Stereo_right(2),

	/** Reserved for future use. */
	Mono(3);

	public final int value;

	private eLeapPerspectiveType(int value)
	{
		this.value = value;
	}


	@Override
	public int getValue()
	{
		return value;
	}
}