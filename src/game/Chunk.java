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
		byte[] bcolra = {0,60};
		byte[] bcolga = {0,120};
		byte[] bcolba = {0,20};
		byte[] bcolrb = {0,100};
		byte[] bcolgb = {0,125};
		byte[] bcolbb = {0,40};
		double shadeLevel = 0.15;
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
						double[] vcol = {0.8,0.8,0.8,0.8,0.7,0.7,0.7,0.7,0.8,0.8,0.8,0.8,0.6,0.6,0.6,0.6,0.8,0.8,0.8,0.8,1,1,1,1};
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
						double weight = PerlinNoise.pNoise((x*32+i)*0.15, (y*32+j)*0.15, 0.2, 2);
						byte colr = (byte) (bcolra[bid] + (bcolra[bid] - bcolrb[bid])*weight);
						byte colg = (byte) (bcolga[bid] + (bcolga[bid] - bcolgb[bid])*weight);
						byte colb = (byte) (bcolba[bid] + (bcolba[bid] - bcolbb[bid])*weight);

						if (dircol[0]) { //+x axis stable
							vertexToAL(i+1.0f,j+1.0f,k+1.0f,(byte) (colr*vcol[0]),(byte) (colg*vcol[0]),(byte) (colb*vcol[0]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+0.0f,k+1.0f,(byte) (colr*vcol[1]),(byte) (colg*vcol[1]),(byte) (colb*vcol[1]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+0.0f,k+0.0f,(byte) (colr*vcol[2]),(byte) (colg*vcol[2]),(byte) (colb*vcol[2]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+1.0f,k+0.0f,(byte) (colr*vcol[3]),(byte) (colg*vcol[3]),(byte) (colb*vcol[3]),(byte) 1,vboDataAL,vboDataALE);
						}
						if (dircol[1]) { //-x axis stable
							vertexToAL(i+0.0f,j+1.0f,k+0.0f,(byte) (colr*vcol[4]),(byte) (colg*vcol[4]),(byte) (colb*vcol[4]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+0.0f,(byte) (colr*vcol[5]),(byte) (colg*vcol[5]),(byte) (colb*vcol[5]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+1.0f,(byte) (colr*vcol[6]),(byte) (colg*vcol[6]),(byte) (colb*vcol[6]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+1.0f,k+1.0f,(byte) (colr*vcol[7]),(byte) (colg*vcol[7]),(byte) (colb*vcol[7]),(byte) 1,vboDataAL,vboDataALE);
						}
						if (dircol[2]) { //+y axis stable
							vertexToAL(i+1.0f,j+1.0f,k+0.0f,(byte) (colr*vcol[8]),(byte) (colg*vcol[8]),(byte) (colb*vcol[8]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+1.0f,k+0.0f,(byte) (colr*vcol[9]),(byte) (colg*vcol[9]),(byte) (colb*vcol[9]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+1.0f,k+1.0f,(byte) (colr*vcol[10]),(byte) (colg*vcol[10]),(byte) (colb*vcol[10]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+1.0f,k+1.0f,(byte) (colr*vcol[11]),(byte) (colg*vcol[11]),(byte) (colb*vcol[11]),(byte) 1,vboDataAL,vboDataALE);
						}
						if (dircol[5]) { //-z axis stable
							vertexToAL(i+1.0f,j+0.0f,k+0.0f,(byte) (colr*vcol[12]),(byte) (colg*vcol[12]),(byte) (colb*vcol[12]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+0.0f,(byte) (colr*vcol[13]),(byte) (colg*vcol[13]),(byte) (colb*vcol[13]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+1.0f,k+0.0f,(byte) (colr*vcol[14]),(byte) (colg*vcol[14]),(byte) (colb*vcol[14]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+1.0f,k+0.0f,(byte) (colr*vcol[15]),(byte) (colg*vcol[15]),(byte) (colb*vcol[15]),(byte) 1,vboDataAL,vboDataALE);
						}
						if (dircol[3]) { //-y axis stable
							vertexToAL(i+1.0f,j+0.0f,k+1.0f,(byte) (colr*vcol[16]),(byte) (colg*vcol[16]),(byte) (colb*vcol[16]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+1.0f,(byte) (colr*vcol[17]),(byte) (colg*vcol[17]),(byte) (colb*vcol[17]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+0.0f,(byte) (colr*vcol[18]),(byte) (colg*vcol[18]),(byte) (colb*vcol[18]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+0.0f,k+0.0f,(byte) (colr*vcol[19]),(byte) (colg*vcol[19]),(byte) (colb*vcol[19]),(byte) 1,vboDataAL,vboDataALE);
						}
						if (dircol[4]) { //+z axis stable
							vertexToAL(i+1.0f,j+1.0f,k+1.0f,(byte) (colr*vcol[20]),(byte) (colg*vcol[20]),(byte) (colb*vcol[20]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+1.0f,k+1.0f,(byte) (colr*vcol[21]),(byte) (colg*vcol[21]),(byte) (colb*vcol[21]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+1.0f,(byte) (colr*vcol[22]),(byte) (colg*vcol[22]),(byte) (colb*vcol[22]),(byte) 1,vboDataAL,vboDataALE);
							vertexToAL(i+1.0f,j+0.0f,k+1.0f,(byte) (colr*vcol[23]),(byte) (colg*vcol[23]),(byte) (colb*vcol[23]),(byte) 1,vboDataAL,vboDataALE);
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
				heightMap[i][j] = (byte) (32+PerlinNoise.pNoise((x*32+i)*0.05, (y*32+j)*0.05, 0.3, 2)*32);
			}
		}
		for (int  i=0; i<width; i++) {
			for (int  j=0; j<width; j++) {
				for (int  k=0; k<height; k++) {
					if (heightMap[i][j] > k) chunkData[i][j][k] = 1;
					else chunkData[i][j][k] = 0;
				}
			}
		}
	}

	public void render() {

		GL11.glPushMatrix();
		
		GL11.glRotated(90, 1, 0, 0);
		GL11.glScaled(1, 1, -1);
		GL11.glTranslated(x*32,y*32,0);

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);

		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vboid);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);

		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vboidc);
		GL11.glColorPointer(4, GL11.GL_BYTE, 0, 0);

		GL11.glDrawArrays(GL11.GL_QUADS, 0, floatBuffer.limit()/3);

		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);//*/
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);

		GL11.glPopMatrix();
	}
}