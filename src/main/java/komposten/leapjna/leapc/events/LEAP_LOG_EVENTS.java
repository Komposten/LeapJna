/*
 * Copyright 2020 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.events;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;


/**
 * A group of system log messages.
 */
@FieldOrder({ "nEvents", "events" })
public class LEAP_LOG_EVENTS extends Structure implements LEAP_EVENT
{
	/** The number of log events being pointed to by the <code>events</code> field. */
	public int nEvents;

	/** A pointer to an array of {@link LEAP_LOG_EVENT}s. */
	public Pointer events;

	private LEAP_LOG_EVENT[] logEvents;

	public LEAP_LOG_EVENTS(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}


	/**
	 * @return The array of {@link LEAP_LOG_EVENT} structs pointed to by {@link #events}.
	 */
	public LEAP_LOG_EVENT[] getEvents()
	{
		return logEvents;
	}


	@Override
	public void read()
	{
		super.read();

		logEvents = new LEAP_LOG_EVENT[nEvents];

		int offset = 0;
		for (int i = 0; i < nEvents; i++)
		{
			logEvents[i] = new LEAP_LOG_EVENT(events.share(offset));
			offset += logEvents[i].size();
		}
	}
}
