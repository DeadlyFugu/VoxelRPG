/*
* Copyright 2011 Benjamin Glatzel <benjamin.glatzel@me.com>.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package game;

/**
* Random number generator based on the Xorshift generator by George Marsaglia.
*
* @author Benjamin Glatzel <benjamin.glatzel@me.com>
*/
public class FastRandom {

    private long _seed = System.currentTimeMillis();

    /**
* Initializes a new instance of the random number generator using
* a specified seed.
*
* @param seed The seed to use
*/
    public FastRandom(long seed) {
        this._seed = seed;
    }

    /**
* Initializes a new instance of the random number generator using
* "System.currentTimeMillis()" as seed.
*/
    public FastRandom() {
    }

    /**
* Returns a random long value.
*
* @return Random value
*/
    long randomLong() {
        _seed ^= (_seed << 21);
        _seed ^= (_seed >>> 35);
        _seed ^= (_seed << 4);
        return _seed;
    }

    /**
* Returns a random int value.
*
* @return Random value
*/
    public int randomInt() {
        return (int) randomLong();
    }

    public int randomInt(int range) {
        return (int) randomLong() % range;
    }

    public int randomIntAbs() {
        return fastAbs(randomInt());
    }

    public int randomIntAbs(int range) {
        return fastAbs(randomInt() % range);
    }
    
    public int fastAbs(int i) {
    	return (i >= 0) ? i : -i;
    }
    
    public double fastAbs(double i) {
    	return (i >= 0) ? i : -i;
    }

    /**
* Returns a random double value.
*
* @return Random value
*/
    public double randomDouble() {
        return randomLong() / ((double) Long.MAX_VALUE - 1d);
    }

    /**
* Returns a random bool.
*
* @return Random value
*/
    public boolean randomBoolean() {
        return randomLong() > 0;
    }

    /**
* Returns a random character string with a specified length.
*
* @param length The length of the generated string
* @return Random character string
*/
    public String randomCharacterString(int length) {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < length / 2; i++) {
            s.append((char) ('a' + fastAbs(randomDouble()) * 26d));
            s.append((char) ('A' + fastAbs(randomDouble()) * 26d));
        }

        return s.toString();
    }

    /**
* Calculates a standardized normal distributed value (using the polar method).
*
* @return The value
*/
    public double standNormalDistrDouble() {

        double q = Double.MAX_VALUE;
        double u1 = 0;
        double u2;

        while (q >= 1d || q == 0) {
            u1 = randomDouble();
            u2 = randomDouble();

            q = Math.pow(u1, 2) + Math.pow(u2, 2);
        }

        double p = Math.sqrt((-2d * (Math.log(q))) / q);
        return u1 * p; // or u2 * p
    }
}