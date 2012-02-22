package game.system;

import game.util.HashmapLoader;

import java.io.*;
import java.util.HashMap;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.util.input.ControllerAdapter;


public class Input {
	HashMap<String, Integer> keyConfig;
	public float ax,ay,cx,cy;
	private float atx,aty,ctx,cty;
	private boolean emulatedAxis;
	private Controller controller;

	public Input() {
		keyConfig = HashmapLoader.readHashmap("config");
		ax = 0; ay = 0;
		cx = 0; cy = 0;
		try {
			Controllers.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Controllers.getControllerCount() > 0) {
			controller = Controllers.getController(0);
			System.out.println("Joystick has "+controller.getButtonCount() +" buttons. Its name is "+controller.getName());
		}
		emulatedAxis = (Controllers.getControllerCount() == 0);
	}

	public boolean isKeyPressed(String key) {
		//COMPLETE: Fix null pointer in this function
		if (emulatedAxis) {
			if (keyConfig.containsKey(key)) {
				return Keyboard.isKeyDown(keyConfig.get(key));
			}
		} else {
			if (keyConfig.containsKey("Joy"+key)) {
				return controller.isButtonPressed(keyConfig.get("Joy"+key));
			}
		}
		return false;
		//Key names: Up,Down,Left,Right,Jump,Sprint,Weapon1,Weapon2,CamUp,CamDown,CamLeft,CamRight
		//return false; //Null-pointer 'fix'.
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
		//System.out.println(aty);
		if (emulatedAxis) {
			if (isKeyPressed("Right")) atx = 1;
			else if (isKeyPressed("Left")) atx = -1;
			else atx = 0;
			if (isKeyPressed("Down")) aty = -1;
			else if (isKeyPressed("Up")) aty = 1;
			else aty = 0;

			if (isKeyPressed("CamRight")) ctx = 1;
			else if (isKeyPressed("CamLeft")) ctx = -1;
			else ctx = 0;
			if (isKeyPressed("CamDown")) cty = 1;
			else if (isKeyPressed("CamUp")) cty = -1;
			else cty = 0;
		} else {
			controller.poll();
			atx = (float) Math.max(Math.min(1,controller.getXAxisValue()*1.1),-1);
			aty = (float) Math.max(Math.min(1,-controller.getYAxisValue()*1.1),-1);
			ctx = (float) Math.max(Math.min(1,controller.getZAxisValue()*1.1),-1);
			cty = (float) Math.max(Math.min(1,controller.getRZAxisValue()*1.1),-1);
		}

		ax += (atx-ax)/10;
		ay += (aty-ay)/10;
		cx += (ctx-cx)/10;
		cy += (cty-cy)/10;

	}


}
