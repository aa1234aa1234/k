package MATH;

import game.Texture;

public class Block {
	private Vertex[] vertices;
	private int id = 0;
	private Texture texture;
	public Block(Vertex[] v, int id) {
		vertices = v;
		this.id = id;
	}
	public void setTexture(String path) {
		texture = new Texture(path);
	}
}
