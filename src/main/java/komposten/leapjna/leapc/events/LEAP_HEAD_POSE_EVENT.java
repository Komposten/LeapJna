package komposten.leapjna.leapc.events;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.LeapC;
import komposten.leapjna.leapc.data.LEAP_QUATERNION;
import komposten.leapjna.leapc.data.LEAP_VECTOR;


/**
 * TODO Test this mapping!
 */
@FieldOrder({ "timestamp", "head_position", "head_orientation" })
public class LEAP_HEAD_POSE_EVENT extends Structure implements LEAP_EVENT
{
	/**
	 * The timestamp for this head pose, in microseconds, referenced against
	 * {@link LeapC#LeapGetNow()}.
	 */
	public long timestamp;

	/**
	 * The position of the user's head. Positional tracking must be enabled.
	 */
	public LEAP_VECTOR head_position;

	/**
	 * The orientation of the user's head. Positional tracking must be enabled.
	 */
	public LEAP_QUATERNION head_orientation;

	public LEAP_HEAD_POSE_EVENT()
	{
		super(ALIGN_NONE);
	}


	public LEAP_HEAD_POSE_EVENT(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		System.out.println(pointer.dump(0, 40));
		read();
	}
}
