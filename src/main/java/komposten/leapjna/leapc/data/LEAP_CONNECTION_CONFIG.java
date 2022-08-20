/*
 * Copyright 2020-2022 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.data;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.leapc.enums.eLeapConnectionConfig;

/**
 * <p>
 * Specifies the configuration for a connection.
 * </p>
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv422LEAP_CONNECTION_CONFIG">LeapC
 *      API - LEAP_CONNECTION_CONFIG</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
@FieldOrder({ "size", "flags", "server_namespace" })
public class LEAP_CONNECTION_CONFIG extends Structure
{
	/** Set to the final size of this structure. */
	public int size;

	/** A combination of {@link eLeapConnectionConfig} flags. Set to 0 to indicate no special flags.*/
	public int flags;

	/** For internal use. */
	public String server_namespace;
}