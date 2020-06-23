
/*
 * Copyright 2020 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.sun.jna.ptr.LongByReference;

import komposten.leapjna.leapc.LeapC;
import komposten.leapjna.leapc.data.LEAP_CONNECTION;
import komposten.leapjna.leapc.data.LEAP_CONNECTION_INFO;
import komposten.leapjna.leapc.data.LEAP_CONNECTION_MESSAGE;
import komposten.leapjna.leapc.data.LEAP_DEVICE;
import komposten.leapjna.leapc.data.LEAP_DEVICE_INFO;
import komposten.leapjna.leapc.data.LEAP_DIGIT;
import komposten.leapjna.leapc.data.LEAP_HAND;
import komposten.leapjna.leapc.data.LEAP_IMAGE;
import komposten.leapjna.leapc.data.LEAP_RECORDING;
import komposten.leapjna.leapc.data.LEAP_RECORDING_PARAMETERS;
import komposten.leapjna.leapc.data.LEAP_RECORDING_STATUS;
import komposten.leapjna.leapc.data.LEAP_VECTOR;
import komposten.leapjna.leapc.enums.eLeapEventType;
import komposten.leapjna.leapc.enums.eLeapImageFormat;
import komposten.leapjna.leapc.enums.eLeapPolicyFlag;
import komposten.leapjna.leapc.enums.eLeapRS;
import komposten.leapjna.leapc.enums.eLeapRecordingFlags;
import komposten.leapjna.leapc.events.LEAP_CONFIG_RESPONSE_EVENT;
import komposten.leapjna.leapc.events.LEAP_CONNECTION_EVENT;
import komposten.leapjna.leapc.events.LEAP_DEVICE_EVENT;
import komposten.leapjna.leapc.events.LEAP_DEVICE_FAILURE_EVENT;
import komposten.leapjna.leapc.events.LEAP_DEVICE_STATUS_CHANGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_IMAGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_LOG_EVENT;
import komposten.leapjna.leapc.events.LEAP_LOG_EVENTS;
import komposten.leapjna.leapc.events.LEAP_POLICY_EVENT;
import komposten.leapjna.leapc.events.LEAP_TRACKING_EVENT;
import komposten.leapjna.util.Configurations;


public class LeapTestGui extends JFrame
{
	private static final int FRAME_RATE = 60;
	private static final float FRAME_TIME = 1000f / FRAME_RATE;
	private RenderPanel renderPanel;
	private LogPanel logPanel;
	private Thread leapJnaThread;

	private LEAP_CONNECTION leapConnection;
	private LEAP_RECORDING recording;

	LongByReference imagesRequestId = new LongByReference();

	private boolean imagesAllowed;
	private boolean imagesEnabled;

	private boolean isPaused;

	public LeapTestGui()
	{
		buildUi();
		addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER && leapJnaThread == null)
				{
					leapJnaThread = new Thread(LeapTestGui.this::startLoop, "LeapJna Thread");
					leapJnaThread.start();
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					terminate();
				}
				else if (e.getKeyCode() == KeyEvent.VK_P)
				{
					requestPause();
				}
				else if (e.getKeyCode() == KeyEvent.VK_R)
				{
					requestRecording();
				}
				else if (e.getKeyCode() == KeyEvent.VK_S)
				{
					requestRecordingStatus();
				}
				else if (e.getKeyCode() == KeyEvent.VK_I)
				{
					toggleImages();
				}
				else if (e.getKeyCode() == KeyEvent.VK_F1)
				{
					displayHelp();
				}
			}
		});

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				terminate();
			}
		});
	}


	private void buildUi()
	{
		setTitle("LeapJna - 2D visualiser");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(500, 500);
		setLocationRelativeTo(null);

		renderPanel = new RenderPanel();
		logPanel = new LogPanel();

		JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		pane.setTopComponent(renderPanel);
		pane.setBottomComponent(logPanel);

		setContentPane(pane);

		setVisible(true);

		pane.setDividerLocation(0.75);
		pane.setResizeWeight(0.75);
	}


	private void startLoop()
	{
		printHeader("Creating connection");
		renderPanel.setStage(RenderPanel.Stage.Connecting);
		leapConnection = new LEAP_CONNECTION();
		eLeapRS result = LeapC.INSTANCE.LeapCreateConnection(null, leapConnection);

		if (result == eLeapRS.Success)
		{
			logPanel.pushSeparator();
			printHeader("Opening connection");
			result = LeapC.INSTANCE.LeapOpenConnection(leapConnection.handle);
			printStatus(leapConnection);

			if (result == eLeapRS.Success)
			{
				logPanel.pushSeparator();
				renderPanel.setStage(RenderPanel.Stage.Running);
				printHeader("Polling connection");
				logPanel.pushSeparator();

				doPollLoop(leapConnection);
			}
			else
			{
				logPanel.pushError("Failed to open a connection to the Leap Motion service: %s",
						result);
			}
		}
		else
		{
			logPanel.pushError("Failed to create a connection to the Leap Motion service: %s",
					result);
		}

		leapJnaThread = null;
	}


	private void doPollLoop(LEAP_CONNECTION leapConnection)
	{
		double trackingTimer = 0;
		double imageTimer = 0;
		long lastTime = System.nanoTime();
		double frameTimer = 0;
		int framerate = 0;

		boolean firstIteration = true;

		while (true)
		{
			LEAP_CONNECTION_MESSAGE message = new LEAP_CONNECTION_MESSAGE();
			eLeapRS result = LeapC.INSTANCE.LeapPollConnection(leapConnection.handle, 30,
					message);

			if (firstIteration)
			{
				// Enable the images and pause policies
				LeapC.INSTANCE.LeapSetPolicyFlags(leapConnection.handle, eLeapPolicyFlag
						.createMask(eLeapPolicyFlag.Images, eLeapPolicyFlag.AllowPauseResume), 0);

				firstIteration = false;
			}

			if (result != eLeapRS.Success)
			{
				logPanel.pushError("Polling failed with result %s for event type %s", result,
						message.getType());
				logPanel.pushSeparator();
			}

			long currentTime = System.nanoTime();
			double deltaTime = (currentTime - lastTime) / 1E6;
			trackingTimer += deltaTime;
			imageTimer += deltaTime;
			frameTimer += deltaTime;
			lastTime = currentTime;

			boolean handled = handleInformationalEvents(message, leapConnection);

			if (!handled)
			{
				if (trackingTimer > FRAME_TIME && message.type == eLeapEventType.Tracking.value)
				{
					LEAP_TRACKING_EVENT trackingEvent = message.getTrackingEvent();

					if (recording != null)
					{
						result = LeapC.INSTANCE.LeapRecordingWrite(recording.handle, trackingEvent,
								null);
					}

					renderPanel.setFrameData(trackingEvent);

					trackingTimer = 0;
					framerate++;
				}

				if (imageTimer > FRAME_TIME && message.type == eLeapEventType.Image.value)
				{
					renderPanel.setImageData(message.getImageEvent());

					imageTimer = 0;
				}
			}

			if (frameTimer > 1000)
			{
				frameTimer -= 1000;
				renderPanel.setFramerate(framerate);
				framerate = 0;
			}


			if (Thread.interrupted())
			{
				break;
			}
		}

		logPanel.pushLog("Closing connection!");
		logPanel.pushSeparator();
		LeapC.INSTANCE.LeapCloseConnection(leapConnection.handle);
		printStatus(leapConnection);
		LeapC.INSTANCE.LeapDestroyConnection(leapConnection.handle);
		closeWindow();
	}


	private boolean handleInformationalEvents(LEAP_CONNECTION_MESSAGE message,
			LEAP_CONNECTION leapConnection)
	{
		boolean handled = true;

		switch (message.getType())
		{
			case Connection :
				handleConnectionEvent(message.getConnectionEvent());
				break;
			case ConnectionLost :
				handleConnectionLostEvent();
				break;
			case Device :
				handleDeviceEvent(message.getDeviceEvent());
				break;
			case DeviceStatusChange :
				handleDeviceStatusChangeEvent(message.getDeviceStatusChangeEvent());
				break;
			case DeviceLost :
				handleDeviceLostEvent(message.getDeviceLostEvent());
				break;
			case DeviceFailure :
				handleDeviceFailureEvent(message.getDeviceFailureEvent());
				break;
			case Policy :
				handlePolicyEvent(message.getPolicyEvent(), leapConnection);
				break;
			case ConfigResponse :
				handleConfigResponseEvent(message);
				break;
			case LogEvent :
				handleLogEvent(message.getLogEvent());
				break;
			case LogEvents :
				handleLogEvents(message.getLogEvents());
				break;
			default :
				handled = false;
				break;
		}

		return handled;
	}


	private void handleConnectionEvent(LEAP_CONNECTION_EVENT event)
	{
		logPanel.pushLog("Connection flags: " + event.getFlags());
		logPanel.pushSeparator();
	}


	private void handleConnectionLostEvent()
	{
		logPanel.pushLog("Connection lost!");
		logPanel.pushSeparator();
	}


	private void handleDeviceEvent(LEAP_DEVICE_EVENT event)
	{
		eLeapRS result;

		// Print device info.
		printHeader("Device detected");
		logPanel.pushLog("  Id: %d", event.device.id);

		// Open the device to retrieve information about it.
		LEAP_DEVICE phDevice = new LEAP_DEVICE();
		result = LeapC.INSTANCE.LeapOpenDevice(event.device, phDevice);

		if (result == eLeapRS.Success)
		{
			// Read the length of the device's serial string
			LEAP_DEVICE_INFO info = new LEAP_DEVICE_INFO();
			result = LeapC.INSTANCE.LeapGetDeviceInfo(phDevice.handle, info);

			if (result == eLeapRS.InsufficientBuffer || result == eLeapRS.Success)
			{
				// Allocate space for the serial and read device info
				info.allocateSerialBuffer(info.serial_length);
				result = LeapC.INSTANCE.LeapGetDeviceInfo(phDevice.handle, info);

				if (result == eLeapRS.Success)
				{
					logPanel.pushLog("  Status: %s", Arrays.toString(info.getStatus()));
					logPanel.pushLog("  Baseline: %d \u00b5m", info.baseline);
					logPanel.pushLog("  FoV: %.02f\u00b0 x %.02f\u00b0 (HxV)",
							Math.toDegrees(info.h_fov), Math.toDegrees(info.v_fov));
					logPanel.pushLog("  Range: %d \u00b5m", info.range);
					logPanel.pushLog("  Serial: %s", info.serial);
					logPanel.pushLog("  Product ID: %s (%d)",
							LeapC.INSTANCE.LeapDevicePIDToString(info.pid), info.pid);
					logPanel.pushLog("  Capabilities: %s", Arrays.toString(info.getCapabilities()));

					// Close the device since we no longer need it.
					LeapC.INSTANCE.LeapCloseDevice(phDevice.handle);
				}
				else
				{
					logPanel.pushError("Failed to read device info: " + result);
				}
			}
			else
			{
				logPanel.pushError("Failed to read device info to get serial length: " + result);
			}
		}
		else
		{
			logPanel.pushError("Failed to open device: " + result);
		}

		logPanel.pushSeparator();
	}


	private void handleDeviceStatusChangeEvent(LEAP_DEVICE_STATUS_CHANGE_EVENT event)
	{
		logPanel.pushLog("Device status changed: %d | From %s to %s", event.device.id,
				Arrays.toString(event.getLastStatus()), Arrays.toString(event.getStatus()));
		logPanel.pushSeparator();
	}


	private void handlePolicyEvent(LEAP_POLICY_EVENT event, LEAP_CONNECTION leapConnection)
	{
		logPanel.pushLog("Active policies: " + Arrays.toString(event.getCurrentPolicy()));
		logPanel.pushSeparator();

		// Request the current images_mode setting to check if images were activated
		// through the Leap Motion control panel.
		LeapC.INSTANCE.LeapRequestConfigValue(leapConnection.handle,
				Configurations.Tracking.IMAGES_MODE, imagesRequestId);
	}


	private void handleLogEvent(LEAP_LOG_EVENT event)
	{
		logPanel.pushLog("[LOG] " + event.getSeverity() + ": " + event.message);
		logPanel.pushSeparator();
	}


	private void handleLogEvents(LEAP_LOG_EVENTS events)
	{
		for (LEAP_LOG_EVENT logEvent : events.getEvents())
		{
			handleLogEvent(logEvent);
		}
	}


	private void handleDeviceFailureEvent(LEAP_DEVICE_FAILURE_EVENT event)
	{
		logPanel.pushLog("Device failure: %s", Arrays.toString(event.getStatus()));
		logPanel.pushSeparator();
	}


	private void handleDeviceLostEvent(LEAP_DEVICE_EVENT event)
	{
		logPanel.pushLog("Device was lost:");
		logPanel.pushLog("  Id: %d", event.device.id);
		logPanel.pushLog("  Status: %d", Arrays.toString(event.getStatus()));
		logPanel.pushSeparator();
	}


	private void handleConfigResponseEvent(LEAP_CONNECTION_MESSAGE message)
	{
		LEAP_CONFIG_RESPONSE_EVENT responseEvent = message.getConfigResponseEvent();

		if (responseEvent.requestID == imagesRequestId.getValue())
		{
			imagesAllowed = responseEvent.value.getInt() == 2;
		}
	}


	private eLeapRS printStatus(LEAP_CONNECTION leapConnection)
	{
		LEAP_CONNECTION_INFO connectionStatus = new LEAP_CONNECTION_INFO();
		eLeapRS result = LeapC.INSTANCE.LeapGetConnectionInfo(leapConnection.handle,
				connectionStatus);

		if (result == eLeapRS.Success)
		{
			logPanel.pushLog("Status: %s", connectionStatus.getStatus());
		}
		else
		{
			logPanel.pushError("Failed to get status: %s", result);
		}
		return result;
	}


	private void printHeader(String text)
	{
		logPanel.pushLog(text, true, Color.BLACK, new Object[0]);
	}


	private void requestPause()
	{
		eLeapRS result = LeapC.INSTANCE.LeapSetPause(leapConnection.handle, isPaused ? 0 : 1);

		if (result == eLeapRS.Success)
		{
			isPaused = !isPaused;
			logPanel.pushLog(isPaused ? "Paused" : "Resumed");
		}
		else
		{
			logPanel.pushError("Failed to " + (isPaused ? "resume" : "pause") + ": " + result);
		}

		logPanel.pushSeparator();
	}


	private void requestRecording()
	{
		if (recording == null)
		{
			recording = new LEAP_RECORDING();
			LEAP_RECORDING_PARAMETERS params = new LEAP_RECORDING_PARAMETERS(
					eLeapRecordingFlags.Writing);
			String filePath = "recording_" + System.currentTimeMillis() + ".lmt";

			eLeapRS result = LeapC.INSTANCE.LeapRecordingOpen(recording, filePath, params);

			if (result != eLeapRS.Success)
			{
				recording = null;
				logPanel.pushError("Failed to start recording: " + result);
			}
			else
			{
				logPanel.pushLog("Recording to " + filePath);
			}
		}
		else
		{
			eLeapRS result = LeapC.INSTANCE.LeapRecordingClose(recording);

			if (result != eLeapRS.Success)
			{
				logPanel.pushError("Failed to close recording: " + result);
			}
			else
			{
				logPanel.pushLog("Stopped recording!");
				recording = null;
			}
		}

		logPanel.pushSeparator();
	}


	private void requestRecordingStatus()
	{
		if (recording != null)
		{
			LEAP_RECORDING_STATUS pStatus = new LEAP_RECORDING_STATUS();
			eLeapRS result = LeapC.INSTANCE.LeapRecordingGetStatus(recording.handle, pStatus);

			if (result != eLeapRS.Success)
			{
				logPanel.pushError("Failed to get recording status: " + result);
			}
			else
			{
				logPanel.pushLog(
						"Status of current recording: " + Arrays.toString(pStatus.getMode()));
			}
		}
		else
		{
			logPanel.pushLog("Status of current recording: Not recording");
		}

		logPanel.pushSeparator();
	}


	private void toggleImages()
	{
		if (imagesAllowed)
		{
			imagesEnabled = !imagesEnabled;
			renderPanel.setDrawImages(imagesEnabled);
		}
		else
		{
			logPanel.pushError("Images are not enabled in the Leap Motion control panel.");
			logPanel.pushSeparator();
		}
	}


	private void displayHelp()
	{
		String title = "LeapJna - 2D visualiser Help";
		String message = "Available keyboard commands:" + "\nEscape: Close the program"
				+ "\nP: Pause tracking (must be resumed from the control panel)"
				+ "\nR: Start recording tracking data" + "\nS: Print status of current recording"
				+ "\nI: Toggle drawing of the camera images";

		JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
	}


	private void terminate()
	{
		if (leapJnaThread != null)
		{
			leapJnaThread.interrupt();
		}
		else
		{
			closeWindow();
		}
	}


	private void closeWindow()
	{
		setVisible(false);
		dispose();
	}


	public static void main(String[] args)
	{
		new LeapTestGui();
	}
}



class RenderPanel extends JPanel
{
	public enum Stage
	{
		Startup, Connecting, Running;
	}

	private Stage stage = Stage.Startup;
	private LEAP_TRACKING_EVENT data;
	private BufferedImage textureLeft;
	private BufferedImage textureRight;
	private int framerate;
	private boolean drawImages;

	public void setStage(Stage stage)
	{
		this.stage = stage;
		repaint();
	}


	public void setDrawImages(boolean drawImages)
	{
		this.drawImages = drawImages;
	}


	public void setFrameData(LEAP_TRACKING_EVENT data)
	{
		this.data = data;
		repaint();
	}


	public void setImageData(LEAP_IMAGE_EVENT data)
	{
		textureRight = createTexture(data.image[0], textureRight);
		textureLeft = createTexture(data.image[1], textureLeft);
		SwingUtilities.invokeLater(this::repaint);
	}


	@SuppressWarnings("null")
	private BufferedImage createTexture(LEAP_IMAGE image, BufferedImage texture)
	{
		int width = image.properties.width;
		int height = image.properties.height;

		boolean newTexture = (texture == null || texture.getWidth() != width
				|| texture.getHeight() != height);

		byte[] imageData = image.getData();

		if (image.properties.getFormat() == eLeapImageFormat.IR)
		{
			if (newTexture)
			{
				texture = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
			}

			byte[] textureData = ((DataBufferByte) texture.getRaster().getDataBuffer())
					.getData();
			System.arraycopy(imageData, 0, textureData, 0, imageData.length);
		}

		return texture;
	}


	public void setFramerate(int framerate)
	{
		this.framerate = framerate;
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

		if (drawImages)
		{
			drawImages(g2d);
		}

		g2d.setColor(Color.BLACK);
		g2d.drawString("Press F1 for help", 10, getHeight() - 30);
		g2d.drawString("Press ESC to exit", 10, getHeight() - 10);

		if (stage == Stage.Startup)
		{
			g2d.setColor(Color.BLACK);
			g2d.drawString("Press ENTER to start tracking", 10, 15);
		}
		else if (stage == Stage.Connecting)
		{
			g2d.setColor(Color.BLACK);
			g2d.drawString("Connecting...", 10, 15);
		}
		else if (stage == Stage.Running)
		{
			g2d.setColor(Color.BLACK);
			g2d.drawString(String.format("Drawing FPS: %d", framerate), 10, 15);

			if (data != null)
			{
				for (int i = 0; i < data.getHands().length; i++)
				{
					drawHand(data.getHands()[i], g2d, offsetX, offsetY);
				}

				g2d.setColor(Color.BLACK);
				drawTrackingInfo(g2d);
			}
		}
	}


	private void drawImages(Graphics2D g2d)
	{
		float ratio;

		if (textureLeft != null)
		{
			ratio = textureLeft.getWidth() / (float) textureLeft.getHeight();
		}
		else if (textureRight != null)
		{
			ratio = textureRight.getWidth() / (float) textureRight.getHeight();
		}
		else
		{
			return;
		}

		int width = getWidth() / 2;
		int height = (int) (getHeight() / ratio);
		int y = (int) (getHeight() / 2f - height / 2f);

		if (textureLeft != null)
		{
			g2d.drawImage(textureLeft, width, y, 0, y + height, 0, 0, textureLeft.getWidth(),
					textureLeft.getHeight(), null);
		}

		if (textureRight != null)
		{
			g2d.drawImage(textureRight, width + width, y, width, y + height, 0, 0,
					textureRight.getWidth(), textureRight.getHeight(), null);
		}
	}


	private void drawHand(LEAP_HAND hand, Graphics2D g2d, int offsetX, int offsetY)
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


	private void drawFinger(LEAP_DIGIT finger, Graphics2D g2d, int offsetX, int offsetY)
	{
		g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
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
		g2d.fillRect((int) (position.x + offsetX - size / 2f),
				(int) (-position.y + offsetY - size / 2), size, size);
	}


	private void drawTrackingInfo(Graphics2D g2d)
	{
		g2d.drawString(String.format("Tracking FPS: %.02f", data.framerate), 10, 30);

		float y = 60;
		for (int i = 0; i < data.nHands; i++)
		{
			LEAP_HAND hand = data.getHands()[i];

			float roll = hand.palm.orientation.getRoll();
			float pitch = hand.palm.orientation.getPitch();
			float yaw = hand.palm.orientation.getYaw();
			float lineHeight = 20;

			g2d.drawString(String.format("Hand %d: %s", i, hand.getType()), 10, y);
			y += lineHeight;
			g2d.drawString(String.format("Roll (x): %.02f", Math.toDegrees(roll)), 10, y);
			y += lineHeight;
			g2d.drawString(String.format("Pitch (z): %.02f", Math.toDegrees(pitch)), 10, y);
			y += lineHeight;
			g2d.drawString(String.format("Yaw (y): %.02f", Math.toDegrees(yaw)), 10, y);
			y += lineHeight * 2;
		}
	}
}



class LogPanel extends JPanel
{
	private JPanel logView;
	private JScrollPane scrollPane;

	public LogPanel()
	{
		super(new BorderLayout());
		logView = createLogView();
		scrollPane = new JScrollPane(logView);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		add(scrollPane, BorderLayout.CENTER);
	}


	private JPanel createLogView()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		return panel;
	}


	public void pushLog(String log, Object... args)
	{
		pushLog(log, false, Color.BLACK, args);
	}


	public void pushError(String error, Object... args)
	{
		pushLog(error, true, Color.RED, args);
	}


	public void pushLog(String log, boolean bold, Color color, Object... args)
	{
		JLabel label = new JLabel(String.format(log, args));
		label.setBorder(new EmptyBorder(2, 6, 2, 6));
		label.setForeground(color);

		if (bold)
		{
			label.setFont(label.getFont().deriveFont(Font.BOLD));
		}
		else
		{
			label.setFont(label.getFont().deriveFont(Font.PLAIN));
		}

		logView.add(label);
		logView.revalidate();
		SwingUtilities.invokeLater(() -> {
			JScrollBar scrollbar = scrollPane.getVerticalScrollBar();
			scrollbar.setValue(scrollbar.getMaximum());
		});
	}


	public void pushSeparator()
	{
		JSeparator separator = new JSeparator();
		logView.add(separator);
	}
}
