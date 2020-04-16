package komposten.leapjna.leapc.data;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.LeapC;
import komposten.leapjna.leapc.enums.eLeapDeviceStatus;
import komposten.leapjna.leapc.enums.eLeapRecordingFlags;


/**
 * <p>
 * Information about a current {@link LEAP_RECORDING}.
 * </p>
 * <p>
 * Filled in by a call to
 * {@link LeapC#LeapRecordingGetStatus(Pointer, LEAP_RECORDING_STATUS)}.
 * </p>
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/group___structs.html#struct_l_e_a_p___r_e_c_o_r_d_i_n_g___s_t_a_t_u_s">LeapC
 *      API - LEAP_RECORDING_STATUS</a>
 */
@FieldOrder({ "mode" })
public class LEAP_RECORDING_STATUS extends Structure
{
	/**
	 * <p>
	 * Some combination of eLeapRecordingFlags indicating the status of the recording.
	 * </p>
	 * <p>
	 * Use {@link #getMode()} to get the mode as an array of {@link eLeapRecordingFlags}
	 * values.
	 * </p>
	 */
	public int mode;

	private eLeapRecordingFlags[] modeE;

	/**
	 * @return The mode flags as an {@link eLeapDeviceStatus} array instead of an
	 *         <code>int</code>.
	 */
	public eLeapRecordingFlags[] getMode()
	{
		if (modeE == null)
		{
			modeE = eLeapRecordingFlags.parseMask(mode);
		}

		return modeE;
	}
}
