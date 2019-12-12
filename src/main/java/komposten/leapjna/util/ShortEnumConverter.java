package komposten.leapjna.util;

import com.sun.jna.FromNativeContext;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;

public class ShortEnumConverter implements TypeConverter
{

	@Override
	public Object fromNative(Object nativeValue, FromNativeContext context)
	{
		Short value = (Short)nativeValue;
		Class<?> targetClass = context.getTargetType();
		
		System.out.println("Val: " + value);
		
		if (!JnaShortEnum.class.isAssignableFrom(targetClass)) {
			return null;
		}
		Object[] enums = targetClass.getEnumConstants();
		if (enums.length == 0) {
			throw new RuntimeException("Could not convert type; no valid values assigned!");
		}

		System.out.format("senum: %s, value: %s (%s)%n", targetClass.getSimpleName(),
				Integer.toHexString(value), ((JnaShortEnum<?>)enums[0]).getForValue(value));
		
		JnaShortEnum<?> instance = (JnaShortEnum<?>) enums[0];
		return instance.getForValue(value);
	}


	@Override
	public Object toNative(Object value, ToNativeContext context)
	{
		JnaShortEnum<?> enumValue = (JnaShortEnum<?>) value;
		
		if (enumValue == null)
			return null;
		return enumValue.getShortValue();
	}

	
	@Override
	public Class<?> nativeType()
	{
		return Short.class;
	}
}
