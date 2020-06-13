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
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;


class ArrayPointerTest
{
	private void assertMemoryContains(Pointer pointer, int offset, int a, double b, long c)
	{
		assertThat(pointer.getInt(offset)).isEqualTo(a);
		assertThat(pointer.getDouble(offset + 4)).isEqualTo(b);
		assertThat(pointer.getLong(offset + 12)).isEqualTo(c);
	}


	@Test
	void empty_structureWithoutNoArgCtor_IllegalArgumentException()
	{
		assertThatThrownBy(() -> ArrayPointer.empty(StructureNoCtors.class, 1))
				.isInstanceOf(IllegalArgumentException.class);
	}


	@Test
	void empty_validStructure_correctSizeAllocated()
	{
		int arraySize = 5;
		ArrayPointer<StructureWithCtors> arrayPointer = ArrayPointer
				.empty(StructureWithCtors.class, arraySize);

		int structureSize = new StructureWithCtors().size();
		int expectedSize = structureSize * arraySize;
		assertThat(arrayPointer.size()).isEqualTo(expectedSize);
	}


	@Test
	void fromArray_structureWithoutNoArgCtor_noException()
	{
		StructureWithCtors[] values = new StructureWithCtors[] { new StructureWithCtors() };

		assertThatCode(() -> ArrayPointer.fromArray(values)).doesNotThrowAnyException();
	}


	@Test
	void fromArray_zeroLengthArray_IllegalArgumentException()
	{
		StructureWithCtors[] values = new StructureWithCtors[0];

		assertThatIllegalArgumentException().isThrownBy(() -> ArrayPointer.fromArray(values));
	}


	@Test
	void fromArray_onlyNullInArray_NullPointerException()
	{
		StructureWithCtors[] values = new StructureWithCtors[] { null, null, null };

		assertThatNullPointerException().isThrownBy(() -> ArrayPointer.fromArray(values));
	}


	@Test
	void fromArray_validArray_createdSuccessfully()
	{
		StructureWithCtors[] values = new StructureWithCtors[] {
				new StructureWithCtors(1, 2, 3), new StructureWithCtors(5, 4.5, 4) };

		ArrayPointer<StructureWithCtors> arrayPointer = ArrayPointer.fromArray(values);
		assertMemoryContains(arrayPointer, 0, 1, 2, 3);
		assertMemoryContains(arrayPointer, 20, 5, 4.5, 4);
	}


	@Test
	void fromArray_validArrayWithNulls_createdSuccessfully()
	{
		StructureWithCtors[] values = new StructureWithCtors[] { null,
				new StructureWithCtors(1, 2, 3), null, null, new StructureWithCtors(5, 4.5, 4) };

		ArrayPointer<StructureWithCtors> arrayPointer = ArrayPointer.fromArray(values);
		assertMemoryContains(arrayPointer, 0, 0, 0, 0);
		assertMemoryContains(arrayPointer, 20, 1, 2, 3);
		assertMemoryContains(arrayPointer, 40, 0, 0, 0);
		assertMemoryContains(arrayPointer, 60, 0, 0, 0);
		assertMemoryContains(arrayPointer, 80, 5, 4.5, 4);
	}
	
	
	@Test
	void fromPointer_nullPointer_nullPointerException()
	{
		assertThatNullPointerException().as("null passed instead of pointer")
				.isThrownBy(() -> ArrayPointer.fromPointer(null, StructureWithCtors.class, 1));

		assertThatNullPointerException().as("null pointer passed").isThrownBy(
				() -> ArrayPointer.fromPointer(new Pointer(0), StructureWithCtors.class, 1));
	}
	
	
	@Test
	void fromPointer_validPointer_elementsCorrect()
	{
		// Create an array of structures
		StructureWithCtors expected1 = new StructureWithCtors(1, 1.5, 2);
		StructureWithCtors expected2 = new StructureWithCtors(-1, 5.2, 7);
		StructureWithCtors[] expectedArray = { expected1, expected2 };
		
		// Write those structures to some point in memory.
		Pointer expectedPointer = ArrayPointer.fromArray(expectedArray);
		
		// Use fromPointer to create an ArrayPointer based on the pointer from above.
		ArrayPointer<StructureWithCtors> array = ArrayPointer.fromPointer(expectedPointer,
				StructureWithCtors.class, expectedArray.length);
		
		assertThat(array.getArraySize()).isEqualTo(expectedArray.length);
		for (int i = 0; i < array.getArraySize(); i++)
		{
			StructureWithCtors actual = array.getElement(i);
			StructureWithCtors expected = expectedArray[i];
			assertThat(actual.a).isEqualTo(expected.a);
			assertThat(actual.b).isCloseTo(expected.b, Offset.offset(0.001));
			assertThat(actual.c).isEqualTo(expected.c);
		}
	}


	@Test
	void setElement_newValue_overwriteExisting()
	{
		ArrayPointer<StructureWithCtors> arrayPointer = ArrayPointer
				.empty(StructureWithCtors.class, 2);

		arrayPointer.setElement(1, new StructureWithCtors(7, 8, 9));
		assertMemoryContains(arrayPointer, 0, 0, 0, 0);
		assertMemoryContains(arrayPointer, 20, 7, 8, 9);
	}


	@Test
	void setElement_nullValue_clearExisting()
	{
		StructureWithCtors[] values = new StructureWithCtors[] {
				new StructureWithCtors(1, 2, 3), new StructureWithCtors(5, 4.5, 4) };

		ArrayPointer<StructureWithCtors> arrayPointer = ArrayPointer.fromArray(values);

		arrayPointer.setElement(1, null);
		assertMemoryContains(arrayPointer, 0, 1, 2, 3);
		assertMemoryContains(arrayPointer, 20, 0, 0, 0);
	}


	@Test
	void setElements_newValues_overwriteExisting()
	{
		ArrayPointer<StructureWithCtors> arrayPointer = ArrayPointer
				.empty(StructureWithCtors.class, 4);

		arrayPointer.setElements(1, new StructureWithCtors[] {
				new StructureWithCtors(7, 8, 9), new StructureWithCtors(5, 4, 3) });
		assertMemoryContains(arrayPointer, 0, 0, 0, 0);
		assertMemoryContains(arrayPointer, 20, 7, 8, 9);
		assertMemoryContains(arrayPointer, 40, 5, 4, 3);
		assertMemoryContains(arrayPointer, 60, 0, 0, 0);
	}


	@Test
	void setElements_nullValues_clearExisting()
	{
		StructureWithCtors[] values = new StructureWithCtors[] {
				new StructureWithCtors(1, 2, 3), new StructureWithCtors(3, 5, 6),
				new StructureWithCtors(9, 8, 7), new StructureWithCtors(6, 5, 4) };

		ArrayPointer<StructureWithCtors> arrayPointer = ArrayPointer.fromArray(values);

		arrayPointer.setElements(1, new StructureWithCtors[2]);
		assertMemoryContains(arrayPointer, 0, 1, 2, 3);
		assertMemoryContains(arrayPointer, 20, 0, 0, 0);
		assertMemoryContains(arrayPointer, 40, 0, 0, 0);
		assertMemoryContains(arrayPointer, 60, 6, 5, 4);
	}


	@Test
	void getElements_initialisedFromArray_correctValues()
	{
		StructureWithCtors[] inputValues = new StructureWithCtors[] {
				new StructureWithCtors(1, 2, 3), new StructureWithCtors(5, 4.5, 4) };

		ArrayPointer<StructureWithCtors> arrayPointer = ArrayPointer.fromArray(inputValues);

		StructureWithCtors[] values = arrayPointer
				.getElements(new StructureWithCtors[arrayPointer.getArraySize()]);

		assertThat(values).hasSize(2);
		assertThat(values[0]).satisfies(value -> {
			assertThat(value.a).isEqualTo(1);
			assertThat(value.b).isEqualTo(2d);
			assertThat(value.c).isEqualTo(3);
		});
		assertThat(values[1]).satisfies(value -> {
			assertThat(value.a).isEqualTo(5);
			assertThat(value.b).isEqualTo(4.5d);
			assertThat(value.c).isEqualTo(4);
		});
	}


	@Test
	void getElements_setWithSetElements_correctValues()
	{
		ArrayPointer<StructureWithCtors> arrayPointer = ArrayPointer
				.empty(StructureWithCtors.class, 4);

		arrayPointer.setElements(1, new StructureWithCtors[] {
				new StructureWithCtors(1, 2, 3), new StructureWithCtors(5, 4.5, 4) });

		StructureWithCtors[] values = arrayPointer
				.getElements(new StructureWithCtors[arrayPointer.getArraySize()]);

		assertThat(values).hasSize(4);
		assertThat(values[0]).satisfies(value -> {
			assertThat(value.a).isEqualTo(0);
			assertThat(value.b).isEqualTo(0);
			assertThat(value.c).isEqualTo(0);
		});
		assertThat(values[1]).satisfies(value -> {
			assertThat(value.a).isEqualTo(1);
			assertThat(value.b).isEqualTo(2d);
			assertThat(value.c).isEqualTo(3);
		});
		assertThat(values[2]).satisfies(value -> {
			assertThat(value.a).isEqualTo(5);
			assertThat(value.b).isEqualTo(4.5d);
			assertThat(value.c).isEqualTo(4);
		});
		assertThat(values[3]).satisfies(value -> {
			assertThat(value.a).isEqualTo(0);
			assertThat(value.b).isEqualTo(0);
			assertThat(value.c).isEqualTo(0);
		});
	}


	@Test
	void getElements_setWithSetElement_correctValues()
	{
		ArrayPointer<StructureWithCtors> arrayPointer = ArrayPointer
				.empty(StructureWithCtors.class, 3);

		arrayPointer.setElement(1, new StructureWithCtors(1, 2, 3));

		StructureWithCtors[] values = arrayPointer
				.getElements(new StructureWithCtors[arrayPointer.getArraySize()]);

		assertThat(values).hasSize(3);
		assertThat(values[0]).satisfies(value -> {
			assertThat(value.a).isEqualTo(0);
			assertThat(value.b).isEqualTo(0);
			assertThat(value.c).isEqualTo(0);
		});
		assertThat(values[1]).satisfies(value -> {
			assertThat(value.a).isEqualTo(1);
			assertThat(value.b).isEqualTo(2d);
			assertThat(value.c).isEqualTo(3);
		});
		assertThat(values[2]).satisfies(value -> {
			assertThat(value.a).isEqualTo(0);
			assertThat(value.b).isEqualTo(0);
			assertThat(value.c).isEqualTo(0);
		});
	}


	@Test
	void getElements_structureWithoutPointerCtor_mustReadManually()
	{
		StructureNoPointerCtor[] inputValues = new StructureNoPointerCtor[] {
				new StructureNoPointerCtor(1), new StructureNoPointerCtor(5) };

		ArrayPointer<StructureNoPointerCtor> arrayPointer = ArrayPointer
				.fromArray(inputValues);

		StructureNoPointerCtor[] values = arrayPointer
				.getElements(new StructureNoPointerCtor[arrayPointer.getArraySize()]);

		assertThat(values).hasSize(2);

		assertThat(values[0].a).isEqualTo(0);
		assertThat(values[1].a).isEqualTo(0);

		values[0].read();
		values[1].read();

		assertThat(values[0].a).isEqualTo(1);
		assertThat(values[1].a).isEqualTo(5);
	}


	@Test
	void getElement_initialisedFromArray_correctValues()
	{
		StructureWithCtors[] values = new StructureWithCtors[] {
				new StructureWithCtors(1, 2, 3), new StructureWithCtors(5, 4.5, 4) };

		ArrayPointer<StructureWithCtors> arrayPointer = ArrayPointer.fromArray(values);
		StructureWithCtors element0 = arrayPointer.getElement(0);
		StructureWithCtors element1 = arrayPointer.getElement(1);

		assertThat(element0).satisfies(value -> {
			assertThat(value.a).isEqualTo(1);
			assertThat(value.b).isEqualTo(2d);
			assertThat(value.c).isEqualTo(3);
		});
		assertThat(element1).satisfies(value -> {
			assertThat(value.a).isEqualTo(5);
			assertThat(value.b).isEqualTo(4.5d);
			assertThat(value.c).isEqualTo(4);
		});
	}


	public static class StructureNoCtors extends Structure
	{
	}


	public static class StructureWithZeroSize extends Structure
	{
		public StructureWithZeroSize()
		{}
	}


	@FieldOrder({ "a" })
	public static class StructureNoPointerCtor extends Structure
	{
		public int a;

		public StructureNoPointerCtor()
		{}


		public StructureNoPointerCtor(int a)
		{
			this.a = a;
			write();
		}
	}


	@FieldOrder({ "a", "b", "c" })
	public static class StructureWithCtors extends Structure
	{
		public int a;
		public double b;
		public long c;

		public StructureWithCtors(int a, double b, long c)
		{
			this(null);
			this.a = a;
			this.b = b;
			this.c = c;
			write();
		}


		public StructureWithCtors()
		{
			this(null);
		}


		public StructureWithCtors(Pointer pointer)
		{
			super(pointer, ALIGN_NONE);
			read();
		}
	}
}