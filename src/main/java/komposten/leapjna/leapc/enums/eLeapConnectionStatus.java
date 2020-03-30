package komposten.leapjna.leapc.enums;

import komposten.leapjna.LeapC;
import komposten.leapjna.leapc.data.LEAP_CONNECTION_INFO;


/**
 * <p>
 * The connection status codes.
 * </p>
 * <p>
 * These codes can be read from the {@link LEAP_CONNECTION_INFO} struct created by a call
 * to {@link LeapC#LeapGetConnectionInfo(Pointer, LEAP_CONNECTION_INFO.ByReference)}
 * </p>
 */
public enum eLeapConnectionStatus
{
	NotConnected(0),
	Connected(1),
	HandshakeIncomplete(2),
	NotRunning(0xE7030004),
	Unknown(-1);
	
	public final int value;

	private eLeapConnectionStatus(int value)
	{
		this.value = value;
	}
	

	public static eLeapConnectionStatus parse(int value,
			eLeapConnectionStatus defaultStatus)
	{
		switch (value)
		{
			case 0 :
				return NotConnected;
			case 1 :
				return Connected;
			case 2 :
				return HandshakeIncomplete;
			case 0xE7030004 :
				return NotRunning;
			default :
				return defaultStatus;
		}
	}
}