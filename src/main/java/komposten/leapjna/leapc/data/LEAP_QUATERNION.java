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


/**
 * <p>
 * A four-element, floating-point quaternion.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___q_u_a_t_e_r_n_i_o_n">LeapC
 *      API - LEAP_QUATERNION</a>
 */
@FieldOrder({ "w", "x", "y", "z" })
public class LEAP_QUATERNION extends Structure
{
	/** The scalar position of the quaternion. */
	public float w;

	/** The x-coefficient of the vector portion of the quaternion. */
	public float x;

	/** The y-coefficient of the vector portion of the quaternion. */
	public float y;

	/** The z-coefficient of the vector portion of the quaternion. */
	public float z;
	
	public LEAP_QUATERNION()
	{
		super(ALIGN_NONE);
	}
	

	/**
	 * @return The quaternion's values as an array in the order: w, x, y, z.
	 */
	public float[] asArray()
	{
		return new float[] { w, x, y, z };
	}


	/**
	 * Calculates the roll, or rotation around the z-axis, described by this
	 * quaternion.
	 * @return The roll in radians.
	 */
	public float getRoll()
	{
		double nominator = 2 * (w * x + y * z);
		double denominator = 1 - 2 * (x * x + y * y);
		
		return (float) Math.atan2(nominator, denominator);
	}


	/**
	 * Calculates the roll, or rotation around the y-axis, described by this
	 * quaternion.
	 * @return The yaw in radians.
	 */
	public float getYaw()
	{
		float sinp = 2 * (w * y - z * x);
		float yaw;
		
		if (Math.abs(sinp) >= 1)
			yaw = (float) (sinp > 0 ? Math.PI / 2 : -Math.PI / 2);
		else
			yaw = (float) Math.asin(sinp);
		
		return yaw;
	}


	/**
	 * Calculates the roll, or rotation around the x-axis, described by this
	 * quaternion.
	 * @return The pitch in radians.
	 */
	public float getPitch()
	{
		double nominator = -2 * (w * z + x * y);
		double denominator = 1 - 2 * (y * y + z * z);

		return (float) Math.atan2(nominator, denominator);
	}


	/**
	 * @return This rotation represented as Euler angles in radians, ordered: roll (z-axis
	 *         rotation), yaw (y-axis rotation) and pitch (x-axis rotation).
	 */
	public float[] getEuler()
	{
		return new float[] { getRoll(), getYaw(), getPitch() };
	}
}