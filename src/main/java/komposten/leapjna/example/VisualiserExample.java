/*
 * Copyright 2020-2022 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.example;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import komposten.leapjna.example.VisualiserBackend.State;
import komposten.leapjna.leapc.events.LEAP_IMAGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_TRACKING_EVENT;


class VisualiserExample
{
	private JFrame window;
	private VisualiserBackend backend;
	private RenderPanel renderPanel;
	private LogPanel logPanel;
	private Thread leapJnaThread;


	private boolean imagesEnabled;

	public VisualiserExample()
	{
		backend = new VisualiserBackend(visualiserListener);

		buildUi();
		window.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER && leapJnaThread == null)
				{
					leapJnaThread = new Thread(backend::start, "LeapJna Thread");
					leapJnaThread.start();
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					terminate();
				}
				else if (e.getKeyCode() == KeyEvent.VK_P)
				{
					backend.requestPause();
				}
				else if (e.getKeyCode() == KeyEvent.VK_R)
				{
					backend.requestRecording();
				}
				else if (e.getKeyCode() == KeyEvent.VK_S)
				{
					backend.requestRecordingStatus();
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

		window.addWindowListener(new WindowAdapter()
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
		window = new JFrame("LeapJna - 2D visualiser");
		window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		window.setSize(500, 500);
		window.setLocationRelativeTo(null);

		renderPanel = new RenderPanel();
		logPanel = new LogPanel();

		JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		pane.setTopComponent(renderPanel);
		pane.setBottomComponent(logPanel);

		window.setContentPane(pane);
		window.setVisible(true);

		pane.setDividerLocation(0.75);
		pane.setResizeWeight(0.75);
	}


	public void toggleImages()
	{
		if (backend.areImagesAllowed())
		{
			imagesEnabled = !imagesEnabled;
			renderPanel.setDrawImages(imagesEnabled);
		}
		else
		{
			logPanel.pushError("Images are not enabled in the Ultraleap Tracking control panel.");
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

		JOptionPane.showMessageDialog(window, message, title, JOptionPane.INFORMATION_MESSAGE);
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
		window.setVisible(false);
		window.dispose();
	}


	private VisualiserListener visualiserListener = new VisualiserListener()
	{
		@Override
		public void onStateChanged(State state)
		{
			switch (state)
			{
				case CONNECTING :
					onLogMessage(LogType.HEADER, "Connecting to the Ultraleap Tracking Service");
					renderPanel.setState(state);
					break;
				case POLLING :
					onLogMessage(LogType.HEADER, "Polling connection");
					renderPanel.setState(state);
					break;
				case CLOSED :
					closeWindow();
					leapJnaThread = null;
					break;
				case ERROR :
					leapJnaThread = null;
					break;
				default :
					break;
			}
		}


		@Override
		public void onLogMessage(LogType type, String message, Object... args)
		{
			switch (type)
			{
				case ERROR :
					logPanel.pushError(message, args);
					break;
				case HEADER :
					logPanel.pushLog(message, true, Color.BLACK, args);
					break;
				case SEPARATOR :
					logPanel.pushSeparator();
					break;
				default :
					logPanel.pushLog(message, args);
					break;
			}
		}


		@Override
		public void onFrame(LEAP_TRACKING_EVENT frameEvent)
		{
			renderPanel.setFrameData(frameEvent);
		}


		@Override
		public void onImage(LEAP_IMAGE_EVENT imageEvent)
		{
			renderPanel.setImageData(imageEvent);
		}
	};


	public static void main(String[] args)
	{
		new VisualiserExample();
	}
}
