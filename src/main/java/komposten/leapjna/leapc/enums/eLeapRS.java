package komposten.leapjna.leapc.enums;

import komposten.leapjna.util.JnaEnum;


/**
 * Codes returned by all LeapC functions.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#ga10647f52cdf6742a654aab0054ce3d3e">LeapC
 *      API - eLeapRS</a>
 */
public enum eLeapRS implements JnaEnum<eLeapRS>
{
	Success(0),
	UnknownError(0xE2010000),
	InvalidArgument(0xE2010001),
	InsufficientResources(0xE2010002),
	InsufficientBuffer(0xE2010003),
	Timeout(0xE2010004),
	NotConnected(0xE2010005),
	HandshakeIncomplete(0xE2010006),
	BufferSizeOverflow(0xE2010007),
	ProtocolError(0xE2010008),
	InvalidClientID(0xE2010009),
	UnexpectedClosed(0xE201000A),
	UnknownImageFrameRequest(0xE201000B),
	UnknownTrackingFrameID(0xE201000C),
	RoutineIsNotSeer(0xE201000D),
	TimestampTooEarly(0xE201000E),
	ConcurrentPoll(0xE201000F),
	NotAvailable(0xE7010002),
	NotStreaming(0xE7010004),
	CannotOpenDevice(0xE7010005);

	private final int value;

	private eLeapRS(int value)
	{
		this.value = value;
	}


	@Override
	public int getIntValue()
	{
		return value;
	}


	@Override
	public eLeapRS getForValue(int value)
	{
		for (eLeapRS o : eLeapRS.values())
		{
			if (o.getIntValue() == value)
			{
				return o;
			}
		}

		return null;
	}
}