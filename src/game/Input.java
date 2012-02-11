package game;

import java.io.*;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;


public class Input {
	HashMap<String, Integer> keyConfig = new HashMap<String,Integer>();
	float ax,atx,ay,aty,cx,ctx,cy,cty;
	
	public Input() {
		readFromConfigFile("config.txt");
		ax = 0; ay = 0;
		cx = 0; cy = 0;
	}
	
	public boolean isKeyPressed(String key) {
		//TODO: Fix null pointer in this function
		//return Keyboard.isKeyDown(keyConfig.get(key));
		//Key names: Up,Down,Left,Right,Jump,Sprint,Weapon1,Weapon2,CamUp,CamDown,CamLeft,CamRight
		return false; //Null-pointer 'fix'.
	}
	
	public float getAxis(int axis) {
		switch (axis) {
		case 1: return ax;
		case 2: return ay;
		case 3: return cx; 
		default: return cy;
		}
	}
	
	public void updateAxis() {
		if (isKeyPressed("Right")) atx = 1;
		else if (isKeyPressed("Left")) atx = -1;
		else atx = 0;
		if (isKeyPressed("Down")) aty = 1;
		else if (isKeyPressed("Up")) aty = -1;
		else aty = 0;
		
		if (isKeyPressed("CamRight")) ctx = 1;
		else if (isKeyPressed("CamLeft")) ctx = -1;
		else ctx = 0;
		if (isKeyPressed("CamDown")) cty = 1;
		else if (isKeyPressed("CamUp")) cty = -1;
		else cty = 0;
	}
	
	public void readFromConfigFile(String filename) {
		File file = new File(filename);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(String k:keyConfig.keySet()) {
				bw.write(k + "," + keyConfig.get(k));
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void writeConfigToFile(String filename) {
		HashMap<String, Boolean> hashmap = new HashMap<String, Boolean>();
		File file = new File(filename);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String l;
			while((l = br.readLine()) != null) {
				String[] args = l.split("[,]", 2);
				if(args.length != 2)continue;
				String p = args[0].replaceAll(" ", "");
				String b = args[1].replaceAll(" ", "");
				if(b.equalsIgnoreCase("true"))hashmap.put(p, true);
				else hashmap.put(p, false);
			}
		br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
