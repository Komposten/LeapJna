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
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___enum.html#_CPPv416eLeapVersionPart">LeapC
 *      API - eLeapVersionPart</a>
 * @since LeapJna 1.2.0
 * @since Ultraleap Gemini SDK 5.2.0
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