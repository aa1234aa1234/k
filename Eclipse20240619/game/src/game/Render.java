package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Render extends JPanel {
	@Override
	public void paintComponent(Graphics graphic) {
		super.paintComponent(graphic);
		Graphics2D g2d = (Graphics2D)graphic;
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, 1280, 960);
	}
}
