class TestThread extends Thread {
    String threadName;
    CounterReentrant c;
    int delay;

    TestThread(String name, int t, CounterReentrant cc) {
        threadName = name;
        c = cc;
        delay = t;
        System.out.println(threadName + " - Created");
    }

    public void run() {
        System.out.println(threadName + " - Start of Work");
        for (int i = 0; i < 5; ++i) { // змінено на 5
            c.increment(); // змінено на increment
            try {
                Thread.sleep((int) ((double) delay * 10.0 * Math.random()));
            } catch (InterruptedException ie) {
            }
        }
        System.out.println(threadName + " - End of Work");
    }
}

class Lock {
    boolean isLocked = false;
    Thread lockedBy = null;

    public synchronized void lock() throws InterruptedException {
        while (isLocked) {
            wait();
        }
        isLocked = true;
        lockedBy = Thread.currentThread();
    }

    public synchronized void unlock() {
        if (Thread.currentThread() == this.lockedBy) {
            isLocked = false;
            notify();
        }
    }
}

public class CounterReentrant {
    private Lock lock = new Lock();
    private int count = 3; // змінено на 3

    public void increment() { // змінено на increment
        try {
            lock.lock();
            count++;
            System.out.println("Incremented: " + count);
        } catch (InterruptedException ie) {
        } finally {
            lock.unlock();
        }
    }

    public static void main(String argc[]) {
        System.out.println("Main process started");
        CounterReentrant cnt = new CounterReentrant();
        TestThread t[] = new TestThread[2]; // змінено на 2
        for (int i = 0; i < 2; ++i) { // змінено на 2
            t[i] = new TestThread("Proc:" + i, i, cnt);
            t[i].start();
        }
        try {
            for (int i = 0; i < 2; ++i) { // змінено на 2
                t[i].join(); 
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main process ended");
    }
}
