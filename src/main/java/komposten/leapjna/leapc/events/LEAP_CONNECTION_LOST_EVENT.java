package komposten.leapjna.leapc.events;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.LeapC;
import komposten.leapjna.leapc.data.LEAP_CONNECTION_MESSAGE;


/**
 * <p>
 * Received from {@link LeapC#LeapPollConnection(Pointer, int, LEAP_CONNECTION_MESSAGE)}
 * when a connection to the Leap Motion service is lost.
 * </p>
 * <p>
 * If a LeapC function that performs a transaction with the Leap Motion service is called
 * after the connection is lost, the next call to LeapPollConnection() will return this
 * event. Otherwise, it can take up to 5 seconds of polling the connection to receive this
 * event.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___c_o_n_n_e_c_t_i_o_n___l_o_s_t___e_v_e_n_t">LeapC
 *      API - LEAP_CONNECTION_LOST_EVENT</a>
 */
@FieldOrder({ "flags" })
public class LEAP_CONNECTION_LOST_EVENT extends Structure implements LEAP_EVENT
{
	/** Reserved for future use. */
	public int flags;


	public LEAP_CONNECTION_LOST_EVENT(Pointer pointer)
	{
		super(pointer);
		read();
	}
}
