package game.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

import game.world.block.Block;

public class ListLoader {
	public static Block[] loadBlocks() {
		ArrayList<Block> blocks = new ArrayList<Block>();
		blocks.add(null);
		File file = new File("res/conf/blocks.txt");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String l;
			while((l = br.readLine()) != null) {
				blocks.add(Block.loadBlock(l));
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return (Block[]) blocks.toArray(new Block[blocks.size()]);
	}
}