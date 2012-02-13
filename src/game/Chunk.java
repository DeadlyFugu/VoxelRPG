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
		byte[] bcolr = {0,60};
		byte[] bcolg = {0,120};
		byte[] bcolb = {0,20};
		double shadeLevel = 0.2;
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
						//if (dircol[2]) System.out.println("Yes"); else System.out.println("Nope");
						//System.out.println((int) chunkData[i][j][k])
						//Bottomright,bottomleft, topleft, topright
						double[] vcol = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
						if (i!= 0 && i!= 31 && j!= 0 && j!= 31 && k!= 0 && k!= 63) {
							//+Z axis
							if (chunkData[i][j][k+1] == 0) {
								if (chunkData[i-1][j][k+1] != 0) {vcol[22] -= shadeLevel; vcol[21] -= shadeLevel;}
								if (chunkData[i+1][j][k+1] != 0) {vcol[20] -= shadeLevel; vcol[23] -= shadeLevel;}
								if (chunkData[i][j-1][k+1] != 0) {vcol[23] -= shadeLevel; vcol[22] -= shadeLevel;}
								if (chunkData[i][j+1][k+1] != 0) {vcol[20] -= shadeLevel; vcol[21] -= shadeLevel;}
								
								if (chunkData[i-1][j-1][k+1] != 0) vcol[22] -= shadeLevel;
								if (chunkData[i+1][j-1][k+1] != 0) vcol[23] -= shadeLevel;
								if (chunkData[i-1][j+1][k+1] != 0) vcol[21] -= shadeLevel;
								if (chunkData[i+1][j+1][k+1] != 0) vcol[20] -= shadeLevel;								
							}
							//-X axis
							if (chunkData[i-1][j][k] == 0) {
								if (chunkData[i-1][j-1][k] != 0) {vcol[5] -= shadeLevel; vcol[6] -= shadeLevel;}
								if (chunkData[i-1][j+1][k] != 0) {vcol[4] -= shadeLevel; vcol[7] -= shadeLevel;}
								if (chunkData[i-1][j][k-1] != 0) {vcol[4] -= shadeLevel; vcol[5] -= shadeLevel;}
								if (chunkData[i-1][j][k+1] != 0) {vcol[7] -= shadeLevel; vcol[6] -= shadeLevel;}
								
								if (chunkData[i-1][j-1][k-1] != 0) vcol[5] -= shadeLevel;
								if (chunkData[i-1][j+1][k-1] != 0) vcol[4] -= shadeLevel;
								if (chunkData[i-1][j-1][k+1] != 0) vcol[6] -= shadeLevel;
								if (chunkData[i-1][j+1][k+1] != 0) vcol[7] -= shadeLevel;								
							}//*/
							//-Y axis
							if (chunkData[i][j-1][k] == 0) {
								if (chunkData[i-1][j-1][k] != 0) {vcol[17] -= shadeLevel; vcol[18] -= shadeLevel;}
								if (chunkData[i+1][j-1][k] != 0) {vcol[16] -= shadeLevel; vcol[19] -= shadeLevel;}
								if (chunkData[i][j-1][k-1] != 0) {vcol[19] -= shadeLevel; vcol[18] -= shadeLevel;}
								if (chunkData[i][j-1][k+1] != 0) {vcol[16] -= shadeLevel; vcol[17] -= shadeLevel;}
								
								if (chunkData[i-1][j-1][k-1] != 0) vcol[18] -= shadeLevel;
								if (chunkData[i+1][j-1][k-1] != 0) vcol[19] -= shadeLevel;
								if (chunkData[i-1][j-1][k+1] != 0) vcol[17] -= shadeLevel;
								if (chunkData[i+1][j-1][k+1] != 0) vcol[16] -= shadeLevel;								
							}//*/
						}
						byte bid = chunkData[i][j][k];

						if (dircol[0]) { //+x axis stable
							vertexToAL(i+1.0f,j+1.0f,k+1.0f,(byte) (bcolr[bid]*vcol[0]),(byte) (bcolg[bid]*vcol[0]),(byte) (bcolb[bid]*vcol[0]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+0.0f,k+1.0f,(byte) (bcolr[bid]*vcol[1]),(byte) (bcolg[bid]*vcol[1]),(byte) (bcolb[bid]*vcol[1]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+0.0f,k+0.0f,(byte) (bcolr[bid]*vcol[2]),(byte) (bcolg[bid]*vcol[2]),(byte) (bcolb[bid]*vcol[2]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+1.0f,k+0.0f,(byte) (bcolr[bid]*vcol[3]),(byte) (bcolg[bid]*vcol[3]),(byte) (bcolb[bid]*vcol[3]),(byte) 1,vboDataAL,vboDataALE);
						}
						if (dircol[1]) { //-x axis stable
							vertexToAL(i+0.0f,j+1.0f,k+0.0f,(byte) (bcolr[bid]*vcol[4]),(byte) (bcolg[bid]*vcol[4]),(byte) (bcolb[bid]*vcol[4]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+0.0f,(byte) (bcolr[bid]*vcol[5]),(byte) (bcolg[bid]*vcol[5]),(byte) (bcolb[bid]*vcol[5]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+1.0f,(byte) (bcolr[bid]*vcol[6]),(byte) (bcolg[bid]*vcol[6]),(byte) (bcolb[bid]*vcol[6]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+1.0f,k+1.0f,(byte) (bcolr[bid]*vcol[7]),(byte) (bcolg[bid]*vcol[7]),(byte) (bcolb[bid]*vcol[7]),(byte) 1,vboDataAL,vboDataALE);
						}
						if (dircol[2]) { //+y axis stable
							vertexToAL(i+1.0f,j+1.0f,k+0.0f,(byte) (bcolr[bid]*vcol[8]),(byte) (bcolg[bid]*vcol[8]),(byte) (bcolb[bid]*vcol[8]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+1.0f,k+0.0f,(byte) (bcolr[bid]*vcol[9]),(byte) (bcolg[bid]*vcol[9]),(byte) (bcolb[bid]*vcol[9]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+1.0f,k+1.0f,(byte) (bcolr[bid]*vcol[10]),(byte) (bcolg[bid]*vcol[10]),(byte) (bcolb[bid]*vcol[10]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+1.0f,k+1.0f,(byte) (bcolr[bid]*vcol[11]),(byte) (bcolg[bid]*vcol[11]),(byte) (bcolb[bid]*vcol[11]),(byte) 1,vboDataAL,vboDataALE);
						}
						if (dircol[5]) { //-z axis stable
							vertexToAL(i+1.0f,j+0.0f,k+0.0f,(byte) (bcolr[bid]*vcol[12]),(byte) (bcolg[bid]*vcol[12]),(byte) (bcolb[bid]*vcol[12]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+0.0f,(byte) (bcolr[bid]*vcol[13]),(byte) (bcolg[bid]*vcol[13]),(byte) (bcolb[bid]*vcol[13]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+1.0f,k+0.0f,(byte) (bcolr[bid]*vcol[14]),(byte) (bcolg[bid]*vcol[14]),(byte) (bcolb[bid]*vcol[14]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+1.0f,k+0.0f,(byte) (bcolr[bid]*vcol[15]),(byte) (bcolg[bid]*vcol[15]),(byte) (bcolb[bid]*vcol[15]),(byte) 1,vboDataAL,vboDataALE);
						}
						if (dircol[3]) { //-y axis stable
							vertexToAL(i+1.0f,j+0.0f,k+1.0f,(byte) (bcolr[bid]*vcol[16]),(byte) (bcolg[bid]*vcol[16]),(byte) (bcolb[bid]*vcol[16]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+1.0f,(byte) (bcolr[bid]*vcol[17]),(byte) (bcolg[bid]*vcol[17]),(byte) (bcolb[bid]*vcol[17]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+0.0f,(byte) (bcolr[bid]*vcol[18]),(byte) (bcolg[bid]*vcol[18]),(byte) (bcolb[bid]*vcol[18]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+0.0f,k+0.0f,(byte) (bcolr[bid]*vcol[19]),(byte) (bcolg[bid]*vcol[19]),(byte) (bcolb[bid]*vcol[19]),(byte) 1,vboDataAL,vboDataALE);
						}
						if (dircol[4]) { //+z axis stable
							vertexToAL(i+1.0f,j+1.0f,k+1.0f,(byte) (bcolr[bid]*vcol[20]),(byte) (bcolg[bid]*vcol[20]),(byte) (bcolb[bid]*vcol[20]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+1.0f,k+1.0f,(byte) (bcolr[bid]*vcol[21]),(byte) (bcolg[bid]*vcol[21]),(byte) (bcolb[bid]*vcol[21]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+1.0f,(byte) (bcolr[bid]*vcol[22]),(byte) (bcolg[bid]*vcol[22]),(byte) (bcolb[bid]*vcol[22]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+0.0f,k+1.0f,(byte) (bcolr[bid]*vcol[23]),(byte) (bcolg[bid]*vcol[23]),(byte) (bcolb[bid]*vcol[23]),(byte) 1,vboDataAL,vboDataALE);
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
				//heightMap[i][j] = (byte) (FastNoise.noise(i*2, j*2, 10));
				heightMap[i][j] = (byte) (32+PerlinNoise.pNoise((x*32+i)*0.1, (y*32+j)*0.1, 0.3, 2)*32);
			}
		}
		/*for (int i=0; i<32; i++) {
			for (int j=0; j<32; j++) {
				heightMap[i][j] = (byte) ((heightMap[Math.min(i+1,31)][j]+
						heightMap[Math.max(i-1,0)][j]+
						heightMap[j][Math.min(j+1,31)]+
						heightMap[j][Math.max(j-1,0)])/8);
			}
		}//*/
		for (int  i=0; i<width; i++) {
			for (int  j=0; j<width; j++) {
				for (int  k=0; k<height; k++) {
					if (heightMap[i][j] > k) chunkData[i][j][k] = 1;
					else chunkData[i][j][k] = 0;//*/
					/*if (k < 13) {
						if (FastNoise.noise(i, j, 14-k) != 0) chunkData[i][j][k] = 1; else chunkData[i][j][k] = 0;
					} else {
						chunkData[i][j][k] = 0;
					}*/
					//System.out.println(PerlinNoise.noise(i*100, j*100, k*100));
				}
			}
		}
	}

	public void render() {
		//GL11.glDrawArrays(mode, first, count)
		System.out.println("Chunk is rendering"+x);

		GL11.glPushMatrix();
		
		GL11.glRotated(90, 1, 0, 0);
		GL11.glScaled(1, 1, -1);
		GL11.glTranslated(x*32,y*32,0);

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