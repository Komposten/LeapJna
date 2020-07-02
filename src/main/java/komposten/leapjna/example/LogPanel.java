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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;


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