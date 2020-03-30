package komposten.leapjna.leapc.data;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;


/**
 * <p>
 * Describes a bone's position and orientation.
 * </p>
 * <p>
 * Bones are members of the {@link LEAP_DIGIT} struct.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___b_o_n_e">LeapC
 *      API - LEAP_BONE</a>
 */
@FieldOrder({ "prev_joint", "next_joint", "width", "rotation" })
public class LEAP_BONE extends Structure
{
	/** The base of the bone, closer to the heart. I.e. the bone's origin. */
	public LEAP_VECTOR prev_joint;

	/** The end of the bone, further from the heart. */
	public LEAP_VECTOR next_joint;

	/** The average width of the flesh around the bone in millimetres. */
	public float width;

	/**
	 * <p>
	 * Rotation in world space from the forward direction.
	 * </p>
	 * <p>
	 * Convert the quaternion to a matrix to derive the basis vectors.
	 * </p>
	 */
	public LEAP_QUATERNION rotation;
}