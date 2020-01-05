package komposten.leapjna.leapc.data;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

@FieldOrder({ "x", "y", "z" })
public class LEAP_VECTOR extends Structure
{
	public float x;
	public float y;
	public float z;
}