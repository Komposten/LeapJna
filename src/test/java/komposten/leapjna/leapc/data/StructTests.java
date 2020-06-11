package komposten.leapjna.leapc.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import java.util.Comparator;

import org.assertj.core.api.Condition;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import komposten.leapjna.leapc.enums.eLeapConnectionStatus;
import komposten.leapjna.leapc.enums.eLeapDeviceCaps;
import komposten.leapjna.leapc.enums.eLeapDevicePID;
import komposten.leapjna.leapc.enums.eLeapDeviceStatus;
import komposten.leapjna.leapc.enums.eLeapEventType;
import komposten.leapjna.leapc.enums.eLeapHandType;
import komposten.leapjna.leapc.enums.eLeapImageFormat;
import komposten.leapjna.leapc.enums.eLeapImageType;
import komposten.leapjna.leapc.enums.eLeapRecordingFlags;
import komposten.leapjna.leapc.enums.eLeapValueType;


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
			assertGetEvent(eLeapEventType.ConnectionLost,	() -> struct.getConnectionLostEvent());
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

			assertThat(struct.getStatus())
					.usingElementComparator(new IdentityComparator<>())
					.containsExactlyInAnyOrder(expected);
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
			assertThat(struct.getStatus())
					.usingElementComparator(new IdentityComparator<>())
					.containsExactlyInAnyOrder(expected);
		}


		@Test
		void getCapabilities_validMask_correctConstants()
		{
			eLeapDeviceCaps[] expected = { eLeapDeviceCaps.Color };
			struct.caps = eLeapDeviceCaps.createMask(expected);

			assertThat(struct.getCapabilities())
					.usingElementComparator(new IdentityComparator<>())
					.containsExactlyInAnyOrder(expected);
		}


		@Test
		void getCapabilities_maskChanged_correctConstants()
		{
			eLeapDeviceCaps[] expected = { eLeapDeviceCaps.Color };
			struct.caps = eLeapDeviceCaps.createMask(expected);
			struct.getCapabilities();

			expected[0] = eLeapDeviceCaps.None;
			struct.caps = eLeapDeviceCaps.createMask(expected);
			assertThat(struct.getCapabilities())
					.usingElementComparator(new IdentityComparator<>())
					.containsExactlyInAnyOrder(expected);
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


	@Nested
	class LEAP_DIGIT_TEST
	{
		@Test
		void boneArray_allBonesInOrder()
		{
			LEAP_DIGIT digit = new LEAP_DIGIT();
			digit.metacarpal = new LEAP_BONE();
			digit.proximal = new LEAP_BONE();
			digit.intermediate = new LEAP_BONE();
			digit.distal = new LEAP_BONE();

			LEAP_BONE[] expected = { digit.metacarpal, digit.proximal,
					digit.intermediate, digit.distal };

			assertThat(digit.boneArray())
					.usingElementComparator(new IdentityComparator<>())
					.containsExactly(expected);
		}
	}
	
	
	@Nested
	class LEAP_HAND_TEST
	{
		private LEAP_HAND struct;

		@BeforeEach
		void setup()
		{
			struct = new LEAP_HAND();
		}


		@Test
		void getType_validType_correctConstant()
		{
			eLeapHandType expected = eLeapHandType.Right;
			struct.type = expected.value;

			assertThat(struct.getType()).isSameAs(expected);
		}


		@Test
		void getType_invalidType_correctConstant()
		{
			struct.type = -10;
			assertThat(struct.getType()).isSameAs(eLeapHandType.Unknown);
		}


		@Test
		void getType_typeChanged_correctConstant()
		{
			eLeapHandType expected = eLeapHandType.Right;
			struct.type = expected.value;
			struct.getType();

			expected = eLeapHandType.Left;
			struct.type = expected.value;
			assertThat(struct.getType()).isSameAs(expected);
		}


		@Nested
		class DigitStructTest
		{
			@Test
			void asArray_allDigitsInOrder()
			{
				struct.digits = new LEAP_HAND.DigitStruct();
				struct.digits.thumb = new LEAP_DIGIT();
				struct.digits.index = new LEAP_DIGIT();
				struct.digits.middle = new LEAP_DIGIT();
				struct.digits.ring = new LEAP_DIGIT();
				struct.digits.pinky = new LEAP_DIGIT();

				LEAP_DIGIT[] expected = { struct.digits.thumb, struct.digits.index,
						struct.digits.middle, struct.digits.ring, struct.digits.pinky };

				assertThat(struct.digits.asArray())
						.usingElementComparator(new IdentityComparator<>())
						.containsExactly(expected);
			}
		}
	}
	
	
	@Nested
	class LEAP_IMAGE_PROPERTIES_TEST
	{
		private LEAP_IMAGE_PROPERTIES struct;

		@BeforeEach
		void setup()
		{
			struct = new LEAP_IMAGE_PROPERTIES();
		}


		@Test
		void getType_validType_correctConstant()
		{
			eLeapImageType expected = eLeapImageType.Raw;
			struct.type = expected.value;

			assertThat(struct.getType()).isSameAs(expected);
		}


		@Test
		void getType_invalidType_correctConstant()
		{
			struct.type = -1234;
			assertThat(struct.getType()).isSameAs(eLeapImageType.Unknown);
		}


		@Test
		void getType_typeChanged_correctConstant()
		{
			eLeapImageType expected = eLeapImageType.Raw;
			struct.type = expected.value;
			struct.getType();

			expected = eLeapImageType.Default;
			struct.type = expected.value;
			assertThat(struct.getType()).isSameAs(expected);
		}


		@Test
		void getFormat_validFormat_correctConstant()
		{
			eLeapImageFormat expected = eLeapImageFormat.RGBIr;
			struct.format = expected.value;

			assertThat(struct.getFormat()).isSameAs(expected);
		}


		@Test
		void getFormat_invalidFormat_correctConstant()
		{
			struct.format = -1234;
			assertThat(struct.getFormat()).isSameAs(eLeapImageFormat.Unknown);
		}


		@Test
		void getFormat_formatChanged_correctConstant()
		{
			eLeapImageFormat expected = eLeapImageFormat.RGBIr;
			struct.format = expected.value;
			struct.getFormat();

			expected = eLeapImageFormat.IR;
			struct.format = expected.value;
			assertThat(struct.getFormat()).isSameAs(expected);
		}
	}


	@Nested
	class LEAP_RECORDING_PARAMETERS_TEST
	{
		private LEAP_RECORDING_PARAMETERS struct;

		@BeforeEach
		void setup()
		{
			struct = new LEAP_RECORDING_PARAMETERS();
		}
		
		
		@Test
		void getMode_invalidMask_correctConstant()
		{
			struct.mode = 0x100000;

			assertThat(struct.getMode())
					.usingElementComparator(new IdentityComparator<>())
					.containsExactlyInAnyOrder(eLeapRecordingFlags.Error);
		}


		@Test
		void getMode_validMask_correctConstants()
		{
			eLeapRecordingFlags[] expected = { eLeapRecordingFlags.Writing,
					eLeapRecordingFlags.Flushing };
			struct.mode = eLeapRecordingFlags.createMask(expected);

			assertThat(struct.getMode())
					.usingElementComparator(new IdentityComparator<>())
					.containsExactlyInAnyOrder(expected);
		}


		@Test
		void getMode_maskChanged_correctConstants()
		{
			eLeapRecordingFlags[] expected = { eLeapRecordingFlags.Writing,
					eLeapRecordingFlags.Flushing };
			struct.mode = eLeapRecordingFlags.createMask(expected);
			struct.getMode();

			expected[1] = eLeapRecordingFlags.Reading;
			struct.mode = eLeapRecordingFlags.createMask(expected);
			assertThat(struct.getMode())
					.usingElementComparator(new IdentityComparator<>())
					.containsExactlyInAnyOrder(expected);
		}
	}


	@Nested
	class LEAP_RECORDING_STATUS_TEST
	{
		private LEAP_RECORDING_STATUS struct;

		@BeforeEach
		void setup()
		{
			struct = new LEAP_RECORDING_STATUS();
		}
		
		
		@Test
		void getMode_invalidMask_correctConstant()
		{
			struct.mode = 0x100000;

			assertThat(struct.getMode())
					.usingElementComparator(new IdentityComparator<>())
					.containsExactlyInAnyOrder(eLeapRecordingFlags.Error);
		}


		@Test
		void getMode_validMask_correctConstants()
		{
			eLeapRecordingFlags[] expected = { eLeapRecordingFlags.Writing,
					eLeapRecordingFlags.Flushing };
			struct.mode = eLeapRecordingFlags.createMask(expected);

			assertThat(struct.getMode())
					.usingElementComparator(new IdentityComparator<>())
					.containsExactlyInAnyOrder(expected);
		}


		@Test
		void getMode_maskChanged_correctConstants()
		{
			eLeapRecordingFlags[] expected = { eLeapRecordingFlags.Writing,
					eLeapRecordingFlags.Flushing };
			struct.mode = eLeapRecordingFlags.createMask(expected);
			struct.getMode();

			expected[1] = eLeapRecordingFlags.Reading;
			struct.mode = eLeapRecordingFlags.createMask(expected);
			assertThat(struct.getMode())
					.usingElementComparator(new IdentityComparator<>())
					.containsExactlyInAnyOrder(expected);
		}
	}
	
	
	@Nested
	class LEAP_VARIANT_TEST
	{
		private LEAP_VARIANT struct;

		@BeforeEach
		void setup()
		{
			struct = new LEAP_VARIANT(true);
		}


		@Test
		void getType_validType_correctConstant()
		{
			eLeapValueType expected = eLeapValueType.Boolean;
			struct.type = expected.value;

			assertThat(struct.getType()).isSameAs(expected);
		}


		@Test
		void getType_invalidType_correctConstant()
		{
			struct.type = -1234;
			assertThat(struct.getType()).isSameAs(eLeapValueType.Unknown);
		}


		@Test
		void getType_typeChanged_correctConstant()
		{
			eLeapValueType expected = eLeapValueType.Boolean;
			struct.type = expected.value;
			struct.getType();

			expected = eLeapValueType.Float;
			struct.type = expected.value;
			assertThat(struct.getType()).isSameAs(expected);
		}
		
		
		@Test
		void getBoolean_booleanType_correctValue()
		{
			LEAP_VARIANT variant = new LEAP_VARIANT(true);
			assertThat(variant.getBoolean()).isTrue();
			
			variant = new LEAP_VARIANT(false);
			assertThat(variant.getBoolean()).isFalse();
		}
		
		
		@Test
		void getBoolean_nonBooleanType_throwsIllegalStateException()
		{
			LEAP_VARIANT variant = new LEAP_VARIANT(1);
			assertThatIllegalStateException().isThrownBy(() -> variant.getBoolean());
		}
		
		
		@Test
		void getInt_intType_correctValue()
		{
			LEAP_VARIANT variant = new LEAP_VARIANT(1);
			assertThat(variant.getInt()).isEqualTo(1);
			
			variant = new LEAP_VARIANT(2);
			assertThat(variant.getInt()).isEqualTo(2);
		}
		
		
		@Test
		void getInt_nonIntType_throwsIllegalStateException()
		{
			LEAP_VARIANT variant = new LEAP_VARIANT(1.0f);
			assertThatIllegalStateException().isThrownBy(() -> variant.getInt());
		}
		
		
		@Test
		void getFloat_floatType_correctValue()
		{
			LEAP_VARIANT variant = new LEAP_VARIANT(1.0f);
			assertThat(variant.getFloat()).isEqualTo(1.0f);
			
			variant = new LEAP_VARIANT(1.5f);
			assertThat(variant.getFloat()).isEqualTo(1.5f);
		}
		
		
		@Test
		void getFloat_nonFloatType_throwsIllegalStateException()
		{
			LEAP_VARIANT variant = new LEAP_VARIANT(1);
			assertThatIllegalStateException().isThrownBy(() -> variant.getFloat());
		}
		
		
		@Test
		void getString_stringType_correctValue()
		{
			LEAP_VARIANT variant = new LEAP_VARIANT("leap");
			assertThat(variant.getString()).isEqualTo("leap");
			
			variant = new LEAP_VARIANT("sleep");
			assertThat(variant.getString()).isEqualTo("sleep");
		}
		
		
		@Test
		void getString_nonStringType_throwsIllegalStateException()
		{
			LEAP_VARIANT variant = new LEAP_VARIANT(true);
			assertThatIllegalStateException().isThrownBy(() -> variant.getString());
		}
		
		
		@Test
		void getValue_hasValue_returnsValue()
		{
			LEAP_VARIANT variant = new LEAP_VARIANT(true);
			assertThat(variant.getValue()).isEqualTo(true);
			
			variant = new LEAP_VARIANT(1);
			assertThat(variant.getValue()).isEqualTo(1);
			
			variant = new LEAP_VARIANT(1.0f);
			assertThat(variant.getValue()).isEqualTo(1.0f);
			
			variant = new LEAP_VARIANT("leap");
			assertThat(variant.getValue()).isEqualTo("leap");
		}
		
		
		@Test
		void getValue_noValue_returnsNull()
		{
			LEAP_VARIANT variant = new LEAP_VARIANT();
			assertThat(variant.getValue()).isNull();
		}
	}
	
	
	@Nested
	class LEAP_VECTOR_TEST
	{
		private final Offset<Float> PRECISION = Offset.offset(0.01f);
		
		@Test
		void constructor_withParameters_intialisedCorrectly()
		{
			LEAP_VECTOR vector = new LEAP_VECTOR(1, 2, 3);

			assertThat(vector.x).isCloseTo(1, PRECISION);
			assertThat(vector.y).isCloseTo(2, PRECISION);
			assertThat(vector.z).isCloseTo(3, PRECISION);
		}
		

		@Test
		void set_updatesValues()
		{
			LEAP_VECTOR vector = new LEAP_VECTOR(1, 2, 3);
			
			vector.set(2, 4, 6);

			assertThat(vector.x).isCloseTo(2, PRECISION);
			assertThat(vector.y).isCloseTo(4, PRECISION);
			assertThat(vector.z).isCloseTo(6, PRECISION);
		}
		
		
		@Test
		void asArray_correctOrder()
		{
			LEAP_VECTOR vector = new LEAP_VECTOR(1, 2, 3);
			float[] expected = { 1, 2, 3 };
			
			assertThat(vector.asArray()).containsExactly(expected, PRECISION);
		}
	}
	
	
	/**
	 * A comparator used to do <code>==</code> comparison of objects.
	 * This comparator should only be used for equality comparisons, as it does not
	 * ever return <code>1</code> (<code>-1</code> is used when the compared objects
	 * are not the same).
	 */
	class IdentityComparator<T> implements Comparator<T>
	{
		@Override
		public int compare(T o1, T o2)
		{
			// This violates the contract that sign(compare(x, y)) == -sign(compare(y, x))
			// for all x and y. However, since this class is only used for equality-type
			// comparisons it doesn't matter.
			return o1 == o2 ? 0 : -1;
		}
	}
}
