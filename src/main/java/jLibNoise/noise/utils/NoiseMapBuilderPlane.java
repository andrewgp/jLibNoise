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
import jLibNoise.noise.Interp;
import jLibNoise.noise.model.Plane;

/**
 * @source 'noiseutils.h/cpp'
 */
public class NoiseMapBuilderPlane extends NoiseMapBuilder {

    // A flag specifying whether seamless tiling is enabled.
    private boolean m_isSeamlessEnabled;
    // Lower x boundary of the planar noise map, in units.
    private double m_lowerXBound;
    // Lower z boundary of the planar noise map, in units.
    private double m_lowerZBound;
    // Upper x boundary of the planar noise map, in units.
    private double m_upperXBound;
    // Upper z boundary of the planar noise map, in units.
    private double m_upperZBound;

    @Override
    public void Build() {
        if (m_upperXBound <= m_lowerXBound
                || m_upperZBound <= m_lowerZBound
                || m_destWidth <= 0
                || m_destHeight <= 0
                || m_pSourceModule == null
                || m_pDestNoiseMap == null) {
            throw new ExceptionInvalidParam();
        }

        // Resize the destination noise map so that it can store the new output values from the source model.
        m_pDestNoiseMap.SetSize(m_destWidth, m_destHeight);

        // Create the plane model.
        Plane planeModel = new Plane();
        planeModel.SetModule(m_pSourceModule);

        double xExtent = m_upperXBound - m_lowerXBound;
        double zExtent = m_upperZBound - m_lowerZBound;
        double xDelta = xExtent / (double) m_destWidth;
        double zDelta = zExtent / (double) m_destHeight;
        double xCur = m_lowerXBound;
        double zCur = m_lowerZBound;

        // Fill every point in the noise map with the output values from the model.
        ArrayPointer.NativeFloatPrim pDest = m_pDestNoiseMap.GetSlabPtr(0);
        for (int z = 0; z < m_destHeight; z++) {
            xCur = m_lowerXBound;
            for (int x = 0; x < m_destWidth; x++) {
                float finalValue;
                if (!m_isSeamlessEnabled) {
                    finalValue = (float) planeModel.GetValue(xCur, zCur);
                } else {
                    double swValue, seValue, nwValue, neValue;
                    swValue = planeModel.GetValue(xCur, zCur);
                    seValue = planeModel.GetValue(xCur + xExtent, zCur);
                    nwValue = planeModel.GetValue(xCur, zCur + zExtent);
                    neValue = planeModel.GetValue(xCur + xExtent, zCur + zExtent);
                    double xBlend = 1.0 - ((xCur - m_lowerXBound) / xExtent);
                    double zBlend = 1.0 - ((zCur - m_lowerZBound) / zExtent);
                    double z0 = Interp.LinearInterp(swValue, seValue, xBlend);
                    double z1 = Interp.LinearInterp(nwValue, neValue, xBlend);
                    finalValue = (float) Interp.LinearInterp(z0, z1, zBlend);
                }
                pDest.floatAssignThenIncrementPosition(finalValue);
                xCur += xDelta;
            }
            zCur += zDelta;
            if (m_pCallback != null) {
                m_pCallback.callback(z);
            }
        }
    }

    /**
     * Enables or disables seamless tiling.
     * <p/>
     * Enabling seamless tiling builds a noise map with no seams at the
     * edges.  This allows the noise map to be tileable.
     *
     * @param enable A flag that enables or disables seamless tiling.
     */
    public void EnableSeamless(boolean enable) {
        m_isSeamlessEnabled = enable;
    }

    public void EnableSeamless() {
        EnableSeamless(true);
    }

    /**
     * Returns the lower x boundary of the planar noise map.
     *
     * @return The lower x boundary of the planar noise map, in units.
     */
    public double GetLowerXBound() {
        return m_lowerXBound;
    }

    /**
     * Returns the lower z boundary of the planar noise map.
     *
     * @return The lower z boundary of the noise map, in units.
     */
    public double GetLowerZBound() {
        return m_lowerZBound;
    }

    /**
     * Returns the upper x boundary of the planar noise map.
     *
     * @return The upper x boundary of the noise map, in units.
     */
    public double GetUpperXBound() {
        return m_upperXBound;
    }

    /**
     * Returns the upper z boundary of the planar noise map.
     *
     * @return The upper z boundary of the noise map, in units.
     */
    public double GetUpperZBound() {
        return m_upperZBound;
    }

    /**
     * Determines if seamless tiling is enabled.
     * <p/>
     * Enabling seamless tiling builds a noise map with no seams at the
     * edges.  This allows the noise map to be tileable.
     *
     * @return true if seamless tiling is enabled.
     */
    public boolean IsSeamlessEnabled() {
        return m_isSeamlessEnabled;
    }

    /**
     * Sets the boundaries of the planar noise map.
     *
     * @param lowerXBound The lower x boundary of the noise map, in units.
     * @param upperXBound upperXBound The upper x boundary of the noise map, in units.
     * @param lowerZBound The lower z boundary of the noise map, in units.
     * @param upperZBound The upper z boundary of the noise map, in units.
     * @throws ExceptionInvalidParam See the preconditions.
     * @pre The lower x boundary is less than the upper x boundary.
     * @pre The lower z boundary is less than the upper z boundary.
     */
    public void SetBounds(double lowerXBound, double upperXBound, double lowerZBound, double upperZBound) {
        if (lowerXBound >= upperXBound || lowerZBound >= upperZBound) {
            throw new ExceptionInvalidParam();
        }

        m_lowerXBound = lowerXBound;
        m_upperXBound = upperXBound;
        m_lowerZBound = lowerZBound;
        m_upperZBound = upperZBound;
    }
}
