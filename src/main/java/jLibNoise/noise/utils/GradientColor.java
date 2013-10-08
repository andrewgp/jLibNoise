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
import jLibNoise.noise.Misc;

/**
 * Defines a color gradient.
 * <p/>
 * A color gradient is a list of gradually-changing colors.  A color
 * gradient is defined by a list of <i>gradient points</i>.  Each
 * gradient point has a position and a color.  In a color gradient, the
 * colors between two adjacent gradient points are linearly interpolated.
 * <p/>
 * To add a gradient point to the color gradient, pass its position and
 * color to the AddGradientPoint() method.
 * <p/>
 * To retrieve a color from a specific position in the color gradient,
 * pass that position to the GetColor() method.
 * <p/>
 * This class is a useful tool for coloring height maps based on
 * elevation.
 * <p/>
 * <b>Gradient example</b>
 * <p/>
 * Suppose a gradient object contains the following gradient points:
 * <p/>
 * - -1.0 maps to black.
 * - 0.0 maps to white.
 * - 1.0 maps to red.
 * <p/>
 * If an application passes -0.5 to the GetColor() method, this method
 * will return a gray color that is halfway between black and white.
 * <p/>
 * If an application passes 0.25 to the GetColor() method, this method
 * will return a very light pink color that is one quarter of the way
 * between white and red.
 *
 * @source 'noiseutils.h/cpp'
 */
public class GradientColor {

    // Number of gradient points.
    int gradientPointCount;

    /// Array that stores the gradient points.
    GradientPoint[] gradientPoints;

    // A color object that is used by a gradient object to store a temporary value.
    /*mutable*/
    Color workingColor;

    public GradientColor() {
        workingColor = new Color();
    }

    /**
     * Adds a gradient point to this gradient object.
     * <p/>
     * It does not matter which order these gradient points are added.
     *
     * @param gradientPos   The position of this gradient point.
     * @param gradientColor The color of this gradient point.
     * @throws :ExceptionInvalidParam See the precondition.
     * @pre No two gradient points have the same position.
     */
    public void addGradientPoint(double gradientPos, Color gradientColor) {
        // Find the insertion point for the new gradient point and insert the new
        // gradient point at that insertion point.  The gradient point array will
        // remain sorted by gradient position.
        int insertionPos = findInsertionPos(gradientPos);
        insertAtPos(insertionPos, gradientPos, gradientColor);
    }

    /**
     * Deletes all the gradient points from this gradient object.
     *
     * @post All gradient points from this gradient object are deleted.
     */
    public void clear() {
        gradientPoints = null;
        gradientPointCount = 0;
    }

    /**
     * Returns the color at the specified position in the color gradient.
     *
     * @param gradientPos The specified position.
     * @return The color at that position.
     */
    public Color getColor(double gradientPos) {
        assert (gradientPointCount >= 2);

        // Find the first element in the gradient point array that has a gradient
        // position larger than the gradient position passed to this method.
        int indexPos;
        for (indexPos = 0; indexPos < gradientPointCount; indexPos++) {
            if (gradientPos < gradientPoints[indexPos].pos) {
                break;
            }
        }

        // Find the two nearest gradient points so that we can perform linear
        // interpolation on the color.
        int index0 = Misc.clampValue(indexPos - 1, 0, gradientPointCount - 1);
        int index1 = Misc.clampValue(indexPos, 0, gradientPointCount - 1);

        // If some gradient points are missing (which occurs if the gradient
        // position passed to this method is greater than the largest gradient
        // position or less than the smallest gradient position in the array), get
        // the corresponding gradient color of the nearest gradient point and exit
        // now.
        if (index0 == index1) {
            workingColor = new Color(gradientPoints[index1].color);
            return workingColor;
        }

        // Compute the alpha value used for linear interpolation.
        double input0 = gradientPoints[index0].pos;
        double input1 = gradientPoints[index1].pos;
        double alpha = (gradientPos - input0) / (input1 - input0);

        // Now perform the linear interpolation given the alpha value.
        Color color0 = gradientPoints[index0].color;
        Color color1 = gradientPoints[index1].color;
        Utils.linearInterpColor(color0, color1, (float) alpha, workingColor);
        return workingColor;
    }

    /**
     * Returns a pointer to the array of gradient points in this object.
     * <p/>
     * Before calling this method, call GetGradientPointCount() to
     * determine the number of gradient points in this array.
     * <p/>
     * It is recommended that an application does not store this pointer
     * for later use since the pointer to the array may change if the
     * application calls another method of this object.
     *
     * @return A pointer to the array of gradient points.
     */
    public GradientPoint[] getGradientPointArray() {
        return gradientPoints;
    }

    /**
     * Returns the number of gradient points stored in this object.
     *
     * @return The number of gradient points stored in this object.
     */
    public int getGradientPointCount() {
        return gradientPointCount;
    }

    /**
     * Determines the array index in which to insert the gradient point
     * into the internal gradient-point array.
     * <p/>
     * By inserting the gradient point at the returned array index, this
     * object ensures that the gradient-point array is sorted by input
     * value.  The code that maps a value to a color requires a sorted
     * gradient-point array.
     *
     * @param gradientPos The position of this gradient point.
     * @return The array index in which to insert the gradient point.
     * @throws jLibNoise.noise.ExceptionInvalidParam
     *          See the precondition.
     * @pre No two gradient points have the same input value.
     */
    private int findInsertionPos(double gradientPos) {
        int insertionPos;
        for (insertionPos = 0; insertionPos < gradientPointCount;
             insertionPos++) {
            if (gradientPos < gradientPoints[insertionPos].pos) {
                // We found the array index in which to insert the new gradient point, exit now.
                break;
            } else if (gradientPos == gradientPoints[insertionPos].pos) {
                // Each gradient point is required to contain a unique gradient position, so throw an exception.
                throw new ExceptionInvalidParam();
            }
        }
        return insertionPos;
    }

    /**
     * Inserts the gradient point at the specified position in the
     * internal gradient-point array.
     * <p/>
     * To make room for this new gradient point, this method reallocates
     * the gradient-point array and shifts all gradient points occurring
     * after the insertion position up by one.
     * <p/>
     * Because this object requires that all gradient points in the array
     * must be sorted by the position, the new gradient point should be
     * inserted at the position in which the order is still preserved.
     *
     * @param insertionPos  The zero-based array position in which to insert the gradient point.
     * @param gradientPos   The position of this gradient point.
     * @param gradientColor The color of this gradient point.
     */
    private void insertAtPos(int insertionPos, double gradientPos, Color gradientColor) {
        // Make room for the new gradient point at the specified insertion position
        // within the gradient point array.  The insertion position is determined by
        // the gradient point's position; the gradient points must be sorted by
        // gradient position within that array.
        GradientPoint[] newGradientPoints = new GradientPoint[gradientPointCount + 1];
        newGradientPoints[gradientPointCount] = new GradientPoint();
        for (int i = 0; i < gradientPointCount; i++) {
            if (i < insertionPos) {
                newGradientPoints[i] = gradientPoints[i];
            } else {
                newGradientPoints[i + 1] = gradientPoints[i];
            }
        }
        gradientPoints = newGradientPoints;
        ++gradientPointCount;

        // Now that we've made room for the new gradient point within the array, add
        // the new gradient point.
        gradientPoints[insertionPos].pos = gradientPos;
        gradientPoints[insertionPos].color = gradientColor;
    }
}
