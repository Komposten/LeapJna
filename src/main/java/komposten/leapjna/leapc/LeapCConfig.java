package komposten.leapjna.leapc;

class LeapCConfig
{
	private static final String DLL_NAME = "LeapC";
	private static final String MOCK_DLL_NAME = "MockLeapC";
	
	private static boolean useMockDll;
	
	static void useMockDll(boolean useMockDll)
	{
		LeapCConfig.useMockDll = useMockDll;
	}
	
	static String getDllName()
	{
		return useMockDll ? MOCK_DLL_NAME : DLL_NAME;
	}
}
