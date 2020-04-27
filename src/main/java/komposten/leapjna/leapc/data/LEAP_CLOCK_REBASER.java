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
 * An opaque clock rebase state structure.
 * </p>
 * <p>
 * Use {@link #handle} when interacting with the Leap API methods.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___c_l_o_c_k___r_e_b_a_s_e_r">LeapC
 *      API - LEAP_CLOCK_REBASER</a>
 */
@FieldOrder({ "handle" })
public class LEAP_CLOCK_REBASER extends Structure
{
	/**
	 * A handle to a Leap clock rebaser object.
	 */
	public Pointer handle;
}