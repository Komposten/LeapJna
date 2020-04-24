package komposten.leapjna.leapc.enums;

import komposten.leapjna.leapc.enums.Enums.IntEnum;


/**
 * Camera perspective types.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#ga2bbde1c3af778eba35f0b1a8cf1338c2">LeapC
 *      API - eLeapPerspectiveType</a>
 */
public enum eLeapPerspectiveType implements IntEnum
{
	/** An unknown or invalid type. */
	Invalid(0),

	/** A canonically left image. */
	Stereo_left(1),

	/** A canonically right image. */
	Stereo_right(2),

	/** Reserved for future use. */
	Mono(3);

	public final int value;

	private eLeapPerspectiveType(int value)
	{
		this.value = value;
	}


	@Override
	public int getValue()
	{
		return value;
	}
}