package komposten.leapjna.leapc.enums;

import komposten.leapjna.leapc.data.LEAP_HAND;
import komposten.leapjna.leapc.enums.Enums.ByteEnum;


/**
 * <p>
 * The hand chirality types.
 * </p>
 * <p>
 * Used in the {@link LEAP_HAND} struct.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#ga6d751aedb178355c21ec1cac4706e044">LeapC
 *      API - eLeapHandType</a>
 */
public enum eLeapHandType implements ByteEnum
{
	Left((byte) 0x000),
	Right((byte) 0x001),
	Unknown((byte) -0x001);

	public final byte value;

	private eLeapHandType(byte value)
	{
		this.value = value;
	}


	@Override
	public byte getValue()
	{
		return value;
	}
}