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
		if (world.placeFree(x,y,z-0.1f)) {
			z-= 0.1;
		}//*/
		if (input.isKeyPressed("Jump")) {
			//Jump!
		}
		if (input.isKeyPressed("Left")) {
			if (world.placeFree(x+0.1f, y, z)) {
				x+=0.1;
			} else if (world.placeFree(x+0.1f, y, z+1)) {
				x+=0.1;
				z+=1;
			}
		}
		if (input.isKeyPressed("Up")) {
			if (world.placeFree(x, y+0.1f, z)) {
				y+=0.1;
			}
		}
		
		GL11.glPushMatrix();
		GL11.glTranslatef(x, z, y);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3d(-0.5, 1, 0);
		GL11.glVertex3d(0.5, 1, 0);
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
