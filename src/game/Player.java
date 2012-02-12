package game;

import org.lwjgl.input.Keyboard;

public class Player {
	private World world;
	
	public float x,y,z;
	private float xspd,ypsd,zspd;

	public Player() {
		x = 10;
		y = 10;
		z = 40;
	}
	
	public void update(Input input) {
		if (input.isKeyPressed("Jump")) {
			//Jump!
		}
		
	}
	
	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
	
}
