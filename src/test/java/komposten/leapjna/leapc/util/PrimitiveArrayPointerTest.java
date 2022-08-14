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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.sun.jna.Native;


class PrimitiveArrayPointerTest
{
	@Test
	void ints_correctSizeAllocated()
	{
		int arraySize = 5;
		PrimitiveArrayPointer arrayPointer = PrimitiveArrayPointer.ints(arraySize);

		int expectedSize = Native.getNativeSize(int.class) * arraySize;
		assertThat(arrayPointer.size()).isEqualTo(expectedSize);
	}


	@Test
	void floats_correctSizeAllocated()
	{
		int arraySize = 5;
		PrimitiveArrayPointer arrayPointer = PrimitiveArrayPointer.floats(arraySize);

		int expectedSize = Native.getNativeSize(float.class) * arraySize;
		assertThat(arrayPointer.size()).isEqualTo(expectedSize);
	}


	@Test
	void ints_allValuesZero()
	{
		int arraySize = 5;
		PrimitiveArrayPointer arrayPointer = PrimitiveArrayPointer.ints(arraySize);
		assertThat(arrayPointer.toIntArray(new int[arraySize])).containsOnly(0);
	}


	@Test
	void floats_allValuesZero()
	{
		int arraySize = 5;
		PrimitiveArrayPointer arrayPointer = PrimitiveArrayPointer.floats(arraySize);
		assertThat(arrayPointer.toFloatArray(new float[arraySize])).containsOnly(0);
	}


	@Test
	void getArraySize_correctSize()
	{
		int arraySize = 5;
		PrimitiveArrayPointer arrayPointer = PrimitiveArrayPointer.floats(arraySize);
		assertThat(arrayPointer.getArraySize()).isEqualTo(arraySize);
	}


	@Test
	void getIntAt_floatArray_throwsIllegalStateException()
	{
		PrimitiveArrayPointer arrayPointer = PrimitiveArrayPointer.floats(5);
		assertThatThrownBy(() -> arrayPointer.getIntAt(0))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("Incorrect primitive type: FLOAT != INT");
	}


	@ParameterizedTest
	@ValueSource(ints = { -1, 10 })
	void getIntAt_outOfBounds_throwsArrayIndexOutOfBoundsException(int index)
	{
		PrimitiveArrayPointer arrayPointer = PrimitiveArrayPointer.ints(5);
		assertThatThrownBy(() -> arrayPointer.getIntAt(index))
				.isInstanceOf(ArrayIndexOutOfBoundsException.class)
				.hasMessage("Array index out of range: " + index);
	}


	@Test
	void getIntAt_withValues_returnsCorrectValue()
	{
		PrimitiveArrayPointer arrayPointer = PrimitiveArrayPointer.ints(5);
		int[] values = { 5, 4, 3, 2, 1 };
		arrayPointer.write(0, values, 0, values.length);

		assertThat(arrayPointer.getIntAt(1)).isEqualTo(4);
		assertThat(arrayPointer.getIntAt(2)).isEqualTo(3);
		assertThat(arrayPointer.getIntAt(4)).isEqualTo(1);
	}


	@Test
	void getFloatAt_intArray_throwsIllegalStateException()
	{
		PrimitiveArrayPointer arrayPointer = PrimitiveArrayPointer.ints(5);
		assertThatThrownBy(() -> arrayPointer.getFloatAt(0))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("Incorrect primitive type: INT != FLOAT");
	}


	@ParameterizedTest
	@ValueSource(ints = { -1, 10 })
	void getFloatAt_outOfBounds_throwsArrayIndexOutOfBoundsException(int index)
	{
		PrimitiveArrayPointer arrayPointer = PrimitiveArrayPointer.floats(5);
		assertThatThrownBy(() -> arrayPointer.getFloatAt(index))
				.isInstanceOf(ArrayIndexOutOfBoundsException.class)
				.hasMessage("Array index out of range: " + index);
	}


	@Test
	void getFloatAt_withValues_returnsCorrectValue()
	{
		PrimitiveArrayPointer arrayPointer = PrimitiveArrayPointer.floats(5);
		float[] values = { 5, 4, 3, 2, 1 };
		arrayPointer.write(0, values, 0, values.length);

		assertThat(arrayPointer.getFloatAt(1)).isEqualTo(4);
		assertThat(arrayPointer.getFloatAt(2)).isEqualTo(3);
		assertThat(arrayPointer.getFloatAt(4)).isEqualTo(1);
	}


	@Test
	void toIntArray_floatArray_throwsIllegalStateException()
	{
		PrimitiveArrayPointer arrayPointer = PrimitiveArrayPointer.floats(5);
		assertThatThrownBy(() -> arrayPointer.toIntArray(new int[0]))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("Incorrect primitive type: FLOAT != INT");
	}


	@Test
	void toIntArray_withValues_returnsCorrectValue()
	{
		PrimitiveArrayPointer arrayPointer = PrimitiveArrayPointer.ints(5);
		int[] values = { 5, 4, 3, 2, 1 };
		arrayPointer.write(0, values, 0, values.length);

		assertThat(arrayPointer.toIntArray(new int[0])).containsExactly(values);
	}


	@Test
	void toFloatArray_intArray_throwsIllegalStateException()
	{
		PrimitiveArrayPointer arrayPointer = PrimitiveArrayPointer.ints(5);
		assertThatThrownBy(() -> arrayPointer.toFloatArray(new float[0]))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("Incorrect primitive type: INT != FLOAT");
	}


	@Test
	void toFloatArray_withValues_returnsCorrectValue()
	{
		PrimitiveArrayPointer arrayPointer = PrimitiveArrayPointer.floats(5);
		float[] values = { 5, 4, 3, 2, 1 };
		arrayPointer.write(0, values, 0, values.length);

		assertThat(arrayPointer.toFloatArray(new float[0])).containsExactly(values);
	}
}