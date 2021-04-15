package my.javalab.executorservice;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorServiceInvokeAll {

    private static ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Callable<String>> callables = Arrays.asList(
                new DelayedCallable("fast thread", 100),
                new DelayedCallable("slow thread", 3000));

        long startProcessingTime = System.currentTimeMillis();
        List<Future<String>> futures = WORKER_THREAD_POOL.invokeAll(callables);
        awaitTerminationAfterShutdown(WORKER_THREAD_POOL);
        long totalProcessingTime = System.currentTimeMillis() - startProcessingTime;
        System.out.println("total processing time: " + totalProcessingTime);

        System.out.println(futures.get(0).get());
        System.out.println(futures.get(1).get());
    }

    public static void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            //threadPool.awaitTermination blocks until all tasks have completed
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
            System.out.println("threadPool.awaitTermination returns true, all threads are done");
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
