package komposten.leapjna;

import java.util.HashMap;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

import komposten.leapjna.leapc.data.LEAP_CONNECTION_INFO;
import komposten.leapjna.leapc.enums.eLeapEventType;
import komposten.leapjna.leapc.enums.eLeapRS;
import komposten.leapjna.leapc.events.LEAP_TRACKING_EVENT;
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


	/**
	 * @param hConnection
	 * @param timestamp
	 * @param pEvent A LEAP_TRACKING_EVENT pointer with enough allocated space to fit the
	 *          frame data. Use {@link LEAP_TRACKING_EVENT.ByReference#ByReference(int)}
	 *          to set it up.
	 * @param ncbEvent
	 * @return
	 */
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
}