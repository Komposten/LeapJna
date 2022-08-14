/*
 * Copyright 2022 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.events;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.leapc.data.LEAP_VECTOR;


@FieldOrder({ "frame_id", "timestamp", "left_eye_position", "right_eye_position",
		"left_eye_estimated_error", "right_eye_estimated_error" })
public class LEAP_EYE_EVENT extends Structure implements LEAP_EVENT
{
	/**
	 * The ID of the frame corresponding to the source of the currently tracked eye
	 * positions.
	 */
	public long frame_id;

	/**
	 * The timestamp for this image, in microseconds, referenced against LeapGetNow().
	 */
	public long timestamp;

	/**
	 * The position of the user's left eye.
	 */
	public LEAP_VECTOR left_eye_position;

	/**
	 * The position of the user's right eye.
	 */
	public LEAP_VECTOR right_eye_position;

	/**
	 * An error estimate of the tracked left eye position. Higher values indicate uncertain
	 * tracking and a higher likelihood of there being no such eye in view of the sensor.
	 */
	public float left_eye_estimated_error;

	/**
	 * An error estimate of the tracked right eye position. Higher values indicate uncertain
	 * tracking and a higher likelihood of there being no such eye in view of the sensor.
	 */
	public float right_eye_estimated_error;

	public LEAP_EYE_EVENT(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}
}
