package komposten.leapjna.leapc.data;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.leapc.enums.eLeapHandType;

@FieldOrder({ "id", "flags", "type", "confidence", "visible_time", "pinch_distance",
		"grab_angle", "pinch_strength", "grab_strength", "palm", "digits", "arm" })
public class LEAP_HAND extends Structure
{
	//FIXME Calculate this size dynamically. Is a must to work on
	// e.g. 32-bit systems (pointers are 4 bytes instead of 8).
	public static final int SIZE = 1084;
	@FieldOrder({ "thumb", "index", "middle", "ring", "pinky" })
	public static class DigitStruct extends Structure
	{
		public LEAP_DIGIT thumb;
		public LEAP_DIGIT index;
		public LEAP_DIGIT middle;
		public LEAP_DIGIT ring;
		public LEAP_DIGIT pinky;
		
		/**
		 * @return All digits as an array in the order: thumb, index, middle, ring, pinky.
		 */
		public LEAP_DIGIT[] asArray()
		{
			return new LEAP_DIGIT[] { thumb, index, middle, ring, pinky };
		}
	}

	public int id;
	public int flags;
	/**
	 * The hand type as a byte. Either 0 (left) or 1 (right).
	 * Use {@link #getType()} to get the type as a {@link eLeapHandType} value. 
	 */
	public byte type;
	public float confidence;
	public long visible_time;
	public float pinch_distance;
	public float grab_angle;
	public float pinch_strength;
	public float grab_strength;
	public LEAP_PALM palm;
	public LEAP_HAND.DigitStruct digits;
	public LEAP_BONE arm;
	
	private eLeapHandType typeE;
	
	
	public LEAP_HAND()
	{
		super();
		read();
	}
	
	
	public LEAP_HAND(Pointer pointer)
	{
		super(pointer);
		read();
	}
	
	
	public eLeapHandType getType()
	{
		if (typeE == null)
		{
			typeE = eLeapHandType.parse(type, eLeapHandType.Unknown);
		}
		
		return typeE;
	}

	public static class ByReference extends LEAP_HAND implements Structure.ByReference
	{
	}
}