package komposten.leapjna.leapc.enums;

public enum eLeapDroppedFrameType
{
	Unknown(-1),
	PreprocessingQueue(0),
	TrackingQueue(1),
	Other(2);

	public final int value;

	private eLeapDroppedFrameType(int value)
	{
		this.value = value;
	}


	public static eLeapDroppedFrameType parse(int value, eLeapDroppedFrameType defaultValue)
	{
		for (eLeapDroppedFrameType o : eLeapDroppedFrameType.values())
		{
			if (o.value == value)
			{
				return o;
			}
		}

		return defaultValue;
	}
}
