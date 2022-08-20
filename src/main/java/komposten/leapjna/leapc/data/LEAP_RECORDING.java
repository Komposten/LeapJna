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

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;


/**
 * <p>
 * A representation of a Leap recording, used to interact with the Leap API.
 * </p>
 * <p>
 * Use {@link #handle} when interacting with the Leap API methods.
 * </p>
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv414LEAP_RECORDING">LeapC
 *      API - LEAP_RECORDING</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.2.0
 */
@FieldOrder({ "handle" })
public class LEAP_RECORDING extends Structure
{
	/**
	 * A handle to the Leap recording object.
	 */
	public Pointer handle;
}
