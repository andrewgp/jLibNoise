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

import jLibNoise.noise.module.Module;

/**
 * Abstract base class for a noise-map builder
 * <p/>
 * A builder class builds a noise map by filling it with coherent-noise
 * values generated from the surface of a three-dimensional mathematical
 * object.  Each builder class defines a specific three-dimensional
 * surface, such as a cylinder, sphere, or plane.
 * <p/>
 * A builder class describes these input values using a coordinate system
 * applicable for the mathematical object (e.g., a latitude/longitude
 * coordinate system for the spherical noise-map builder.)  It then
 * "flattens" these coordinates onto a plane so that it can write the
 * coherent-noise values into a two-dimensional noise map.
 * <p/>
 * <b>Building the Noise Map</b>
 * <p/>
 * To build the noise map, perform the following steps:
 * - Pass the bounding coordinates to the SetBounds() method.
 * - Pass the noise map size, in points, to the SetDestSize() method.
 * - Pass a NoiseMap object to the SetDestNoiseMap() method.
 * - Pass a noise module (derived from noise::module::Module) to the
 * SetSourceModule() method.
 * - Call the Build() method.
 * <p/>
 * You may also pass a callback function to the SetCallback() method.
 * The Build() method calls this callback function each time it fills a
 * row of the noise map with coherent-noise values.  This callback
 * function has a single integer parameter that contains a count of the
 * rows that have been completed.  It returns void.
 * <p/>
 * Note that SetBounds() is not defined in the abstract base class; it is
 * only defined in the derived classes.  This is because each model uses
 * a different coordinate system.
 *
 * @source 'noiseutils.h/cpp'
 */
public abstract class NoiseMapBuilder {

    /**
     * The callback function that Build() calls each time it fills a row
     * of the noise map with coherent-noise values.
     * <p/>
     * This callback function has a single integer parameter that
     * contains a count of the rows that have been completed.  It returns
     * void.  Pass a function with this signature to the SetCallback() method.
     */
    protected NoiseMapCallback callback;
    // Height of the destination noise map, in points.
    protected int destHeight;
    // Width of the destination noise map, in points.
    protected int destWidth;
    // Destination noise map that will contain the coherent-noise values.
    protected NoiseMap destNoiseMap;
    // Source noise module that will generate the coherent-noise values.
    protected Module sourceModule;

    /**
     * Builds the noise map.
     * <p/>
     * If this method is successful, the destination noise map contains
     * the coherent-noise values from the noise module specified by SetSourceModule().
     *
     * @throws jLibNoise.noise.ExceptionInvalidParam
     *          See the preconditions.
     * @throws jLibNoise.noise.ExceptionOutOfMemory
     *          Out of memory.
     * @pre SetBounds() was previously called.
     * @pre SetDestNoiseMap() was previously called.
     * @pre SetSourceModule() was previously called.
     * @pre The width and height values specified by SetDestSize() are positive.
     * @pre The width and height values specified by SetDestSize() do not
     * exceed the maximum possible width and height for the noise map.
     * @post The original contents of the destination noise map is destroyed.
     */
    public abstract void build();

    /**
     * Returns the height of the destination noise map.
     * <p/>
     * This object does not change the height in the destination noise
     * map object until the Build() method is called.
     *
     * @return The height of the destination noise map, in points.
     */
    public double getDestHeight() {
        return destHeight;
    }

    /**
     * Returns the width of the destination noise map.
     * <p/>
     * This object does not change the height in the destination noise
     * map object until the Build() method is called.
     *
     * @return The width of the destination noise map, in points.
     */
    public double getDestWidth() {
        return destWidth;
    }

    /**
     * Sets the callback function that Build() calls each time it fills a
     * row of the noise map with coherent-noise values.
     * <p/>
     * This callback function has a single integer parameter that
     * contains a count of the rows that have been completed.  It returns
     * void.  Pass a function with this signature to the SetCallback() method.
     *
     * @param pCallback The callback function.
     */
    public void setCallback(NoiseMapCallback callback) {
        this.callback = callback;
    }

    /**
     * Sets the destination noise map.
     * <p/>
     * The destination noise map will contain the coherent-noise values
     * from this noise map after a successful call to the Build() method.
     * <p/>
     * The destination noise map must exist throughout the lifetime of
     * this object unless another noise map replaces that noise map.
     *
     * @param destNoiseMap The destination noise map.
     */
    public void setDestNoiseMap(NoiseMap destNoiseMap) {
        this.destNoiseMap = destNoiseMap;
    }

    /**
     * Sets the source module.
     * <p/>
     * This object fills in a noise map with the coherent-noise values from this source module.
     * <p/>
     * The source module must exist throughout the lifetime of this
     * object unless another noise module replaces that noise module.
     *
     * @param sourceModule The source module.
     */
    public void setSourceModule(Module sourceModule) {
        this.sourceModule = sourceModule;
    }

    /**
     * Sets the size of the destination noise map.
     * <p/>
     * This method does not change the size of the destination noise map until the Build() method is called.
     *
     * @param destWidth  The width of the destination noise map, in points.
     * @param destHeight The height of the destination noise map, in points.
     */
    public void setDestSize(int destWidth, int destHeight) {
        this.destWidth = destWidth;
        this.destHeight = destHeight;
    }
}
