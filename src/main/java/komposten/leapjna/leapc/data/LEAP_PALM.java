package komposten.leapjna.leapc.data;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

@FieldOrder({ "position", "stabilized_position", "velocity", "normal", "width",
		"direction", "orientation" })
public class LEAP_PALM extends Structure
{
	public LEAP_VECTOR position;
	public LEAP_VECTOR stabilized_position;
	public LEAP_VECTOR velocity;
	public LEAP_VECTOR normal;
	public float width;
	public LEAP_VECTOR direction;
	public LEAP_QUATERNION orientation;
}