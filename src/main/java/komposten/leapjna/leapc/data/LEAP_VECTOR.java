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


/**
 * <p>
 * A three-element, floating-point vector.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___v_e_c_t_o_r">LeapC
 *      API - LEAP_VECTOR</a>
 */
@FieldOrder({ "x", "y", "z" })
public class LEAP_VECTOR extends Structure
{
	public float x;
	public float y;
	public float z;

	public LEAP_VECTOR()
	{
		super(ALIGN_NONE);
	}


	/**
	 * Creates a new <code>LEAP_VECTOR</code> to the specified coordinate.
	 * 
	 * @param x The x-coordinate of the vector.
	 * @param y The y-coordinate of the vector.
	 * @param z The z-coordinate of the vector.
	 */
	public LEAP_VECTOR(float x, float y, float z)
	{
		this();
		set(x, y, z);
	}


	public LEAP_VECTOR(Pointer pointer)
	{
		super(pointer);
		read();
	}


	/**
	 * @return The vector's values as an array in the order: x, y, z.
	 */
	public float[] asArray()
	{
		return new float[] { x, y, z };
	}


	/**
	 * Sets the values in this vector and writes them to native memory.
	 * 
	 * @param x The new x-coordinate of the vector.
	 * @param y The new y-coordinate of the vector.
	 * @param z The new z-coordinate of the vector.
	 */
	public void set(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;

		write();
	}


	/**
	 * <p>
	 * A pass-by-value implementation of <code>LEAP_VECTOR</code>.
	 * </p>
	 * <p>
	 * <b>Note:</b> This should only be used with methods that explicitly require
	 * <code>LEAP_VECTOR.ByValue</code>.
	 * </p>
	 */
	public static class ByValue extends LEAP_VECTOR implements Structure.ByValue
	{
		public ByValue()
		{
			super();
		}


		public ByValue(float x, float y, float z)
		{
			super(x, y, z);
		}
	}
}