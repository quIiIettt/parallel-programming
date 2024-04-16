class TestThread extends Thread {
  String threadName;
  Monitor m;

  TestThread(String name, Monitor mm) {
    threadName = name;
    m = mm;
    System.out.println(threadName + " - Created");
  }

  public void run() {
    m.procIncrement();
    System.out.println(threadName + " - Start of Work");
    try {
      Thread.sleep(100);
    } catch (InterruptedException ie) {
    }
    m.procDecrement();
    System.out.println(threadName + " - End of Work");
  }
}

public class Monitor extends Thread {
  int procNum = 0;

  Monitor(int procNumber) {
    for (int i = 0; i < procNumber; ++i) {
      new TestThread("Proc:" + i, this).start();
    }
  }

  public void run() {
    System.out.println("Monitor - Started: " + procNum);
    try {
      synchronized (this) {
        while (procNum != 0) {
          wait();
        }
      }
    } catch (InterruptedException ee) {
      System.out.println("Monitor - Interrupted Exception: " + ee.toString());
    }
    System.out.println("Monitor - Ended: " + procNum);
  }

  public synchronized void procIncrement() {
    ++procNum;
    System.out.println("Monitor - Processes Number: " + procNum);
  }

  public synchronized void procDecrement() {
    --procNum;
    notify();
    System.out.println("Monitor - Processes Number: " + procNum);
    if (procNum == 3) {
      System.out.println("Dotsenko Dmytro. Only three processes left active.");
    }
  }

  public static void main(String argc[]) {
    System.out.println("Main process started");
    Monitor monitor = new Monitor(5);
    monitor.start();
    try {
      monitor.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Main process ended");
  }
}
