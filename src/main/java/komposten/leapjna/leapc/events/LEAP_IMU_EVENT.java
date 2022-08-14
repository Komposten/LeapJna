/*
 * Copyright 2022 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.events;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.leapc.data.LEAP_VECTOR;
import komposten.leapjna.leapc.enums.Enums;
import komposten.leapjna.leapc.enums.eLeapIMUFlag;


@FieldOrder({ "timestamp", "timestamp_hw", "flags",
		"accelerometer", "gyroscope", "temperature" })
public class LEAP_IMU_EVENT extends Structure implements LEAP_EVENT
{
	/**
	 * The timestamp for these measurements, in microseconds, referenced against
	 * LeapGetNow().
	 */
	public long timestamp;
	/**
	 * The timestamp for these measurements, in microseconds, referenced against the
	 * device's internal clock.
	 */
	public long timestamp_hw;

	/**
	 * A combination of {@link eLeapIMUFlag} flags.
	 */
	public int flags;

	/**
	 * The accelerometer measurements, in m/s^2.
	 */
	public LEAP_VECTOR accelerometer;

	/**
	 * The gyroscope measurements, in rad/s.
	 */
	public LEAP_VECTOR gyroscope;

	/**
	 * The measured temperature, in deg C.
	 */
	public float temperature;

	public LEAP_IMU_EVENT(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}
	
	/**
	 * @return An array containing the {@link eLeapIMUFlag}s.
	 */
	public eLeapIMUFlag[] getFlags()
	{
		return Enums.parseMask(flags, eLeapIMUFlag.class);
	}
}
