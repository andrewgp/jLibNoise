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
    private boolean isSeamlessEnabled;
    // Lower x boundary of the planar noise map, in units.
    private double lowerXBound;
    // Lower z boundary of the planar noise map, in units.
    private double lowerZBound;
    // Upper x boundary of the planar noise map, in units.
    private double upperXBound;
    // Upper z boundary of the planar noise map, in units.
    private double upperZBound;

    @Override
    public void build() {
        if (upperXBound <= lowerXBound
                || upperZBound <= lowerZBound
                || destWidth <= 0
                || destHeight <= 0
                || sourceModule == null
                || destNoiseMap == null) {
            throw new ExceptionInvalidParam();
        }

        // Resize the destination noise map so that it can store the new output values from the source model.
        destNoiseMap.setSize(destWidth, destHeight);

        // Create the plane model.
        Plane planeModel = new Plane();
        planeModel.setModule(sourceModule);

        double xExtent = upperXBound - lowerXBound;
        double zExtent = upperZBound - lowerZBound;
        double xDelta = xExtent / (double) destWidth;
        double zDelta = zExtent / (double) destHeight;
        double xCur;// = lowerXBound;
        double zCur = lowerZBound;

        // Fill every point in the noise map with the output values from the model.
        ArrayPointer.NativeFloatPrim pDest = destNoiseMap.getSlabPtr(0);
        for (int z = 0; z < destHeight; z++) {
            xCur = lowerXBound;
            for (int x = 0; x < destWidth; x++) {
                float finalValue;
                if (!isSeamlessEnabled) {
                    finalValue = (float) planeModel.getValue(xCur, zCur);
                } else {
                    double swValue, seValue, nwValue, neValue;
                    swValue = planeModel.getValue(xCur, zCur);
                    seValue = planeModel.getValue(xCur + xExtent, zCur);
                    nwValue = planeModel.getValue(xCur, zCur + zExtent);
                    neValue = planeModel.getValue(xCur + xExtent, zCur + zExtent);
                    double xBlend = 1.0 - ((xCur - lowerXBound) / xExtent);
                    double zBlend = 1.0 - ((zCur - lowerZBound) / zExtent);
                    double z0 = Interp.linearInterp(swValue, seValue, xBlend);
                    double z1 = Interp.linearInterp(nwValue, neValue, xBlend);
                    finalValue = (float) Interp.linearInterp(z0, z1, zBlend);
                }
                pDest.floatAssignThenIncrementPosition(finalValue);
                xCur += xDelta;
            }
            zCur += zDelta;
            if (callback != null) {
                callback.callback(z);
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
    public void enableSeamless(boolean enable) {
        isSeamlessEnabled = enable;
    }

    public void enableSeamless() {
        enableSeamless(true);
    }

    /**
     * Returns the lower x boundary of the planar noise map.
     *
     * @return The lower x boundary of the planar noise map, in units.
     */
    public double getLowerXBound() {
        return lowerXBound;
    }

    /**
     * Returns the lower z boundary of the planar noise map.
     *
     * @return The lower z boundary of the noise map, in units.
     */
    public double getLowerZBound() {
        return lowerZBound;
    }

    /**
     * Returns the upper x boundary of the planar noise map.
     *
     * @return The upper x boundary of the noise map, in units.
     */
    public double getUpperXBound() {
        return upperXBound;
    }

    /**
     * Returns the upper z boundary of the planar noise map.
     *
     * @return The upper z boundary of the noise map, in units.
     */
    public double getUpperZBound() {
        return upperZBound;
    }

    /**
     * Determines if seamless tiling is enabled.
     * <p/>
     * Enabling seamless tiling builds a noise map with no seams at the
     * edges.  This allows the noise map to be tileable.
     *
     * @return true if seamless tiling is enabled.
     */
    public boolean isSeamlessEnabled() {
        return isSeamlessEnabled;
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
    public void setBounds(double lowerXBound, double upperXBound, double lowerZBound, double upperZBound) {
        if (lowerXBound >= upperXBound || lowerZBound >= upperZBound) {
            throw new ExceptionInvalidParam();
        }

        this.lowerXBound = lowerXBound;
        this.upperXBound = upperXBound;
        this.lowerZBound = lowerZBound;
        this.upperZBound = upperZBound;
    }
}
