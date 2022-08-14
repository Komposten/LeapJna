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


/**
 * @since 1.2.0 (Gemini 5.6.0)
 */
public enum eLeapVersionPart implements IntEnum
{
  /** The parameter for requesting the version of the client. */
	ClientLibrary(0),
	
	/** The parameter for requesting the protocol version of the client. */
	ClientProtocol(1),

	/** The parameter for requesting the version of the server. */
	ServerLibrary(2),

	/** The parameter for requesting the protocol version of the server. */
	ServerProtocol(3);

	public final int value;

	private eLeapVersionPart(int value)
	{
		this.value = value;
	}


	@Override
	public int getValue()
	{
		return value;
	}
}