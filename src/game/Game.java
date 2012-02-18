package game;

/* Code stolen from Nehe's Lesson 07 LWJGL version.
 * It's original header is below.
 *
 *      This Code Was Created By Jeff Molofee 2000
 *      A HUGE Thanks To Fredric Echols For Cleaning Up
 *      And Optimizing The Base Code, Making It More Flexible!
 *      If You've Found This Code Useful, Please Let Me Know.
 *      Visit My Site At nehe.gamedev.net
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.input.Keyboard;

/**
 * @author Mark Bernard
 * date:    16-Nov-2003
 *
 * Port of NeHe's Lesson 7 to LWJGL
 * Title: Texture Filters, Lighting & Keyboard Control
 * Uses version 0.8alpha of LWJGL http://www.lwjgl.org/
 *
 * Be sure that the LWJGL libraries are in your classpath
 *
 * Ported directly from the C++ version
 *
 * 2004-05-08: Updated to version 0.9alpha of LWJGL.
 *             Changed from all static to all instance objects.
 * 2004-09-22: Updated to version 0.92alpha of LWJGL.
 * 2004-12-19: Updated to version 0.94alpha of LWJGL and to use
 *             DevIL for image loading.
 */
public class Game {
	private boolean done = false;
	private boolean fullscreen = false;
	private final String windowTitle = "VoxelRPG";
	private DisplayMode displayMode;

	private Input input;
	private World world;
	private Player player;
	private Camera camera;

	private int shader;
	private int vertShader;
	private int fragShader;
	private boolean useShader = false;

	public static void main(String args[]) {
		boolean fullscreen = false;
		if(args.length>0) {
			if(args[0].equalsIgnoreCase("fullscreen")) {
				fullscreen = true;
			}
		}

		Game game = new Game();
		game.run(fullscreen);
	}

	public void run(boolean fullscreen) {
		this.fullscreen = fullscreen;
		try {
			init();
			while (!done) {
				checkInput();
				render();
				Display.update();
			}
			cleanup();
			System.exit(0);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void checkInput() {
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {       // Exit if Escape is pressed
			done = true;
		}
		if(Display.isCloseRequested()) {                     // Exit if window is closed
			done = true;
		}
	}

	private void switchMode() {
		fullscreen = !fullscreen;
		try {
			Display.setFullscreen(fullscreen);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private boolean render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);          // Clear The Screen And The Depth Buffer

		GL11.glLoadIdentity();                          // Reset The Current Modelview Matrix

		input.updateAxis();
		camera.updateView();

		world.update();

		return true;
	}
	private void createWindow() throws Exception {
		Display.setFullscreen(fullscreen);
		if (!fullscreen) {
			DisplayMode d[] = Display.getAvailableDisplayModes();
			for (int i = 0; i < d.length; i++) {
				System.out.println(d[i].toString());
				if (d[i].getWidth() == 1600
						&& d[i].getHeight() == 900
						&& d[i].getBitsPerPixel() == 32) {
					displayMode = d[i];
					break;
				}
			}
		} else {
			displayMode = Display.getDesktopDisplayMode();
		}
		Display.setDisplayMode(displayMode);
		Display.setTitle(windowTitle);
		Display.create();
		Display.setVSyncEnabled(true);
	}
	private void init() throws Exception {
		
		createWindow();

		initGL();

		input = new Input();
		player = new Player();
		camera = new Camera(input,player);
		world = new World(input,player,camera);
		player.setWorld(world);
		player.setCam(camera);
		
		new VBOThread(world).start();
	}
	private void initGL() {
		GL11.glEnable(GL11.GL_TEXTURE_2D); // Enable Texture Mapping
		GL11.glShadeModel(GL11.GL_SMOOTH); // Enable Smooth Shading
		GL11.glClearColor(0.8f, 0.9f, 0.95f, 1.0f); // Black Background
		GL11.glClearDepth(1.0f); // Depth Buffer Setup
		//GL11.glEnable(GL11.GL_CULL_FACE);
		//GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
		GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Testing To Do

		GL11.glEnable(GL11.GL_FOG); //Fog
		GL11.glFogf(GL11.GL_FOG_MODE,GL11.GL_LINEAR);
		GL11.glFogf(GL11.GL_FOG_START,10.0f);
		GL11.glFogf(GL11.GL_FOG_END,64.0f);
		FloatBuffer fogColor = BufferUtils.createFloatBuffer(4);
		fogColor.put(new float [] {0.8f,0.9f,0.95f,1.0f}).rewind();
		GL11.glFog(GL11.GL_FOG_COLOR, fogColor);

		GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
		GL11.glLoadIdentity(); // Reset The Projection Matrix

		// Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(45.0f, 1.78f,0.1f,100.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix

		// Really Nice Perspective Calculations
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);

		/*
		 * create the shader program. If OK, create vertex
		 * and fragment shaders
		 */
		shader=ARBShaderObjects.glCreateProgramObjectARB();

		if(shader!=0){
			vertShader=createVertShader("shaders/screen.vsh");
			fragShader=createFragShader("shaders/screen.fsh");
		}
		else useShader=false;

		/*
		 * if the vertex and fragment shaders setup sucessfully,
		 * attach them to the shader program, link the sahder program
		 * (into the GL context I suppose), and validate
		 */
		if(vertShader !=0 && fragShader !=0){
			ARBShaderObjects.glAttachObjectARB(shader, vertShader);
			ARBShaderObjects.glAttachObjectARB(shader, fragShader);

			ARBShaderObjects.glLinkProgramARB(shader);
			if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
				printLogInfo(shader);
				useShader=false;
			}
			ARBShaderObjects.glValidateProgramARB(shader);
			if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
				printLogInfo(shader);
				useShader=false;
			}
		}else useShader=false;

		if(useShader) {
			ARBShaderObjects.glUseProgramObjectARB(shader);
			System.out.println("hhh");
		}

		//Check to see if required extensions are supported
		if (GLContext.getCapabilities().GL_ARB_vertex_buffer_object){
			System.out.println("GL VBOs are supported! :D");
		} else {
			System.out.println("GL VBOs aren't supported! D:");
			System.exit(1);
		}
	}

	/*
	 * With the exception of syntax, setting up vertex and fragment shaders
	 * is the same.
	 * @param the name and path to the vertex shader
	 */
	private int createVertShader(String filename){
		//vertShader will be non zero if succefully created

		vertShader=ARBShaderObjects.glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
		//if created, convert the vertex shader code to a String
		if(vertShader==0){return 0;}
		String vertexCode="";
		String line;
		try{
			BufferedReader reader=new BufferedReader(new FileReader(filename));
			while((line=reader.readLine())!=null){
				vertexCode+=line + "\n";
				System.out.println(line);
			}
		}catch(Exception e){
			System.out.println("Fail reading vertex shading code");
			return 0;
		}
		/*
		 * associate the vertex code String with the created vertex shader
		 * and compile
		 */
		ARBShaderObjects.glShaderSourceARB(vertShader, vertexCode);
		ARBShaderObjects.glCompileShaderARB(vertShader);
		//if there was a problem compiling, reset vertShader to zero
		if(!printLogInfo(vertShader)){
			//vertShader=0;
		}
		//if zero we won't be using the shader
		return vertShader;
	}

	//same as per the vertex shader except for method syntax
	private int createFragShader(String filename){

		fragShader=ARBShaderObjects.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
		if(fragShader==0){return 0;}
		String fragCode="";
		String line;
		try{
			BufferedReader reader=new BufferedReader(new FileReader(filename));
			while((line=reader.readLine())!=null){
				fragCode+=line + "\n";
			}
		}catch(Exception e){
			System.out.println("Fail reading fragment shading code");
			return 0;
		}
		ARBShaderObjects.glShaderSourceARB(fragShader, fragCode);
		ARBShaderObjects.glCompileShaderARB(fragShader);
		if(!printLogInfo(fragShader)){
			//fragShader=0;
		}

		return fragShader;
	}

	private static boolean printLogInfo(int obj){
		System.out.println("lolnice");
		IntBuffer iVal = BufferUtils.createIntBuffer(1);
		ARBShaderObjects.glGetObjectParameterARB(obj,
				ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB, iVal);

		int length = iVal.get();
		if (length > 1) {
			// We have some info we need to output.
			ByteBuffer infoLog = BufferUtils.createByteBuffer(length);
			iVal.flip();
			ARBShaderObjects.glGetInfoLogARB(obj, iVal, infoLog);
			byte[] infoBytes = new byte[length];
			infoLog.get(infoBytes);
			String out = new String(infoBytes);
			System.out.println("Info log:\n"+out);
		}
		else return true;
		return false;
	}

	private void cleanup() {
		Display.destroy();
	}
}
