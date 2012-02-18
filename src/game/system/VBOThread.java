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
				} catch (Exception e) {
					e.printStackTrace();
				}
				world.chunkQueue.remove(c);
				System.out.println("Chunk in Queue complete");
			}
		}
	}
}
