package komposten.leapjna.leapc.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;


public class ArrayByReference<T extends Structure> extends Memory implements Disposable
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
	public static <T extends Structure> ArrayByReference<T> empty(Class<T> clazz,
			int arraySize)
	{
		int elementSize = Structure.newInstance(clazz).size();
		return new ArrayByReference<>(clazz, elementSize, arraySize);
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
	public static <T extends Structure> ArrayByReference<T> fromArray(T[] values)
	{
		if (values.length < 0)
		{
			throw new IllegalArgumentException("elements must have at least one element!");
		}

		ArrayByReference<T> result = new ArrayByReference<T>((Class<T>) values[0].getClass(),
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
	private ArrayByReference(Class<T> clazz, int elementSize, int arraySize)
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
		return createInstance(share(index * elementSize));
	}


	/**
	 * @return The array referenced by this object.
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


	private T createInstance(Pointer pointer)
	{
		Constructor<T> ctor;
		try
		{
			ctor = clazz.getDeclaredConstructor(Pointer.class);
			return ctor.newInstance(pointer);
		}
		catch (NoSuchMethodException | IllegalAccessException e)
		{
			String msg = "The structure class " + clazz.getName()
					+ " must have a public constructor with the signature " + clazz.getSimpleName()
					+ "(Pointer)!";
			throw new LeapException(msg, e);
		}
		catch (InstantiationException e)
		{
			String msg = "The structure class " + clazz.getName() + " must not be abstract!";
			throw new LeapException(msg, e);
		}
		catch (IllegalArgumentException e)
		{
			throw new LeapException("This should not happen.", e);
		}
		catch (InvocationTargetException e)
		{
			String msg = "A exception occurred while creating a new " + clazz.getName()
					+ " instance!";
			throw new LeapException(msg, e);
		}
	}


	@Override
	public synchronized void dispose()
	{
		super.dispose();
	}
}
