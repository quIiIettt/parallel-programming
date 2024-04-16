class SendingThread extends Thread {
    BoundedSemaphore semaphore = null;
    String name;
    long procNum;
  
    SendingThread( String n, long p, BoundedSemaphore semaphore) {
      this.semaphore = semaphore;
      name = n;
      procNum = p;
      System.out.println( name + ": created");
      }
  
    public void run() {
      System.out.println( name + ": started");
      for (int i = 0; i < 4; ++i) {
        System.out.println( name + ": do(" + (i * procNum) + ")");
        try {
          this.semaphore.take( i * procNum);
          }
        catch (InterruptedException ie) {
          }
        }
      System.out.println( name + ": stoped");
      }
    }
  
  class ReceivingThread extends Thread {
    BoundedSemaphore semaphore = null;
    String name;
    int procNum;
    long sum = 0;
  
    ReceivingThread( String n, int p, BoundedSemaphore semaphore) {
      this.semaphore = semaphore;
      name = n;
      procNum = p;
      System.out.println( name + ": created");
      }
  
    public void run() {
      System.out.println( name + ": started");
      while(true) {
        try {
          long m = this.semaphore.release();
          sum += m;
          System.out.println( name + ": do(" + m + "), " + sum);
          }
        catch (InterruptedException ie) {
          }
        }
      }
    }
  
  class BoundedSemaphore {
    private int signals = 0;
    private int bound = 0;
    private long message[];
  
    public BoundedSemaphore( int upperBound) {
      this.bound = upperBound;
      message = new long[upperBound];
      }
  
    public synchronized void take( long m) throws InterruptedException {
      while(this.signals == bound) {
        wait();
        }
      message[this.signals] = m;
      this.signals++;
      System.out.println( "BoundedSemaphore(take): " + this.signals);
      this.notify();
      }
  
    public synchronized long release() throws InterruptedException {
      while(this.signals == 0) {
        wait();
        }
      this.signals--;
      long m = message[this.signals];
      message[this.signals] = 0;
      System.out.println( "BoundedSemaphore(release): " + this.signals);
      this.notify();
      return( m);
      }
    }
  
  public class BoundedSemaphoreDemo {
    public static void main( String argc[]) {
      System.out.println( "Main process started");
      BoundedSemaphore semaphore = new BoundedSemaphore( 2);
  
      (new ReceivingThread( "Receiver", 1, semaphore)).start();
  
      (new SendingThread( "Sender 1", 1, semaphore)).start();
      (new SendingThread( "Sender 2", 10, semaphore)).start();
      (new SendingThread( "Sender 3", 100, semaphore)).start();
      (new SendingThread( "Sender 4", 1000, semaphore)).start();
      (new SendingThread( "Sender 5", 10000, semaphore)).start();
      (new SendingThread( "Sender 6", 100000, semaphore)).start();
  
      System.out.println( "Main process ended");
      }
    }