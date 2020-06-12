/*
 * Copyright 2020 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc.events;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import komposten.leapjna.leapc.enums.eLeapDeviceStatus;
import komposten.leapjna.leapc.enums.eLeapDroppedFrameType;
import komposten.leapjna.leapc.enums.eLeapLogSeverity;
import komposten.leapjna.leapc.enums.eLeapPolicyFlag;
import komposten.leapjna.leapc.enums.eLeapServiceDisposition;


class EventTests
{
	@Nested
	class LEAP_CONNECTION_EVENT_TEST
	{
		private LEAP_CONNECTION_EVENT struct;

		@BeforeEach
		void setup()
		{
			struct = new LEAP_CONNECTION_EVENT(null);
		}


		@Test
		void getFlags_validFlags_correctConstant()
		{
			eLeapServiceDisposition expected = eLeapServiceDisposition.LowFpsDetected;
			struct.flags = expected.value;

			assertThat(struct.getFlags()).isSameAs(expected);
		}


		@Test
		void getFlags_invalidFlags_correctConstant()
		{
			struct.flags = -1234;
			assertThat(struct.getFlags()).isSameAs(eLeapServiceDisposition.Unknown);
		}


		@Test
		void getFlags_flagsChanged_correctConstant()
		{
			eLeapServiceDisposition expected = eLeapServiceDisposition.LowFpsDetected;
			struct.flags = expected.value;
			struct.getFlags();

			expected = eLeapServiceDisposition.PoorPerformancePause;
			struct.flags = expected.value;
			assertThat(struct.getFlags()).isSameAs(expected);
		}
	}


	@Nested
	class LEAP_DEVICE_EVENT_TEST
	{
		private LEAP_DEVICE_EVENT struct;

		@BeforeEach
		void setup()
		{
			struct = new LEAP_DEVICE_EVENT(null);
		}


		@Test
		void getStatus_invalidMask_correctConstant()
		{
			struct.status = 0x100000;

			assertThat(struct.getStatus()).containsExactlyInAnyOrder(eLeapDeviceStatus.None);
		}


		@Test
		void getStatus_validMask_correctConstants()
		{
			eLeapDeviceStatus[] expected = { eLeapDeviceStatus.Robust,
					eLeapDeviceStatus.Smudged };
			struct.status = eLeapDeviceStatus.createMask(expected);

			assertThat(struct.getStatus()).containsExactlyInAnyOrder(expected);
		}


		@Test
		void getStatus_maskChanged_correctConstants()
		{
			eLeapDeviceStatus[] expected = { eLeapDeviceStatus.Robust,
					eLeapDeviceStatus.Smudged };
			struct.status = eLeapDeviceStatus.createMask(expected);
			struct.getStatus();

			expected[1] = eLeapDeviceStatus.Streaming;
			struct.status = eLeapDeviceStatus.createMask(expected);
			assertThat(struct.getStatus()).containsExactlyInAnyOrder(expected);
		}
	}


	@Nested
	class LEAP_DEVICE_FAILURE_EVENT_TEST
	{
		private LEAP_DEVICE_FAILURE_EVENT struct;

		@BeforeEach
		void setup()
		{
			struct = new LEAP_DEVICE_FAILURE_EVENT(null);
		}


		@Test
		void getStatus_invalidMask_correctConstant()
		{
			struct.status = 0x100000;

			assertThat(struct.getStatus()).containsExactlyInAnyOrder(eLeapDeviceStatus.None);
		}


		@Test
		void getStatus_validMask_correctConstants()
		{
			eLeapDeviceStatus[] expected = { eLeapDeviceStatus.Robust,
					eLeapDeviceStatus.Smudged };
			struct.status = eLeapDeviceStatus.createMask(expected);

			assertThat(struct.getStatus()).containsExactlyInAnyOrder(expected);
		}


		@Test
		void getStatus_maskChanged_correctConstants()
		{
			eLeapDeviceStatus[] expected = { eLeapDeviceStatus.Robust,
					eLeapDeviceStatus.Smudged };
			struct.status = eLeapDeviceStatus.createMask(expected);
			struct.getStatus();

			expected[1] = eLeapDeviceStatus.Streaming;
			struct.status = eLeapDeviceStatus.createMask(expected);
			assertThat(struct.getStatus()).containsExactlyInAnyOrder(expected);
		}
	}


	@Nested
	class LEAP_DEVICE_STATUS_CHANGE_EVENT_TEST
	{
		private LEAP_DEVICE_STATUS_CHANGE_EVENT struct;

		@BeforeEach
		void setup()
		{
			struct = new LEAP_DEVICE_STATUS_CHANGE_EVENT(null);
		}


		@Test
		void getStatus_invalidMask_correctConstant()
		{
			struct.status = 0x100000;

			assertThat(struct.getStatus()).containsExactlyInAnyOrder(eLeapDeviceStatus.None);
		}


		@Test
		void getStatus_validMask_correctConstants()
		{
			eLeapDeviceStatus[] expected = { eLeapDeviceStatus.Robust,
					eLeapDeviceStatus.Smudged };
			struct.status = eLeapDeviceStatus.createMask(expected);

			assertThat(struct.getStatus()).containsExactlyInAnyOrder(expected);
		}


		@Test
		void getStatus_maskChanged_correctConstants()
		{
			eLeapDeviceStatus[] expected = { eLeapDeviceStatus.Robust,
					eLeapDeviceStatus.Smudged };
			struct.status = eLeapDeviceStatus.createMask(expected);
			struct.getStatus();

			expected[1] = eLeapDeviceStatus.Streaming;
			struct.status = eLeapDeviceStatus.createMask(expected);
			assertThat(struct.getStatus()).containsExactlyInAnyOrder(expected);
		}


		@Test
		void getLastStatus_invalidMask_correctConstant()
		{
			struct.last_status = 0x100000;

			assertThat(struct.getLastStatus())
					.containsExactlyInAnyOrder(eLeapDeviceStatus.None);
		}


		@Test
		void getLastStatus_validMask_correctConstants()
		{
			eLeapDeviceStatus[] expected = { eLeapDeviceStatus.Robust,
					eLeapDeviceStatus.Smudged };
			struct.last_status = eLeapDeviceStatus.createMask(expected);

			assertThat(struct.getLastStatus()).containsExactlyInAnyOrder(expected);
		}


		@Test
		void getLastStatus_maskChanged_correctConstants()
		{
			eLeapDeviceStatus[] expected = { eLeapDeviceStatus.Robust,
					eLeapDeviceStatus.Smudged };
			struct.last_status = eLeapDeviceStatus.createMask(expected);
			struct.getLastStatus();

			expected[1] = eLeapDeviceStatus.Streaming;
			struct.last_status = eLeapDeviceStatus.createMask(expected);
			assertThat(struct.getLastStatus()).containsExactlyInAnyOrder(expected);
		}
	}


	@Nested
	class LEAP_DROPPED_FRAME_EVENT_TEST
	{
		private LEAP_DROPPED_FRAME_EVENT struct;

		@BeforeEach
		void setup()
		{
			struct = new LEAP_DROPPED_FRAME_EVENT(null);
		}


		@Test
		void getType_validType_correctConstant()
		{
			eLeapDroppedFrameType expected = eLeapDroppedFrameType.TrackingQueue;
			struct.type = expected.value;

			assertThat(struct.getType()).isSameAs(expected);
		}


		@Test
		void getType_invalidType_correctConstant()
		{
			struct.type = -1234;
			assertThat(struct.getType()).isSameAs(eLeapDroppedFrameType.Unknown);
		}


		@Test
		void getType_typeChanged_correctConstant()
		{
			eLeapDroppedFrameType expected = eLeapDroppedFrameType.TrackingQueue;
			struct.type = expected.value;
			struct.getType();

			expected = eLeapDroppedFrameType.Other;
			struct.type = expected.value;
			assertThat(struct.getType()).isSameAs(expected);
		}
	}


	@Nested
	class LEAP_LOG_EVENT_TEST
	{
		private LEAP_LOG_EVENT struct;

		@BeforeEach
		void setup()
		{
			struct = new LEAP_LOG_EVENT(null);
		}


		@Test
		void getSeverity_validSeverity_correctConstant()
		{
			eLeapLogSeverity expected = eLeapLogSeverity.Warning;
			struct.severity = expected.value;

			assertThat(struct.getSeverity()).isSameAs(expected);
		}


		@Test
		void getSeverity_invalidSeverity_correctConstant()
		{
			struct.severity = -1234;
			assertThat(struct.getSeverity()).isSameAs(eLeapLogSeverity.Unknown);
		}


		@Test
		void getSeverity_severityChanged_correctConstant()
		{
			eLeapLogSeverity expected = eLeapLogSeverity.Warning;
			struct.severity = expected.value;
			struct.getSeverity();

			expected = eLeapLogSeverity.Critical;
			struct.severity = expected.value;
			assertThat(struct.getSeverity()).isSameAs(expected);
		}
	}


	@Nested
	class LEAP_POLICY_EVENT_TEST
	{
		private LEAP_POLICY_EVENT struct;

		@BeforeEach
		void setup()
		{
			struct = new LEAP_POLICY_EVENT(null);
		}


		@Test
		void getCurrentPolicy_invalidMask_correctConstant()
		{
			struct.current_policy = 0x100000;

			assertThat(struct.getCurrentPolicy()).containsExactlyInAnyOrder(eLeapPolicyFlag.None);
		}


		@Test
		void getCurrentPolicy_validMask_correctConstants()
		{
			eLeapPolicyFlag[] expected = { eLeapPolicyFlag.Images,
					eLeapPolicyFlag.MapPoints };
			struct.current_policy = eLeapPolicyFlag.createMask(expected);

			assertThat(struct.getCurrentPolicy()).containsExactlyInAnyOrder(expected);
		}


		@Test
		void getCurrentPolicy_maskChanged_correctConstants()
		{
			eLeapPolicyFlag[] expected = { eLeapPolicyFlag.Images,
					eLeapPolicyFlag.MapPoints };
			struct.current_policy = eLeapPolicyFlag.createMask(expected);
			struct.getCurrentPolicy();

			expected[1] = eLeapPolicyFlag.OptimiseHMD;
			struct.current_policy = eLeapPolicyFlag.createMask(expected);
			assertThat(struct.getCurrentPolicy()).containsExactlyInAnyOrder(expected);
		}
	}
}
