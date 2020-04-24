package komposten.leapjna.leapc.enums;

import komposten.leapjna.leapc.enums.Enums.IntEnum;

public enum eLeapValueType implements IntEnum
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
	
	
	@Override
	public int getValue()
	{
		return value;
	}
}
