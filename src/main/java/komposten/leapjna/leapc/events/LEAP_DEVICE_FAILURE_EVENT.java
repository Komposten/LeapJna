package komposten.leapjna.leapc.events;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.LeapC;
import komposten.leapjna.leapc.data.LEAP_CONNECTION_MESSAGE;
import komposten.leapjna.leapc.data.LEAP_DEVICE;
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
 * <p>
 * TODO Test this mapping!
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___d_e_v_i_c_e___f_a_i_l_u_r_e___e_v_e_n_t">LeapC
 *      API - LEAP_DEVICE_FAILURE_EVENT</a>
 */
@FieldOrder({ "status", "hDevice" })
public class LEAP_DEVICE_FAILURE_EVENT extends Structure implements LEAP_EVENT
{
	/** The status of this failure event. */
	public int status;

	/**
	 * <p>
	 * A handle to the device generating this failure event, if available, otherwise
	 * <code>null</code>.
	 * </p>
	 * <p>
	 * You are not responsible for closing this handle.
	 * </p>
	 * TODO Maybe replace this with a {@link LEAP_DEVICE}, is possible (determine when
	 * testing).
	 */
	public Pointer hDevice;

	private eLeapDeviceStatus[] statusE;

	public LEAP_DEVICE_FAILURE_EVENT(Pointer pointer)
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

}
