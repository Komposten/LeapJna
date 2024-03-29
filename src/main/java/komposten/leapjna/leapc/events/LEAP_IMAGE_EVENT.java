/*
 * Copyright 2020-2022 Jakob Hjelm (Komposten)
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

import komposten.leapjna.leapc.LeapC;
import komposten.leapjna.leapc.data.LEAP_ALLOCATOR;
import komposten.leapjna.leapc.data.LEAP_CALIBRATION;
import komposten.leapjna.leapc.data.LEAP_CONNECTION_MESSAGE;
import komposten.leapjna.leapc.data.LEAP_FRAME_HEADER;
import komposten.leapjna.leapc.data.LEAP_IMAGE;


/**
 * <p>
 * Streaming stereo image pairs from the device.
 * </p>
 * <p>
 * {@link LeapC#LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)} produces this
 * message when an image is available. The struct contains image properties, the
 * distortion grid, and a pointer to the buffer containing the image data -- which was
 * allocated using the allocator function passed to LeapC using the
 * {@link LeapC#LeapSetAllocator(Pointer, LEAP_ALLOCATOR)}.
 * </p>
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv416LEAP_IMAGE_EVENT">LeapC
 *      API - LEAP_IMAGE_EVENT</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 4.0.0
 */
@FieldOrder({ "info", "image", "calib" })
public class LEAP_IMAGE_EVENT extends Structure implements LEAP_EVENT
{
	/** The information header identifying the images tracking frame. */
	public LEAP_FRAME_HEADER info;

	/** The left and right images. */
	public LEAP_IMAGE[] image;

	/** For internal use only. */
	public LEAP_CALIBRATION calib;

	public LEAP_IMAGE_EVENT(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		image = new LEAP_IMAGE[2];
		calculateSize(true);
		read();
	}
}
