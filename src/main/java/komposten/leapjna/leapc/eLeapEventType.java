package komposten.leapjna.leapc;

import komposten.leapjna.util.JnaShortEnum;

/**
 * @author Jakob Hjelm
 * @see https://developer.leapmotion.com/documentation/v4/group___enum.html#ga10647f52cdf6742a654aab0054ce3d3e
 */
public enum eLeapEventType implements JnaShortEnum<eLeapEventType>
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
	
	private final short value;

	private eLeapEventType(int value)
	{
		this.value = (short)value;
	}


	@Override
	public short getShortValue()
	{
		return value;
	}


	@Override
	public eLeapEventType getForValue(short value)
	{
		for (eLeapEventType o : eLeapEventType.values())
		{
			if (o.getShortValue() == value)
			{
				return o;
			}
		}
		
		return null;
	}
}