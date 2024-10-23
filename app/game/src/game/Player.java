package game;

import java.awt.Point;
import java.awt.Rectangle;

public class Player {
	private Point position;
	private Rectangle collisionbox, interactionbox;
	private int direction;
	private Interaction it;
	
	public Player() {
		
	}
	
	public void interact() {
		it.interact();
	}
	
	public void setInteraction(Interaction it) {
		this.it = it;
	}
}

interface Interaction {
	public void interact();
}
