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
 * @since LeapJna 1.2.0
 * @since Ultraleap Gemini SDK 5.6.0
 */
public enum eLeapConnectionConfig implements IntEnum
{
  /**
   * The client is aware of how to handle multiple devices through the API.
   */
	MultiDeviceAware(1);

	public final int value;

	private eLeapConnectionConfig(int value)
	{
		this.value = value;
	}


	@Override
	public int getValue()
	{
		return value;
	}
}