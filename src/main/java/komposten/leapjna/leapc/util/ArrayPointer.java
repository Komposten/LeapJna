package komposten.leapjna.leapc.util;

import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Memory;
import com.sun.jna.Structure;


/**
 * <p>
 * A pointer to a memory block containing one or more instances of a
 * <em>constant-size</em> {@link Structure}.
 * </p>
 * <p>
 * In order for this class to work properly the structure class stored by this array must
 * have a public no-arg constructor, a public constructor taking a single Pointer as
 * argument, and a constant size.
 * </p>
 * 
 * 
 * @param <T> The structure type contained in the array.
 */
public class ArrayPointer<T extends Structure> extends Memory implements Disposable
{
	private int elementSize;
	private int size;
	private Class<T> clazz;

	/**
	 * <p>
	 * Creates an empty <code>ArrayByReference</code> based on the provided type and array
	 * size.
	 * </p>
	 * <p>
	 * <b>Note</b>: All elements will be assumed to have the same size, calculated using
	 * {@link Structure#size()}!
	 * </p>
	 * 
	 * @param clazz The type to store in the array.
	 * @param arraySize The number of elements to allocate space for.
	 */
	public static <T extends Structure> ArrayPointer<T> empty(Class<T> clazz, int arraySize)
	{
		int elementSize = Structure.newInstance(clazz).size();
		return new ArrayPointer<>(clazz, elementSize, arraySize);
	}


	/**
	 * <p>
	 * Creates an <code>ArrayByReference</code> based on the provided array.
	 * </p>
	 * <p>
	 * <b>Note</b>: All elements will be assumed to have the same size, calculated using
	 * {@link Structure#size()} on the first element in <code>values</code>!
	 * </p>
	 * 
	 * @param values The elements to store in the array.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Structure> ArrayPointer<T> fromArray(T[] values)
	{
		if (values.length < 0)
		{
			throw new IllegalArgumentException("elements must have at least one element!");
		}

		ArrayPointer<T> result = new ArrayPointer<T>((Class<T>) values[0].getClass(),
				values[0].size(), values.length);
		result.setValues(0, values);

		return result;
	}


	/**
	 * <p>
	 * Creates a new <code>ArrayByReference</code> based on the provided type.
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
		super(arraySize * elementSize);
		this.elementSize = elementSize;
		this.clazz = clazz;
		size = arraySize;
	}


	/**
	 * @return The size of the array referenced by this object.
	 */
	public int getArraySize()
	{
		return size;
	}


	/**
	 * Updates a single element in the array referenced by this object.
	 * 
	 * @param index The array index of the element to update.
	 * @param value The new value.
	 * @throws ArrayIndexOutOfBoundsException If the index is negative or
	 *           <code>>= </code>{@link #getArraySize()}.
	 */
	public void setElement(int index, T value)
	{
		if (index < 0 || index >= size)
		{
			throw new ArrayIndexOutOfBoundsException(index);
		}

		byte[] buffer = new byte[elementSize];
		value.getPointer().read(0, buffer, 0, elementSize);
		write(index * elementSize, buffer, 0, elementSize);
	}


	/**
	 * Copies the provided elements into the array referenced by this object.
	 * 
	 * @param offset The array index to copy the elements to.
	 * @param values The new values.
	 * @throws ArrayIndexOutOfBoundsException If the offset is negative or the offset plus
	 *           the array size is larger than {@link #getArraySize()}.
	 */
	public void setValues(int offset, T[] values)
	{
		if (offset < 0)
		{
			String msg = String.format("The offset must be zero or positive: %d < 0", offset);
			throw new ArrayIndexOutOfBoundsException(msg);
		}

		if (offset + values.length > size)
		{
			String msg = String.format(
					"offset + values.length cannot be larger than the array size: %d > %d",
					offset + values.length, size);
			throw new ArrayIndexOutOfBoundsException(msg);
		}

		byte[] buffer = new byte[elementSize];
		for (int i = 0; i < values.length; i++)
		{
			values[i].getPointer().read(0, buffer, 0, elementSize);
			write((offset + i) * elementSize, buffer, 0, elementSize);
		}
	}


	/**
	 * @param index The index of the element to fetch.
	 * @return The value at the specified index in the array referenced by this object.
	 */
	public T getElement(int index)
	{
		return Structure.newInstance(clazz, share(index * elementSize));
	}


	/**
	 * @param array The array into which the elements of this list are to be stored, if it
	 *          is big enough; otherwise, a new array of the same runtime type is allocated
	 *          for this purpose.
	 * @return An array containing the data referenced by this object.
	 */
	public T[] getValues(T[] array)
	{
		List<T> list = new ArrayList<>(size);

		for (int i = 0; i < size; i++)
		{
			list.add(getElement(i));
		}

		return list.toArray(array);
	}


	@Override
	public synchronized void dispose()
	{
		super.dispose();
	}
}
