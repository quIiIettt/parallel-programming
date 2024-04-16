class SendingThread extends Thread {
    CountingSemaphore semaphore = null;
  
    SendingThread( CountingSemaphore semaphore) {
      this.semaphore = semaphore;
      System.out.println( "SendingThread created");
      }
  
    public void run() {
      System.out.println( "SendingThread started");
      for (int i = 0; i < 6; ++i) {
        System.out.println( "SendingThread do(" + i + ")");
        this.semaphore.take( i);
        }
      System.out.println( "SendingThread stoped");
      }
    }
  
  class ReceivingThread extends Thread {
    CountingSemaphore semaphore = null;
  
    ReceivingThread( CountingSemaphore semaphore) {
      this.semaphore = semaphore;
      System.out.println( "ReceivingThread created");
      }
  
    public void run() {
      int sum = 0;
      System.out.println( "ReceivingThread started");
      while(true) {
        try {
          int m = this.semaphore.release();
          sum += m;
          System.out.println( "ReceivingThread do(" + m + "): " + sum);
          }
        catch (InterruptedException ie) {
          }
        }
      }
    }
  
  class CountingSemaphore {
    private int signals = 0;
    private int message[];
  
    CountingSemaphore( int bufferSize) {
      message = new int[bufferSize];
      }
  
    public synchronized void take( int m) {
      message[this.signals] = m;
      this.signals++;
      this.notify();
      System.out.println( "CountingSemaphore(take): " + this.signals);
      }
  
    public synchronized int release() throws InterruptedException {
      while(this.signals == 0) {
        wait();
        }
      this.signals--;
      System.out.println( "CountingSemaphore(release): " + this.signals);
      return( message[this.signals]);
      }
    }
  
  public class CountingSemaphoreDemo {
    public static void main( String argc[]) {
      System.out.println( "Main process started");
      CountingSemaphore semaphore = new CountingSemaphore( 100);
  
      (new ReceivingThread( semaphore)).start();
      (new SendingThread( semaphore)).start();
      (new SendingThread( semaphore)).start();
  
      System.out.println( "Main process ended");
      }
    } 