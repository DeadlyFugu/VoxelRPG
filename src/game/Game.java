//Basic game setup for LWJGL.

package game;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

import static org.lwjgl.opengl.GL11.*;

public class Game {
	private Input input;
	private World world;
	private Player player;
	private Camera camera;
	
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
		
        //GL11.glEnable(GL11.GL_TEXTURE_2D); // Enable Texture Mapping
        //GL11.glShadeModel(GL11.GL_SMOOTH); // Enable Smooth Shading
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
        GL11.glClearDepth(1.0); // Depth Buffer Setup
        GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
        GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Testing To Do

        GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
        GL11.glLoadIdentity(); // Reset The Projection Matrix

        // Calculate The Aspect Ratio Of The Window
        GLU.gluPerspective(
          45.0f,
          1280/960,
          0.1f,
          100.0f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix

        // Really Nice Perspective Calculations
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		
		//Check to see if required extensions are supported
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object) {
			System.out.println("GL VBOs are supported! :D");
		} else {
			System.out.println("GL VBOs aren't supported! D:");
			System.exit(1);
		}
		
		//Code init
		input = new Input();
		player = new Player();
		world = new World(input,player);
		player.setWorld(world);
		camera = new Camera(input,player);
	}
	
	public void launch() {
		initialise();
		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
		    
			input.updateAxis();
		    
			// Clear the screen and depth buffer
		    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);	
		    
		    glLoadIdentity();
		    
			camera.updateView();
		    
		    // set the color of the quad (R,G,B,A)
		    glColor3f(0.5f,0.5f,1.0f);
		    	
		    // draw quad
		    /*glBegin(GL_QUADS);
		    glVertex2f(-1000,-1000);
			glVertex2f(1000,-1000);
			glVertex2f(1000,1000);
			glVertex2f(-1000,1000);
		    glEnd();//*/
			
			world.update();
			
			//glFlush();
		    
			Display.update();
		}
		
		Display.destroy();
	}
	
	public static void main(String[] argv) {
		new Game().launch();
		System.exit(0);
	}
}
