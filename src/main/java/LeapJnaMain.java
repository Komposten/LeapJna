

import com.sun.jna.ptr.LongByReference;

import komposten.leapjna.LeapC;
import komposten.leapjna.LeapC.LEAP_CONNECTION_MESSAGE;
import komposten.leapjna.leapc.data.LEAP_CONNECTION;
import komposten.leapjna.leapc.data.LEAP_CONNECTION_INFO;
import komposten.leapjna.leapc.enums.eLeapEventType;
import komposten.leapjna.leapc.enums.eLeapRS;
import komposten.leapjna.leapc.events.LEAP_TRACKING_EVENT;

public class LeapJnaMain
{
	public LeapJnaMain()
	{
		printHeader("Checking clocks");
		long leapTime = LeapC.INSTANCE.LeapGetNow();
		long javaTime = System.nanoTime() / 1000;
		System.out.println(leapTime);
		System.out.println(javaTime);
		System.out.printf("Diff: %d%n", javaTime - leapTime);

		printHeader("Creating connection");
		LEAP_CONNECTION leapConnection = new LEAP_CONNECTION();
		eLeapRS result = LeapC.INSTANCE.LeapCreateConnection(null, leapConnection);
		
		printStatus(leapConnection);
		
		if (result == eLeapRS.Success)
		{
			printHeader("Opening connection");
			result = LeapC.INSTANCE.LeapOpenConnection(leapConnection.handle);
			printStatus(leapConnection);
			
			if (result == eLeapRS.Success)
			{
				printHeader("Polling connection");
				LEAP_CONNECTION_MESSAGE messageRef = new LEAP_CONNECTION_MESSAGE();
				result = LeapC.INSTANCE.LeapPollConnection(leapConnection.handle, 500,
						messageRef);
				
				for (int i = 0; i < 15; i++)
				{
					System.out.format("Message type: %s%n", messageRef.type);
					result = LeapC.INSTANCE.LeapPollConnection(leapConnection.handle, 500,
							messageRef);
				}

				System.out.println("Message type: " + messageRef.type);
				System.out.println("Message size: " + messageRef.size);
				
				printStatus(leapConnection);

				printHeader("Checking frame size");
				leapTime = LeapC.INSTANCE.LeapGetNow();
				LongByReference size = new LongByReference();
				result = LeapC.INSTANCE.LeapGetFrameSize(leapConnection.handle, leapTime, size);

				System.out.println(size.getValue());

				printHeader("Checking frame data");
				LEAP_TRACKING_EVENT.ByReference trackingEvent = new LEAP_TRACKING_EVENT.ByReference();
				result = LeapC.INSTANCE.LeapInterpolateFrame(leapConnection.handle, leapTime,
						trackingEvent, size.getValue());
				
				System.out.println("Tracking event: " + trackingEvent);
				
				while (true)
				{
					LeapC.INSTANCE.LeapPollConnection(leapConnection.handle, 500, messageRef);
					
					if (messageRef.type == eLeapEventType.Tracking.value)
					{
						LEAP_TRACKING_EVENT event = new LEAP_TRACKING_EVENT(messageRef.pEvent);
					}
//					leapTime = LeapC.INSTANCE.LeapGetNow();
//					result = LeapC.INSTANCE.LeapGetFrameSize(leapConnection.getValue(), leapTime, size);
//					result = LeapC.INSTANCE.LeapInterpolateFrame(leapConnection.getValue(), leapTime,
//							trackingEvent, size.getValue());
					
					try
					{
						Thread.sleep(100);
					}
					catch (InterruptedException e)
					{
					}
				}
			}
		}
	}


	private eLeapRS printStatus(LEAP_CONNECTION leapConnection)
	{
		eLeapRS result;
		LEAP_CONNECTION_INFO.ByReference connectionStatus;
		
		printHeader("Connection status");
		
		connectionStatus = new LEAP_CONNECTION_INFO.ByReference();
		connectionStatus.size = 1024;
		
		result = LeapC.INSTANCE.LeapGetConnectionInfo(leapConnection.handle, connectionStatus);
		System.out.println("Size: " + connectionStatus.size);
		return result;
	}


	private void printHeader(String text)
	{
		System.out.println();
		System.out.println("===" + text + "===");
		
		try
		{
			Thread.sleep(250);
		}
		catch (InterruptedException e)
		{
		}
	}


	public static void main(String[] args)
	{
		new LeapJnaMain();
	}
}
