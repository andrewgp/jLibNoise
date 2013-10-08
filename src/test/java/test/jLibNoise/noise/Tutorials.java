package test.jLibNoise.noise;

import jLibNoise.noise.module.Perlin;
import jLibNoise.noise.utils.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class Tutorials {
    
    @Test
    public void tutorial2() {
        Perlin myModule = new Perlin();

        double value = myModule.getValue(1.25, 0.75, 0.50);
        Assert.assertEquals(0.686347, value, 0.0000009);

        value = myModule.getValue(1.2501, 0.7501, 0.5001);
        Assert.assertEquals(0.685644, value, 0.0000009);

        value = myModule.getValue(14.50, 20.25, 75.75);
        Assert.assertEquals(-0.972999, value, 0.0000009);
    }

    @Test
    public void tutorial3a() throws Exception {
        Perlin myModule = new Perlin();
        NoiseMap heightMap = new NoiseMap();

        NoiseMapBuilderPlane heightMapBuilder = new NoiseMapBuilderPlane();
        heightMapBuilder.setSourceModule(myModule);
        heightMapBuilder.setDestNoiseMap(heightMap);
        heightMapBuilder.setDestSize(256, 256);
        heightMapBuilder.setBounds(2.0, 6.0, 1.0, 5.0);
        heightMapBuilder.build();

        RendererImage renderer = new RendererImage();
        Image image = new Image();
        renderer.setSourceNoiseMap(heightMap);
        renderer.setDestImage(image);
        renderer.render();

        WriterBMP writer = new WriterBMP();
        writer.setSourceImage(image);
        writer.setDestFilename("tutorial_3a.png");
        writer.writeDestFile();

        // test output against reference
        compareImages(new File("tutorial_3a.png"), "/META-INF/tutorials/tutorial_3a.png");
    }

    /**
     * Same as 3a but with gradient colours
     */
    @Test
    public void tutorial3b() throws Exception {
        Perlin myModule = new Perlin();
        NoiseMap heightMap = new NoiseMap();

        NoiseMapBuilderPlane heightMapBuilder = new NoiseMapBuilderPlane();
        heightMapBuilder.setSourceModule(myModule);
        heightMapBuilder.setDestNoiseMap(heightMap);
        heightMapBuilder.setDestSize(256, 256);
        heightMapBuilder.setBounds(2.0, 6.0, 1.0, 5.0);
        heightMapBuilder.build();

        RendererImage renderer = new RendererImage();
        Image image = new Image();
        renderer.setSourceNoiseMap(heightMap);
        renderer.setDestImage(image);
        renderer.clearGradient();
        renderer.addGradientPoint(-1.0000, new Color(0, 0, 128, 255)); // deeps
        renderer.addGradientPoint(-0.2500, new Color(0, 0, 255, 255)); // shallow
        renderer.addGradientPoint(0.0000, new Color(0, 128, 255, 255)); // shore
        renderer.addGradientPoint(0.0625, new Color(240, 240, 64, 255)); // sand
        renderer.addGradientPoint(0.1250, new Color(32, 160, 0, 255)); // grass
        renderer.addGradientPoint(0.3750, new Color(224, 224, 0, 255)); // dirt
        renderer.addGradientPoint(0.7500, new Color(128, 128, 128, 255)); // rock
        renderer.addGradientPoint(1.0000, new Color(255, 255, 255, 255)); // snow
        renderer.render();

        WriterBMP writer = new WriterBMP();
        writer.setSourceImage(image);
        writer.setDestFilename("tutorial_3b.png");
        writer.writeDestFile();

        // test output against reference
        compareImages(new File("tutorial_3b.png"), "/META-INF/tutorials/tutorial_3b.png");
    }

    /**
     * Same as 3b but with lighting enabled
     */
    @Test
    public void tutorial3c() throws Exception {
        Perlin myModule = new Perlin();
        NoiseMap heightMap = new NoiseMap();

        NoiseMapBuilderPlane heightMapBuilder = new NoiseMapBuilderPlane();
        heightMapBuilder.setSourceModule(myModule);
        heightMapBuilder.setDestNoiseMap(heightMap);
        heightMapBuilder.setDestSize(256, 256);
        heightMapBuilder.setBounds(2.0, 6.0, 1.0, 5.0);
        heightMapBuilder.build();

        RendererImage renderer = new RendererImage();
        Image image = new Image();
        renderer.setSourceNoiseMap(heightMap);
        renderer.setDestImage(image);
        renderer.clearGradient();
        renderer.addGradientPoint(-1.0000, new Color(0, 0, 128, 255)); // deeps
        renderer.addGradientPoint(-0.2500, new Color(0, 0, 255, 255)); // shallow
        renderer.addGradientPoint(0.0000, new Color(0, 128, 255, 255)); // shore
        renderer.addGradientPoint(0.0625, new Color(240, 240, 64, 255)); // sand
        renderer.addGradientPoint(0.1250, new Color(32, 160, 0, 255)); // grass
        renderer.addGradientPoint(0.3750, new Color(224, 224, 0, 255)); // dirt
        renderer.addGradientPoint(0.7500, new Color(128, 128, 128, 255)); // rock
        renderer.addGradientPoint(1.0000, new Color(255, 255, 255, 255)); // snow
        renderer.enableLight();
        renderer.setLightContrast(3.0); // Triple the contrast
        renderer.setLightBrightness(2.0); // Double the brightness
        renderer.render();

        WriterBMP writer = new WriterBMP();
        writer.setSourceImage(image);
        writer.setDestFilename("tutorial_3c.png");
        writer.writeDestFile();

        // test output against reference
        compareImages(new File("tutorial_3c.png"), "/META-INF/tutorials/tutorial_3c.png");
    }

    /**
     * Generates a lit tile to the east of the 3c's output
     */
    @Test
    public void tutorial3d() throws Exception {
        Perlin myModule = new Perlin();
        NoiseMap heightMap = new NoiseMap();

        NoiseMapBuilderPlane heightMapBuilder = new NoiseMapBuilderPlane();
        heightMapBuilder.setSourceModule(myModule);
        heightMapBuilder.setDestNoiseMap(heightMap);
        heightMapBuilder.setDestSize(256, 256);
        heightMapBuilder.setBounds(6.0, 10.0, 1.0, 5.0);
        heightMapBuilder.build();

        RendererImage renderer = new RendererImage();
        Image image = new Image();
        renderer.setSourceNoiseMap(heightMap);
        renderer.setDestImage(image);
        renderer.clearGradient();
        renderer.addGradientPoint(-1.0000, new Color(0, 0, 128, 255)); // deeps
        renderer.addGradientPoint(-0.2500, new Color(0, 0, 255, 255)); // shallow
        renderer.addGradientPoint(0.0000, new Color(0, 128, 255, 255)); // shore
        renderer.addGradientPoint(0.0625, new Color(240, 240, 64, 255)); // sand
        renderer.addGradientPoint(0.1250, new Color(32, 160, 0, 255)); // grass
        renderer.addGradientPoint(0.3750, new Color(224, 224, 0, 255)); // dirt
        renderer.addGradientPoint(0.7500, new Color(128, 128, 128, 255)); // rock
        renderer.addGradientPoint(1.0000, new Color(255, 255, 255, 255)); // snow
        renderer.enableLight();
        renderer.setLightContrast(3.0); // Triple the contrast
        renderer.setLightBrightness(2.0); // Double the brightness
        renderer.render();

        WriterBMP writer = new WriterBMP();
        writer.setSourceImage(image);
        writer.setDestFilename("tutorial_3d.png");
        writer.writeDestFile();

        // test output against reference
        compareImages(new File("tutorial_3d.png"), "/META-INF/tutorials/tutorial_3d.png");
    }

    /**
     * Generates 2 maps with different octaves for the noise generation (1 & 6)
     * <p/>
     * NOTE: Seems to fail for oct=1
     */
    @Test
    public void tutorial4a() throws Exception {
        Perlin myModule = new Perlin();
        myModule.setOctaveCount(1);
        NoiseMap heightMap = new NoiseMap();

        NoiseMapBuilderPlane heightMapBuilder = new NoiseMapBuilderPlane();
        heightMapBuilder.setSourceModule(myModule);
        heightMapBuilder.setDestNoiseMap(heightMap);
        heightMapBuilder.setDestSize(256, 256);
        heightMapBuilder.setBounds(6.0, 10.0, 1.0, 5.0);
        heightMapBuilder.build();

        RendererImage renderer = new RendererImage();
        Image image = new Image();
        renderer.setSourceNoiseMap(heightMap);
        renderer.setDestImage(image);
        renderer.clearGradient();
        renderer.addGradientPoint(-1.0000, new Color(0, 0, 128, 255)); // deeps
        renderer.addGradientPoint(-0.2500, new Color(0, 0, 255, 255)); // shallow
        renderer.addGradientPoint(0.0000, new Color(0, 128, 255, 255)); // shore
        renderer.addGradientPoint(0.0625, new Color(240, 240, 64, 255)); // sand
        renderer.addGradientPoint(0.1250, new Color(32, 160, 0, 255)); // grass
        renderer.addGradientPoint(0.3750, new Color(224, 224, 0, 255)); // dirt
        renderer.addGradientPoint(0.7500, new Color(128, 128, 128, 255)); // rock
        renderer.addGradientPoint(1.0000, new Color(255, 255, 255, 255)); // snow
        renderer.enableLight();
        renderer.setLightContrast(3.0); // Triple the contrast
        renderer.setLightBrightness(2.0); // Double the brightness
        renderer.render();

        WriterBMP writer = new WriterBMP();
        writer.setSourceImage(image);
        writer.setDestFilename("tutorial_4a_1.png");
        writer.writeDestFile();

        // test output against reference
        compareImages(new File("tutorial_4a_1.png"), "/META-INF/tutorials/tutorial_4a_1.png", 1);

        // now octave=6
        myModule.setOctaveCount(6);
        heightMap = new NoiseMap();
        heightMapBuilder.setDestNoiseMap(heightMap);
        heightMapBuilder.build();
        image = new Image();
        renderer.setSourceNoiseMap(heightMap);
        renderer.setDestImage(image);
        renderer.render();
        
        writer = new WriterBMP();
        writer.setSourceImage(image);
        writer.setDestFilename("tutorial_4a_6.png");
        writer.writeDestFile();

        // test output against reference
        compareImages(new File("tutorial_4a_6.png"), "/META-INF/tutorials/tutorial_4a_6.png");
    }

    /**
     * Generates 2 maps with different frequencies for the noise generation (1 & 6)
     */
    @Test
    public void tutorial4b() throws Exception {
        Perlin myModule = new Perlin();
        myModule.setOctaveCount(6);
        myModule.setFrequency(1);
        NoiseMap heightMap = new NoiseMap();

        NoiseMapBuilderPlane heightMapBuilder = new NoiseMapBuilderPlane();
        heightMapBuilder.setSourceModule(myModule);
        heightMapBuilder.setDestNoiseMap(heightMap);
        heightMapBuilder.setDestSize(256, 256);
        heightMapBuilder.setBounds(6.0, 10.0, 1.0, 5.0);
        heightMapBuilder.build();

        RendererImage renderer = new RendererImage();
        Image image = new Image();
        renderer.setSourceNoiseMap(heightMap);
        renderer.setDestImage(image);
        renderer.clearGradient();
        renderer.addGradientPoint(-1.0000, new Color(0, 0, 128, 255)); // deeps
        renderer.addGradientPoint(-0.2500, new Color(0, 0, 255, 255)); // shallow
        renderer.addGradientPoint(0.0000, new Color(0, 128, 255, 255)); // shore
        renderer.addGradientPoint(0.0625, new Color(240, 240, 64, 255)); // sand
        renderer.addGradientPoint(0.1250, new Color(32, 160, 0, 255)); // grass
        renderer.addGradientPoint(0.3750, new Color(224, 224, 0, 255)); // dirt
        renderer.addGradientPoint(0.7500, new Color(128, 128, 128, 255)); // rock
        renderer.addGradientPoint(1.0000, new Color(255, 255, 255, 255)); // snow
        renderer.enableLight();
        renderer.setLightContrast(3.0); // Triple the contrast
        renderer.setLightBrightness(2.0); // Double the brightness
        renderer.render();

        WriterBMP writer = new WriterBMP();
        writer.setSourceImage(image);
        writer.setDestFilename("tutorial_4b_1.png");
        writer.writeDestFile();

        // test output against reference
        compareImages(new File("tutorial_4b_1.png"), "/META-INF/tutorials/tutorial_4b_1.png");

        // now freq=6
        myModule.setFrequency(6);
        heightMapBuilder.build();
        renderer.render();
        writer.setDestFilename("tutorial_4b_6.png");
        writer.writeDestFile();

        // test output against reference
        compareImages(new File("tutorial_4b_6.png"), "/META-INF/tutorials/tutorial_4b_6.png");
    }

    /**
     * Generates 2 maps with different persistence's for the noise generation (1 & 6)
     * <p/>
     * NOTE: Both seem to fail
     */
    @Test
    public void tutorial4c() throws Exception {
        Perlin myModule = new Perlin();
        myModule.setOctaveCount(6);
        myModule.setFrequency(1);
        myModule.setPersistence(0.25);
        NoiseMap heightMap = new NoiseMap();

        NoiseMapBuilderPlane heightMapBuilder = new NoiseMapBuilderPlane();
        heightMapBuilder.setSourceModule(myModule);
        heightMapBuilder.setDestNoiseMap(heightMap);
        heightMapBuilder.setDestSize(256, 256);
        heightMapBuilder.setBounds(6.0, 10.0, 1.0, 5.0);
        heightMapBuilder.build();

        RendererImage renderer = new RendererImage();
        Image image = new Image();
        renderer.setSourceNoiseMap(heightMap);
        renderer.setDestImage(image);
        renderer.clearGradient();
        renderer.addGradientPoint(-1.0000, new Color(0, 0, 128, 255)); // deeps
        renderer.addGradientPoint(-0.2500, new Color(0, 0, 255, 255)); // shallow
        renderer.addGradientPoint(0.0000, new Color(0, 128, 255, 255)); // shore
        renderer.addGradientPoint(0.0625, new Color(240, 240, 64, 255)); // sand
        renderer.addGradientPoint(0.1250, new Color(32, 160, 0, 255)); // grass
        renderer.addGradientPoint(0.3750, new Color(224, 224, 0, 255)); // dirt
        renderer.addGradientPoint(0.7500, new Color(128, 128, 128, 255)); // rock
        renderer.addGradientPoint(1.0000, new Color(255, 255, 255, 255)); // snow
        renderer.enableLight();
        renderer.setLightContrast(3.0); // Triple the contrast
        renderer.setLightBrightness(2.0); // Double the brightness
        renderer.render();

        WriterBMP writer = new WriterBMP();
        writer.setSourceImage(image);
        writer.setDestFilename("tutorial_4c_25.png");
        writer.writeDestFile();

        // test output against reference
        compareImages(new File("tutorial_4c_25.png"), "/META-INF/tutorials/tutorial_4c_25.png", 1);

        // now per=0.75
        myModule.setPersistence(0.75);
        heightMapBuilder.build();
        renderer.render();
        writer.setDestFilename("tutorial_4c_75.png");
        writer.writeDestFile();

        // test output against reference
        compareImages(new File("tutorial_4c_75.png"), "/META-INF/tutorials/tutorial_4c_75.png", 1);
    }

    @Test
    public void tutorial8() throws Exception {
        Perlin myModule = new Perlin();
        myModule.setOctaveCount(10);

        NoiseMap heightMap = new NoiseMap();
        NoiseMapBuilderSphere heightMapBuilder = new NoiseMapBuilderSphere();
        heightMapBuilder.setSourceModule(myModule);
        heightMapBuilder.setDestNoiseMap(heightMap);
        heightMapBuilder.setDestSize(512, 256);
        heightMapBuilder.setBounds(-90.0, 90.0, -180.0, 180.0);
        heightMapBuilder.build();

        RendererImage renderer = new RendererImage();
        Image image = new Image();
        renderer.setSourceNoiseMap(heightMap);
        renderer.setDestImage(image);
        renderer.clearGradient();
        renderer.addGradientPoint(-1.0000, new Color(0, 0, 128, 255)); // deeps
        renderer.addGradientPoint(-0.2500, new Color(0, 0, 255, 255)); // shallow
        renderer.addGradientPoint(0.0000, new Color(0, 128, 255, 255)); // shore
        renderer.addGradientPoint(0.0625, new Color(240, 240, 64, 255)); // sand
        renderer.addGradientPoint(0.1250, new Color(32, 160, 0, 255)); // grass
        renderer.addGradientPoint(0.3750, new Color(224, 224, 0, 255)); // dirt
        renderer.addGradientPoint(0.7500, new Color(128, 128, 128, 255)); // rock
        renderer.addGradientPoint(1.0000, new Color(255, 255, 255, 255)); // snow
        renderer.enableLight();
        renderer.setLightContrast(3.0);
        renderer.setLightBrightness(2.0);
        renderer.render();

        WriterBMP writer = new WriterBMP();
        writer.setSourceImage(image);
        writer.setDestFilename("tutorial_8.png");
        writer.writeDestFile();

        // test output against reference
        compareImages(new File("tutorial_8.png"), "/META-INF/tutorials/tutorial_8.png");
    }

    private void compareImages(File fileA, String fileB) throws IOException {
        compareImages(fileA, fileB, 0);
    }
    
    private void compareImages(File fileA, String fileB, int tolerance) throws IOException {
        // load both images
        BufferedImage imgA = ImageIO.read(fileA);

        InputStream inB = getClass().getResourceAsStream(fileB);
        BufferedImage imgB = ImageIO.read(inB);
        inB.close();

        // compare sizes
        Assert.assertEquals(imgA.getWidth(), imgB.getWidth());
        Assert.assertEquals(imgB.getHeight(), imgB.getHeight());

        Assert.assertEquals(4, imgA.getColorModel().getNumComponents());
        Assert.assertEquals(4, imgB.getColorModel().getNumComponents());

        // compare pixels line by line
        int[] lineBufA = new int[imgA.getWidth() * 4];
        int[] lineBufB = new int[imgB.getWidth() * 4];
        for (int y = 0; y < imgA.getHeight(); y++) {
            // get lines
            imgA.getData().getPixels(0, y, imgA.getWidth(), 1, lineBufA);
            imgB.getData().getPixels(0, y, imgB.getWidth(), 1, lineBufB);

            for (int x = 0, i = 0; x < imgA.getWidth(); x++, i += 4) {
                int redA = lineBufA[i];
                int redB = lineBufB[i];
                int greenA = lineBufA[i + 1];
                int greenB = lineBufB[i + 1];
                int blueA = lineBufA[i + 2];
                int blueB = lineBufB[i + 2];
                int alphaA = lineBufA[i + 3];
                int alphaB = lineBufB[i + 3];

                if (redA != redB || greenA != greenB || blueA != blueB || alphaA != alphaB) {
                    // now check using the tolerance
                    if (redA < redB - tolerance || redA > redB + tolerance
                        || greenA < greenB - tolerance || greenA > greenB + tolerance
                        || blueA < blueB - tolerance || blueA > blueB + tolerance
                        || alphaA < alphaB - tolerance || alphaA > alphaB + tolerance) {
                        Assert.fail(String.format("Pixel mismatch @ %s,%s (expecting=%s,%s,%s,%s actual=%s,%s,%s,%s)",
                                                  x + 1, y + 1, redA, greenA, blueA, alphaA, redB, greenB, blueB, alphaB));
                    }
                }
            }
        }
    }
}
