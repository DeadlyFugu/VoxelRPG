package game.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/*
 * Hashmap code obtained from Technius at http://forums.bukkit.org/threads/saving-loading-hashmap.56447/
 * All credit for original code goes to him.
 */

public class HashmapLoader {
	public static void writeHashmap(String filename, HashMap<String,Integer> hashMap) {
		File file = new File(filename);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(String k:hashMap.keySet()) {
				bw.write(k + "," + hashMap.get(k));
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static HashMap<String,Integer> readHashmap(String filename) {
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		File file = new File("res/conf/"+filename+".txt");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String l;
			while((l = br.readLine()) != null) {
				String[] args = l.split("[,]", 2);
				if(args.length != 2)continue;
				String p = args[0].replaceAll(" ", "");
				String b = args[1].replaceAll(" ", "");
				hashMap.put(p, Integer.parseInt(b));
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return hashMap;
	}
}
