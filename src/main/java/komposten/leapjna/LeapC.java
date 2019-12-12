package komposten.leapjna;

import java.util.HashMap;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.Union;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

import komposten.leapjna.leapc.eLeapConnectionStatus;
import komposten.leapjna.leapc.eLeapEventType;
import komposten.leapjna.leapc.eLeapHandType;
import komposten.leapjna.leapc.eLeapRS;
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


	public eLeapRS LeapCreateConnection(LEAP_CONNECTION_CONFIG pConfig,
			PointerByReference phConnection);


	public eLeapRS LeapOpenConnection(Pointer hConnection);


	public eLeapRS LeapPollConnection(Pointer hConnection, int timeout,
			LEAP_CONNECTION_MESSAGE.ByReference message);


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
		public eLeapConnectionStatus status;
		
		public static class ByReference extends LEAP_CONNECTION_INFO
				implements Structure.ByReference
		{
		}
	}


	@FieldOrder({ "size", "type", "union" })
	public static class LEAP_CONNECTION_MESSAGE extends Structure
	{
		public static class EventUnion extends Union
		{
			public Pointer pointer;
			public Pointer connection_event;
			public Pointer connection_lost_event;
			public Pointer device_event;
			public Pointer device_status_change_event;
			public Pointer policy_event;
			public Pointer device_failure_event;
			public LEAP_TRACKING_EVENT.ByReference tracking_event;
			public Pointer log_event;
			public Pointer log_events;
			public Pointer config_response_event;
			public Pointer config_change_event;
			public Pointer dropped_frame_event;
			public Pointer image_event;
			public Pointer point_mapping_change_event;
			public Pointer head_pose_event;
		}


		public int size;
		//public IntByReference type;
		public short type;
		//CURRENT Trying to create a ShortEnum type. If it doesn't work, settle for
		// using short straight up.
		private eLeapEventType typeE;
		public EventUnion union;
		
		@Override
		public void read()
		{
			super.read();

			typeE = eLeapEventType.None.getForValue(type);
			
			if (typeE == null)
			{
				typeE = eLeapEventType.None;
			}
			
//			if (type == null)
//			{
//				type = eLeapEventType.None;
//			}
			
			
			switch (typeE)
			{
				case ConfigChange :
					union.setType("config_change_event");
					break;
				case ConfigResponse :
					union.setType("config_response_event");
					break;
				case Connection :
					union.setType("connection_event");
					break;
				case ConnectionLost :
					union.setType("connection_lost_event");
					break;
				case Device :
					union.setType("device_event");
					break;
				case DeviceFailure :
					union.setType("device_failure_event");
					break;
				case DeviceLost :
					union.setType("device_event");
					break;
				case DeviceStatusChange :
					union.setType("device_status_change_event");
					break;
				case DroppedFrame :
					union.setType("dropped_frame_event");
					break;
				case HeadPose :
					union.setType("head_pose_event");
					break;
				case Image :
					union.setType("image_event");
					break;
				case ImageComplete :
					break;
				case ImageRequestError :
					break;
				case LogEvent :
					union.setType("log_event");
					break;
				case LogEvents :
					union.setType("log_events");
					break;
				case None :
					union.setType("pointer");
					break;
				case PointMappingChange :
					union.setType("point_mapping_change_event");
					break;
				case Policy :
					union.setType("policy_event");
					break;
				case Tracking :
					union.setType("tracking_event");
					break;
				default :
					System.out.println("Unknown message type: " + typeE);
					break;
			}
			
			System.out.println("Union type: " + typeE);
			
			union.read();
		}


		public static class ByReference extends LEAP_CONNECTION_MESSAGE
				implements Structure.ByReference
		{
		}
	}
	
	
	@FieldOrder({ "info", "tracking_frame_id", "nHands", "pHands", "framerate" })
	public static class LEAP_TRACKING_EVENT extends Structure
	{
		public LEAP_FRAME_HEADER info;
		public long tracking_frame_id;
		public int nHands;
		public Pointer pHands;
		//public LEAP_HAND pHands[] = new LEAP_HAND[1];
		public float framerate;
		
		private LEAP_HAND[] hands;
		
		
		@Override
		public void read()
		{
			super.read();
			
			/*
			 * CURRENT
			 * - info, tracking_frame_id and nHands are all read correctly.
			 * - The hands and framerate are incorrect. BUT! The hand info does seem to come
			 * soon after the nHands variable in memory since the current setup has palm Z change
			 * depending on hand position (but not accurate by any means)!
			 */
			System.out.println("======================");
			System.out.println("ID: " + tracking_frame_id);
			System.out.println("Hands: " + nHands);
			
			if (nHands > 0)
			{
				LEAP_HAND hand = new LEAP_HAND(pHands);
				
				System.out.println("Hi");
			}
//			System.out.println("===");
//			System.out.println(pHands[0].id);
//			System.out.println(pHands[0].flags);
//			System.out.println(pHands[0].confidence);
//			System.out.println(pHands[0].type);
//			System.out.println(pHands[0].palm.position.union.struct.z);
//			System.out.println("===");
			System.out.println("Framerate: " + framerate);
			System.out.println("======================");
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
			"grab_angle", "pinch_strength", "grab_strength", "palm", "digitUnion", "arm" })
	public static class LEAP_HAND extends Structure
	{
		@FieldOrder({ "thumb", "index", "middle", "ring", "pinky" })
		public static class DigitStruct extends Structure
		{
			public LEAP_DIGIT thumb;
			public LEAP_DIGIT index;
			public LEAP_DIGIT middle;
			public LEAP_DIGIT ring;
			public LEAP_DIGIT pinky;
		}


		@FieldOrder({ "struct", "digits" })
		public static class DigitUnion extends Union
		{
			public DigitStruct digitStruct;
			public LEAP_DIGIT[] digits = new LEAP_DIGIT[5];
		}


		public int id;
		public int flags;
		//public eLeapHandType type; //Commented out since we only have 0 and 1 as values so int is probably too large.
		public byte type;
		public float confidence;
		public long visible_time;
		public float pinch_distance;
		public float grab_angle;
		public float pinch_strength;
		public float grab_strength;
		public LEAP_PALM palm;
		public DigitUnion digitUnion;
		public LEAP_BONE arm;
		
		
		public LEAP_HAND()
		{
			super();
		}
		
		
		public LEAP_HAND(Pointer pointer)
		{
			super(pointer);
		}
		
		@Override
		public void read()
		{
			super.read();
			digitUnion.setType(DigitStruct.class);
			digitUnion.read();
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


	@FieldOrder({ "finger_id", "boneUnion", "is_extended" })
	public static class LEAP_DIGIT extends Structure
	{
		@FieldOrder({ "metacarpal", "proximal", "intermediate", "distal" })
		public static class BoneStruct extends Structure
		{
			public LEAP_BONE metacarpal;
			public LEAP_BONE proximal;
			public LEAP_BONE intermediate;
			public LEAP_BONE distal;
		}


		@FieldOrder({ "bones", "struct" })
		public static class BoneUnion extends Union
		{
			public LEAP_BONE[] bones = new LEAP_BONE[4];
			public BoneStruct boneStruct;
		}

		public int finger_id;
		public BoneUnion boneUnion;
		public int is_extended;
		
		@Override
		public void read()
		{
			super.read();
			boneUnion.setType(BoneStruct.class);
			boneUnion.read();
		}
	}


	@FieldOrder({ "union" })
	public static class LEAP_VECTOR extends Structure
	{
		@FieldOrder({ "x", "y", "z" })
		public static class VectorStruct extends Structure
		{
			public float x;
			public float y;
			public float z;
		}


		@FieldOrder({ "v", "struct" })
		public static class VectorUnion extends Union
		{
			public float[] v = new float[3];
			public VectorStruct struct;
		}

		public VectorUnion union;
		
		@Override
		public void read()
		{
			super.read();
			union.setType(VectorStruct.class);
			union.read();
		}
	}


	@FieldOrder({ "union" })
	public static class LEAP_QUATERNION extends Structure
	{
		@FieldOrder({ "x", "y", "z", "w" })
		public static class QuaternionStruct extends Structure
		{
			public float x;
			public float y;
			public float z;
			public float w;
		}


		@FieldOrder({ "v", "struct" })
		public static class QuaternionUnion extends Union
		{
			public float[] v = new float[4];
			public QuaternionStruct struct;
		}

		public QuaternionUnion union;
		
		@Override
		public void read()
		{
			super.read();
			union.setType(QuaternionStruct.class);
			union.read();
		}
	}
}