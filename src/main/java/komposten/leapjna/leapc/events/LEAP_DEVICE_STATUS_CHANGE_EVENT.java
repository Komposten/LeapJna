package komposten.leapjna.leapc.events;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.leapc.data.LEAP_DEVICE_REF;
import komposten.leapjna.leapc.enums.eLeapDeviceStatus;


/**
 * <p>
 * A notification that a device's status has changed.
 * </p>
 * <p>
 * One of these messages is received by the client as soon as the service is connected, or
 * when a new device is attached.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___d_e_v_i_c_e___s_t_a_t_u_s___c_h_a_n_g_e___e_v_e_n_t">LeapC
 *      API - LEAP_DEVICE_STATUS_CHANGE_EVENT</a>
 */
@FieldOrder({ "device", "last_status", "status" })
public class LEAP_DEVICE_STATUS_CHANGE_EVENT extends Structure implements LEAP_EVENT
{
	/** The handle reference of the newly attached device. */
	public LEAP_DEVICE_REF device;

	/**
	 * <p>
	 * The last known status of the device.
	 * </p>
	 * <p>
	 * A combination of flags from the {@link eLeapDeviceStatus} collection.
	 * </p>
	 * <p>
	 * Use {@link #getLastStatus()} to get the status as a {@link eLeapDeviceStatus} value.
	 * </p>
	 */
	public int last_status;

	/**
	 * <p>
	 * The current status of the device.
	 * </p>
	 * <p>
	 * A combination of flags from the {@link eLeapDeviceStatus} collection.
	 * </p>
	 * <p>
	 * Use {@link #getStatus()} to get the status as a {@link eLeapDeviceStatus} value.
	 * </p>
	 */
	public int status;

	private eLeapDeviceStatus[] statusE;
	private eLeapDeviceStatus[] lastStatusE;

	public LEAP_DEVICE_STATUS_CHANGE_EVENT(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}


	/**
	 * @return The status flags as an {@link eLeapDeviceStatus} array instead of an
	 *         <code>int</code> mask.
	 */
	public eLeapDeviceStatus[] getStatus()
	{
		if (statusE == null)
		{
			statusE = eLeapDeviceStatus.parseMask(status);
		}

		return statusE;
	}


	/**
	 * @return The last status flags as an {@link eLeapDeviceStatus} array instead of an
	 *         <code>int</code> mask.
	 */
	public eLeapDeviceStatus[] getLastStatus()
	{
		if (lastStatusE == null)
		{
			lastStatusE = eLeapDeviceStatus.parseMask(last_status);
		}

		return lastStatusE;
	}
}
