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
 * @source 'noiseutils.h'
 */
public abstract class Utils {

    /**
     * Performs linear interpolation between two 8-bit channel values.
     *
     * @param channel0
     * @param channel1
     * @param alpha
     * @return
     */
    public static short BlendChannel(short channel0, short channel1, float alpha) {
        float c0 = (float) channel0 / 255.0f;
        float c1 = (float) channel1 / 255.0f;
        return (short) (((c1 * alpha) + (c0 * (1.0f - alpha))) * 255.0f);
    }

    /**
     * Performs linear interpolation between two colors and stores the result in out.
     *
     * @param color0
     * @param color1
     * @param alpha
     * @param out
     */
    public static void LinearInterpColor(Color color0, Color color1, float alpha, Color out) {
        out.alpha = BlendChannel(color0.alpha, color1.alpha, alpha);
        out.blue = BlendChannel(color0.blue, color1.blue, alpha);
        out.green = BlendChannel(color0.green, color1.green, alpha);
        out.red = BlendChannel(color0.red, color1.red, alpha);
    }

    /**
     * Unpacks a floating-point value into four bytes.  This function is
     * specific to Intel machines.  A portable version will come soon (I hope.)
     *
     * @param bytes
     * @param value
     * @return
     */
    public static byte[] UnpackFloat(byte[] bytes, float value) {
        int intBits = Float.floatToRawIntBits(value);
        return UnpackLittle32(bytes, intBits);
    }

    /**
     * Unpacks a 16-bit integer value into two bytes in BIG endian format.
     *
     * @param bytes
     * @param integer
     * @return
     */
    public static byte[] UnpackLittle16(byte[] bytes, short integer) {
        bytes[1] = (byte) ((integer & 0x00ff));
        bytes[0] = (byte) ((integer & 0xff00) >> 8);
        return bytes;
    }

    /**
     * Unpacks a 32-bit integer value into four bytes in BIG endian format.
     * 
     * @param bytes
     * @param integer
     * @return
     */
    public static byte[] UnpackLittle32(byte[] bytes, int integer) {
        bytes[3] = (byte) ((integer & 0x000000ff));
        bytes[2] = (byte) ((integer & 0x0000ff00) >> 8);
        bytes[1] = (byte) ((integer & 0x00ff0000) >> 16);
        bytes[0] = (byte) ((integer & 0xff000000) >> 24);
        return bytes;
    }
}
