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

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;


/**
 * <p>
 * Describes a digit of a hand.
 * </p>
 * <p>
 * Digits are members of the {@link LEAP_HAND} struct.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___d_i_g_i_t">LeapC
 *      API - LEAP_DIGIT</a>
 */
@FieldOrder({ "finger_id", "metacarpal", "proximal", "intermediate", "distal",
		"is_extended" })
public class LEAP_DIGIT extends Structure
{
	/** The Leap identifier of this finger. */
	public int finger_id;

	/**
	 * <p>
	 * The finger bone wholly inside the hand.
	 * </p>
	 * <p>
	 * For thumbs, this bone is set to have zero length and width, an identity basis matrix,
	 * and its joint positions are equal. Note that this is anatomically incorrect; in
	 * anatomical terms, the intermediate phalange is absent in a real thumb, rather than
	 * the metacarpal bone. In the Leap Motion model, however, we use a "zero" metacarpal
	 * bone instead for ease of programming.
	 * </p>
	 */
	public LEAP_BONE metacarpal;

	/** The phalange extending from the knuckle. */
	public LEAP_BONE proximal;

	/** The bone between the proximal phalange and the distal phalange. */
	public LEAP_BONE intermediate;

	/** The distal phalange terminating at the finger tip. */
	public LEAP_BONE distal;

	/** Reports whether the finger is more or less straight. */
	public int is_extended;
	
	public LEAP_DIGIT()
	{
		super(ALIGN_NONE);
	}

	
	/**
	 * Bundles the bones in an array to facilitate iteration over them.
	 * 
	 * @return The digit's bones as an array in the order: metacarpal, proximal,
	 *         intermediate, distal.
	 */
	public LEAP_BONE[] boneArray()
	{
		return new LEAP_BONE[] { metacarpal, proximal, intermediate, distal };
	}
}