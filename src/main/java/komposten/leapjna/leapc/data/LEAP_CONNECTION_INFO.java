package komposten.leapjna.leapc.data;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.leapc.enums.eLeapConnectionStatus;

@FieldOrder({ "size", "status" })
public class LEAP_CONNECTION_INFO extends Structure
{
	public int size;
	/**
	 * The connection status as an int. Use {@link #getStatus()} to get the
	 * status as a {@link eLeapConnectionStatus} value.
	 */
	public int status;
	
	private eLeapConnectionStatus statusE;
	
	
	public eLeapConnectionStatus getStatus()
	{
		if (statusE == null)
		{
			statusE = eLeapConnectionStatus.parse(status, eLeapConnectionStatus.Unknown);
		}
		
		return statusE;
	}
	
	public static class ByReference extends LEAP_CONNECTION_INFO
			implements Structure.ByReference
	{
	}
}