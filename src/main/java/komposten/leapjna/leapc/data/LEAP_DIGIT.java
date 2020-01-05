package komposten.leapjna.leapc.data;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

@FieldOrder({ "finger_id", "metacarpal", "proximal", "intermediate", "distal", "is_extended" })
public class LEAP_DIGIT extends Structure
{
	public int finger_id;
	public LEAP_BONE metacarpal;
	public LEAP_BONE proximal;
	public LEAP_BONE intermediate;
	public LEAP_BONE distal;
	public int is_extended;

	/**
	 * @return The digit's bones as an array in the order: metacarpal, proximal, intermediate, distal.
	 */
	public LEAP_BONE[] boneArray()
	{
		return new LEAP_BONE[] { metacarpal, proximal, intermediate, distal };
	}
}