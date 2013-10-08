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
import jLibNoise.noise.MathConst;
import jLibNoise.noise.Misc;

/**
 * Renders an image from a noise map.
 * <p/>
 * This class renders an image given the contents of a noise-map object.
 * <p/>
 * An application can configure the output of the image in three ways:
 * - Specify the color gradient.
 * - Specify the light source parameters.
 * - Specify the background image.
 * <p/>
 * <b>Specify the color gradient</b>
 * <p/>
 * This class uses a color gradient to calculate the color for each pixel
 * in the destination image according to the value from the corresponding
 * position in the noise map.
 * <p/>
 * A color gradient is a list of gradually-changing colors.  A color
 * gradient is defined by a list of <i>gradient points</i>.  Each
 * gradient point has a position and a color.  In a color gradient, the
 * colors between two adjacent gradient points are linearly interpolated.
 * <p/>
 * For example, suppose this class contains the following color gradient:
 * <p/>
 * - -1.0 maps to dark blue.
 * - -0.2 maps to light blue.
 * - -0.1 maps to tan.
 * - 0.0 maps to green.
 * - 1.0 maps to white.
 * <p/>
 * The value 0.5 maps to a greenish-white color because 0.5 is halfway
 * between 0.0 (mapped to green) and 1.0 (mapped to white).
 * <p/>
 * The value -0.6 maps to a medium blue color because -0.6 is halfway
 * between -1.0 (mapped to dark blue) and -0.2 (mapped to light blue).
 * <p/>
 * The color gradient requires a minimum of two gradient points.
 * <p/>
 * This class contains two pre-made gradients: a grayscale gradient and a
 * color gradient suitable for terrain.  To use these pre-made gradients,
 * call the BuildGrayscaleGradient() or BuildTerrainGradient() methods,
 * respectively.
 *
 * @note The color value passed to AddGradientPoint() has an alpha
 * channel.  This alpha channel specifies how a pixel in the background
 * image (if specified) is blended with the calculated color.  If the
 * alpha value is high, this class weighs the blend towards the
 * calculated color, and if the alpha value is low, this class weighs the
 * blend towards the color from the corresponding pixel in the background
 * image.
 * <p/>
 * <b>Specify the light source parameters</b>
 * <p/>
 * This class contains a parallel light source that lights the image.  It
 * interprets the noise map as a bump map.
 * <p/>
 * To enable or disable lighting, pass a Boolean value to the
 * EnableLight() method.
 * <p/>
 * To set the position of the light source in the "sky", call the
 * SetLightAzimuth() and SetLightElev() methods.
 * <p/>
 * To set the color of the light source, call the SetLightColor() method.
 * <p/>
 * To set the intensity of the light source, call the SetLightIntensity()
 * method.  A good intensity value is 2.0, although that value tends to
 * "wash out" very light colors from the image.
 * <p/>
 * To set the contrast amount between areas in light and areas in shadow,
 * call the SetLightContrast() method.  Determining the correct contrast
 * amount requires some trial and error, but if your application
 * interprets the noise map as a height map that has its elevation values
 * measured in meters and has a horizontal resolution of @a h meters, a
 * good contrast amount to use is ( 1.0 / @a h ).
 * <p/>
 * <b>Specify the background image</b>
 * <p/>
 * To specify a background image, pass an Image object to the
 * SetBackgroundImage() method.
 * <p/>
 * This class determines the color of a pixel in the destination image by
 * blending the calculated color with the color of the corresponding
 * pixel from the background image.
 * <p/>
 * The blend amount is determined by the alpha of the calculated color.
 * If the alpha value is high, this class weighs the blend towards the
 * calculated color, and if the alpha value is low, this class weighs the
 * blend towards the color from the corresponding pixel in the background
 * image.
 * <p/>
 * <b>Rendering the image</b>
 * <p/>
 * To render the image, perform the following steps:
 * - Pass a NoiseMap object to the SetSourceNoiseMap() method.
 * - Pass an Image object to the SetDestImage() method.
 * - Pass an Image object to the SetBackgroundImage() method (optional)
 * - Call the Render() method.
 */
public class RendererImage {

    // The cosine of the azimuth of the light source.
    private double cosAzimuth;
    // The cosine of the elevation of the light source.
    private double cosElev;
    // The color gradient used to specify the image colors.
    private GradientColor gradient;
    // A flag specifying whether lighting is enabled.
    private boolean isLightEnabled;
    // A flag specifying whether wrapping is enabled.
    private boolean isWrapEnabled;
    // The azimuth of the light source, in degrees.
    private double lightAzimuth;
    // The brightness of the light source.
    private double lightBrightness;
    // The color of the light source.
    private Color lightColor;
    // The contrast between areas in light and areas in shadow.
    private double lightContrast;
    // The elevation of the light source, in degrees.
    private double lightElev;
    // The intensity of the light source.
    private double lightIntensity;
    // A pointer to the background image.
    private Image backgroundImage;
    // A pointer to the destination image.
    private Image destImage;
    // A pointer to the source noise map.
    private NoiseMap sourceNoiseMap;
    
    /**
     * Used by the CalcLightIntensity() method to recalculate the light values only if the light parameters change.
     * <p/>
     * When the light parameters change, this value is set to True.  When
     * the CalcLightIntensity() method is called, this value is set to false.
     */
    private boolean recalcLightValues;
    // The sine of the azimuth of the light source.
    private double sinAzimuth;
    // The sine of the elevation of the light source.
    private double sinElev;

    public RendererImage() {
        isLightEnabled = false;
        isWrapEnabled = false;
        lightAzimuth = 45.0;
        lightBrightness = 1.0;
        lightColor = new Color(255, 255, 255, 255);
        lightContrast = 1.0;
        lightElev = 45.0;
        lightIntensity = 1.0;
        recalcLightValues = true;
        gradient = new GradientColor();

        buildGrayscaleGradient();
    }

    /**
     * Adds a gradient point to this gradient object.
     * <p/>
     * This object uses a color gradient to calculate the color for each
     * pixel in the destination image according to the value from the
     * corresponding position in the noise map.
     * <p/>
     * The gradient requires a minimum of two gradient points.
     * <p/>
     * The specified color value passed to this method has an alpha
     * channel.  This alpha channel specifies how a pixel in the
     * background image (if specified) is blended with the calculated
     * color.  If the alpha value is high, this object weighs the blend
     * towards the calculated color, and if the alpha value is low, this
     * object weighs the blend towards the color from the corresponding
     * pixel in the background image.
     *
     * @param gradientPos   The position of this gradient point.
     * @param gradientColor The color of this gradient point.
     * @throws jLibNoise.noise.ExceptionInvalidParam
     *          See the preconditions.
     * @pre No two gradient points have the same position.
     */
    public void addGradientPoint(double gradientPos, Color gradientColor) {
        gradient.addGradientPoint(gradientPos, gradientColor);
    }

    /**
     * Builds a gray-scale gradient.
     * <p/>
     * This color gradient contains the following gradient points:
     * - -1.0 maps to black
     * - 1.0 maps to white
     *
     * @post The original gradient is cleared and a grayscale gradient is created.
     */
    public void buildGrayscaleGradient() {
        clearGradient();
        gradient.addGradientPoint(-1.0, new Color(0, 0, 0, 255));
        gradient.addGradientPoint(1.0, new Color(255, 255, 255, 255));
    }

    /**
     * Builds a color gradient suitable for terrain.
     * <p/>
     * This gradient color at position 0.0 is the "sea level".  Above
     * that value, the gradient contains greens, browns, and whites.
     * Below that value, the gradient contains various shades of blue.
     *
     * @post The original gradient is cleared and a terrain gradient is created.
     */
    public void buildTerrainGradient() {
        clearGradient();
        gradient.addGradientPoint(-1.00, new Color(0, 0, 128, 255));
        gradient.addGradientPoint(-0.20, new Color(32, 64, 128, 255));
        gradient.addGradientPoint(-0.04, new Color(64, 96, 192, 255));
        gradient.addGradientPoint(-0.02, new Color(192, 192, 128, 255));
        gradient.addGradientPoint(0.00, new Color(0, 192, 0, 255));
        gradient.addGradientPoint(0.25, new Color(192, 192, 0, 255));
        gradient.addGradientPoint(0.50, new Color(160, 96, 64, 255));
        gradient.addGradientPoint(0.75, new Color(128, 255, 255, 255));
        gradient.addGradientPoint(1.00, new Color(255, 255, 255, 255));
    }

    /**
     * Clears the color gradient.
     * <p/>
     * Before calling the Render() method, the application must specify a
     * new color gradient with at least two gradient points.
     */
    public void clearGradient() {
        gradient.clear();
    }

    /**
     * Enables or disables the light source.
     * <p/>
     * If the light source is enabled, this object will interpret the
     * noise map as a bump map.
     *
     * @param enable A flag that enables or disables the light source.
     */
    public void enableLight(boolean enable) {
        isLightEnabled = enable;
    }

    public void enableLight() {
        enableLight(true);
    }

    /**
     * Enables or disables noise-map wrapping.
     * <p/>
     * This object requires five points (the initial point and its four
     * neighbors) to calculate light shading.  If wrapping is enabled,
     * and the initial point is on the edge of the noise map, the
     * appropriate neighbors that lie outside of the noise map will
     * "wrap" to the opposite side(s) of the noise map.  Otherwise, the
     * appropriate neighbors are cropped to the edge of the noise map.
     * <p/>
     * Enabling wrapping is useful when creating spherical renderings and
     * tileable textures.
     *
     * @param enable A flag that enables or disables noise-map wrapping.
     */
    public void enableWrap(boolean enable) {
        isWrapEnabled = enable;
    }

    public void EnableWrap() {
        enableWrap(true);
    }

    /**
     * Returns the azimuth of the light source, in degrees.
     * The azimuth is the location of the light source around the horizon:
     * - 0.0 degrees is east.
     * - 90.0 degrees is north.
     * - 180.0 degrees is west.
     * - 270.0 degrees is south.
     *
     * @return The azimuth of the light source.
     */
    public double getLightAzimuth() {
        return lightAzimuth;
    }

    /**
     * Returns the brightness of the light source.
     *
     * @return The brightness of the light source.
     */
    public double getLightBrightness() {
        return lightBrightness;
    }

    /**
     * Returns the color of the light source.
     *
     * @return The color of the light source.
     */
    public Color getLightColor() {
        return lightColor;
    }

    /**
     * Returns the contrast of the light source.
     * <p/>
     * The contrast specifies how sharp the boundary is between the
     * light-facing areas and the shadowed areas.
     * <p/>
     * The contrast determines the difference between areas in light and
     * areas in shadow.  Determining the correct contrast amount requires
     * some trial and error, but if your application interprets the noise
     * map as a height map that has a spatial resolution of @a h meters
     * and an elevation resolution of 1 meter, a good contrast amount to
     * use is ( 1.0 / @a h ).
     *
     * @return The contrast of the light source.
     */
    public double getLightContrast() {
        return lightContrast;
    }

    /**
     * Returns the elevation of the light source, in degrees.
     * <p/>
     * The elevation is the angle above the horizon:
     * - 0 degrees is on the horizon.
     * - 90 degrees is straight up.
     *
     * @return The elevation of the light source.
     */
    public double getLightElev() {
        return lightElev;
    }

    /**
     * Returns the intensity of the light source.
     *
     * @return The intensity of the light source.
     */
    public double getLightIntensity() {
        return lightIntensity;
    }

    /**
     * Determines if the light source is enabled.
     *
     * @return true if the light source is enabled.
     */
    public boolean isLightEnabled() {
        return isLightEnabled;
    }

    /**
     * Determines if noise-map wrapping is enabled.
     * <p/>
     * This object requires five points (the initial point and its four
     * neighbors) to calculate light shading.  If wrapping is enabled,
     * and the initial point is on the edge of the noise map, the
     * appropriate neighbors that lie outside of the noise map will
     * "wrap" to the opposite side(s) of the noise map.  Otherwise, the
     * appropriate neighbors are cropped to the edge of the noise map.
     * <p/>
     * Enabling wrapping is useful when creating spherical renderings and
     * tileable textures
     *
     * @return true if noise-map wrapping is enabled.
     */
    public boolean isWrapEnabled() {
        return isWrapEnabled;
    }

    /**
     * Renders the destination image using the contents of the source noise map and an optional background image.
     * <p/>
     * The background image and the destination image can safely refer to
     * the same image, although in this case, the destination image is
     * irretrievably blended into the background image.
     *
     * @throws jLibNoise.noise.ExceptionInvalidParam
     *          See the preconditions.
     * @pre SetSourceNoiseMap() has been previously called.
     * @pre SetDestImage() has been previously called.
     * @pre There are at least two gradient points in the color gradient.
     * @pre No two gradient points have the same position.
     * @pre If a background image was specified, it has the exact same size as the source height map.
     * @post The original contents of the destination image is destroyed.
     */
    public void render() {
        if (sourceNoiseMap == null
                || destImage == null
                || sourceNoiseMap.getWidth() <= 0
                || sourceNoiseMap.getHeight() <= 0
                || gradient.getGradientPointCount() < 2) {
            throw new ExceptionInvalidParam();
        }

        int width = sourceNoiseMap.getWidth();
        int height = sourceNoiseMap.getHeight();

        // If a background image was provided, make sure it is the same size the source noise map.
        if (backgroundImage != null) {
            if (backgroundImage.getWidth() != width || backgroundImage.getHeight() != height) {
                throw new ExceptionInvalidParam();
            }
        }

        // Create the destination image.  It is safe to reuse it if this is also the
        // background image.
        if (destImage != backgroundImage) {
            destImage.setSize(width, height);
        }

        ArrayPointer.NativeFloatPrim pSource = sourceNoiseMap.getConstSlabPtr(0);
        ArrayPointer<Color> pDest = destImage.getSlabPtr(0);

        for (int y = 0; y < height; y++) {
            ArrayPointer<Color> pBackground = null;
            if (backgroundImage != null) {
                pBackground = backgroundImage.getConstSlabPtr(y);
            }

            for (int x = 0; x < width; x++) {
                // Get the color based on the value at the current point in the noise map.
                Color destColor = gradient.getColor(pSource.get());
                
                // If lighting is enabled, calculate the light intensity based on the rate of change at the current point in the noise map.
                double lightIntensityLocal;
                if (isLightEnabled) {
                    // Calculate the positions of the current point's four-neighbors.
                    int xLeftOffset, xRightOffset;
                    int yUpOffset, yDownOffset;
                    if (isWrapEnabled) {
                        if (x == 0) {
                            xLeftOffset = width - 1;
                            xRightOffset = 1;
                        } else if (x == width - 1) {
                            xLeftOffset = -1;
                            xRightOffset = -(width - 1);
                        } else {
                            xLeftOffset = -1;
                            xRightOffset = 1;
                        }
                        if (y == 0) {
                            yDownOffset = height - 1;
                            yUpOffset = 1;
                        } else if (y == height - 1) {
                            yDownOffset = -1;
                            yUpOffset = -(height - 1);
                        } else {
                            yDownOffset = -1;
                            yUpOffset = 1;
                        }
                    } else {
                        if (x == 0) {
                            xLeftOffset = 0;
                            xRightOffset = 1;
                        } else if (x == width - 1) {
                            xLeftOffset = -1;
                            xRightOffset = 0;
                        } else {
                            xLeftOffset = -1;
                            xRightOffset = 1;
                        }
                        if (y == 0) {
                            yDownOffset = 0;
                            yUpOffset = 1;
                        } else if (y == height - 1) {
                            yDownOffset = -1;
                            yUpOffset = 0;
                        } else {
                            yDownOffset = -1;
                            yUpOffset = 1;
                        }
                    }
                    yDownOffset *= sourceNoiseMap.getStride();
                    yUpOffset *= sourceNoiseMap.getStride();

                    // Get the noise value of the current point in the source noise map and the noise values of its four-neighbors.
                    double nc = (double) pSource.get();
                    double nl = (double) pSource.get(xLeftOffset);
                    double nr = (double) pSource.get(xRightOffset);
                    double nd = (double) pSource.get(yDownOffset);
                    double nu = (double) pSource.get(yUpOffset);

                    // Now we can calculate the lighting intensity.
                    lightIntensityLocal = calcLightIntensity(nc, nl, nr, nd, nu);
                    lightIntensityLocal *= lightBrightness;
                } else {
                    // These values will apply no lighting to the destination image.
                    lightIntensityLocal = 1.0;
                }

                // Get the current background color from the background image.
                Color backgroundColor = new Color(255, 255, 255, 255);
                if (backgroundImage != null) {
                    backgroundColor = pBackground.get();
                }

                // Blend the destination color, background color, and the light
                // intensity together, then update the destination image with that color.
                pDest.assignThenIncrementPosition(calcDestColor(destColor, backgroundColor, lightIntensityLocal));

                // Go to the next point.
                pSource.increment();
                if (backgroundImage != null) {
                    pBackground.increment();
                }
            }
        }
    }

    /**
     * Sets the background image.
     * <p/>
     * If a background image has been specified, the Render() method
     * blends the pixels from the background image onto the corresponding
     * pixels in the destination image.  The blending weights are
     * determined by the alpha channel in the pixels in the destination
     * image.
     * <p/>
     * The destination image must exist throughout the lifetime of this
     * object unless another image replaces that image.
     *
     * @param backgroundImage The background image.
     */
    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    /**
     * Sets the destination image.
     * <p/>
     * The destination image will contain the rendered image after a
     * successful call to the Render() method.
     * <p/>
     * The destination image must exist throughout the lifetime of this
     * object unless another image replaces that image
     *
     * @param destImage The destination image.
     */
    public void setDestImage(Image destImage) {
        this.destImage = destImage;
    }

    /**
     * Sets the azimuth of the light source, in degrees.
     * <p/>
     * The azimuth is the location of the light source around the horizon:
     * - 0.0 degrees is east.
     * - 90.0 degrees is north.
     * - 180.0 degrees is west.
     * - 270.0 degrees is south.
     * <p/>
     * Make sure the light source is enabled via a call to the
     * EnableLight() method before calling the Render() method.
     *
     * @param lightAzimuth The azimuth of the light source.
     */
    public void setLightAzimuth(double lightAzimuth) {
        this.lightAzimuth = lightAzimuth;
        recalcLightValues = true;
    }

    /**
     * Sets the brightness of the light source.
     * <p/>
     * Make sure the light source is enabled via a call to the
     * EnableLight() method before calling the Render() method.
     *
     * @param lightBrightness The brightness of the light source.
     */
    public void setLightBrightness(double lightBrightness) {
        this.lightBrightness = lightBrightness;
        recalcLightValues = true;
    }

    /**
     * Sets the color of the light source.
     * <p/>
     * Make sure the light source is enabled via a call to the
     * EnableLight() method before calling the Render() method.
     *
     * @param lightColor The light color.
     */
    public void setLightColor(Color lightColor) {
        this.lightColor = lightColor;
    }

    /**
     * Sets the contrast of the light source.
     * <p/>
     * light-facing areas and the shadowed areas.
     * <p/>
     * The contrast determines the difference between areas in light and
     * areas in shadow.  Determining the correct contrast amount requires
     * some trial and error, but if your application interprets the noise
     * map as a height map that has a spatial resolution of @a h meters
     * and an elevation resolution of 1 meter, a good contrast amount to
     * use is ( 1.0 / @a h ).
     * <p/>
     * Make sure the light source is enabled via a call to the
     * EnableLight() method before calling the Render() method.
     *
     * @param lightContrast The contrast of the light source.
     * @throws jLibNoise.noise.ExceptionInvalidParam
     *          See the preconditions.
     * @pre The specified light contrast is positive.
     */
    public void setLightContrast(double lightContrast) {
        if (lightContrast <= 0.0) {
            throw new ExceptionInvalidParam();
        }

        this.lightContrast = lightContrast;
        recalcLightValues = true;
    }

    /**
     * Sets the elevation of the light source, in degrees.
     * <p/>
     * The elevation is the angle above the horizon:
     * - 0 degrees is on the horizon.
     * - 90 degrees is straight up.
     * <p/>
     * Make sure the light source is enabled via a call to the
     * EnableLight() method before calling the Render() method.
     *
     * @param lightElev The elevation of the light source.
     */
    public void setLightElev(double lightElev) {
        this.lightElev = lightElev;
        recalcLightValues = true;
    }

    /**
     * Returns the intensity of the light source.
     * <p/>
     * A good value for intensity is 2.0.
     * <p/>
     * Make sure the light source is enabled via a call to the
     * EnableLight() method before calling the Render() method.
     *
     * @param lightIntensity The intensity of the light source.
     */
    public void setLightIntensity(double lightIntensity) {
        if (lightIntensity < 0.0) {
            throw new ExceptionInvalidParam();
        }

        this.lightIntensity = lightIntensity;
        recalcLightValues = true;
    }

    /**
     * Sets the source noise map.
     * <p/>
     * The destination image must exist throughout the lifetime of this
     * object unless another image replaces that image.
     *
     * @param sourceNoiseMap The source noise map.
     */
    public void setSourceNoiseMap(NoiseMap sourceNoiseMap) {
        this.sourceNoiseMap = sourceNoiseMap;
    }

    /// Calculates the destination color.
    ///
    /// @param sourceColor The source color generated from the color
    /// gradient.
    /// @param backgroundColor The color from the background image at the
    /// corresponding position.
    /// @param lightValue The intensity of the light at that position.
    ///
    /// @returns The destination color.
    private Color calcDestColor(Color sourceColor, Color backgroundColor, double lightValue) {
        double sourceRed = (double) sourceColor.red / 255.0;
        double sourceGreen = (double) sourceColor.green / 255.0;
        double sourceBlue = (double) sourceColor.blue / 255.0;
        double sourceAlpha = (double) sourceColor.alpha / 255.0;
        double backgroundRed = (double) backgroundColor.red / 255.0;
        double backgroundGreen = (double) backgroundColor.green / 255.0;
        double backgroundBlue = (double) backgroundColor.blue / 255.0;

        // First, blend the source color to the background color using the alpha
        // of the source color.
        double red = Interp.linearInterp(backgroundRed, sourceRed, sourceAlpha);
        double green = Interp.linearInterp(backgroundGreen, sourceGreen, sourceAlpha);
        double blue = Interp.linearInterp(backgroundBlue, sourceBlue, sourceAlpha);

        if (isLightEnabled) {
            // Now calculate the light color.
            double lightRed = lightValue * (double) lightColor.red / 255.0;
            double lightGreen = lightValue * (double) lightColor.green / 255.0;
            double lightBlue = lightValue * (double) lightColor.blue / 255.0;

            // Apply the light color to the new color.
            red *= lightRed;
            green *= lightGreen;
            blue *= lightBlue;
        }

        // Clamp the color channels to the (0..1) range.
        red = (red < 0.0) ? 0.0 : red;
        red = (red > 1.0) ? 1.0 : red;
        green = (green < 0.0) ? 0.0 : green;
        green = (green > 1.0) ? 1.0 : green;
        blue = (blue < 0.0) ? 0.0 : blue;
        blue = (blue > 1.0) ? 1.0 : blue;

        // Rescale the color channels to the noise::uint8 (0..255) range and return
        // the new color.
        Color newColor = new Color(
                (short) (red * 255.0),
                (short) (green * 255.0),
                (short) (blue * 255.0),
                Misc.GetMax(sourceColor.alpha, backgroundColor.alpha));
        return newColor;
    }

    /**
     * Calculates the intensity of the light given some elevation values.
     * <p/>
     * These values come directly from the noise map.
     *
     * @param center Elevation of the center point.
     * @param left   Elevation of the point directly left of the center point.
     * @param right  Elevation of the point directly right of the center point.
     * @param down   Elevation of the point directly below the center point.
     * @param up     Elevation of the point directly above the center point.
     * @return
     */
    private double calcLightIntensity(double center, double left, double right, double down, double up) {
        // Recalculate the sine and cosine of the various light values if
        // necessary so it does not have to be calculated each time this method is
        // called.
        if (recalcLightValues) {
            cosAzimuth = Math.cos(lightAzimuth * MathConst.DEG_TO_RAD);
            sinAzimuth = Math.sin(lightAzimuth * MathConst.DEG_TO_RAD);
            cosElev = Math.cos(lightElev * MathConst.DEG_TO_RAD);
            sinElev = Math.sin(lightElev * MathConst.DEG_TO_RAD);
            recalcLightValues = false;
        }

        // Now do the lighting calculations.
        double I_MAX = 1.0;
        double io = I_MAX * MathConst.SQRT_2 * sinElev / 2.0;
        double ix = (I_MAX - io) * lightContrast * MathConst.SQRT_2 * cosElev
                * cosAzimuth;
        double iy = (I_MAX - io) * lightContrast * MathConst.SQRT_2 * cosElev
                * sinAzimuth;
        double intensity = (ix * (left - right) + iy * (down - up) + io);
        if (intensity < 0.0) {
            intensity = 0.0;
        }
        return intensity;
    }
}
