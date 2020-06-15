/*
 * Copyright 2020 Jakob Hjelm (Komposten)
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

import komposten.leapjna.leapc.data.LEAP_FRAME_HEADER;
import komposten.leapjna.leapc.data.LEAP_HAND;


/**
 * A snapshot, or frame of data, containing the tracking data for a single moment in time.
 *
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___t_r_a_c_k_i_n_g___e_v_e_n_t">LeapC
 *      API - LEAP_TRACKING_EVENT</a>
 */
@FieldOrder({ "info", "tracking_frame_id", "nHands", "pHands", "framerate" })
public class LEAP_TRACKING_EVENT extends Structure implements LEAP_EVENT
{
	/** A universal frame identification header. */
	public LEAP_FRAME_HEADER info;

	/**
	 * <p>
	 * An identifier for this tracking frame.
	 * </p>
	 * <p>
	 * This identifier is meant to be monotonically increasing, but values may be skipped if
	 * the client application does not poll for messages fast enough. This number also
	 * generally increases at the same rate as <code>info.frame_id</code>, but if the server
	 * cannot process every image received from the device cameras, the
	 * <code>info.frame_id</code> identifier may increase faster.
	 * </p>
	 */
	public long tracking_frame_id;

	/**
	 * The number of hands tracked in this frame. I.e. the number of elements in the
	 * <code>pHands</code> array.
	 */
	public int nHands;

	/**
	 * A pointer to the array of hands tracked in this frame. Use {@link #getHands()} to
	 * obtain the array itself.
	 */
	public Pointer pHands;

	/**
	 * <p>
	 * Current tracking frame rate in hertz.
	 * </p>
	 * <p>
	 * This frame rate is distinct from the image frame rate, which is the rate that images
	 * are being read from the device. Depending on host CPU limitations, the tracking frame
	 * rate may be substantially less than the device frame rate.
	 * </p>
	 * <p>
	 * This number is generally equal to or less than the device frame rate, but there is
	 * one case where this number may be <em>higher</em> than the device frame rate: When
	 * the device rate drops. In this case, the device frame rate will fall sooner than the
	 * tracking frame rate.
	 * </p>
	 * <p>
	 * This number is equal to zero if there are not enough frames to estimate frame rate.
	 * </p>
	 * <p>
	 * This number cannot be negative.
	 * </p>
	 */
	public float framerate;

	private LEAP_HAND[] hands;

	public LEAP_TRACKING_EVENT()
	{
		super(ALIGN_NONE);
	}


	public LEAP_TRACKING_EVENT(int size)
	{
		super(ALIGN_NONE);
		allocateMemory(size);
	}


	public LEAP_TRACKING_EVENT(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}


	/**
	 * @return An array of {@link LEAP_HAND} structs holding the hand data for this frame.
	 */
	public LEAP_HAND[] getHands()
	{
		return hands;
	}


	@Override
	public void read()
	{
		super.read();

		hands = new LEAP_HAND[nHands];
		
		if (nHands > 0)
		{
			hands[0] = new LEAP_HAND(pHands);
			int size = hands[0].size();
			for (int i = 1; i < nHands; i++)
			{
				int offset = i * size;
				hands[i] = new LEAP_HAND(pHands.share(offset));
			}
		}
	}
}