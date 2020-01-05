package komposten.leapjna.leapc.events;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

import komposten.leapjna.leapc.data.LEAP_FRAME_HEADER;
import komposten.leapjna.leapc.data.LEAP_HAND;

@FieldOrder({ "info", "tracking_frame_id", "nHands", "pHands", "framerate" })
public class LEAP_TRACKING_EVENT extends Structure
{
	public LEAP_FRAME_HEADER info;
	public long tracking_frame_id;
	public int nHands;

	public Pointer pHands;
	public float framerate;
	
	private LEAP_HAND[] hands;
	
	public LEAP_TRACKING_EVENT()
	{
		super(ALIGN_NONE);
	}
	
	public LEAP_TRACKING_EVENT(Pointer pointer)
	{
		super(pointer, ALIGN_NONE);
		read();
	}
	
	
	public LEAP_HAND[] getHands()
	{
		return hands;
	}
	
	
	@Override
	public void read()
	{
		super.read();
		
		hands = new LEAP_HAND[nHands];
		if (nHands > 0)
		{
			for (int i = 0; i < nHands; i++)
			{
				int offset = i * LEAP_HAND.SIZE;
				hands[i] = new LEAP_HAND(pHands.share(offset));
			}
		}
	}


	public static class ByReference extends LEAP_TRACKING_EVENT
			implements Structure.ByReference
	{
	}
}