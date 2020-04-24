package komposten.leapjna.leapc.enums;

import komposten.leapjna.leapc.enums.Enums.IntEnum;


public enum eLeapDroppedFrameType implements IntEnum
{
	Unknown(-1), PreprocessingQueue(0), TrackingQueue(1), Other(2);

	public final int value;

	private eLeapDroppedFrameType(int value)
	{
		this.value = value;
	}


	@Override
	public int getValue()
	{
		return value;
	}
}
