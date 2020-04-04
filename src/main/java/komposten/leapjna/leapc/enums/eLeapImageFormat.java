package komposten.leapjna.leapc.enums;

/**
 * Image formats.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#ga88575674641d03abef07b3a6712c0e95">LeapC
 *      API - eLeapImageFormat</a>
 */
public enum eLeapImageFormat
{
	/** An invalid or unknown format. */
	Unknown(0),

	/** An infrared image. */
	IR(0x317249),

	/** A Bayer RGBIr image with uncorrected RGB channels. */
	RGBIr(0x49425247);

	public final int value;

	private eLeapImageFormat(int value)
	{
		this.value = value;
	}


	public static eLeapImageFormat parse(int value, eLeapImageFormat defaultValue)
	{
		for (eLeapImageFormat o : eLeapImageFormat.values())
		{
			if (o.value == value)
			{
				return o;
			}
		}

		return defaultValue;
	}
}
