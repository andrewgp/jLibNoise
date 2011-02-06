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
    private double m_eastLonBound;
    // Northern boundary of the spherical noise map, in degrees.
    private double m_northLatBound;
    // Southern boundary of the spherical noise map, in degrees.
    private double m_southLatBound;
    // Western boundary of the spherical noise map, in degrees.
    private double m_westLonBound;

    @Override
    public void Build() {
        if (m_eastLonBound <= m_westLonBound
                || m_northLatBound <= m_southLatBound
                || m_destWidth <= 0
                || m_destHeight <= 0
                || m_pSourceModule == null
                || m_pDestNoiseMap == null) {
            throw new ExceptionInvalidParam();
        }

        // Resize the destination noise map so that it can store the new output
        // values from the source model.
        m_pDestNoiseMap.SetSize(m_destWidth, m_destHeight);

        // Create the plane model.
        Sphere sphereModel = new Sphere();
        sphereModel.SetModule(m_pSourceModule);

        double lonExtent = m_eastLonBound - m_westLonBound;
        double latExtent = m_northLatBound - m_southLatBound;
        double xDelta = lonExtent / (double) m_destWidth;
        double yDelta = latExtent / (double) m_destHeight;
        double curLon = m_westLonBound;
        double curLat = m_southLatBound;

        // Fill every point in the noise map with the output values from the model.
        ArrayPointer.NativeFloatPrim pDest = m_pDestNoiseMap.GetSlabPtr(0);
        for (int y = 0; y < m_destHeight; y++) {
            curLon = m_westLonBound;
            for (int x = 0; x < m_destWidth; x++) {
                float curValue = (float) sphereModel.GetValue(curLat, curLon);
                pDest.floatAssignThenIncrementPosition(curValue);
                curLon += xDelta;
            }
            curLat += yDelta;
            if (m_pCallback != null) {
                m_pCallback.callback(y);
            }
        }
    }

    /**
     * Returns the eastern boundary of the spherical noise map.
     *
     * @return The eastern boundary of the noise map, in degrees.
     */
    public double GetEastLonBound() {
        return m_eastLonBound;
    }

    /**
     * Returns the northern boundary of the spherical noise map
     *
     * @return The northern boundary of the noise map, in degrees.
     */
    public double GetNorthLatBound() {
        return m_northLatBound;
    }

    /**
     * Returns the southern boundary of the spherical noise map
     *
     * @return The southern boundary of the noise map, in degrees.
     */
    public double GetSouthLatBound() {
        return m_southLatBound;
    }

    /**
     * Returns the western boundary of the spherical noise map
     *
     * @return The western boundary of the noise map, in degrees.
     */
    public double GetWestLonBound() {
        return m_westLonBound;
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
    public void SetBounds(double southLatBound, double northLatBound, double westLonBound, double eastLonBound) {
        if (southLatBound >= northLatBound || westLonBound >= eastLonBound) {
            throw new ExceptionInvalidParam();
        }

        m_southLatBound = southLatBound;
        m_northLatBound = northLatBound;
        m_westLonBound = westLonBound;
        m_eastLonBound = eastLonBound;
    }
}
