package komposten.leapjna.util;

import com.sun.jna.DefaultTypeMapper;

public class LeapTypeMapper extends DefaultTypeMapper
{
	public LeapTypeMapper()
	{
		addTypeConverter(JnaEnum.class, new EnumConverter());
		addTypeConverter(JnaShortEnum.class, new ShortEnumConverter());
	}
}
