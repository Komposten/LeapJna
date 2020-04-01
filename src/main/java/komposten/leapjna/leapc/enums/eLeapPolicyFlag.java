package komposten.leapjna.leapc.enums;

import java.util.ArrayList;
import java.util.List;


/**
 * The service policy flags.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#gaf9b8fb0f14bd75188519ab4eaedd6a47">LeapC
 *      API - eLeapDeviceStatus</a>
 */
public enum eLeapPolicyFlag
{
	/** No active policy flags. */
	None(0x00000000),

	/** The policy allowing an application to receive frames in the background. */
	BackgroundFrames(0x00000001),

	/** The policy specifying whether to automatically stream images from the device. */
	Images(0x00000002),

	/** The policy specifying whether to optimise tracking for head-mounted device. */
	OptimiseHMD(0x00000004),

	/** The policy allowing an application to pause or resume service tracking. */
	AllowPauseResume(0x00000008),

	/** The policy allowing an application to receive per-frame map points. */
	MapPoints(0x00000080);

	public final int value;

	private eLeapPolicyFlag(int value)
	{
		this.value = value;
	}


	public static eLeapPolicyFlag[] parseMask(int mask)
	{
		List<eLeapPolicyFlag> flags = new ArrayList<>();

		for (eLeapPolicyFlag o : eLeapPolicyFlag.values())
		{
			if ((mask & o.value) == o.value && o != None)
			{
				flags.add(o);
			}
		}

		if (flags.isEmpty())
		{
			return new eLeapPolicyFlag[] { None };
		}
		else
		{
			return flags.toArray(new eLeapPolicyFlag[flags.size()]);
		}
	}


	public static int createMask(eLeapPolicyFlag... flags)
	{
		int mask = 0;
		for (eLeapPolicyFlag flag : flags)
		{
			mask |= flag.value;
		}

		return mask;
	}
}
