/*
 * Copyright 2020-2022 Jakob Hjelm (Komposten)
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

import komposten.leapjna.leapc.LeapC;
import komposten.leapjna.leapc.data.LEAP_CONNECTION_MESSAGE;
import komposten.leapjna.leapc.enums.Enums;
import komposten.leapjna.leapc.enums.eLeapPolicyFlag;


/**
 * <p>
 * The response from a request to get or set a policy.
 * </p>
 * <p>
 * {@link LeapC#LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)} creates this
 * struct when the response becomes available.
 * </p>
 * 
 * @see <a href=
 *      "https://docs.ultraleap.com/tracking-api/group/group___structs.html#_CPPv417LEAP_POLICY_EVENT">LeapC
 *      API - LEAP_POLICY_EVENT</a>
 * @since LeapJna 1.0.0
 * @since Ultraleap Orion SDK 3.0.0
 */
@FieldOrder({ "reserved", "current_policy" })
public class LEAP_POLICY_EVENT extends Structure implements LEAP_EVENT
{
	/** Reserved for future use. */
	public int reserved;

	/**
	 * A bitfield containing the policies effective at the time the policy event was
	 * processed. Use {@link #getCurrentPolicy()} to get the policies as an array of
	 * {@link eLeapPolicyFlag} values.
	 */
	public int current_policy;

	public LEAP_POLICY_EVENT(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}


	/**
	 * @return An array containing the current {@link eLeapPolicyFlag}s.
	 */
	public eLeapPolicyFlag[] getCurrentPolicy()
	{
		return Enums.parseMask(current_policy, eLeapPolicyFlag.class);
	}
}
