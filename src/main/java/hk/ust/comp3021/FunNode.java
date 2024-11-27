package hk.ust.comp3021;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FunNode<T> {
  private List<Optional<T>> inputs;
  private Optional<T> output = Optional.empty();
  private Function<List<T>, T> f;

  public FunNode(int arity, Function<List<T>, T> fun) {
    // part 1: function data dependency graph node
    inputs = new ArrayList<Optional<T>>(arity);
    IntStream.range(0, arity).forEach(i -> inputs.add(Optional.empty()));
    f = fun;
  }

  public Optional<FunNode<T>> setInput(int i, T value) {
    // part 1: function data dependency graph node
    inputs.set(i, Optional.of(value));

    if (inputs.stream().anyMatch(Optional::isEmpty)) {
      return Optional.empty();
    } else {
      return Optional.of(this);
    }
  }

  public T getResult() {
    synchronized (this) {
      while (output.isEmpty()) {
        try {
          wait();
        } catch (InterruptedException e) {}
      }
      return output.get();
    }
  }

  public void eval() {
    synchronized (this) {
      output = Optional.of(f.apply(inputs.stream().map(Optional::get).toList()));
      notifyAll();
    }
  }
}
