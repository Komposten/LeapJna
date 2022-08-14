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

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;


/**
 * <p>
 * Holds an image and related information from one of the UltraLeap Tracking device's cameras.
 * </p>
 */
@FieldOrder({ "properties", "matrix_version", "distortion_matrix", "data", "offset" })
public class LEAP_IMAGE extends Structure
{
	/** The properties of the received image. */
	public LEAP_IMAGE_PROPERTIES properties;

	/**
	 * <p>
	 * A version number for the distortion matrix. When the distortion matrix changes, this
	 * number is updated. This number is guaranteed not to repeat for the lifetime of the
	 * connection. This version number is also guaranteed to be distinct for each
	 * perspective of an image.
	 * </p>
	 * <p>
	 * This value is guaranteed to be nonzero if it is valid.
	 * </p>
	 * <p>
	 * The distortion matrix only changes when the streaming device changes or when the
	 * device orientation flips -- inverting the image and the distortion grid. Since
	 * building a matrix to undistort an image can be a time-consuming task, you can
	 * optimise the process by only rebuilding this matrix (or whatever data type you use to
	 * correct image distortion) when the grid actually changes.
	 * </p>
	 */
	public long matrix_version;

	/**
	 * A pointer to the camera's distortion matrix. Use {@link #getMatrix()} to obtain the
	 * actual matrix.
	 */
	public Pointer distortion_matrix;

	/** A pointer to the image data. Use {@link #getData()} to obtain the actual data. */
	public Pointer data;

	/**
	 * Offset, in bytes, from the beginning of the data pointer to the actual beginning of
	 * the image data.
	 */
	public int offset;

	private byte[] imageData;

	private LEAP_DISTORTION_MATRIX matrixData;

	public LEAP_IMAGE()
	{
		super(ALIGN_NONE);
	}


	/**
	 * <p>
	 * The image data is not loaded from native memory until this method is called. This
	 * means that the data might overridden or freed if this method is not called
	 * immediately after receiving the <code>LEAP_IMAGE</code>.
	 * </p>
	 * <p>
	 * After {@link #getData()} has been called once, successive calls will return
	 * a cached array rather than load the data from native memory every time.
	 * </p>
	 * 
	 * @return The image data as a byte array.
	 */
	public byte[] getData()
	{
		if (imageData == null)
		{
			imageData = data.getByteArray(offset,
					properties.width * properties.height * properties.bpp);
		}
		return imageData;
	}


	/**
	 * <p>
	 * The first call to this method on a <code>LEAP_IMAGE</code> instance reads the
	 * distortion matrix from memory and creates a {@link LEAP_DISTORTION_MATRIX} instance.
	 * </p>
	 * <p>
	 * <b>NOTE</b>: For performance reasons it is best to only use this method if
	 * {@link #matrix_version} has changed since you last read a distortion matrix!
	 * </p>
	 * 
	 * @return A {@link LEAP_DISTORTION_MATRIX} containing the matrix.
	 */
	public LEAP_DISTORTION_MATRIX getMatrix()
	{
		if (matrixData == null)
		{
			matrixData = new LEAP_DISTORTION_MATRIX(distortion_matrix);
		}

		return matrixData;
	}
}
