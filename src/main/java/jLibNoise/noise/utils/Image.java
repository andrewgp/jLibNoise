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
    private Color m_borderValue;
    // The current height of the image.
    private int m_height;
    /**
     * The amount of memory allocated for the image.
     * This value is equal to the number of Color objects allocated for the image, not the number of bytes.
     */
    long m_memUsed;
    // A pointer to the image buffer.
    Color[] m_pImage;
    // The stride amount of the image.
    private int m_stride;
    // The current width of the image.
    private int m_width;

    /**
     * Constructor.
     * Creates an empty image.
     */
    public Image() {
        InitObj();
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
        InitObj();
        SetSize(width, height);
    }

    /**
     * Copy constructor.
     *
     * @param rhs Image to copy
     * @throws jLibNoise.noise.ExceptionOutOfMemory
     *          Out of memory.
     */
    public Image(Image rhs) {
        InitObj();
        CopyImage(rhs);
    }

    /**
     * Clears the image to a specified color value.
     *
     * @param value The color value that all positions within the image are cleared to.
     */
    public void Clear(Color value) {
        for (int i = 0; i < m_height * m_width; i++) {
            m_pImage[i] = value;
        }
        throw new NotImplementedException();
    }

    /**
     * Returns the color value used for all positions outside of the image.
     * <p/>
     * All positions outside of the image are assumed to have a common
     * color value known as the <i>border value</i>.
     *
     * @return The color value used for all positions outside of the image.
     */
    public Color GetBorderValue() {
        return m_borderValue;
    }

    /**
     * Returns a const pointer to a slab.
     *
     * @return A const pointer to a slab at the position (0, 0), or @a NULL if the image is empty.
     */
    public Color GetConstSlabPtr() {
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
    public ArrayPointer<Color> GetConstSlabPtr(int row) {
        return GetConstSlabPtr(0, row);
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
    public ArrayPointer<Color> GetConstSlabPtr(int x, int y) {
        return GetSlabPtr(x, y);
    }

    /**
     * Returns the height of the image.
     *
     * @return The height of the image.
     */
    public int GetHeight() {
        return m_height;
    }

    /**
     * Returns the amount of memory allocated for this image.
     * <p/>
     * This method returns the number of Color objects allocated.
     *
     * @return Returns the amount of memory allocated for this image.
     */
    public long GetMemUsed() {
        return m_memUsed;
    }

    /**
     * Returns a pointer to a slab.
     *
     * @return A pointer to a slab at the position (0, 0), or @a NULL if the image is empty.
     */
    public Color GetSlabPtr() {
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
    public ArrayPointer<Color> GetSlabPtr(int row) {
        return GetSlabPtr(0, row);
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
    public ArrayPointer<Color> GetSlabPtr(int x, int y) {
//      return m_pImage + x + m_stride * y;
        return new ArrayPointer<Color>(m_pImage, x + (y * m_width));
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
    public int GetStride() {
        return m_stride;
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
    public Color GetValue(int x, int y) {
        if (m_pImage != null) {
            if (x >= 0 && x < m_width && y >= 0 && y < m_height) {
                return m_pImage[x * y];
            }
        }
        // The coordinates specified are outside the image.  Return the border value.
        return m_borderValue;
    }

    /**
     * Returns the width of the image.
     *
     * @return The width of the image.
     */
    public int GetWidth() {
        return m_width;
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
    public void ReclaimMem() {
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
    public void SetBorderValue(Color borderValue) {
        m_borderValue = borderValue;
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
    public void SetSize(int width, int height) {
        if (width < 0 || height < 0 || width > RASTER_MAX_WIDTH || height > RASTER_MAX_HEIGHT) {
            // Invalid width or height.
            throw new ExceptionInvalidParam();
        } else if (width == 0 || height == 0) {
            // An empty image was specified.  Delete it and zero out the size member variables.
            DeleteImageAndReset();
        } else {
            // A new image size was specified.  Allocate a new image buffer unless
            // the current buffer is large enough for the new image (we don't want
            // costly reallocations going on.)
            long newMemUsage = CalcMinMemUsage(width, height);
            if (m_memUsed < newMemUsage) {
                // The new size is too big for the current image buffer.  We need to
                // reallocate.
                DeleteImageAndReset();
                try {
                    m_pImage = new Color[(int) newMemUsage];
                } catch (Exception e) {
                    throw new ExceptionOutOfMemory();
                }
                m_memUsed = newMemUsage;
            }
            m_stride = (int) CalcStride(width);
            m_width = width;
            m_height = height;
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
    public void SetValue(int x, int y, Color value) {
//        if (m_pImage != NULL) {
//            if (x >= 0 && x < m_width && y >= 0 && y < m_height) {
//                *(GetSlabPtr(x, y)) = value;
//            }
//        }
        m_pImage[x * y] = value;
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
    public void TakeOwnership(Image source) {
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
    private long CalcMinMemUsage(int width, int height) {
        return CalcStride(width * height);
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
    private long CalcStride(int width) {
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
    private void CopyImage(Image source) {
//        // Resize the image buffer, then copy the slabs from the source image
//        // buffer to this image buffer.
//        SetSize(source.GetWidth(), source.GetHeight());
//        for (int y = 0; y < source.GetHeight(); y++) {
//            const Color * pSource = source.GetConstSlabPtr(0, y);
//            Color * pDest = GetSlabPtr(0, y);
//            memcpy(pDest, pSource, (size_t) source.GetWidth() * sizeof(float));
//        }
//
//        // Copy the border value as well.
//        m_borderValue = source.m_borderValue;
        throw new NotImplementedException();
    }

    /**
     * Resets the image object.
     * <p/>
     * This method is similar to the InitObj() method, except this method
     * deletes the memory allocated to the image.
     */
    private void DeleteImageAndReset() {
//        delete[] m_pImage;
        InitObj();
    }

    /**
     * Initializes the image object.
     *
     * @pre Must be called during object construction.
     * @pre The image buffer must not exist.
     */
    void InitObj() {
        m_pImage = null;
        m_height = 0;
        m_width = 0;
        m_stride = 0;
        m_memUsed = 0;
        m_borderValue = new Color(0, 0, 0, 0);
    }

}
