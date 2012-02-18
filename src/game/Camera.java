package game;

import static org.lwjgl.opengl.GL11.*;

import game.system.Input;
import game.util.MathEXT;
import game.world.World;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Camera {
	
	private Input input;
	private Player player;
	private World world;
	public float x,y,z;
	private float tx,ty,tz;
	private float lx,ly,lz;
	public float[] cp = {-10,42,-10,0,32,0,0,1,0};
	private float xsteps, ysteps, zsteps = 0;
	public float yaw,zoom;
	private boolean isUnderwater = false;
	FloatBuffer fogColorUnderwater;
	FloatBuffer fogColorNormal;
	
	public Camera(Input input, Player player) {
		this.input = input;
		this.player = player;
		yaw = 1;
		zoom = 10;
		x = tx = (float) (player.x+(zoom*MathEXT.cosd(yaw)));
		y = ty = (float) (player.y-(zoom*MathEXT.sind(yaw)));
		z = tz = player.z+zoom/2;
		lx = player.x;
		ly = player.y;
		lz = player.z;

		fogColorUnderwater = BufferUtils.createFloatBuffer(4);
		fogColorUnderwater.put(new float [] {0.2f,0.45f,0.98f,1.0f}).rewind();
		fogColorNormal = BufferUtils.createFloatBuffer(4);
		fogColorNormal.put(new float [] {0.8f,0.9f,0.95f,1.0f}).rewind();
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public void updateView() {
		
		if (z < 15.5 && isUnderwater == false) {
			isUnderwater = true;
			GL11.glFogf(GL11.GL_FOG_START,0.0f);
			GL11.glFogf(GL11.GL_FOG_END,32.0f);
			GL11.glFog(GL11.GL_FOG_COLOR, fogColorUnderwater);
		} else if (z > 15.5 && isUnderwater == true) {
			isUnderwater = false;
			GL11.glFogf(GL11.GL_FOG_START,10.0f);
			GL11.glFogf(GL11.GL_FOG_END,64.0f);
			GL11.glFog(GL11.GL_FOG_COLOR, fogColorNormal);
		}
		
		
	    
		player.kfc = (float) MathEXT.point_direction(x, y, player.x, player.y);
		
		yaw+=input.cx*2;
		
		zoom = Math.min(Math.max(zoom+input.cy/4,3),20);
		
		float azoom = zoom;
		
		while(!world.placeFree((float) (player.x+(azoom*MathEXT.cosd(yaw))), (float) (player.y-(azoom*MathEXT.sind(yaw))), player.z+azoom/2) && azoom > 4f) {
			azoom-=0.5f;
		}
		azoom-=2f;
		
		tx = (float) (player.x+(azoom*MathEXT.cosd(yaw)));
		ty = (float) (player.y-(azoom*MathEXT.sind(yaw)));
		tz = player.z+azoom/2;
		
	    x += (tx-x)/10;
	    y += (ty-y)/10;
	    z += (tz-z)/10;
	    

		
	    lx += (player.x-lx)/6;
	    ly += (player.y-ly)/6;
	    lz += (player.z-lz)/6;
		
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
	              lx,lz,ly,
	              0,1,0);
	    //GLU.gluLookAt(0, 0, 10, 2, 0, 5, 0, 0, 1);
	    //GLU.gluLookAt( cp[0], cp[1], cp[2], cp[3], cp[4], cp[5], cp[6], cp[7], cp[8]);
	}
}
