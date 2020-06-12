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

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.jna.FromNativeContext;

import komposten.leapjna.leapc.enums.eLeapRS;


class eLeapRSConverterTest
{
	private eLeapRSConverter converter;
	private FromNativeContext eLeapRSContext;
	private FromNativeContext stringContext;

	@BeforeEach
	void setup()
	{
		converter = new eLeapRSConverter();

		try
		{
			Constructor<FromNativeContext> constructor = FromNativeContext.class
					.getDeclaredConstructor(Class.class);
			constructor.setAccessible(true);
			eLeapRSContext = constructor.newInstance(eLeapRS.class);
			stringContext = constructor.newInstance(String.class);
		}
		catch (NoSuchMethodException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e)
		{
			throw new RuntimeException("Failed to create FromNativeContext instances: ", e);
		}
	}


	@Test
	void fromNative_validInt_correctEnumConstant()
	{
		Object actual = converter.fromNative(eLeapRS.Timeout.getValue(), eLeapRSContext);
		assertThat(actual).isEqualTo(eLeapRS.Timeout);
	}


	@Test
	void fromNative_invalidInt_unknown()
	{
		Object actual = converter.fromNative(12345, eLeapRSContext);
		assertThat(actual).isEqualTo(eLeapRS.Unknown);
	}


	@Test
	void fromNative_nonInt_null()
	{
		Object actual = converter.fromNative("12345", eLeapRSContext);
		assertThat(actual).isNull();
	}


	@Test
	void fromNative_incorrectContext_null()
	{
		Object actual = converter.fromNative(eLeapRS.Timeout.getValue(), stringContext);
		assertThat(actual).isNull();
	}


	@Test
	void toNative_validConstant_correctInteger()
	{
		Object actual = converter.toNative(eLeapRS.Timeout, null);
		assertThat(actual).isEqualTo(eLeapRS.Timeout.getValue());
	}


	@Test
	void toNative_null_null()
	{
		Object actual = converter.toNative(null, null);
		assertThat(actual).isNull();
	}


	@Test
	void toNative_incorrectValueType_null()
	{
		Object actual = converter.toNative("timeout", null);
		assertThat(actual).isNull();
	}
}
