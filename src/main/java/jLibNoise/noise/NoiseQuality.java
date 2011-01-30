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
 * Enumerates the noise quality.
 *
 * @source 'noisegen.h'
 */
public enum NoiseQuality {

    /**
     * Generates coherent noise quickly.  When a coherent-noise function with
     * this quality setting is used to generate a bump-map image, there are
     * noticeable "creasing" artifacts in the resulting image.  This is
     * because the derivative of that function is discontinuous at integer
     * boundaries.
     */
    QUALITY_FAST(0),

    /**
     * Generates standard-quality coherent noise.  When a coherent-noise
     * function with this quality setting is used to generate a bump-map
     * image, there are some minor "creasing" artifacts in the resulting
     * image.  This is because the second derivative of that function is
     * discontinuous at integer boundaries.
     */
    QUALITY_STD(1),

    /**
     * Generates the best-quality coherent noise.  When a coherent-noise
     * function with this quality setting is used to generate a bump-map
     * image, there are no "creasing" artifacts in the resulting image.  This
     * is because the first and second derivatives of that function are
     * continuous at integer boundaries.
     */
    QUALITY_BEST(2);

    int value;

    NoiseQuality(int value) {
        // NOTE: Do we need these?
        this.value = value;
    }

}
