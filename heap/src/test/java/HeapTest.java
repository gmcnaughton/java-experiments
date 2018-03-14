/*
 * This Java source file was generated by the Gradle 'init' task.
 */
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Arrays;

public class HeapTest {
  @Test public void testEmptyHeapIsValid() {
    Heap<Integer> heap = new Heap<Integer>(Heap.Type.MIN);
    assertTrue(heap.valid());

    heap = new Heap<Integer>(Heap.Type.MAX);
    assertTrue(heap.valid());
  }

  @Test public void testUnaryHeapIsValid() {
    Heap<Integer> heap = new Heap<Integer>(Heap.Type.MIN);
    heap.push(1);
    assertTrue(heap.valid());

    heap = new Heap<Integer>(Heap.Type.MAX);
    heap.push(1);
    assertTrue(heap.valid());
  }

  @Test public void testSimpleHeapIsValid() {
    Heap<Integer> heap = new Heap<Integer>(Heap.Type.MIN);
    heap.push(1);
    heap.push(2);
    heap.push(3);
    assertTrue(heap.valid());

    heap = new Heap<Integer>(Heap.Type.MAX);
    heap.push(1);
    heap.push(2);
    heap.push(3);
    assertTrue(heap.valid());
  }

  @Test public void testHeapWithEqualValuesIsValid() {
    Heap<Integer> heap = new Heap<Integer>(Heap.Type.MIN);
    heap.push(1);
    heap.push(1);
    heap.push(1);
    assertTrue(heap.valid());

    heap = new Heap<Integer>(Heap.Type.MAX);
    heap.push(1);
    heap.push(1);
    heap.push(1);
    assertTrue(heap.valid());
  }

  @Test public void testHeapWithRandomValuesIsValid() {
    Heap<Integer> heap = new Heap<Integer>(Heap.Type.MIN);
    for (int i = 0; i < 25; i++) {
      // random values in the range [Integer.MIN_VALUE, Integer.MAX_VALUE)
      heap.push((int)((long)Integer.MAX_VALUE - (long)Math.floor(Math.random() * (Integer.MAX_VALUE - Integer.MIN_VALUE))));
    }
    assertTrue(heap.valid());

    heap = new Heap<Integer>(Heap.Type.MAX);
    for (int i = 0; i < 25; i++) {
      heap.push((int)((long)Integer.MAX_VALUE - (long)Math.floor(Math.random() * (Integer.MAX_VALUE - Integer.MIN_VALUE))));
    }
    assertTrue(heap.valid());
  }

  @Test public void testPushWithEmptyHeap() {
    Heap<Integer> heap = new Heap<Integer>(Heap.Type.MIN);
    assertTrue(heap.isEmpty());
    assertEquals(0, heap.size());
    heap.push(123);
    assertFalse(heap.isEmpty());
    assertEquals(1, heap.size());
    assertEquals(123, (int)heap.peek());

    heap = new Heap<Integer>(Heap.Type.MAX);
    assertTrue(heap.isEmpty());
    assertEquals(0, heap.size());
    heap.push(123);
    assertFalse(heap.isEmpty());
    assertEquals(1, heap.size());
    assertEquals(123, (int)heap.peek());
  }

  @Test public void testPushMaximalValue() {
    Heap<Integer> heap = new Heap<Integer>(Heap.Type.MIN);
    heap.push(2);
    heap.push(1);
    assertHeapEquals(heap, Arrays.asList(1, 2));

    heap = new Heap<Integer>(Heap.Type.MAX);
    heap.push(1);
    heap.push(2);
    assertHeapEquals(heap, Arrays.asList(2, 1));
  }

  @Test public void testPushMinimalValue() {
    Heap<Integer> heap = new Heap<Integer>(Heap.Type.MIN);
    heap.push(1);
    heap.push(2);
    assertHeapEquals(heap, Arrays.asList(1, 2));

    heap = new Heap<Integer>(Heap.Type.MAX);
    heap.push(2);
    heap.push(1);
    assertHeapEquals(heap, Arrays.asList(2, 1));
  }

  @Test public void testPushIntermediateValue() {
    Heap<Integer> heap = new Heap<Integer>(Heap.Type.MIN);
    heap.push(1);
    heap.push(3);
    heap.push(2);
    assertHeapEquals(heap, Arrays.asList(1, 2, 3));

    heap = new Heap<Integer>(Heap.Type.MAX);
    heap.push(1);
    heap.push(3);
    heap.push(2);
    assertHeapEquals(heap, Arrays.asList(3, 2, 1));
  }

  private static <V extends Comparable<? super V>> void assertHeapEquals(Heap<V> heap, List<V> vals) {
    assertEquals(vals.size(), heap.size());

    for (int i = 0; i < vals.size(); i++) {
      V val = heap.pop();
      assertEquals(vals.get(i), val);
    }
  }
}
