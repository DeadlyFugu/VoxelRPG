package game;

import java.util.ArrayList;

public class VBOThread extends Thread {
	private World world;
	public VBOThread(World world) {
		this.world = world;
	}
	
	public void run() {
		while(true) {
			try {
				sleep(1000);
			for (Chunk c : world.chunkQueue) {
				c.loadVBO();
			}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
