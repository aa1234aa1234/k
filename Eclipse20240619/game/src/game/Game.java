package game;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Game extends JFrame {
	private static Game instance;
	private Thread mainthread;
	private Render renderPanel = new Render();
	private JLabel fps = new JLabel();
	public Game() {
		initialize();
		setdata();
		instance = this;
	}
	private void initialize() {
		setSize(new Dimension(1280,960));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("game");
		setVisible(true);
		fps.setForeground(Color.WHITE);
		Container c = getContentPane();
		c.add(renderPanel);
		renderPanel.add(fps);
	}
	private void setdata() {
		mainthread = new Thread(new Runnable() {
			@Override
			public void run() {
				double currentTime = System.nanoTime();
				double previousTime = 0, deltaTime = 0;
				double f = 1, pf = 0;
				try {
					while(true) {
						currentTime = System.nanoTime();
						deltaTime = (currentTime-previousTime)/1e9;
						previousTime = currentTime;
						pf += deltaTime;
						f++;
						//f *= 1.0/deltaTime;
						renderPanel.repaint();
						if(pf >= 1.0) { fps.setText(String.valueOf(f/(deltaTime*1e9))); f = 0; pf=0; }
						
						
					}
					
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		mainthread.start();
	}
	public static Game getInstance() {
		return instance;
	}
}


