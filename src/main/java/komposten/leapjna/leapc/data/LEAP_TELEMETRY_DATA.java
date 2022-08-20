/*
 * Copyright 2020-2022 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.data;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;


/**
 * Holds telemetry configuration information.
 * 
 * @deprecated
 * @since LeapJna 1.0.0
 */
@Deprecated
@FieldOrder({ "thread_id", "start_time", "end_time", "zone_depth", "file_name",
		"line_number", "zone_name" })
public class LEAP_TELEMETRY_DATA extends Structure
{
	public int thread_id;
	public long start_time;
	public long end_time;
	public int zone_depth;
	public String file_name;
	public int line_number;
	public String zone_name;
	
	public LEAP_TELEMETRY_DATA()
	{
		super(ALIGN_NONE);
	}
}
