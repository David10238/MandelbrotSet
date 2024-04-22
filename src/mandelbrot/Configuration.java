package mandelbrot;

// global configurations
public class Configuration {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;

    // these are set for CLI arguments
    public static double ZOOM = 1.0;
    public static double MOVE_X = 0.0;
    public static double MOVE_Y = 0.0;

    public static final int NUM_PIXELS = WIDTH * HEIGHT;

    public static final int NUM_JOBS = NUM_PIXELS / 100;
    public static final int NUM_THREADS = 8;
}
