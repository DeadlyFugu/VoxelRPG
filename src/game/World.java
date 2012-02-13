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
	}
	
	public void update() {
		//Update
	    
	    player.update(input);
		
		for (Entity e : entities) {
			e.update();
		}
		
		int pcx = (int) (player.x/32);
		int pcy = (int) (player.y/32);
		
		Chunk[] suitableChunksFound = {null,null,null,null,null,null,null,null,null};
		for (Chunk c : allChunks) {
			if (c.x == pcx-1 && c.y == pcy-1) suitableChunksFound[0] = c;
			else if (c.x == pcx-1 && c.y == pcy) suitableChunksFound[1] = c;
			else if (c.x == pcx-1 && c.y == pcy+1) suitableChunksFound[2] = c;

			else if (c.x == pcx && c.y == pcy-1) suitableChunksFound[3] = c;
			else if (c.x == pcx && c.y == pcy) suitableChunksFound[4] = c;
			else if (c.x == pcx && c.y == pcy+1) suitableChunksFound[5] = c;
			
			else if (c.x == pcx+1 && c.y == pcy-1) suitableChunksFound[6] = c;
			else if (c.x == pcx+1 && c.y == pcy) suitableChunksFound[7] = c;
			else if (c.x == pcx+1 && c.y == pcy+1) suitableChunksFound[8] = c;
		}
		
		for (int i=0; i<9; i++) {
			if (suitableChunksFound[i] != null) {
				if (!activeChunks.contains(suitableChunksFound[i])) {
					activeChunks.add(suitableChunksFound[i]);
				}
			} else {
				allChunks.add(new Chunk((int) (pcx+(Math.floor(i/3))-1),pcy+(i%3)-1));
				activeChunks.add(allChunks.get(allChunks.size()-1));
			}
		}
		
		for (Chunk c : activeChunks) {
			c.render();
		}
	}
}
