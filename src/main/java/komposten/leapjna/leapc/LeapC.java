/*
 * Copyright 2020 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc;

import java.util.HashMap;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

import komposten.leapjna.leapc.data.LEAP_ALLOCATOR;
import komposten.leapjna.leapc.data.LEAP_CLOCK_REBASER;
import komposten.leapjna.leapc.data.LEAP_CONNECTION;
import komposten.leapjna.leapc.data.LEAP_CONNECTION_CONFIG;
import komposten.leapjna.leapc.data.LEAP_CONNECTION_INFO;
import komposten.leapjna.leapc.data.LEAP_CONNECTION_MESSAGE;
import komposten.leapjna.leapc.data.LEAP_DEVICE;
import komposten.leapjna.leapc.data.LEAP_DEVICE_INFO;
import komposten.leapjna.leapc.data.LEAP_DEVICE_REF;
import komposten.leapjna.leapc.data.LEAP_POINT_MAPPING;
import komposten.leapjna.leapc.data.LEAP_RECORDING;
import komposten.leapjna.leapc.data.LEAP_RECORDING_PARAMETERS;
import komposten.leapjna.leapc.data.LEAP_RECORDING_STATUS;
import komposten.leapjna.leapc.data.LEAP_TELEMETRY_DATA;
import komposten.leapjna.leapc.data.LEAP_VARIANT;
import komposten.leapjna.leapc.data.LEAP_VECTOR;
import komposten.leapjna.leapc.enums.eLeapEventType;
import komposten.leapjna.leapc.enums.eLeapPerspectiveType;
import komposten.leapjna.leapc.enums.eLeapPolicyFlag;
import komposten.leapjna.leapc.enums.eLeapRS;
import komposten.leapjna.leapc.enums.eLeapRecordingFlags;
import komposten.leapjna.leapc.events.LEAP_CONFIG_CHANGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_CONFIG_RESPONSE_EVENT;
import komposten.leapjna.leapc.events.LEAP_HEAD_POSE_EVENT;
import komposten.leapjna.leapc.events.LEAP_IMAGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_POLICY_EVENT;
import komposten.leapjna.leapc.events.LEAP_TRACKING_EVENT;
import komposten.leapjna.leapc.util.ArrayPointer;
import komposten.leapjna.leapc.util.LeapTypeMapper;
import komposten.leapjna.util.Configurations;


public interface LeapC extends Library
{
	public LeapC INSTANCE = (LeapC) Native
			.synchronizedLibrary(Native.load(LeapCConfig.getDllName(), LeapC.class, new HashMap<String, Object>()
			{
				{
					put(Library.OPTION_TYPE_MAPPER, new LeapTypeMapper());
				}
			}));


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
	 * Destroys a previously opened connection.
	 * </p>
	 * <p>
	 * This method closes the specified connection object if it is opened, destroys the
	 * underlying object, and releases all resources associated with it.
	 * </p>
	 * <p>
	 * This method never fails.
	 * </p>
	 * <p>
	 * Be sure that no other functions are accessing the connection when this function is
	 * called.
	 * </p>
	 * 
	 * @param hConnection A handle to the connection object to be destroyed, created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#ga06fc1079537dde473bf37cc2fb0220ee">LeapC
	 *      API - LeapDestroyConnection</a>
	 */
	public void LeapDestroyConnection(Pointer hConnection);


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
	 * Closes a previously opened connection.
	 * </p>
	 * <p>
	 * This method closes the specified connection object if it is opened
	 * </p>
	 * <p>
	 * This method never fails.
	 * </p>
	 * 
	 * @param hConnection A handle to the connection object, created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#ga1d71e3d5a42c884bcfa981c7ccf11ddb">LeapC
	 *      API - LeapCloseConnection</a>
	 */
	public void LeapCloseConnection(Pointer hConnection);


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
	public eLeapRS LeapGetConnectionInfo(Pointer hConnection, LEAP_CONNECTION_INFO pInfo);


	/**
	 * <p>
	 * Retrieves a list of Leap Motion devices currently attached to the system.
	 * </p>
	 * <p>
	 * To get the number of connected devices, call this function with the
	 * <code>pArray</code> parameter set to <code>null</code>. The number of devices is
	 * written to the memory specified by <code>pnArray</code>. Use the device count to
	 * create an array of {@link LEAP_DEVICE_REF} structs large enough to hold the number of
	 * connected devices. Finally, call <code>LeapGetDeviceList()</code> with this array and
	 * known count to get the list of Leap devices. A device must be opened with
	 * {@link #LeapOpenDevice(LEAP_DEVICE_REF, Pointer)} before device properties can be
	 * queried.
	 * </p>
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param pArray A pointer to an array that LeapC fills with the device list. Use
	 *          {@link ArrayPointer#empty(Class, int)} with the appropriate array size to
	 *          create the pointer.
	 * @param pnArray On input, set to the number of elements in <code>pArray</code>; on
	 *          output LeapC sets this to the number of valid device handles.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#ga93640c45298cc4d756e4b51c890e0a68">LeapC
	 *      API - LeapGetDeviceList</a>
	 */
	public eLeapRS LeapGetDeviceList(Pointer hConnection,
			ArrayPointer<LEAP_DEVICE_REF> pArray, IntByReference pnArray);


	/**
	 * <p>
	 * Opens a device reference and retrieves a handle to the device.
	 * </p>
	 * <p>
	 * To ensure resources are properly freed, users must call
	 * {@link #LeapCloseDevice(Pointer)} when finished with the device, even if the retrieved
	 * device has problems or cannot stream.
	 * </p>
	 * 
	 * @param rDevice A device reference.
	 * @param phDevice A pointer that receives the opened device handle.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#ga992f1420318c3569a6a0f3ceaac43754">LeapC
	 *      API - LeapOpenDevice</a>
	 */
	public eLeapRS LeapOpenDevice(LEAP_DEVICE_REF rDevice, LEAP_DEVICE phDevice);


	/**
	 * Closes a device handle previously opened with
	 * {@link #LeapOpenDevice(LEAP_DEVICE_REF, LEAP_DEVICE)}.
	 * 
	 * @param hDevice The device handle to close. Use {@link LEAP_DEVICE#handle} to obtain
	 *          the handle from the device object.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#gab272a3771f067d7a9d34461d557a7907">LeapC
	 *      API - LeapCloseDevice</a>
	 */
	public void LeapCloseDevice(Pointer hDevice);


	/**
	 * <p>
	 * Gets device properties.
	 * </p>
	 * <p>
	 * To get the device serial number you must supply a {@link LEAP_DEVICE_INFO} struct
	 * whose serial member points to a char array large enough to hold the null-terminated
	 * serial number string. To get the required length, call
	 * {@link #LeapGetDeviceInfo(Pointer, LEAP_DEVICE_INFO)} using a
	 * <code>LEAP_DEVICE_INFO</code> struct that has serial set to <code>null</code>. This
	 * will return {@link eLeapRS#InsufficientBuffer}, but LeapC still sets the
	 * <code>serial_length</code> field of the struct to the required length. You can then
	 * allocate memory for the string (using e.g.
	 * {@link LEAP_DEVICE_INFO#allocateSerialBuffer(int)}), set the serial field, and call
	 * this function again.
	 * </p>
	 * 
	 * @param hDevice A handle to the device to be queried. Use {@link LEAP_DEVICE#handle}
	 *          to obtain the handle from the device object.
	 * @param info The struct to receive the device property data.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#gafae71b1c2b532c22cbfcf8a49df7ed3a">LeapC
	 *      API - LeapGetDeviceInfo</a>
	 */
	public eLeapRS LeapGetDeviceInfo(Pointer hDevice, LEAP_DEVICE_INFO info);


	/**
	 * <p>
	 * Provides the human-readable canonical name of the specified device model.
	 * </p>
	 * <p>
	 * This method is guaranteed to never return <code>null</code> for the
	 * {@link LEAP_DEVICE_INFO#pid} field returned by a successful call to
	 * {@link LeapC#LeapGetDeviceInfo(Pointer, LEAP_DEVICE_INFO)}.
	 * </p>
	 * 
	 * @param pid The pid of the device.
	 * 
	 * @return The string name of the device model, or <code>null</code> if the device type
	 *         string is invalid.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#gada2b0efcd4531790617465723ed3059a">LeapC
	 *      API - LeapDevicePIDToString</a>
	 */
	public String LeapDevicePIDToString(int pid);


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
	 * Constructs a frame at the specified timestamp by interpolating between a frame near
	 * the timestamp and a frame near the sourceTimestamp.
	 * </p>
	 * <p>
	 * Caller is responsible for allocating a buffer large enough to hold the data of the
	 * frame. Use {@link #LeapGetFrameSize(Pointer, long, LongByReference)} to calculate the
	 * minimum size of this buffer.
	 * </p>
	 * <p>
	 * Use {@link #LeapCreateClockRebaser(LEAP_CLOCK_REBASER)},
	 * {@link #LeapUpdateRebase(Pointer, long, long)}, and
	 * {@link #LeapRebaseClock(Pointer, long, LongByReference)} to synchronize time
	 * measurements in the application with time measurements in the Leap Motion service.
	 * This process is required to achieve accurate, smooth interpolation.
	 * </p>
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param timestamp The timestamp to which to interpolate the frame data.
	 * @param sourceTimestamp The timestamp of the beginning frame from which to interpolate
	 *          the data.
	 * @param pEvent A <code>LEAP_TRACKING_EVENT</code> with enough allocated memory to fit
	 *          the frame data. Use <code>LeapGetFrameSize</code> to get the required size,
	 *          and then {@link LEAP_TRACKING_EVENT#LEAP_TRACKING_EVENT(int)} to create the
	 *          struct and allocate memory.
	 * @param ncbEvent The size of the <code>pEvent</code> struct in bytes.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#gabb48588b94c0d66bd9291f3052170a89">LeapC
	 *      API - LeapInterpolateFrameFromTime</a>
	 */
	public eLeapRS LeapInterpolateFrameFromTime(Pointer hConnection, long timestamp,
			long sourceTimestamp, LEAP_TRACKING_EVENT pEvent, long ncbEvent);


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
	 * Use {@link #LeapCreateClockRebaser(LEAP_CLOCK_REBASER)},
	 * {@link #LeapUpdateRebase(Pointer, long, long)}, and
	 * {@link #LeapRebaseClock(Pointer, long, LongByReference)} to synchronize time
	 * measurements in the application with time measurements in the Leap Motion service.
	 * This process is required to achieve accurate, smooth interpolation.
	 * </p>
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
	 * Use {@link #LeapCreateClockRebaser(LEAP_CLOCK_REBASER)},
	 * {@link #LeapUpdateRebase(Pointer, long, long)}, and
	 * {@link #LeapRebaseClock(Pointer, long, LongByReference)} to synchronize time
	 * measurements in the application with time measurements in the Leap Motion service.
	 * This process is required to achieve accurate, smooth interpolation.
	 * </p>
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
	 * Pauses the service.
	 * </p>
	 * <p>
	 * Attempts to pause or unpause the service depending on the argument. This is treated
	 * as a 'user pause', as though a user had requested a pause through the Leap Control
	 * Panel. The connection must have the {@link eLeapPolicyFlag#AllowPauseResume} policy
	 * set or it will fail with {@link eLeapRS#InvalidArgument}.
	 * </p>
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param pause Set to '<code>1</code>' to pause, or '<code>0</code>' to unpause
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#gab1da8139358849a062e1665e6edef2fb">LeapC
	 *      API - LeapSetPause</a>
	 */
	public eLeapRS LeapSetPause(Pointer hConnection, int pause);


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
	 * The change is performed asynchronously � and may fail.
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
	 * Initializes a new Leap clock-rebaser handle object.
	 * </p>
	 * <p>
	 * Pass the filled-in {@link LEAP_CLOCK_REBASER} object to calls to
	 * {@link #LeapUpdateRebase(LEAP_CLOCK_REBASER, long, long)},
	 * {@link #LeapRebaseClock(LEAP_CLOCK_REBASER, long, LongByReference)}, and
	 * {@link #LeapDestroyClockRebaser(Pointer)}.
	 * </p>
	 * 
	 * @param phClockRebaser A pointer to the clock-rebaser object to be initialized.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#ga95c0d7e31b8337021c41f13d4ef6849b">LeapC
	 *      API - LeapCreateClockRebaser</a>
	 */
	public eLeapRS LeapCreateClockRebaser(LEAP_CLOCK_REBASER phClockRebaser);


	/**
	 * <p>
	 * Destroys a previously created clock-rebaser object.
	 * </p>
	 * <p>
	 * This method destroys the specified clock-rebaser object, and releases all resources
	 * associated with it.
	 * </p>
	 * 
	 * @param hClockRebaser The handle to the clock-rebaser object to be destroyed. Use
	 *          {@link LEAP_CLOCK_REBASER#handle} to obtain the handle from the clock
	 *          rebaser object.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#ga4335f1588e9c1a3277f4c0815521956c">LeapC
	 *      API - LeapDestroyClockRebaser</a>
	 */
	public void LeapDestroyClockRebaser(Pointer hClockRebaser);


	/**
	 * <p>
	 * Computes the Leap Motion clock corresponding to a specified application clock value.
	 * </p>
	 * <p>
	 * Use this function to translate your application clock to the Leap Motion clock when
	 * interpolating frames. {@link #LeapUpdateRebase(LEAP_CLOCK_REBASER, long, long)} must
	 * be called for every rendered frame for the relationship between the two clocks to
	 * remain synchronised.
	 * </p>
	 * 
	 * @param hClockRebaser The handle to a rebaser object created by
	 *          {@link #LeapCreateClockRebaser(LEAP_CLOCK_REBASER)}. Use
	 *          {@link LEAP_CLOCK_REBASER#handle} to obtain the handle from the clock
	 *          rebaser object.
	 * @param userClock The clock in microseconds referenced to the application clock.
	 * @param pLeapClock The corresponding Leap Motion clock value.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#gadd9e1af6480d7948b77ccf40fbca337a">LeapC
	 *      API - LeapRebaseClock</a>
	 */
	public eLeapRS LeapRebaseClock(Pointer hClockRebaser, long userClock,
			LongByReference pLeapClock);


	/**
	 * <p>
	 * Updates the relationship between the Leap Motion clock and the user clock.
	 * </p>
	 * <p>
	 * When using {@link #LeapInterpolateFrame(Pointer, long, LEAP_TRACKING_EVENT, long)},
	 * call this function for every graphics frame rendered by your application. The
	 * function should be called as close to the actual point of rendering as possible.
	 * </p>
	 * <p>
	 * The relationship between the application clock and the Leap Motion clock is neither
	 * fixed nor stable. Simulation restarts can cause user clock values to change
	 * instantaneously. Certain systems simulate slow motion, or respond to heavy load, by
	 * reducing the tick rate of the user clock. As a result, the
	 * <code>LeapUpdateRebase()</code> function must be called for every rendered frame.
	 * </p>
	 * 
	 * @param hClockRebaser The handle to a rebaser object created by
	 *          {@link #LeapCreateClockRebaser(LEAP_CLOCK_REBASER)}. Use
	 *          {@link LEAP_CLOCK_REBASER#handle} to obtain the handle from the clock
	 *          rebaser object.
	 * @param userClock A clock value supplied by the application, in microseconds, sampled
	 *          at about the same time as {@link #LeapGetNow()} was sampled.
	 * @param leapClock The Leap Motion clock value sampled by a call to
	 *          {@link #LeapGetNow()}.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#gac111f105a4b418e1f7ed08a1b74c8bca">LeapC
	 *      API - LeapUpdateRebase</a>
	 */
	public eLeapRS LeapUpdateRebase(Pointer hClockRebaser, long userClock, long leapClock);


	/**
	 * <p>
	 * Provides the corrected camera ray intercepting the specified point on the image.
	 * </p>
	 * <p>
	 * Given a point on the image, <code>LeapPixelToRectilinear()</code> corrects for camera
	 * distortion and returns the true direction from the camera to the source of that image
	 * point within the Leap Motion field of view.
	 * </p>
	 * <p>
	 * This direction vector has an x and y component [x, y, 1], with the third element
	 * always 1. Note that this vector uses the 2D camera coordinate system where the x-axis
	 * parallels the longer (typically horizontal) dimension and the y-axis parallels the
	 * shorter (vertical) dimension. The camera coordinate system does not correlate to the
	 * 3D Leap Motion coordinate system.
	 * </p>
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param camera The camera to use, a member of the {@link eLeapPerspectiveType}
	 *          enumeration.
	 * @param pixel A vector containing the position of a pixel in the image.
	 * @return A vector containing the ray direction (the z-component of the vector is
	 *         always 1).
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#gacc6a5c80f87a60c63889407a45a3344b">LeapC
	 *      API - LeapPixelToRectilinear</a>
	 */
	public LEAP_VECTOR.ByValue LeapPixelToRectilinear(Pointer hConnection, int camera,
			LEAP_VECTOR.ByValue pixel);


	/**
	 * <p>
	 * Provides the point in the image corresponding to a ray projecting from the camera.
	 * </p>
	 * <p>
	 * Given a ray projected from the camera in the specified direction,
	 * <code>LeapRectilinearToPixel()</code> corrects for camera distortion and returns the
	 * corresponding pixel coordinates in the image.
	 * </p>
	 * <p>
	 * The ray direction is specified in relationship to the camera. The first vector
	 * element is the tangent of the "horizontal" view angle; the second element is the
	 * tangent of the "vertical" view angle.
	 * </p>
	 * <p>
	 * The <code>LeapRectilinearToPixel()</code> function returns pixel coordinates outside
	 * of the image bounds if you project a ray toward a point for which there is no
	 * recorded data.
	 * </p>
	 * <p>
	 * <code>LeapRectilinearToPixel()</code> is typically not fast enough for realtime
	 * distortion correction. For better performance, use a shader program executed on a
	 * GPU.
	 * </p>
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param camera The camera to use, a member of the {@link eLeapPerspectiveType}
	 *          enumeration.
	 * @param rectilinear A vector containing the ray direction.
	 * @return A vector containing the pixel coordinates [x, y, 1] (with z always 1).
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#ga0f8c7bb9c7d46fed78efe183244f9812">LeapC
	 *      API - LeapRectilinearToPixel</a>
	 */
	public LEAP_VECTOR.ByValue LeapRectilinearToPixel(Pointer hConnection, int camera,
			LEAP_VECTOR.ByValue rectilinear);


	/**
	 * <b>Note</b>: Telemetry profiling has documentation in neither the API reference nor
	 * <code>leapc.h</code> in the SDK. It seems like <code>telemetryData</code> needs to be
	 * pre-populated with information before passed into this method, but what that
	 * information should be I don't know.
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param telemetryData A {@link LEAP_TELEMETRY_DATA} instance.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 */
	public eLeapRS LeapTelemetryProfiling(Pointer hConnection,
			LEAP_TELEMETRY_DATA telemetryData);


	public long LeapTelemetryGetNow();


	/**
	 * <p>
	 * Opens or creates a {@link LEAP_RECORDING}.
	 * </p>
	 * <p>
	 * Pass the {@link LEAP_RECORDING} pointer to <code>LeapRecordingOpen()</code> to
	 * initiate reading from or writing to a recording. The recording path is relative to
	 * the "user path" which is the SD card on Android.
	 * </p>
	 * 
	 * @param ppRecording The recording being opened.
	 * @param filePath The file path. This will be passed directly to the OS without
	 *          modification. An ".lmt" suffix is suggested.
	 * @param params The {@link LEAP_RECORDING_PARAMETERS} describing what operations are
	 *          requested.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#ga483c11c1bc9a91467f14d188e7055cb9">LeapC
	 *      API - LeapRecordingOpen</a>
	 */
	public eLeapRS LeapRecordingOpen(LEAP_RECORDING ppRecording, String filePath,
			LEAP_RECORDING_PARAMETERS params);


	/**
	 * Writes a tracking frame to a {@link LEAP_RECORDING} file.
	 * 
	 * @param pRecording The recording being written to. Use {@link LEAP_RECORDING#handle}
	 *          to obtain the handle from the recording object.
	 * @param pEvent A <code>LEAP_TRACKING_EVENT</code> with the frame data to write.
	 * @param pnBytesWritten If non-null the number of bytes written.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#gadeddce2276e9e01bae39ac30df910083">LeapC
	 *      API - LeapRecordingWrite</a>
	 */
	public eLeapRS LeapRecordingWrite(Pointer pRecording, LEAP_TRACKING_EVENT pEvent,
			LongByReference pnBytesWritten);


	/**
	 * <p>
	 * Fills in a {@link LEAP_RECORDING_STATUS} struct for an open recording.
	 * </p>
	 * <p>
	 * This struct provides the applicable {@link eLeapRecordingFlags}.
	 * </p>
	 * 
	 * @param pRecording The open recording. Use {@link LEAP_RECORDING#handle} to obtain the
	 *          handle from the recording object.
	 * @param pStatus A {@link LEAP_RECORDING_STATUS} struct to receive the recording
	 *          status.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#ga2065c14a97c2dc184904f14fe2dee39e">LeapC
	 *      API - LeapRecordingGetStatus</a>
	 */
	public eLeapRS LeapRecordingGetStatus(Pointer pRecording,
			LEAP_RECORDING_STATUS pStatus);


	/**
	 * <p>
	 * Reads a tracking frame from a {@link LEAP_RECORDING} file.
	 * </p>
	 * <p>
	 * Caller is responsible for allocating a buffer large enough to hold the data of the
	 * frame. Use {@link #LeapRecordingReadSize(Pointer, LongByReference)} to calculate the
	 * minimum size of this buffer.
	 * </p>
	 * 
	 * @param pRecording The recording being read from. Use {@link LEAP_RECORDING#handle} to
	 *          obtain the handle from the recording object.
	 * @param pEvent A <code>LEAP_TRACKING_EVENT</code> with enough allocated memory to fit
	 *          the frame data. Use <code>LeapRecordingReadSize</code> to get the required
	 *          size, and then {@link LEAP_TRACKING_EVENT#LEAP_TRACKING_EVENT(int)} to
	 *          create the struct and allocate memory.
	 * @param ncbEvent The size of the <code>pEvent</code> struct in bytes.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#gae199628d40e996fc6586559b74e8aeb4">LeapC
	 *      API - LeapRecordingRead</a>
	 */
	public eLeapRS LeapRecordingRead(Pointer pRecording, LEAP_TRACKING_EVENT pEvent,
			long ncbEvent);


	/**
	 * <p>
	 * Retrieves the number of bytes required to allocate the next frame in a recording.
	 * </p>
	 * <p>
	 * Use this function to determine the size of the buffer to allocate before calling
	 * {@link #LeapRecordingRead(Pointer, LEAP_TRACKING_EVENT, long)}.
	 * </p>
	 * 
	 * @param hConnection The recording being read from. Use {@link LEAP_RECORDING#handle}
	 *          to obtain the handle from the recording object.
	 * @param pncbEvent A pointer that receives the number of bytes required to store the
	 *          next frame.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#ga73bde2a17dd2d8714546a2a41e18fe01">LeapC
	 *      API - LeapRecordingReadSize</a>
	 */
	public eLeapRS LeapRecordingReadSize(Pointer pRecording, LongByReference pncbEvent);


	/**
	 * Closes a {@link LEAP_RECORDING}.
	 * 
	 * @param ppRecording The recording being closed. Will modify *ppRecording to be null.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#gaba62f3b921e087f034dc7b44cd3f2939">LeapC
	 *      API - LeapRecordingClose</a>
	 */
	public eLeapRS LeapRecordingClose(LEAP_RECORDING ppRecording);
}