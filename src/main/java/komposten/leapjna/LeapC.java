package komposten.leapjna;

import java.util.HashMap;
import java.util.function.Function;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.ptr.LongByReference;

import komposten.leapjna.leapc.data.LEAP_ALLOCATOR;
import komposten.leapjna.leapc.data.LEAP_CONNECTION;
import komposten.leapjna.leapc.data.LEAP_CONNECTION_INFO;
import komposten.leapjna.leapc.data.LEAP_POINT_MAPPING;
import komposten.leapjna.leapc.data.LEAP_VARIANT;
import komposten.leapjna.leapc.enums.eLeapEventType;
import komposten.leapjna.leapc.enums.eLeapPolicyFlag;
import komposten.leapjna.leapc.enums.eLeapRS;
import komposten.leapjna.leapc.events.LEAP_CONFIG_CHANGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_CONFIG_RESPONSE_EVENT;
import komposten.leapjna.leapc.events.LEAP_CONNECTION_EVENT;
import komposten.leapjna.leapc.events.LEAP_CONNECTION_LOST_EVENT;
import komposten.leapjna.leapc.events.LEAP_DEVICE_EVENT;
import komposten.leapjna.leapc.events.LEAP_DEVICE_FAILURE_EVENT;
import komposten.leapjna.leapc.events.LEAP_DEVICE_STATUS_CHANGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_DROPPED_FRAME_EVENT;
import komposten.leapjna.leapc.events.LEAP_EVENT;
import komposten.leapjna.leapc.events.LEAP_HEAD_POSE_EVENT;
import komposten.leapjna.leapc.events.LEAP_IMAGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_LOG_EVENT;
import komposten.leapjna.leapc.events.LEAP_LOG_EVENTS;
import komposten.leapjna.leapc.events.LEAP_POINT_MAPPING_CHANGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_POLICY_EVENT;
import komposten.leapjna.leapc.events.LEAP_TRACKING_EVENT;
import komposten.leapjna.leapc.util.Configurations;
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

	/**
	 * <p>
	 * Samples the universal clock used by the system to timestamp image and tracking
	 * frames. The returned counter value is given in microseconds since an epoch time.
	 * <p>
	 * <p>
	 * The clock used for the counter itself is implementation-defined, but generally
	 * speaking, it is global, monotonic, and makes use of the most accurate
	 * high-performance counter available on the system.
	 * <p>
	 * 
	 * @return microseconds since an unspecified epoch
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#ga4ef33708af974ecd618ad9784aa38161">LeapC
	 *      API - LeapGetNow</a>
	 */
	public long LeapGetNow();


	/**
	 * <p>
	 * Creates a new connection to the Leap Motion service and stores a handle for the
	 * connection in the provided {@link LEAP_CONNECTION}.
	 * </p>
	 * <p>
	 * Pass the LEAP_CONNECTION pointer to {@link #LeapOpenConnection(Pointer)} to establish
	 * a connection to the Leap Motion service; and to subsequent operations on the same
	 * connection.
	 * </p>
	 * 
	 * @param pConfig The configuration to be used with the newly created connection. If
	 *          pConfig is null, a connection is created with a default configuration.
	 * @param phConnection Receives a pointer to the connection object.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#ga5bcc831cf503136f45dde040f29973b5">LeapC
	 *      API - LeapCreateConnection</a>
	 */
	public eLeapRS LeapCreateConnection(LEAP_CONNECTION_CONFIG pConfig,
			LEAP_CONNECTION phConnection);


	/**
	 * <p>
	 * Opens a connection to the Leap Motion service.
	 * </p>
	 * <p>
	 * This routine will not block. A connection to the service will not be established
	 * until the first invocation of
	 * {@link #LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)}.
	 * </p>
	 * 
	 * @param hConnection A handle to the connection object, created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#ga49a90e9ca6d880d59fa3f1813689f7fd">LeapC
	 *      API - LeapOpenConnection</a>
	 */
	public eLeapRS LeapOpenConnection(Pointer hConnection);


	/**
	 * <p>
	 * Polls the connection for a new event.
	 * </p>
	 * <p>
	 * The specific types of events that may be received are not configurable in this
	 * entrypoint. Configure the device or connection object directly to change what events
	 * will be received.
	 * </p>
	 * <p>
	 * Pointers in the retrieved event message structure will be valid until the associated
	 * connection or device is closed, or the next call to
	 * <code>LeapPollConnection()</code>.
	 * </p>
	 * <p>
	 * Calling this method concurrently will return {@link eLeapRS#ConcurrentPoll}.
	 * </p>
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param timeout The maximum amount of time to wait, in milliseconds. If this value is
	 *          zero, the <code>message</code> pointer references the next queued message,
	 *          if there is one, and returns immediately.
	 * @param message A structure that is filled with event information. This structure will
	 *          be valid as long as the <code>LEAP_CONNECTION</code> object is valid.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration. If
	 *         the operation times out, this method will return {@link eLeapRS#Timeout} and
	 *         the <code>message</code> pointer will reference a message of type
	 *         {@link eLeapEventType#None}.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#ga2a8aecad339f0fd339ca22a3e7b389f6">LeapC
	 *      API - LeapPollConnection</a>
	 */
	public eLeapRS LeapPollConnection(Pointer hConnection, int timeout,
			LEAP_CONNECTION_MESSAGE message);


	/**
	 * 
	 * <p>
	 * Retrieves status information about the specified connection.
	 * </p>
	 * <p>
	 * Call {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 * LeapCreateConnection()} to generate the handle for the connection; call
	 * {@link #LeapOpenConnection(Pointer) LeapOpenConnection} to establish the connection;
	 * then call this function to check the connection status.
	 * </p>
	 * 
	 * @param hConnection The handle of the connection of interest. Created by
	 *          <code>LeapCreateConnection()</code>. Use {@link LEAP_CONNECTION#handle} to
	 *          obtain the handle from the connection object.
	 * @param pInfo A pointer to a structure that receives additional connection
	 *          information. On input, the size field of <code>pInfo</code> is the size of
	 *          the buffer (i.e. the size of a {@link LEAP_CONNECTION_INFO} struct); On
	 *          output, the size field of <code>pInfo</code> receives the size necessary to
	 *          hold the entire information block.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#ga5225116e8e8fad45d0e21e0ec7e3af76">LeapC
	 *      API - LeapGetConnectionInfo</a>
	 */
	public eLeapRS LeapGetConnectionInfo(Pointer hConnection,
			LEAP_CONNECTION_INFO.ByReference pInfo);


	/**
	 * <p>
	 * Sets the allocator functions to use for a particular connection.
	 * </p>
	 * <p>
	 * If user-supplied allocator functions are not supplied, the functions that require
	 * dynamic memory allocation will not be available.
	 * </p>
	 * <p>
	 * <b>Note:</b> Not required for e.g. {@link LEAP_IMAGE_EVENT}s even though their
	 * documentation says otherwise.
	 * </p>
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param allocator A {@link LEAP_ALLOCATOR} structure containing the allocator
	 *          functions to be called as needed by the library.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 */
	public eLeapRS LeapSetAllocator(Pointer hConnection, LEAP_ALLOCATOR allocator);


	/**
	 * <p>
	 * Retrieves the number of bytes required to allocate an interpolated frame at the
	 * specified time.
	 * </p>
	 * <p>
	 * Use this function to determine the size of the buffer to allocate when calling
	 * {@link #LeapInterpolateFrame(Pointer, long, LEAP_TRACKING_EVENT, long)}.
	 * </p>
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param timestamp The timestamp of the frame whose size is to be queried.
	 * @param pncbEvent A pointer that receives the number of bytes required to store the
	 *          specified frame.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#gae680ca44ccf77a25c4a61f9ae1a311bc">LeapC
	 *      API - LeapGetFrameSize</a>
	 */
	public eLeapRS LeapGetFrameSize(Pointer hConnection, long timestamp,
			LongByReference pncbEvent);


	/**
	 * <p>
	 * Constructs a frame at the specified timestamp by interpolating between measured
	 * frames.
	 * </p>
	 * <p>
	 * Caller is responsible for allocating a buffer large enough to hold the data of the
	 * frame. Use {@link #LeapGetFrameSize(Pointer, long, LongByReference)} to calculate the
	 * minimum size of this buffer.
	 * </p>
	 * <p>
	 * Use LeapCreateClockRebaser(), LeapUpdateRebase(), and LeapRebaseClock() to
	 * synchronize time measurements in the application with time measurements in the Leap
	 * Motion service. This process is required to achieve accurate, smooth interpolation.
	 * </p>
	 * TODO Update method references when rebasing has been added.
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param timestamp The timestamp at which to interpolate the frame data.
	 * @param pEvent A <code>LEAP_TRACKING_EVENT</code> with enough allocated memory to fit
	 *          the frame data. Use <code>LeapGetFrameSize</code> to get the required size,
	 *          and then {@link LEAP_TRACKING_EVENT#LEAP_TRACKING_EVENT(int)} to create the
	 *          struct and allocate memory.
	 * @param ncbEvent The size of the <code>pEvent</code> struct in bytes.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#ga7cbdc29069fbcd6aca1a16989722e85c">LeapC
	 *      API - LeapInterpolateFrame</a>
	 */
	public eLeapRS LeapInterpolateFrame(Pointer hConnection, long timestamp,
			LEAP_TRACKING_EVENT pEvent, long ncbEvent);


	/**
	 * <p>
	 * Gets the head tracking pose at the specified timestamp by interpolating between
	 * measured frames.
	 * </p>
	 * <p>
	 * Use LeapCreateClockRebaser(), LeapUpdateRebase(), and LeapRebaseClock() to
	 * synchronize time measurements in the application with time measurements in the Leap
	 * Motion service. This process is required to achieve accurate, smooth interpolation.
	 * </p>
	 * TODO Test this to make sure everything is working. <br/>
	 * TODO Update method references when rebasing has been added.
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param timestamp The timestamp at which to interpolate the frame data.
	 * @param pEvent A pointer to a flat buffer which is filled with an interpolated frame.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#gab756331205d6ee0cd61708d77d968536">LeapC
	 *      API - LeapInterpolateHeadPose</a>
	 */
	public eLeapRS LeapInterpolateHeadPose(Pointer hConnection, long timestamp,
			LEAP_HEAD_POSE_EVENT pEvent);


	/**
	 * <p>
	 * Sets or clears one or more policy flags.
	 * </p>
	 * <p>
	 * Changing policies is asynchronous. After you call this function, a subsequent call to
	 * {@link #LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)} provides a
	 * {@link LEAP_POLICY_EVENT} containing the current policies, reflecting any changes.
	 * </p>
	 * To get the current policies without changes, specify zero for both the set and clear
	 * parameters. When ready, <code>LeapPollConnection()</code> provides the a
	 * <code>LEAP_POLICY_EVENT</code> containing the current settings.
	 * <p>
	 * The {@link eLeapPolicyFlag} enumeration defines the policy flags.
	 * </p>
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param set A bitwise combination of flags to be set. Set to 0 if not setting any
	 *          flags.
	 * @param clear A bitwise combination of flags to be cleared. Set to 0 if not clearing
	 *          any flags.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#gab57050814a0763ec07ed088e3d2de7f2">LeapC
	 *      API - LeapSetPolicyFlags</a>
	 * @see {@link eLeapPolicyFlag#createMask(eLeapPolicyFlag...)}
	 */
	public eLeapRS LeapSetPolicyFlags(Pointer hConnection, long set, long clear);


	/**
	 * <p>
	 * Requests the current value of a service configuration setting.
	 * </p>
	 * <p>
	 * The value is fetched asynchronously since it requires a service transaction.
	 * {@link #LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)} returns a
	 * {@link LEAP_CONFIG_RESPONSE_EVENT} structure when the request has been processed. Use
	 * the <code>pRequestID</code> value to correlate the response to the originating
	 * request.
	 * </p>
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param key The key of the configuration to request. See {@link Configurations} for a
	 *          list of keys.
	 * @param pRequestID A pointer to a memory location to which the id for this request is
	 *          written.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#ga6fdd92015369e64bc8d09d1cbc77fb46">LeapC
	 *      API - LeapRequestConfigValue</a>
	 */
	public eLeapRS LeapRequestConfigValue(Pointer hConnection, String key,
			LongByReference pRequestID);


	/**
	 * <p>
	 * Causes the client to commit a configuration change to the Leap Motion service.
	 * </p>
	 * <p>
	 * The change is performed asynchronously – and may fail.
	 * {@link #LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)} returns a
	 * {@link LEAP_CONFIG_CHANGE_EVENT} structure when the request has been processed. Use
	 * the <code>pRequestID</code> value to correlate the response to the originating
	 * request.
	 * </p>
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param key The key of the configuration to commit. See {@link Configurations} for a
	 *          list of keys.
	 * @param value The value of the configuration to commit.
	 * @param pRequestID A pointer to a memory location to which the id for this request is
	 *          written, or a null pointer if this value is not needed.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#ga8f80709d76bd235949e295055cd3bf9d">LeapC
	 *      API - LeapSaveConfigValue</a>
	 */
	public eLeapRS LeapSaveConfigValue(Pointer hConnection, String key, LEAP_VARIANT value,
			LongByReference pRequestID);


	/**
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param pSize A pointer that receives the number of bytes required to store the point
	 *          mapping.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 */
	public eLeapRS LeapGetPointMappingSize(Pointer hConnection, LongByReference pSize);


	/**
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param pointMapping A <code>LEAP_POINT_MAPPING</code> with enough allocated memory to
	 *          fit the frame data. Use
	 *          {@link #LeapGetPointMappingSize(Pointer, LongByReference)} to get the
	 *          required size, and then {@link LEAP_POINT_MAPPING#LEAP_POINT_MAPPING(int)}
	 *          to create the struct and allocate memory.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 */
	public eLeapRS LeapGetPointMapping(Pointer hConnection, LEAP_POINT_MAPPING pointMapping,
			LongByReference pSize);


	/**
	 * <p>
	 * Specifies the configuration for a connection.
	 * </p>
	 * <p>
	 * Currently, there are no externally useful configuration options.
	 * </p>
	 * 
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___c_o_n_n_e_c_t_i_o_n___c_o_n_f_i_g">LeapC
	 *      API - LEAP_CONNECTION_CONFIG</a>
	 */
	@FieldOrder({ "size", "flags", "server_namespace" })
	public static class LEAP_CONNECTION_CONFIG extends Structure
	{
		/** Set to the final size of this structure. */
		public int size;

		/** The connection configuration flags. */
		public int flags;

		/** For internal use. */
		public String server_namespace;
	}


	/**
	 * Defines a basic message from the LeapC message queue.
	 * 
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___c_o_n_n_e_c_t_i_o_n___m_e_s_s_a_g_e">LeapC
	 *      API - LEAP_CONNECTION_MESSAGE</a>
	 */
	@FieldOrder({ "size", "type", "pEvent" })
	public static class LEAP_CONNECTION_MESSAGE extends Structure
	{
		/** The size of this message struct. */
		public int size;

		/**
		 * The event type. Use {@link #getType()} to get the type as a {@link eLeapEventType}
		 * value.
		 */
		public short type;

		/**
		 * <p>
		 * A pointer to the event data for the current type of message.
		 * </p>
		 * <p>
		 * Check the event type using {@link #type} or {@link #getType()} and then call the
		 * relevant <code>get***Event()</code> method for that event type to get the event
		 * data.
		 * </p>
		 */
		public Pointer pEvent;

		private LEAP_EVENT event;
		private eLeapEventType typeE;

		public LEAP_CONNECTION_MESSAGE()
		{}


		public LEAP_CONNECTION_MESSAGE(Pointer pointer)
		{
			super(pointer);
			read();
		}


		/**
		 * @return The event type as an {@link eLeapEventType} instead of a
		 *         <code>short</code>.
		 */
		public eLeapEventType getType()
		{
			if (typeE == null)
			{
				typeE = eLeapEventType.parse(type, eLeapEventType.None);
			}

			return typeE;
		}


		/**
		 * @return The event data as a connection event.
		 * @throws IllegalStateException If this event message is not an
		 *           {@link eLeapEventType#Connection} event.
		 */
		public LEAP_CONNECTION_EVENT getConnectionEvent()
		{
			checkType(eLeapEventType.Connection);
			return getOrCreateEvent(LEAP_CONNECTION_EVENT::new);
		}


		/**
		 * @return The event data as a connection lost event.
		 * @throws IllegalStateException If this event message is not an
		 *           {@link eLeapEventType#ConnectionLost} event.
		 */
		public LEAP_CONNECTION_LOST_EVENT getConnectionLostEvent()
		{
			checkType(eLeapEventType.ConnectionLost);
			return getOrCreateEvent(LEAP_CONNECTION_LOST_EVENT::new);
		}


		/**
		 * @return The event data as a device event.
		 * @throws IllegalStateException If this event message is not an
		 *           {@link eLeapEventType#Device} event.
		 */
		public LEAP_DEVICE_EVENT getDeviceEvent()
		{
			checkType(eLeapEventType.Device);
			return getOrCreateEvent(LEAP_DEVICE_EVENT::new);
		}


		/**
		 * @return The event data as a device status change event.
		 * @throws IllegalStateException If this event message is not an
		 *           {@link eLeapEventType#DeviceStatusChange} event.
		 */
		public LEAP_DEVICE_STATUS_CHANGE_EVENT getDeviceStatusChangeEvent()
		{
			checkType(eLeapEventType.DeviceStatusChange);
			return getOrCreateEvent(LEAP_DEVICE_STATUS_CHANGE_EVENT::new);
		}


		/**
		 * @return The event data as a policy event.
		 * @throws IllegalStateException If this event message is not an
		 *           {@link eLeapEventType#Policy} event.
		 */
		public LEAP_POLICY_EVENT getPolicyEvent()
		{
			checkType(eLeapEventType.Policy);
			return getOrCreateEvent(LEAP_POLICY_EVENT::new);
		}


		/**
		 * @return The event data as a device failure event.
		 * @throws IllegalStateException If this event message is not an
		 *           {@link eLeapEventType#DeviceFailure} event.
		 */
		public LEAP_DEVICE_FAILURE_EVENT getDeviceFailureEvent()
		{
			checkType(eLeapEventType.DeviceFailure);
			return getOrCreateEvent(LEAP_DEVICE_FAILURE_EVENT::new);
		}


		/**
		 * @return The event data as a tracking event.
		 * @throws IllegalStateException If this event message is not an
		 *           {@link eLeapEventType#Tracking} event.
		 */
		public LEAP_TRACKING_EVENT getTrackingEvent()
		{
			checkType(eLeapEventType.Tracking);
			return getOrCreateEvent(LEAP_TRACKING_EVENT::new);
		}


		/**
		 * @return The event data as a log event.
		 * @throws IllegalStateException If this event message is not an
		 *           {@link eLeapEventType#LogEvent} event.
		 */
		public LEAP_LOG_EVENT getLogEvent()
		{
			checkType(eLeapEventType.LogEvent);
			return getOrCreateEvent(LEAP_LOG_EVENT::new);
		}


		/**
		 * @return The event data as multiple log events.
		 * @throws IllegalStateException If this event message is not an
		 *           {@link eLeapEventType#LogEvents} event.
		 */
		public LEAP_LOG_EVENTS getLogEvents()
		{
			checkType(eLeapEventType.LogEvents);
			return getOrCreateEvent(LEAP_LOG_EVENTS::new);
		}


		/**
		 * @return The event data as a config response event.
		 * @throws IllegalStateException If this event message is not an
		 *           {@link eLeapEventType#ConfigResponse} event.
		 */
		public LEAP_CONFIG_RESPONSE_EVENT getConfigResponseEvent()
		{
			checkType(eLeapEventType.ConfigResponse);
			return getOrCreateEvent(LEAP_CONFIG_RESPONSE_EVENT::new);
		}


		/**
		 * @return The event data as a config change event.
		 * @throws IllegalStateException If this event message is not an
		 *           {@link eLeapEventType#ConfigChange} event.
		 */
		public LEAP_CONFIG_CHANGE_EVENT getConfigChangeEvent()
		{
			checkType(eLeapEventType.ConfigChange);
			return getOrCreateEvent(LEAP_CONFIG_CHANGE_EVENT::new);
		}


		/**
		 * @return The event data as a dropped frame event.
		 * @throws IllegalStateException If this event message is not an
		 *           {@link eLeapEventType#DroppedFrame} event;
		 */
		public LEAP_DROPPED_FRAME_EVENT getDroppedFrameEvent()
		{
			checkType(eLeapEventType.DroppedFrame);
			return getOrCreateEvent(LEAP_DROPPED_FRAME_EVENT::new);
		}


		/**
		 * @return The event data as a head pose event.
		 * @throws IllegalStateException If this event message is not an
		 *           {@link eLeapEventType#HeadPose} event;
		 */
		public LEAP_HEAD_POSE_EVENT getHeadPoseEvent()
		{
			checkType(eLeapEventType.HeadPose);
			return getOrCreateEvent(LEAP_HEAD_POSE_EVENT::new);
		}


		/**
		 * @return The event data as a point mapping change event.
		 * @throws IllegalStateException If this event message is not an
		 *           {@link eLeapEventType#PointMappingChange} event;
		 */
		public LEAP_POINT_MAPPING_CHANGE_EVENT getPointMappingChangeEvent()
		{
			checkType(eLeapEventType.PointMappingChange);
			return getOrCreateEvent(LEAP_POINT_MAPPING_CHANGE_EVENT::new);
		}


		public LEAP_IMAGE_EVENT getImageEvent()
		{
			checkType(eLeapEventType.Image);
			return getOrCreateEvent(LEAP_IMAGE_EVENT::new);
		}


		private void checkType(eLeapEventType eventType)
		{
			if (type != eventType.value)
			{
				throw new IllegalStateException(
						"Incorrect event type: " + typeE + " != " + eventType);
			}
		}


		@SuppressWarnings("unchecked")
		private <T extends LEAP_EVENT> T getOrCreateEvent(Function<Pointer, T> createFunction)
		{
			if (event == null)
				event = createFunction.apply(pEvent);

			return (T) event;
		}
	}
}