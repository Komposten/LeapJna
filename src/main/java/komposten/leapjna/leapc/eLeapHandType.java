package komposten.leapjna.leapc;

/**
 * @author Jakob Hjelm
 * @see https://developer.leapmotion.com/documentation/v4/group___enum.html#ga6d751aedb178355c21ec1cac4706e044
 */
public enum eLeapHandType
{
	Left((byte) 0x000),
	Right((byte) 0x001),
	Unknown((byte) -0x001);
	
	public final byte value;

	private eLeapHandType(byte value)
	{
		this.value = value;
	}
	
	
	public static eLeapHandType parse(byte value, eLeapHandType defaultType)
	{
		switch (value)
		{
			case 0x000 :
				return Left;
			case 0x001 :
				return Left;
			default :
				return defaultType;
		}
	}
}