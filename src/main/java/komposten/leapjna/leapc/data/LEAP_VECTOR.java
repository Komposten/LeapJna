package komposten.leapjna.leapc.data;

import com.sun.jna.Pointer;
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

	public LEAP_VECTOR()
	{
		super(ALIGN_NONE);
	}


	public LEAP_VECTOR(float x, float y, float z)
	{
		set(x, y, z);
	}


	public LEAP_VECTOR(Pointer pointer)
	{
		super(pointer);
		read();
	}


	/**
	 * @return The vector's values as an array in the order: x, y, z.
	 */
	public float[] asArray()
	{
		return new float[] { x, y, z };
	}


	/**
	 * Sets the values in this vector and writes them to native memory.
	 */
	public void set(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;

		write();
	}


	public static class ByValue extends LEAP_VECTOR implements Structure.ByValue
	{
		public ByValue()
		{
			super();
		}
	}
}