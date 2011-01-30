/*
 * jNoiseLib [https://github.com/andrewgp/jLibNoise]
 * Original code from libnoise [https://github.com/andrewgp/jLibNoise]
 *
 * Copyright (C) 2003, 2004 Jason Bevins
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License (COPYING.txt) for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * The developer's email is jlbezigvins@gmzigail.com (for great email, take
 * off every 'zig'.)
 */

package jLibNoise.noise;

/**
 * @source 'noisegen.h/cpp'
 */
public class NoiseGen {

    // Constants used by the current version of libnoise.
    public static final int X_NOISE_GEN = 1619;
    public static final int Y_NOISE_GEN = 31337;
    public static final int Z_NOISE_GEN = 6971;
    public static final int SEED_NOISE_GEN = 1013;
    public static final int SHIFT_NOISE_GEN = 8;

    /**
     * Generates a gradient-coherent-noise value from the coordinates of a
     * three-dimensional input value.
     * <p/>
     * The return value ranges from -1.0 to +1.0.
     * <p/>
     * For an explanation of the difference between <i>gradient</i> noise and
     * <i>value</i> noise, see the comments for the GradientNoise3D() function.
     *
     * @param x            The @a x coordinate of the input value.
     * @param y            The @a y coordinate of the input value.
     * @param z            The @a z coordinate of the input value.
     * @param seed         The random number seed.
     * @param noiseQuality The quality of the coherent-noise.
     * @return The generated gradient-coherent-noise value.
     */
    public static double GradientCoherentNoise3D(double x, double y, double z, int seed, NoiseQuality noiseQuality) {
        // Create a unit-length cube aligned along an integer boundary.  This cube
        // surrounds the input point.
        int x0 = (x > 0.0 ? (int) x : (int) x - 1);
        int x1 = x0 + 1;
        int y0 = (y > 0.0 ? (int) y : (int) y - 1);
        int y1 = y0 + 1;
        int z0 = (z > 0.0 ? (int) z : (int) z - 1);
        int z1 = z0 + 1;

        // Map the difference between the coordinates of the input value and the
        // coordinates of the cube's outer-lower-left vertex onto an S-curve.
        double xs = 0, ys = 0, zs = 0;
        switch (noiseQuality) {
            case QUALITY_FAST:
                xs = (x - (double) x0);
                ys = (y - (double) y0);
                zs = (z - (double) z0);
                break;
            case QUALITY_STD:
                xs = Interp.SCurve3(x - (double) x0);
                ys = Interp.SCurve3(y - (double) y0);
                zs = Interp.SCurve3(z - (double) z0);
                break;
            case QUALITY_BEST:
                xs = Interp.SCurve5(x - (double) x0);
                ys = Interp.SCurve5(y - (double) y0);
                zs = Interp.SCurve5(z - (double) z0);
                break;
        }

        // Now calculate the noise values at each vertex of the cube.  To generate
        // the coherent-noise value at the input point, interpolate these eight
        // noise values using the S-curve value as the interpolant (trilinear
        // interpolation.)
        double n0, n1, ix0, ix1, iy0, iy1;
        n0 = GradientNoise3D(x, y, z, x0, y0, z0, seed);
        n1 = GradientNoise3D(x, y, z, x1, y0, z0, seed);
        ix0 = Interp.LinearInterp(n0, n1, xs);
        n0 = GradientNoise3D(x, y, z, x0, y1, z0, seed);
        n1 = GradientNoise3D(x, y, z, x1, y1, z0, seed);
        ix1 = Interp.LinearInterp(n0, n1, xs);
        iy0 = Interp.LinearInterp(ix0, ix1, ys);
        n0 = GradientNoise3D(x, y, z, x0, y0, z1, seed);
        n1 = GradientNoise3D(x, y, z, x1, y0, z1, seed);
        ix0 = Interp.LinearInterp(n0, n1, xs);
        n0 = GradientNoise3D(x, y, z, x0, y1, z1, seed);
        n1 = GradientNoise3D(x, y, z, x1, y1, z1, seed);
        ix1 = Interp.LinearInterp(n0, n1, xs);
        iy1 = Interp.LinearInterp(ix0, ix1, ys);

        return Interp.LinearInterp(iy0, iy1, zs);
    }

    public static double GradientCoherentNoise3D(double x, double y, double z) {
        return GradientCoherentNoise3D(x, y, z, 0, NoiseQuality.QUALITY_STD);
    }

    public static double GradientCoherentNoise3D(double x, double y, double z, int seed) {
        return GradientCoherentNoise3D(x, y, z, seed, NoiseQuality.QUALITY_STD);
    }

    /**
     * Generates a gradient-noise value from the coordinates of a
     * three-dimensional input value and the integer coordinates of a
     * nearby three-dimensional value.
     * <p/>
     * A <i>gradient</i>-noise function generates better-quality noise than a
     * <i>value</i>-noise function.  Most noise modules use gradient noise for
     * this reason, although it takes much longer to calculate.
     * <p/>
     * The return value ranges from -1.0 to +1.0.
     * <p/>
     * This function generates a gradient-noise value by performing the
     * following steps:
     * - It first calculates a random normalized vector based on the
     * nearby integer value passed to this function.
     * - It then calculates a new value by adding this vector to the
     * nearby integer value passed to this function.
     * - It then calculates the dot product of the above-generated value
     * and the floating-point input value passed to this function.
     * <p/>
     * A noise function differs from a random-number generator because it
     * always returns the same output value if the same input value is passed to it.
     *
     * @param fx   The floating-point @a x coordinate of the input value.
     * @param fy   The floating-point @a y coordinate of the input value.
     * @param fz   The floating-point @a z coordinate of the input value.
     * @param ix   The integer @a x coordinate of a nearby value.
     * @param iy   The integer @a y coordinate of a nearby value.
     * @param iz   The integer @a z coordinate of a nearby value.
     * @param seed The random number seed.
     * @return The generated gradient-noise value.
     * @pre The difference between @a fx and @a ix must be less than or equal to one.
     * @pre The difference between @a fy and @a iy must be less than or equal to one.
     * @pre The difference between @a fz and @a iz must be less than or equal to one.
     */
    public static double GradientNoise3D(double fx, double fy, double fz, int ix, int iy, int iz, int seed) {
        // Randomly generate a gradient vector given the integer coordinates of the
        // input value.  This implementation generates a random number and uses it
        // as an index into a normalized-vector lookup table.
        int vectorIndex = (
                X_NOISE_GEN * ix
                        + Y_NOISE_GEN * iy
                        + Z_NOISE_GEN * iz
                        + SEED_NOISE_GEN * seed)
                & 0xffffffff;
        vectorIndex ^= (vectorIndex >> SHIFT_NOISE_GEN);
        vectorIndex &= 0xff;

        double xvGradient = Vectortable.g_randomVectors[(vectorIndex << 2)];
        double yvGradient = Vectortable.g_randomVectors[(vectorIndex << 2) + 1];
        double zvGradient = Vectortable.g_randomVectors[(vectorIndex << 2) + 2];

        // Set up us another vector equal to the distance between the two vectors
        // passed to this function.
        double xvPoint = (fx - (double) ix);
        double yvPoint = (fy - (double) iy);
        double zvPoint = (fz - (double) iz);

        // Now compute the dot product of the gradient vector with the distance
        // vector.  The resulting value is gradient noise.  Apply a scaling value
        // so that this noise value ranges from -1.0 to 1.0.
        return ((xvGradient * xvPoint)
                + (yvGradient * yvPoint)
                + (zvGradient * zvPoint)) * 2.12;
    }

    public static double GradientNoise3D(double fx, double fy, double fz, int ix, int iy, int iz) {
        return GradientNoise3D(fx, fy, fz, ix, iy, iz, 0);
    }

    /**
     * Generates an integer-noise value from the coordinates of a
     * three-dimensional input value.
     * <p/>
     * The return value ranges from 0 to 2147483647.
     * <p/>
     * A noise function differs from a random-number generator because it
     * always returns the same output value if the same input value is passed
     * to it.
     *
     * @param x    The integer @a x coordinate of the input value.
     * @param y    The integer @a y coordinate of the input value.
     * @param z    The integer @a z coordinate of the input value.
     * @param seed A random number seed.
     * @return The generated integer-noise value.
     */
    public static int IntValueNoise3D(int x, int y, int z, int seed) {
        // All constants are primes and must remain prime in order for this noise
        // function to work correctly.
        int n = (
                X_NOISE_GEN * x
                        + Y_NOISE_GEN * y
                        + Z_NOISE_GEN * z
                        + SEED_NOISE_GEN * seed)
                & 0x7fffffff;
        n = (n >> 13) ^ n;
        return (n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff;
    }

    public static int IntValueNoise3D(int x, int y, int z) {
        return IntValueNoise3D(x, y, z, 0);
    }

    /**
     * Modifies a floating-point value so that it can be stored in a
     * noise::int32 variable.
     * <p/>
     * This function does not modify @a n.
     * <p/>
     * In libnoise, the noise-generating algorithms are all integer-based;
     * they use variables of type noise::int32.  Before calling a noise
     * function, pass the @a x, @a y, and @a z coordinates to this function to
     * ensure that these coordinates can be cast to a noise::int32 value.
     * <p/>
     * Although you could do a straight cast from double to noise::int32, the
     * resulting value may differ between platforms.  By using this function,
     * you ensure that the resulting value is identical between platforms.
     *
     * @param n A floating-point number.
     * @return The modified floating-point number.
     */
    public static double MakeInt32Range(double n) {
//        if (n >= 1073741824.0) {
//            return (2.0 * fmod(n, 1073741824.0)) - 1073741824.0;
//        } else if (n <= -1073741824.0) {
//            return (2.0 * fmod(n, 1073741824.0)) + 1073741824.0;
//        } else {
//            return n;
//        }
        return n;   // should not be required for java
    }

    /// Generates a value-coherent-noise value from the coordinates of a
    /// three-dimensional input value.
    ///
    /// @param x The @a x coordinate of the input value.
    /// @param y The @a y coordinate of the input value.
    /// @param z The @a z coordinate of the input value.
    /// @param seed The random number seed.
    /// @param noiseQuality The quality of the coherent-noise.
    ///
    /// @returns The generated value-coherent-noise value.
    ///
    /// The return value ranges from -1.0 to +1.0.
    ///
    /// For an explanation of the difference between <i>gradient</i> noise and
    /// <i>value</i> noise, see the comments for the GradientNoise3D() function.
    public static double ValueCoherentNoise3D(double x, double y, double z, int seed, NoiseQuality noiseQuality) {
        // Create a unit-length cube aligned along an integer boundary.  This cube
        // surrounds the input point.
        int x0 = (x > 0.0 ? (int) x : (int) x - 1);
        int x1 = x0 + 1;
        int y0 = (y > 0.0 ? (int) y : (int) y - 1);
        int y1 = y0 + 1;
        int z0 = (z > 0.0 ? (int) z : (int) z - 1);
        int z1 = z0 + 1;

        // Map the difference between the coordinates of the input value and the
        // coordinates of the cube's outer-lower-left vertex onto an S-curve.
        double xs = 0, ys = 0, zs = 0;
        switch (noiseQuality) {
            case QUALITY_FAST:
                xs = (x - (double) x0);
                ys = (y - (double) y0);
                zs = (z - (double) z0);
                break;
            case QUALITY_STD:
                xs = Interp.SCurve3(x - (double) x0);
                ys = Interp.SCurve3(y - (double) y0);
                zs = Interp.SCurve3(z - (double) z0);
                break;
            case QUALITY_BEST:
                xs = Interp.SCurve5(x - (double) x0);
                ys = Interp.SCurve5(y - (double) y0);
                zs = Interp.SCurve5(z - (double) z0);
                break;
        }

        // Now calculate the noise values at each vertex of the cube.  To generate
        // the coherent-noise value at the input point, interpolate these eight
        // noise values using the S-curve value as the interpolant (trilinear
        // interpolation.)
        double n0, n1, ix0, ix1, iy0, iy1;
        n0 = ValueNoise3D(x0, y0, z0, seed);
        n1 = ValueNoise3D(x1, y0, z0, seed);
        ix0 = Interp.LinearInterp(n0, n1, xs);
        n0 = ValueNoise3D(x0, y1, z0, seed);
        n1 = ValueNoise3D(x1, y1, z0, seed);
        ix1 = Interp.LinearInterp(n0, n1, xs);
        iy0 = Interp.LinearInterp(ix0, ix1, ys);
        n0 = ValueNoise3D(x0, y0, z1, seed);
        n1 = ValueNoise3D(x1, y0, z1, seed);
        ix0 = Interp.LinearInterp(n0, n1, xs);
        n0 = ValueNoise3D(x0, y1, z1, seed);
        n1 = ValueNoise3D(x1, y1, z1, seed);
        ix1 = Interp.LinearInterp(n0, n1, xs);
        iy1 = Interp.LinearInterp(ix0, ix1, ys);
        return Interp.LinearInterp(iy0, iy1, zs);
    }

    public static double ValueCoherentNoise3D(double x, double y, double z, int seed) {
        return ValueCoherentNoise3D(x, y, z, seed, NoiseQuality.QUALITY_STD);
    }

    public static double ValueCoherentNoise3D(double x, double y, double z) {
        return ValueCoherentNoise3D(x, y, z, 0, NoiseQuality.QUALITY_STD);
    }

    /**
     * Generates a value-noise value from the coordinates of a three-dimensional input value.
     * <p/>
     * The return value ranges from -1.0 to +1.0.
     * <p/>
     * A noise function differs from a random-number generator because it
     * always returns the same output value if the same input value is passed
     * to it.
     *
     * @param x    The @a x coordinate of the input value.
     * @param y    The @a y coordinate of the input value.
     * @param z    The @a z coordinate of the input value.
     * @param seed A random number seed.
     * @return The generated value-noise value.
     */
    public static double ValueNoise3D(int x, int y, int z, int seed) {
        return 1.0 - ((double) IntValueNoise3D(x, y, z, seed) / 1073741824.0);
    }

    public static double ValueNoise3D(int x, int y, int z) {
        return ValueNoise3D(x, y, z, 0);
    }
}
