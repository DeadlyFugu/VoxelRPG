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
	public Player player;
	
	public World(Input input, Player player) {
		this.input = input;
		this.player = player;
		allChunks.add(new Chunk(0, 0));
		activeChunks.add(allChunks.get(0));
	}
	
	public void update() {
		//Update
	    
	    player.update(input);
		
		for (Entity e : entities) {
			e.update();
		}
		
		for (Chunk c : activeChunks) {
			c.render();
		}
	}
}
