/*
 * Copyright 2020 Jakob Hjelm (Komposten)
 *
 * This file is part of LeapJna.
 *
 * LeapJna is a free Java library: you can use, redistribute it and/or modify
 * it under the terms of the MIT license as written in the LICENSE file in the root
 * of this project.
 */
package komposten.leapjna.example;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import komposten.leapjna.example.VisualiserBackend.State;
import komposten.leapjna.leapc.data.LEAP_DIGIT;
import komposten.leapjna.leapc.data.LEAP_HAND;
import komposten.leapjna.leapc.data.LEAP_IMAGE;
import komposten.leapjna.leapc.data.LEAP_VECTOR;
import komposten.leapjna.leapc.enums.eLeapImageFormat;
import komposten.leapjna.leapc.events.LEAP_IMAGE_EVENT;
import komposten.leapjna.leapc.events.LEAP_TRACKING_EVENT;


class RenderPanel extends JPanel
{
	private State state = State.INITIAL;
	private LEAP_TRACKING_EVENT data;
	private BufferedImage textureLeft;
	private BufferedImage textureRight;

	private long lastFrameUpdate;
	private int frameCount;
	private int framerate;

	private boolean drawImages;

	public void setState(State state)
	{
		this.state = state;
		repaint();
	}


	public void setDrawImages(boolean drawImages)
	{
		this.drawImages = drawImages;
	}


	public void setFrameData(LEAP_TRACKING_EVENT data)
	{
		this.data = data;

		updateFramerate();
		repaint();
	}


	private void updateFramerate()
	{
		frameCount++;

		if (System.currentTimeMillis() - lastFrameUpdate > 1000)
		{
			framerate = frameCount;
			frameCount = 0;
			lastFrameUpdate = System.currentTimeMillis();
		}
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

		if (state == State.INITIAL)
		{
			g2d.setColor(Color.BLACK);
			g2d.drawString("Press ENTER to start tracking", 10, 15);
		}
		else if (state == State.CONNECTING)
		{
			g2d.setColor(Color.BLACK);
			g2d.drawString("Connecting...", 10, 15);
		}
		else if (state == State.POLLING)
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
				(int) (-position.y + offsetY - size / 2d), size, size);
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
			g2d.drawString(String.format("Roll (z): %.02f", Math.toDegrees(roll)), 10, y);
			y += lineHeight;
			g2d.drawString(String.format("Pitch (x): %.02f", Math.toDegrees(pitch)), 10, y);
			y += lineHeight;
			g2d.drawString(String.format("Yaw (y): %.02f", Math.toDegrees(yaw)), 10, y);
			y += lineHeight * 2;
		}
	}
}