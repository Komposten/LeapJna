package komposten.leapjna.leapc.data;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.ptr.IntByReference;

import komposten.leapjna.LeapC;


/**
 * <p>
 * A reference to a Leap device.
 * </p>
 * <p>
 * Get a <code>LEAP_DEVICE_REF</code> by calling
 * {@link LeapC#LeapGetDeviceList(Pointer, LEAP_DEVICE_REF, IntByReference)}. Access a
 * device by calling {@link LeapC#LeapOpenDevice(LEAP_DEVICE_REF, LEAP_DEVICE)} with this
 * reference. <code>LeapOpenDevice()</code> provides a {@link LEAP_DEVICE} struct, which
 * is a handle to an open device.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___c_o_n_n_e_c_t_i_o_n___e_v_e_n_t">LeapC
 *      API - LEAP_CONNECTION_EVENT</a>
 */
@FieldOrder({ "handle", "id" })
public class LEAP_DEVICE_REF extends Structure
{
	/** A device handle. */
	public Pointer handle;

	/** A generic identifier. */
	public int id;

	public LEAP_DEVICE_REF()
	{
		super(ALIGN_NONE);
	}

	public LEAP_DEVICE_REF(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}
}
