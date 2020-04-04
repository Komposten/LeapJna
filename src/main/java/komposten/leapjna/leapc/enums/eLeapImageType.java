package komposten.leapjna.leapc.enums;

/**
 * Functional image types (not data formats).
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#gab85c92adbf1b49d95cf472defadbaf50">LeapC
 *      API - eLeapImageType</a>
 */
public enum eLeapImageType
{
	/** An invalid or unknown type. */
	Unknown(0),

	/** Default, processed IR images. */
	Default(1),

	/** Raw images from the device. */
	Raw(2);

	public final int value;

	private eLeapImageType(int value)
	{
		this.value = value;
	}


	public static eLeapImageType parse(int value, eLeapImageType defaultValue)
	{
		for (eLeapImageType o : eLeapImageType.values())
		{
			if (o.value == value)
			{
				return o;
			}
		}

		return defaultValue;
	}
}
