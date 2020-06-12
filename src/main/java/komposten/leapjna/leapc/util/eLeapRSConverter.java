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

public class eLeapRSConverter implements TypeConverter
{
	@Override
	public Object fromNative(Object nativeValue, FromNativeContext context)
	{
		Integer value = (Integer)nativeValue;
		Class<?> targetClass = context.getTargetType();
		
		if (!eLeapRS.class.isAssignableFrom(targetClass)) {
			return null;
		}
		
		return Enums.parse(value, eLeapRS.Unknown);
	}


	@Override
	public Object toNative(Object value, ToNativeContext context)
	{
		eLeapRS enumValue = (eLeapRS) value;
		
		if (enumValue == null)
			return null;
		return enumValue.getValue();
	}

	
	@Override
	public Class<?> nativeType()
	{
		return Integer.class;
	}
}
