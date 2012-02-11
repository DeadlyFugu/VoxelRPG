package game;

import java.io.*;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

public class Chunk {
	byte[][][] chunkData;
	public int x,y;
	
	public Chunk(int x, int y) {
		chunkData = new byte[64][64][64];
		this.x = x;
		this.y = y;
		//loadDataFromFile(new File("world/"+Integer.toString(x)+"_"+Integer.toString(y)))
		flattenSurface();
		generateVBO();
	}
	
	private void vertexToAL(float x, float y, float z, byte r, byte g, byte b, byte a, ArrayList<Byte> al) {
		al.add((byte) x);
		al.add((byte) y);
		al.add((byte) z);
		al.add(r);
		al.add(g);
		al.add(b);
		al.add(a);
	}
	
	private void generateVBO() {
		ArrayList<Byte> vboDataAL = new ArrayList<Byte>();
	    for (int  i=0; i<64; i++) {
	    	for (int  j=0; j<64; j++) {
	    		for (int  k=0; k<64; k++) {
                    boolean[] dircol = {(chunkData[Math.min(i+1,63)][j][k] == 0),
                            (chunkData[Math.max(i-1,0)][j][k] == 0),
                            (chunkData[i][Math.min(j+1,63)][k] == 0),
                            (chunkData[i][Math.max(j-1,0)][k] == 0),
                            (chunkData[i][j][Math.min(k+1,63)] == 0),
                            (chunkData[i][j][Math.max(k-1,0)] == 0)
                    };
                    byte[] vcol = {120,120,120,120,120,120,120,120};
                    
                 if (dircol[0]) { //+x axis stable
                    vertexToAL(i+1.0f,j+1.0f,k+1.0f,vcol[0],vcol[0],vcol[0],vcol[1],vboDataAL);
                    vertexToAL(i+1.0f,j+0.0f,k+1.0f,vcol[1],vcol[1],vcol[1],vcol[1],vboDataAL);
                    vertexToAL(i+1.0f,j+0.0f,k+0.0f,vcol[2],vcol[2],vcol[2],vcol[1],vboDataAL);
                    vertexToAL(i+1.0f,j+1.0f,k+0.0f,vcol[3],vcol[3],vcol[3],vcol[1],vboDataAL);
                }
                if (dircol[1]) { //-x axis stable
                    vertexToAL(i+0.0f,j+1.0f,k+0.0f,vcol[4],vcol[4],vcol[4],vcol[1],vboDataAL);
                    vertexToAL(i+0.0f,j+0.0f,k+0.0f,vcol[5],vcol[5],vcol[5],vcol[1],vboDataAL);
                    vertexToAL(i+0.0f,j+0.0f,k+1.0f,vcol[6],vcol[6],vcol[6],vcol[1],vboDataAL);
                    vertexToAL(i+0.0f,j+1.0f,k+1.0f,vcol[7],vcol[7],vcol[7],vcol[1],vboDataAL);
                }
                if (dircol[2]) { //+y axis stable
                    vertexToAL(i+1.0f,j+1.0f,k+0.0f,vcol[3],vcol[3],vcol[3],vcol[1],vboDataAL);
                    vertexToAL(i+0.0f,j+1.0f,k+0.0f,vcol[4],vcol[4],vcol[4],vcol[1],vboDataAL);
                    vertexToAL(i+0.0f,j+1.0f,k+1.0f,vcol[7],vcol[7],vcol[7],vcol[1],vboDataAL);
                    vertexToAL(i+1.0f,j+1.0f,k+1.0f,vcol[0],vcol[0],vcol[0],vcol[1],vboDataAL);
                }
                if (dircol[5]) { //-z axis stable
                    vertexToAL(i+1.0f,j+0.0f,k+0.0f,vcol[2],vcol[2],vcol[2],vcol[1],vboDataAL);
                    vertexToAL(i+0.0f,j+0.0f,k+0.0f,vcol[5],vcol[5],vcol[5],vcol[1],vboDataAL);
                    vertexToAL(i+0.0f,j+1.0f,k+0.0f,vcol[4],vcol[4],vcol[4],vcol[1],vboDataAL);
                    vertexToAL(i+1.0f,j+1.0f,k+0.0f,vcol[3],vcol[3],vcol[3],vcol[1],vboDataAL);
                }
                if (dircol[3]) { //-y axis stable
                    vertexToAL(i+1.0f,j+0.0f,k+1.0f,vcol[1],vcol[1],vcol[1],vcol[1],vboDataAL);
                    vertexToAL(i+0.0f,j+0.0f,k+1.0f,vcol[6],vcol[6],vcol[6],vcol[1],vboDataAL);
                    vertexToAL(i+0.0f,j+0.0f,k+0.0f,vcol[5],vcol[5],vcol[5],vcol[1],vboDataAL);
                    vertexToAL(i+1.0f,j+0.0f,k+0.0f,vcol[2],vcol[2],vcol[2],vcol[1],vboDataAL);
                }
                if (dircol[4]) { //+z axis stable
                    vertexToAL(i+1.0f,j+1.0f,k+1.0f,vcol[0],vcol[0],vcol[0],vcol[1],vboDataAL);
                    vertexToAL(i+0.0f,j+1.0f,k+1.0f,vcol[7],vcol[7],vcol[7],vcol[1],vboDataAL);
                    vertexToAL(i+0.0f,j+0.0f,k+1.0f,vcol[6],vcol[6],vcol[6],vcol[1],vboDataAL);
                    vertexToAL(i+1.0f,j+0.0f,k+1.0f,vcol[1],vcol[1],vcol[1],vcol[1],vboDataAL);
                }//*/
	    		}
	    	}
	    }
	    
	    //Create a VBO and shove stuff into it
	    
	}
	
	private void loadDataFromFile(File file) {
		try {
	    FileInputStream is = new FileInputStream(file);

	    // Get the size of the file
	    long length = file.length();
	    
	    for (int  i=0; i<64; i++) {
	    	for (int  j=0; j<64; j++) {
	    		for (int  k=0; k<64; k++) {
	    			chunkData[i][j][k] = (byte) is.read();
	    		}
	    	}
	    }

	    // Close the input stream and return bytes
	    is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void flattenSurface() {
	    for (int  i=0; i<64; i++) {
	    	for (int  j=0; j<64; j++) {
	    		for (int  k=0; k<64; k++) {
	    			if (k > 32) chunkData[i][j][k] = 1;
	    			else chunkData[i][j][k] = 0;
	    		}
	    	}
	    }
	}
	
	public void render() {
		//GL11.glDrawArrays(mode, first, count)
	}
}