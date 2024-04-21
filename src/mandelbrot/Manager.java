package mandelbrot;

import java.util.LinkedList;
import java.util.Queue;

public class Manager<T_OUT> {
    private final T_OUT[] output;
    private final Queue<JobEnds> jobs = new LinkedList<>();
    private final Worker<T_OUT> worker;

    private Manager(T_OUT[] output, Worker<T_OUT> worker) {
        this.output = output;
        this.worker = worker;

        int PER_JOB = Configuration.NUM_PIXELS / Configuration.NUM_JOBS;
        for (int i = 0; i < Configuration.NUM_JOBS; i++) {
            int start = PER_JOB * i;
            int end = (i == Configuration.NUM_JOBS - 1) ? Configuration.NUM_PIXELS : start + PER_JOB;
            jobs.add(new JobEnds(start, end));
        }
    }

    public void pushOutput(T_OUT[] newOutput, JobEnds job) {
        synchronized (output) {
            for (int i = job.start; i < job.finish; i++) {
                output[i] = newOutput[i - job.start];
            }
        }
    }

    private T_OUT[] execute() {
        Thread[] threads = new Thread[Configuration.NUM_THREADS];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                while (true) {
                    JobEnds nextJob;
                    synchronized (jobs) {
                        nextJob = jobs.poll();
                    }
                    if (nextJob == null) {
                        break;
                    }
                    T_OUT[] newOutput = worker.handleJob(nextJob);
                    pushOutput(newOutput, nextJob);
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return output;
    }

    public static PixelColor[] calculateSet() {
        PixelColor[] output = new PixelColor[Configuration.NUM_PIXELS];
        PixelLocation[] input = new PixelLocation[Configuration.NUM_PIXELS];
        for (int i = 0; i < Configuration.NUM_PIXELS; i++) {
            input[i] = new PixelLocation(i);
        }

        Manager<PixelColor> manager = new Manager<>(output, new MandelbrotWorker(input));
        return manager.execute();
    }

    public static PixelColor[] calculateAntialiasing(PixelColor[] input) {
        PixelColor[] output = new PixelColor[Configuration.NUM_PIXELS];
        Manager<PixelColor> manager = new Manager<>(output, new AntialiasingWorker(input));
        return manager.execute();
    }
}
