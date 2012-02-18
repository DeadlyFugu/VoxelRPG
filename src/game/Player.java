package game;

import org.lwjgl.opengl.GL11;

public class Player {
	private World world;
	
	public float x,y,z,kfc;
	private float px,py,dir;
	private float xspd,ypsd,zspd;
	private float mvspd;
	private Camera cam;

	public Player() {
		x = 42;
		y = 42;
		z = 40;
		kfc = 0;
		px = x;
		py = y;
		dir = 0;
		mvspd = 0.1f;
		zspd = 0;
	}
	
	public void setCam(Camera cam) {
		this.cam = cam;
	}
	
	public void update(Input input) {
		if (input.isKeyPressed("Sprint")) {
			mvspd = 0.3f;
		} else {
			mvspd = 0.15f;
		}
		
		if (input.isKeyPressed("PlaceBlock")) {
			world.setBlockAt((int) x,(int) y,(int) z-1,(byte) 1);
		}
		
		if (input.isKeyPressed("FrontPlaceBlock")) {
			world.setBlockAt((int) (x+(1*MathEXT.cosd(kfc))),(int) (y-(1*MathEXT.sind(kfc))),(int) z,(byte) 1);
		}
		
		if (input.isKeyPressed("ForceChunkReload")) {
			Chunk c = world.getChunkAt((int) x/32,(int) y/32);
			world.activeChunks.remove(c);
			world.allChunks.remove(c);
		}
		
		if (world.placeFree(x,y,z-0.1f)) {
			zspd -= 0.01;
		}//*/
		if (input.isKeyPressed("Jump") && !world.placeFree(x, y, z-0.1f)) {
			zspd = 0.25f;
		}
		
		if (world.placeFree(x,y,z+zspd)) {
			z += zspd;
		}  else {
			zspd = 0;
		}
		/*if (input.isKeyPressed("Left")) {
			if (world.placeFree(x+0.1f, y, z)) {
				x+=0.1;
			} else if (world.placeFree(x+0.1f, y, z+1)) {
				x+=0.1;
				z+=1;
			}
		}*/
		/*if (input.isKeyPressed("Up")) {
			if (world.placeFree((float) (x+(mvspd*MathEXT.cosd(kfc))),(float) (y-(mvspd*MathEXT.sind(kfc))), z)) {
				x=(float) (x+(mvspd*MathEXT.cosd(kfc)));
				y=(float) (y-(mvspd*MathEXT.sind(kfc)));
			} else if (world.placeFree((float) (x+(mvspd*MathEXT.cosd(kfc))),(float) (y-(mvspd*MathEXT.sind(kfc))), z+1)) {
				x=(float) (x+(mvspd*MathEXT.cosd(kfc)));
				y=(float) (y-(mvspd*MathEXT.sind(kfc)));
				z+=1;
			}
		}*/
		
		px = x;
		py = y;
		
		/*if (world.placeFree((float) (x+(input.ay*mvspd*MathEXT.cosd(kfc))),(float) (y-(input.ay*mvspd*MathEXT.sind(kfc))), z)) {
			x=(float) (x+(input.ay*mvspd*MathEXT.cosd(kfc)));
			y=(float) (y-(input.ay*mvspd*MathEXT.sind(kfc)));

		} else if (world.placeFree((float) (x+(input.ay*mvspd*MathEXT.cosd(kfc))),(float) (y-(input.ay*mvspd*MathEXT.sind(kfc))), z+1)) {
			x=(float) (x+(input.ay*mvspd*MathEXT.cosd(kfc)));
			y=(float) (y-(input.ay*mvspd*MathEXT.sind(kfc)));
			z+=1;
		}*/
		if (world.placeFree((float) (x+(input.ay*mvspd*MathEXT.cosd(kfc))),(float) (y-(input.ay*mvspd*MathEXT.sind(kfc))), z)) {
			x=(float) (x+(input.ay*mvspd*MathEXT.cosd(kfc)));
			y=(float) (y-(input.ay*mvspd*MathEXT.sind(kfc)));

		} else if (world.placeFree((float) (x+(input.ay*mvspd*MathEXT.cosd(kfc))),(float) (y-(input.ay*mvspd*MathEXT.sind(kfc))), z+1)) {
			x=(float) (x+(input.ay*mvspd*MathEXT.cosd(kfc)));
			y=(float) (y-(input.ay*mvspd*MathEXT.sind(kfc)));
			z+=0.07;
		}
		if (world.placeFree((float) (x+(input.ax*mvspd*MathEXT.cosd(kfc-90))),(float) (y-(input.ax*mvspd*MathEXT.sind(kfc-90))), z)) {
			x=(float) (x+(input.ax*mvspd*MathEXT.cosd(kfc-90)));
			y=(float) (y-(input.ax*mvspd*MathEXT.sind(kfc-90)));
			cam.yaw-=input.ax*0.8;
		} else if (world.placeFree((float) (x+(input.ax*mvspd*MathEXT.cosd(kfc-90))),(float) (y-(input.ax*mvspd*MathEXT.sind(kfc-90))), z+1)) {
			x=(float) (x+(input.ax*mvspd*MathEXT.cosd(kfc-90)));
			y=(float) (y-(input.ax*mvspd*MathEXT.sind(kfc-90)));
			cam.yaw-=input.ax*0.8;
			z+=0.07;
		}
		
		if (Math.floor(px*100) != Math.floor(x*100) && Math.floor(py*100) != Math.floor(y*100)) dir = kfc-(float) MathEXT.point_direction(0,0,input.ax,input.ay)-90;
		
		GL11.glPushMatrix();
		GL11.glTranslatef(x, z, y);
		GL11.glRotatef(dir+90, 0, 1, 0);
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
