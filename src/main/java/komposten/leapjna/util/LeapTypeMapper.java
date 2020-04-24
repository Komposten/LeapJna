package komposten.leapjna.util;

import com.sun.jna.DefaultTypeMapper;

import komposten.leapjna.leapc.enums.eLeapRS;

public class LeapTypeMapper extends DefaultTypeMapper
{
	public LeapTypeMapper()
	{
		addTypeConverter(eLeapRS.class, new eLeapRSConverter());
	}
}
