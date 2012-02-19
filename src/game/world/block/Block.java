package game.world.block;

import org.lwjgl.util.Color;

public class Block {
	private Color color;
	
	private Block() {
		color = new Color(0,0,0);
	}
	
	private Block(int r, int g, int b) {
		color = new Color(r,g,b);
	}
	
	public Block setColor(int r, int g, int b) {
		this.color.set(r,g,b);
		return this;
	}
	
	public Color getColor(int r, int g, int b) {
		return color;
	}
	
	public static Block[] blocks = {
		null,
		new Block(60,120,20)
	};
}
