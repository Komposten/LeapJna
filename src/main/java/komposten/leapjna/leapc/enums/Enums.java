/*
 * Copyright 2020 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.enums;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class Enums
{
	public interface IntEnum
	{
		int getValue();
	}

	public interface ByteEnum
	{
		byte getValue();
	}

	public interface IntFlagEnum<T extends IntFlagEnum<?>> extends IntEnum
	{
		T getEmptyMaskConstant();


		@SuppressWarnings({ "unchecked" })
		default T[] parseMask(int mask)
		{
			T[] enumValues = (T[]) getClass().getEnumConstants();
			T emptyMask = getEmptyMaskConstant();

			List<T> flags = new ArrayList<>();

			for (T enumValue : enumValues)
			{
				if ((mask & enumValue.getValue()) == enumValue.getValue() && enumValue != emptyMask)
				{
					flags.add(enumValue);
				}
			}

			if (flags.isEmpty())
			{
				flags.add(emptyMask);
			}

			T[] resultArray = (T[]) Array.newInstance(emptyMask.getClass(), flags.size());

			return flags.toArray(resultArray);
		}
	}


	private Enums()
	{}


	/**
	 * Attempts to find an enum constant in the type <code>E</code> matching the provided
	 * integer value.
	 * 
	 * @param <E> The enum type to convert the integer value to.
	 * @param value The integer value of an enum constant.
	 * @param defaultValue The default return value if no enum constant in the specified
	 *          type matches the provided integer value.
	 * 
	 * @return An enum constant of type <code>E</code> matching <code>value</code>, or
	 *         <code>defaultValue</code> if no match is found.
	 */
	public static <E extends IntEnum> E parse(int value, E defaultValue)
	{
		@SuppressWarnings("unchecked")
		E[] enumValues = (E[]) defaultValue.getClass().getEnumConstants();

		for (E enumValue : enumValues)
		{
			if (enumValue.getValue() == value)
			{
				return enumValue;
			}
		}

		return defaultValue;
	}


	/**
	 * Attempts to find an enum constant in the type <code>E</code> matching the provided
	 * byte value.
	 * 
	 * @param <E> The enum type to convert the byte value to.
	 * @param value The byte value of an enum constant.
	 * @param defaultValue The default return value if no enum constant in the specified
	 *          type matches the provided byte value.
	 * 
	 * @return An enum constant of type <code>E</code> matching <code>value</code>, or
	 *         <code>defaultValue</code> if no match is found.
	 */
	public static <E extends ByteEnum> E parse(byte value, E defaultValue)
	{
		@SuppressWarnings("unchecked")
		E[] enumValues = (E[]) defaultValue.getClass().getEnumConstants();

		for (E enumValue : enumValues)
		{
			if (enumValue.getValue() == value)
			{
				return enumValue;
			}
		}

		return defaultValue;
	}


	public static <E extends IntFlagEnum<E>> E[] parseMask(int mask, Class<E> enumClass)
	{
		E[] enumValues = enumClass.getEnumConstants();

		return enumValues[0].parseMask(mask);
	}


	@SuppressWarnings("unchecked")
	public static <E extends IntFlagEnum<E>> int createMask(E... flags)
	{
		int mask = 0;
		for (E flag : flags)
		{
			mask |= flag.getValue();
		}

		return mask;
	}
}
