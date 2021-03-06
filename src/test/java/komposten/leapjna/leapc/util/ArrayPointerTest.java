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

import java.util.Objects;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;


class ArrayPointerTest
{
	private void assertMemoryContains(Pointer pointer, int offset, int a, double b, long c)
	{
		assertMemoryContains(pointer, offset, a, b, c, null);
	}
	
	
	private void assertMemoryContains(Pointer pointer, int offset, int a, double b, long c,
			String description)
	{
		assertThat(pointer.getInt(offset)).as(description).isEqualTo(a);
		assertThat(pointer.getDouble(offset + 4)).as(description).isEqualTo(b);
		assertThat(pointer.getLong(offset + 12)).as(description).isEqualTo(c);
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
	void setElement_outOfBounds_arrayOutOfBoundsException()
	{
		StructureWithCtors[] values = new StructureWithCtors[] {
				new StructureWithCtors(1, 2, 3), new StructureWithCtors(5, 4.5, 4) };

		ArrayPointer<StructureWithCtors> arrayPointer = ArrayPointer.fromArray(values);

		assertThatThrownBy(() -> arrayPointer.setElement(-1, null))
				.isInstanceOf(ArrayIndexOutOfBoundsException.class);
		assertThatThrownBy(() -> arrayPointer.setElement(values.length, null))
				.isInstanceOf(ArrayIndexOutOfBoundsException.class);
	}


	@Test
	void setElements_subsetOfSource_overwriteCorrectValues()
	{
		StructureWithCtors[] initialElements = { new StructureWithCtors(1, 2, 3),
				new StructureWithCtors(4, 5, 6), new StructureWithCtors(7, 8, 9),
				new StructureWithCtors(10, 11, 12) };

		ArrayPointer<StructureWithCtors> arrayPointer = ArrayPointer
				.fromArray(initialElements);

		StructureWithCtors[] newElements = { null, new StructureWithCtors(9, 8, 7), null,
				new StructureWithCtors(5, 4, 3), null, null };
		
		arrayPointer.setElements(newElements, 1, 1, 3);
		assertMemoryContains(arrayPointer, 0, 1, 2, 3, "First element unchanged");
		assertMemoryContains(arrayPointer, 20, 9, 8, 7, "Second element new values");
		assertMemoryContains(arrayPointer, 40, 0, 0, 0, "Third element cleared (set to null)");
		assertMemoryContains(arrayPointer, 60, 5, 4, 3, "Fourth element new values");
	}


	@Test
	void setElements_countIsZero_doNothing()
	{
		StructureWithCtors[] values = { new StructureWithCtors(1, 2, 3) };

		ArrayPointer<StructureWithCtors> arrayPointer = ArrayPointer.fromArray(values);

		arrayPointer.setElements(new StructureWithCtors[5], 0, 0, 0);
		assertMemoryContains(arrayPointer, 0, 1, 2, 3);
	}
	
	
	@Test
	void setElements_negativeCount_illegalArgumentException()
	{
		ArrayPointer<StructureWithCtors> arrayPointer = ArrayPointer
				.empty(StructureWithCtors.class, 1);

		assertThatIllegalArgumentException()
				.isThrownBy(() -> arrayPointer.setElements(new StructureWithCtors[1], 0, 0, -1));
	}
	
	
	@Test
	void setElements_outOfBounds_arrayOutOfBoundsException()
	{
		StructureWithCtors[] values = { new StructureWithCtors(1, 2, 3),
				new StructureWithCtors(4, 5, 6) };

		ArrayPointer<StructureWithCtors> arrayPointer = ArrayPointer.fromArray(values);

		assertThatThrownBy(
				() -> arrayPointer.setElements(new StructureWithCtors[1], -1, 0, 1))
						.as("Negative source offset")
						.isInstanceOf(ArrayIndexOutOfBoundsException.class);
		assertThatThrownBy(
				() -> arrayPointer.setElements(new StructureWithCtors[1], 0, -1, 1))
						.as("Negative destination offset")
						.isInstanceOf(ArrayIndexOutOfBoundsException.class);
		assertThatThrownBy(
				() -> arrayPointer.setElements(new StructureWithCtors[1], 1, 0, 1))
						.as("Offset + count exceeds source bounds")
						.isInstanceOf(ArrayIndexOutOfBoundsException.class);
		assertThatThrownBy(
				() -> arrayPointer.setElements(new StructureWithCtors[5], 1, 0, 3))
						.as("Offset + count exceeds destination bounds")
						.isInstanceOf(ArrayIndexOutOfBoundsException.class);
	}


	@Test
	void setElements_entireSource_overwriteCorrectValues()
	{
		StructureWithCtors[] initialElements = { new StructureWithCtors(1, 2, 3),
				new StructureWithCtors(4, 5, 6), new StructureWithCtors(7, 8, 9),
				new StructureWithCtors(10, 11, 12) };

		ArrayPointer<StructureWithCtors> arrayPointer = ArrayPointer
				.fromArray(initialElements);

		StructureWithCtors[] newElements = { new StructureWithCtors(9, 8, 7), null,
				new StructureWithCtors(5, 4, 3) };
		
		arrayPointer.setElements(newElements, 1);
		assertMemoryContains(arrayPointer, 0, 1, 2, 3, "First element unchanged");
		assertMemoryContains(arrayPointer, 20, 9, 8, 7, "Second element new values");
		assertMemoryContains(arrayPointer, 40, 0, 0, 0, "Third element cleared (set to null)");
		assertMemoryContains(arrayPointer, 60, 5, 4, 3, "Fourth element new values");
	}


	@Test
	void setElements_sourceIsEmpty_doNothing()
	{
		StructureWithCtors[] values = { new StructureWithCtors(1, 2, 3) };

		ArrayPointer<StructureWithCtors> arrayPointer = ArrayPointer.fromArray(values);

		arrayPointer.setElements(new StructureWithCtors[0], 0);
		assertMemoryContains(arrayPointer, 0, 1, 2, 3);
	}
	
	
	@Test
	void setElements_sourceIsNull_nullPointerException()
	{
		StructureWithCtors[] values = { new StructureWithCtors(1, 2, 3) };

		ArrayPointer<StructureWithCtors> arrayPointer = ArrayPointer.fromArray(values);

		assertThatNullPointerException().isThrownBy(() -> arrayPointer.setElements(null, 1));
		assertThatNullPointerException().isThrownBy(() -> arrayPointer.setElements(null, 1, 1, 1));
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

		arrayPointer.setElements(new StructureWithCtors[] {
				new StructureWithCtors(1, 2, 3), new StructureWithCtors(5, 4.5, 4) }, 1);

		StructureWithCtors[] values = arrayPointer
				.getElements(new StructureWithCtors[arrayPointer.getArraySize()]);

		assertThat(values).hasSize(4);
		assertThat(values[0]).satisfies(value -> {
			assertThat(value.a).isZero();
			assertThat(value.b).isZero();
			assertThat(value.c).isZero();
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
			assertThat(value.a).isZero();
			assertThat(value.b).isZero();
			assertThat(value.c).isZero();
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
			assertThat(value.a).isZero();
			assertThat(value.b).isZero();
			assertThat(value.c).isZero();
		});
		assertThat(values[1]).satisfies(value -> {
			assertThat(value.a).isEqualTo(1);
			assertThat(value.b).isEqualTo(2d);
			assertThat(value.c).isEqualTo(3);
		});
		assertThat(values[2]).satisfies(value -> {
			assertThat(value.a).isZero();
			assertThat(value.b).isZero();
			assertThat(value.c).isZero();
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

		assertThat(values[0].a).isZero();
		assertThat(values[1].a).isZero();

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
	
	
	@Test
	void getElement_outOfBounds_arrayIndexOutOfBoundsException()
	{
		StructureWithCtors[] values = new StructureWithCtors[] {
				new StructureWithCtors(1, 2, 3), new StructureWithCtors(5, 4.5, 4) };

		ArrayPointer<StructureWithCtors> arrayPointer = ArrayPointer.fromArray(values);

		assertThatThrownBy(() -> arrayPointer.getElement(-1))
				.isInstanceOf(ArrayIndexOutOfBoundsException.class);
		assertThatThrownBy(() -> arrayPointer.getElement(values.length))
				.isInstanceOf(ArrayIndexOutOfBoundsException.class);
	}
	
	
	@Test
	void equals_sameObject_true()
	{
		ArrayPointer<StructureWithCtors> arrayPointer1 = ArrayPointer
				.empty(StructureWithCtors.class, 5);
		assertThat(arrayPointer1).isEqualTo(arrayPointer1);
	}
	
	
	@Test
	void equals_differentArrayPointer_false()
	{
		ArrayPointer<?> arrayPointer = ArrayPointer.empty(StructureWithCtors.class, 5);
		ArrayPointer<?> differentPeer = ArrayPointer.empty(StructureWithCtors.class, arrayPointer.getArraySize());
		ArrayPointer<?> differentSize = ArrayPointer.fromPointer(arrayPointer, StructureWithCtors.class, 4);
		ArrayPointer<?> differentType = ArrayPointer.fromPointer(arrayPointer, StructureNoPointerCtor.class, arrayPointer.getArraySize());
		ArrayPointer<?> differentValues = ArrayPointer.empty(StructureWithCtors.class, arrayPointer.getArraySize());
		differentValues.setInt(0, 1);

		// Compare to array with same class, size and values but different peer.
		assertThat(arrayPointer).isEqualTo(differentPeer);
		// Compare to array with same class, values and peer but different size.
		assertThat(arrayPointer).isNotEqualTo(differentSize);
		// Compare to array with same values, peer and size but different class.
		assertThat(arrayPointer).isNotEqualTo(differentType);
		// Compare to array with same class, peer and size but different values.
		assertThat(arrayPointer).isNotEqualTo(differentValues);
	}
	
	
	@Test
	void shallowEquals_sameObject_true()
	{
		ArrayPointer<StructureWithCtors> arrayPointer1 = ArrayPointer
				.empty(StructureWithCtors.class, 5);
		assertThat(arrayPointer1.shallowEquals(arrayPointer1)).isTrue();
	}
	
	
	@Test
	void shallowEquals_differentArrayPointer_false()
	{
		ArrayPointer<?> arrayPointer = ArrayPointer.empty(StructureWithCtors.class, 5);
		ArrayPointer<?> differentPeer = ArrayPointer.empty(StructureWithCtors.class, arrayPointer.getArraySize());
		ArrayPointer<?> differentSize = ArrayPointer.fromPointer(arrayPointer, StructureWithCtors.class, 4);
		ArrayPointer<?> differentType = ArrayPointer.fromPointer(arrayPointer, StructureNoPointerCtor.class, arrayPointer.getArraySize());
		ArrayPointer<?> differentValues = ArrayPointer.empty(StructureWithCtors.class, arrayPointer.getArraySize());
		differentValues.setInt(0, 1);

		// Compare to array with same class, size and values but different peer.
		assertThat(arrayPointer.shallowEquals(differentPeer)).isFalse();
		// Compare to array with same class, values and peer but different size.
		assertThat(arrayPointer.shallowEquals(differentSize)).isFalse();
		// Compare to array with same values, peer and size but different class.
		assertThat(arrayPointer.shallowEquals(differentType)).isFalse();
		// Compare to array with same class, peer and size but different values.
		assertThat(arrayPointer.shallowEquals(differentValues)).isFalse();
	}
	
	
	@Test
	void equals_nonArrayPointer_false()
	{
		ArrayPointer<StructureWithCtors> arrayPointer1 = ArrayPointer
				.empty(StructureWithCtors.class, 5);
		assertThat(arrayPointer1).isNotEqualTo("string");
		assertThat(arrayPointer1).isNotEqualTo(null);
	}
	
	
	@Test
	void hashCode_calculatedProperly()
	{
		ArrayPointer<?> arrayPointer = ArrayPointer.empty(StructureWithCtors.class, 5);
		ArrayPointer<?> differentPeer = ArrayPointer.empty(StructureWithCtors.class, arrayPointer.getArraySize());
		ArrayPointer<?> differentSize = ArrayPointer.fromPointer(arrayPointer, StructureWithCtors.class, 4);
		ArrayPointer<?> differentType = ArrayPointer.fromPointer(arrayPointer, StructureNoPointerCtor.class, arrayPointer.getArraySize());
		ArrayPointer<?> differentValues = ArrayPointer.empty(StructureWithCtors.class, arrayPointer.getArraySize());
		differentValues.setInt(0, 1);

		// Compare to array with same class, size and values but different peer.
		assertThat(arrayPointer).hasSameHashCodeAs(differentPeer);
		// Compare to array with same class, values and peer but different size.
		assertThat(arrayPointer.hashCode()).isNotEqualTo(differentSize.hashCode());
		// Compare to array with same values, peer and size but different class.
		assertThat(arrayPointer.hashCode()).isNotEqualTo(differentType.hashCode());
		// Compare to array with same class, peer and size but different values.
		assertThat(arrayPointer.hashCode()).isNotEqualTo(differentValues.hashCode());
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
		
		
		@Override
		public int hashCode()
		{
			return Objects.hash(a);
		}
		
		
		@Override
		public boolean equals(Object o)
		{
			if (o instanceof StructureNoPointerCtor)
			{
				StructureNoPointerCtor o2 = (StructureNoPointerCtor)o;
				
				return o2.a == a;
			}
			
			return false;
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
		
		
		@Override
		public int hashCode()
		{
			return Objects.hash(a, b, c);
		}
		
		
		@Override
		public boolean equals(Object o)
		{
			if (o instanceof StructureWithCtors)
			{
				StructureWithCtors o2 = (StructureWithCtors)o;
				
				return o2.a == a && o2.b == b && o2.c == c;
			}
			
			return false;
		}
	}
}