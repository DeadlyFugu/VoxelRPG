package game.system;

import game.world.Chunk;
import game.world.World;

import java.util.ArrayList;

public class VBOThread extends Thread {
	private World world;
	public VBOThread(World world) {
		this.world = world;
	}
	
	public void run() {
		while(true) {
			ArrayList<Chunk> chunkQueue = new ArrayList<Chunk>(world.chunkQueue);
				//sleep(10);
			for (Chunk c : chunkQueue) {
				try {
				c.loadVBO();
				world.chunkQueue.remove(c);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("Chunk in Queue complete");
			}
		}
	}
}
