package test.jLibNoise.noise;

import jLibNoise.noise.module.Perlin;
import jLibNoise.noise.utils.*;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class Tutorials {

    @Test
    public void tutorial2() {
        Perlin myModule = new Perlin();

        double value = myModule.GetValue(1.25, 0.75, 0.50);
        Assert.assertEquals(0.686347, value, 0.0000009);

        value = myModule.GetValue(1.2501, 0.7501, 0.5001);
        Assert.assertEquals(0.685644, value, 0.0000009);

        value = myModule.GetValue(14.50, 20.25, 75.75);
        Assert.assertEquals(-0.972999, value, 0.0000009);
    }

    @Test
    public void tutorial3a() throws Exception {
        Perlin myModule = new Perlin();
        NoiseMap heightMap = new NoiseMap();

        NoiseMapBuilderPlane heightMapBuilder = new NoiseMapBuilderPlane();
        heightMapBuilder.SetSourceModule(myModule);
        heightMapBuilder.SetDestNoiseMap(heightMap);
        heightMapBuilder.SetDestSize(256, 256);
        heightMapBuilder.SetBounds(2.0, 6.0, 1.0, 5.0);
        heightMapBuilder.Build();

        RendererImage renderer = new RendererImage();
        Image image = new Image();
        renderer.SetSourceNoiseMap(heightMap);
        renderer.SetDestImage(image);
        renderer.Render();

        WriterBMP writer = new WriterBMP();
        writer.SetSourceImage(image);
        writer.SetDestFilename("tutorial_3a.png");
        writer.WriteDestFile();

        // test output against reference
        CompareImages(new File("tutorial_3a.png"), "/META-INF/tutorials/tutorial_3a.png");
    }

    /**
     * Same as 3a but with gradient colours
     */
    @Test
    public void tutorial3b() throws Exception {
        Perlin myModule = new Perlin();
        NoiseMap heightMap = new NoiseMap();

        NoiseMapBuilderPlane heightMapBuilder = new NoiseMapBuilderPlane();
        heightMapBuilder.SetSourceModule(myModule);
        heightMapBuilder.SetDestNoiseMap(heightMap);
        heightMapBuilder.SetDestSize(256, 256);
        heightMapBuilder.SetBounds(2.0, 6.0, 1.0, 5.0);
        heightMapBuilder.Build();

        RendererImage renderer = new RendererImage();
        Image image = new Image();
        renderer.SetSourceNoiseMap(heightMap);
        renderer.SetDestImage(image);
        renderer.ClearGradient();
        renderer.AddGradientPoint(-1.0000, new Color(0, 0, 128, 255)); // deeps
        renderer.AddGradientPoint(-0.2500, new Color(0, 0, 255, 255)); // shallow
        renderer.AddGradientPoint(0.0000, new Color(0, 128, 255, 255)); // shore
        renderer.AddGradientPoint(0.0625, new Color(240, 240, 64, 255)); // sand
        renderer.AddGradientPoint(0.1250, new Color(32, 160, 0, 255)); // grass
        renderer.AddGradientPoint(0.3750, new Color(224, 224, 0, 255)); // dirt
        renderer.AddGradientPoint(0.7500, new Color(128, 128, 128, 255)); // rock
        renderer.AddGradientPoint(1.0000, new Color(255, 255, 255, 255)); // snow
        renderer.Render();

        WriterBMP writer = new WriterBMP();
        writer.SetSourceImage(image);
        writer.SetDestFilename("tutorial_3b.png");
        writer.WriteDestFile();

        // test output against reference
        CompareImages(new File("tutorial_3b.png"), "/META-INF/tutorials/tutorial_3b.png");
    }

    /**
     * Same as 3b but with lighting enabled
     */
    @Test
    public void tutorial3c() throws Exception {
        Perlin myModule = new Perlin();
        NoiseMap heightMap = new NoiseMap();

        NoiseMapBuilderPlane heightMapBuilder = new NoiseMapBuilderPlane();
        heightMapBuilder.SetSourceModule(myModule);
        heightMapBuilder.SetDestNoiseMap(heightMap);
        heightMapBuilder.SetDestSize(256, 256);
        heightMapBuilder.SetBounds(2.0, 6.0, 1.0, 5.0);
        heightMapBuilder.Build();

        RendererImage renderer = new RendererImage();
        Image image = new Image();
        renderer.SetSourceNoiseMap(heightMap);
        renderer.SetDestImage(image);
        renderer.ClearGradient();
        renderer.AddGradientPoint(-1.0000, new Color(0, 0, 128, 255)); // deeps
        renderer.AddGradientPoint(-0.2500, new Color(0, 0, 255, 255)); // shallow
        renderer.AddGradientPoint(0.0000, new Color(0, 128, 255, 255)); // shore
        renderer.AddGradientPoint(0.0625, new Color(240, 240, 64, 255)); // sand
        renderer.AddGradientPoint(0.1250, new Color(32, 160, 0, 255)); // grass
        renderer.AddGradientPoint(0.3750, new Color(224, 224, 0, 255)); // dirt
        renderer.AddGradientPoint(0.7500, new Color(128, 128, 128, 255)); // rock
        renderer.AddGradientPoint(1.0000, new Color(255, 255, 255, 255)); // snow
        renderer.EnableLight();
        renderer.SetLightContrast(3.0); // Triple the contrast
        renderer.SetLightBrightness(2.0); // Double the brightness
        renderer.Render();

        WriterBMP writer = new WriterBMP();
        writer.SetSourceImage(image);
        writer.SetDestFilename("tutorial_3c.png");
        writer.WriteDestFile();

        // test output against reference
        CompareImages(new File("tutorial_3c.png"), "/META-INF/tutorials/tutorial_3c.png");
    }

    /**
     * Generates a lit tile to the east of the 3c's output
     */
    @Test
    public void tutorial3d() throws Exception {
        Perlin myModule = new Perlin();
        NoiseMap heightMap = new NoiseMap();

        NoiseMapBuilderPlane heightMapBuilder = new NoiseMapBuilderPlane();
        heightMapBuilder.SetSourceModule(myModule);
        heightMapBuilder.SetDestNoiseMap(heightMap);
        heightMapBuilder.SetDestSize(256, 256);
        heightMapBuilder.SetBounds(6.0, 10.0, 1.0, 5.0);
        heightMapBuilder.Build();

        RendererImage renderer = new RendererImage();
        Image image = new Image();
        renderer.SetSourceNoiseMap(heightMap);
        renderer.SetDestImage(image);
        renderer.ClearGradient();
        renderer.AddGradientPoint(-1.0000, new Color(0, 0, 128, 255)); // deeps
        renderer.AddGradientPoint(-0.2500, new Color(0, 0, 255, 255)); // shallow
        renderer.AddGradientPoint(0.0000, new Color(0, 128, 255, 255)); // shore
        renderer.AddGradientPoint(0.0625, new Color(240, 240, 64, 255)); // sand
        renderer.AddGradientPoint(0.1250, new Color(32, 160, 0, 255)); // grass
        renderer.AddGradientPoint(0.3750, new Color(224, 224, 0, 255)); // dirt
        renderer.AddGradientPoint(0.7500, new Color(128, 128, 128, 255)); // rock
        renderer.AddGradientPoint(1.0000, new Color(255, 255, 255, 255)); // snow
        renderer.EnableLight();
        renderer.SetLightContrast(3.0); // Triple the contrast
        renderer.SetLightBrightness(2.0); // Double the brightness
        renderer.Render();

        WriterBMP writer = new WriterBMP();
        writer.SetSourceImage(image);
        writer.SetDestFilename("tutorial_3d.png");
        writer.WriteDestFile();

        // test output against reference
        CompareImages(new File("tutorial_3d.png"), "/META-INF/tutorials/tutorial_3d.png");
    }

    /**
     * Generates 2 maps with different octaves for the noise generation (1 & 6)
     * <p/>
     * NOTE: Seems to fail for oct=1
     */
    @Test
    public void tutorial4a() throws Exception {
        Perlin myModule = new Perlin();
        myModule.SetOctaveCount(1);
        NoiseMap heightMap = new NoiseMap();

        NoiseMapBuilderPlane heightMapBuilder = new NoiseMapBuilderPlane();
        heightMapBuilder.SetSourceModule(myModule);
        heightMapBuilder.SetDestNoiseMap(heightMap);
        heightMapBuilder.SetDestSize(256, 256);
        heightMapBuilder.SetBounds(6.0, 10.0, 1.0, 5.0);
        heightMapBuilder.Build();

        RendererImage renderer = new RendererImage();
        Image image = new Image();
        renderer.SetSourceNoiseMap(heightMap);
        renderer.SetDestImage(image);
        renderer.ClearGradient();
        renderer.AddGradientPoint(-1.0000, new Color(0, 0, 128, 255)); // deeps
        renderer.AddGradientPoint(-0.2500, new Color(0, 0, 255, 255)); // shallow
        renderer.AddGradientPoint(0.0000, new Color(0, 128, 255, 255)); // shore
        renderer.AddGradientPoint(0.0625, new Color(240, 240, 64, 255)); // sand
        renderer.AddGradientPoint(0.1250, new Color(32, 160, 0, 255)); // grass
        renderer.AddGradientPoint(0.3750, new Color(224, 224, 0, 255)); // dirt
        renderer.AddGradientPoint(0.7500, new Color(128, 128, 128, 255)); // rock
        renderer.AddGradientPoint(1.0000, new Color(255, 255, 255, 255)); // snow
        renderer.EnableLight();
        renderer.SetLightContrast(3.0); // Triple the contrast
        renderer.SetLightBrightness(2.0); // Double the brightness
        renderer.Render();

        WriterBMP writer = new WriterBMP();
        writer.SetSourceImage(image);
        writer.SetDestFilename("tutorial_4a_1.png");
        writer.WriteDestFile();

        // test output against reference
        CompareImages(new File("tutorial_4a_1.png"), "/META-INF/tutorials/tutorial_4a_1.png");

        // now octave=6
        myModule.SetOctaveCount(6);
        heightMap = new NoiseMap();
        heightMapBuilder.Build();
        image = new Image();
        renderer.Render();
        writer.SetDestFilename("tutorial_4a_6.png");
        writer.WriteDestFile();

        // test output against reference
        CompareImages(new File("tutorial_4a_6.png"), "/META-INF/tutorials/tutorial_4a_6.png");
    }

    /**
     * Generates 2 maps with different frequencies for the noise generation (1 & 6)
     */
    @Test
    public void tutorial4b() throws Exception {
        Perlin myModule = new Perlin();
        myModule.SetOctaveCount(6);
        myModule.SetFrequency(1);
        NoiseMap heightMap = new NoiseMap();

        NoiseMapBuilderPlane heightMapBuilder = new NoiseMapBuilderPlane();
        heightMapBuilder.SetSourceModule(myModule);
        heightMapBuilder.SetDestNoiseMap(heightMap);
        heightMapBuilder.SetDestSize(256, 256);
        heightMapBuilder.SetBounds(6.0, 10.0, 1.0, 5.0);
        heightMapBuilder.Build();

        RendererImage renderer = new RendererImage();
        Image image = new Image();
        renderer.SetSourceNoiseMap(heightMap);
        renderer.SetDestImage(image);
        renderer.ClearGradient();
        renderer.AddGradientPoint(-1.0000, new Color(0, 0, 128, 255)); // deeps
        renderer.AddGradientPoint(-0.2500, new Color(0, 0, 255, 255)); // shallow
        renderer.AddGradientPoint(0.0000, new Color(0, 128, 255, 255)); // shore
        renderer.AddGradientPoint(0.0625, new Color(240, 240, 64, 255)); // sand
        renderer.AddGradientPoint(0.1250, new Color(32, 160, 0, 255)); // grass
        renderer.AddGradientPoint(0.3750, new Color(224, 224, 0, 255)); // dirt
        renderer.AddGradientPoint(0.7500, new Color(128, 128, 128, 255)); // rock
        renderer.AddGradientPoint(1.0000, new Color(255, 255, 255, 255)); // snow
        renderer.EnableLight();
        renderer.SetLightContrast(3.0); // Triple the contrast
        renderer.SetLightBrightness(2.0); // Double the brightness
        renderer.Render();

        WriterBMP writer = new WriterBMP();
        writer.SetSourceImage(image);
        writer.SetDestFilename("tutorial_4b_1.png");
        writer.WriteDestFile();

        // test output against reference
        CompareImages(new File("tutorial_4b_1.png"), "/META-INF/tutorials/tutorial_4b_1.png");

        // now freq=6
        myModule.SetFrequency(6);
        heightMap = new NoiseMap();
        heightMapBuilder.Build();
        image = new Image();
        renderer.Render();
        writer.SetDestFilename("tutorial_4b_6.png");
        writer.WriteDestFile();

        // test output against reference
        CompareImages(new File("tutorial_4b_6.png"), "/META-INF/tutorials/tutorial_4b_6.png");
    }

    /**
     * Generates 2 maps with different persistence's for the noise generation (1 & 6)
     * <p/>
     * NOTE: Both seem to fail
     */
    @Test
    public void tutorial4c() throws Exception {
        Perlin myModule = new Perlin();
        myModule.SetOctaveCount(6);
        myModule.SetFrequency(1);
        myModule.SetPersistence(0.25);
        NoiseMap heightMap = new NoiseMap();

        NoiseMapBuilderPlane heightMapBuilder = new NoiseMapBuilderPlane();
        heightMapBuilder.SetSourceModule(myModule);
        heightMapBuilder.SetDestNoiseMap(heightMap);
        heightMapBuilder.SetDestSize(256, 256);
        heightMapBuilder.SetBounds(6.0, 10.0, 1.0, 5.0);
        heightMapBuilder.Build();

        RendererImage renderer = new RendererImage();
        Image image = new Image();
        renderer.SetSourceNoiseMap(heightMap);
        renderer.SetDestImage(image);
        renderer.ClearGradient();
        renderer.AddGradientPoint(-1.0000, new Color(0, 0, 128, 255)); // deeps
        renderer.AddGradientPoint(-0.2500, new Color(0, 0, 255, 255)); // shallow
        renderer.AddGradientPoint(0.0000, new Color(0, 128, 255, 255)); // shore
        renderer.AddGradientPoint(0.0625, new Color(240, 240, 64, 255)); // sand
        renderer.AddGradientPoint(0.1250, new Color(32, 160, 0, 255)); // grass
        renderer.AddGradientPoint(0.3750, new Color(224, 224, 0, 255)); // dirt
        renderer.AddGradientPoint(0.7500, new Color(128, 128, 128, 255)); // rock
        renderer.AddGradientPoint(1.0000, new Color(255, 255, 255, 255)); // snow
        renderer.EnableLight();
        renderer.SetLightContrast(3.0); // Triple the contrast
        renderer.SetLightBrightness(2.0); // Double the brightness
        renderer.Render();

        WriterBMP writer = new WriterBMP();
        writer.SetSourceImage(image);
        writer.SetDestFilename("tutorial_4c_25.png");
        writer.WriteDestFile();

        // test output against reference
        CompareImages(new File("tutorial_4c_25.png"), "/META-INF/tutorials/tutorial_4c_25.png");

        // now per=0.75
        myModule.SetPersistence(0.75);
        heightMap = new NoiseMap();
        heightMapBuilder.Build();
        image = new Image();
        renderer.Render();
        writer.SetDestFilename("tutorial_4c_75.png");
        writer.WriteDestFile();

        // test output against reference
        CompareImages(new File("tutorial_4c_75.png"), "/META-INF/tutorials/tutorial_4c_75.png");
    }

    @Test
    public void tutorial8() throws Exception {
        Perlin myModule = new Perlin();

        NoiseMap heightMap = new NoiseMap();
        NoiseMapBuilderSphere heightMapBuilder = new NoiseMapBuilderSphere();
        heightMapBuilder.SetSourceModule(myModule);
        heightMapBuilder.SetDestNoiseMap(heightMap);
        heightMapBuilder.SetDestSize(512, 256);
        heightMapBuilder.SetBounds(-90.0, 90.0, -180.0, 180.0);
        heightMapBuilder.Build();

        RendererImage renderer = new RendererImage();
        Image image = new Image();
        renderer.SetSourceNoiseMap(heightMap);
        renderer.SetDestImage(image);
        renderer.ClearGradient();
        renderer.AddGradientPoint(-1.0000, new Color(0, 0, 128, 255)); // deeps
        renderer.AddGradientPoint(-0.2500, new Color(0, 0, 255, 255)); // shallow
        renderer.AddGradientPoint(0.0000, new Color(0, 128, 255, 255)); // shore
        renderer.AddGradientPoint(0.0625, new Color(240, 240, 64, 255)); // sand
        renderer.AddGradientPoint(0.1250, new Color(32, 160, 0, 255)); // grass
        renderer.AddGradientPoint(0.3750, new Color(224, 224, 0, 255)); // dirt
        renderer.AddGradientPoint(0.7500, new Color(128, 128, 128, 255)); // rock
        renderer.AddGradientPoint(1.0000, new Color(255, 255, 255, 255)); // snow
        renderer.EnableLight();
        renderer.SetLightContrast(3.0);
        renderer.SetLightBrightness(2.0);
        renderer.Render();

        WriterBMP writer = new WriterBMP();
        writer.SetSourceImage(image);
        writer.SetDestFilename("tutorial_8.png");
        writer.WriteDestFile();

        // test output against reference
        CompareImages(new File("tutorial_8.png"), "/META-INF/tutorials/tutorial_8.png");
    }

    private void CompareImages(File fileA, String fileB) throws IOException {
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

                if (redA != redB || greenA != greenB || blueA != blueB || alphaA != alphaB)
                    Assert.fail(String.format("Pixel mismatch @ %s,%s (expecting=%s,%s,%s,%s actual=%s,%s,%s,%s)",
                            x + 1, y + 1, redA, redB, greenA, greenB, blueA, blueB, alphaA, alphaB));
            }
        }
    }
}
