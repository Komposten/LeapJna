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

public class LeapException extends RuntimeException
{
	public LeapException()
	{}


	public LeapException(String message)
	{
		super(message);
	}


	public LeapException(Throwable cause)
	{
		super(cause);
	}


	public LeapException(String message, Throwable cause)
	{
		super(message, cause);
	}


	public LeapException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
