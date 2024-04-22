package mandelbrot;

import java.util.ArrayList;

public class AntialiasingWorker implements Worker<PixelColor> {
    // as read-only synchronization isn't needed
    private final PixelColor[] input;
    private final PixelLocation[] locations;

    public AntialiasingWorker(PixelLocation[] locations, PixelColor[] input) {
        this.input = input;
        this.locations = locations;
    }

    @Override
    public PixelColor[] handleJob(JobEnds job) {
        PixelColor[] output = new PixelColor[job.finish - job.start];

        for (int i = job.start; i < job.finish; i++) {
            output[i - job.start] = calculatePixel(locations[i]);
        }

        return output;
    }

    private PixelColor calculatePixel(PixelLocation location) {
        // find pixels in a 4x4 area
        ArrayList<Integer> pixels = new ArrayList<>();
        pixels.add(location.n);
        if (location.x + 1 < Configuration.WIDTH) {
            pixels.add(PixelLocation.toLocation(location.x + 1, location.y));
        }
        if (location.y + 1 < Configuration.HEIGHT) {
            pixels.add(PixelLocation.toLocation(location.x, location.y + 1));
        }
        if (location.x + 1 < Configuration.WIDTH && location.y + 1 < Configuration.HEIGHT) {
            pixels.add(PixelLocation.toLocation(location.x + 1, location.y + 1));
        }

        // calculate the average
        int sum = 0;
        for (int i : pixels) {
            sum += input[i].brightness;
        }

        return new PixelColor(sum / pixels.size());
    }
}
