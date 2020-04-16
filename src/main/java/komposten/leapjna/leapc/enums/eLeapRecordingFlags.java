package komposten.leapjna.leapc.enums;

import java.util.ArrayList;
import java.util.List;

import komposten.leapjna.LeapC;
import komposten.leapjna.leapc.data.LEAP_RECORDING_PARAMETERS;
import komposten.leapjna.leapc.data.LEAP_RECORDING_STATUS;
import komposten.leapjna.leapc.data.LEAP_RECORDING;


/**
 * <p>
 * Defines the recording mode provided to the
 * {@link LeapC#LeapRecordingOpen(LEAP_RECORDING, String, LEAP_RECORDING_PARAMETERS)}
 * function.
 * </p>
 * <p>
 * Also used in members of {@link LEAP_RECORDING_PARAMETERS} and
 * {@link LEAP_RECORDING_STATUS}.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___enum.html#ga7f321cfd29f8d1b74589cde5dfb3a1ed">LeapC
 *      API - eLeapRecordingFlags</a>
 */
public enum eLeapRecordingFlags
{
	Error(0x00000000), Reading(0x00000001), Writing(0x00000002), Flushing(
			0x00000004), Compressed(0x00000008);

	public final int value;

	private eLeapRecordingFlags(int value)
	{
		this.value = value;
	}


	public static eLeapRecordingFlags[] parseMask(int mask)
	{
		List<eLeapRecordingFlags> flags = new ArrayList<>();

		for (eLeapRecordingFlags o : eLeapRecordingFlags.values())
		{
			if ((mask & o.value) == o.value && o != Error)
			{
				flags.add(o);
			}
		}

		if (flags.isEmpty())
		{
			return new eLeapRecordingFlags[] { Error };
		}
		else
		{
			return flags.toArray(new eLeapRecordingFlags[flags.size()]);
		}
	}


	public static int createMask(eLeapRecordingFlags... flags)
	{
		int mask = 0;
		for (eLeapRecordingFlags flag : flags)
		{
			mask |= flag.value;
		}

		return mask;
	}
}