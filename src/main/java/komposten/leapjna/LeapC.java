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
		public eLeapConnectionStatus status;
		
		public static class ByReference extends LEAP_CONNECTION_INFO
				implements Structure.ByReference
		{
		}
	}


	@FieldOrder({ "size", "type", "union" })
	public static class LEAP_CONNECTION_MESSAGE extends Structure
	{
		//TODO Add getters for the different event types (or something like that).
		//			Can probably collapse the union into a single Pointer field.
		public static class EventUnion extends Union
		{
			public Pointer pointer;
			public Pointer connection_event;
			public Pointer connection_lost_event;
			public Pointer device_event;
			public Pointer device_status_change_event;
			public Pointer policy_event;
			public Pointer device_failure_event;
			public Pointer tracking_event;
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
		public short type;
		public EventUnion union;
		
		private eLeapEventType typeE;
		
		public LEAP_CONNECTION_MESSAGE()
		{
		}
		
		
		public LEAP_CONNECTION_MESSAGE(Pointer pointer)
		{
			super(pointer);
			read();
		}
		
		@Override
		public void read()
		{
			super.read();

			typeE = eLeapEventType.None.getForValue(type);
			
			if (typeE == null)
			{
				typeE = eLeapEventType.None;
			}
			
			
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
			
			union.read();
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
			"grab_angle", "pinch_strength", "grab_strength", "palm", "digitUnion", "arm" })
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
		}


		@FieldOrder({ "struct", "digits" })
		public static class DigitUnion extends Union
		{
			public DigitStruct digitStruct;
			public LEAP_DIGIT[] digits = new LEAP_DIGIT[5];
		}


		public int id;
		public int flags;
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
			read();
		}
		
		
		public LEAP_HAND(Pointer pointer)
		{
			super(pointer);
			read();
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