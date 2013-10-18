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
 *
 */
public class ArrayPointer<T> implements Pointer<T> {

    protected T[] array;
    protected int position;

    public ArrayPointer(T[] array, int position) {
        this.array = array;
        this.position = position;
    }

    public ArrayPointer(T[] array) {
        this(array, 0);
    }

    public void assignThenIncrementPosition(T value) {
        array[position] = value;
        position++;
    }

    public T get() {
        return array[position];
    }

    public void increment() {
        position++;
    }

    public int getSize() {
        return array.length;
    }

    public void copyTo(ArrayPointer<T> dest) {
        System.arraycopy(array, position, dest.array, position, getSize() - position);
    }
    
    public T[] getRaw() {
        return array;
    }

    public static class NativeFloatPrim {

        protected float[] array;
        protected int position;

        public NativeFloatPrim(float[] array, int position) {
            this.array = array;
            this.position = position;
        }

        public void floatAssignThenIncrementPosition(float value) {
            array[position] = value;
            position++;
        }

        public float get() {
            return array[position];
        }

        public float get(int offset) {
            return array[position + offset];
        }

        public void increment() {
            position++;
        }

        public void copyTo(NativeFloatPrim dest, int size) {
            System.arraycopy(array, position, dest, position, size);
        }
    }
    
}
