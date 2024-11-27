package hk.ust.comp3021;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ParEvaluator<T> implements Evaluator<T> {
  private HashMap<FunNode<T>, List<Consumer<T>>> listeners = new HashMap<>();
  private TaskPool pool;

  public ParEvaluator(int numThreads) { pool = new TaskPool(numThreads); }

  public void addDependency(FunNode<T> a, FunNode<T> b, int i) {
    // part 4: parallel function evaluator
    listeners.putIfAbsent(a, new ArrayList<>());
    listeners.putIfAbsent(b, new ArrayList<>());

    listeners.get(a).add(t -> {
      final Optional<FunNode<T>> check = b.setInput(i, t);
      if (check.isPresent()) {
        pool.addTask( () -> {
                b.eval();
                listeners.get(b).forEach(c -> c.accept(b.getResult()));
                } );
      }
    } );
  }

  public void terminate() { pool.terminate(); }

  public void start(List<FunNode<T>> nodes) {
    // part 4: parallel function evaluator
      List<Runnable> tasks = nodes.stream()
              .map(node -> (Runnable) () -> {
                  node.eval();
                  listeners.get(node).forEach(c -> c.accept(node.getResult()));
              })
              .collect(Collectors.toList());

      pool.addTasks(tasks);
  }
}
