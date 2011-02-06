package test.jLibNoise.noise;

import jLibNoise.noise.module.Perlin;
import jLibNoise.noise.utils.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

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
    public void tutorial3p1() throws Exception {
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
        writer.SetDestFilename("C:\\Users\\Andrew\\Desktop\\tutorial3_1.png");
        writer.WriteDestFile();
    }

    @Test
    public void tutorial3p2() throws Exception {
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
        writer.SetDestFilename("C:\\Users\\Andrew\\Desktop\\tutorial3_2.png");
        writer.WriteDestFile();
    }

    @Test
    public void tutorial8() throws Exception {
//        NoiseMap heightMap = new NoiseMap();
//        NoiseMapBuilderSphere heightMapBuilder = new NoiseMapBuilderSphere();
//        heightMapBuilder.SetSourceModule (myModule);
//        heightMapBuilder.SetDestNoiseMap (heightMap);
//
//        RendererImage renderer = new RendererImage();
//        Image image = new Image();
//        renderer.SetSourceNoiseMap(heightMap);
//        renderer.SetDestImage(image);
//        renderer.ClearGradient();
//        renderer.AddGradientPoint(-1.0000, new Color(0, 0, 128, 255)); // deeps
//        renderer.AddGradientPoint(-0.2500, new Color(0, 0, 255, 255)); // shallow
//        renderer.AddGradientPoint(0.0000, new Color(0, 128, 255, 255)); // shore
//        renderer.AddGradientPoint(0.0625, new Color(240, 240, 64, 255)); // sand
//        renderer.AddGradientPoint(0.1250, new Color(32, 160, 0, 255)); // grass
//        renderer.AddGradientPoint(0.3750, new Color(224, 224, 0, 255)); // dirt
//        renderer.AddGradientPoint(0.7500, new Color(128, 128, 128, 255)); // rock
//        renderer.AddGradientPoint(1.0000, new Color(255, 255, 255, 255)); // snow
//        renderer.EnableLight();
//        renderer.SetLightContrast(3.0);
//        renderer.SetLightBrightness(2.0);
//        renderer.Render();
//
//        WriterBMP writer = new WriterBMP();
//        writer.SetSourceImage(image);
//        writer.SetDestFilename("C:\\Users\\Andrew\\Desktop\\tutorial8.png");
//        writer.WriteDestFile();
    }
}
