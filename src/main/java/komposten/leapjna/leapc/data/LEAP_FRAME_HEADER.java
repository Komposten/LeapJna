package komposten.leapjna.leapc.data;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.leapc.LeapC;


/**
 * <p>
 * Identifying information for a frame of tracking data.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___f_r_a_m_e___h_e_a_d_e_r">LeapC
 *      API - LEAP_FRAME_HEADER</a>
 */
@FieldOrder({ "reserved", "frame_id", "timestamp" })
public class LEAP_FRAME_HEADER extends Structure
{
	/** Reserved, set to zero. */
	public Pointer reserved;

	/**
	 * <p>
	 * A unique identifier for this frame.
	 * </p>
	 * <p>
	 * All frames carrying this frame ID are part of the same unit of processing. This
	 * counter is generally an increasing counter, but <em>may reset to another value</em>
	 * if the user stops and restarts streaming.
	 * </p>
	 * <p>
	 * For interpolated frames, this value corresponds to the identifier of the frame upper
	 * bound.
	 * </p>
	 */
	public long frame_id;

	/**
	 * The timestamp for this frame, in microseconds, referenced against
	 * {@link LeapC#LeapGetNow()}.
	 */
	public long timestamp;
}