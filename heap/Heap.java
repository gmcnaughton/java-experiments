import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Heap implements the standard heap data structure.
 *
 * A heap is a tree which maintains the 'heap property': the value at a parent
 * node is greater-than-or-equal-to (max-heap) or less-than-or-equal-to (min-heap_)
 * its child nodes. This property is transitive, so by definition a node's value
 * is great-than-or-equal-to all of its descedants' values.
 *
 * No other relationship is guaranteed to hold between nodes in the tree.
 * Siblings are not necessarily ordered with respect to each other. Only the
 * comparison relationship between levels is enforced.
 *
 * By definition values in a heap must be comparable, so that the heap property
 * can be maintained.
 */
public class HeapValidator {
  public static void main(String[] args) {
    test("Empty", new ArrayList<Integer>(Arrays.asList()), Type.MAX, true);
    test("Empty",new ArrayList<Integer>(Arrays.asList()), Type.MIN, true);

    test("Unary null", new ArrayList<Integer>(Arrays.asList((Integer)null)), Type.MAX, true);
    test("Unary null", new ArrayList<Integer>(Arrays.asList((Integer)null)), Type.MIN, true);

    test("Unary", new ArrayList<Integer>(Arrays.asList(1)), Type.MAX, true);
    test("Unary", new ArrayList<Integer>(Arrays.asList(1)), Type.MIN, true);

    test("One two three", new ArrayList<Integer>(Arrays.asList(1, 2, 3)), Type.MAX, false);
    test("One two three", new ArrayList<Integer>(Arrays.asList(1, 2, 3)), Type.MIN, true);

    test("Equal", new ArrayList<Integer>(Arrays.asList(1, 1, 1)), Type.MIN, true);
    test("Equal", new ArrayList<Integer>(Arrays.asList(1, 1, 1)), Type.MAX, true);

    // test("With some nulls", new ArrayList<Integer>(Arrays.asList(1, 1, (Integer)null)), Type.MIN, true);
    test("With some nulls", new ArrayList<Integer>(Arrays.asList(1, (Integer)null, 1)), Type.MAX, true);

    test("Complex", new ArrayList<Integer>(Arrays.asList(
                    1,
           2,                 100,
        3,    4,        9998,      9999,
      9, 3, 5, 101, 10001, 9998, 9999, 9999)
    ), Type.MIN, true);

    test("Complex invalid", new ArrayList<Integer>(Arrays.asList(
                    1,
           2,                 100,
        3,    4,        9998,            9999,
      9, 3, 5, 101, 10001, 9998, /*here*/1, 9999)
    ), Type.MIN, false);

    test("Unbalanced", new ArrayList<Integer>(Arrays.asList(
                    1,
           2,                null,
        3,    null,    null,      null,
      null, null, null, null, null, null, null, null)
    ), Type.MIN, true);
  }

  public enum Type {
    MIN,
    MAX
  }

  public static <V2 extends Comparable<? super V2>> void test(String s, ArrayList<V2> h, Heap.Type type, boolean expected) {
    boolean actual = hasHeapProperty(h, type);
    if (expected != actual) {
      System.out.println("*** Error: mismatch: " + s + " (expected " + expected + ", actual " + actual + ", type = " + type + ")");
    } else {
      System.out.println("Success: " + s + " (" + type + ")");
    }
  }

  public static <V2 extends Comparable<? super V2>> boolean hasHeapProperty(ArrayList<V2> h, Heap.Type type) {
    return hasHeapPropertyIterativeBackwards(h, type);
  }

  // Walks forwards through the tree, starting frmo the 
  public static <V2 extends Comparable<? super V2>> boolean hasHeapPropertyRecurse(ArrayList<V2> h, Heap.Type type) {
    if (h.isEmpty()) return true;

    // for each item in h
    // its value needs to be greater-than-or-equal-to the value of its children (if any)
    // or its children may be null (empty) indicating the end of a branch
    return _hasHeapPropertyRecurse(h, type, 0);
  }

  // Walks backwards through through the tree, starting from leaves and going
  // up towards the root, testing child values against their parent.
  // Parents are guaranteed to have a value if child has a value, otherwise
  // the tree structure is invalid (orphaned tree node).
  //
  // This is O(N) on the size of the array, which worst-cases to 2^M values in the heap
  // if the heap is pathologically unbalanced (as we scan every slot in every level
  // of the tree, which for a sparse tree could include a huge number of empty slots).
  public static <V2 extends Comparable<? super V2>> boolean _hasHeapPropertyRecurse(ArrayList<V2> h, Heap.Type type, int index) {
    V2 val = h.get(index);
    if (val == null) return true;

    int leftIndex = ((index + 1) << 1) - 1;
    int rightIndex = ((index + 1) << 1);

    V2 left = null;
    V2 right = null;

    if (leftIndex < h.size()) {
      left = h.get(leftIndex);
      if (left != null) {
        int cmp = val.compareTo(left);
        if (type == Heap.Type.MAX ? cmp < 0 : cmp > 0) {
          System.out.println("HEAP VIOLATION: parent value (" + val + "), child value (" + left + "), heap type = " + type);
          return false;
        }
      }
    }

    if (rightIndex < h.size()) {
      right = h.get(rightIndex);
      if (right != null) {
        int cmp = val.compareTo(right);
        if (type == Heap.Type.MAX ? cmp < 0 : cmp > 0) {
          System.out.println("HEAP VIOLATION: parent value (" + val + "), child value (" + right + "), heap type = " + type);
          return false;
        }
      }
    }

    return (left == null || _hasHeapPropertyRecurse(h, type, leftIndex)) &&
           (right == null || _hasHeapPropertyRecurse(h, type, rightIndex));
  }

  // Walks backwards through through the heap, testing children against their parents.
  // Parents are guaranteed to have a value if children have a value, otherwise it is an invalid
  // tree (orphaned child node).
  //
  // This is O(N) on the size of the array, which worst-cases to 2^M values in the heap
  // if the heap is pathologically unbalanced (as we scan every slot in every level
  // of the tree, which for a sparse tree could include a huge number of empty slots).
  public static <V2 extends Comparable<? super V2>> boolean hasHeapPropertyIterativeBackwards(ArrayList<V2> h, Heap.Type type) {
    // Note: stop when we reach the root element (i == 0); a one-element heap trivially fulfills the heap property
    for (int i = h.size() - 1; i > 0; i--) {
      V2 val = h.get(i);
      if (val == null) continue;

      int parentIndex = ((i + 1) >> 1) - 1;
      V2 parentVal = h.get(parentIndex);

      int cmp = parentVal.compareTo(val);
      if (type == Heap.Type.MAX ? cmp < 0 : cmp > 0) {
        System.out.println("HEAP VIOLATION: parent value (" + parentVal + "), child value (" + val + "), heap type = " + type);
        return false;
      }
    }
    return true;
  }

}
