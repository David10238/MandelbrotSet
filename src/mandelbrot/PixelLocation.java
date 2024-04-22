package mandelbrot;

import static mandelbrot.Configuration.WIDTH;
import static mandelbrot.Configuration.HEIGHT;
import static mandelbrot.Configuration.MOVE_X;
import static mandelbrot.Configuration.MOVE_Y;
import static mandelbrot.Configuration.ZOOM;

// class for translating between indexes and x and y positions
public class PixelLocation {
    public final int n;
    public final int x;
    public final int y;

    public final double re;
    public final double im;

    public PixelLocation(int n) {
        this.n = n;
        x = n % WIDTH;
        y = n / WIDTH;

        // calculate real and imaginary parts based on width, height, location and zoom
        re = 1.5 * (x - WIDTH / 2.0) / (0.5 * ZOOM * WIDTH) + MOVE_X;
        im = (y - HEIGHT / 2.0) / (0.5 * ZOOM * HEIGHT) + MOVE_Y;
    }

    // reverse x and y to index. this is used to lookup pixels
    public static int toLocation(int x, int y) {
        return x + y * WIDTH;
    }
}
