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
	private int vbosize;
	ArrayList<Float> vboDataAL = new ArrayList<Float>();
	ArrayList<Byte> vboDataALE = new ArrayList<Byte>();
	public boolean hasVBO = false;
	private boolean finalVBO = false;
	private World world;
	Chunk[] friendChunks = {null,null,null,null};

	public Chunk(int x, int y, World world) {
		chunkData = new byte[width][width][height];
		this.x = x;
		this.y = y;
		this.world = world;
		//loadDataFromFile(new File("world/"+Integer.toString(x)+"_"+Integer.toString(y)))
		File f = new File("world/"+x+"_"+y);
		if (f.isFile()) {
			loadDataFromFile(f);
		} else {
			flattenSurface(new Random());
		}
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
		
		vboDataAL.clear();
		vboDataALE.clear();
		
		byte[] bcolra = {0,60};
		byte[] bcolga = {0,120};
		byte[] bcolba = {0,20};
		byte[] bcolrb = {0,100};
		byte[] bcolgb = {0,125};
		byte[] bcolbb = {0,40};
		double shadeLevel = 0.15;

		friendChunks[0] = world.getChunkAt(x+1, y);
		friendChunks[1] = world.getChunkAt(x-1, y);
		friendChunks[2] = world.getChunkAt(x, y+1);
		friendChunks[3] = world.getChunkAt(x, y-1);
		
		for (int  i=0; i<width; i++) {
			for (int  j=0; j<width; j++) {
				for (int  k=0; k<height; k++) {
					if (chunkData[i][j][k] != 0) {
						boolean requiresFullChk = !(i!= 0 && i!= 31 && j!= 0 && j!= 31);
						boolean[] dircol = {(blockAt(i+1,j,k,requiresFullChk) == 0),
								(blockAt(i-1,j,k,requiresFullChk) == 0),
								(blockAt(i,j+1,k,requiresFullChk) == 0),
								(blockAt(i,j-1,k,requiresFullChk) == 0),
								(blockAt(i,j,Math.min(k+1,63),requiresFullChk) == 0),
								(blockAt(i,j,Math.max(k-1,0),requiresFullChk) == 0)
						};
						//if (dircol[2]) System.out.println("Yes"); else System.out.println("Nope");
						//System.out.println((int) blockAt(i,j,k])
						//topleft, topright, Bottomright,bottomleft
						double dc = 1;
						if (k < 15)
							dc = (float) k/16;
						double[] vcol = {dc*0.8,dc*0.8,dc*0.8,dc*0.8,
								dc*0.7,dc*0.7,dc*0.7,dc*0.7,
								dc*0.8,dc*0.8,dc*0.8,dc*0.8,
								dc*0.6,dc*0.6,dc*0.6,dc*0.6,
								dc*0.8,dc*0.8,dc*0.8,dc*0.8,
								dc*1,dc*1,dc*1,dc*1};
						//if (i!= 0 && i!= 31 && j!= 0 && j!= 31 && k!= 0 && k!= 63) {
						//+Z axis
						
						if (blockAt(i,j,k+1,requiresFullChk) == 0) {
							if (blockAt(i-1,j,k+1,requiresFullChk) != 0) {vcol[22] -= shadeLevel; vcol[21] -= shadeLevel;}
							if (blockAt(i+1,j,k+1,requiresFullChk) != 0) {vcol[20] -= shadeLevel; vcol[23] -= shadeLevel;}
							if (blockAt(i,j-1,k+1,requiresFullChk) != 0) {vcol[23] -= shadeLevel; vcol[22] -= shadeLevel;}
							if (blockAt(i,j+1,k+1,requiresFullChk) != 0) {vcol[20] -= shadeLevel; vcol[21] -= shadeLevel;}

							if (blockAt(i-1,j-1,k+1,requiresFullChk) != 0) vcol[22] -= shadeLevel;
							if (blockAt(i+1,j-1,k+1,requiresFullChk) != 0) vcol[23] -= shadeLevel;
							if (blockAt(i-1,j+1,k+1,requiresFullChk) != 0) vcol[21] -= shadeLevel;
							if (blockAt(i+1,j+1,k+1,requiresFullChk) != 0) vcol[20] -= shadeLevel;								
						}
						//-X axis
						if (blockAt(i-1,j,k,requiresFullChk) == 0) {
							if (blockAt(i-1,j-1,k,requiresFullChk) != 0) {vcol[5] -= shadeLevel; vcol[6] -= shadeLevel;}
							if (blockAt(i-1,j+1,k,requiresFullChk) != 0) {vcol[4] -= shadeLevel; vcol[7] -= shadeLevel;}
							if (blockAt(i-1,j,k-1,requiresFullChk) != 0) {vcol[4] -= shadeLevel; vcol[5] -= shadeLevel;}
							if (blockAt(i-1,j,k+1,requiresFullChk) != 0) {vcol[7] -= shadeLevel; vcol[6] -= shadeLevel;}

							if (blockAt(i-1,j-1,k-1,requiresFullChk) != 0) vcol[5] -= shadeLevel;
							if (blockAt(i-1,j+1,k-1,requiresFullChk) != 0) vcol[4] -= shadeLevel;
							if (blockAt(i-1,j-1,k+1,requiresFullChk) != 0) vcol[6] -= shadeLevel;
							if (blockAt(i-1,j+1,k+1,requiresFullChk) != 0) vcol[7] -= shadeLevel;								
						}//*/
						//+X axis
						if (blockAt(i+1,j,k,requiresFullChk) == 0) {
							if (blockAt(i+1,j-1,k,requiresFullChk) != 0) {vcol[2] -= shadeLevel; vcol[1] -= shadeLevel;}
							if (blockAt(i+1,j+1,k,requiresFullChk) != 0) {vcol[3] -= shadeLevel; vcol[0] -= shadeLevel;}
							if (blockAt(i+1,j,k-1,requiresFullChk) != 0) {vcol[3] -= shadeLevel; vcol[2] -= shadeLevel;}
							if (blockAt(i+1,j,k+1,requiresFullChk) != 0) {vcol[0] -= shadeLevel; vcol[1] -= shadeLevel;}

							if (blockAt(i+1,j-1,k-1,requiresFullChk) != 0) vcol[2] -= shadeLevel;
							if (blockAt(i+1,j+1,k-1,requiresFullChk) != 0) vcol[3] -= shadeLevel;
							if (blockAt(i+1,j-1,k+1,requiresFullChk) != 0) vcol[1] -= shadeLevel;
							if (blockAt(i+1,j+1,k+1,requiresFullChk) != 0) vcol[0] -= shadeLevel;								
						}//*/
						//-Y axis
						if (blockAt(i,j-1,k,requiresFullChk) == 0) {
							if (blockAt(i-1,j-1,k,requiresFullChk) != 0) {vcol[17] -= shadeLevel; vcol[18] -= shadeLevel;}
							if (blockAt(i+1,j-1,k,requiresFullChk) != 0) {vcol[16] -= shadeLevel; vcol[19] -= shadeLevel;}
							if (blockAt(i,j-1,k-1,requiresFullChk) != 0) {vcol[19] -= shadeLevel; vcol[18] -= shadeLevel;}
							if (blockAt(i,j-1,k+1,requiresFullChk) != 0) {vcol[16] -= shadeLevel; vcol[17] -= shadeLevel;}

							if (blockAt(i-1,j-1,k-1,requiresFullChk) != 0) vcol[18] -= shadeLevel;
							if (blockAt(i+1,j-1,k-1,requiresFullChk) != 0) vcol[19] -= shadeLevel;
							if (blockAt(i-1,j-1,k+1,requiresFullChk) != 0) vcol[17] -= shadeLevel;
							if (blockAt(i+1,j-1,k+1,requiresFullChk) != 0) vcol[16] -= shadeLevel;								
						}//*/
						//+Y axis 8-11
						if (blockAt(i,j+1,k,requiresFullChk) == 0) {
							if (blockAt(i-1,j+1,k,requiresFullChk) != 0) {vcol[10] -= shadeLevel; vcol[9] -= shadeLevel;}
							if (blockAt(i+1,j+1,k,requiresFullChk) != 0) {vcol[11] -= shadeLevel; vcol[8] -= shadeLevel;}
							if (blockAt(i,j+1,k-1,requiresFullChk) != 0) {vcol[8] -= shadeLevel; vcol[9] -= shadeLevel;}
							if (blockAt(i,j+1,k+1,requiresFullChk) != 0) {vcol[11] -= shadeLevel; vcol[10] -= shadeLevel;}

							if (blockAt(i-1,j+1,k-1,requiresFullChk) != 0) vcol[9] -= shadeLevel;
							if (blockAt(i+1,j+1,k-1,requiresFullChk) != 0) vcol[8] -= shadeLevel;
							if (blockAt(i-1,j+1,k+1,requiresFullChk) != 0) vcol[10] -= shadeLevel;
							if (blockAt(i+1,j+1,k+1,requiresFullChk) != 0) vcol[11] -= shadeLevel;								
						}//*/

						byte bid = blockAt(i,j,k,requiresFullChk);
						double weight = new PerlinNoise(world.getSeed().hashCode()+7).noise((x*32+i)*0.015, (y*32+j)*0.015, k*0.015);
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
		

		System.out.println("VBO Cleared");

		hasVBO = true;

	}

	public byte blockAt(int i, int j, int l, boolean fullChk) {
		int k = Math.max(Math.min(l,63),0);
		if (fullChk) {
			//return world.blockAt(x*32+i, y*32+j, k);
			if (i == -1) {
				if (j == -1 || j == 32) return 0;
				return friendChunks[1].chunkData[31][j][k];
			}
			if (i == 32) {
				if (j == -1 || j == 32) return 0;
				return friendChunks[0].chunkData[0][j][k];
			}
			if (j == -1) {
				return friendChunks[3].chunkData[i][31][k];
			}
			if (j == 32) {
				return friendChunks[2].chunkData[i][0][k];
			}
			//return 0;
		}
		return chunkData[i][j][k];
	}

	public void loadVBO() {
		//if (!hasVBO) {
			generateVBO();
		//}
	}

	public void loadDataFromFile(File file) {
		try {
			FileInputStream is = new FileInputStream(file);

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
	
	public void saveDataToFile(File file) {
		try {
			FileOutputStream os = new FileOutputStream(file);

			for (int  i=0; i<width; i++) {
				for (int  j=0; j<width; j++) {
					for (int  k=0; k<height; k++) {
						 os.write(chunkData[i][j][k]);
					}
				}
			}

			// Close the input stream and return bytes
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void flattenSurface(Random random) {
		world.chunkGenerator.generate(this);
	}
	
	private void makeVBO() {
		//Create a VBO and shove stuff into it

		vboid = VBOHandler.createVBOID();
		vboidc = VBOHandler.createVBOID();

		FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(vbosize = vboDataAL.size());
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

	public void render() {
		
		if (!finalVBO && hasVBO) {
			makeVBO();
			finalVBO = true;
		}

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

		GL11.glDrawArrays(GL11.GL_QUADS, 0, vbosize/3);

		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);//*/
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);

		GL11.glPopMatrix();
	}

	public void clearVBO() {
		// TODO Auto-generated method stub
		//ARBVertexBufferObject.glDeleteBuffersARB(vboid);
		//ARBVertexBufferObject.glDeleteBuffersARB(vboidc);
		hasVBO = false;
	}
}