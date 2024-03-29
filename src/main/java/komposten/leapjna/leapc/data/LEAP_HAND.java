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

import komposten.leapjna.leapc.enums.Enums;
import komposten.leapjna.leapc.enums.eLeapHandType;


/**
 * <p>
 * Describes a tracked hand.
 * </p>
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv49LEAP_HAND">LeapC
 *      API - LEAP_HAND</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
@FieldOrder({ "id", "flags", "type", "confidence", "visible_time", "pinch_distance",
		"grab_angle", "pinch_strength", "grab_strength", "palm", "digits", "arm" })
public class LEAP_HAND extends Structure
{
	/**
	 * A struct to store the digits of a hand.
	 */
	@FieldOrder({ "thumb", "index", "middle", "ring", "pinky" })
	public static class DigitStruct extends Structure
	{
		public LEAP_DIGIT thumb;
		public LEAP_DIGIT index;
		public LEAP_DIGIT middle;
		public LEAP_DIGIT ring;
		public LEAP_DIGIT pinky;
		
		public DigitStruct()
		{
			super(ALIGN_NONE);
		}

		/**
		 * Bundles the digits in an array to facilitate iteration over them.
		 * 
		 * @return All digits as an array in the order: thumb, index, middle, ring, pinky.
		 */
		public LEAP_DIGIT[] asArray()
		{
			return new LEAP_DIGIT[] { thumb, index, middle, ring, pinky };
		}
	}


	/**
	 * <p>
	 * A unique ID for a hand tracked across frames.
	 * </p>
	 * <p>
	 * If tracking of a physical hand is lost, a new ID is assigned when tracking is
	 * reacquired.
	 * </p>
	 */
	public int id;

	/** Reserved for future use. */
	public int flags;

	/**
	 * The chirality of this hand. Either 0 (left) or 1 (right). Use {@link #getType()} to
	 * get the type as a {@link eLeapHandType} value.
	 */
	public int type;

	/**
	 * <p>
	 * How confident we are with a given hand pose.
	 * </p>
	 * <p>
	 * Not currently used (always 1.0).
	 * </p>
	 */
	public float confidence;

	/** The total amount of time this hand has been tracked, in microseconds. */
	public long visible_time;

	/** The distance between index finger and thumb. */
	public float pinch_distance;

	/** The average angle of fingers to palm. */
	public float grab_angle;

	/**
	 * <p>
	 * The normalized estimate of the pinch pose.
	 * </p>
	 * <p>
	 * Zero is not pinching; one is fully pinched.
	 * </p>
	 */
	public float pinch_strength;

	/**
	 * <p>
	 * The normalized estimate of the grab hand pose.
	 * </p>
	 * <p>
	 * Zero is not grabbing; one is fully grabbing.
	 * </p>
	 */
	public float grab_strength;

	/** Additional information associated with the palm. */
	public LEAP_PALM palm;


	/** The fingers of this hand. */
	public LEAP_HAND.DigitStruct digits;

	/**
	 * <p>
	 * The arm to which this hand is attached.
	 * </p>
	 * <p>
	 * An arm consists of a single LEAP_BONE struct.
	 * </p>
	 */
	public LEAP_BONE arm;


	public LEAP_HAND()
	{
		super(ALIGN_NONE);
		read();
	}


	public LEAP_HAND(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}


	/**
	 * @return The hand type as an {@link eLeapHandType} instead of a <code>byte</code>.
	 */
	public eLeapHandType getType()
	{
		return Enums.parse(type, eLeapHandType.Unknown);
	}
}