package komposten.leapjna.leapc.enums;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


public class eLeapDeviceStatusTest
{
	private eLeapDeviceStatus base = eLeapDeviceStatus.None;

	@Test
	void parseMask_unknownFailure_returnUnknownFailure()
	{
		eLeapDeviceStatus[] status = base.parseMask(eLeapDeviceStatus.UnknownFailure.value);

		assertThat(status).containsExactlyInAnyOrder(eLeapDeviceStatus.UnknownFailure);
	}


	@Test
	void parseMask_validFailureMask_returnComponents()
	{
		int mask = Enums.createMask(eLeapDeviceStatus.BadControl,
				eLeapDeviceStatus.BadFirmware);
		eLeapDeviceStatus[] status = base.parseMask(mask);

		assertThat(status).containsExactlyInAnyOrder(eLeapDeviceStatus.BadControl,
				eLeapDeviceStatus.BadFirmware);
	}


	@Test
	void parseMask_invalidFailureMask_returnOnlyMatchingFlags()
	{
		int mask = eLeapDeviceStatus.BadControl.value | 0x100;
		eLeapDeviceStatus[] status = base.parseMask(mask);

		assertThat(status).containsExactlyInAnyOrder(eLeapDeviceStatus.BadControl);
	}

	@Test
	void parseMask_none_returnNone()
	{
		eLeapDeviceStatus[] status = base.parseMask(eLeapDeviceStatus.None.value);

		assertThat(status).containsExactlyInAnyOrder(eLeapDeviceStatus.None);
	}


	@Test
	void parseMask_validFunctionalMask_returnComponents()
	{
		int mask = Enums.createMask(eLeapDeviceStatus.Robust,
				eLeapDeviceStatus.Smudged);
		eLeapDeviceStatus[] status = base.parseMask(mask);

		assertThat(status).containsExactlyInAnyOrder(eLeapDeviceStatus.Robust,
				eLeapDeviceStatus.Smudged);
	}


	@Test
	void parseMask_invalidFunctionalMask_returnOnlyMatchingFlags()
	{
		int mask = eLeapDeviceStatus.Smudged.value | 0x100;
		eLeapDeviceStatus[] status = base.parseMask(mask);

		assertThat(status).containsExactlyInAnyOrder(eLeapDeviceStatus.Smudged);
	}
}
