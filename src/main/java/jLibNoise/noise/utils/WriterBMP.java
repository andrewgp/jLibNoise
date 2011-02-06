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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Windows bitmap image writer class.
 *
 * This class creates a file in Windows bitmap (*.bmp) format given the
 * contents of an image object.
 *
 * <b>Writing the image</b>
 *
 * To write the image to a file, perform the following steps:
 * - Pass the filename to the SetDestFilename() method.
 * - Pass an Image object to the SetSourceImage() method.
 * - Call the WriteDestFile() method.
 *
 * The SetDestFilename() and SetSourceImage() methods must be called
 * before calling the WriteDestFile() method.
 */
public class WriterBMP {

    public static final int BMP_HEADER_SIZE = 54;

    // Name of the file to write.
    protected String m_destFilename;
    // A pointer to the image object that will be written to the file.
    protected Image m_pSourceImage;

    public WriterBMP() {
    }

    /**
     * Returns the name of the file to write.
     * @return The name of the file to write.
     */
    public String GetDestFilename ()
    {
      return m_destFilename;
    }

    /**
     * Sets the name of the file to write.
     *
     * Call this method before calling the WriteDestFile() method.
     *
     * @param filename The name of the file to write.
     */
    public void SetDestFilename (String filename)
    {
      m_destFilename = filename;
    }

    /**
     * Sets the image object that is written to the file.
     *
     * This object only stores a pointer to an image object, so make sure
     * this object exists before calling the WriteDestFile() method.
     *
     * @param sourceImage The image object to write.
     */
    public void SetSourceImage (Image sourceImage)
    {
      m_pSourceImage = sourceImage;
    }


    /**
     * Writes the contents of the image object to the file.
     *
     * @pre SetDestFilename() has been previously called.
     * @pre SetSourceImage() has been previously called.
     * @throws jLibNoise.noise.ExceptionInvalidParam See the preconditions.
     * @throws jLibNoise.noise.ExceptionOutOfMemory Out of memory.
     * @throws Exception An unknown exception occurred.
     * Possibly the file could not be written.
     * <p/>
     * This method encodes the contents of the image and writes it to a
     * file.  Before calling this method, call the SetSourceImage()
     * method to specify the image, then call the SetDestFilename()
     * method to specify the name of the file to write.
     */
    public void WriteDestFile() throws IOException {
        int width = m_pSourceImage.GetWidth();
        int height = m_pSourceImage.GetHeight();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

//        OutputStreamWriter os = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(new File("d:/java.txt"))));

        int index = 0;
        int[] pixels = new int[width * height];
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                int pos = x + (y * width);
                jLibNoise.noise.utils.Color clr = m_pSourceImage.m_pImage[pos];
                pixels[index] = new java.awt.Color(clr.red, clr.green, clr.blue, clr.alpha).getRGB();
                index++;

//                os.write(x + "," + y + "=" + clr.red + "," + clr.green + "," + clr.blue + "," + clr.alpha);
//                os.write("\r\n");
            }
        }

        img.setRGB(0, 0, width, height, pixels, 0, width);

        ImageIO.write(img, "png", new File(m_destFilename));

//        os.close();

//        if (m_pSourceImage == null) {
//            throw new ExceptionInvalidParam();
//        }
//
//        int width = m_pSourceImage.GetWidth();
//        int height = m_pSourceImage.GetHeight();
//
//        // The width of one line in the file must be aligned on a 4-byte boundary.
//        int bufferSize = CalcWidthByteCount(width);
//        int destSize = bufferSize * height;
//
//        // This buffer holds one horizontal line in the destination file.
//        byte[] pLineBuffer = new byte[bufferSize * 3];
//
//        // File object used to write the file.
//        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(m_destFilename));
//        OutputStreamWriter osw = new OutputStreamWriter(os);
//
//        // Build the header.
//        byte d[] = new byte[4];
//        osw.write("BM");
//        os.write(Utils.UnpackLittle32(d, destSize + BMP_HEADER_SIZE));
//        osw.write("\0\0\0\0");
//        os.write(Utils.UnpackLittle32(d, BMP_HEADER_SIZE));
//        os.write(Utils.UnpackLittle32(d, 40));   // Palette offset
//        os.write(Utils.UnpackLittle32(d, width));
//        os.write(Utils.UnpackLittle32(d, height));
//        os.write(Utils.UnpackLittle16(d, (short) 1));   // Planes per pixel
//        os.write(Utils.UnpackLittle16(d, (short) 24));   // Bits per plane
//        osw.write("\0\0\0\0"); // Compression (0 = none)
//        os.write(Utils.UnpackLittle32(d, destSize));
//        os.write(Utils.UnpackLittle32(d, 2834)); // X pixels per meter
//        os.write(Utils.UnpackLittle32(d, 2834)); // Y pixels per meter
//        osw.write("\0\0\0\0");
//        osw.write("\0\0\0\0");
//
//        // Build and write each horizontal line to the file.
//        ArrayPointer<Color> pSource = m_pSourceImage.GetSlabPtr(0);
//        for (int y = 0; y < height; y++) {
////            memset(pLineBuffer, 0, bufferSize);
//            byte[] pDest = pLineBuffer;
//            int pDestIdx = 0;
//            for (int x = 0; x < width; x++) {
//                Color clr = pSource.get();
//                pDest[pDestIdx++] = (byte) clr.blue;
//                pDest[pDestIdx++] = (byte) clr.green;
//                pDest[pDestIdx++] = (byte) clr.red;
//                pSource.increment();
//            }
//            os.write(pLineBuffer);
//        }
//
//        osw.close();
    }

    /**
     * Calculates the width of one horizontal line in the file, in bytes.
     *
     * Windows bitmap files require that the width of one horizontal line
     * must be aligned to a 32-bit boundary.
     *
     * @param width The width of the image, in points.
     * @return The width of one horizontal line in the file.
     */
    protected int CalcWidthByteCount (int width) {
        return (width * 2);
    }
}
