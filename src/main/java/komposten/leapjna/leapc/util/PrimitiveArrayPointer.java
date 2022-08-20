/*
 * Copyright 2022 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.util;

import com.sun.jna.Memory;
import com.sun.jna.Native;

/**
 * <p>
 * A pointer to a memory block containing one or more values of a primitive type.
 * </p>
 */
public class PrimitiveArrayPointer extends Memory
{
	public enum Primitive
	{
		INT(int.class),
		FLOAT(float.class);

		private final Class<?> clazz;

		Primitive(Class<?> clazz)
		{
			this.clazz = clazz;
		}
	}

	private int arraySize;
	private int elementSize;
	private Primitive primitive;

	/**
	 * <p>
	 * Creates an empty <code>PrimitiveArrayPointer</code> containing primitive int values.
	 * </p>
	 * <p>
	 * Use {@link #toIntArray(int[])} or {@link #getIntAt(int)} to access the array data.
	 * </p>
	 * 
	 * @param arraySize The number of elements to allocate space for.
	 * @return A {@link PrimitiveArrayPointer} of the specified size pointing to an "empty" memory
	 *         block.
	 * @throws IllegalArgumentException If the specified type has no public no-arg
	 *           constructor, if its size is zero, or if its size cannot be determined.
	 */
	public static PrimitiveArrayPointer ints(int arraySize)
	{
		int elementSize = Native.getNativeSize(int.class);
		return new PrimitiveArrayPointer(Primitive.INT, elementSize, arraySize);
	}


	/**
	 * <p>
	 * Creates an empty <code>PrimitiveArrayPointer</code> containing primitive float
	 * values.
	 * </p>
	 * <p>
	 * Use {@link #toFloatArray(float[])} or {@link #getFloatAt(int)} to access the array data.
	 * </p>
	 * 
	 * @param arraySize The number of elements to allocate space for.
	 * @return A {@link PrimitiveArrayPointer} of the specified size pointing to an "empty" memory
	 *         block.
	 * @throws IllegalArgumentException If the specified type has no public no-arg
	 *           constructor, if its size is zero, or if its size cannot be determined.
	 */
	public static PrimitiveArrayPointer floats(int arraySize)
	{
		int elementSize = Native.getNativeSize(float.class);
		return new PrimitiveArrayPointer(Primitive.FLOAT, elementSize, arraySize);
	}


	public PrimitiveArrayPointer(Primitive primitive, int elementSize, int arraySize)
	{
		super((long)elementSize * arraySize);
		this.primitive = primitive;
		this.elementSize = Native.getNativeSize(primitive.clazz);
		this.arraySize = arraySize;

		// Clear the memory to get rid of garbage data.
		int memorySize = (int) size();
		write(0, new byte[memorySize], 0, memorySize);
	}


	/**
	 * @return The size of the array referenced by this object.
	 */
	public int getArraySize()
	{
		return arraySize;
	}
	
	public int getIntAt(int index)
	{
		checkType(Primitive.INT);
		

		if (index < 0 || index >= arraySize)
		{
			throw new ArrayIndexOutOfBoundsException(index);
		}
		
		return getInt((long)index * elementSize);
	}
	
	public float getFloatAt(int index)
	{
		checkType(Primitive.FLOAT);
		

		if (index < 0 || index >= arraySize)
		{
			throw new ArrayIndexOutOfBoundsException(index);
		}
		
		return getFloat((long)index * elementSize);
	}
	
	/**
	 * @param array The array into which the elements of this list are to be stored, if it
	 *          is big enough; otherwise, a new array of the same runtime type is allocated
	 *          for this purpose.
	 * @return An array containing the data referenced by this object.
	 */
	public int[] toIntArray(int[] array)
	{
		checkType(Primitive.INT);
		
		if (array.length < arraySize)
		{
			array = new int[arraySize];
		}
		
		for (int i = 0; i < arraySize; i++)
		{
			array[i] = getInt((long)i * elementSize);
		}
		
		return array;
	}
	
	/**
	 * @param array The array into which the elements of this list are to be stored, if it
	 *          is big enough; otherwise, a new array of the same runtime type is allocated
	 *          for this purpose.
	 * @return An array containing the data referenced by this object.
	 */
	public float[] toFloatArray(float[] array)
	{
		checkType(Primitive.FLOAT);
		
		if (array.length < arraySize)
		{
			array = new float[arraySize];
		}
		
		for (int i = 0; i < arraySize; i++)
		{
			array[i] = getFloat((long)i * elementSize);
		}
		
		return array;
	}


	private void checkType(Primitive expected)
	{
		if (primitive != expected)
		{
			throw new IllegalStateException(
					"Incorrect primitive type: " + primitive + " != " + expected);
		}
	}
}
