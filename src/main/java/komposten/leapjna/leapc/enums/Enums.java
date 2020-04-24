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
		default boolean useDefaultParseMask()
		{
			return true;
		}


		T getEmptyMaskConstant();


		@SuppressWarnings("unused")
		default T[] parseMask(int mask)
		{
			return null;
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

		if (enumValues[0].useDefaultParseMask())
		{
			return defaultParseMask(mask, enumValues[0].getEmptyMaskConstant());
		}
		else
		{
			return enumValues[0].parseMask(mask);
		}
	}


	@SuppressWarnings("unchecked")
	private static <E extends IntFlagEnum<E>> E[] defaultParseMask(int mask, E emptyMask)
	{
		E[] enumValues = (E[]) emptyMask.getClass().getEnumConstants();

		List<E> flags = new ArrayList<>();

		for (E enumValue : enumValues)
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

		E[] resultArray = (E[]) Array.newInstance(emptyMask.getClass(), flags.size());

		return flags.toArray(resultArray);
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
