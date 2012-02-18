package game;

public class ChunkGeneratorTerrain extends ChunkGenerator {
	public ChunkGeneratorTerrain(World world) {
		super(world);
		// TODO Auto-generated constructor stub
	}

	public void generate(Chunk c) {
		if (c.x == 0 || c.y == 0 || c.x == 402 || c.y == 402) {
			for (int i=0; i<32; i++) {
				for (int j=0; j<32; j++) {
					for (int k=0; k<64; k++) {
						if (k != 63) c.chunkData[i][j][k] = 1;
						else c.chunkData[i][j][k] = 0;
					}
				}
			}
			return;
		}
		byte[][] heightMap = new byte[32][32];
		for (int i=0; i<32; i++) {
			for (int j=0; j<32; j++) {
				int px = c.x*32+i;
				int py = c.y*32+j;
				double terrainHeight = calcHeight(px,py);
				heightMap[i][j] = (byte) (wAvg(oceanHeight(px,py)*20+1,32+baseHeight(px,py)*16,48+mountainHeight(px,py)*6,1+Math.min(terrainHeight,0),Math.max(terrainHeight,0)));
				//System.out.println(heightMap[i][j]);
				//System.out.println(terrainHeight);
			}
		}//*/
		for (int i=0; i<32; i++) {
			for (int j=0; j<32; j++) {
				for (int k=0; k<64; k++) {
					if (heightMap[i][j] > k) c.chunkData[i][j][k] = 1;
					else c.chunkData[i][j][k] = 0;
				}
			}
		}
	}

	private double baseHeight(int x,int y) {
		return _pGen1.fBm(x*0.001,0, y*0.001);
	}

	private double oceanHeight(int x,int y) {
		return Math.abs(_pGen2.fBm(x*0.0015,0, y*0.0015));
	}

	private double mountainHeight(int x,int y) {
		return _pGen3.fBm(x*0.001,0, y*0.001)+1;
	}

	public double calcHeight(double x, double y) {
		return _pGen5.fBm(x * 0.005, 0, 0.005 * y);
	}
	
	public double calcTemperature(double x, double y) {
		double result = _pGen6.fBm(x * 0.0005, 0, 0.0005 * y);
		return clamp((result + 1.0) / 2.0);
	}

	public double calcHumidity(double x, double y) {
		double result = _pGen7.fBm(x * 0.0005, 0, 0.0005 * y);
		return clamp((result + 1.0) / 2.0);
	}
	
    public static double clamp(double value) {
        if (value > 1.0)
            return 1.0;
        if (value < 0.0)
            return 0.0;
        return value;
    }
    
    public static double wAvg(double x, double x1, double x2, double q00, double q01) {
    	//System.out.println(x+" "+x1+" "+x2+" "+q00+" "+q01);
        return Math.abs((x-x1)+(x1*q00)) + Math.abs((x1-x2)+(x2*q01));
    }
}