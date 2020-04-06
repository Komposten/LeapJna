package komposten.leapjna.leapc.util;

public class LeapException extends RuntimeException
{
	public LeapException()
	{}


	public LeapException(String message)
	{
		super(message);
	}


	public LeapException(Throwable cause)
	{
		super(cause);
	}


	public LeapException(String message, Throwable cause)
	{
		super(message, cause);
	}


	public LeapException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
