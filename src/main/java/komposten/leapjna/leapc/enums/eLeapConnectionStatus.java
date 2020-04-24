package komposten.leapjna.leapc.enums;

import com.sun.jna.Pointer;

import komposten.leapjna.LeapC;
import komposten.leapjna.leapc.data.LEAP_CONNECTION_INFO;
import komposten.leapjna.leapc.enums.Enums.IntEnum;


/**
 * <p>
 * The connection status codes.
 * </p>
 * <p>
 * These codes can be read from the {@link LEAP_CONNECTION_INFO} struct created by a call
 * to {@link LeapC#LeapGetConnectionInfo(Pointer, LEAP_CONNECTION_INFO.ByReference)}
 * </p>
 */
public enum eLeapConnectionStatus implements IntEnum
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


	@Override
	public int getValue()
	{
		return value;
	}
}