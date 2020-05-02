package komposten.leapjna.leapc.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import org.assertj.core.api.Condition;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import komposten.leapjna.leapc.enums.eLeapConnectionStatus;
import komposten.leapjna.leapc.enums.eLeapDeviceCaps;
import komposten.leapjna.leapc.enums.eLeapDevicePID;
import komposten.leapjna.leapc.enums.eLeapDeviceStatus;
import komposten.leapjna.leapc.enums.eLeapEventType;


public class StructTests
{
	@Nested
	class LEAP_CONNECTION_INFO_TEST
	{
		private LEAP_CONNECTION_INFO struct;

		@BeforeEach
		void setup()
		{
			struct = new LEAP_CONNECTION_INFO();
		}


		@Test
		void getStatus_validStatus_correctConstant()
		{
			eLeapConnectionStatus expected = eLeapConnectionStatus.Connected;
			struct.status = expected.value;

			assertThat(struct.getStatus()).isSameAs(expected);
		}


		@Test
		void getStatus_invalidStatus_correctConstant()
		{
			struct.status = -1234;
			assertThat(struct.getStatus()).isSameAs(eLeapConnectionStatus.Unknown);
		}


		@Test
		void getStatus_statusChanged_correctConstant()
		{
			eLeapConnectionStatus expected = eLeapConnectionStatus.Connected;
			struct.status = expected.value;
			struct.getStatus();

			expected = eLeapConnectionStatus.HandshakeIncomplete;
			struct.status = expected.value;
			assertThat(struct.getStatus()).isSameAs(expected);
		}
	}


	@Nested
	class LEAP_CONNECTION_MESSAGE_TEST
	{
		private LEAP_CONNECTION_MESSAGE struct;

		@BeforeEach
		void setup()
		{
			struct = new LEAP_CONNECTION_MESSAGE();
		}


		@Test
		void getType_validType_correctConstant()
		{
			eLeapEventType expected = eLeapEventType.Tracking;
			struct.type = expected.value;

			assertThat(struct.getType()).isSameAs(expected);
		}


		@Test
		void getType_invalidType_correctConstant()
		{
			struct.type = -1234;
			assertThat(struct.getType()).isSameAs(eLeapEventType.Unknown);
		}


		@Test
		void getType_typeChanged_correctConstant()
		{
			eLeapEventType expected = eLeapEventType.Tracking;
			struct.type = expected.value;
			struct.getType();

			expected = eLeapEventType.Device;
			struct.type = expected.value;
			assertThat(struct.getType()).isSameAs(expected);
		}


		@Test
		void getXEvent_correctType_noException()
		{
			assertGetEvent(eLeapEventType.Connection, () -> struct.getConnectionEvent());
			assertGetEvent(eLeapEventType.ConnectionLost, () -> struct.getConnectionLostEvent());
			assertGetEvent(eLeapEventType.Device, () -> struct.getDeviceEvent());
			assertGetEvent(eLeapEventType.DeviceLost, () -> struct.getDeviceLostEvent());
			assertGetEvent(eLeapEventType.DeviceStatusChange, () -> struct.getDeviceStatusChangeEvent());
			assertGetEvent(eLeapEventType.Policy, () -> struct.getPolicyEvent());
			assertGetEvent(eLeapEventType.DeviceFailure, () -> struct.getDeviceFailureEvent());
			assertGetEvent(eLeapEventType.Tracking, () -> struct.getTrackingEvent());
			assertGetEvent(eLeapEventType.LogEvent, () -> struct.getLogEvent());
			assertGetEvent(eLeapEventType.LogEvents, () -> struct.getLogEvents());
			assertGetEvent(eLeapEventType.ConfigResponse, () -> struct.getConfigResponseEvent());
			assertGetEvent(eLeapEventType.ConfigChange, () -> struct.getConfigChangeEvent());
			assertGetEvent(eLeapEventType.DroppedFrame, () -> struct.getDroppedFrameEvent());
			assertGetEvent(eLeapEventType.HeadPose, () -> struct.getHeadPoseEvent());
			assertGetEvent(eLeapEventType.PointMappingChange, () -> struct.getPointMappingChangeEvent());
			assertGetEvent(eLeapEventType.Image, () -> struct.getImageEvent());
		}


		@Test
		void getXEvent_incorrectType_IllegalStateException()
		{
			assertGetEventThrows(eLeapEventType.Image, () -> struct.getConnectionEvent());
			assertGetEventThrows(eLeapEventType.Connection, () -> struct.getConnectionLostEvent());
			assertGetEventThrows(eLeapEventType.ConnectionLost, () -> struct.getDeviceEvent());
			assertGetEventThrows(eLeapEventType.Device, () -> struct.getDeviceLostEvent());
			assertGetEventThrows(eLeapEventType.DeviceLost, () -> struct.getDeviceStatusChangeEvent());
			assertGetEventThrows(eLeapEventType.DeviceStatusChange, () -> struct.getPolicyEvent());
			assertGetEventThrows(eLeapEventType.Policy, () -> struct.getDeviceFailureEvent());
			assertGetEventThrows(eLeapEventType.DeviceFailure, () -> struct.getTrackingEvent());
			assertGetEventThrows(eLeapEventType.Tracking, () -> struct.getLogEvent());
			assertGetEventThrows(eLeapEventType.LogEvent, () -> struct.getLogEvents());
			assertGetEventThrows(eLeapEventType.LogEvents, () -> struct.getConfigResponseEvent());
			assertGetEventThrows(eLeapEventType.ConfigResponse, () -> struct.getConfigChangeEvent());
			assertGetEventThrows(eLeapEventType.ConfigChange, () -> struct.getDroppedFrameEvent());
			assertGetEventThrows(eLeapEventType.DroppedFrame, () -> struct.getHeadPoseEvent());
			assertGetEventThrows(eLeapEventType.HeadPose, () -> struct.getPointMappingChangeEvent());
			assertGetEventThrows(eLeapEventType.PointMappingChange, () -> struct.getImageEvent());
		}


		void assertGetEvent(eLeapEventType type, ThrowingCallable code)
		{
			struct = new LEAP_CONNECTION_MESSAGE();
			struct.type = type.value;
			assertThatCode(code)
					.isNot(new Condition<>(t -> t instanceof IllegalStateException,
							"an illegal state exception", ""));
		}


		void assertGetEventThrows(eLeapEventType type, ThrowingCallable code)
		{
			struct = new LEAP_CONNECTION_MESSAGE();
			struct.type = type.value;
			assertThatIllegalStateException().isThrownBy(code);
		}
	}

	
	@Nested
	class LEAP_DEVICE_INFO_TEST
	{
		private LEAP_DEVICE_INFO struct;

		@BeforeEach
		void setup()
		{
			struct = new LEAP_DEVICE_INFO();
		}


		@Test
		void getStatus_validMask_correctConstants()
		{
			eLeapDeviceStatus[] expected = { eLeapDeviceStatus.Robust,
					eLeapDeviceStatus.Streaming };
			struct.status = eLeapDeviceStatus.createMask(expected);

			assertThat(struct.getStatus()).containsExactlyInAnyOrder(expected);
		}


		@Test
		void getStatus_maskChanged_correctConstants()
		{
			eLeapDeviceStatus[] expected = { eLeapDeviceStatus.Robust,
					eLeapDeviceStatus.Streaming };
			struct.status = eLeapDeviceStatus.createMask(expected);
			struct.getStatus();

			expected[1] = eLeapDeviceStatus.Smudged;
			struct.status = eLeapDeviceStatus.createMask(expected);
			assertThat(struct.getStatus()).containsExactlyInAnyOrder(expected);
		}


		@Test
		void getCapabilities_validMask_correctConstants()
		{
			eLeapDeviceCaps[] expected = { eLeapDeviceCaps.Color };
			struct.status = eLeapDeviceCaps.createMask(expected);

			assertThat(struct.getCapabilities()).containsExactlyInAnyOrder(expected);
		}


		@Test
		void getCapabilities_maskChanged_correctConstants()
		{
			eLeapDeviceCaps[] expected = { eLeapDeviceCaps.Color };
			struct.status = eLeapDeviceCaps.createMask(expected);
			struct.getCapabilities();

			expected[0] = eLeapDeviceCaps.None;
			struct.status = eLeapDeviceCaps.createMask(expected);
			assertThat(struct.getCapabilities()).containsExactlyInAnyOrder(expected);
		}


		@Test
		void getPid_validPid_correctConstant()
		{
			eLeapDevicePID expected = eLeapDevicePID.Dragonfly;
			struct.pid = expected.value;

			assertThat(struct.getPid()).isSameAs(expected);
		}


		@Test
		void getPid_invalidPid_correctConstant()
		{
			struct.pid = -1234;
			assertThat(struct.getPid()).isSameAs(eLeapDevicePID.Invalid);
		}


		@Test
		void getPid_pidChanged_correctConstant()
		{
			eLeapDevicePID expected = eLeapDevicePID.Dragonfly;
			struct.pid = expected.value;
			struct.getPid();

			expected = eLeapDevicePID.Peripheral;
			struct.pid = expected.value;
			assertThat(struct.getPid()).isSameAs(expected);
		}
	}
}
