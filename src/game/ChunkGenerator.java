package game;

public abstract class ChunkGenerator {
	protected final PerlinNoise _pGen1, _pGen2, _pGen3, _pGen4, _pGen5, _pGen6, _pGen7, _pGen8;

	/**
	 * Init. the generator with a given seed value.
	 *
	 * @param generatorManager The generator manager
	 */
	public ChunkGenerator(World world) {
		_pGen1 = new PerlinNoise(world.getSeed().hashCode());
		_pGen1.setOctaves(8);

		_pGen2 = new PerlinNoise(world.getSeed().hashCode() + 1);
		_pGen2.setOctaves(8);

		_pGen3 = new PerlinNoise(world.getSeed().hashCode() + 2);
		_pGen3.setOctaves(8);

		_pGen4 = new PerlinNoise(world.getSeed().hashCode() + 3);
		_pGen5 = new PerlinNoise(world.getSeed().hashCode() + 4);
		_pGen6 = new PerlinNoise(world.getSeed().hashCode() + 5);
		_pGen7 = new PerlinNoise(world.getSeed().hashCode() + 6);
		_pGen8 = new PerlinNoise(world.getSeed().hashCode() + 7);
	}

	/**
	 * Apply the generation process to the given chunk.
	 *
	 * @param c The chunk to generate/populate
	 */
	public void generate(Chunk c) {
	}
}
