package com.splinter.weatherforcast;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class Application {

	public static void main(String[] args) {
		// System.out.println(Thread.currentThread().getName());
		EventQueue.invokeLater(() -> {
			// System.out.println(Thread.currentThread().getName());
			MainFrame mainFrame = new MainFrame("WeatherForcast");
			mainFrame.setResizable(false);
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.pack();
			mainFrame.setLocationRelativeTo(null);
			mainFrame.setVisible(true);
		});

	}

}
