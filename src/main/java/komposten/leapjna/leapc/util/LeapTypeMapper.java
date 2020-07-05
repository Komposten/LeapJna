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

import com.sun.jna.DefaultTypeMapper;

import komposten.leapjna.leapc.enums.eLeapRS;


/**
 * A type mapper for internal use by LeapJna to properly map certain native
 * types to appropriate Java types.
 */
public class LeapTypeMapper extends DefaultTypeMapper
{
	public LeapTypeMapper()
	{
		addTypeConverter(eLeapRS.class, new eLeapRSConverter());
	}
}
