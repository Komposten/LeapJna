/*
 * Copyright 2022 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.data;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.leapc.events.LEAP_EVENT;


/**
 * <p>
 * Version information.
 * </p>
 * <p>
 * The members can be converted to a version string using the format:
 * 
 * <pre>
 * major.minor.patch.build
 * </pre>
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv412LEAP_VERSION">LeapC
 *      API - LEAP_VERSION</a>
 * @since LeapJna 1.2.0
 * @since Ultraleap Gemini SDK 5.2.0
 */
@FieldOrder({ "major", "minor", "patch" })
public class LEAP_VERSION extends Structure implements LEAP_EVENT
{
	/**
	 * The major version.
	 */
	public int major;
	/**
	 * The minor version.
	 */
	public int minor;

	/**
	 * The patch version.
	 */
	public int patch;

	public LEAP_VERSION()
	{
		super(ALIGN_NONE);
	}

	public LEAP_VERSION(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}
}
