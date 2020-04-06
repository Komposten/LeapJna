package komposten.leapjna.leapc.enums;

/**
 * Device hardware types.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#ga5ed369865afe29bdadc65c450eb44c75">LeapC
 *      API - eLeapDevicePID</a>
 */
public enum eLeapDevicePID
{
	/** An unknown device. */
	Unknown(0x0000),

	/** The Leap Motion consumer peripheral. */
	Peripheral(0x0003),

	/** Internal research product codename "Dragonfly". */
	Dragonfly(0x1102),

	/** Internal research product codename "Nightcrawler". */
	Nightcrawler(0x1201),

	/** Research product codename "Rigel". */
	Rigel(0x1202),

	/** An invalid device type value. */
	Invalid(0xFFFFFFFF);

	public final int value;

	private eLeapDevicePID(int value)
	{
		this.value = value;
	}


	public static eLeapDevicePID parse(int value, eLeapDevicePID defaultValue)
	{
		for (eLeapDevicePID o : eLeapDevicePID.values())
		{
			if (o.value == value)
			{
				return o;
			}
		}

		return defaultValue;
	}
}