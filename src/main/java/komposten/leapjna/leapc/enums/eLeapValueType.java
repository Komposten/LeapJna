package komposten.leapjna.leapc.enums;

public enum eLeapValueType
{
	/** The type is unknown (which is an abnormal condition). */
	Unknown(0),
	Boolean(1),
	Int32(2),
	Float(3),
	String(4);

	public final int value;

	private eLeapValueType(int value)
	{
		this.value = value;
	}


	public static eLeapValueType parse(int value, eLeapValueType defaultValue)
	{
		for (eLeapValueType o : eLeapValueType.values())
		{
			if (o.value == value)
			{
				return o;
			}
		}

		return defaultValue;
	}
}
