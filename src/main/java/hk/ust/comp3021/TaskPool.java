package hk.ust.comp3021;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

public class TaskPool {
  public class TaskQueue {
    private ArrayDeque<Runnable> queue = new ArrayDeque<>();
    private boolean terminated = false;
    private int working;
    private Semaphore idle;

    public TaskQueue(int numThreads, Semaphore idle) {
      working = numThreads;
      this.idle = idle;
    }

    public Optional<Runnable> getTask() throws InterruptedException {
      // part 3: task pool
      synchronized (this) {
        while (queue.isEmpty()) {
          --working;
          if (working == 0) {
            idle.release();
            working = queue.size();
          }
          wait();
          ++working;
        }

        try {
          return Optional.of(queue.remove());
        } catch (Exception e) {
          return Optional.empty();
        }
      }
    }

    public synchronized void addTask(Runnable task) {
      // part 3: task pool
      queue.add(task);
      notify();
    }

    public void terminate() {
      // part 3: task pool
      idle.release(working);
      terminated = true;
    }
  }

  private TaskQueue queue;
  private Thread workers[];
  private Semaphore idle = new Semaphore(0);

  public TaskPool(int numThreads) {
    // part 3: task pool
    workers = new Thread[numThreads];
    queue = new TaskQueue(numThreads, idle);

    IntStream.range(0, numThreads).forEach(i -> {
      workers[i] = new Thread(() -> {
        while (true) {
          if (queue.terminated) {break;}
          //if (queue.queue.isEmpty()) {idle.release();}
          try {
            queue.getTask().ifPresent(Runnable::run);
          } catch (InterruptedException e) {
            //break;
          }
        }
      });
      workers[i].start();
    });
  }

  public void addTask(Runnable task) { queue.addTask(task); }

  public synchronized void addTasks(List<Runnable> tasks) {
    // part 3: task pool
    tasks.forEach(queue::addTask);

    idle.drainPermits();
    try {
      idle.acquire();
    } catch (InterruptedException e) {}
  }

  public void terminate() {
    queue.terminate();
    for (Thread thread : workers) {
      try {
        // this will send an InterruptedException to the thread to wake it up
        // from blocking operations such as Thread.sleep.
        if (thread.isAlive())
          thread.interrupt();
        thread.join();
      } catch (InterruptedException ex) {
      }
    }
  }
}
