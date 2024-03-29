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

import komposten.leapjna.leapc.LeapC;


/**
 * <p>
 * Identifying information for a frame of tracking data.
 * </p>
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv417LEAP_FRAME_HEADER">LeapC
 *      API - LEAP_FRAME_HEADER</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
@FieldOrder({ "reserved", "frame_id", "timestamp" })
public class LEAP_FRAME_HEADER extends Structure
{
	/** Reserved, set to zero. */
	public Pointer reserved;

	/**
	 * <p>
	 * A unique identifier for this frame.
	 * </p>
	 * <p>
	 * All frames carrying this frame ID are part of the same unit of processing. This
	 * counter is generally an increasing counter, but <em>may reset to another value</em>
	 * if the user stops and restarts streaming.
	 * </p>
	 * <p>
	 * For interpolated frames, this value corresponds to the identifier of the frame upper
	 * bound.
	 * </p>
	 */
	public long frame_id;

	/**
	 * The timestamp for this frame, in microseconds, referenced against
	 * {@link LeapC#LeapGetNow()}.
	 */
	public long timestamp;
}