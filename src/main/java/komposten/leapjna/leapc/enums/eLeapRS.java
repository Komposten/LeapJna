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
 * Codes returned by all LeapC functions.
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___enum.html#_CPPv47eLeapRS">LeapC
 *      API - eLeapRS</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
public enum eLeapRS implements IntEnum
{
	/**
	 * Indicates that an unknown result code, i.e. one which doesn't match any of the
	 * constants, was encountered.
	 */
	Unknown(-1),
	Success(0),
	UnknownError(0xE2010000),
	InvalidArgument(0xE2010001),
	InsufficientResources(0xE2010002),
	InsufficientBuffer(0xE2010003),
	Timeout(0xE2010004),
	NotConnected(0xE2010005),
	HandshakeIncomplete(0xE2010006),
	BufferSizeOverflow(0xE2010007),
	ProtocolError(0xE2010008),
	InvalidClientID(0xE2010009),
	UnexpectedClosed(0xE201000A),
	UnknownImageFrameRequest(0xE201000B),
	UnknownTrackingFrameID(0xE201000C),
	/** @since Ultraleap Orion SDK 3.1.2 */
	RoutineIsNotSeer(0xE201000D),
	/** @since Ultraleap Orion SDK 3.1.2 */
	TimestampTooEarly(0xE201000E),
	/** @since Ultraleap Orion SDK 3.1.2 */
	ConcurrentPoll(0xE201000F),
	NotAvailable(0xE7010002),
	NotStreaming(0xE7010004),
	CannotOpenDevice(0xE7010005),
	Unsupported(0xE7010006);

	private final int value;

	private eLeapRS(int value)
	{
		this.value = value;
	}


	@Override
	public int getValue()
	{
		return value;
	}
}