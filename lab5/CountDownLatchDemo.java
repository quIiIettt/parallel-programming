import java.util.concurrent.*;

class LatchedThread extends Thread {
  private final CountDownLatch startLatch;
  private int procNumber;

  public LatchedThread( CountDownLatch s, int n) {
    startLatch = s;
    procNumber = n;
    System.out.println( "Thread(" + procNumber + "): created");
    }

  public void run() {
    System.out.println( "Thread(" + procNumber + "): started");
    try {
      startLatch.await();
      System.out.println( "Thread(" + procNumber + "): running");
      }
    catch(InterruptedException e) {
      System.err.println( e.toString());
      }
    System.out.println( "Thread(" + procNumber + "): stoped");
    }
  }

public class CountDownLatchDemo {
  public static void main( String args[]) {
    System.out.println( "Main process started");
    CountDownLatch startLatch = new CountDownLatch( 1);
    for (int i = 0; i < 4; ++i) {
      LatchedThread t = new LatchedThread(startLatch, i+1);
      t.start();
      }
    try {
      Thread.sleep( 200);
      }
    catch( InterruptedException e) {
      System.err.println( e.toString());
      }
    startLatch.countDown();
    System.out.println( "Main process stoped");
    }
  }