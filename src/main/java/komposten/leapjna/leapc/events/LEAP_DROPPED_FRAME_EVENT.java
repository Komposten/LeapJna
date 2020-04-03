package komposten.leapjna.leapc.events;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.leapc.enums.eLeapDroppedFrameType;
import komposten.leapjna.leapc.enums.eLeapLogSeverity;


/**
 * A dropped frame event.
 */
@FieldOrder({ "frame_id", "type" })
public class LEAP_DROPPED_FRAME_EVENT extends Structure
{
	/**
	 * The type of message. Use {@link #getSeverity()} to get the severity as an
	 * {@link eLeapLogSeverity} value.
	 */
	public long frame_id;

	/**
	 * <p>
	 * The dropped frame type. Use {@link #getType()} to get the type as an
	 * {@link eLeapDroppedFrameType} value.
	 * </p>
	 */
	public int type;

	private eLeapDroppedFrameType typeE;

	public LEAP_DROPPED_FRAME_EVENT(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}


	/**
	 * @return The type as an {@link eLeapDroppedFrameType} instead of an <code>int</code>.
	 */
	public eLeapDroppedFrameType getType()
	{
		if (typeE == null)
		{
			typeE = eLeapDroppedFrameType.parse(type, eLeapDroppedFrameType.Unknown);
		}
		return typeE;
	}
}
