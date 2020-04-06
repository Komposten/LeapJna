package komposten.leapjna.leapc.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;


public class ArrayByReference<T extends Structure> extends Memory
{
	private int elementSize;
	private int size;
	private Class<T> clazz;

	@SuppressWarnings("unchecked")
	public ArrayByReference(T structure, int arraySize)
	{
		this((Class<T>) structure.getClass(), structure.size(), arraySize);
	}


	public ArrayByReference(Class<T> clazz, int elementSize, int arraySize)
	{
		super(arraySize * elementSize);
		this.elementSize = elementSize;
		this.clazz = clazz;
		size = arraySize;
	}


	public ArrayByReference(Class<T> clazz, int elementSize, T... values)
	{
		super(values.length * elementSize);
		this.elementSize = elementSize;
		this.clazz = clazz;
		size = values.length;
		setValues(0, values);
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
	public void setValues(int offset, T... values)
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
}
