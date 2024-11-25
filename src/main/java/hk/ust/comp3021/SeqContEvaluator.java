package hk.ust.comp3021;

import java.util.*;
import java.util.function.Consumer;

public class SeqContEvaluator<T> implements Evaluator<T> {
  private ArrayDeque<FunNode<T>> toEval = new ArrayDeque<>();
  private HashMap<FunNode<T>, List<Consumer<T>>> listeners = new HashMap<>();

  public void addDependency(FunNode<T> a, FunNode<T> b, int i) {
    // part 2: sequential function evaluator
    listeners.putIfAbsent(a, new ArrayList<>());
    listeners.putIfAbsent(b, new ArrayList<>());
    listeners.get(a).add(t -> {
      Optional<FunNode<T>> check = b.setInput(i, t);
      if (check.isPresent()) {
        toEval.add(check.get());
      }
    } );
  }

  public void start(List<FunNode<T>> nodes) {
    // part 2: sequential function evaluator
    nodes.forEach(node -> {
      node.eval();
      listeners.get(node).forEach(c -> c.accept(node.getResult()));
    });

    while (!toEval.isEmpty()) {
      FunNode<T> node = toEval.removeFirst();
      node.eval();
      listeners.get(node).forEach(c -> c.accept(node.getResult()));
    }
  }
}
