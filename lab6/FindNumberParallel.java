import java.util.concurrent.*;

class FindNumberThread extends Thread {
    private final int[] numbers;
    private final int target;
    private final int startIdx;
    private final int endIdx;
    private final CountDownLatch startLatch;
    private final CountDownLatch stopLatch;

    public FindNumberThread(int[] nums, int tgt, int start, int end, CountDownLatch startLatch, CountDownLatch stopLatch) {
        this.numbers = nums;
        this.target = tgt;
        this.startIdx = start;
        this.endIdx = end;
        this.startLatch = startLatch;
        this.stopLatch = stopLatch;
    }

    public void run() {
        try {
            startLatch.await();
            // пошук елемента, який менше заданого числа
            for (int i = startIdx; i < endIdx; i++) {
                if (numbers[i] < target) {
                    System.out.println("Thread found at index: " + i);
                    break;
                }
            }
        } catch (InterruptedException e) {
            System.err.println(e.toString());
        } finally {
            stopLatch.countDown();
        }
    }
}

public class FindNumberParallel {
    public static void main(String[] args) {
        int[] numbers = {4, 8, 15, 16, 23, 42};
        int target = 24;

        int numThreads = Integer.parseInt(args[0]);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch stopLatch = new CountDownLatch(numThreads);


        int batchSize = numbers.length / numThreads;
        for (int i = 0; i < numThreads; i++) {
            int start = i * batchSize;
            int end = (i == numThreads - 1) ? numbers.length : (i + 1) * batchSize;
            FindNumberThread t = new FindNumberThread(numbers, target, start, end, startLatch, stopLatch);
            t.start();
        }
        startLatch.countDown();

        try {
            stopLatch.await();
        } catch (InterruptedException e) {
            System.err.println(e.toString());
        }

        System.out.println("Main process stopped");
    }
}
