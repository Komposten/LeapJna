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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;


/**
 * <p>
 * A pointer to a memory block containing one or more instances of a
 * <em>constant-size</em> {@link Structure}.
 * </p>
 * <p>
 * Use {@link #getElement(int)} or {@link #getElements(Structure[])} to access the array
 * data. <br>
 * Use {@link #setElement(int, Structure)} or {@link #setElements(Structure[], int)} to
 * write data to the array.
 * </p>
 * <p>
 * In order for this class to work properly the structure class stored by this array must
 * have a public no-arg constructor, a public constructor taking a single Pointer as
 * argument, and a constant size.
 * </p>
 * 
 * @param <T> The structure type contained in the array.
 */
public class ArrayPointer<T extends Structure> extends Memory
{
	private int elementSize;
	private int arraySize;
	private Class<T> clazz;

	/**
	 * <p>
	 * Creates an empty <code>ArrayPointer</code> based on the provided type and array
	 * size.
	 * </p>
	 * <p>
	 * <b>Note</b>: All elements will be assumed to have the same size, calculated using
	 * {@link Structure#size()}!
	 * </p>
	 * <p>
	 * Use {@link #getElement(int)} or {@link #getElements(Structure[])} to access the array
	 * data. <br>
	 * Use {@link #setElement(int, Structure)} or {@link #setElements(Structure[], int)} to
	 * write data to the array.
	 * </p>
	 * 
	 * @param clazz The type to store in the array.
	 * @param arraySize The number of elements to allocate space for.
	 * @param <T> The structure type the array should store.
	 * @return An {@link ArrayPointer} of the specified size pointing to an "empty" memory block.
	 * @throws IllegalArgumentException If the specified type has no public no-arg
	 *           constructor, if its size is zero, or if its size cannot be determined.
	 */
	public static <T extends Structure> ArrayPointer<T> empty(Class<T> clazz, int arraySize)
	{
		int elementSize = Structure.newInstance(clazz).size();
		return new ArrayPointer<>(clazz, elementSize, arraySize);
	}


	/**
	 * <p>
	 * Creates an <code>ArrayPointer</code> based on the provided array. The elements in
	 * the provided array will be copied from native memory, so ensure that they have been
	 * {@link Structure#write() written} before calling this method.
	 * </p>
	 * <p>
	 * Since all elements are stored in a contiguous memory block, <code>null</code>
	 * elements will be replaced by "empty" elements (all values set to 0).
	 * </p>
	 * <p>
	 * <b>Note</b>: All elements will be assumed to have the same size, calculated using
	 * {@link Structure#size()} on the first element in <code>values</code>!
	 * </p>
	 * <p>
	 * Use {@link #getElement(int)} or {@link #getElements(Structure[])} to access the array
	 * data. <br>
	 * Use {@link #setElement(int, Structure)} or {@link #setElements(Structure[], int)} to
	 * write data to the array.
	 * </p>
	 * 
	 * @param values The elements to store in the array.
	 * @param <T> The structure type the array should store.
	 * @return An {@link ArrayPointer} containing copies of the provided values.
	 * @throws IllegalArgumentException If <code>values</code> is zero-length.
	 * @throws NullPointerException If all of the elements in <code>values</code> is null.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Structure> ArrayPointer<T> fromArray(T[] values)
	{
		if (values.length < 1)
		{
			throw new IllegalArgumentException("elements must have at least one element!");
		}

		T nonNullValue = null;
		for (T value : values)
		{
			if (value != null)
			{
				nonNullValue = value;
				break;
			}
		}

		if (nonNullValue == null)
		{
			throw new NullPointerException("At least one value in values must be non-null!"
					+ " Use ArrayPointer.empty() if you want to create an empty array.");
		}

		ArrayPointer<T> result = new ArrayPointer<>((Class<T>) nonNullValue.getClass(),
				nonNullValue.size(), values.length);
		result.setElements(values, 0);

		return result;
	}
	
	
	/**
	 * <p>
	 * Creates an <code>ArrayPointer</code> based on the provided pointer, element
	 * type and array size.
	 * </p>
	 * <p>
	 * <b>Note</b>: All elements will be assumed to have the same size, calculated
	 * using {@link Structure#size()}!
	 * </p>
	 * <p>
	 * <b>Note</b>: This instance is not registered in
	 * {@link Memory#allocatedMemory} and will therefore not be disposed by
	 * {@link Memory#disposeAll()}. It will still be disposed when GC'd, though.
	 * </p>
	 * <p>
	 * Use {@link #getElement(int)} or {@link #getElements(Structure[])} to access
	 * the array data. <br>
	 * Use {@link #setElement(int, Structure)} or
	 * {@link #setElements(Structure[], int)} to write data to the array.
	 * </p>
	 * 
	 * @param pointer A pointer to the memory block of an existing array of
	 *          <code>clazz</code> structs.
	 * @param clazz The type of the structs stored in the array.
	 * @param arraySize The number of elements in the array.
	 * @param <T> The structure type the array should store.
	 * @return An {@link ArrayPointer} of the specified size pointing to the
	 *         provided memory block.
	 * @throws IllegalArgumentException If the specified type has no public no-arg
	 *           constructor.
	 */
	public static <T extends Structure> ArrayPointer<T> fromPointer(Pointer pointer, Class<T> clazz, int arraySize)
	{
		if (pointer == null || Pointer.nativeValue(pointer) == 0)
		{
			throw new NullPointerException("pointer must not be null or a null pointer!");
		}
		
		int elementSize = Structure.newInstance(clazz).size();
		return new ArrayPointer<>(pointer, clazz, elementSize, arraySize);
	}


	/**
	 * <p>
	 * Creates a new <code>ArrayPointer</code> based on the provided type.
	 * </p>
	 * <p>
	 * <b>Note</b>: All elements will be assumed to have the same size!
	 * </p>
	 * 
	 * @param clazz The type to store in the array.
	 * @param elementSize The size, in bytes, each element needs.
	 * @param arraySize The number of elements to allocate space for.
	 */
	private ArrayPointer(Class<T> clazz, int elementSize, int arraySize)
	{
		super((long)arraySize * elementSize);
		this.elementSize = elementSize;
		this.clazz = clazz;
		this.arraySize = arraySize;

		// Clear the memory to get rid of garbage data.
		int memorySize = (int) size();
		write(0, new byte[memorySize], 0, memorySize);
	}
	
	
	/**
	 * <p>
	 * Creates a new <code>ArrayPointer</code> based on the provided type, located
	 * at the specified pointer.
	 * </p>
	 * <p>
	 * <b>Note</b>: All elements will be assumed to have the same size!
	 * </p>
	 * <p>
	 * <b>Note</b>: This instance is not registered in
	 * {@link Memory#allocatedMemory} and will therefore not be disposed by
	 * {@link Memory#disposeAll()}. It will still be disposed when GC'd, though.
	 * </p>
	 * 
	 * @param pointer A pointer to an existing memory block to use for the array.
	 * @param clazz The type to store in the array.
	 * @param elementSize The size, in bytes, each element needs.
	 * @param arraySize The number of elements to allocate space for.
	 */
	private ArrayPointer(Pointer pointer, Class<T> clazz, int elementSize, int arraySize)
	{
		super();
		this.elementSize = elementSize;
		this.clazz = clazz;
		this.arraySize = arraySize;
		
		peer = Pointer.nativeValue(pointer);
		size = (long)arraySize * elementSize;
	}


	/**
	 * @return The size of the array referenced by this object.
	 */
	public int getArraySize()
	{
		return arraySize;
	}


	/**
	 * <p>
	 * Updates a single element in the array referenced by this object. The
	 * element will be copied from native memory, so ensure that it has been
	 * {@link Structure#write() written} before calling this method.
	 * </p>
	 * <p>
	 * <b>Note:</b> If <code>value</code> is <code>null</code>, the memory block
	 * at the given index will only be cleared with zeros. This means that calling
	 * <code>getElement</code> on that index will return an "empty" structure (all
	 * values are 0) rather than <code>null</code>.
	 * </p>
	 * 
	 * @param index The array index of the element to update.
	 * @param value The new value. May be <code>null</code>.
	 * @throws ArrayIndexOutOfBoundsException If the index is negative, or
	 *           greater than or equal to {@link #getArraySize()}.
	 */
	public void setElement(int index, T value)
	{
		if (index < 0 || index >= arraySize)
		{
			throw new ArrayIndexOutOfBoundsException(index);
		}

		byte[] buffer = new byte[elementSize];

		if (value != null)
		{
			value.getPointer().read(0, buffer, 0, elementSize);
		}

		write((long)index * elementSize, buffer, 0, elementSize);
	}


	/**
	 * <p>
	 * Copies the provided elements into the array referenced by this object. The
	 * provided elements will be copied from native memory, so ensure that they
	 * have been {@link Structure#write() written} before calling this method.
	 * </p>
	 * <p>
	 * <b>Note:</b> If any of the values are <code>null</code>, the memory block
	 * at those indices will only be cleared with zeros. This means that calling
	 * <code>getElement</code> on those indices will return "empty" structures
	 * (all values are 0) rather than <code>null</code>.
	 * </p>
	 * @param source The new elements. May contain <code>null</code> values.
	 * @param destOffset The array index to copy the elements to.
	 * 
	 * @throws ArrayIndexOutOfBoundsException If the offset is negative or the
	 *           offset plus the array size is larger than
	 *           {@link #getArraySize()}.
	 */
	public void setElements(T[] source, int destOffset)
	{
		setElements(source, 0, destOffset, source != null ? source.length : 0);
	}
	
	
	/**
	 * <p>
	 * Copies the provided elements into the array referenced by this object. The
	 * provided elements will be copied from native memory, so ensure that they
	 * have been {@link Structure#write() written} before calling this method.
	 * </p>
	 * <p>
	 * <b>Note:</b> If any of the values are <code>null</code>, the memory block
	 * at those indices will only be cleared with zeros. This means that calling
	 * <code>getElement</code> on those indices will return "empty" structures
	 * (all values are 0) rather than <code>null</code>.
	 * </p>
	 * 
	 * @param source The source array to copy elements from.
	 * @param sourceOffset The offset into <code>source</code> to start copying
	 *          from.
	 * @param destOffset The offset into this <code>ArrayPointer</code> to copy
	 *          to.
	 * @param count The number of objects to copy.
	 * @throws NullPointerException If <code>source</code> is <code>null</code>.
	 * @throws IllegalArgumentException If <code>count</code> is negative.
	 * @throws ArrayIndexOutOfBoundsException If the specified source or
	 *           destination range exceeds the source or destination bounds.
	 */
	public void setElements(T[] source, int sourceOffset, int destOffset, int count)
	{
		if (source == null)
		{
			throw new NullPointerException("source must not be null");
		}
		
		boundsCheck(source, sourceOffset, destOffset, count);

		byte[] buffer = new byte[elementSize];
		for (int i = 0; i < count; i++)
		{
			int si = i + sourceOffset;
			int di = i + destOffset;
			
			if (source[si] != null)
				source[si].getPointer().read(0, buffer, 0, elementSize);
			else
				Arrays.fill(buffer, (byte) 0);

			write(((long)di) * elementSize, buffer, 0, elementSize);
		}
	}


	private void boundsCheck(T[] source, int sourceOffset, int destOffset, int count)
	{
		if (count < 0)
		{
			String msg = String.format("count must be positive: %d < 0", count);
			throw new IllegalArgumentException(msg);
		}

		if (sourceOffset < 0)
		{
			String msg = String.format("sourceOffset must be positive: %d < 0", sourceOffset);
			throw new ArrayIndexOutOfBoundsException(msg);
		}
		
		if (sourceOffset + count > source.length)
		{
			String msg = String.format(
					"sourceOffset + count must not exceed the source length: %d > %d", sourceOffset,
					source.length);
			throw new ArrayIndexOutOfBoundsException(msg);
		}
		
		if (destOffset < 0)
		{
			String msg = String.format("destOffset must be positive: %d < 0", destOffset);
			throw new ArrayIndexOutOfBoundsException(msg);
		}
		
		if (destOffset + count > getArraySize())
		{
			String msg = String.format(
					"destOffset + count must not exceed the destination length: %d > %d", getArraySize(),
					source.length);
			throw new ArrayIndexOutOfBoundsException(msg);
		}
	}


	/**
	 * @param index The index of the element to fetch.
	 * @return The value at the specified index in the array referenced by this object. If
	 *         the class <code>T</code> does not have a <code>T(Pointer)</code> constructor,
	 *         you may need to manually call {@link Structure#read()} on the element before
	 *         it contains its actual data.
	 */
	public T getElement(int index)
	{
		if (index < 0 || index >= arraySize)
		{
			throw new ArrayIndexOutOfBoundsException(index);
		}
		
		return Structure.newInstance(clazz, share((long)index * elementSize));
	}


	/**
	 * @param array The array into which the elements of this list are to be stored, if it
	 *          is big enough; otherwise, a new array of the same runtime type is allocated
	 *          for this purpose.
	 * @return An array containing the data referenced by this object. If the class
	 *         <code>T</code> does not have a <code>T(Pointer)</code> constructor, you may
	 *         need to manually call {@link Structure#read()} on each element before it
	 *         contains its actual data.
	 */
	public T[] getElements(T[] array)
	{
		List<T> list = new ArrayList<>(arraySize);

		for (int i = 0; i < arraySize; i++)
		{
			T element = getElement(i);
			list.add(element);
		}

		return list.toArray(array);
	}
	
	
	@Override
	public int hashCode()
	{
		int elementHash = 1;
		for (int i = 0; i < arraySize; i++)
		{
			elementHash = 31 * elementHash + getElement(i).hashCode();
		}
		
		return Objects.hash(arraySize, elementSize, clazz, elementHash);
	}
	
	
	/**
	 * <p>
	 * Checks whether this <code>ArrayPointer</code> is equal to another object.
	 * <code>other</code> is considered equal if and only if it is an
	 * <code>ArrayPointer</code> with the same array size, element size and
	 * element type, and its elements are equal to the elements in this
	 * <code>ArrayPointer</code>.
	 * </p>
	 * <p>
	 * Element equality is tested using the elements' <code>equals()</code>
	 * methods.
	 * </p>
	 * <p>
	 * Use {@link #shallowEquals(Object)} instead if you want to compare the
	 * native peer values (i.e. memory addresses) instead of the actual elements.
	 * </p>
	 */
	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof ArrayPointer))
		{
			return false;
		}

		ArrayPointer<?> otherArray = (ArrayPointer<?>) other;

		if (other != this)
		{
			if (otherArray.clazz != clazz || otherArray.elementSize != elementSize
					|| otherArray.arraySize != arraySize)
			{
				return false;
			}

			for (int i = 0; i < arraySize; i++)
			{
				if (!getElement(i).equals(otherArray.getElement(i)))
				{
					return false;
				}
			}
		}

		return true;
	}
	
	
	/**
	 * <p>
	 * Checks whether this <code>ArrayPointer</code> is "shallowly" equal to
	 * another object. <code>other</code> is considered "shallowly" equal if and
	 * only if it is an <code>ArrayPointer</code> with the same array size,
	 * element size, element type and native peer value.
	 * </p>
	 * 
	 * @param other The reference object with which to compare.
	 * @return <code>true</code> if this object is the same as the other object;
	 *         <code>false</code> otherwise.
	 */
	public boolean shallowEquals(Object other)
	{
		if (other instanceof ArrayPointer)
		{
			ArrayPointer<?> otherArray = (ArrayPointer<?>) other;

			if (other == this)
			{
				return true;
			}
			if (otherArray.clazz == clazz && otherArray.elementSize == elementSize
					&& otherArray.arraySize == arraySize && otherArray.peer == peer)
			{
				return true;
			}
		}

		return false;
	}


	@Override
	public synchronized void dispose()
	{
		super.dispose();
	}
}
