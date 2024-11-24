package hk.ust.comp3021;

import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ArrayUtils {
  // HINT: try to use this chunk size to partition your work.
  static final int CHUNK_SIZE = 1 << 16;

  public static int[] seqMap(int[] input, IntUnaryOperator map) {
    int[] output = new int[input.length];
    IntStream.range(0, input.length).forEach(i -> {
      output[i] = map.applyAsInt(input[i]);
    });
    return output;
  }

  public static int[] parMap(int[] input, IntUnaryOperator map, TaskPool pool) {
    // Bonus part
    throw new UnsupportedOperationException();
  }

  public static void seqInclusivePrefixSum(int[] input, IntBinaryOperator op) {
    IntStream.range(1, input.length).forEach(i -> {
      input[i] = op.applyAsInt(input[i - 1], input[i]);
    });
  }

  public static void parInclusivePrefixSum(int[] input, IntBinaryOperator op,
                                           TaskPool pool) {
    // Bonus part
    throw new UnsupportedOperationException();
  }
}
