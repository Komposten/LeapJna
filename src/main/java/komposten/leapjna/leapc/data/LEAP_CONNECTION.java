package komposten.leapjna.leapc.data;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;


/**
 * <p>
 * A representation of a Leap connection, used to interact with the Leap API.
 * </p>
 * <p>
 * Use {@link #handle} when interacting with the Leap API methods.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___c_o_n_n_e_c_t_i_o_n">LeapC
 *      API - LEAP_CONNECTION</a>
 */
@FieldOrder({ "handle" })
public class LEAP_CONNECTION extends Structure
{
	/**
	 * A handle to the Leap connection object.
	 */
	public Pointer handle;
}