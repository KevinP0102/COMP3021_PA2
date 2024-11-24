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
    throw new UnsupportedOperationException();
  }

  public Optional<FunNode<T>> setInput(int i, T value) {
    // part 1: function data dependency graph node
    throw new UnsupportedOperationException();
  }

  public T getResult() { return output.get(); }

  public void eval() {
    // part 1: function data dependency graph node
    throw new UnsupportedOperationException();
  }
}
