package game;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.ArrayList;

public class World {
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<Chunk> allChunks = new ArrayList<Chunk>();
	private ArrayList<Chunk> activeChunks = new ArrayList<Chunk>();
	
	public Input input;
	
	public World(Input input) {
		this.input = input;
	}
	
	public void update() {
		//Update
		
		// Clear the screen and depth buffer
	    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);	
		
	    // set the color of the quad (R,G,B,A)
	    glColor3f(0.5f,0.5f,1.0f);
	    	
	    // draw quad
	    glBegin(GL_QUADS);
	    glVertex2f(100,100);
		glVertex2f(100+200,100);
		glVertex2f(100+200,100+200);
		glVertex2f(100,100+200);
	    glEnd();
		
		for (Entity e : entities) {
			e.update();
		}
	}
}
