package komposten.leapjna.leapc;

import komposten.leapjna.util.JnaEnum;

/**
 * @author Jakob Hjelm
 * @see https://developer.leapmotion.com/documentation/v4/group___enum.html#ga10647f52cdf6742a654aab0054ce3d3e
 */
public enum eLeapConnectionStatus implements JnaEnum<eLeapConnectionStatus>
{
	NotConnected(0),
	Connected(1),
	HandshakeIncomplete(2),
	NotRunning(0xE7030004);
	
	private final int value;

	private eLeapConnectionStatus(int value)
	{
		this.value = value;
	}


	@Override
	public int getIntValue()
	{
		return value;
	}


	@Override
	public eLeapConnectionStatus getForValue(int value)
	{
		for (eLeapConnectionStatus o : eLeapConnectionStatus.values())
		{
			if (o.getIntValue() == value)
			{
				return o;
			}
		}
		
		return null;
	}
}