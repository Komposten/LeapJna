/*
 * Copyright 2020-2022 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.leapc;

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
import komposten.leapjna.leapc.data.LEAP_IMAGE;
import komposten.leapjna.leapc.data.LEAP_POINT_MAPPING;
import komposten.leapjna.leapc.data.LEAP_RECORDING;
import komposten.leapjna.leapc.data.LEAP_RECORDING_PARAMETERS;
import komposten.leapjna.leapc.data.LEAP_RECORDING_STATUS;
import komposten.leapjna.leapc.data.LEAP_TELEMETRY_DATA;
import komposten.leapjna.leapc.data.LEAP_VARIANT;
import komposten.leapjna.leapc.data.LEAP_VECTOR;
import komposten.leapjna.leapc.data.LEAP_VERSION;
import komposten.leapjna.leapc.enums.eLeapConnectionConfig;
import komposten.leapjna.leapc.enums.eLeapEventType;
import komposten.leapjna.leapc.enums.eLeapPerspectiveType;
import komposten.leapjna.leapc.enums.eLeapPolicyFlag;
import komposten.leapjna.leapc.enums.eLeapRS;
import komposten.leapjna.leapc.enums.eLeapRecordingFlags;
import komposten.leapjna.leapc.enums.eLeapTrackingMode;
import komposten.leapjna.leapc.enums.eLeapVersionPart;
import komposten.leapjna.leapc.events.LEAP_CONFIG_CHANGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_CONFIG_RESPONSE_EVENT;
import komposten.leapjna.leapc.events.LEAP_HEAD_POSE_EVENT;
import komposten.leapjna.leapc.events.LEAP_IMAGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_POLICY_EVENT;
import komposten.leapjna.leapc.events.LEAP_TRACKING_EVENT;
import komposten.leapjna.leapc.events.LEAP_TRACKING_MODE_EVENT;
import komposten.leapjna.leapc.util.ArrayPointer;
import komposten.leapjna.leapc.util.PrimitiveArrayPointer;
import komposten.leapjna.util.Configurations;


/**
 * <p>
 * The main interface for interacting with the UltraLeap C API.
 * </p>
 * <p>
 * Use {@link #INSTANCE LeapC.INSTANCE} to obtain an instance of this interface
 * that is linked to the native API using JNA. After that all API methods can be
 * called on that instance in a similar way to how the C API is used.
 * </p>
 */
@SuppressWarnings("deprecation")
public interface LeapC extends Library
{
	final LeapC INSTANCE = (LeapC) Native.synchronizedLibrary(Native
			.load(LeapCConfig.getDllName(), LeapC.class, LeapCConfig.getLibraryOptions()));


	/**
	 * <p>
	 * Creates a new connection to the Ultraleap Tracking Service and stores a handle for the
	 * connection in the provided {@link LEAP_CONNECTION}.
	 * </p>
	 * <p>
	 * Pass the LEAP_CONNECTION pointer to {@link #LeapOpenConnection(Pointer)} to establish
	 * a connection to the Ultraleap Tracking Service; and to subsequent operations on the same
	 * connection.
	 * </p>
	 * 
	 * @param pConfig The configuration to be used with the newly created connection. If
	 *          pConfig is null, a connection is created with a default configuration.
	 * @param phConnection Receives a pointer to the connection object, set to invalid on
	 *          failure.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv420LeapCreateConnectionPK22LEAP_CONNECTION_CONFIGP15LEAP_CONNECTION">LeapC
	 *      API - LeapCreateConnection</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.0.0
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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv421LeapDestroyConnection15LEAP_CONNECTION">LeapC
	 *      API - LeapDestroyConnection</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.0.0
	 */
	public void LeapDestroyConnection(Pointer hConnection);


	/**
	 * <p>
	 * Opens a connection to the Ultraleap Tracking Service.
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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv418LeapOpenConnection15LEAP_CONNECTION">LeapC
	 *      API - LeapOpenConnection</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.0.0
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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv419LeapCloseConnection15LEAP_CONNECTION">LeapC
	 *      API - LeapCloseConnection</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 4.0.0
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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv418LeapPollConnection15LEAP_CONNECTION8uint32_tP23LEAP_CONNECTION_MESSAGE">LeapC
	 *      API - LeapPollConnection</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.0.0
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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv421LeapGetConnectionInfo15LEAP_CONNECTIONP20LEAP_CONNECTION_INFO">LeapC
	 *      API - LeapGetConnectionInfo</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.0.0
	 */
	public eLeapRS LeapGetConnectionInfo(Pointer hConnection, LEAP_CONNECTION_INFO pInfo);


	/**
	 * <p>
	 * Retrieves a list of Ultraleap Tracking camera devices currently attached to the system.
	 * </p>
	 * <p>
	 * To get the number of connected devices, call this function with the
	 * <code>pArray</code> parameter set to <code>null</code>. The number of devices is
	 * written to the memory specified by <code>pnArray</code>. Use the device count to
	 * create an array of {@link LEAP_DEVICE_REF} structs large enough to hold the number of
	 * connected devices. Finally, call <code>LeapGetDeviceList()</code> with this array and
	 * known count to get the list of Leap devices. A device must be opened with
	 * {@link #LeapOpenDevice(LEAP_DEVICE_REF, LEAP_DEVICE)} before device properties can be
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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv417LeapGetDeviceList15LEAP_CONNECTIONP15LEAP_DEVICE_REFP8uint32_t">LeapC
	 *      API - LeapGetDeviceList</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.0.0
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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv414LeapOpenDevice15LEAP_DEVICE_REFP11LEAP_DEVICE">LeapC
	 *      API - LeapOpenDevice</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.0.0
	 */
	public eLeapRS LeapOpenDevice(LEAP_DEVICE_REF rDevice, LEAP_DEVICE phDevice);


	/**
	 * Closes a device handle previously opened with
	 * {@link #LeapOpenDevice(LEAP_DEVICE_REF, LEAP_DEVICE)}.
	 * 
	 * @param hDevice The device handle to close. Use {@link LEAP_DEVICE#handle} to obtain
	 *          the handle from the device object.
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv415LeapCloseDevice11LEAP_DEVICE">LeapC
	 *      API - LeapCloseDevice</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.0.0
	 */
	public void LeapCloseDevice(Pointer hDevice);
	

	/**
	 * <p>
	 * For a multi-device aware client, sets the device to use in the context of non-"Ex"
	 * API functions which are logically device-specific but don't provide a device
	 * parameter.
	 * </p>
	 * <p>
	 * Automatically subscribes to the specified device (see
	 * {@link #LeapSubscribeEvents(Pointer, Pointer)}), and if
	 * <code>unsubscribeOthers</code> is '<code>1</code>', then unsubscribes from all other
	 * devices as well (see {@link #LeapUnsubscribeEvents(Pointer, Pointer)}).
	 * </p>
	 * <p>
	 * Affects future invocations of the following functions:
	 * <ul>
	 * <li>LeapCameraMatrix()
	 * <li>LeapDistortionCoeffs()
	 * <li>LeapGetFrameSize()
	 * <li>LeapInterpolateFrame()
	 * <li>LeapInterpolateFrameFromTime()
	 * <li>LeapPixelToRectilinear()
	 * <li>LeapRectilinearToPixel()
	 * </ul>
	 * <p>
	 * It is not necessary to call this function from a client that does not claim to be
	 * multi-device-aware (see {@link eLeapConnectionConfig} and
	 * {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)}).
	 * </p>
	 *
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param hDevice A handle to the device to be queried. Use {@link LEAP_DEVICE#handle}
	 *          to obtain the handle from the device object.
	 * @param unsubscribeOthers Set to '<code>1</code>' to unsubscribe from all other
	 *          devices, or <code>0</code> to keep all subscriptions.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @since LeapJna 1.2.0
	 * @since Ultraleap Gemini SDK 5.4.0
	 */
	public eLeapRS LeapSetPrimaryDevice(Pointer hConnection, Pointer hDevice, int unsubscribeOthers);


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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv417LeapGetDeviceInfo11LEAP_DEVICEP16LEAP_DEVICE_INFO">LeapC
	 *      API - LeapGetDeviceInfo</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.0.0
	 */
	public eLeapRS LeapGetDeviceInfo(Pointer hDevice, LEAP_DEVICE_INFO info);
	
	/**
	 * <p>
	 * Get the transform to world coordinates from 3D Leap coordinates.
	 * </p>
	 * <p>
	 * To get the transform, you must supply an array of 16 elements.
	 * </p>
	 * <p>
	 * The function will return a an array representing a 4 x 4 matrix of the form:
	 * <pre>
	 * R, t
	 * 0, 1
	 *
	 * where:
	 * R is a 3 x 3 rotation matrix
	 * t is a 3 x 1 translation vector
	 * </pre>
	 * <p>
	 * Note that the matrix is in column major, e.g. transform[12] corresponds to the x coordinate of the
	 * translation vector t.
	 * </p>
	 * <p>
	 * A possible pipeline would be, for example:
	 * <ol>
	 * <li>Get "palm_pos" the position of the center of the palm (as a 3x1 vector)
	 * <li>Construct a 4x1 vector using the palm_position: palm_pos_4 = (palm_pos.x; palm_pos.y; palm_pos.z; 1.0f)
	 * <li>Create a 4x4 matrix "trans_mat" as illustrated above using the returned transform
	 * <li>Get the position of the center of the palm in world coordinates by multiplying trans_mat and palm_pos_4:
	 *    center_world_4 = trans_mat * palm_pos_4
	 * </ol>
	 * <p>
	 * This function returns eLeapRS_Unsupported in the case where this functionality is not yet supported.
	 * </p>
	 * 
	 * @param hDevice A handle to the device to be queried. Use {@link LEAP_DEVICE#handle}
	 *          to obtain the handle from the device object.
	 * @param transform A pointer to a single-precision float array of size 16, containing
	 *  the coefficients of the 4x4 matrix in Column Major order.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @since LeapJna 1.2.0
	 * @since Ultraleap Gemini SDK 5.4.0
	 */
	public eLeapRS LeapGetDeviceTransform(Pointer hDevice, PrimitiveArrayPointer transform);


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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv421LeapDevicePIDToString14eLeapDevicePID">LeapC
	 *      API - LeapDevicePIDToString</a>
	 * @since LeapJna 1.0.0
	 */
	public String LeapDevicePIDToString(int pid);
	

	/**
	 * <p>
	 * Subscribe to event messages based on device.
	 * </p>
	 * <p>
	 * If events from multiple devices are being sent from a service, this function allows
	 * the client to receive events from the specified device. Clients that claim to be
	 * multi-device-aware (see {@link eLeapConnectionConfig} and
	 * {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)}) must
	 * subscribe to a device to receive various device-specific events.
	 * </p>
	 *
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param hDevice The device handle to close. Use {@link LEAP_DEVICE#handle} to obtain
	 *          the handle from the device object.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @since LeapJna 1.2.0
	 * @since Ultraleap Gemini SDK 5.4.0
	 */
	public eLeapRS LeapSubscribeEvents(Pointer hConnection, Pointer hDevice);
	
	/**
	 * <p>
	 * Unsubscribe from event messages based on device.
	 * </p>
	 * <p>
	 * If events from multiple devices are being sent from a service, this function
	 * prevents receiving further events from the specified device that had
	 * previously been enabled using a call to {@link #LeapSubscribeEvents(Pointer, Pointer)}.
	 * </p>
	 *
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param hDevice The device handle to close. Use {@link LEAP_DEVICE#handle} to obtain
	 *          the handle from the device object.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @since LeapJna 1.2.0
	 * @since Ultraleap Gemini SDK 5.4.0
	 */
	public eLeapRS LeapUnsubscribeEvents(Pointer hConnection, Pointer hDevice);
	
	
	/**
	 *
	 * Returns the version of a specified part of the system.
	 *
	 * If an invalid connection handle is provided only the version details of the client
	 * will be available.
	 *
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param versionPart The version part to return, this will reference one part of the
	 *          system. A member of the {@link eLeapVersionPart} enumeration.
	 * @param pVersion A pointer to a struct used to store the version number.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv414LeapGetVersion15LEAP_CONNECTION16eLeapVersionPartP12LEAP_VERSION">LeapC
	 *      API - LeapGetVersion</a>
	 * @since LeapJna 1.2.0
	 * @since Ultraleap Gemini SDK 5.2.0
	 */
	public eLeapRS LeapGetVersion(Pointer hConnection, int versionPart, LEAP_VERSION pVersion);


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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv414LeapGetVersion15LEAP_CONNECTION16eLeapVersionPartP12LEAP_VERSION">LeapC
	 *      API - LeapGetFrameSize</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.1.1
	 */
	public eLeapRS LeapGetFrameSize(Pointer hConnection, long timestamp, LongByReference pncbEvent);


	/**
	 * <p>
	 * Retrieves the number of bytes required to allocate an interpolated frame at the
	 * specified time for a particular device.
	 * </p>
	 * <p>
	 * Use this function to determine the size of the buffer to allocate when calling
	 * {@link #LeapInterpolateFrameEx(Pointer, Pointer, long, LEAP_TRACKING_EVENT, long)}.
	 * </p>
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param hDevice The device handle to close. Use {@link LEAP_DEVICE#handle} to obtain
	 *          the handle from the device object.
	 * @param timestamp The timestamp of the frame whose size is to be queried.
	 * @param pncbEvent A pointer that receives the number of bytes required to store the
	 *          specified frame.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv418LeapGetFrameSizeEx15LEAP_CONNECTION11LEAP_DEVICE7int64_tP8uint64_t">LeapC
	 *      API - LeapGetFrameSizeEx</a>
	 * @since LeapJna 1.2.0
	 */
	public eLeapRS LeapGetFrameSizeEx(Pointer hConnection, Pointer hDevice, long timestamp,
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
	 * measurements in the application with time measurements in the Ultraleap Tracking Service.
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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv428LeapInterpolateFrameFromTime15LEAP_CONNECTION7int64_t7int64_tP19LEAP_TRACKING_EVENT8uint64_t">LeapC
	 *      API - LeapInterpolateFrameFromTime</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.1.1
	 */
	public eLeapRS LeapInterpolateFrameFromTime(Pointer hConnection, long timestamp,
			long sourceTimestamp, LEAP_TRACKING_EVENT pEvent, long ncbEvent);


	/**
	 * <p>
	 * Constructs a frame at the specified timestamp for a particular device by
	 * interpolating between a frame near the timestamp and a frame near the
	 * sourceTimestamp.
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
	 * measurements in the application with time measurements in the Ultraleap Tracking Service.
	 * This process is required to achieve accurate, smooth interpolation.
	 * </p>
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param hDevice The device handle to close. Use {@link LEAP_DEVICE#handle} to obtain
	 *          the handle from the device object.
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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv430LeapInterpolateFrameFromTimeEx15LEAP_CONNECTION11LEAP_DEVICE7int64_t7int64_tP19LEAP_TRACKING_EVENT8uint64_t">LeapC
	 *      API - LeapInterpolateFrameFromTimeEx</a>
	 * @since LeapJna 1.2.0
	 */
	public eLeapRS LeapInterpolateFrameFromTimeEx(Pointer hConnection, Pointer hDevice,
			long timestamp, long sourceTimestamp, LEAP_TRACKING_EVENT pEvent, long ncbEvent);


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
	 * measurements in the application with time measurements in the Ultraleap Tracking Service.
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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv420LeapInterpolateFrame15LEAP_CONNECTION7int64_tP19LEAP_TRACKING_EVENT8uint64_t">LeapC
	 *      API - LeapInterpolateFrame</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.1.1
	 */
	public eLeapRS LeapInterpolateFrame(Pointer hConnection, long timestamp,
			LEAP_TRACKING_EVENT pEvent, long ncbEvent);


	/**
	 * <p>
	 * Constructs a frame at the specified timestamp for a particular device by
	 * interpolating between measured frames. frames.
	 * </p>
	 * <p>
	 * Caller is responsible for allocating a buffer large enough to hold the data of the
	 * frame. Use {@link #LeapGetFrameSizeEx(Pointer, Pointer, long, LongByReference)} to
	 * calculate the minimum size of this buffer.
	 * </p>
	 * <p>
	 * Use {@link #LeapCreateClockRebaser(LEAP_CLOCK_REBASER)},
	 * {@link #LeapUpdateRebase(Pointer, long, long)}, and
	 * {@link #LeapRebaseClock(Pointer, long, LongByReference)} to synchronize time
	 * measurements in the application with time measurements in the Ultraleap Tracking Service.
	 * This process is required to achieve accurate, smooth interpolation.
	 * </p>
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param hDevice The device handle to close. Use {@link LEAP_DEVICE#handle} to obtain
	 *          the handle from the device object.
	 * @param timestamp The timestamp at which to interpolate the frame data.
	 * @param pEvent A <code>LEAP_TRACKING_EVENT</code> with enough allocated memory to fit
	 *          the frame data. Use <code>LeapGetFrameSize</code> to get the required size,
	 *          and then {@link LEAP_TRACKING_EVENT#LEAP_TRACKING_EVENT(int)} to create the
	 *          struct and allocate memory.
	 * @param ncbEvent The size of the <code>pEvent</code> struct in bytes.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv422LeapInterpolateFrameEx15LEAP_CONNECTION11LEAP_DEVICE7int64_tP19LEAP_TRACKING_EVENT8uint64_t">LeapC
	 *      API - LeapInterpolateFrameEx</a>
	 * @since LeapJna 1.2.0
	 */
	public eLeapRS LeapInterpolateFrameEx(Pointer hConnection, Pointer hDevice, long timestamp,
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
	 * measurements in the application with time measurements in the Ultraleap Tracking Service.
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
	 * @deprecated This function is no longer supported. Calling it will have no effect.
	 * @see <a href=
	 *      "https://developer.leapmotion.com/documentation/v4/group___functions.html#gab756331205d6ee0cd61708d77d968536">LeapC
	 *      API - LeapInterpolateHeadPose</a>
	 * @since LeapJna 1.0.0
	 */
	@Deprecated
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
	 * @see eLeapPolicyFlag#createMask(eLeapPolicyFlag...)
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv418LeapSetPolicyFlags15LEAP_CONNECTION8uint64_t8uint64_t">LeapC
	 *      API - LeapSetPolicyFlags</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.0.0
	 */
	public eLeapRS LeapSetPolicyFlags(Pointer hConnection, long set, long clear);


	/**
	 * <p>
	 * Sets or clears one or more policy flags for a particular device.
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
	 * @param hDevice The device handle to close. Use {@link LEAP_DEVICE#handle} to obtain
	 *          the handle from the device object.
	 * @param set A bitwise combination of flags to be set. Set to 0 if not setting any
	 *          flags.
	 * @param clear A bitwise combination of flags to be cleared. Set to 0 if not clearing
	 *          any flags.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see eLeapPolicyFlag#createMask(eLeapPolicyFlag...)
	 * @since LeapJna 1.2.0
	 * @since Ultraleap Gemini SDK 5.4.0
	 */
	public eLeapRS LeapSetPolicyFlagsEx(Pointer hConnection, Pointer hDevice, long set, long clear);
	
	
	/**
	 * <p>
	 * Requests a tracking mode.
	 * </p>
	 * <p>
	 * Changing tracking modes is asynchronous. After you call this function, a subsequent
	 * call to {@link #LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)} provides a
	 * {@link LEAP_POLICY_EVENT} containing the current policies, reflecting any changes.
	 * </p>
	 * 
	 * <p>
	 * The {@link eLeapTrackingMode} enumeration defines the tracking mode.
	 * </p>
	 *
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param mode The enum value specifying the requested tracking mode.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv419LeapSetTrackingMode15LEAP_CONNECTION17eLeapTrackingMode">LeapC
	 *      API - LeapSetTrackingMode</a>
	 * @since LeapJna 1.1.0
	 * @since Ultraleap Gemini SDK 5.0.0
	 */
	public eLeapRS LeapSetTrackingMode(Pointer hConnection, int mode);
	
	
	/**
	 * <p>
	 * Requests a tracking mode for a particular device.
	 * </p>
	 * <p>
	 * Changing tracking modes is asynchronous. After you call this function, a subsequent
	 * call to {@link #LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)} provides a
	 * {@link LEAP_POLICY_EVENT} containing the current policies, reflecting any changes.
	 * </p>
	 *
	 * <p>
	 * The {@link eLeapTrackingMode} enumeration defines the tracking mode.
	 * </p>
	 *
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param hDevice The device handle to close. Use {@link LEAP_DEVICE#handle} to obtain
	 *          the handle from the device object.
	 * @param mode The enum value specifying the requested tracking mode.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @since LeapJna 1.2.0
	 * @since Ultraleap Gemini SDK 5.4.0
	 */
	public eLeapRS LeapSetTrackingModeEx(Pointer hConnection, Pointer hDevice, int mode);

	
	/**
	 * <p>
	 * Requests the currently set tracking mode.
	 * </p>
	 *
	 * <p>
	 * Requesting the current tracking mode is asynchronous. After you call this function, a
	 * subsequent call to {@link #LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)}
	 * provides a {@link LEAP_TRACKING_MODE_EVENT} containing the current tracking mode,
	 * reflecting any changes.
	 * </p>
	 *
	 * <p>
	 * The {@link eLeapTrackingMode} enumeration defines the tracking mode.
	 * </p>
	 *
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv419LeapGetTrackingMode15LEAP_CONNECTION">LeapC
	 *      API - LeapGetTrackingMode</a>
	 * @since LeapJna 1.2.0
	 */
	public eLeapRS LeapGetTrackingMode(Pointer hConnection);

	
	/**
	 * <p>
	 * Requests the currently set tracking mode.
	 * </p>
	 *
	 * <p>
	 * Requesting the current tracking mode is asynchronous. After you call this function, a
	 * subsequent call to {@link #LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)}
	 * provides a {@link LEAP_TRACKING_MODE_EVENT} containing the current tracking mode,
	 * reflecting any changes.
	 * </p>
	 *
	 * <p>
	 * The {@link eLeapTrackingMode} enumeration defines the tracking mode.
	 * </p>
	 *
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param hDevice The device handle to close. Use {@link LEAP_DEVICE#handle} to obtain
	 *          the handle from the device object.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @since LeapJna 1.2.0
	 * @since Ultraleap Gemini SDK 5.4.0
	 */
	public eLeapRS LeapGetTrackingModeEx(Pointer hConnection, Pointer hDevice);


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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv412LeapSetPause15LEAP_CONNECTIONb">LeapC
	 *      API - LeapSetPause</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 4.0.0
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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv422LeapRequestConfigValue15LEAP_CONNECTIONPKcP8uint32_t">LeapC
	 *      API - LeapRequestConfigValue</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.0.0
	 */
	public eLeapRS LeapRequestConfigValue(Pointer hConnection, String key,
			LongByReference pRequestID);


	/**
	 * <p>
	 * Causes the client to commit a configuration change to the Ultraleap Tracking Service.
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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv419LeapSaveConfigValue15LEAP_CONNECTIONPKcPK12LEAP_VARIANTP8uint32_t">LeapC
	 *      API - LeapSaveConfigValue</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.0.0
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
	 * @deprecated This function is no longer supported. Calling it will have no effect.
	 * @since LeapJna 1.0.0
	 */
	@Deprecated
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
	 * @param pSize A pointer to the size of <code>pointMapping</code>.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @deprecated This function is no longer supported. Calling it will have no effect.
	 * @since LeapJna 1.0.0
	 */
	@Deprecated
	public eLeapRS LeapGetPointMapping(Pointer hConnection, LEAP_POINT_MAPPING pointMapping,
			LongByReference pSize);


	/**
	 * <p>
	 * Samples the universal clock used by the system to timestamp image and
	 * tracking frames. The returned counter value is given in microseconds since
	 * an epoch time.
	 * </p>
	 * <p>
	 * The clock used for the counter itself is implementation-defined, but
	 * generally speaking, it is global, monotonic, and makes use of the most
	 * accurate high-performance counter available on the system.
	 * </p>
	 * 
	 * @return microseconds since an unspecified epoch
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv410LeapGetNowv">LeapC
	 *      API - LeapGetNow</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.0.0
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
	 * <b>Note:</b> Not required for e.g. {@link LEAP_IMAGE_EVENT}s even though its
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
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv416LeapSetAllocator15LEAP_CONNECTIONPK14LEAP_ALLOCATOR">LeapC
	 *      API - LeapSetAllocator</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 4.0.0
	 */
	public eLeapRS LeapSetAllocator(Pointer hConnection, LEAP_ALLOCATOR allocator);


	/**
	 * <p>
	 * Initializes a new Leap clock-rebaser handle object.
	 * </p>
	 * <p>
	 * Pass the filled-in {@link LEAP_CLOCK_REBASER} object to calls to
	 * {@link #LeapUpdateRebase(Pointer, long, long)},
	 * {@link #LeapRebaseClock(Pointer, long, LongByReference)}, and
	 * {@link #LeapDestroyClockRebaser(Pointer)}.
	 * </p>
	 * 
	 * @param phClockRebaser A pointer to the clock-rebaser object to be initialized.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv422LeapCreateClockRebaserP18LEAP_CLOCK_REBASER">LeapC
	 *      API - LeapCreateClockRebaser</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.1.2
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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv423LeapDestroyClockRebaser18LEAP_CLOCK_REBASER">LeapC
	 *      API - LeapDestroyClockRebaser</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.1.2
	 */
	public void LeapDestroyClockRebaser(Pointer hClockRebaser);


	/**
	 * <p>
	 * Computes the Ultraleap Tracking Service clock corresponding to a specified
	 * application clock value.
	 * </p>
	 * <p>
	 * Use this function to translate your application clock to the Ultraleap Tracking
	 * Service clock when interpolating frames.
	 * {@link #LeapUpdateRebase(Pointer, long, long)} must be called for every rendered
	 * frame for the relationship between the two clocks to remain synchronised.
	 * </p>
	 * 
	 * @param hClockRebaser The handle to a rebaser object created by
	 *          {@link #LeapCreateClockRebaser(LEAP_CLOCK_REBASER)}. Use
	 *          {@link LEAP_CLOCK_REBASER#handle} to obtain the handle from the clock
	 *          rebaser object.
	 * @param userClock The clock in microseconds referenced to the application clock.
	 * @param pLeapClock The corresponding Ultraleap Tracking Service clock value.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv415LeapRebaseClock18LEAP_CLOCK_REBASER7int64_tP7int64_t">LeapC
	 *      API - LeapRebaseClock</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.1.2
	 */
	public eLeapRS LeapRebaseClock(Pointer hClockRebaser, long userClock,
			LongByReference pLeapClock);


	/**
	 * <p>
	 * Updates the relationship between the Ultraleap Tracking Service clock and the user
	 * clock.
	 * </p>
	 * <p>
	 * When using {@link #LeapInterpolateFrame(Pointer, long, LEAP_TRACKING_EVENT, long)},
	 * call this function for every graphics frame rendered by your application. The
	 * function should be called as close to the actual point of rendering as possible.
	 * </p>
	 * <p>
	 * The relationship between the application clock and the Ultraleap Tracking Service
	 * clock is neither fixed nor stable. Simulation restarts can cause user clock values to
	 * change instantaneously. Certain systems simulate slow motion, or respond to heavy
	 * load, by reducing the tick rate of the user clock. As a result, the
	 * <code>LeapUpdateRebase()</code> function must be called for every rendered frame.
	 * </p>
	 * 
	 * @param hClockRebaser The handle to a rebaser object created by
	 *          {@link #LeapCreateClockRebaser(LEAP_CLOCK_REBASER)}. Use
	 *          {@link LEAP_CLOCK_REBASER#handle} to obtain the handle from the clock
	 *          rebaser object.
	 * @param userClock A clock value supplied by the application, in microseconds, sampled
	 *          at about the same time as {@link #LeapGetNow()} was sampled.
	 * @param leapClock The Ultraleap Tracking Service clock value sampled by a call to
	 *          {@link #LeapGetNow()}.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv416LeapUpdateRebase18LEAP_CLOCK_REBASER7int64_t7int64_t">LeapC
	 *      API - LeapUpdateRebase</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.1.2
	 */
	public eLeapRS LeapUpdateRebase(Pointer hClockRebaser, long userClock, long leapClock);


	/**
	 * <p>
	 * Provides the corrected camera ray intercepting the specified point on the image.
	 * </p>
	 * <p>
	 * Given a point on the image, <code>LeapPixelToRectilinear()</code> corrects for camera
	 * distortion and returns the true direction from the camera to the source of that image
	 * point within the Ultraleap Tracking camera field of view.
	 * </p>
	 * <p>
	 * This direction vector has an x and y component [x, y, 1], with the third element
	 * always 1. Note that this vector uses the 2D camera coordinate system where the x-axis
	 * parallels the longer (typically horizontal) dimension and the y-axis parallels the
	 * shorter (vertical) dimension. The camera coordinate system does not correlate to the
	 * 3D Ultraleap Tracking coordinate system.
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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv422LeapPixelToRectilinear15LEAP_CONNECTION20eLeapPerspectiveType11LEAP_VECTOR">LeapC
	 *      API - LeapPixelToRectilinear</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.1.3
	 */
	public LEAP_VECTOR.ByValue LeapPixelToRectilinear(Pointer hConnection, int camera,
			LEAP_VECTOR.ByValue pixel);


	/**
	 * <p>
	 * Provides the corrected camera ray intercepting the specified point on the image for a
	 * particular device.
	 * </p>
	 * <p>
	 * Given a point on the image, <code>LeapPixelToRectilinearEx()</code> corrects for
	 * camera distortion and returns the true direction from the camera to the source of
	 * that image point within the Ultraleap Tracking camera field of view.
	 * </p>
	 * <p>
	 * This direction vector has an x and y component [x, y, 1], with the third element
	 * always 1. Note that this vector uses the 2D camera coordinate system where the x-axis
	 * parallels the longer (typically horizontal) dimension and the y-axis parallels the
	 * shorter (vertical) dimension. The camera coordinate system does not correlate to the
	 * 3D Ultraleap Tracking coordinate system.
	 * </p>
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param hDevice The device handle to close. Use {@link LEAP_DEVICE#handle} to obtain
	 *          the handle from the device object.
	 * @param camera The camera to use, a member of the {@link eLeapPerspectiveType}
	 *          enumeration.
	 * @param pixel A vector containing the position of a pixel in the image.
	 * @return A vector containing the ray direction (the z-component of the vector is
	 *         always 1).
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv424LeapPixelToRectilinearEx15LEAP_CONNECTION11LEAP_DEVICE20eLeapPerspectiveType26eLeapCameraCalibrationType11LEAP_VECTOR">LeapC
	 *      API - LeapPixelToRectilinearEx</a>
	 * @since LeapJna 1.2.0
	 * @since Ultraleap Gemini SDK 5.4.0
	 */
	public LEAP_VECTOR.ByValue LeapPixelToRectilinearEx(Pointer hConnection, Pointer hDevice,
			int camera, LEAP_VECTOR.ByValue pixel);


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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv422LeapRectilinearToPixel15LEAP_CONNECTION20eLeapPerspectiveType11LEAP_VECTOR">LeapC
	 *      API - LeapRectilinearToPixel</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.1.3
	 */
	public LEAP_VECTOR.ByValue LeapRectilinearToPixel(Pointer hConnection, int camera,
			LEAP_VECTOR.ByValue rectilinear);


	/**
	 * <p>
	 * Provides the point in the image corresponding to a ray projecting from the camera for
	 * a particular device.
	 * </p>
	 * <p>
	 * Given a ray projected from the camera in the specified direction,
	 * <code>LeapRectilinearToPixelEx()</code> corrects for camera distortion and returns
	 * the corresponding pixel coordinates in the image.
	 * </p>
	 * <p>
	 * The ray direction is specified in relationship to the camera. The first vector
	 * element is the tangent of the "horizontal" view angle; the second element is the
	 * tangent of the "vertical" view angle.
	 * </p>
	 * <p>
	 * The <code>LeapRectilinearToPixelEx()</code> function returns pixel coordinates
	 * outside of the image bounds if you project a ray toward a point for which there is no
	 * recorded data.
	 * </p>
	 * <p>
	 * <code>LeapRectilinearToPixelEx()</code> is typically not fast enough for realtime
	 * distortion correction. For better performance, use a shader program executed on a
	 * GPU.
	 * </p>
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param hDevice The device handle to close. Use {@link LEAP_DEVICE#handle} to obtain
	 *          the handle from the device object.
	 * @param camera The camera to use, a member of the {@link eLeapPerspectiveType}
	 *          enumeration.
	 * @param rectilinear A vector containing the ray direction.
	 * @return A vector containing the pixel coordinates [x, y, 1] (with z always 1).
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv424LeapRectilinearToPixelEx15LEAP_CONNECTION11LEAP_DEVICE20eLeapPerspectiveType26eLeapCameraCalibrationType11LEAP_VECTOR">LeapC
	 *      API - LeapRectilinearToPixelEx</a>
	 * @since LeapJna 1.2.0
	 * @since Ultraleap Gemini SDK 5.4.0
	 */
	public LEAP_VECTOR.ByValue LeapRectilinearToPixelEx(Pointer hConnection, Pointer hDevice,
			int camera, LEAP_VECTOR.ByValue rectilinear);


	/**
	 * Returns an OpenCV-compatible camera matrix.
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param camera The camera to use, a member of the {@link eLeapPerspectiveType}
	 *          enumeration
	 * @param dest A pointer to a single-precision float array of size 9
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv416LeapCameraMatrix15LEAP_CONNECTION20eLeapPerspectiveTypePf">LeapC
	 *      API - LeapCameraMatrix</a>
	 * @since LeapJna 1.2.0
	 */
	public void LeapCameraMatrix(Pointer hConnection, int camera, PrimitiveArrayPointer dest);


	/**
	 * Returns an OpenCV-compatible camera matrix for a particular device.
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param hDevice The device handle to close. Use {@link LEAP_DEVICE#handle} to obtain
	 *          the handle from the device object.
	 * @param camera The camera to use, a member of the {@link eLeapPerspectiveType}
	 *          enumeration
	 * @param dest A pointer to a single-precision float array of size 9
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv418LeapCameraMatrixEx15LEAP_CONNECTION11LEAP_DEVICE20eLeapPerspectiveTypePf">LeapC
	 *      API - LeapCameraMatrixEx</a>
	 * @since LeapJna 1.2.0
	 * @since Ultraleap Gemini SDK 5.4.0
	 */
	public void LeapCameraMatrixEx(Pointer hConnection, Pointer hDevice, int camera,
			PrimitiveArrayPointer dest);


	/**
	 * This finds the default device and returns the result
	 * {@link #LeapExtrinsicCameraMatrixEx(Pointer, Pointer, int, PrimitiveArrayPointer)}.
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param camera The camera to use, a member of the {@link eLeapPerspectiveType}
	 *          enumeration
	 * @param dest A pointer to a single-precision float array of size 16, containing the
	 *          coefficients of the 4x4 matrix in Column Major order
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv425LeapExtrinsicCameraMatrix15LEAP_CONNECTION20eLeapPerspectiveTypePf">LeapC
	 *      API - LeapExtrinsicCameraMatrix</a>
	 * @since LeapJna 1.2.0
	 * @since Ultraleap Gemini SDK 5.1.0
	 */
	public void LeapExtrinsicCameraMatrix(Pointer hConnection, int camera,
			PrimitiveArrayPointer dest);


	/**
	 * <p>
	 * Returns a transformation matrix from 3D Leap coordinate space to the coordinate
	 * system of the requested camera This is composed of a 4 x 4 matrix of the form:
	 * </p>
	 * 
	 * <pre>
	 * R, t
	 * 0, 1
	 *
	 * R is a 3 x 3 rotation matrix
	 * t is a 3 x 1 translation vector
	 * </pre>
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param hDevice The device handle to close. Use {@link LEAP_DEVICE#handle} to obtain
	 *          the handle from the device object.
	 * @param camera The camera to use, a member of the {@link eLeapPerspectiveType}
	 *          enumeration
	 * @param dest A pointer to a single-precision float array of size 16, containing the
	 *          coefficients of the 4x4 matrix in Column Major order
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv427LeapExtrinsicCameraMatrixEx15LEAP_CONNECTION11LEAP_DEVICE20eLeapPerspectiveTypePf">LeapC
	 *      API - LeapExtrinsicCameraMatrixEx</a>
	 * @since LeapJna 1.2.0
	 * @since Ultraleap Gemini SDK 5.1.0
	 */
	public void LeapExtrinsicCameraMatrixEx(Pointer hConnection, Pointer hDevice, int camera,
			PrimitiveArrayPointer dest);


	/**
	 * <p>
	 * Returns an OpenCV-compatible lens distortion using the 8-parameter rational model.
	 * </p>
	 * <p>
	 * The order of the returned array is: [k1, k2, p1, p2, k3, k4, k5, k6]
	 * </p>
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param camera The camera to use, a member of the {@link eLeapPerspectiveType}
	 *          enumeration
	 * @param dest A pointer to a single-precision float array of size 8
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv420LeapDistortionCoeffs15LEAP_CONNECTION20eLeapPerspectiveTypePf">LeapC
	 *      API - LeapDistortionCoeffs</a>
	 * @since LeapJna 1.2.0
	 */
	public void LeapDistortionCoeffs(Pointer hConnection, int camera,
			PrimitiveArrayPointer dest);


	/**
	 * <p>
	 * Returns an OpenCV-compatible lens distortion for a particular device, using the
	 * 8-parameter rational model.
	 * </p>
	 * <p>
	 * The order of the returned array is: [k1, k2, p1, p2, k3, k4, k5, k6]
	 * </p>
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param hDevice The device handle to close. Use {@link LEAP_DEVICE#handle} to obtain
	 *          the handle from the device object.
	 * @param camera The camera to use, a member of the {@link eLeapPerspectiveType}
	 *          enumeration
	 * @param dest A pointer to a single-precision float array of size 8
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv422LeapDistortionCoeffsEx15LEAP_CONNECTION11LEAP_DEVICE20eLeapPerspectiveTypePf">LeapC
	 *      API - LeapDistortionCoeffsEx</a>
	 * @since LeapJna 1.2.0
	 * @since Ultraleap Gemini SDK 5.4.0
	 */
	public void LeapDistortionCoeffsEx(Pointer hConnection, Pointer hDevice, int camera,
			PrimitiveArrayPointer dest);


	/**
	 * This finds the default device and returns the result of
	 * {@link #LeapScaleOffsetMatrixEx(Pointer, Pointer, int, PrimitiveArrayPointer)}
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param camera The camera to use, a member of the {@link eLeapPerspectiveType}
	 *          enumeration
	 * @param dest A pointer to a single-precision float array of size 16, containing the
	 *          coefficients of the 4x4 matrix in Column Major order
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv421LeapScaleOffsetMatrix15LEAP_CONNECTION20eLeapPerspectiveTypePf">LeapC
	 *      API - LeapScaleOffsetMatrix</a>
	 * @since LeapJna 1.2.0
	 * @since Ultraleap Gemini SDK 5.x.x
	 */
	public void LeapScaleOffsetMatrix(Pointer hConnection, int camera,
			PrimitiveArrayPointer dest);


	/**
	 * <p>
	 * Returns the appropriate scale and offset coefficients required to project normalised
	 * Rectilinear coordinates to image-scale coordinates.
	 * </p>
	 * <p>
	 * This is composed of a 4 x 4 matrix of the form:
	 * 
	 * <pre>
	 * scale_x, 0, 0, offset_x, 0, 1, 0, 0, 0, 0, scale_z, offset_z 0, 0, 0, 1
	 * </pre>
	 * <p>
	 * This matrix is specific to the size of the current image as contained within
	 * {@link LEAP_IMAGE}.
	 * </p>
	 * <p>
	 * In practical terms, use this matrix in combination with normalised rays to project 3D
	 * points into a rectilinear image space (i.e. to visualise hands on an undistorted
	 * image).
	 * </p>
	 * <p>
	 * The pipeline would be:
	 * <ol>
	 * <li>Take 3D points from hand tracking.
	 * <li>Apply an extrinsic transformation to a specific camera's coordinate system (@sa
	 * LeapExtrinsicCameraMatrixEx)
	 * <li>Apply a perspective division to transform 3D points to rays.
	 * <li>Apply the ScaleOffset matrix to these points.
	 * </ol>
	 * <p>
	 * These points will now be in the correct coordinate system consistent with the
	 * undistorted rectilinear image provided by {@link LEAP_IMAGE#distortion_matrix}.
	 * </p>
	 * 
	 * @param hConnection The connection handle created by
	 *          {@link #LeapCreateConnection(LEAP_CONNECTION_CONFIG, LEAP_CONNECTION)
	 *          LeapCreateConnection()}. Use {@link LEAP_CONNECTION#handle} to obtain the
	 *          handle from the connection object.
	 * @param hDevice The device handle to close. Use {@link LEAP_DEVICE#handle} to obtain
	 *          the handle from the device object.
	 * @param camera The camera to use, a member of the {@link eLeapPerspectiveType}
	 *          enumeration
	 * @param dest A pointer to a single-precision float array of size 16, containing the
	 *          coefficients of the 4x4 matrix in Column Major order
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv423LeapScaleOffsetMatrixEx15LEAP_CONNECTION11LEAP_DEVICE20eLeapPerspectiveTypePf">LeapC
	 *      API - LeapScaleOffsetMatrixEx</a>
	 * @since LeapJna 1.2.0
	 * @since Ultraleap Gemini SDK 5.x.x
	 */
	public void LeapScaleOffsetMatrixEx(Pointer hConnection, Pointer hDevice, int camera,
			PrimitiveArrayPointer dest);


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
	 * @deprecated This function is no longer supported. Calling it will have no effect.
	 * @since LeapJna 1.0.0
	 */
	@Deprecated
	public eLeapRS LeapTelemetryProfiling(Pointer hConnection,
			LEAP_TELEMETRY_DATA telemetryData);


	/**
	 * @return 
	 * @deprecated This function is no longer supported. Calling it will have no effect.
	 * @since LeapJna 1.0.0
	 */
	@Deprecated
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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv417LeapRecordingOpenP14LEAP_RECORDINGPKc25LEAP_RECORDING_PARAMETERS">LeapC
	 *      API - LeapRecordingOpen</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.2.0
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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv418LeapRecordingWrite14LEAP_RECORDINGP19LEAP_TRACKING_EVENTP8uint64_t">LeapC
	 *      API - LeapRecordingWrite</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.2.0
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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv422LeapRecordingGetStatus14LEAP_RECORDINGP21LEAP_RECORDING_STATUS">LeapC
	 *      API - LeapRecordingGetStatus</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.2.0
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
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv417LeapRecordingRead14LEAP_RECORDINGP19LEAP_TRACKING_EVENT8uint64_t">LeapC
	 *      API - LeapRecordingRead</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.2.0
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
	 * @param pRecording The recording being read from. Use {@link LEAP_RECORDING#handle}
	 *          to obtain the handle from the recording object.
	 * @param pncbEvent A pointer that receives the number of bytes required to store the
	 *          next frame.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv421LeapRecordingReadSize14LEAP_RECORDINGP8uint64_t">LeapC
	 *      API - LeapRecordingReadSize</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.2.0
	 */
	public eLeapRS LeapRecordingReadSize(Pointer pRecording, LongByReference pncbEvent);


	/**
	 * Closes a {@link LEAP_RECORDING}.
	 * 
	 * @param ppRecording The recording being closed. Will modify *ppRecording to be null.
	 * @return The operation result code, a member of the {@link eLeapRS} enumeration.
	 * @see <a href=
	 *      "https://docs.ultraleap.com/tracking-api/group/group___functions.html#_CPPv418LeapRecordingCloseP14LEAP_RECORDING">LeapC
	 *      API - LeapRecordingClose</a>
	 * @since LeapJna 1.0.0
	 * @since Ultraleap Orion SDK 3.2.0
	 */
	public eLeapRS LeapRecordingClose(LEAP_RECORDING ppRecording);
}