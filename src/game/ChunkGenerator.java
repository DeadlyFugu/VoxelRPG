package game;

public class ChunkGenerator {
	public void generate(Chunk c) {
		byte[][] heightMap = new byte[32][32];
		for (int i=0; i<32; i++) {
			for (int j=0; j<32; j++) {
				heightMap[i][j] = (byte) (32+PerlinNoise.pNoise((c.x*32+i)*0.05, (c.y*32+j)*0.05, 0.3, 2)*32);
			}
		}
		for (int  i=0; i<32; i++) {
			for (int  j=0; j<32; j++) {
				for (int  k=0; k<64; k++) {
					if (heightMap[i][j] > k) c.chunkData[i][j][k] = 1;
					else c.chunkData[i][j][k] = 0;
				}
			}
		}
	}
}
