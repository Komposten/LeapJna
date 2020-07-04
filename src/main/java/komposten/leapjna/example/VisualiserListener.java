/*
 * Copyright 2020 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.example;

import komposten.leapjna.example.VisualiserBackend.State;
import komposten.leapjna.leapc.events.LEAP_IMAGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_TRACKING_EVENT;


interface VisualiserListener
{
	public enum LogType
	{
		NORMAL, HEADER, ERROR, SEPARATOR
	}

	void onStateChanged(State state);


	void onLogMessage(LogType type, String message, Object... args);


	void onFrame(LEAP_TRACKING_EVENT frameEvent);


	void onImage(LEAP_IMAGE_EVENT imageEvent);
}