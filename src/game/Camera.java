package game;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.glu.GLU;

public class Camera {
	
	private Input input;
	private Player player;
	public float x,y,z;
	private float tx,ty,tz;
	private float lx,ly,lz;
	public float[] cp = {-10,42,-10,0,32,0,0,1,0};
	private float xsteps, ysteps, zsteps = 0;
	private float yaw,zoom;
	
	public Camera(Input input, Player player) {
		this.input = input;
		this.player = player;
		yaw = 1;
		zoom = 10;
		x = tx = (float) (player.x+(zoom*MathEXT.cosd(yaw)));
		y = ty = (float) (player.y-(zoom*MathEXT.sind(yaw)));
		z = tz = player.z+zoom/2;
	}
	
	public void updateView() {
		
		tx = (float) (player.x+(zoom*MathEXT.cosd(yaw)));
		ty = (float) (player.y-(zoom*MathEXT.sind(yaw)));
		tz = player.z+zoom/2;
		
	    x += (tx-x)/10;
	    y += (ty-y)/10;
	    z += (tz-z)/10;
	    
		yaw+=input.ax*2;
		zoom = Math.min(Math.max(zoom+input.cy/4,3),20);
		
		player.kfc = (float) MathEXT.point_direction(x, y, player.x, player.y);
		
		/*if (input.isKeyPressed("CamUp")) {
			cp[0] += 0.3;
			cp[2] += 0.3;
		}
		if (input.isKeyPressed("CamDown")) {
			cp[0] -= 0.3;
			cp[2] -= 0.3;
		}
		if (input.isKeyPressed("CamRight")) {
			cp[0] -= 0.3;
			cp[2] += 0.3;
		}
		if (input.isKeyPressed("CamLeft")) {
			cp[0] += 0.3;
			cp[2] -= 0.3;
		}
		if (input.isKeyPressed("Jump")) {
			cp[1] += 0.3;
		}
		if (input.isKeyPressed("Sprint")) {
			cp[1] -= 0.3;
		}
		cp[3] = cp[0] + 10;
		cp[4] = cp[1] -10;
		cp[5] = cp[2] + 10;//*/
		
		
		//System.out.println("View updated"+cp[2]);
	    //glLoadIdentity();
	    
	    GLU.gluLookAt(x, z, y,
	              player.x,player.z,player.y,
	              0,1,0);
	    //GLU.gluLookAt(0, 0, 10, 2, 0, 5, 0, 0, 1);
	    //GLU.gluLookAt( cp[0], cp[1], cp[2], cp[3], cp[4], cp[5], cp[6], cp[7], cp[8]);
	}
}
