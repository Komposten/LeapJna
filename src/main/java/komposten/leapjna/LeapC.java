package komposten.leapjna;

import java.util.HashMap;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

import komposten.leapjna.leapc.enums.eLeapConnectionStatus;
import komposten.leapjna.leapc.enums.eLeapEventType;
import komposten.leapjna.leapc.enums.eLeapHandType;
import komposten.leapjna.leapc.enums.eLeapRS;
import komposten.leapjna.util.LeapTypeMapper;


public interface LeapC extends Library
{
	public LeapC INSTANCE = (LeapC) Native
			.synchronizedLibrary(Native.load("LeapC", LeapC.class, new HashMap<String, Object>()
			{
				{
					put(Library.OPTION_TYPE_MAPPER, new LeapTypeMapper());
				}
			}));

	public long LeapGetNow();


	//TODO Maybe replace all eLeapRS returns with ints?
	//	Might improve performance a tiny bit.
	public eLeapRS LeapCreateConnection(LEAP_CONNECTION_CONFIG pConfig,
			LEAP_CONNECTION phConnection);


	public eLeapRS LeapOpenConnection(Pointer hConnection);


	public eLeapRS LeapPollConnection(Pointer hConnection, int timeout,
			LEAP_CONNECTION_MESSAGE message);


	public eLeapRS LeapGetConnectionInfo(Pointer hConnection,
			LEAP_CONNECTION_INFO.ByReference pInfo);


	public eLeapRS LeapGetFrameSize(Pointer hConnection, long timestamp,
			LongByReference pncbEvent);


	public eLeapRS LeapInterpolateFrame(Pointer hConnection, long timestamp,
			LEAP_TRACKING_EVENT.ByReference pEvent, long ncbEvent);


	public static class LEAP_CONNECTION extends PointerByReference
	{
		public LEAP_CONNECTION()
		{
			super();
		}
	}


	@FieldOrder({ "size", "flags", "server_namespace" })
	public static class LEAP_CONNECTION_CONFIG extends Structure
	{
		public int size;
		public int flags;
		public String server_namespace;
	}


	@FieldOrder({ "size", "status" })
	public static class LEAP_CONNECTION_INFO extends Structure
	{
		public int size;
		/**
		 * The connection status as an int. Use {@link #getStatus()} to get the
		 * status as a {@link eLeapConnectionStatus} value.
		 */
		public int status;
		
		private eLeapConnectionStatus statusE;
		
		
		public eLeapConnectionStatus getStatus()
		{
			if (statusE == null)
			{
				statusE = eLeapConnectionStatus.parse(status, eLeapConnectionStatus.Unknown);
			}
			
			return statusE;
		}
		
		public static class ByReference extends LEAP_CONNECTION_INFO
				implements Structure.ByReference
		{
		}
	}


	@FieldOrder({ "size", "type", "pEvent" })
	public static class LEAP_CONNECTION_MESSAGE extends Structure
	{
//		public static class EventUnion extends Union
//		{
//			public Pointer pointer;
//			public Pointer connection_event;
//			public Pointer connection_lost_event;
//			public Pointer device_event;
//			public Pointer device_status_change_event;
//			public Pointer policy_event;
//			public Pointer device_failure_event;
//			public Pointer tracking_event;
//			public Pointer log_event;
//			public Pointer log_events;
//			public Pointer config_response_event;
//			public Pointer config_change_event;
//			public Pointer dropped_frame_event;
//			public Pointer image_event;
//			public Pointer point_mapping_change_event;
//			public Pointer head_pose_event;
//		}

		public int size;
		/**
		 * The event type. Use {@link #getType()} to get the type as a
		 * {@link eLeapEventType} value.
		 */
		public short type;
		/**
		 * A pointer to the event data. Check the event type using <code>type</code>
		 * and then call the getter for that event type to get the event data.
		 */
		public Pointer pEvent;
		
		private Structure event;
		private eLeapEventType typeE;
		
		public LEAP_CONNECTION_MESSAGE()
		{
		}
		
		
		public LEAP_CONNECTION_MESSAGE(Pointer pointer)
		{
			super(pointer);
			read();
		}
		
		
		public eLeapEventType getType()
		{
			if (typeE == null)
			{
				typeE = eLeapEventType.parse(type, eLeapEventType.None);
			}
			
			return typeE;
		}
		
		
		public LEAP_TRACKING_EVENT getTrackingEvent()
		{
			checkType(eLeapEventType.Tracking);
			
			if (event == null)
				event = new LEAP_TRACKING_EVENT(pEvent);
			
			return (LEAP_TRACKING_EVENT) event;
		}
		
		
		private void checkType(eLeapEventType eventType)
		{
			if (type != eventType.value)
			{
				throw new IllegalStateException(
						"Incorrect event type: " + typeE + " != " + eventType);
			}
		}
	}
	
	
	@FieldOrder({ "info", "tracking_frame_id", "nHands", "pHands", "framerate" })
	public static class LEAP_TRACKING_EVENT extends Structure
	{
		public LEAP_FRAME_HEADER info;
		public long tracking_frame_id;
		public int nHands;

		public Pointer pHands;
		public float framerate;
		
		private LEAP_HAND[] hands;
		
		public LEAP_TRACKING_EVENT()
		{
			super(ALIGN_NONE);
		}
		
		public LEAP_TRACKING_EVENT(Pointer pointer)
		{
			super(pointer, ALIGN_NONE);
			read();
		}
		
		
		public LEAP_HAND[] getHands()
		{
			return hands;
		}
		
		
		@Override
		public void read()
		{
			super.read();
			
			hands = new LEAP_HAND[nHands];
			if (nHands > 0)
			{
				for (int i = 0; i < nHands; i++)
				{
					int offset = i * LEAP_HAND.SIZE;
					hands[i] = new LEAP_HAND(pHands.share(offset));
				}
			}
		}


		public static class ByReference extends LEAP_TRACKING_EVENT
				implements Structure.ByReference
		{
		}
	}


	@FieldOrder({ "reserved", "frame_id", "timestamp" })
	public static class LEAP_FRAME_HEADER extends Structure
	{
		public Pointer reserved;
		public long frame_id;
		public long timestamp;
	}


	@FieldOrder({ "id", "flags", "type", "confidence", "visible_time", "pinch_distance",
			"grab_angle", "pinch_strength", "grab_strength", "palm", "digits", "arm" })
	public static class LEAP_HAND extends Structure
	{
		//FIXME Calculate this size dynamically. Is a must to work on
		// e.g. 32-bit systems (pointers are 4 bytes instead of 8).
		static final int SIZE = 1084;
		@FieldOrder({ "thumb", "index", "middle", "ring", "pinky" })
		public static class DigitStruct extends Structure
		{
			public LEAP_DIGIT thumb;
			public LEAP_DIGIT index;
			public LEAP_DIGIT middle;
			public LEAP_DIGIT ring;
			public LEAP_DIGIT pinky;
			
			/**
			 * @return All digits as an array in the order: thumb, index, middle, ring, pinky.
			 */
			public LEAP_DIGIT[] asArray()
			{
				return new LEAP_DIGIT[] { thumb, index, middle, ring, pinky };
			}
		}

		public int id;
		public int flags;
		/**
		 * The hand type as a byte. Either 0 (left) or 1 (right).
		 * Use {@link #getType()} to get the type as a {@link eLeapHandType} value. 
		 */
		public byte type;
		public float confidence;
		public long visible_time;
		public float pinch_distance;
		public float grab_angle;
		public float pinch_strength;
		public float grab_strength;
		public LEAP_PALM palm;
		public DigitStruct digits;
		public LEAP_BONE arm;
		
		private eLeapHandType typeE;
		
		
		public LEAP_HAND()
		{
			super();
			read();
		}
		
		
		public LEAP_HAND(Pointer pointer)
		{
			super(pointer);
			read();
		}
		
		
		public eLeapHandType getType()
		{
			if (typeE == null)
			{
				typeE = eLeapHandType.parse(type, eLeapHandType.Unknown);
			}
			
			return typeE;
		}

		public static class ByReference extends LEAP_HAND implements Structure.ByReference
		{
		}
	}


	@FieldOrder({ "prev_joint", "next_joint", "width", "rotation" })
	public static class LEAP_BONE extends Structure
	{
		public LEAP_VECTOR prev_joint;
		public LEAP_VECTOR next_joint;
		public float width;
		public LEAP_QUATERNION rotation;
	}


	@FieldOrder({ "position", "stabilized_position", "velocity", "normal", "width",
			"direction", "orientation" })
	public static class LEAP_PALM extends Structure
	{
		public LEAP_VECTOR position;
		public LEAP_VECTOR stabilized_position;
		public LEAP_VECTOR velocity;
		public LEAP_VECTOR normal;
		public float width;
		public LEAP_VECTOR direction;
		public LEAP_QUATERNION orientation;
	}


	@FieldOrder({ "finger_id", "metacarpal", "proximal", "intermediate", "distal", "is_extended" })
	public static class LEAP_DIGIT extends Structure
	{
		public int finger_id;
		public LEAP_BONE metacarpal;
		public LEAP_BONE proximal;
		public LEAP_BONE intermediate;
		public LEAP_BONE distal;
		public int is_extended;

		/**
		 * @return The digit's bones as an array in the order: metacarpal, proximal, intermediate, distal.
		 */
		public LEAP_BONE[] boneArray()
		{
			return new LEAP_BONE[] { metacarpal, proximal, intermediate, distal };
		}
	}


	@FieldOrder({ "x", "y", "z" })
	public static class LEAP_VECTOR extends Structure
	{
		public float x;
		public float y;
		public float z;
	}


	@FieldOrder({ "x", "y", "z", "w" })
	public static class LEAP_QUATERNION extends Structure
	{
		public float x;
		public float y;
		public float z;
		public float w;
	}
}