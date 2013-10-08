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
import jLibNoise.noise.ExceptionOutOfMemory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Implements an image, a 2-dimensional array of color values.
 * <p/>
 * An image can be used to store a color texture.
 * <p/>
 * These color values are of type Color.
 * <p/>
 * The size (width and height) of the image can be specified during
 * object construction or at any other time.
 * <p/>
 * The GetValue() and SetValue() methods can be used to access individual
 * color values stored in the image.
 * <p/>
 * This class manages its own memory.  If you copy an image object into
 * another image object, the original contents of the image object will
 * be freed.
 * <p/>
 * If you specify a new size for the image and the new size is smaller
 * than the current size, the allocated memory will not be reallocated.
 * Call ReclaimMem() to reclaim the wasted memory.
 * <p/>
 * <b>Border Values</b>
 * <p/>
 * All of the color values outside of the image are assumed to have a
 * common color value known as the <i>border value</i>.
 * <p/>
 * To set the border value, call the SetBorderValue() method.
 * <p/>
 * The GetValue() method returns the border value if the specified
 * position lies outside of the image.
 * <p/>
 * <b>Internal Image Structure</b>
 * <p/>
 * Internally, the color values are organized into horizontal rows called
 *
 * @a slabs.  Slabs are ordered from bottom to top.
 * <p/>
 * Each slab contains a contiguous row of color values in memory.  The
 * color values in a slab are organized left to right.  These values are
 * of type Color.
 * <p/>
 * The offset between the starting points of any two adjacent slabs is
 * called the <i>stride amount</i>.  The stride amount is measured by the
 * number of Color objects between these two starting points, not by the
 * number of bytes.  For efficiency reasons, the stride is often a
 * multiple of the machine word size.
 * <p/>
 * The GetSlabPtr() methods allow you to retrieve pointers to the slabs
 * themselves.
 */
public class Image {

    // The raster's stride length must be a multiple of this constant.
    public static final int RASTER_STRIDE_BOUNDARY = 4;
    public static final int RASTER_MAX_WIDTH = 32767;
    // The maximum height of a raster.
    public static final int RASTER_MAX_HEIGHT = 32767;

    // The Color value used for all positions outside of the image.
    private Color borderValue;
    // The current height of the image.
    private int height;
    /**
     * The amount of memory allocated for the image.
     * This value is equal to the number of Color objects allocated for the image, not the number of bytes.
     */
    long memUsed;
    // A pointer to the image buffer.
    Color[] image;
    // The stride amount of the image.
    private int stride;
    // The current width of the image.
    private int width;

    /**
     * Constructor.
     * Creates an empty image.
     */
    public Image() {
        initObj();
    }

    /**
     * Constructor.
     * <p/>
     * Creates an image with uninitialized color values.
     * It is considered an error if the specified dimensions are not positive.
     *
     * @param width  The width of the new image.
     * @param height The height of the new image.
     * @throws jLibNoise.noise.ExceptionInvalidParam
     *          See the preconditions.
     * @throws jLibNoise.noise.ExceptionOutOfMemory
     *          Out of memory.
     * @pre The width and height values are positive.
     * @pre The width and height values do not exceed the maximum possible width and height for the image.
     */
    public Image(int width, int height) {
        initObj();
        setSize(width, height);
    }

    /**
     * Copy constructor.
     *
     * @param rhs Image to copy
     * @throws jLibNoise.noise.ExceptionOutOfMemory
     *          Out of memory.
     */
    public Image(Image rhs) {
        initObj();
        copyImage(rhs);
    }

    /**
     * Clears the image to a specified color value.
     *
     * @param value The color value that all positions within the image are cleared to.
     */
    public void clear(Color value) {
        for (int i = 0; i < height * width; i++) {
            image[i] = value;
        }
    }

    /**
     * Returns the color value used for all positions outside of the image.
     * <p/>
     * All positions outside of the image are assumed to have a common
     * color value known as the <i>border value</i>.
     *
     * @return The color value used for all positions outside of the image.
     */
    public Color getBorderValue() {
        return borderValue;
    }

    /**
     * Returns a const pointer to a slab.
     *
     * @return A const pointer to a slab at the position (0, 0), or @a NULL if the image is empty.
     */
    public Color getConstSlabPtr() {
        throw new NotImplementedException();
//      return m_pImage;
    }

    /**
     * Returns a const pointer to a slab at the specified row.
     * <p/>
     * This method does not perform bounds checking so be careful when calling it.
     *
     *
     * @param row The row, or @a y coordinate.
     * @return A const pointer to a slab at the position ( 0, @a row ), or @a NULL if the image is empty.
     * @pre The coordinates must exist within the bounds of the image.
     */
    public ArrayPointer<Color> getConstSlabPtr(int row) {
        return getConstSlabPtr(0, row);
    }

    /**
     * Returns a const pointer to a slab at the specified position.
     * <p/>
     * This method does not perform bounds checking so be careful when calling it.
     *
     *
     * @param x The x coordinate of the position.
     * @param y The y coordinate of the position.
     * @return A const pointer to a slab at the position ( @a x, @a y ), or @a NULL if the image is empty.
     * @pre The coordinates must exist within the bounds of the image.
     */
    public ArrayPointer<Color> getConstSlabPtr(int x, int y) {
        return getSlabPtr(x, y);
    }

    /**
     * Returns the height of the image.
     *
     * @return The height of the image.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the amount of memory allocated for this image.
     * <p/>
     * This method returns the number of Color objects allocated.
     *
     * @return Returns the amount of memory allocated for this image.
     */
    public long getMemUsed() {
        return memUsed;
    }

    /**
     * Returns a pointer to a slab.
     *
     * @return A pointer to a slab at the position (0, 0), or @a NULL if the image is empty.
     */
    public Color getSlabPtr() {
//        return m_pImage;
        throw new NotImplementedException();
    }

    /**
     * Returns a pointer to a slab at the specified row.
     * <p/>
     * This method does not perform bounds checking so be careful when calling it.
     *
     * @param row The row, or @a y coordinate.
     * @return A pointer to a slab at the position ( 0, @a row ), or @a NULL if the image is empty.
     * @pre The coordinates must exist within the bounds of the image.
     */
    public ArrayPointer<Color> getSlabPtr(int row) {
        return getSlabPtr(0, row);
    }

    /**
     * Returns a pointer to a slab at the specified position.
     * <p/>
     * This method does not perform bounds checking so be careful when calling it.
     *
     *
     * @param x The x coordinate of the position.
     * @param y The y coordinate of the position.
     * @return A pointer to a slab at the position ( @a x, @a y ), or @a NULL if the image is empty.
     * @pre The coordinates must exist within the bounds of the image.
     */
    public ArrayPointer<Color> getSlabPtr(int x, int y) {
        return new ArrayPointer<Color>(image, x + (y * width));
    }

    /**
     * Returns the stride amount of the image.
     * <p/>
     * - The <i>stride amount</i> is the offset between the starting
     * points of any two adjacent slabs in an image.
     * - The stride amount is measured by the number of Color objects
     * between these two points, not by the number of bytes.
     *
     * @return The stride amount of the image.
     */
    public int getStride() {
        return stride;
    }

    /**
     * Returns a color value from the specified position in the image.
     * <p/>
     * This method returns the border value if the coordinates exist outside of the image.
     *
     * @param x The x coordinate of the position.
     * @param y The y coordinate of the position.
     * @return The color value at that position.
     */
    public Color getValue(int x, int y) {
        if (image != null) {
            if (x >= 0 && x < width && y >= 0 && y < height) {
                return image[x * y];
            }
        }
        // The coordinates specified are outside the image.  Return the border value.
        return borderValue;
    }

    /**
     * Returns the width of the image.
     *
     * @return The width of the image.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Reallocates the image to recover wasted memory.
     * <p/>
     * The contents of the image is unaffected.
     *
     * @throws jLibNoise.noise.ExceptionOutOfMemory
     *          Out of memory.  (Yes, this
     *          method can return an out-of-memory exception because two images
     *          will exist temporarily in memory during this call.)
     */
    public void reclaimMem() {
//        size_t newMemUsage = CalcMinMemUsage(m_width, m_height);
//        if (m_memUsed > newMemUsage) {
//            // There is wasted memory.  Create the smallest buffer that can fit the
//            // data and copy the data to it.
//            Color * pNewImage = NULL;
//            try {
//                pNewImage = new Color[newMemUsage];
//            } catch (...){
//                throw noise::ExceptionOutOfMemory();
//            }
//            memcpy(pNewImage, m_pImage, newMemUsage * sizeof(float));
//            delete[] m_pImage;
//            m_pImage = pNewImage;
//            m_memUsed = newMemUsage;
//        }
        throw new NotImplementedException();
    }

    /**
     * Sets the color value to use for all positions outside of the image.
     * <p/>
     * All positions outside of the image are assumed to have a common
     * color value known as the <i>border value</i>.
     *
     * @param borderValue The color value to use for all positions outside of the image.
     */
    public void setBorderValue(Color borderValue) {
        borderValue = borderValue;
    }

    /**
     * Sets the new size for the image.
     * <p/>
     * On exit, the contents of the image are undefined.
     * If the @a OUT_OF_MEMORY exception occurs, this image becomes empty.
     * If the @a INVALID_PARAM exception occurs, the image is unmodified.
     *
     * @param width  The new width for the image.
     * @param height The new height for the image.
     * @throws jLibNoise.noise.ExceptionInvalidParam
     *          See the preconditions.
     * @throws jLibNoise.noise.ExceptionOutOfMemory
     *          Out of memory.
     * @pre The width and height values are positive.
     * @pre The width and height values do not exceed the maximum possible width and height for the image.
     */
    public void setSize(int width, int height) {
        if (width < 0 || height < 0 || width > RASTER_MAX_WIDTH || height > RASTER_MAX_HEIGHT) {
            // Invalid width or height.
            throw new ExceptionInvalidParam();
        } else if (width == 0 || height == 0) {
            // An empty image was specified.  Delete it and zero out the size member variables.
            deleteImageAndReset();
        } else {
            // A new image size was specified.  Allocate a new image buffer unless
            // the current buffer is large enough for the new image (we don't want
            // costly reallocations going on.)
            long newMemUsage = calcMinMemUsage(width, height);
            if (memUsed < newMemUsage) {
                // The new size is too big for the current image buffer.  We need to
                // reallocate.
                deleteImageAndReset();
                try {
                    image = new Color[(int) newMemUsage];
                } catch (Exception e) {
                    throw new ExceptionOutOfMemory();
                }
                memUsed = newMemUsage;
            }
            stride = (int) calcStride(width);
            this.width = width;
            this.height = height;
        }
    }

    /**
     * Sets a color value at a specified position in the image.
     * <p/>
     * This method does nothing if the image is empty or the position is
     * outside the bounds of the image.
     *
     * @param x     The x coordinate of the position.
     * @param y     The y coordinate of the position.
     * @param value The color value to set at the given position.
     */
    public void setValue(int x, int y, Color value) {
        if (image != null) {
            if (x >= 0 && x < width && y >= 0 && y < height) {
                image[x * y] = value;
            }
        }
    }

    /**
     * Takes ownership of the buffer within the source image.
     * <p/>
     * On exit, the source image object becomes empty.
     * <p/>
     * This method only moves the buffer pointer so this method is very quick.
     *
     * @param source The source image.
     */
    public void takeOwnership(Image source) {
        // Copy the values and the image buffer from the source image to this image.
//        // Now this image pwnz the source buffer.
//        delete[] m_pImage;
//        m_memUsed = source.m_memUsed;
//        m_height = source.m_height;
//        m_pImage = source.m_pImage;
//        m_stride = source.m_stride;
//        m_width = source.m_width;
//
//        // Now that the source buffer is assigned to this image, reset the source
//        // image object.
//        source.InitObj();
        throw new NotImplementedException();
    }

    /**
     * Returns the minimum amount of memory required to store an image of the specified size.
     * <p/>
     * The returned color value is measured by the number of Color
     * objects required to store the image, not by the number of bytes.
     *
     * @param width  The width of the image.
     * @param height The height of the image.
     * @return The minimum amount of memory required to store the image.
     */
    private long calcMinMemUsage(int width, int height) {
        return calcStride(width * height);
    }

    /**
     * Calculates the stride amount for an image.
     * <p/>
     * - The <i>stride amount</i> is the offset between the starting
     * points of any two adjacent slabs in an image.
     * - The stride amount is measured by the number of Color objects
     * between these two points, not by the number of bytes.
     *
     * @param width The width of the image.
     * @return The stride amount.
     */
    private long calcStride(int width) {
        return (((width + RASTER_STRIDE_BOUNDARY - 1) / RASTER_STRIDE_BOUNDARY) * RASTER_STRIDE_BOUNDARY);
    }

    /**
     * Copies the contents of the buffer in the source image into this image.
     * <p/>
     * This method reallocates the buffer in this image object if necessary.
     *
     * @param source The source image.
     * @throws jLibNoise.noise.ExceptionOutOfMemory
     *          Out of memory.
     */
    private void copyImage(Image source) {
        // Resize the image buffer, then copy the slabs from the source image
        // buffer to this image buffer.
        setSize(source.getWidth(), source.getHeight());
        for (int y = 0; y < source.getHeight(); y++) {
            ArrayPointer<Color> pSource = source.getConstSlabPtr(0, y);
            ArrayPointer<Color> pDest = getSlabPtr(0, y);
            pSource.copyTo(pDest);
        }

        // Copy the border value as well.
        borderValue = source.borderValue;
    }

    /**
     * Resets the image object.
     * <p/>
     * This method is similar to the InitObj() method, except this method
     * deletes the memory allocated to the image.
     */
    private void deleteImageAndReset() {
        initObj();
    }

    /**
     * Initializes the image object.
     *
     * @pre Must be called during object construction.
     * @pre The image buffer must not exist.
     */
    void initObj() {
        image = null;
        height = 0;
        width = 0;
        stride = 0;
        memUsed = 0;
        borderValue = new Color(0, 0, 0, 0);
    }

}
