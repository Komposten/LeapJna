package komposten.leapjna.leapc.events;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.ptr.LongByReference;

import komposten.leapjna.LeapC;
import komposten.leapjna.LeapC.LEAP_CONNECTION_MESSAGE;
import komposten.leapjna.leapc.data.LEAP_VARIANT;


/**
 * <p>
 * Contains the response to a configuration value request.
 * </p>
 * <p>
 * Call {@link LeapC#LeapRequestConfigValue(Pointer, String, LongByReference)} to request a
 * service config value. The value is fetched asynchronously since it requires a service
 * transaction. {@link LeapC#LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)}
 * returns this event structure when the request has been processed. Use the requestID
 * value to correlate the response to the originating request.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___c_o_n_f_i_g___r_e_s_p_o_n_s_e___e_v_e_n_t">LeapC
 *      API - LEAP_CONFIG_RESPONSE_EVENT</a>
 */
@FieldOrder({ "requestID", "value" })
public class LEAP_CONFIG_RESPONSE_EVENT extends Structure
{
	/** An identifier for correlating the request and response. */
	public int requestID;

	/**
	 * The configuration value retrieved from the service. Do not free any memory pointed to
	 * by this member. The value held is only valid until the next call to
	 * {@link LeapC#LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)}.
	 */
	public LEAP_VARIANT value;

	public LEAP_CONFIG_RESPONSE_EVENT(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}
}
