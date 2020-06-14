package komposten.leapjna.leapc;

import java.util.HashMap;
import java.util.Map;

import com.sun.jna.Library;

import komposten.leapjna.leapc.util.LeapTypeMapper;

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
	
	static Map<String, Object> getLibraryOptions()
	{
		Map<String, Object> map = new HashMap<>();
		map.put(Library.OPTION_TYPE_MAPPER, new LeapTypeMapper());
		
		return map;
	}
}
