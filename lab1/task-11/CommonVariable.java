class TestThread extends Thread {
    String threadName;
    CommonVariable parentClass;

    TestThread(String name, CommonVariable c) {
        threadName = name;
        parentClass = c;
        System.out.println(threadName + " - Created");
    }

    public void run() {
        System.out.println(threadName + " - Start of Work");
        synchronized (parentClass) {
            try {
                parentClass.wait(); // Почекати, поки зміниться стан
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(threadName + " - " + parentClass.state);
        System.out.println(threadName + " - End of Work");
    }
}

public class CommonVariable {
    boolean state = false;

    CommonVariable() {
        state = false;
    }

    public static void main(String argc[]) {
        System.out.println("Main process started");
        CommonVariable cv = new CommonVariable();
        TestThread t1 = new TestThread("First", cv);
        TestThread t2 = new TestThread("Second", cv);
        TestThread t3 = new TestThread("Third", cv);

        t1.start();
        t2.start();
        t3.start();

        System.out.println("Main process: " + cv.state);

        try {
            Thread.sleep(60);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        synchronized (cv) {
            cv.state = true;
            System.out.println("Main process: " + cv.state);
            cv.notifyAll(); // Сповістити всі потоки, що стан змінився
        }

        try {
            // Чекаємо завершення потоків, 1-2-3
            t1.join();
            t2.join();
            t3.join(); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Main process ended");
    }
}
