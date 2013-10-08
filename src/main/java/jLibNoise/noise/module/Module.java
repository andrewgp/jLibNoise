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
package jLibNoise.noise.module;

import jLibNoise.noise.ExceptionInvalidParam;
import jLibNoise.noise.ExceptionNoModule;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Abstract base class for noise modules.
 *
 * @source 'module/modulebase.h/cpp'
 */
public abstract class Module {

    // An array containing the pointers to each source module required by this noise module.
    protected Module[] sourceModule;

    public Module(int sourceModuleCount) {
        // Create an array of pointers to all source modules required by this
        // noise module.
        if (sourceModuleCount > 0) {
            sourceModule = new Module[sourceModuleCount];
        }
    }

    /**
     * Returns a reference to a source module connected to this noise module.
     * <p/>
     * Each noise module requires the attachment of a certain number of
     * source modules before an application can call the GetValue()
     * method.
     *
     * @param index The index value assigned to the source module.
     * @return A reference to the source module.
     * @throws ExceptionNoModule See the preconditions for more information.
     * @pre The index value ranges from 0 to one less than the number of
     * source modules required by this noise module.
     * @pre A source module with the specified index value has been added
     * to this noise module via a call to SetSourceModule().
     */
    public Module getSourceModule(int index) {
        assert (sourceModule != null);

        // The following fix was provided by Will Hawkins:
        // m_pSourceModule[index] != NULL
        // was incorrect; it should be:
        // m_pSourceModule[index] == NULL
        if (index >= getSourceModuleCount() || index < 0 || sourceModule[index] == null) {
            throw new ExceptionNoModule();
        }
        return sourceModule[index];
    }

    /**
     * Returns the number of source modules required by this noise
     * module.
     *
     * @return The number of source modules required by this noise module.
     */
    public int getSourceModuleCount() {
        throw new NotImplementedException();
    }

    /**
     * Generates an output value given the coordinates of the specified
     * input value.
     * <p/>
     * Before an application can call this method, it must first connect
     * all required source modules via the SetSourceModule() method.  If
     * these source modules are not connected to this noise module, this
     * method raises a debug assertion.
     * <p/>
     * To determine the number of source modules required by this noise
     * module, call the GetSourceModuleCount() method.
     *
     * @param x The @a x coordinate of the input value.
     * @param y The @a y coordinate of the input value.
     * @param z The @a z coordinate of the input value.
     * @return The output value.
     * @pre All source modules required by this noise module have been
     * passed to the SetSourceModule() method.
     */
    public abstract double getValue(double x, double y, double z);

    /**
     * Connects a source module to this noise module.
     *
     * A noise module mathematically combines the output values from the
     * source modules to generate the value returned by GetValue().
     *
     * The index value to assign a source module is a unique identifier
     * for that source module.  If an index value has already been
     * assigned to a source module, this noise module replaces the old
     * source module with the new source module.
     *
     * Before an application can call the GetValue() method, it must
     * first connect all required source modules.  To determine the
     * number of source modules required by this noise module, call the
     * GetSourceModuleCount() method.
     *
     * This source module must exist throughout the lifetime of this
     * noise module unless another source module replaces that source
     * module.
     *
     * A noise module does not modify a source module; it only modifies
     * its output values.
     * @param index An index value to assign to this source module.
     * @param sourceModule The source module to attach.
     * @throws ExceptionInvalidParam An invalid parameter was
     * specified; see the preconditions for more information.
     * @pre The index value ranges from 0 to one less than the number of
     * source modules required by this noise module.
     */
    public void setSourceModule(int index, Module sourceModule) {
        assert (this.sourceModule != null);
        if (index >= getSourceModuleCount() || index < 0) {
            throw new ExceptionInvalidParam();
        }
        this.sourceModule[index] = sourceModule;
    }
}
