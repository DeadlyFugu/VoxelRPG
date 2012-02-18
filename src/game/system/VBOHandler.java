package game.system;

import java.nio.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

public class VBOHandler {

	public static int createVBOID() {
		return ARBVertexBufferObject.glGenBuffersARB();
	}

	public static void bufferData(int id, FloatBuffer buffer) {
		ARBVertexBufferObject.glBindBufferARB(
				ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, id);
		ARBVertexBufferObject.glBufferDataARB(
				ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, buffer,
				ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
	}
	
	public static void bufferData(int id, ByteBuffer buffer) {
		ARBVertexBufferObject.glBindBufferARB(
				ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, id);
		ARBVertexBufferObject.glBufferDataARB(
				ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, buffer,
				ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
	}

	public static void bufferElementData(int id, ShortBuffer buffer) {
		ARBVertexBufferObject.glBindBufferARB(
				ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, id);
		ARBVertexBufferObject.glBufferDataARB(
				ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, buffer,
				ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
	}

	public static void bindBuffer(int id) {
		ARBVertexBufferObject.glBindBufferARB(
				ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, id);
	}

	public static void bindElementBuffer(int id) {
		ARBVertexBufferObject.glBindBufferARB(
				ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, id);
	}
}
