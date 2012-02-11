//Basic game setup for LWJGL.

package game;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

public class Game {
	private Input input;
	private World world;
	public void launch() {
		initialise();
		
		while (!Display.isCloseRequested()) {
			input.updateAxis();
			
			world.update();
		    
			Display.update();
		}
		
		Display.destroy();
	}
	
	private void initialise() {
		//Display init
		try {
			Display.setDisplayMode(new DisplayMode(1280,960));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		//GL init
	    glClearColor(1,1,1,1);
	    glEnable(GL_CULL_FACE);
	    glCullFace(GL_BACK);

	    glEnable(GL_DEPTH_TEST);
	    glDepthFunc(GL_LESS);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 800, 0, 600, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		//Code init
		input = new Input();
		world = new World(input);
	}
	
	public static void main(String[] argv) {
		new Game().launch();
	}
}
