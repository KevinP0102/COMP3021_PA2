package hk.ust.comp3021;

import java.util.*;
import java.util.function.Consumer;


public class SeqEvaluator<T> implements Evaluator<T> {
  private HashMap<FunNode<T>, List<Consumer<T>>> listeners = new HashMap<>();

  public void addDependency(FunNode<T> a, FunNode<T> b, int i) {
    // part 2: sequential function evaluator
    listeners.putIfAbsent(a, new ArrayList<>());
    listeners.putIfAbsent(b, new ArrayList<>());
    listeners.get(a).add(t -> {
      Optional<FunNode<T>> check = b.setInput(i, t);
      if (check.isPresent()) {
        b.eval();
        listeners.get(b).forEach(c -> c.accept(b.getResult()));
      }
    } );
  }

  public void start(List<FunNode<T>> nodes) {
    // part 2: sequential function evaluator
    nodes.forEach(node -> {
      node.eval();
      listeners.get(node).forEach(c -> c.accept(node.getResult()));
    });
  }
}
