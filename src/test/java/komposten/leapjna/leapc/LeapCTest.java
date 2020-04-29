package komposten.leapjna.leapc;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sun.jna.Pointer;

import komposten.leapjna.leapc.data.LEAP_CONNECTION;
import komposten.leapjna.leapc.data.LEAP_CONNECTION_CONFIG;
import komposten.leapjna.leapc.data.LEAP_CONNECTION_MESSAGE;
import komposten.leapjna.leapc.data.LEAP_DIGIT;
import komposten.leapjna.leapc.data.LEAP_DISTORTION_MATRIX;
import komposten.leapjna.leapc.data.LEAP_HAND;
import komposten.leapjna.leapc.data.LEAP_IMAGE;
import komposten.leapjna.leapc.enums.Enums;
import komposten.leapjna.leapc.enums.eLeapDeviceStatus;
import komposten.leapjna.leapc.enums.eLeapDroppedFrameType;
import komposten.leapjna.leapc.enums.eLeapEventType;
import komposten.leapjna.leapc.enums.eLeapHandType;
import komposten.leapjna.leapc.enums.eLeapImageFormat;
import komposten.leapjna.leapc.enums.eLeapImageType;
import komposten.leapjna.leapc.enums.eLeapLogSeverity;
import komposten.leapjna.leapc.enums.eLeapPolicyFlag;
import komposten.leapjna.leapc.enums.eLeapRS;
import komposten.leapjna.leapc.enums.eLeapServiceDisposition;
import komposten.leapjna.leapc.enums.eLeapValueType;
import komposten.leapjna.leapc.events.LEAP_CONFIG_CHANGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_CONFIG_RESPONSE_EVENT;
import komposten.leapjna.leapc.events.LEAP_CONNECTION_EVENT;
import komposten.leapjna.leapc.events.LEAP_CONNECTION_LOST_EVENT;
import komposten.leapjna.leapc.events.LEAP_DEVICE_EVENT;
import komposten.leapjna.leapc.events.LEAP_DEVICE_FAILURE_EVENT;
import komposten.leapjna.leapc.events.LEAP_DEVICE_STATUS_CHANGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_DROPPED_FRAME_EVENT;
import komposten.leapjna.leapc.events.LEAP_HEAD_POSE_EVENT;
import komposten.leapjna.leapc.events.LEAP_IMAGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_LOG_EVENT;
import komposten.leapjna.leapc.events.LEAP_LOG_EVENTS;
import komposten.leapjna.leapc.events.LEAP_POINT_MAPPING_CHANGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_POLICY_EVENT;
import komposten.leapjna.leapc.events.LEAP_TRACKING_EVENT;


public class LeapCTest
{
	private static final Offset<Float> PRECISION = Offset.offset(0.001f);;


	@BeforeAll
	static void globalSetup()
	{
		LeapCConfig.useMockDll(true);
	}


	@Test
	void LeapCreateConnection_noConfig_success()
	{
		LEAP_CONNECTION phConnection = new LEAP_CONNECTION();
		eLeapRS result = LeapC.INSTANCE.LeapCreateConnection(null, phConnection);
		phConnection.read();

		assertThat(result).isEqualTo(eLeapRS.Success);
	}


	@Test
	void LeapCreateConnection_withConfig_differentReturnValue()
	{
		// MockLeapC's LeapCreateConnection() returns the eLeapRS constant matching
		// the flags specified in the LEAP_CONNECTION_CONFIG object.
		// We use that here to test that the return value is parsed correctly.

		LEAP_CONNECTION phConnection = new LEAP_CONNECTION();
		LEAP_CONNECTION_CONFIG pConfig = new LEAP_CONNECTION_CONFIG();
		pConfig.flags = eLeapRS.NotAvailable.getValue();

		eLeapRS result = LeapC.INSTANCE.LeapCreateConnection(pConfig, phConnection);
		assertThat(result).isEqualTo(eLeapRS.NotAvailable);
	}


	@Test
	void LeapOpenConnection_success()
	{
		eLeapRS result = LeapC.INSTANCE.LeapOpenConnection(null);
		assertThat(result).isEqualTo(eLeapRS.Success);
	}


	@Test
	void LeapPollConnection_eventTypeConnectionEvent_mapsProperly()
	{
		LEAP_CONNECTION_MESSAGE message = assertLeapPollConnection(eLeapEventType.Connection);
		LEAP_CONNECTION_EVENT event = message.getConnectionEvent();
		assertThat(event.flags).isEqualTo(eLeapServiceDisposition.LowFpsDetected.value);
	}


	@Test
	void LeapPollConnection_eventTypeConnectionLostEvent_mapsProperly()
	{
		LEAP_CONNECTION_MESSAGE message = assertLeapPollConnection(eLeapEventType.ConnectionLost);
		LEAP_CONNECTION_LOST_EVENT event = message.getConnectionLostEvent();
		assertThat(event.flags).isEqualTo(eLeapEventType.ConnectionLost.value);
	}


	@Test
	void LeapPollConnection_eventTypeDeviceEvent_mapsProperly()
	{
		LEAP_CONNECTION_MESSAGE message = assertLeapPollConnection(eLeapEventType.Device);
		LEAP_DEVICE_EVENT event = message.getDeviceEvent();
		assertThat(event.flags).isEqualTo(eLeapEventType.Device.value);
		assertThat(event.status).isEqualTo(eLeapDeviceStatus.Streaming.value);
		assertThat(event.device.id).isEqualTo(1);
		assertThat(event.device.handle).isEqualTo(event.getPointer());
	}


	@Test
	void LeapPollConnection_eventTypeDeviceStatusChangeEvent_mapsProperly()
	{
		LEAP_CONNECTION_MESSAGE message = assertLeapPollConnection(eLeapEventType.DeviceStatusChange);
		LEAP_DEVICE_STATUS_CHANGE_EVENT event = message.getDeviceStatusChangeEvent();
		assertThat(event.last_status).isEqualTo(eLeapDeviceStatus.Paused.value);
		assertThat(event.status).isEqualTo(eLeapDeviceStatus.Streaming.value);
		assertThat(event.device.id).isEqualTo(1);
		assertThat(event.device.handle).isEqualTo(event.getPointer());
	}


	@Test
	void LeapPollConnection_eventTypeDeviceFailureEvent_mapsProperly()
	{
		LEAP_CONNECTION_MESSAGE message = assertLeapPollConnection(eLeapEventType.DeviceFailure);
		LEAP_DEVICE_FAILURE_EVENT event = message.getDeviceFailureEvent();
		assertThat(event.status).isEqualTo(eLeapDeviceStatus.BadFirmware.value);
		// Can't test the handle since it's an opaque struct.
	}


	@Test
	void LeapPollConnection_eventTypeDeviceLostEvent_mapsProperly()
	{
		LEAP_CONNECTION_MESSAGE message = assertLeapPollConnection(eLeapEventType.DeviceLost);
		LEAP_DEVICE_EVENT event = message.getDeviceLostEvent();
		assertThat(event.flags).isEqualTo(eLeapEventType.Device.value);
		assertThat(event.device.id).isEqualTo(1);
		assertThat(event.device.handle).isEqualTo(event.getPointer());
	}
	
	
	@Test
	void LeapPollConnection_eventTypePolicyEvent_mapsProperly()
	{
		LEAP_CONNECTION_MESSAGE message = assertLeapPollConnection(eLeapEventType.Policy);
		LEAP_POLICY_EVENT event = message.getPolicyEvent();
		assertThat(event.reserved).isEqualTo(1);
		assertThat(event.current_policy).isEqualTo(eLeapPolicyFlag.Images.value);
	}


	@Test
	void LeapPollConnection_eventTypeTrackingEvent_mapsProperly()
	{
		LEAP_CONNECTION_MESSAGE message = assertLeapPollConnection(eLeapEventType.Tracking);
		LEAP_TRACKING_EVENT trackingEvent = message.getTrackingEvent();
		assertThat(trackingEvent.tracking_frame_id).isEqualTo(1);
		assertThat(trackingEvent.nHands).isEqualTo(2);
		assertThat(trackingEvent.framerate).isCloseTo(100, PRECISION);

		assertThat(trackingEvent.info.reserved).isNotEqualTo(Pointer.NULL);
		assertThat(trackingEvent.info.reserved.getByte(0)).isEqualTo((byte) 1);
		assertThat(trackingEvent.info.frame_id).isEqualTo(2);
		assertThat(trackingEvent.info.timestamp).isEqualTo(3);

		assertThatHandCorrect(trackingEvent.getHands()[0], 0);
		assertThatHandCorrect(trackingEvent.getHands()[1], 1);

		// TODO Freeing used memory causes crash?
		// Native.free(Pointer.nativeValue(trackingEvent.pHands));
		// Native.free(Pointer.nativeValue(trackingEvent.info.reserved));
		// Native.free(Pointer.nativeValue(message.pEvent));
		// Native.free(Pointer.nativeValue(message.getPointer()));
	}


	private void assertThatHandCorrect(LEAP_HAND hand, int id)
	{
		assertThat(hand.id).isEqualTo(id);
		assertThat(hand.flags).isEqualTo(0xff0 + id);
		assertThat(hand.getType()).isEqualTo(Enums.parse((byte) id, eLeapHandType.Unknown));
		assertThat(hand.confidence).isCloseTo(1 + id, PRECISION);
		assertThat(hand.visible_time).isEqualTo(1500000 * (id + 1));
		assertThat(hand.pinch_distance).isCloseTo(1 / (id + 1f), PRECISION);
		assertThat(hand.grab_angle).isCloseTo(1 / (id + 2f), PRECISION);
		assertThat(hand.pinch_strength).isCloseTo(1 / (id + 3f), PRECISION);
		assertThat(hand.grab_strength).isCloseTo(1 / (id + 4f), PRECISION);

		assertThat(hand.palm.position.asArray()).containsExactly(new float[] { 0.25f, 0.50f, 0.75f }, PRECISION);
		assertThat(hand.palm.stabilized_position.asArray()).containsExactly(new float[] { 0.30f, 0.55f, 0.80f }, PRECISION);
		assertThat(hand.palm.velocity.asArray()).containsExactly(new float[] { 1, 2, 3 }, PRECISION);
		assertThat(hand.palm.normal.asArray()).containsExactly(new float[] { -1, -2, -3 }, PRECISION);
		assertThat(hand.palm.width).isCloseTo(115, PRECISION);
		assertThat(hand.palm.direction.asArray()).containsExactly(new float[] { -1, 3, -2 }, PRECISION);
		assertThat(hand.palm.orientation.asArray()).containsExactly(new float[] { 1, 2, 3, 4 }, PRECISION);

		assertThatDigitCorrect(hand.digits.thumb, id, 0);
		assertThatDigitCorrect(hand.digits.index, id, 1);
		assertThatDigitCorrect(hand.digits.middle, id, 2);
		assertThatDigitCorrect(hand.digits.ring, id, 3);
		assertThatDigitCorrect(hand.digits.pinky, id, 4);

		assertThat(hand.arm.next_joint.asArray()).containsExactly(new float[] { 0.1f + id, 0.2f + id, 0.3f + id }, PRECISION);
		assertThat(hand.arm.prev_joint.asArray()).containsExactly(new float[] { 0.2f + id, 0.4f + id, 0.6f + id }, PRECISION);
		assertThat(hand.arm.width).isCloseTo(75, PRECISION);
		assertThat(hand.arm.rotation.asArray()).containsExactly(new float[] { 0.1f + id, 0.2f + id, 0.3f + id, 0.4f + id }, PRECISION);
	}


	private void assertThatDigitCorrect(LEAP_DIGIT digit, int handId, int id)
	{
		Offset<Float> precision = Offset.offset(0.001f);

		float x, y, z, rx, ry, rz, rw;
		x = rx = 1 + handId + id * 10.0f;
		y = ry = 2 + handId + id * 10.0f;
		z = rz = 3 + handId + id * 10.0f;
		rw = 4 + handId + id * 10.0f;
		
		assertThat(digit.finger_id).isEqualTo(handId * 5 + id);
		assertThat(digit.is_extended).isEqualTo(handId % 2);

		
		assertThat(digit.metacarpal.prev_joint.asArray()).containsExactly(new float[] { x++, y++, z++ }, precision);
		assertThat(digit.metacarpal.next_joint.asArray()).containsExactly(new float[] { x++, y++, z++ }, precision);
		assertThat(digit.metacarpal.width).isCloseTo(20, precision);
		assertThat(digit.metacarpal.rotation.asArray()).containsExactly(new float[] { rx++, ry++, rz++, rw++ }, precision);

		assertThat(digit.proximal.prev_joint.asArray()).containsExactly(new float[] { x++, y++, z++ }, precision);
		assertThat(digit.proximal.next_joint.asArray()).containsExactly(new float[] { x++, y++, z++ }, precision);
		assertThat(digit.proximal.width).isCloseTo(18, precision);
		assertThat(digit.proximal.rotation.asArray()).containsExactly(new float[] { rx++, ry++, rz++, rw++ }, precision);

		assertThat(digit.intermediate.prev_joint.asArray()).containsExactly(new float[] { x++, y++, z++ }, precision);
		assertThat(digit.intermediate.next_joint.asArray()).containsExactly(new float[] { x++, y++, z++ }, precision);
		assertThat(digit.intermediate.width).isCloseTo(16, precision);
		assertThat(digit.intermediate.rotation.asArray()).containsExactly(new float[] { rx++, ry++, rz++, rw++ }, precision);
		
		assertThat(digit.distal.prev_joint.asArray()).containsExactly(new float[] { x++, y++, z++ }, precision);
		assertThat(digit.distal.next_joint.asArray()).containsExactly(new float[] { x++, y++, z++ }, precision);
		assertThat(digit.distal.width).isCloseTo(14, precision);
		assertThat(digit.distal.rotation.asArray()).containsExactly(new float[] { rx++, ry++, rz++, rw++ }, precision);
	}


	@Test
	void LeapPollConnection_eventTypeLogEvent_mapsProperly()
	{
		LEAP_CONNECTION_MESSAGE message = assertLeapPollConnection(eLeapEventType.LogEvent);
		LEAP_LOG_EVENT event = message.getLogEvent();
		
		assertThat(event.severity).isEqualTo(eLeapLogSeverity.Information.value);
		assertThat(event.timestamp).isEqualTo(12345);
		assertThat(event.message).isEqualTo("Hello Leap!");
	}


	@Test
	void LeapPollConnection_eventTypeLogEvents_mapsProperly()
	{
		LEAP_CONNECTION_MESSAGE message = assertLeapPollConnection(eLeapEventType.LogEvents);
		LEAP_LOG_EVENTS event = message.getLogEvents();
		
		assertThat(event.nEvents).isEqualTo(2);
		assertThat(event.events).isNotEqualTo(Pointer.NULL);
		
		assertThat(event.getEvents()[0].severity).isEqualTo(eLeapLogSeverity.Information.value);
		assertThat(event.getEvents()[0].timestamp).isEqualTo(12345);
		assertThat(event.getEvents()[0].message).isEqualTo("Hello Leap!");

		assertThat(event.getEvents()[1].severity).isEqualTo(eLeapLogSeverity.Critical.value);
		assertThat(event.getEvents()[1].timestamp).isEqualTo(12346);
		assertThat(event.getEvents()[1].message).isEqualTo("Bye!");
	}


	@Test
	void LeapPollConnection_eventTypeConfigResponseEvent_mapsProperly()
	{
		LEAP_CONNECTION_MESSAGE message = assertLeapPollConnection(eLeapEventType.ConfigResponse);
		LEAP_CONFIG_RESPONSE_EVENT event = message.getConfigResponseEvent();
		
		assertThat(event.requestID).isEqualTo(1);
		assertThat(event.value.type).isEqualTo(eLeapValueType.Boolean.value);
		assertThat(event.value.union.boolValue).isEqualTo(true);
	}


	@Test
	void LeapPollConnection_eventTypeConfigChangeEvent_mapsProperly()
	{
		LEAP_CONNECTION_MESSAGE message = assertLeapPollConnection(eLeapEventType.ConfigChange);
		LEAP_CONFIG_CHANGE_EVENT event = message.getConfigChangeEvent();
		
		assertThat(event.requestID).isEqualTo(1);
		assertThat(event.status).isEqualTo(true);
	}


	@Test
	void LeapPollConnection_eventTypeHeadPoseEvent_mapsProperly()
	{
		LEAP_CONNECTION_MESSAGE message = assertLeapPollConnection(eLeapEventType.HeadPose);
		LEAP_HEAD_POSE_EVENT event = message.getHeadPoseEvent();
		
		assertThat(event.timestamp).isEqualTo(12345);
		assertThat(event.head_position.asArray()).containsExactly(new float[] { 0.1f, 0.2f, 0.3f }, PRECISION);
		assertThat(event.head_orientation.asArray()).containsExactly(new float[] { 0.4f, 0.3f, 0.2f, 0.1f }, PRECISION);
	}


	@Test
	void LeapPollConnection_eventTypeFrameDroppedEvent_mapsProperly()
	{
		LEAP_CONNECTION_MESSAGE message = assertLeapPollConnection(eLeapEventType.DroppedFrame);
		LEAP_DROPPED_FRAME_EVENT event = message.getDroppedFrameEvent();
		
		assertThat(event.frame_id).isEqualTo(1);
		assertThat(event.type).isEqualTo(eLeapDroppedFrameType.Other.value);
	}


	@Test
	void LeapPollConnection_eventTypePointMappingChangeEvent_mapsProperly()
	{
		LEAP_CONNECTION_MESSAGE message = assertLeapPollConnection(eLeapEventType.PointMappingChange);
		LEAP_POINT_MAPPING_CHANGE_EVENT event = message.getPointMappingChangeEvent();
		
		assertThat(event.frame_id).isEqualTo(1);
		assertThat(event.timestamp).isEqualTo(12345);
		assertThat(event.nPoints).isEqualTo(2);
	}


	@Test
	void LeapPollConnection_eventTypeImageEvent_mapsProperly()
	{
		LEAP_CONNECTION_MESSAGE message = assertLeapPollConnection(eLeapEventType.Image);
		LEAP_IMAGE_EVENT event = message.getImageEvent();

		assertThat(event.info.reserved).isNotEqualTo(Pointer.NULL);
		assertThat(event.info.reserved.getByte(0)).isEqualTo((byte) 1);
		assertThat(event.info.frame_id).isEqualTo(2);
		assertThat(event.info.timestamp).isEqualTo(3);

		assertThat(event.image).hasSize(2);
		assertThatImageCorrect(event.image[0], 0);
		assertThatImageCorrect(event.image[1], 1);
		
		// Can't test the calibration handle since it is an opaque type.
	}


	private void assertThatImageCorrect(LEAP_IMAGE image, int id)
	{
		assertThat(image.properties.type).isEqualTo(eLeapImageType.Default.value);
		assertThat(image.properties.format).isEqualTo(eLeapImageFormat.IR.value);
		assertThat(image.properties.bpp).isEqualTo(1);
		assertThat(image.properties.width).isEqualTo(16);
		assertThat(image.properties.height).isEqualTo(8);
		assertThat(image.properties.x_scale).isCloseTo(0.1f + id, PRECISION);
		assertThat(image.properties.y_scale).isCloseTo(0.2f + id, PRECISION);
		assertThat(image.properties.x_offset).isCloseTo(0.3f + id, PRECISION);
		assertThat(image.properties.y_offset).isCloseTo(0.4f + id, PRECISION);

		assertThat(image.matrix_version).isEqualTo(1);
		assertThat(image.offset).isEqualTo(2);
		
		assertThat(image.distortion_matrix).isNotEqualTo(Pointer.NULL);
		assertThatMatrixCorrect(image.getMatrix(), id);
		
		assertThat(image.data).isNotEqualTo(Pointer.NULL);
		assertThatImageDataCorrect(image.getData(), id);
	}


	private void assertThatMatrixCorrect(LEAP_DISTORTION_MATRIX matrix, int imageId)
	{
		int matrixSize = LEAP_DISTORTION_MATRIX.LEAP_DISTORTION_MATRIX_N
				* LEAP_DISTORTION_MATRIX.LEAP_DISTORTION_MATRIX_N * 2;
		
		float[] expectedMatrix = new float[matrixSize];
		
		for (int i = 0; i < matrixSize; i++)
		{
			expectedMatrix[i] = i / (imageId + 1f);
		}
		
		assertThat(matrix.matrix).containsExactly(expectedMatrix, PRECISION);
	}


	private void assertThatImageDataCorrect(byte[] data, int imageId)
	{
		byte[] expectedData = new byte[data.length];
		
		for (int i = 0; i < expectedData.length; i++)
		{
			int value = i * (imageId + 1);
			
			expectedData[i] = (byte)(value % 256);
		}
		
		assertThat(data).containsExactly(expectedData);
	}


	private LEAP_CONNECTION_MESSAGE assertLeapPollConnection(eLeapEventType eventType)
	{
		LEAP_CONNECTION_MESSAGE message = new LEAP_CONNECTION_MESSAGE();
		message.type = eventType.value;

		eLeapRS result = LeapC.INSTANCE.LeapPollConnection(null, 0, message);
		assertThat(result).isEqualTo(eLeapRS.Success);
		assertThat(message.type).isEqualTo(eventType.value);
		
		return message;
	}
}
