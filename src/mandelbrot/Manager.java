package mandelbrot;

import java.util.LinkedList;
import java.util.Queue;

// a generic thread manager that dictates jobs to workers
// an early design required the generics, which were kept for flexibility
public class Manager<T_OUT> {
    private final T_OUT[] output;
    private final Queue<JobEnds> jobs = new LinkedList<>();
    private final Worker<T_OUT> worker;

    private Manager(T_OUT[] output, Worker<T_OUT> worker) {
        this.output = output;
        this.worker = worker;

        // calculate out the jobs
        int PER_JOB = Configuration.NUM_PIXELS / Configuration.NUM_JOBS;
        for (int i = 0; i < Configuration.NUM_JOBS; i++) {
            // make jobs even size
            int start = PER_JOB * i;

            // ensure every pixel is used
            int end = (i == Configuration.NUM_JOBS - 1) ? Configuration.NUM_PIXELS : start + PER_JOB;
            jobs.add(new JobEnds(start, end));
        }
    }

    // synchronized method to combine the results of the worker threads
    public void pushOutput(T_OUT[] newOutput, JobEnds job) {
        synchronized (output) {
            if (job.finish - job.start >= 0) {
                System.arraycopy(newOutput, 0, output, job.start, job.finish - job.start);
            }
        }
    }

    // generic execution method
    private T_OUT[] execute() {
        // create the worker threads
        Thread[] threads = new Thread[Configuration.NUM_THREADS];

        for (int i = 0; i < threads.length; i++) {
            // add functionality to the threads
            threads[i] = new Thread(() -> {
                // loop until no new jobs
                while (true) {
                    JobEnds nextJob;
                    // thread-safe poll a new job
                    synchronized (jobs) {
                        nextJob = jobs.poll();
                    }
                    // there is no jobs left, so end the worker
                    if (nextJob == null) {
                        break;
                    }
                    // store the results of the worker
                    T_OUT[] newOutput = worker.handleJob(nextJob);
                    pushOutput(newOutput, nextJob);
                }
            });
            threads[i].start();
        }

        // wait for all workers to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return output;
    }

    // function for the mandelbrot
    public static PixelColor[] calculateSet() {
        PixelColor[] output = new PixelColor[Configuration.NUM_PIXELS];
        PixelLocation[] input = new PixelLocation[Configuration.NUM_PIXELS];
        for (int i = 0; i < Configuration.NUM_PIXELS; i++) {
            input[i] = new PixelLocation(i);
        }

        Manager<PixelColor> manager = new Manager<>(output, new MandelbrotWorker(input));
        return manager.execute();
    }

    // function for antialiasing
    public static PixelColor[] calculateAntialiasing(PixelColor[] input) {
        PixelColor[] output = new PixelColor[Configuration.NUM_PIXELS];
        PixelLocation[] locations = new PixelLocation[Configuration.NUM_PIXELS];
        for (int i = 0; i < Configuration.NUM_PIXELS; i++) {
            locations[i] = new PixelLocation(i);
        }

        Manager<PixelColor> manager = new Manager<>(output, new AntialiasingWorker(locations, input));
        return manager.execute();
    }
}
