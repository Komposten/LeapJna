package komposten.leapjna.leapc.enums;

/**
 * Defines the various types of data that may be allocated using the allocator.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#ga4ef8bec6852369fbf3dcd97906d0a9fa">LeapC
 *      API - eLeapAllocatorType</a>
 */
public enum eLeapAllocatorType
{
	Unknown(-1),
	
	/** Signed 8-bit integer (char) */
	Int8(0),

	/** Unsigned 8-bit integer (byte) */
	Uint8(1),

	/** Signed 16-bit integer */
	Int16(2),

	/** Unsigned 16-bit integer */
	UInt16(3),

	/** Signed 32-bit integer */
	Int32(4),

	/** Unsigned 32-bit integer */
	UInt32(5),

	/** Single-precision 32-bit floating-point */
	Float(6),

	/** Signed 64-bit integer */
	Int64(8),

	/** Unsigned 64-bit integer */
	UInt64(9),

	/** Double-precision 64-bit floating-point */
	Double(10);

	public final int value;

	private eLeapAllocatorType(int value)
	{
		this.value = value;
	}


	public static eLeapAllocatorType parse(int value, eLeapAllocatorType defaultValue)
	{
		for (eLeapAllocatorType o : eLeapAllocatorType.values())
		{
			if (o.value == value)
			{
				return o;
			}
		}

		return defaultValue;
	}
}
