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

package jLibNoise.noise.utils;

/**
 * Defines a color.
 * <p/>
 * A color object contains four 8-bit channels: red, green, blue, and an
 * alpha (transparency) channel.  Channel values range from 0 to 255.
 * <p/>
 * The alpha channel defines the transparency of the color.  If the alpha
 * channel has a value of 0, the color is completely transparent. If the
 * alpha channel has a value of 255, the color is completely opaque.
 *
 * @source 'noiseutils.h'
 */
public class Color {
    // Value of the alpha (transparency) channel.
    short alpha;

    // Value of the blue channel.
    short blue;

    // Value of the green channel.
    short green;

    // Value of the red channel.
    short red;

    public Color() {
    }

    public Color(Color copy) {
        alpha = copy.alpha;
        red = copy.red;
        green = copy.green;
        blue = copy.blue;
    }

    /**
     * @param r Value of the red channel.
     * @param g Value of the green channel.
     * @param b Value of the blue channel.
     * @param a Value of the alpha (transparency) channel.
     */
    public Color(short r, short g, short b, short a) {
        if (r > 255)
            r = 255;
        if (g > 255)
            g = 255;
        if (b > 255)
            b = 255;
        if (a > 255)
            a = 255;

        if (r < 0)
            r = 0;
        if (g < 0)
            g = 0;
        if (b < 0)
            b = 0;
        if (a < 0)
            a = 0;

        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
    }

    public Color(int r, int g, int b, int a) {
        this((short) r, (short) g, (short) b, (short) a);
    }
}
