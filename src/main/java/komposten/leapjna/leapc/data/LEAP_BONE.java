package komposten.leapjna.leapc.data;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

@FieldOrder({ "prev_joint", "next_joint", "width", "rotation" })
public class LEAP_BONE extends Structure
{
	public LEAP_VECTOR prev_joint;
	public LEAP_VECTOR next_joint;
	public float width;
	public LEAP_QUATERNION rotation;
}