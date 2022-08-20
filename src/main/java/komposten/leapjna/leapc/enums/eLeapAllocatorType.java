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
 * Defines the various types of data that may be allocated using the allocator.
 *
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___enum.html#_CPPv418eLeapAllocatorType">LeapC
 *      API - eLeapAllocatorType</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 4.0.0
 */
public enum eLeapAllocatorType implements IntEnum
{
	Unknown(-1),

	/** Signed 8-bit integer (char) */
	Int8(0),

	/** Unsigned 8-bit integer (byte) */
	Uint8(1),

	/** Signed 16-bit integer */
	Int16(2),

	/** Unsigned 16-bit integer */
	UInt16(3),

	/** Signed 32-bit integer */
	Int32(4),

	/** Unsigned 32-bit integer */
	UInt32(5),

	/** Single-precision 32-bit floating-point */
	Float(6),

	/** Signed 64-bit integer */
	Int64(8),

	/** Unsigned 64-bit integer */
	UInt64(9),

	/** Double-precision 64-bit floating-point */
	Double(10);

	public final int value;

	private eLeapAllocatorType(int value)
	{
		this.value = value;
	}


	@Override
	public int getValue()
	{
		return value;
	}
}
