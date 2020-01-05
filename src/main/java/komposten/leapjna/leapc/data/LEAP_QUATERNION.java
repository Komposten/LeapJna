package komposten.leapjna.leapc.data;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

@FieldOrder({ "x", "y", "z", "w" })
public class LEAP_QUATERNION extends Structure
{
	public float x;
	public float y;
	public float z;
	public float w;
}