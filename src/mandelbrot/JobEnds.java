package mandelbrot;

// class that stores how pixels are split into jobs
public class JobEnds {
    public final int start; // inclusive
    public final int finish; // exclusive

    public JobEnds(int start, int finish) {
        this.start = start;
        this.finish = finish;
    }
}
