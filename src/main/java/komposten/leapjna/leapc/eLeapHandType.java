package komposten.leapjna.leapc;

import komposten.leapjna.util.JnaEnum;

/**
 * @author Jakob Hjelm
 * @see https://developer.leapmotion.com/documentation/v4/group___enum.html#ga10647f52cdf6742a654aab0054ce3d3e
 */
public enum eLeapHandType implements JnaEnum<eLeapHandType>
{
	Left(0x000),
	Right(0x001);
	
	private final int value;

	private eLeapHandType(int value)
	{
		this.value = value;
	}


	@Override
	public int getIntValue()
	{
		return value;
	}


	@Override
	public eLeapHandType getForValue(int value)
	{
		for (eLeapHandType o : eLeapHandType.values())
		{
			if (o.getIntValue() == value)
			{
				return o;
			}
		}
		
		return null;
	}
}