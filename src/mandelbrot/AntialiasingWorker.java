package mandelbrot;

import javax.xml.stream.Location;
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
        // provide double weight to actual pixel
        int count = 1;
        int sum = input[location.n].brightness;

        // average a 3x3 area
        for (int x = location.x - 1; x <= location.x + 1; x++) {
            for (int y = location.y - 1; y <= location.y + 1; y++) {
                // keep within bounds
                if (PixelLocation.exists(x, y)) {
                    int n = PixelLocation.toLocation(x, y);
                    sum += input[n].brightness;
                    count++;
                }
            }
        }

        return new PixelColor(sum / count);
    }
}
