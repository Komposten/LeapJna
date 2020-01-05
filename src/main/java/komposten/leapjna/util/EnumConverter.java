package komposten.leapjna.util;

import com.sun.jna.FromNativeContext;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;

public class EnumConverter implements TypeConverter
{

	@Override
	public Object fromNative(Object nativeValue, FromNativeContext context)
	{
		Integer value = (Integer)nativeValue;
		Class<?> targetClass = context.getTargetType();
		
		if (!JnaEnum.class.isAssignableFrom(targetClass)) {
			return null;
		}
		Object[] enums = targetClass.getEnumConstants();
		if (enums.length == 0) {
			throw new RuntimeException("Could not convert type; no valid values assigned!");
		}

		JnaEnum<?> instance = (JnaEnum<?>) enums[0];
		return instance.getForValue(value);
	}


	@Override
	public Object toNative(Object value, ToNativeContext context)
	{
		JnaEnum<?> enumValue = (JnaEnum<?>) value;
		
		if (enumValue == null)
			return null;
		return enumValue.getIntValue();
	}

	
	@Override
	public Class<?> nativeType()
	{
		return Integer.class;
	}
}
