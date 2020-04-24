package komposten.leapjna.leapc.enums;

import komposten.leapjna.leapc.enums.Enums.IntEnum;

public enum eLeapServiceDisposition implements IntEnum
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
	
	
	@Override
	public int getValue()
	{
		return value;
	}
}