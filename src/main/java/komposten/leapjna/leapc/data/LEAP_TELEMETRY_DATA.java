package komposten.leapjna.leapc.data;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;


@FieldOrder({ "thread_id", "start_time", "end_time", "zone_depth", "file_name",
		"line_number", "zone_name" })
public class LEAP_TELEMETRY_DATA extends Structure
{
	public int thread_id;
	public long start_time;
	public long end_time;
	public int zone_depth;
	public String file_name;
	public int line_number;
	public String zone_name;
	
	public LEAP_TELEMETRY_DATA()
	{
		super(ALIGN_NONE);
	}
}
