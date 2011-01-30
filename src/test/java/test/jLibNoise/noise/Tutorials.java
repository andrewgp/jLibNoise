package test.jLibNoise.noise;

import jLibNoise.noise.module.Perlin;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class Tutorials {

    @Test
    public void tutorial2() {
        Perlin myModule = new Perlin();
        
        double value = myModule.GetValue(1.25, 0.75, 0.50);
        Assert.assertEquals(0.686347, value, 0.0000009);

        value = myModule.GetValue (1.2501, 0.7501, 0.5001);
        Assert.assertEquals(0.685644, value, 0.0000009);

        value = myModule.GetValue (14.50, 20.25, 75.75);
        Assert.assertEquals(-0.972999, value, 0.0000009);
    }

    @Test
    public void tutorial3() {
        Perlin myModule = new Perlin();
        NoiseMap heightMap;
    }
}
