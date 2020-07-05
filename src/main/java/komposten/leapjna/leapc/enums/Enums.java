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


/**
 * Utility functions for converting from primitives to enum constants and back.
 */
public class Enums
{
	/**
	 * An interface for enums with integer values.
	 */
	public interface IntEnum
	{
		/**
		 * @return The integer value associated with this enum constant.
		 */
		int getValue();
	}

	/**
	 * An interface for enums with byte values.
	 */
	public interface ByteEnum
	{
		/**
		 * @return The byte value associated with this enum constant.
		 */
		byte getValue();
	}

	/**
	 * An interface for enums with integer values that are meant to be used as
	 * flags/bitmasks.
	 */
	public interface IntFlagEnum<T extends IntFlagEnum<?>> extends IntEnum
	{
		/**
		 * @return This enum's constant which represents an empty mask (typically
		 *         has a value of 0).
		 */
		T getEmptyMaskConstant();


		/**
		 * Parses the provided bitmask into the corresponding array of enum
		 * constants.
		 * 
		 * @param mask A bitmask.
		 * @return An array of enum constants corresponding to the flags set in the
		 *         mask, or {@link #getEmptyMaskConstant()} if there are no matching
		 *         enum constants or the mask is empty.
		 */
		@SuppressWarnings({ "unchecked" })
		default T[] parseMask(int mask)
		{
			T[] enumValues = (T[]) getClass().getEnumConstants();
			T emptyMask = getEmptyMaskConstant();

			List<T> flags = new ArrayList<>();

			for (T enumValue : enumValues)
			{
				if ((mask & enumValue.getValue()) == enumValue.getValue()
						&& enumValue != emptyMask)
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
	 * Attempts to find an enum constant in the type <code>E</code> matching the
	 * provided integer value.
	 * 
	 * @param <E> The enum type to convert the integer value to.
	 * @param value The integer value of an enum constant.
	 * @param defaultValue The default return value if no enum constant in the
	 *          specified type matches the provided integer value.
	 * 
	 * @return An enum constant of type <code>E</code> matching
	 *         <code>value</code>, or <code>defaultValue</code> if no match is
	 *         found.
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
	 * Attempts to find an enum constant in the type <code>E</code> matching the
	 * provided byte value.
	 * 
	 * @param <E> The enum type to convert the byte value to.
	 * @param value The byte value of an enum constant.
	 * @param defaultValue The default return value if no enum constant in the
	 *          specified type matches the provided byte value.
	 * 
	 * @return An enum constant of type <code>E</code> matching
	 *         <code>value</code>, or <code>defaultValue</code> if no match is
	 *         found.
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


	/**
	 * Parses the provided bitmask into an array of corresponding enum constants.
	 * 
	 * @param mask A bitmask.
	 * @param <E> The flag enum type whose constants to use.
	 * @param enumClass The flag enum class whose constants to use.
	 * @return An array of enum constants corresponding to the flags set in the
	 *         mask, or the enum class' {@link IntFlagEnum#getEmptyMaskConstant()
	 *         empty constant} if there are no matching enum constants or the mask
	 *         is empty.
	 */
	public static <E extends IntFlagEnum<E>> E[] parseMask(int mask, Class<E> enumClass)
	{
		E[] enumValues = enumClass.getEnumConstants();

		return enumValues[0].parseMask(mask);
	}


	/**
	 * Converts a set of flag enum constants into a bitmask using bitwise or.
	 * 
	 * @param <E> The flag enum type the constants belong to.
	 * @param flags The flag constants to merge into a bitmask.
	 * @return A bitmask corresponding to the provided flags.
	 */
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
