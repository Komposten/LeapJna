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
import com.sun.jna.Union;

import komposten.leapjna.leapc.enums.Enums;
import komposten.leapjna.leapc.enums.eLeapValueType;


/**
 * A variant data type used to get and set service configuration values.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___v_a_r_i_a_n_t">LeapC
 *      API - LEAP_VARIANT</a>
 */
@FieldOrder({ "type", "union" })
public class LEAP_VARIANT extends Structure
{
	public static class ValueUnion extends Union
	{
		/** A Boolean value. */
		public boolean boolValue;

		/** An integer value. */
		public int iValue;

		/** A floating-point value. */
		public float fValue;

		/** A String value. */
		public String strValue;

		public ValueUnion()
		{}


		private ValueUnion(boolean value)
		{
			boolValue = value;
			setType("boolValue");
		}


		private ValueUnion(int value)
		{
			iValue = value;
			setType("iValue");
		}


		private ValueUnion(float value)
		{
			fValue = value;
			setType("fValue");
		}


		private ValueUnion(String value)
		{
			strValue = value;
			setType("strValue");
		}
	}

	/** The active data type in this instance. */
	public int type;

	/**
	 * <p>
	 * A union containing the value of this instance.
	 * </p>
	 * <p>
	 * Access the value either through the appropriate field in <code>union</code>, or by
	 * using one of the {@link #getBoolean()}, {@link #getInt()}, {@link #getFloat()},
	 * {@link #getString()} or {@link #getValue()} methods in this class.
	 * </p>
	 */
	public ValueUnion union;

	private eLeapValueType typeE;

	public LEAP_VARIANT()
	{
		super(ALIGN_NONE);
	}


	@Override
	public void read()
	{
		super.read();
	}


	public LEAP_VARIANT(boolean value)
	{
		super(ALIGN_NONE);
		type = eLeapValueType.Boolean.value;
		union = new ValueUnion(value);
	}


	public LEAP_VARIANT(int value)
	{
		super(ALIGN_NONE);
		type = eLeapValueType.Int32.value;
		union = new ValueUnion(value);
	}


	public LEAP_VARIANT(float value)
	{
		super(ALIGN_NONE);
		type = eLeapValueType.Float.value;
		union = new ValueUnion(value);
	}


	public LEAP_VARIANT(String value)
	{
		super(ALIGN_NONE);
		type = eLeapValueType.String.value;
		union = new ValueUnion(value);
	}


	/**
	 * @return The type as an {@link eLeapValueType} instead of an int.
	 */
	public eLeapValueType getType()
	{
		if (typeE == null)
		{
			typeE = Enums.parse(type, eLeapValueType.Unknown);
		}

		return typeE;
	}


	/**
	 * @return The value as a boolean.
	 * @throws IllegalStateException If this variant is not an
	 *           {@link eLeapValueType#Boolean} variant.
	 */
	public boolean getBoolean()
	{
		checkType(eLeapValueType.Boolean);

		return union.boolValue;
	}


	/**
	 * @return The value as an integer.
	 * @throws IllegalStateException If this variant is not an {@link eLeapValueType#Int32}
	 *           variant.
	 */
	public int getInt()
	{
		checkType(eLeapValueType.Int32);

		return union.iValue;
	}


	/**
	 * @return The value as a floating-point value.
	 * @throws IllegalStateException If this variant is not an {@link eLeapValueType#Float}
	 *           variant.
	 */
	public float getFloat()
	{
		checkType(eLeapValueType.Float);

		return union.fValue;
	}


	/**
	 * @return The value as a String.
	 * @throws IllegalStateException If this variant is not an {@link eLeapValueType#String}
	 *           variant.
	 */
	public String getString()
	{
		checkType(eLeapValueType.String);

		return union.strValue;
	}


	/**
	 * @return The value held by this instance, regardless of the value type.
	 */
	public Object getValue()
	{
		switch (getType())
		{
			case Boolean :
				return union.boolValue;
			case Int32 :
				return union.iValue;
			case Float :
				return union.fValue;
			case String :
				return union.strValue;
			case Unknown :
			default :
				return null;
		}
	}


	private void checkType(eLeapValueType valueType)
	{
		if (type != valueType.value)
		{
			throw new IllegalStateException(
					"Incorrect value type: " + typeE + " != " + valueType);
		}
	}
}
