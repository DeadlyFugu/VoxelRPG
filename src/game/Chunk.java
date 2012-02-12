package game;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.nio.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

public class Chunk {
	byte[][][] chunkData;
	public int x,y;
	private int vboid, vboidc;
	int height = 64;
	int width = 32;
	FloatBuffer floatBuffer;

	public Chunk(int x, int y) {
		chunkData = new byte[width][width][height];
		this.x = x;
		this.y = y;
		//loadDataFromFile(new File("world/"+Integer.toString(x)+"_"+Integer.toString(y)))
		flattenSurface(new Random());
		generateVBO();
	}

	private void vertexToAL(float x, float y, float z, byte r, byte g, byte b, byte a, ArrayList<Float> al, ArrayList<Byte> ale) {
		al.add(x);
		al.add(y);
		al.add(z);
		ale.add(r);
		ale.add(g);
		ale.add(b);
		ale.add(a);
	}

	private void generateVBO() {
		ArrayList<Float> vboDataAL = new ArrayList<Float>();
		ArrayList<Byte> vboDataALE = new ArrayList<Byte>();
		for (int  i=0; i<width; i++) {
			for (int  j=0; j<width; j++) {
				for (int  k=0; k<height; k++) {
					if (chunkData[i][j][k] != 0) {
						boolean[] dircol = {(chunkData[Math.min(i+1,width-1)][j][k] == 0),
								(chunkData[Math.max(i-1,0)][j][k] == 0),
								(chunkData[i][Math.min(j+1,width-1)][k] == 0),
								(chunkData[i][Math.max(j-1,0)][k] == 0),
								(chunkData[i][j][Math.min(k+1,height-1)] == 0),
								(chunkData[i][j][Math.max(k-1,0)] == 0)
						};
						System.out.println((int) chunkData[i][j][k]);
						//if (dircol[2]) System.out.println("Yes"); else System.out.println("Nope");
						//System.out.println((int) chunkData[i][j][k])
						byte[] vcol = {120,110,100,90,80,70,60,50};

						if (dircol[0]) { //+x axis stable
							vertexToAL(i+1.0f,j+1.0f,k+1.0f,vcol[0],vcol[0],vcol[0],vcol[1],vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+0.0f,k+1.0f,vcol[1],vcol[1],vcol[1],vcol[1],vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+0.0f,k+0.0f,vcol[2],vcol[2],vcol[2],vcol[1],vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+1.0f,k+0.0f,vcol[3],vcol[3],vcol[3],vcol[1],vboDataAL,vboDataALE);
						}
						if (dircol[1]) { //-x axis stable
							vertexToAL(i+0.0f,j+1.0f,k+0.0f,vcol[4],vcol[4],vcol[4],vcol[1],vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+0.0f,vcol[5],vcol[5],vcol[5],vcol[1],vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+1.0f,vcol[6],vcol[6],vcol[6],vcol[1],vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+1.0f,k+1.0f,vcol[7],vcol[7],vcol[7],vcol[1],vboDataAL,vboDataALE);
						}
						if (dircol[2]) { //+y axis stable
							vertexToAL(i+1.0f,j+1.0f,k+0.0f,vcol[3],vcol[3],vcol[3],vcol[1],vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+1.0f,k+0.0f,vcol[4],vcol[4],vcol[4],vcol[1],vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+1.0f,k+1.0f,vcol[7],vcol[7],vcol[7],vcol[1],vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+1.0f,k+1.0f,vcol[0],vcol[0],vcol[0],vcol[1],vboDataAL,vboDataALE);
						}
						if (dircol[5]) { //-z axis stable
							vertexToAL(i+1.0f,j+0.0f,k+0.0f,vcol[2],vcol[2],vcol[2],vcol[1],vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+0.0f,vcol[5],vcol[5],vcol[5],vcol[1],vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+1.0f,k+0.0f,vcol[4],vcol[4],vcol[4],vcol[1],vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+1.0f,k+0.0f,vcol[3],vcol[3],vcol[3],vcol[1],vboDataAL,vboDataALE);
						}
						if (dircol[3]) { //-y axis stable
							vertexToAL(i+1.0f,j+0.0f,k+1.0f,vcol[1],vcol[1],vcol[1],vcol[1],vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+1.0f,vcol[6],vcol[6],vcol[6],vcol[1],vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+0.0f,vcol[5],vcol[5],vcol[5],vcol[1],vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+0.0f,k+0.0f,vcol[2],vcol[2],vcol[2],vcol[1],vboDataAL,vboDataALE);
						}
						if (dircol[4]) { //+z axis stable
							vertexToAL(i+1.0f,j+1.0f,k+1.0f,vcol[0],vcol[0],vcol[0],vcol[1],vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+1.0f,k+1.0f,vcol[7],vcol[7],vcol[7],vcol[1],vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+1.0f,vcol[6],vcol[6],vcol[6],vcol[1],vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+0.0f,k+1.0f,vcol[1],vcol[1],vcol[1],vcol[1],vboDataAL,vboDataALE);
						}//*/
					}
				}
			}
		}

		//Create a VBO and shove stuff into it

		vboid = VBOHandler.createVBOID();
		vboidc = VBOHandler.createVBOID();

		floatBuffer = BufferUtils.createFloatBuffer(vboDataAL.size());
		float[] alAsFloat = new float[vboDataAL.size()];
		int index = 0;
		for (float b : vboDataAL) {
			alAsFloat[index++] = b;
		}
		floatBuffer.put(alAsFloat);
		floatBuffer.rewind();
		System.out.println(floatBuffer.toString());
		VBOHandler.bufferData(vboid, floatBuffer);

		ByteBuffer byteBuffer = BufferUtils.createByteBuffer(vboDataALE.size());
		byte[] aleAsByte = new byte[vboDataALE.size()];
		index = 0;
		for (byte b : vboDataALE) {
			aleAsByte[index++] = b;
		}
		byteBuffer.put(aleAsByte);
		byteBuffer.rewind();
		VBOHandler.bufferData(vboidc, byteBuffer);//*/

	}

	private void loadDataFromFile(File file) {
		try {
			FileInputStream is = new FileInputStream(file);

			// Get the size of the file
			long length = file.length();

			for (int  i=0; i<width; i++) {
				for (int  j=0; j<width; j++) {
					for (int  k=0; k<height; k++) {
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

	private void flattenSurface(Random random) {
		byte[][] heightMap = new byte[32][32];
		for (int i=0; i<32; i++) {
			for (int j=0; j<32; j++) {
				heightMap[i][j] = (byte) (4+random.nextInt(10));
			}
		}
		for (int i=0; i<32; i++) {
			for (int j=0; j<32; j++) {
				heightMap[i][j] = (byte) ((heightMap[Math.min(i+1,31)][j]+
						heightMap[Math.max(i-1,0)][j]+
						heightMap[j][Math.min(j+1,31)]+
						heightMap[j][Math.max(j-1,0)])/8);
			}
		}
		for (int  i=0; i<width; i++) {
			for (int  j=0; j<width; j++) {
				for (int  k=0; k<height; k++) {
					if (heightMap[i][j] > k) chunkData[i][j][k] = 1;
					else chunkData[i][j][k] = 0;//*/
					//if (PerlinNoise.noise(i*100, j*100, k*100) != 0) chunkData[i][j][k] = 1; else chunkData[i][j][k] = 0;
					//System.out.println(PerlinNoise.noise(i*100, j*100, k*100));
				}
			}
		}
	}

	public void render() {
		//GL11.glDrawArrays(mode, first, count)
		System.out.println("Chunk is rendering");

		GL11.glPushMatrix();
		
		GL11.glRotated(90, 1, 0, 0);
		GL11.glScaled(1, 1, -1);

		//GL11.glColor3d(0.5,0,0);

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);

		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vboid);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vboidc);
		GL11.glColorPointer(4, GL11.GL_BYTE, 0, 0);

		//GL11.glVertexPointer(3,0,floatBuffer);

		GL11.glDrawArrays(GL11.GL_QUADS, 0, floatBuffer.limit()/3);

		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);//*/
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);

		GL11.glPopMatrix();
	}
}