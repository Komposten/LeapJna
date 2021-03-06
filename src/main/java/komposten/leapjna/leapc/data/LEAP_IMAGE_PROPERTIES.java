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

import komposten.leapjna.leapc.enums.Enums;
import komposten.leapjna.leapc.enums.eLeapImageFormat;
import komposten.leapjna.leapc.enums.eLeapImageType;


/**
 * Properties of a sensor image.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___i_m_a_g_e___p_r_o_p_e_r_t_i_e_s">LeapC
 *      API - LEAP_IMAGE_PROPERTIES</a>
 */
@FieldOrder({ "type", "format", "bpp", "width", "height", "x_scale", "y_scale",
		"x_offset", "y_offset" })
public class LEAP_IMAGE_PROPERTIES extends Structure
{
	/**
	 * The type of this image. Use {@link #getType()} to get the type as an
	 * {@link eLeapImageType} value.
	 */
	public int type;

	/**
	 * The format of this image. Use {@link #getFormat()} to get the type as an
	 * {@link eLeapImageFormat} value.
	 */
	public int format;

	/** The number of bytes per image pixel. */
	public int bpp;

	/** The number of horizontal pixels in the image. */
	public int width;

	/** The number of rows of pixels in the image. */
	public int height;

	/** Reserved for future use. */
	public float x_scale;

	/** Reserved for future use. */
	public float y_scale;

	/** Reserved for future use. */
	public float x_offset;

	/** Reserved for future use. */
	public float y_offset;


	public LEAP_IMAGE_PROPERTIES()
	{
		super(ALIGN_NONE);
	}


	/**
	 * @return The type as an {@link eLeapImageType} instead of an <code>int</code>.
	 */
	public eLeapImageType getType()
	{
		return Enums.parse(type, eLeapImageType.Unknown);
	}


	/**
	 * @return The format as an {@link eLeapImageFormat} instead of an <code>int</code>.
	 */
	public eLeapImageFormat getFormat()
	{
		return Enums.parse(format, eLeapImageFormat.Unknown);
	}
}
