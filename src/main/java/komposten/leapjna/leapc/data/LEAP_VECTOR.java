package komposten.leapjna.leapc.data;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;


/**
 * <p>
 * A three-element, floating-point vector.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___v_e_c_t_o_r">LeapC
 *      API - LEAP_VECTOR</a>
 */
@FieldOrder({ "x", "y", "z" })
public class LEAP_VECTOR extends Structure
{
	public float x;
	public float y;
	public float z;

	/**
	 * @return The vector's values as an array in the order: x, y, z.
	 */
	public float[] asArray()
	{
		return new float[] { x, y, z };
	}
}