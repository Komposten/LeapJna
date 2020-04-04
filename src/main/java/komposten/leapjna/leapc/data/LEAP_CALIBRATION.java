package komposten.leapjna.leapc.data;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;


/**
 * <p>
 * Represents a calibration object.
 * </p>
 * <p>
 * Not currently of any particular use.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#ga555686ca5178769989951edd14b69582">LeapC
 *      API - LEAP_CALIBRATION</a>
 */
@FieldOrder({ "handle" })
public class LEAP_CALIBRATION extends Structure
{
	/**
	 * A handle to a Leap device object.
	 */
	public Pointer handle;
}
