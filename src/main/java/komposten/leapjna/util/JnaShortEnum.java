package komposten.leapjna.util;

public interface JnaShortEnum<T>
{
	public short getShortValue();
	public T getForValue(short value);
}
