package komposten.leapjna.leapc.events;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.ptr.LongByReference;

import komposten.leapjna.leapc.LeapC;
import komposten.leapjna.leapc.data.LEAP_CONNECTION_MESSAGE;
import komposten.leapjna.leapc.data.LEAP_VARIANT;


/**
 * <p>
 * The result of a configuration change request. Contains a status of true for a
 * successful change.
 * </p>
 * <p>
 * Call {@link LeapC#LeapSaveConfigValue(Pointer, String, LEAP_VARIANT, LongByReference)}
 * to request a service config change. The change is performed asynchronously -- and may
 * fail. {@link LeapC#LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)} returns
 * this event structure when the request has been processed. Use the requestID value to
 * correlate the response to the originating request.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___c_o_n_f_i_g___c_h_a_n_g_e___e_v_e_n_t">LeapC
 *      API - LEAP_CONFIG_CHANGE_EVENT</a>
 */
@FieldOrder({ "requestID", "value" })
public class LEAP_CONFIG_CHANGE_EVENT extends Structure implements LEAP_EVENT
{
	/** An identifier for correlating the request and response. */
	public int requestID;

	/**
	 * The result of the change operation: true on success; false on failure.
	 */
	public boolean status;

	public LEAP_CONFIG_CHANGE_EVENT(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}
}
