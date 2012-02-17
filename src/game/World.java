package game;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.File;
import java.util.ArrayList;

public class World {
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<Chunk> allChunks = new ArrayList<Chunk>();
	public ArrayList<Chunk> activeChunks = new ArrayList<Chunk>();
	public ArrayList<Chunk> chunkQueue = new ArrayList<Chunk>();
	
	public Input input;
	public Player player;
	private Camera camera;
	public ChunkGenerator chunkGenerator;
	
	public World(Input input, Player player, Camera camera) {
		this.input = input;
		this.player = player;
		this.camera = camera;
		this.chunkGenerator = new ChunkGenerator();
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
				allChunks.add(new Chunk((int) (pcx+(Math.floor(i/5))-2),pcy+(i%5)-2, this));
				activeChunks.add(allChunks.get(allChunks.size()-1));
			}
		}
		
		chunkQueue.clear();
		
		for (Chunk c : activeChunks) {
			if (!c.hasVBO) {
				chunkQueue.add(c);
			} else {
				c.render();
			}
		}
	}
	
	public boolean placeFree(float i, float j, float k) {
		return placeFree((int) i, (int) j, (int) k);
	}
	
	public boolean placeFree(int i, int j, int k) {
		return blockAt(i,j,k) == 0;
	}
	
	public byte blockAt(int i, int j, int k) {
		int cxp = (int) Math.floor(i/32);
		int cyp = (int) Math.floor(j/32);
		for (Chunk c : activeChunks) {
			if (c.x == cxp && c.y == cyp) {
				return c.chunkData[Math.abs(i%32)][Math.abs(j%32)][k%64];
			}
		}
		Chunk nc = new Chunk(cxp,cyp, this);
		return nc.chunkData[Math.abs(i%32)][Math.abs(j%32)][k%64];
	}
	
	public void setBlockAt(int i, int j, int k, byte id) {
		int cxp = (int) Math.floor(i/32);
		int cyp = (int) Math.floor(j/32);
		
		Chunk ctr = null;
		
		for (Chunk c : activeChunks) {
			if (c.x == cxp && c.y == cyp) {
				c.chunkData[Math.abs(i%32)][Math.abs(j%32)][k%64] = id;
				//c.hasVBO = false;
				//c.loadVBO();
				c.saveDataToFile(new File("world/"+c.x+"_"+c.y));
				//c.clearVBO();
				//ctr = c;
				//allChunks.remove(c);
			}
		}
		
		if (ctr != null) {
			activeChunks.remove(ctr);
		}
	}
	
	public Chunk getChunkAt(int i, int j) {
		for (Chunk c : activeChunks) {
			if (c.x == i && c.y == j) {
				return c;
			}
		}
		return new Chunk(i,j, this);
	}
}
