package mandelbrot;

// generic multithreaded algorithm
// handles jobs which are segmented by pixels
public interface Worker<T_OUT> {
    T_OUT[] handleJob(JobEnds job);
}
