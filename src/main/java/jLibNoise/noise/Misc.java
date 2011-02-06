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
 * @source 'misc/h'
 */
public abstract class Misc {

    /**
     * Clamps a value onto a clamping range.
     *
     * @param value      The value to clamp.
     * @param lowerBound The lower bound of the clamping range.
     * @param upperBound The upper bound of the clamping range.
     * @return value if @a value lies between @a lowerBound and @a upperBound.
     *         lowerBound if @a value is less than @a lowerBound.
     *         upperBound if @a value is greater than @a upperBound.
     */
    public static int ClampValue(int value, int lowerBound, int upperBound) {
        if (value < lowerBound) {
            return lowerBound;
        } else if (value > upperBound) {
            return upperBound;
        } else {
            return value;
        }
    }

    /**
     * Returns the maximum of two values.
     * @param a The first value.
     * @param b The second value.
     * @param <T>
     * @return The maximum of the two values.
     */
    public static <T extends Comparable<T>> T GetMax(T a, T b)
    {
        return (a.compareTo(b) > 0 ? a : b);
    }
}
