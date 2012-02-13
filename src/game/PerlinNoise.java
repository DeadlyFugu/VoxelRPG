package game;
/**
 * Stolen from a Perlin noise sample applet by Bruno Augier.
 * Original header below.
 * 
 * Perlin sample Applet
 *
 * Sample 2D perlin noise generator with sample applet
 *
 * @author bruno augier
 * @email bruno.augier@dzzd.net
 * @website http://dzzd.net/
 * @version 1.00 2004/08/06
 * @updated 2007/11/28
 */

public class PerlinNoise {

	/**
	 * Brut noise generator using pseudo-random
	 */
	public static double noise(int x, int y) {
		x = x + y * 57;
		x = ((x << 13) ^ x);
		double t = (x * (x * x * 15731 + 789221) + 1376312589) & 0x7fffffff;
		return 1 - t * 0.000000000931322574615478515625;

	}

	/**
	 * Smoothed noise generator using 9 brut noise
	 */
	public static double sNoise(int x, int y) {
		double corners = (noise(x - 1, y - 1) + noise(x + 1, y - 1)
				+ noise(x - 1, y + 1) + noise(x + 1, y + 1)) * 0.0625;
		double sides = (noise(x - 1, y) + noise(x + 1, y) + noise(x, y - 1) + noise(
				x, y + 1)) * 0.125;
		double center = noise(x, y) * 0.25;
		return corners + sides + center;
	}

	/**
	 * Linear Interpolator
	 * 
	 * @param a
	 *            value 1
	 * @param b
	 *            value 2
	 * @param x
	 *            interpolator factor
	 * 
	 * @return value interpolated from a to b using x factor by linear
	 *         interpolation
	 */
	public static double lInterpoleLin(double a, double b, double x) {
		return a * (1 - x) + b * x;
	}

	/**
	 * Cosine Interpolator
	 * 
	 * @param a
	 *            value 1
	 * @param b
	 *            value 2
	 * @param x
	 *            interpolator factor
	 * 
	 * @return value interpolated from a to b using x factor by cosin
	 *         interpolation
	 */
	public static double lInterpoleCos(double a, double b, double x) {

		double ft = x * 3.1415927;
		double f = (1 - Math.cos(ft)) * .5;
		return a * (1 - f) + b * f;
	}

	/**
	 * Smooth noise generator with two input 2D <br>
	 * You may change the interpolation method : cosin , linear , cubic </br>
	 * 
	 * @param x
	 *            x parameter
	 * @param y
	 *            y parameter
	 * 
	 * @return value of smoothed noise for 2d value x,y
	 */
	public static double iNoise(double x, double y) {
		int iX = (int) x;
		int iY = (int) y;
		double dX = x - iX;
		double dY = y - iY;
		double p1 = sNoise(iX, iY);
		double p2 = sNoise(iX + 1, iY);
		double p3 = sNoise(iX, iY + 1);
		double p4 = sNoise(iX + 1, iY + 1);
		double i1 = lInterpoleLin(p1, p2, dX);
		double i2 = lInterpoleLin(p3, p4, dX);
		return lInterpoleLin(i1, i2, dY);
	}

	/**
	 * Perlin noise generator for two input 2D
	 * 
	 * @param x
	 *            x parameter
	 * @param y
	 *            y parameter
	 * @param octave
	 *            maximum octave/harmonic
	 * @param persistence
	 *            noise persitence
	 * @return perlin noise value for given entry
	 */
	public static double pNoise(double x, double y, double persistence, int octave) {
		double result;
		double amplitude = 1;
		int frequence = 1;
		result = 0;
		for (int n = 0; n < octave; n++) {
			result += iNoise(x * frequence, y * frequence) * amplitude;
			frequence <<= 1;
			amplitude *= persistence;
		}
		return result;
	}
}
