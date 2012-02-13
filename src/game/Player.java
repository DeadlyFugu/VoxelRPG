package game;

import org.lwjgl.opengl.GL11;

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
		
		GL11.glPushMatrix();
		GL11.glTranslatef(x, z, y);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3d(-0.5, -1, 0);
		GL11.glVertex3d(0.5, -1, 0);
		GL11.glVertex3d(0.5, 0, 0);
		GL11.glVertex3d(-0.5, 0, 0);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
	
}
