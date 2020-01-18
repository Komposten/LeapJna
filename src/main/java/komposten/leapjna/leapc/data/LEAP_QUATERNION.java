package komposten.leapjna.leapc.data;

import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;


@FieldOrder({ "w", "x", "y", "z" })
public class LEAP_QUATERNION extends Structure
{
	public float w;
	public float x;
	public float y;
	public float z;


	/**
	 * @return The roll (x-axis rotation) in radians.
	 */
	public float getRoll()
	{
		double nominator = 2 * (w * x + y * z);
		double denominator = 1 - 2 * (x * x + y * y);

		return (float) Math.atan2(nominator, denominator);
	}


	/**
	 * @return The yaw (y-axis rotation) in radians.
	 */
	public float getYaw()
	{
		return (float) Math.asin(2 * (w * y - z * x));
	}


	/**
	 * @return The pitch (z-axis rotation) in radians.
	 */
	public float getPitch()
	{
		double nominator = 2 * (w * z + x * y);
		double denominator = 1 - 2 * (y * y + z * z);

		return (float) Math.atan2(nominator, denominator);
	}


	/**
	 * @return This rotation represented as Euler angles in radians, ordered: roll
	 *         (x-axis rotation), yaw (y-axis rotation) and pitch (z-axis
	 *         rotation).
	 */
	public float[] getEuler()
	{
		return new float[] { getRoll(), getYaw(), getPitch() };
	}
}