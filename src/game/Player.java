package game;

import org.lwjgl.opengl.GL11;

public class Player {
	private World world;
	
	public float x,y,z,kfc;
	private float xspd,ypsd,zspd;
	private float mvspd;

	public Player() {
		x = 10;
		y = 10;
		z = 40;
		kfc = 0;
		mvspd = 0.1f;
	}
	
	public void update(Input input) {
		if (input.isKeyPressed("Sprint")) {
			mvspd = 0.2f;
		} else {
			mvspd = 0.1f;
		}
		if (world.placeFree(x,y,z-0.1f)) {
			z-= 0.1;
		}//*/
		if (input.isKeyPressed("Jump")) {
			//Jump!
		}
		/*if (input.isKeyPressed("Left")) {
			if (world.placeFree(x+0.1f, y, z)) {
				x+=0.1;
			} else if (world.placeFree(x+0.1f, y, z+1)) {
				x+=0.1;
				z+=1;
			}
		}*/
		if (input.isKeyPressed("Up")) {
			if (world.placeFree((float) (x+(mvspd*MathEXT.cosd(kfc))),(float) (y-(mvspd*MathEXT.sind(kfc))), z)) {
				x=(float) (x+(mvspd*MathEXT.cosd(kfc)));
				y=(float) (y-(mvspd*MathEXT.sind(kfc)));
			} else if (world.placeFree((float) (x+(mvspd*MathEXT.cosd(kfc))),(float) (y-(mvspd*MathEXT.sind(kfc))), z+1)) {
				x=(float) (x+(mvspd*MathEXT.cosd(kfc)));
				y=(float) (y-(mvspd*MathEXT.sind(kfc)));
				z+=1;
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
