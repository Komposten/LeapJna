import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.jna.ptr.LongByReference;

import komposten.leapjna.LeapC;
import komposten.leapjna.LeapC.LEAP_CONNECTION;
import komposten.leapjna.LeapC.LEAP_CONNECTION_INFO;
import komposten.leapjna.LeapC.LEAP_CONNECTION_MESSAGE;
import komposten.leapjna.LeapC.LEAP_DIGIT;
import komposten.leapjna.LeapC.LEAP_HAND;
import komposten.leapjna.LeapC.LEAP_TRACKING_EVENT;
import komposten.leapjna.LeapC.LEAP_VECTOR;
import komposten.leapjna.leapc.eLeapEventType;
import komposten.leapjna.leapc.eLeapRS;
import komposten.utilities.logging.LogUtils;

/*FIXME Check for memory leaks!
 *   We're currently releasing the native memory from the tracking events after
 *   Passing the data to the render panel.
 *
 *   Should probably mention that this might be needed in the docs for 
 *   LEAP_CONNECTION_MESSAGE. But verify that it is needed first!
 *   
 *   Might not be enough with just data.clear(). I still see the same pattern
 *   of occasionally climbing memory over long periods of times (15+ minutes).
 *   
 */

public class LeapTestGui extends JFrame
{
	private RenderPanel renderPanel;
	private Thread leapJnaThread;

	public LeapTestGui()
	{
		LogUtils.writeToFile("log.txt");
		buildUi();
		addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					leapJnaThread = new Thread(LeapTestGui.this::startLoop,
							"LeapJna Thread");
					leapJnaThread.start();
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					if (leapJnaThread != null)
					{
						leapJnaThread.interrupt();
					}
					
					setVisible(false);
					dispose();
				}
			}
		});
	}


	private void buildUi()
	{
		setTitle("LeapJna - 2D visualiser");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 500);
		setLocationRelativeTo(null);

		renderPanel = new RenderPanel();
		setContentPane(renderPanel);

		setVisible(true);
	}


	private void startLoop()
	{
		printHeader("Creating connection");
		LEAP_CONNECTION leapConnection = new LEAP_CONNECTION();
		eLeapRS result = LeapC.INSTANCE.LeapCreateConnection(null, leapConnection);

		printStatus(leapConnection);
		if (result == eLeapRS.Success)
		{
			printHeader("Opening connection");
			result = LeapC.INSTANCE.LeapOpenConnection(leapConnection.getValue());
			printStatus(leapConnection);

			if (result == eLeapRS.Success)
			{
				printHeader("Polling connection");

//				doInterpolateLoop(leapConnection);
				 doPollLoop(leapConnection);
			}
		}
	}


	private void doInterpolateLoop(LEAP_CONNECTION leapConnection)
	{
		// Poll once to open a connection.
		LEAP_CONNECTION_MESSAGE message = new LEAP_CONNECTION_MESSAGE();
		LeapC.INSTANCE.LeapPollConnection(leapConnection.getValue(), 500, message);

		while (true)
		{
			//FIXME Try using the clock rebaser stuff from the tutorial here.
			long timestamp = LeapC.INSTANCE.LeapGetNow();
			LEAP_TRACKING_EVENT.ByReference pEvent = new LEAP_TRACKING_EVENT.ByReference();
			LongByReference pFrameSize = new LongByReference();
			LeapC.INSTANCE.LeapGetFrameSize(leapConnection.getValue(), timestamp,
					pFrameSize);
			LeapC.INSTANCE.LeapInterpolateFrame(leapConnection.getValue(), timestamp,
					pEvent, pFrameSize.getValue());

			renderPanel.setFrameData(pEvent);
			
			//TODO Release the memory used by the frame after rendering?

			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
			}
			
			if (Thread.interrupted())
			{
				break;
			}
		}
	}


	private void doPollLoop(LEAP_CONNECTION leapConnection)
	{
		double timer = 0;
		long lastTime = System.nanoTime();

		while (true)
		{
			LEAP_CONNECTION_MESSAGE message = new LEAP_CONNECTION_MESSAGE();
			LeapC.INSTANCE.LeapPollConnection(leapConnection.getValue(), 500,
					message);

			if (message.type == eLeapEventType.Tracking.value)
			{
				long currentTime = System.nanoTime();
				timer += (currentTime - lastTime) / 1E6;
				lastTime = currentTime;

				if (timer > 16)
				{
					renderPanel.setFrameData(message.getTrackingEvent());
					timer = 0;
				}
			}
			
			if (Thread.interrupted())
			{
				break;
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

		result = LeapC.INSTANCE.LeapGetConnectionInfo(leapConnection.getValue(),
				connectionStatus);
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
		new LeapTestGui();
	}
}



class RenderPanel extends JPanel
{
	private LEAP_TRACKING_EVENT data;

	void setFrameData(LEAP_TRACKING_EVENT data)
	{
		this.data = data;
		repaint();
		data.clear();
	}


	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		int offsetX = getWidth() / 2;
		int offsetY = getHeight();

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		if (data != null)
		{
			g2d.setColor(Color.BLACK);
			g2d.drawString(String.format("Tracking FPS: %.02f", data.framerate), 10,
					10);

			for (int i = 0; i < data.getHands().length; i++)
			{
				drawHand(data.getHands()[i], g2d, offsetX, offsetY);
			}
		}
	}


	private void drawHand(LEAP_HAND hand, Graphics2D g2d, int offsetX,
			int offsetY)
	{
		g2d.setColor(Color.RED);
		drawPosition(hand.palm.position, 1, g2d, offsetX, offsetY);

		g2d.setColor(Color.BLUE);
		drawFinger(hand.digits.thumb, g2d, offsetX, offsetY);
		drawFinger(hand.digits.index, g2d, offsetX, offsetY);
		drawFinger(hand.digits.middle, g2d, offsetX, offsetY);
		drawFinger(hand.digits.ring, g2d, offsetX, offsetY);
		drawFinger(hand.digits.pinky, g2d, offsetX, offsetY);
	}


	private void drawFinger(LEAP_DIGIT finger, Graphics2D g2d, int offsetX,
			int offsetY)
	{
		g2d.setStroke(
				new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2d.drawLine((int) finger.metacarpal.prev_joint.x + offsetX,
				(int) -finger.metacarpal.prev_joint.y + offsetY,
				(int) finger.metacarpal.next_joint.x + offsetX,
				(int) -finger.metacarpal.next_joint.y + offsetY);
		g2d.drawLine((int) finger.proximal.prev_joint.x + offsetX,
				(int) -finger.proximal.prev_joint.y + offsetY,
				(int) finger.proximal.next_joint.x + offsetX,
				(int) -finger.proximal.next_joint.y + offsetY);
		g2d.drawLine((int) finger.intermediate.prev_joint.x + offsetX,
				(int) -finger.intermediate.prev_joint.y + offsetY,
				(int) finger.intermediate.next_joint.x + offsetX,
				(int) -finger.intermediate.next_joint.y + offsetY);
		g2d.drawLine((int) finger.distal.prev_joint.x + offsetX,
				(int) -finger.distal.prev_joint.y + offsetY,
				(int) finger.distal.next_joint.x + offsetX,
				(int) -finger.distal.next_joint.y + offsetY);
		drawPosition(finger.distal.next_joint, 0.5f, g2d, offsetX, offsetY);
	}


	private void drawPosition(LEAP_VECTOR position, float scale, Graphics2D g2d,
			int offsetX, int offsetY)
	{
		int size = (int) ((position.z + 200) / 400 * 20 * scale + 5);
		g2d.fillRect((int) (position.x + offsetX - size/2f),
				(int) (-position.y + offsetY - size/2),
				size, size);
	}
}
