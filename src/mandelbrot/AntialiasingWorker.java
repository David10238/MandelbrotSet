package mandelbrot;

public class AntialiasingWorker implements Worker<PixelColor> {
    private final PixelColor[] input;

    public AntialiasingWorker(PixelColor[] input) {
        this.input = input;
    }

    @Override
    public PixelColor[] handleJob(JobEnds job) {
        // todo handle antialisting
        return new PixelColor[0];
    }
}
