package komposten.leapjna.leapc.enums;

import komposten.leapjna.LeapC;


/**
 * The types of event messages resulting from calling
 * {@link LeapC#LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)}.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#ga04f93b375f7c8040178ec5be2bf658ec">LeapC
 *      API - eLeapEventType</a>
 */
public enum eLeapEventType
{
	None(0x000),
	Connection(0x001),
	ConnectionLost(0x002),
	Device(0x003),
	DeviceFailure(0x004),
	Policy(0x005),
	Tracking(0x100),
	ImageRequestError(0x101),
	ImageComplete(0x102),
	LogEvent(0x103),
	DeviceLost(0x104),
	ConfigResponse(0x105),
	ConfigChange(0x106),
	DeviceStatusChange(0x107),
	DroppedFrame(0x108),
	Image(0x109),
	PointMappingChange(0x10A),
	LogEvents(0x10B),
	HeadPose(0x10C);
	
	public final int value;

	private eLeapEventType(int value)
	{
		this.value = value;
	}
	
	
	public static eLeapEventType parse(int value, eLeapEventType defaultValue)
	{
		for (eLeapEventType o : eLeapEventType.values())
		{
			if (o.value == value)
			{
				return o;
			}
		}
		
		return defaultValue;
	}
}