package my.javalab.executorservice;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class UseExecutorCompletionService {

    private static ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletionService<String> service
                = new ExecutorCompletionService<>(WORKER_THREAD_POOL);

        List<Callable<String>> callables = Arrays.asList(
                new DelayedCallable("fast thread", 100),
                new DelayedCallable("slow thread", 3000));

        for (Callable<String> callable : callables) {
            service.submit(callable);
        }

        Future<String> future = service.take();
        System.out.println(future.get());
        future = service.take();
        System.out.println(future.get());

        // the thread is waiting here
        awaitTerminationAfterShutdown(WORKER_THREAD_POOL);
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
