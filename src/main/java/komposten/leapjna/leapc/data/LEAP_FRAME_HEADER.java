package komposten.leapjna.leapc.data;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

@FieldOrder({ "reserved", "frame_id", "timestamp" })
public class LEAP_FRAME_HEADER extends Structure
{
	public Pointer reserved;
	public long frame_id;
	public long timestamp;
}