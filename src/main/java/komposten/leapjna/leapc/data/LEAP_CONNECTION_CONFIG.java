package komposten.leapjna.leapc.data;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

/**
 * <p>
 * Specifies the configuration for a connection.
 * </p>
 * <p>
 * Currently, there are no externally useful configuration options.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___c_o_n_n_e_c_t_i_o_n___c_o_n_f_i_g">LeapC
 *      API - LEAP_CONNECTION_CONFIG</a>
 */
@FieldOrder({ "size", "flags", "server_namespace" })
public class LEAP_CONNECTION_CONFIG extends Structure
{
	/** Set to the final size of this structure. */
	public int size;

	/** The connection configuration flags. */
	public int flags;

	/** For internal use. */
	public String server_namespace;
}