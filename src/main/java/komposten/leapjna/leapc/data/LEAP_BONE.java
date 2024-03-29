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


/**
 * <p>
 * Describes a bone's position and orientation.
 * </p>
 * <p>
 * Bones are members of the {@link LEAP_DIGIT} struct.
 * </p>
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv49LEAP_BONE">LeapC
 *      API - LEAP_BONE</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
@FieldOrder({ "prev_joint", "next_joint", "width", "rotation" })
public class LEAP_BONE extends Structure
{
	/** The base of the bone, closer to the heart. I.e. the bone's origin. */
	public LEAP_VECTOR prev_joint;

	/** The end of the bone, further from the heart. */
	public LEAP_VECTOR next_joint;

	/** The average width of the flesh around the bone in millimetres. */
	public float width;

	/**
	 * <p>
	 * Rotation in world space from the forward direction.
	 * </p>
	 * <p>
	 * Convert the quaternion to a matrix to derive the basis vectors.
	 * </p>
	 * 
	 * @since Ultraleap Orion SDK 3.1.2
	 */
	public LEAP_QUATERNION rotation;

	public LEAP_BONE()
	{
		super(ALIGN_NONE);
	}
}