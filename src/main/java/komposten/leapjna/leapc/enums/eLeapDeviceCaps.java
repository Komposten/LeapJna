package komposten.leapjna.leapc.enums;

import komposten.leapjna.leapc.enums.Enums.IntFlagEnum;


/**
 * Flags enumerating Leap device capabilities.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#ga5d5cfae19b88160a1a5c14cc46faf435">LeapC
 *      API - eLeapDeviceCaps</a>
 */
public enum eLeapDeviceCaps implements IntFlagEnum<eLeapDeviceCaps>
{
	/** The device has no specific capabilities. */
	None(0x00000000),

	/** The device can send color images. */
	Color(0x00000001);

	public final int value;

	private eLeapDeviceCaps(int value)
	{
		this.value = value;
	}


	@Override
	public int getValue()
	{
		return value;
	}


	@Override
	public eLeapDeviceCaps getEmptyMaskConstant()
	{
		return None;
	}


	public static int createMask(eLeapDeviceCaps... flags)
	{
		return Enums.createMask(flags);
	}
}