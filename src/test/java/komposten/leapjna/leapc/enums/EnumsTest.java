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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import komposten.leapjna.leapc.enums.Enums.ByteEnum;
import komposten.leapjna.leapc.enums.Enums.IntEnum;
import komposten.leapjna.leapc.enums.Enums.IntFlagEnum;


class EnumsTest
{
	@Test
	void parse_intEnumValidValue_enumConstant()
	{
		TestIntEnum expected = TestIntEnum.First;
		TestIntEnum actual = Enums.parse(expected.value, TestIntEnum.Default);
		assertThat(actual).isSameAs(expected);

		expected = TestIntEnum.Second;
		actual = Enums.parse(expected.value, TestIntEnum.Default);
		assertThat(actual).isSameAs(expected);
	}


	@Test
	void parse_intEnumInvalidValue_providedDefault()
	{
		TestIntEnum expected = TestIntEnum.Default;
		TestIntEnum actual = Enums.parse(-1234, expected);
		assertThat(actual).isSameAs(expected);

		expected = TestIntEnum.Second;
		actual = Enums.parse(-1234, expected);
		assertThat(actual).isSameAs(expected);
	}


	@Test
	void parse_byteEnumValidValue_enumConstant()
	{
		TestByteEnum expected = TestByteEnum.First;
		TestByteEnum actual = Enums.parse(expected.value, TestByteEnum.Default);
		assertThat(actual).isSameAs(expected);

		expected = TestByteEnum.Second;
		actual = Enums.parse(expected.value, TestByteEnum.Default);
		assertThat(actual).isSameAs(expected);
	}


	@Test
	void parse_byteEnumInvalidValue_providedDefault()
	{
		TestByteEnum expected = TestByteEnum.Default;
		TestByteEnum actual = Enums.parse((byte) -10, expected);
		assertThat(actual).isSameAs(expected);

		expected = TestByteEnum.Second;
		actual = Enums.parse((byte) -10, expected);
		assertThat(actual).isSameAs(expected);
	}


	@Test
	@SuppressWarnings("unchecked")
	void createMask_noValues_emptyMask()
	{
		int actual = Enums.createMask();
		assertThat(actual).isZero();
	}


	@Test
	void createMask_singleValue_returnInput()
	{
		TestFlagEnum expected = TestFlagEnum.First;
		int actual = Enums.createMask(expected);

		assertThat(actual).isEqualTo(expected.value);
	}


	@Test
	void createMask_multipleValues_correctMask()
	{
		int expected = TestFlagEnum.First.value | TestFlagEnum.Third.value
				| TestFlagEnum.Fourth.value;
		int actual = Enums.createMask(TestFlagEnum.First, TestFlagEnum.Third,
				TestFlagEnum.Fourth);

		assertThat(actual).isEqualTo(expected);
	}


	@Test
	void parseMask_validMask_correctConstants()
	{
		int mask = TestFlagEnum.First.value | TestFlagEnum.Third.value
				| TestFlagEnum.Fourth.value;
		TestFlagEnum[] expected = { TestFlagEnum.First, TestFlagEnum.Third,
				TestFlagEnum.Fourth };

		assertThat(Enums.parseMask(mask, TestFlagEnum.class))
				.containsExactlyInAnyOrder(expected);
	}


	@Test
	void parseMask_validMaskCustomParse_correctConstants()
	{
		int mask = TestFlagEnum2.First.value | TestFlagEnum2.Third.value
				| TestFlagEnum2.Fourth.value;
		TestFlagEnum2[] expected = { TestFlagEnum2.Fourth };

		assertThat(Enums.parseMask(mask, TestFlagEnum2.class))
				.containsExactlyInAnyOrder(expected);
	}


	@Test
	void parseMask_emptyMask_emptyMaskConstant()
	{
		assertThat(Enums.parseMask(0, TestFlagEnum.class))
				.containsExactly(TestFlagEnum.Empty);
		assertThat(Enums.parseMask(0, TestFlagEnum2.class))
				.containsExactly(TestFlagEnum2.Fourth);
	}


	static enum TestIntEnum implements IntEnum
	{
		Default(-1), First(7), Second(23);

		private int value;


		private TestIntEnum(int value)
		{
			this.value = value;
		}


		@Override
		public int getValue()
		{
			return value;
		}
	}


	static enum TestByteEnum implements ByteEnum
	{
		Default(-1), First(7), Second(23);

		private byte value;


		private TestByteEnum(int value)
		{
			this.value = (byte) value;
		}


		@Override
		public byte getValue()
		{
			return value;
		}
	}


	static enum TestFlagEnum implements IntFlagEnum<TestFlagEnum>
	{
		Empty(0), First(4), Second(8), Third(32), Fourth(64);

		private int value;


		private TestFlagEnum(int value)
		{
			this.value = value;
		}


		@Override
		public int getValue()
		{
			return value;
		}


		@Override
		public TestFlagEnum getEmptyMaskConstant()
		{
			return Empty;
		}
	}


	static enum TestFlagEnum2 implements IntFlagEnum<TestFlagEnum2>
	{
		Empty(0), First(4), Second(8), Third(32), Fourth(64);

		private int value;


		private TestFlagEnum2(int value)
		{
			this.value = value;
		}


		@Override
		public int getValue()
		{
			return value;
		}


		@Override
		public TestFlagEnum2[] parseMask(int mask)
		{
			return new TestFlagEnum2[] { Fourth };
		}


		@Override
		public TestFlagEnum2 getEmptyMaskConstant()
		{
			return Third;
		}
	}
}
