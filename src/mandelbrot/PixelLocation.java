package mandelbrot;

import static mandelbrot.Configuration.WIDTH;
import static mandelbrot.Configuration.HEIGHT;
import static mandelbrot.Configuration.MOVE_X;
import static mandelbrot.Configuration.MOVE_Y;
import static mandelbrot.Configuration.ZOOM;

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


        re = 1.5 * (x - WIDTH / 2.0) / (0.5 * ZOOM * WIDTH) + MOVE_X;
        im = (y - HEIGHT / 2.0) / (0.5 * ZOOM * HEIGHT) + MOVE_Y;
    }
}
