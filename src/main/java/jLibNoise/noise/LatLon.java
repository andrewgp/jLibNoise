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
 * @source 'latlon.h/cpp'
 */
public abstract class LatLon {

    /**
     * Converts latitude/longitude coordinates on a unit sphere into 3D
     * Cartesian coordinates.
     *
     * @param lat The latitude, in degrees.
     * @param lon lon The longitude, in degrees.
     * @return Returns the x,y,z coordinates.
     *
     * @pre lat must range from @b -90 to @b +90.
     * @pre lon must range from @b -180 to @b +180.
     */
    public static double[] latLonToXYZ(double lat, double lon) {
        double r = Math.cos(MathConst.DEG_TO_RAD * lat);
        double x, y, z;
        x = r * Math.cos(MathConst.DEG_TO_RAD * lon);
        y = Math.sin(MathConst.DEG_TO_RAD * lat);
        z = r * Math.sin(MathConst.DEG_TO_RAD * lon);
        return new double[] { x, y, z };
    }
}
