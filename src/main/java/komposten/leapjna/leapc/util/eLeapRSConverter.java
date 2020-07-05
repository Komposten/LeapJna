/*
 * Copyright 2020 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.util;

import com.sun.jna.FromNativeContext;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;

import komposten.leapjna.leapc.enums.Enums;
import komposten.leapjna.leapc.enums.eLeapRS;


/**
 * A type converter for converting integer values to and from {@link eLeapRS}
 * constants. This exists for internal usage by LeapJna wherever
 * <code>eLeapRS</code> is used as return value.
 */
public class eLeapRSConverter implements TypeConverter
{
	@Override
	public Object fromNative(Object nativeValue, FromNativeContext context)
	{
		eLeapRS result = null;

		if (nativeValue instanceof Integer)
		{
			Integer value = (Integer) nativeValue;
			Class<?> targetClass = context.getTargetType();

			if (eLeapRS.class.isAssignableFrom(targetClass))
			{
				result = Enums.parse(value, eLeapRS.Unknown);
			}
		}

		return result;
	}


	@Override
	public Object toNative(Object value, ToNativeContext context)
	{
		Integer result = null;

		if (value instanceof eLeapRS)
		{
			eLeapRS enumValue = (eLeapRS) value;

			result = enumValue.getValue();
		}

		return result;
	}


	@Override
	public Class<?> nativeType()
	{
		return Integer.class;
	}
}
