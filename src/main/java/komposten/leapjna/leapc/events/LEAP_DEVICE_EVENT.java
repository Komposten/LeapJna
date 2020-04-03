package komposten.leapjna.leapc.events;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.LeapC;
import komposten.leapjna.LeapC.LEAP_CONNECTION_MESSAGE;
import komposten.leapjna.leapc.data.LEAP_DEVICE_REF;
import komposten.leapjna.leapc.enums.eLeapDeviceStatus;


/**
 * <p>
 * Device event information.
 * </p>
 * <p>
 * {@link LeapC#LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)} produces a
 * message containing this event when a new device is detected. You can use the handle
 * provided by the device filed to open a device so that you can access its properties.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___d_e_v_i_c_e___e_v_e_n_t">LeapC
 *      API - LEAP_DEVICE_EVENT</a>
 */
@FieldOrder({ "flags", "device", "status" })
public class LEAP_DEVICE_EVENT extends Structure implements LEAP_EVENT
{
	/** Reserved for future use. */
	public int flags;

	/** The handle reference of the newly attached device. */
	public LEAP_DEVICE_REF device;

	/**
	 * <p>
	 * The status of the connected device.
	 * </p>
	 * <p>
	 * A combination of flags from the {@link eLeapDeviceStatus} collection.
	 * </p>
	 * <p>
	 * Use {@link #getStatus()} to get the status as a {@link eLeapDeviceStatus} value.
	 * </p>
	 */
	public int status;

	private eLeapDeviceStatus statusE;

	public LEAP_DEVICE_EVENT(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}


	/**
	 * @return The status as an {@link eLeapDeviceStatus} instead of an
	 *         <code>int</code>.
	 */
	public eLeapDeviceStatus getStatus()
	{
		if (statusE == null)
		{
			statusE = eLeapDeviceStatus.parse(status, eLeapDeviceStatus.Unknown);
		}
		return statusE;
	}
}
