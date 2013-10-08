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

import jLibNoise.noise.ExceptionInvalidParam;
import jLibNoise.noise.model.Sphere;

/**
 * Builds a spherical noise map.
 * <p/>
 * This class builds a noise map by filling it with coherent-noise values
 * generated from the surface of a sphere.
 * <p/>
 * This class describes these input values using a (latitude, longitude)
 * coordinate system.  After generating the coherent-noise value from the
 * input value, it then "flattens" these coordinates onto a plane so that
 * it can write the values into a two-dimensional noise map.
 * <p/>
 * The sphere model has a radius of 1.0 unit.  Its center is at the
 * origin.
 * <p/>
 * The x coordinate in the noise map represents the longitude.  The y
 * coordinate in the noise map represents the latitude.
 * <p/>
 * The application must provide the southern, northern, western, and
 * eastern bounds of the noise map, in degrees.
 */
public class NoiseMapBuilderSphere extends NoiseMapBuilder {

    // Eastern boundary of the spherical noise map, in degrees.
    private double eastLonBound;
    // Northern boundary of the spherical noise map, in degrees.
    private double northLatBound;
    // Southern boundary of the spherical noise map, in degrees.
    private double southLatBound;
    // Western boundary of the spherical noise map, in degrees.
    private double westLonBound;

    @Override
    public void build() {
        if (eastLonBound <= westLonBound
                || northLatBound <= southLatBound
                || destWidth <= 0
                || destHeight <= 0
                || sourceModule == null
                || destNoiseMap == null) {
            throw new ExceptionInvalidParam();
        }

        // Resize the destination noise map so that it can store the new output
        // values from the source model.
        destNoiseMap.setSize(destWidth, destHeight);

        // Create the plane model.
        Sphere sphereModel = new Sphere();
        sphereModel.setModule(sourceModule);

        double lonExtent = eastLonBound - westLonBound;
        double latExtent = northLatBound - southLatBound;
        double xDelta = lonExtent / (double) destWidth;
        double yDelta = latExtent / (double) destHeight;
        double curLon = westLonBound;
        double curLat = southLatBound;

        // Fill every point in the noise map with the output values from the model.
        ArrayPointer.NativeFloatPrim pDest = destNoiseMap.getSlabPtr(0);
        for (int y = 0; y < destHeight; y++) {
            curLon = westLonBound;
            for (int x = 0; x < destWidth; x++) {
                float curValue = (float) sphereModel.getValue(curLat, curLon);
                pDest.floatAssignThenIncrementPosition(curValue);
                curLon += xDelta;
            }
            curLat += yDelta;
            if (callback != null) {
                callback.callback(y);
            }
        }
    }

    /**
     * Returns the eastern boundary of the spherical noise map.
     *
     * @return The eastern boundary of the noise map, in degrees.
     */
    public double getEastLonBound() {
        return eastLonBound;
    }

    /**
     * Returns the northern boundary of the spherical noise map
     *
     * @return The northern boundary of the noise map, in degrees.
     */
    public double getNorthLatBound() {
        return northLatBound;
    }

    /**
     * Returns the southern boundary of the spherical noise map
     *
     * @return The southern boundary of the noise map, in degrees.
     */
    public double getSouthLatBound() {
        return southLatBound;
    }

    /**
     * Returns the western boundary of the spherical noise map
     *
     * @return The western boundary of the noise map, in degrees.
     */
    public double getWestLonBound() {
        return westLonBound;
    }

    /**
     * Sets the coordinate boundaries of the noise map.
     *
     * @param southLatBound The southern boundary of the noise map, in degrees.
     * @param northLatBound The northern boundary of the noise map, in degrees.
     * @param westLonBound The western boundary of the noise map, in degrees.
     * @param eastLonBound The eastern boundary of the noise map, in degrees.
     * @pre The southern boundary is less than the northern boundary.
     * @pre The western boundary is less than the eastern boundary.
     * @throws ExceptionInvalidParam See the preconditions.
     */
    public void setBounds(double southLatBound, double northLatBound, double westLonBound, double eastLonBound) {
        if (southLatBound >= northLatBound || westLonBound >= eastLonBound) {
            throw new ExceptionInvalidParam();
        }

        southLatBound = southLatBound;
        northLatBound = northLatBound;
        westLonBound = westLonBound;
        eastLonBound = eastLonBound;
    }
}
