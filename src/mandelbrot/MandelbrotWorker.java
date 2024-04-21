package mandelbrot;

public class MandelbrotWorker implements Worker<PixelColor> {
    private final int MAX_ITERATIONS = 1000;
    private final PixelLocation[] input;

    public MandelbrotWorker(PixelLocation[] input) {
        this.input = input;
    }

    @Override
    public PixelColor[] handleJob(JobEnds job) {
        PixelColor[] output = new PixelColor[job.finish - job.start];

        for (int i = job.start; i < job.finish; i++) {
            output[i - job.start] = calculatePixel(input[i]);
        }

        return output;
    }

    private PixelColor calculatePixel(PixelLocation pixel) {
        double newRe = 0, newIm = 0, oldRe, oldIm;
        int i;
        for (i = 0; i < MAX_ITERATIONS; i++) {
            //remember value of previous iteration
            oldRe = newRe;
            oldIm = newIm;

            //the actual iteration, the real and imaginary part are calculated
            newRe = oldRe * oldRe - oldIm * oldIm + pixel.re;
            newIm = 2 * oldRe * oldIm + pixel.im;

            //if the point is outside the circle with radius 2, stop
            if (Math.hypot(newRe, newIm) > 2) break;
        }

        double z = Math.hypot(newRe, newIm);
        double brightness = 256 * log2(1.75 + i - log2(log2(z))) / log2(MAX_ITERATIONS);
        return new PixelColor((int) brightness);
    }

    private double log2(double n) {
        return Math.log(n) / Math.log(2);
    }
}
