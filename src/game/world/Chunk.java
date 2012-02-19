package game.world;

import game.render.ChunkRenderer;
import game.system.VBOHandler;
import game.util.PerlinNoise;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.nio.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

public class Chunk {
	public byte[][][] chunkData;
	public int x,y;
	private int vboid, vboidc;
	int height = 64;
	int width = 32;
	private int vbosize;
	public ArrayList<Float> vboDataAL = new ArrayList<Float>();
	public ArrayList<Byte> vboDataALE = new ArrayList<Byte>();
	public boolean hasVBO = false;
	private boolean finalVBO = false;
	public World world;
	public Chunk[] friendChunks = {null,null,null,null};

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

	private void generateVBO() {
		ChunkRenderer.renderChunk(this);
		hasVBO = true;
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
		world.chunkGeneratorTerrain.generate(this);
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