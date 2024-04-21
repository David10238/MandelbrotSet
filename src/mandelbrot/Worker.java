package mandelbrot;

public interface Worker<T_OUT> {
    T_OUT[] handleJob(JobEnds job);
}
