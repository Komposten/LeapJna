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
 * Properties associated with the palm of the hand.
 * </p>
 * <p>
 * The Palm is a member of the {@link LEAP_HAND} struct.
 * </p>
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv49LEAP_PALM">LeapC
 *      API - LEAP_PALM</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
@FieldOrder({ "position", "stabilized_position", "velocity", "normal", "width",
		"direction", "orientation" })
public class LEAP_PALM extends Structure
{
	/** The centre position of the palm in millimetres from the Ultraleap Tracking camera device origin. */
	public LEAP_VECTOR position;

	/**
	 * <p>
	 * The time-filtered and stabilised position of the palm.
	 * </p>
	 * <p>
	 * Smoothing and stabilisation is performed in order to make this value more suitable
	 * for interaction with 2D content. The stabilised position lags behind the palm
	 * position by a variable amount, depending primarily on the speed of movement.
	 * </p>
	 */
	public LEAP_VECTOR stabilized_position;

	/** The rate of change of the palm position in millimetres per second. */
	public LEAP_VECTOR velocity;

	/**
	 * <p>
	 * The normal vector of the palm.
	 * </p>
	 * <p>
	 * If your hand is flat, this vector will point downward, or "out" of the front surface
	 * of your palm.
	 * </p>
	 */
	public LEAP_VECTOR normal;

	/** The estimated width of the palm when the hand is in a flat position. */
	public float width;

	/** The unit direction vector pointing from the palm position towards the fingers. */
	public LEAP_VECTOR direction;

	/**
	 * The quaternion representing the palm's orientation corresponding to the basis
	 * <code>{normal x direction, -normal, -direction}</code>.
	 * 
	 * @since Ultraleap Orion SDK 3.1.3
	 */
	public LEAP_QUATERNION orientation;
	
	public LEAP_PALM()
	{
		super(ALIGN_NONE);
	}
}