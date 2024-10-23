package game;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Game extends JFrame{
	private GamePanel gamepanel;
	
	
	
	private Player player;
	private ArrayList<Room> rooms;
	
	public Game() {
		initialize();
	}
	
	public void initialize() {
		player = new Player();
		player.setInteraction(new Interaction() {

			@Override
			public void interact() {
				// TODO Auto-generated method stub
				
			}
		});
		Container c = getContentPane();
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				switch(e.getKeyCode()) {
				case KeyEvent.VK_Z:
					player.interact();
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		gamepanel = new GamePanel();
		gamepanel.setLocation(0,0);
		c.setLayout(null);
		c.add(gamepanel);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700,700);
	}
}

class GamePanel extends JPanel {
	public GamePanel() {
		setSize(100,100);
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g2d = (Graphics2D)graphics;
		
		g2d.setColor(Color.CYAN);
	}
}
