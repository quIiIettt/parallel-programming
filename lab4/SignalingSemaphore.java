class SendingThread extends Thread {
  Semaphore semaphore = null;

  SendingThread( Semaphore s) {
    semaphore = s;
    System.out.println( "SendingThread created");
    }

  public void run() {
    System.out.println( "SendingThread started");
    for (int i = 0; i < 6; ++i) {
      System.out.println( "SendingThread (" + i + ")");
      try {
        semaphore.take( i);
        }
      catch (InterruptedException ie) {
        }
      }
      System.out.println( "SendingThread stoped");
    }
  }

class ReceivingThread extends Thread {
  Semaphore semaphore = null;

  ReceivingThread( Semaphore s) {
    semaphore = s;
    System.out.println( "ReceivingThread created");
    }

  public void run() {
    System.out.println( "ReceivingThread started");
    while(true) {
      try {
        int m = semaphore.release();
        System.out.println( "\t\t\tReceivingThread (" + m + ")");
        }
      catch (InterruptedException ie) {
        }
      }
    }
  }

class Semaphore {
  private boolean signal = false;
  private int message = 0;

  public synchronized void take( int m) throws InterruptedException {
    while(signal) {
      wait();
      }
    signal = true;
    message = m;
    System.out.println( "\tSemaphore take = " + message);
    notify();
    }

  public synchronized int release() throws InterruptedException {
    while(!signal) {
      wait();
      }
    signal = false;
    System.out.println( "\t\tSemaphore release = " + message);
    notify();
    return( message);
    }
  }

public class SignalingSemaphore {
  public static void main( String argc[]) {
    System.out.println( "Main process started");
    Semaphore semaphore = new Semaphore();
    SendingThread sender = new SendingThread( semaphore);
    ReceivingThread receiver = new ReceivingThread( semaphore);

    receiver.start();
    sender.start();
    System.out.println( "Main process ended");
    }
  }