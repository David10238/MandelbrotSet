package mandelbrot;

import java.io.FileWriter;
import java.io.IOException;

import static mandelbrot.Configuration.WIDTH;
import static mandelbrot.Configuration.HEIGHT;

// NetPBM renderer
public class Graphics {
    public static void drawPixels(PixelColor[] colors) {
        try {
            FileWriter writer = new FileWriter("mandelbrotImage.pbm");

            // write a header to the file
            writer.write(String.format("P3\n%d %d\n255\n", WIDTH, HEIGHT));

            // write the pixels
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    int n = y * WIDTH + x;
                    PixelColor pixel = colors[n];
                    writer.write(String.format("%d %d %d ", pixel.brightness, pixel.brightness, pixel.brightness));
                }
            }

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
