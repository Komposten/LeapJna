package komposten.leapjna.leapc.enums;

import java.util.ArrayList;
import java.util.List;


/**
 * Flags enumerating Leap device capabilities.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#ga5d5cfae19b88160a1a5c14cc46faf435">LeapC
 *      API - eLeapDeviceCaps</a>
 */
public enum eLeapDeviceCaps
{
	/** The device has no specific capabilities. */
	None(0x00000000),

	/** The device can send color images. */
	Color(0x00000001);

	public final int value;

	private eLeapDeviceCaps(int value)
	{
		this.value = value;
	}


	public static eLeapDeviceCaps[] parseMask(int mask)
	{
		List<eLeapDeviceCaps> flags = new ArrayList<>();

		for (eLeapDeviceCaps o : eLeapDeviceCaps.values())
		{
			if ((mask & o.value) == o.value && o != None)
			{
				flags.add(o);
			}
		}

		if (flags.isEmpty())
		{
			return new eLeapDeviceCaps[] { None };
		}
		else
		{
			return flags.toArray(new eLeapDeviceCaps[flags.size()]);
		}
	}


	public static int createMask(eLeapDeviceCaps... flags)
	{
		int mask = 0;
		for (eLeapDeviceCaps flag : flags)
		{
			mask |= flag.value;
		}

		return mask;
	}
}