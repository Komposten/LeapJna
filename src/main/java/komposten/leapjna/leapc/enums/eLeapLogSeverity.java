package komposten.leapjna.leapc.enums;

/**
 * System message severity types.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#ga03d3b2203fa8af12c5436b6974a10fbb">LeapC
 *      API - eLeapLogSeverity</a>
 */
public enum eLeapLogSeverity
{
	/** The message severity is not known or was not specified. */
	Unknown(0),

	/** A message about a fault that could render the software or device non-functional. */
	Critical(1),

	/** A message warning about a condition that could degrade device capabilities. */
	Warning(2),

	/** A system status message. */
	Information(3);

	public final int value;

	private eLeapLogSeverity(int value)
	{
		this.value = value;
	}


	public static eLeapLogSeverity parse(int value, eLeapLogSeverity defaultValue)
	{
		for (eLeapLogSeverity o : eLeapLogSeverity.values())
		{
			if (o.value == value)
			{
				return o;
			}
		}

		return defaultValue;
	}
}
