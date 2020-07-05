/*
 * Copyright 2020 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.data;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.leapc.enums.eLeapAllocatorType;


/**
 * Specifies the allocator/deallocator functions to be used when the library needs to
 * dynamically manage memory.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___a_l_l_o_c_a_t_o_r">LeapC
 *      API - LEAP_ALLOCATOR</a>
 */
@FieldOrder({ "allocate", "deallocate", "state" })
public class LEAP_ALLOCATOR extends Structure
{
	/**
	 * A callback for allocating native memory.
	 */
	public static interface allocate extends Callback
	{
		/**
		 * @param size The amount of bytes to allocate.
		 * @param typeHint The data buffer type. One of the
		 *          {@link eLeapAllocatorType} constants.
		 * @param state [undocumented]
		 * @return A pointer to the allocated data buffer.
		 */
		Pointer invoke(int size, int typeHint, Pointer state);
	}

	/**
	 * A callback for freeing previously allocated native memory.
	 */
	public static interface deallocate extends Callback
	{
		/**
		 * @param ptr A pointer to the data buffer to deallocate.
		 * @param state [undocumented]
		 */
		void invoke(Pointer ptr, Pointer state);
	}

	/**
	 * <p>
	 * Function pointer to an allocator function that is expected to return a pointer to
	 * memory of at least the specified size in bytes.
	 * </p>
	 * <p>
	 * This will be called when the library needs a block of memory that will be provided
	 * back to the client in a subsequent event or response. A type hint is provided in the
	 * case where the underlying buffer type needs to be known at allocation time.
	 * </p>
	 */
	public allocate allocate;

	/**
	 * <p>
	 * Function pointer to a deallocator function.
	 * </p>
	 * <p>
	 * The function receives the address of a previously allocated block of memory from the
	 * {@link #allocate} function pointer. The caller is not required to deallocate the
	 * memory, but rather this call is used by the library to indicate to the client that it
	 * will no longer reference the memory at this address, and that the callee may
	 * deallocate the memory when it is ready to do so.
	 * </p>
	 */
	public deallocate deallocate;

	/** Pointer to state to be passed to the allocate and deallocate functions. */
	public Pointer state;


	public LEAP_ALLOCATOR(allocate allocate, deallocate deallocate)
	{
		super(ALIGN_NONE);
		this.allocate = allocate;
		this.deallocate = deallocate;
		write();
	}
}
