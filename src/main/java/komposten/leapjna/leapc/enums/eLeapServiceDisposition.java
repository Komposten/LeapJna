package komposten.leapjna.leapc.enums;

public enum eLeapServiceDisposition
{
	/** No flags. */
	None(0),
	
	/** The service cannot receive frames fast enough from the underlying hardware. */
	LowFpsDetected(1),

	/**
	 * The service has paused itself due to an insufficient frame rate from the hardware.
	 */
	PoorPerformancePause(2),

	/** The combination of all valid flags in this enumeration. */
	All(1 | 2);

	public final int value;

	private eLeapServiceDisposition(int value)
	{
		this.value = value;
	}


	public static eLeapServiceDisposition parse(int value,
			eLeapServiceDisposition defaultStatus)
	{
		switch (value)
		{
			case 0 :
				return None;
			case 1 :
				return LowFpsDetected;
			case 2 :
				return PoorPerformancePause;
			case (1 | 2) :
				return All;
			default :
				return defaultStatus;
		}
	}
}