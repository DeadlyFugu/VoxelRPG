package game;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.glu.GLU;

public class Camera {
	
	private Input input;
	private Player player;
	private float x,y,z;
	private float tx,ty,tz;
	private float lx,ly,lz;
	private float[] cp = {-10,42,-10,0,32,0,0,1,0};
	private float xsteps, ysteps, zsteps = 0;
	
	public Camera(Input input, Player player) {
		this.input = input;
		this.player = player;
		x = player.x-10;
		y = player.y;
		z = player.z-4;
	}
	
	public void updateView() {
		
		if (input.isKeyPressed("Up")) {
			cp[0] += 0.3;
			cp[2] += 0.3;
		}
		if (input.isKeyPressed("Down")) {
			cp[0] -= 0.3;
			cp[2] -= 0.3;
		}
		if (input.isKeyPressed("Right")) {
			cp[0] -= 0.3;
			cp[2] += 0.3;
		}
		if (input.isKeyPressed("Left")) {
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
		cp[5] = cp[2] + 10;
		
		//System.out.println("View updated"+cp[2]);
	    //glLoadIdentity();
	    
	    /*GLU.gluLookAt(x, y, z,
	              player.x,player.y,player.z,
	              0.0f,1.0f,0.0f);*/
	    //GLU.gluLookAt(0, 0, 10, 2, 0, 5, 0, 0, 1);
	    GLU.gluLookAt( cp[0], cp[1], cp[2], cp[3], cp[4], cp[5], cp[6], cp[7], cp[8]);
	}
}
