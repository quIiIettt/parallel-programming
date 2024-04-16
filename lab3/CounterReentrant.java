class TestThread extends Thread {
    String threadName;
    CounterReentrant c;
    int delay;
  
    TestThread( String name, int t, CounterReentrant cc) {
      threadName = name;
      c = cc;
      delay = t;
      System.out.println( threadName + " - Created");
      }
  
    public void run() {
      System.out.println( threadName + " - Start of Work");
      for (int i = 0; i < 10; ++i) {
        if (delay % 2 == 0) {
          System.out.println( threadName + " - increment " + c.inc());
          } else {
          System.out.println( threadName + " - decrement " + c.dec());
          }
        try {
          Thread.sleep( (int)((double)delay * 10.0 * Math.random()));
          }
        catch (InterruptedException ie) {
          }
        }
      System.out.println( threadName + " - End of Work");
      }
    }
  
  class Lock {
    boolean isLocked = false;
    Thread lockedBy = null;
    int lockedCount = 0;
    public synchronized void lock() throws InterruptedException {
      Thread callingThread = Thread.currentThread();
      while(isLocked && lockedBy != callingThread) {
        wait();
        }
      isLocked = true;
      lockedCount++;
      lockedBy = callingThread;
      }
  
    public synchronized void unlock() {
      if (Thread.currentThread() == this.lockedBy) {
        lockedCount--;
        if(lockedCount == 0) {
          isLocked = false;
          notify();
          }
        }
      }
    }
  
  public class CounterReentrant {
    private Lock lock = new Lock();
    private int count = 0;
    private int newCount;
  
    public int inc() {
      try {
        lock.lock();
        newCount = ++count;
        lock.unlock();
        }
      catch (InterruptedException ie) {
        }
      return newCount;
      }
  
    public int dec() {
      try {
        lock.lock();
        newCount = --count;
        lock.unlock();
        }
      catch (InterruptedException ie) {
        }
      return newCount;
      }
  
    public static void main( String argc[]) {
      System.out.println( "Main process started");
      CounterReentrant cnt = new CounterReentrant();
      TestThread t[] = new TestThread[10];
      for (int i = 0; i < 10; ++i) {
        t[i] = new TestThread( "Proc:" + i, i, cnt);
        t[i].start();
        }
    System.out.println( "Main process ended");
    }
  }