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
	private Camera camera;
	
	public World(Input input, Player player, Camera camera) {
		this.input = input;
		this.player = player;
		this.camera = camera;
	}
	
	public void update() {
		//Update
	    
	    player.update(input);
		
		for (Entity e : entities) {
			e.update();
		}
		
		int pcx = (int) (camera.x/32);
		int pcy = (int) (camera.y/32);
		
		Chunk[] suitableChunksFound = {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null};
		for (Chunk c : allChunks) {
			/*if (c.x == pcx-1 && c.y == pcy-1) suitableChunksFound[0] = c;
			else if (c.x == pcx-1 && c.y == pcy) suitableChunksFound[1] = c;
			else if (c.x == pcx-1 && c.y == pcy+1) suitableChunksFound[2] = c;

			else if (c.x == pcx && c.y == pcy-1) suitableChunksFound[3] = c;
			else if (c.x == pcx && c.y == pcy) suitableChunksFound[4] = c;
			else if (c.x == pcx && c.y == pcy+1) suitableChunksFound[5] = c;
			
			else if (c.x == pcx+1 && c.y == pcy-1) suitableChunksFound[6] = c;
			else if (c.x == pcx+1 && c.y == pcy) suitableChunksFound[7] = c;
			else if (c.x == pcx+1 && c.y == pcy+1) suitableChunksFound[8] = c;//*/
			//System.out.println(c.x+" "+c.y+" "+((pcx-c.x+1)*3+(pcy-c.y+1)));
			if (c.x > pcx-3 && c.y > pcy-3 && c.x < pcx+3 && c.y < pcy+3) suitableChunksFound[((c.x-pcx+2)*5+(c.y-pcy+2))] = c;
			else if (activeChunks.contains(c)) {
				activeChunks.remove(c);
			}//*/
		}
		
		for (int i=0; i<25; i++) {
			if (suitableChunksFound[i] != null) {
				if (!activeChunks.contains(suitableChunksFound[i])) {
					activeChunks.add(suitableChunksFound[i]);
				}
			} else {
				System.out.println("New chunk added");
				allChunks.add(new Chunk((int) (pcx+(Math.floor(i/5))-2),pcy+(i%5)-2));
				activeChunks.add(allChunks.get(allChunks.size()-1));
			}
		}
		
		for (Chunk c : activeChunks) {
			c.render();
		}
	}
	
	public boolean placeFree(float i, float j, float k) {
		return placeFree((int) i, (int) j, (int) k);
	}

	public boolean placeFree(int i, int j, int k) {
		// TODO Auto-generated method stub
		int cxp = (int) Math.floor(i/32);
		int cyp = (int) Math.floor(j/32);
		for (Chunk c : activeChunks) {
			if (c.x == cxp && c.y == cyp) {
				//System.out.println("placeFree called on loaded chunk at "+cxp+","+cyp+" "+i%32+","+j%32+","+k%64+" ="+c.chunkData[i%32][j%32][k]);
				return c.chunkData[i%32][j%32][k%64] == 0;
			}
		}
		//System.out.println("placeFree called on unloaded chunk at "+cxp+","+cyp);
		return false;
	}
}
