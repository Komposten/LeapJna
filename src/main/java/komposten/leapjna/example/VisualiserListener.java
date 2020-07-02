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