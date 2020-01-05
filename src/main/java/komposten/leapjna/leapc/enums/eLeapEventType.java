package komposten.leapjna.leapc.enums;

/**
 * @author Jakob Hjelm
 * @see https://developer.leapmotion.com/documentation/v4/group___enum.html#ga04f93b375f7c8040178ec5be2bf658ec
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
	
	public final short value;

	private eLeapEventType(int value)
	{
		this.value = (short)value;
	}
	
	
	public static eLeapEventType parse(short value, eLeapEventType defaultValue)
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