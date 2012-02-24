package game.world.block;

import java.util.HashMap;

import game.util.HashmapLoader;
import game.util.ListLoader;

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

	public Color getColor() {
		return color;
	}

	/*public static Block[] blocks = {
		null,
		new Block(60,120,20)
	};//*/
	public static Block[] blocks = ListLoader.loadBlocks();

	public static Block loadBlock(String name) {
		Block block = new Block();
		HashMap<String,Integer> hm = HashmapLoader.readHashmap("block/"+name);
		block.setColor(hm.containsKey("colorR") ? hm.get("colorR") : 0,
				hm.containsKey("colorG") ? hm.get("colorG") : 0,
				hm.containsKey("colorB") ? hm.get("colorB") : 0);
		return block;
	}
}
