class TestThread extends Thread {
  String threadName;
  CommonVariable parentClass;
  boolean shouldStop;

  TestThread(String name, CommonVariable c) {
      threadName = name;
      parentClass = c;
      shouldStop = false;
      System.out.println(threadName + " - Created");
  }

  public void run() {
      System.out.println(threadName + " - Start of Work");
      while (!shouldStop && !parentClass.state) {
          System.out.println(threadName + " - " + parentClass.state);
          try {
              Thread.sleep(10);
          } catch (InterruptedException ie) {
          }
      }
      System.out.println(threadName + " - " + parentClass.state);
      System.out.println(threadName + " - End of Work");
  }

  public void stopOtherThreads(TestThread[] threads) {
      shouldStop = true;
      for (TestThread thread : threads) {
          if (!thread.getName().equals(threadName)) {
              thread.interrupt();
          }
      }
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

      TestThread[] threads = {t1, t2, t3};

      t1.start();
      t2.start();
      t3.start();

      System.out.println("Main process: " + cv.state);

      try {
          Thread.sleep(60);
      } catch (InterruptedException ie) {
      }

      synchronized (cv) {
          cv.state = true;
          System.out.println("Main process: " + cv.state);
          t1.stopOtherThreads(threads);
      }

      System.out.println("Main process ended");
  }
}
