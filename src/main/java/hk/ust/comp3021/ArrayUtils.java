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
    int[] output = new int[input.length];

    var tasks = IntStream.range(0, input.length / CHUNK_SIZE + (input.length % CHUNK_SIZE != 0 ? 1 : 0))
            .mapToObj(i -> (Runnable) () -> {
              IntStream.range(i * CHUNK_SIZE, Math.min((i + 1) * CHUNK_SIZE, input.length)).forEach(j -> {
                output[j] = map.applyAsInt(input[j]);
              });
            })
            .collect(Collectors.toList());

    pool.addTasks(tasks);

    return output;
  }

  public static void seqInclusivePrefixSum(int[] input, IntBinaryOperator op) {
    IntStream.range(1, input.length).forEach(i -> {
      input[i] = op.applyAsInt(input[i - 1], input[i]);
    });
  }

  public static void parInclusivePrefixSum(int[] input, IntBinaryOperator op,
                                           TaskPool pool) {
    // Bonus part
    int[] z = new int[input.length / CHUNK_SIZE + (input.length % CHUNK_SIZE != 0 ? 1 : 0)];

    var tasks = IntStream.range(0, z.length).mapToObj(i -> (Runnable) () -> {
                  z[i] = input[i * CHUNK_SIZE];
                  z[i] = IntStream.range(i * CHUNK_SIZE + 1, Math.min((i + 1) * CHUNK_SIZE, input.length))
                          .reduce(z[i], (acc, j) -> op.applyAsInt(acc, input[j]));

              })
              .collect(Collectors.toList());

    pool.addTasks(tasks);

    IntStream.range(1, z.length).forEach(i -> {
        z[i] = op.applyAsInt(z[i - 1], z[i]);
    });

    var tasks2 = IntStream.range(0, z.length).mapToObj(i -> (Runnable) () -> {
        if (i != 0) {
            input[i * CHUNK_SIZE] = op.applyAsInt(z[i - 1], input[i * CHUNK_SIZE]);
        }
        IntStream.range(i * CHUNK_SIZE + 1, Math.min((i + 1) * CHUNK_SIZE, input.length)).forEach(j -> {
            input[j] = op.applyAsInt(input[j - 1], input[j]);
        });
        }).collect(Collectors.toList());

    pool.addTasks(tasks2);
  }
}
