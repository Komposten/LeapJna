/*
 * Copyright 2020 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.enums;

import java.util.ArrayList;
import java.util.List;

import komposten.leapjna.leapc.enums.Enums.IntFlagEnum;


/**
 * The device status codes.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#ga1d2b70b83923751202242cbfcf56072b">LeapC
 *      API - eLeapDeviceStatus</a>
 */
public enum eLeapDeviceStatus implements IntFlagEnum<eLeapDeviceStatus>
{
	None(0x0),

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

	private static final eLeapDeviceStatus[] FUNCTIONAL = { Streaming, Paused, Robust,
			Smudged, LowResource };
	private static final eLeapDeviceStatus[] KNOWN_FAILURES = { BadCalibration, BadFirmware,
			BadTransport, BadControl };

	public final int value;

	private eLeapDeviceStatus(int value)
	{
		this.value = value;
	}


	@Override
	public int getValue()
	{
		return value;
	}


	@Override
	public eLeapDeviceStatus getEmptyMaskConstant()
	{
		return None;
	}


	@Override
	public eLeapDeviceStatus[] parseMask(int mask)
	{
		List<eLeapDeviceStatus> flags = new ArrayList<>();

		if ((mask & UnknownFailure.value) == UnknownFailure.value)
		{
			for (eLeapDeviceStatus o : KNOWN_FAILURES)
			{
				if ((mask & o.value) == o.value)
				{
					flags.add(o);
				}
			}

			if (flags.isEmpty())
			{
				flags.add(UnknownFailure);
			}
		}
		else
		{
			for (eLeapDeviceStatus o : FUNCTIONAL)
			{
				if ((mask & o.value) == o.value)
				{
					flags.add(o);
				}
			}

			if (flags.isEmpty())
			{
				flags.add(None);
			}
		}

		return flags.toArray(new eLeapDeviceStatus[flags.size()]);
	}


	public static int createMask(eLeapDeviceStatus... flags)
	{
		return Enums.createMask(flags);
	}
}