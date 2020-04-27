/*
 * Copyright 2020 Jakob Hjelm (Komposten)
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
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#ga871aa19c42787e8ad38c03b8f03f6bbf">LeapC
 *      API - LEAP_RECORDING</a>
 */
@FieldOrder({ "handle" })
public class LEAP_RECORDING extends Structure
{
	/**
	 * A handle to the Leap recording object.
	 */
	public Pointer handle;
}
