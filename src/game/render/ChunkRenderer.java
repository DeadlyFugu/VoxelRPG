package game.render;

import java.util.ArrayList;

import org.lwjgl.util.Color;

import game.util.PerlinNoise;
import game.world.Chunk;
import game.world.block.Block;

public class ChunkRenderer {
	public static void renderChunk(Chunk c) {
		c.vboDataAL.clear();
		c.vboDataALE.clear();
		
		double shadeLevel = 0.15;

		c.friendChunks[0] = c.world.getChunkAt(c.x+1, c.y);
		c.friendChunks[1] = c.world.getChunkAt(c.x-1, c.y);
		c.friendChunks[2] = c.world.getChunkAt(c.x, c.y+1);
		c.friendChunks[3] = c.world.getChunkAt(c.x, c.y-1);
		
		for (int  i=0; i<32; i++) {
			for (int  j=0; j<32; j++) {
				for (int  k=0; k<64; k++) {
					if (c.chunkData[i][j][k] != 0) {
						boolean requiresFullChk = !(i!= 0 && i!= 31 && j!= 0 && j!= 31);
						boolean[] dircol = {(blockAt(i+1,j,k,requiresFullChk,c) == 0),
								(blockAt(i-1,j,k,requiresFullChk,c) == 0),
								(blockAt(i,j+1,k,requiresFullChk,c) == 0),
								(blockAt(i,j-1,k,requiresFullChk,c) == 0),
								(blockAt(i,j,Math.min(k+1,63),requiresFullChk,c) == 0),
								(blockAt(i,j,Math.max(k-1,0),requiresFullChk,c) == 0)
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
						
						if (blockAt(i,j,k+1,requiresFullChk,c) == 0) {
							if (blockAt(i-1,j,k+1,requiresFullChk,c) != 0) {vcol[22] -= shadeLevel; vcol[21] -= shadeLevel;}
							if (blockAt(i+1,j,k+1,requiresFullChk,c) != 0) {vcol[20] -= shadeLevel; vcol[23] -= shadeLevel;}
							if (blockAt(i,j-1,k+1,requiresFullChk,c) != 0) {vcol[23] -= shadeLevel; vcol[22] -= shadeLevel;}
							if (blockAt(i,j+1,k+1,requiresFullChk,c) != 0) {vcol[20] -= shadeLevel; vcol[21] -= shadeLevel;}

							if (blockAt(i-1,j-1,k+1,requiresFullChk,c) != 0) vcol[22] -= shadeLevel;
							if (blockAt(i+1,j-1,k+1,requiresFullChk,c) != 0) vcol[23] -= shadeLevel;
							if (blockAt(i-1,j+1,k+1,requiresFullChk,c) != 0) vcol[21] -= shadeLevel;
							if (blockAt(i+1,j+1,k+1,requiresFullChk,c) != 0) vcol[20] -= shadeLevel;								
						}
						//-X axis
						if (blockAt(i-1,j,k,requiresFullChk,c) == 0) {
							if (blockAt(i-1,j-1,k,requiresFullChk,c) != 0) {vcol[5] -= shadeLevel; vcol[6] -= shadeLevel;}
							if (blockAt(i-1,j+1,k,requiresFullChk,c) != 0) {vcol[4] -= shadeLevel; vcol[7] -= shadeLevel;}
							if (blockAt(i-1,j,k-1,requiresFullChk,c) != 0) {vcol[4] -= shadeLevel; vcol[5] -= shadeLevel;}
							if (blockAt(i-1,j,k+1,requiresFullChk,c) != 0) {vcol[7] -= shadeLevel; vcol[6] -= shadeLevel;}

							if (blockAt(i-1,j-1,k-1,requiresFullChk,c) != 0) vcol[5] -= shadeLevel;
							if (blockAt(i-1,j+1,k-1,requiresFullChk,c) != 0) vcol[4] -= shadeLevel;
							if (blockAt(i-1,j-1,k+1,requiresFullChk,c) != 0) vcol[6] -= shadeLevel;
							if (blockAt(i-1,j+1,k+1,requiresFullChk,c) != 0) vcol[7] -= shadeLevel;								
						}//*/
						//+X axis
						if (blockAt(i+1,j,k,requiresFullChk,c) == 0) {
							if (blockAt(i+1,j-1,k,requiresFullChk,c) != 0) {vcol[2] -= shadeLevel; vcol[1] -= shadeLevel;}
							if (blockAt(i+1,j+1,k,requiresFullChk,c) != 0) {vcol[3] -= shadeLevel; vcol[0] -= shadeLevel;}
							if (blockAt(i+1,j,k-1,requiresFullChk,c) != 0) {vcol[3] -= shadeLevel; vcol[2] -= shadeLevel;}
							if (blockAt(i+1,j,k+1,requiresFullChk,c) != 0) {vcol[0] -= shadeLevel; vcol[1] -= shadeLevel;}

							if (blockAt(i+1,j-1,k-1,requiresFullChk,c) != 0) vcol[2] -= shadeLevel;
							if (blockAt(i+1,j+1,k-1,requiresFullChk,c) != 0) vcol[3] -= shadeLevel;
							if (blockAt(i+1,j-1,k+1,requiresFullChk,c) != 0) vcol[1] -= shadeLevel;
							if (blockAt(i+1,j+1,k+1,requiresFullChk,c) != 0) vcol[0] -= shadeLevel;								
						}//*/
						//-Y axis
						if (blockAt(i,j-1,k,requiresFullChk,c) == 0) {
							if (blockAt(i-1,j-1,k,requiresFullChk,c) != 0) {vcol[17] -= shadeLevel; vcol[18] -= shadeLevel;}
							if (blockAt(i+1,j-1,k,requiresFullChk,c) != 0) {vcol[16] -= shadeLevel; vcol[19] -= shadeLevel;}
							if (blockAt(i,j-1,k-1,requiresFullChk,c) != 0) {vcol[19] -= shadeLevel; vcol[18] -= shadeLevel;}
							if (blockAt(i,j-1,k+1,requiresFullChk,c) != 0) {vcol[16] -= shadeLevel; vcol[17] -= shadeLevel;}

							if (blockAt(i-1,j-1,k-1,requiresFullChk,c) != 0) vcol[18] -= shadeLevel;
							if (blockAt(i+1,j-1,k-1,requiresFullChk,c) != 0) vcol[19] -= shadeLevel;
							if (blockAt(i-1,j-1,k+1,requiresFullChk,c) != 0) vcol[17] -= shadeLevel;
							if (blockAt(i+1,j-1,k+1,requiresFullChk,c) != 0) vcol[16] -= shadeLevel;								
						}//*/
						//+Y axis 8-11
						if (blockAt(i,j+1,k,requiresFullChk,c) == 0) {
							if (blockAt(i-1,j+1,k,requiresFullChk,c) != 0) {vcol[10] -= shadeLevel; vcol[9] -= shadeLevel;}
							if (blockAt(i+1,j+1,k,requiresFullChk,c) != 0) {vcol[11] -= shadeLevel; vcol[8] -= shadeLevel;}
							if (blockAt(i,j+1,k-1,requiresFullChk,c) != 0) {vcol[8] -= shadeLevel; vcol[9] -= shadeLevel;}
							if (blockAt(i,j+1,k+1,requiresFullChk,c) != 0) {vcol[11] -= shadeLevel; vcol[10] -= shadeLevel;}

							if (blockAt(i-1,j+1,k-1,requiresFullChk,c) != 0) vcol[9] -= shadeLevel;
							if (blockAt(i+1,j+1,k-1,requiresFullChk,c) != 0) vcol[8] -= shadeLevel;
							if (blockAt(i-1,j+1,k+1,requiresFullChk,c) != 0) vcol[10] -= shadeLevel;
							if (blockAt(i+1,j+1,k+1,requiresFullChk,c) != 0) vcol[11] -= shadeLevel;								
						}//*/

						byte bid = blockAt(i,j,k,requiresFullChk,c);
						//double weight = new PerlinNoise(c.world.getSeed().hashCode()+7).noise((c.x*32+i)*0.015, (c.y*32+j)*0.015, k*0.015);
						//byte colr = (byte) (bcolra[bid] + (bcolra[bid] - bcolrb[bid])*weight);
						//byte colg = (byte) (bcolga[bid] + (bcolga[bid] - bcolgb[bid])*weight);
						//byte colb = (byte) (bcolba[bid] + (bcolba[bid] - bcolbb[bid])*weight);
						Color col = Block.blocks[bid].getColor();
						byte colr = (byte) col.getRed();
						byte colg = (byte) col.getGreen();
						byte colb = (byte) col.getBlue();
						
						if (dircol[0]) { //+x axis stable
							vertexToAL(i+1.0f,j+1.0f,k+1.0f,(byte) (colr*vcol[0]),(byte) (colg*vcol[0]),(byte) (colb*vcol[0]),(byte) 1,c.vboDataAL,c.vboDataALE);
							vertexToAL(i+1.0f,j+0.0f,k+1.0f,(byte) (colr*vcol[1]),(byte) (colg*vcol[1]),(byte) (colb*vcol[1]),(byte) 1,c.vboDataAL,c.vboDataALE);
							vertexToAL(i+1.0f,j+0.0f,k+0.0f,(byte) (colr*vcol[2]),(byte) (colg*vcol[2]),(byte) (colb*vcol[2]),(byte) 1,c.vboDataAL,c.vboDataALE);
							vertexToAL(i+1.0f,j+1.0f,k+0.0f,(byte) (colr*vcol[3]),(byte) (colg*vcol[3]),(byte) (colb*vcol[3]),(byte) 1,c.vboDataAL,c.vboDataALE);
						}
						if (dircol[1]) { //-x axis stable
							vertexToAL(i+0.0f,j+1.0f,k+0.0f,(byte) (colr*vcol[4]),(byte) (colg*vcol[4]),(byte) (colb*vcol[4]),(byte) 1,c.vboDataAL,c.vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+0.0f,(byte) (colr*vcol[5]),(byte) (colg*vcol[5]),(byte) (colb*vcol[5]),(byte) 1,c.vboDataAL,c.vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+1.0f,(byte) (colr*vcol[6]),(byte) (colg*vcol[6]),(byte) (colb*vcol[6]),(byte) 1,c.vboDataAL,c.vboDataALE);
							vertexToAL(i+0.0f,j+1.0f,k+1.0f,(byte) (colr*vcol[7]),(byte) (colg*vcol[7]),(byte) (colb*vcol[7]),(byte) 1,c.vboDataAL,c.vboDataALE);
						}
						if (dircol[2]) { //+y axis stable
							vertexToAL(i+1.0f,j+1.0f,k+0.0f,(byte) (colr*vcol[8]),(byte) (colg*vcol[8]),(byte) (colb*vcol[8]),(byte) 1,c.vboDataAL,c.vboDataALE);
							vertexToAL(i+0.0f,j+1.0f,k+0.0f,(byte) (colr*vcol[9]),(byte) (colg*vcol[9]),(byte) (colb*vcol[9]),(byte) 1,c.vboDataAL,c.vboDataALE);
							vertexToAL(i+0.0f,j+1.0f,k+1.0f,(byte) (colr*vcol[10]),(byte) (colg*vcol[10]),(byte) (colb*vcol[10]),(byte) 1,c.vboDataAL,c.vboDataALE);
							vertexToAL(i+1.0f,j+1.0f,k+1.0f,(byte) (colr*vcol[11]),(byte) (colg*vcol[11]),(byte) (colb*vcol[11]),(byte) 1,c.vboDataAL,c.vboDataALE);
						}
						if (dircol[5]) { //-z axis stable
							vertexToAL(i+1.0f,j+0.0f,k+0.0f,(byte) (colr*vcol[12]),(byte) (colg*vcol[12]),(byte) (colb*vcol[12]),(byte) 1,c.vboDataAL,c.vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+0.0f,(byte) (colr*vcol[13]),(byte) (colg*vcol[13]),(byte) (colb*vcol[13]),(byte) 1,c.vboDataAL,c.vboDataALE);
							vertexToAL(i+0.0f,j+1.0f,k+0.0f,(byte) (colr*vcol[14]),(byte) (colg*vcol[14]),(byte) (colb*vcol[14]),(byte) 1,c.vboDataAL,c.vboDataALE);
							vertexToAL(i+1.0f,j+1.0f,k+0.0f,(byte) (colr*vcol[15]),(byte) (colg*vcol[15]),(byte) (colb*vcol[15]),(byte) 1,c.vboDataAL,c.vboDataALE);
						}
						if (dircol[3]) { //-y axis stable
							vertexToAL(i+1.0f,j+0.0f,k+1.0f,(byte) (colr*vcol[16]),(byte) (colg*vcol[16]),(byte) (colb*vcol[16]),(byte) 1,c.vboDataAL,c.vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+1.0f,(byte) (colr*vcol[17]),(byte) (colg*vcol[17]),(byte) (colb*vcol[17]),(byte) 1,c.vboDataAL,c.vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+0.0f,(byte) (colr*vcol[18]),(byte) (colg*vcol[18]),(byte) (colb*vcol[18]),(byte) 1,c.vboDataAL,c.vboDataALE);
							vertexToAL(i+1.0f,j+0.0f,k+0.0f,(byte) (colr*vcol[19]),(byte) (colg*vcol[19]),(byte) (colb*vcol[19]),(byte) 1,c.vboDataAL,c.vboDataALE);
						}
						if (dircol[4]) { //+z axis stable
							vertexToAL(i+1.0f,j+1.0f,k+1.0f,(byte) (colr*vcol[20]),(byte) (colg*vcol[20]),(byte) (colb*vcol[20]),(byte) 1,c.vboDataAL,c.vboDataALE);
							vertexToAL(i+0.0f,j+1.0f,k+1.0f,(byte) (colr*vcol[21]),(byte) (colg*vcol[21]),(byte) (colb*vcol[21]),(byte) 1,c.vboDataAL,c.vboDataALE);
							vertexToAL(i+0.0f,j+0.0f,k+1.0f,(byte) (colr*vcol[22]),(byte) (colg*vcol[22]),(byte) (colb*vcol[22]),(byte) 1,c.vboDataAL,c.vboDataALE);
							vertexToAL(i+1.0f,j+0.0f,k+1.0f,(byte) (colr*vcol[23]),(byte) (colg*vcol[23]),(byte) (colb*vcol[23]),(byte) 1,c.vboDataAL,c.vboDataALE);
						}//*/
					}
				}
			}
		}
	}
	
	public static byte blockAt(int i, int j, int l, boolean fullChk, Chunk c) {
		int k = Math.max(Math.min(l,63),0);
		if (fullChk) {
			//return world.blockAt(x*32+i, y*32+j, k);
			if (i == -1) {
				if (j == -1 || j == 32) return 0;
				return c.friendChunks[1].chunkData[31][j][k];
			}
			if (i == 32) {
				if (j == -1 || j == 32) return 0;
				return c.friendChunks[0].chunkData[0][j][k];
			}
			if (j == -1) {
				return c.friendChunks[3].chunkData[i][31][k];
			}
			if (j == 32) {
				return c.friendChunks[2].chunkData[i][0][k];
			}
			//return 0;
		}
		return c.chunkData[i][j][k];
	}
	
	private static void vertexToAL(float x, float y, float z, byte r, byte g, byte b, byte a, ArrayList<Float> al, ArrayList<Byte> ale) {
		al.add(x);
		al.add(y);
		al.add(z);
		ale.add(r);
		ale.add(g);
		ale.add(b);
		ale.add(a);
	}
}
