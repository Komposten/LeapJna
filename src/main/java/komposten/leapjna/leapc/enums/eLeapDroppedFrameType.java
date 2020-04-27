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

import komposten.leapjna.leapc.enums.Enums.IntEnum;


public enum eLeapDroppedFrameType implements IntEnum
{
	Unknown(-1), PreprocessingQueue(0), TrackingQueue(1), Other(2);

	public final int value;

	private eLeapDroppedFrameType(int value)
	{
		this.value = value;
	}


	@Override
	public int getValue()
	{
		return value;
	}
}
