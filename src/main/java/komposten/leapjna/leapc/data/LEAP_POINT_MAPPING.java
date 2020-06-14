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

import komposten.leapjna.leapc.LeapC;


/**
 * <p>
 * A notification that a device's point mapping has changed.
 * </p>
 * <p>
 * It contains the entire set of points being mapped.
 * </p>
 * TODO Test this mapping!
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___p_o_i_n_t___m_a_p_p_i_n_g">LeapC
 *      API - LEAP_POINT_MAPPING</a>
 */
@FieldOrder({ "frame_id", "timestamp", "nPoints", "pPoints", "pIDs" })
public class LEAP_POINT_MAPPING extends Structure
{
	/** The ID of the frame corresponding to the source of the currently tracked points. */
	public long frame_id;

	/**
	 * The timestamp of the frame, in microseconds, referenced against
	 * {@link LeapC#LeapGetNow()}.
	 */
	public long timestamp;

	/** The number of points being tracked. */
	public int nPoints;

	/**
	 * A pointer to the array of 3D points being mapped. Use {@link #getPoints()} to obtain
	 * the array itself.
	 */
	public Pointer pPoints;

	/**
	 * A pointer an array with the IDs of the points being mapped. Use {@link #getIds()} to
	 * obtain the array itself.
	 */
	public Pointer pIDs;

	private LEAP_VECTOR[] points;
	private int[] ids;

	public LEAP_POINT_MAPPING()
	{
		super(ALIGN_NONE);
	}


	public LEAP_POINT_MAPPING(int size)
	{
		super(ALIGN_NONE);
		allocateMemory(size);
	}


	public LEAP_VECTOR[] getPoints()
	{
		return points;
	}


	public int[] getIds()
	{
		return ids;
	}


	@Override
	public void read()
	{
		super.read();

		points = new LEAP_VECTOR[nPoints];
		ids = new int[nPoints];

		int pointOffset = 0;
		int idOffset = 0;

		for (int i = 0; i < nPoints; i++)
		{
			points[i] = new LEAP_VECTOR(pPoints.share(pointOffset));
			ids[i] = pIDs.getInt(idOffset);

			pointOffset += points[i].size();
			idOffset += 4;
		}
	}
}
