package komposten.leapjna.leapc.enums;

/**
 * The device status codes.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#ga1d2b70b83923751202242cbfcf56072b">LeapC
 *      API - eLeapDeviceStatus</a>
 */
public enum eLeapDeviceStatus
{
	Unknown(-0x1),
	
	/** The device is sending out frames. */
	Streaming(0x00000001),

	/** Device streaming has been paused. */
	Paused(0x00000002),

	/**
	 * There are known sources of infrared interference. Device has transitioned to robust
	 * mode in order to compensate.
	 */
	Robust(0x00000004),

	/** The device's window is smudged, tracking may be degraded. */
	Smudged(0x00000008),

	/** The device has entered low-resource mode. */
	LowResource(0x00000010),

	/** The device has failed, but the failure reason is not known. */
	UnknownFailure(0xE8010000),

	/** The device has a bad calibration record and cannot send frames. */
	BadCalibration(0xE8010001),

	/** The device reports corrupt firmware or cannot install a required firmware update. */
	BadFirmware(0xE8010002),

	/** The device USB connection is faulty. */
	BadTransport(0xE8010003),

	/** The device USB control interfaces failed to initialise. */
	BadControl(0xE8010004);

	public final int value;

	private eLeapDeviceStatus(int value)
	{
		this.value = value;
	}


	public static eLeapDeviceStatus parse(int value, eLeapDeviceStatus defaultValue)
	{
		for (eLeapDeviceStatus o : eLeapDeviceStatus.values())
		{
			if (o.value == value)
			{
				return o;
			}
		}

		return defaultValue;
	}
}