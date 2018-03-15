import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.StringJoiner;

/**
 * Heap implements a standard heap data structure.
 * 
 * <p>A heap is a tree which satisfies the *heap property*: the value of a parent
 * is greater than or equal to (for a max-heap) or less than or equal to (for a min-
 * heap) the values of its children.
 * 
 * <p>No other relationship is guaranteed between nodes. Siblings and cousins are not
 * ordered with respect to each other. Only the relationship between parents and
 * children is enforced.
 * 
 * <p>Values in a heap must be comparable (naturally ordered) so that the heap
 * property can be maintained.
 * 
 * <p>Operations in this library are not thread-safe.
 *
 * <pre>
 * {@code
 *     Heap<Integer> h = new Heap<>(Heap.Type.MIN);
 *     h.push(100);
 *     h.push(2);
 *     h.push(1);
 * 
 *     h.peek(); // => 1
 *
 *     h.pop(); // => 1
 *     h.pop(); // => 2
 *     h.pop(); // => 100
 *     h.pop(); // => IllegalStateException
 * }
 * </pre>
 */
public class Heap<V extends Comparable<? super V>> implements Iterable<V>, Cloneable {
  public static void main(String[] args) {
    Heap<Integer> h = new Heap<Integer>(Type.MIN);
    h.push(100);
    h.push(2);
    h.push(3);
    h.push(3);
    h.push(101);
    h.push(4);
    h.push(2);
    System.out.println(h);

    System.out.println("\nPopping...");
    while (!h.isEmpty()) {
      Integer i = h.pop();
      System.out.println(i);
      System.out.println(h);
      System.out.println("");
    }
    System.out.println("Done.");
  }

  public enum Type {
    MIN,
    MAX
  }

  private ArrayList<V> _values;
  private Type _type;

  public Heap(Type type) {
    _values = new ArrayList<V>();
    _type = type;
  }

  /**
   * @return the maximum value (for a max-heap) or minimum value (for a min-heap)
   * @throws IllegalStateException if the heap is empty
   */
  public V peek() {
    if (_values.isEmpty()) throw new IllegalStateException("Heap is empty");

    return _values.get(0);
  }

  /**
   * Adds the given value to the heap.
   *
   * @param val the value to add
   */
  public void push(V val) {
    if (val == null) throw new IllegalArgumentException("Cannot add null");

    _values.add(val);
    if (_values.size() > 1) {
      _siftUp(_values.size() - 1);
    }

    _assertInvariant();
  }

  /**
   * Removes the first value in the heap and returns it.
   *
   * @return maximum value (for a max-heap) or minimum value (for a min-heap)
   * @throws IllegalStateException if the heap is empty
   */
  public V pop() {
    if (_values.isEmpty()) throw new IllegalStateException("Heap is empty");

    V val = _values.get(0);
    V lastVal = _values.remove(_values.size() - 1);

    if (!_values.isEmpty()) {
      _values.set(0, lastVal);
      _siftDown(0);
    }

    _assertInvariant();
    return val;
  }

  /**
   * Replaces the first value in the heap with the given value. This is more
   * efficient than a pop followed by a push, as only one sift operation is
   * needed to maintain the heap property.
   *
   * @param val the value to add
   * @return maximum value (for a max-heap) or minimum value (for a min-heap)
   * @throws IllegalStateException if the heap is empty
   */
  public V replace(V val) {
    if (_values.isEmpty()) throw new IllegalStateException("Heap is empty");

    V origVal = _values.get(0);
    _values.set(0, val);
    _siftDown(0);
    _assertInvariant();
    return origVal;
  }

  /**
   * @return true if the heap property is currently satisfied. A value of false
   *         indicates an internal error!
   */
  public boolean valid() {
    return _checkInvariant();
  }

  public Iterator<V> iterator() {
    return _values.iterator();
  }

  /**
   * @return true if the heap contains no values
   */
  public boolean isEmpty() {
    return _values.isEmpty();
  }

  /**
   * @return the number of values in the heap
   */
  public int size() {
    return _values.size();
  }

  // sift up, aka swap up, aka bubble up
  // used when a node has been inserted; it's added to the end and then bubbles up (O(log N) swaps)
  private void _siftUp(int index) {
    // starting at index, move a value up until the heap property is satisfied
    // by swapping it with its parent and then checking again
    if (index < 1) return;
 
    int parentIndex = _parentIndex(index);
    V parentVal = _values.get(parentIndex);
    V childVal = _values.get(index);
    if (!_valid(parentVal, childVal)) {
      _swap(parentIndex, index);
      _siftUp(parentIndex); // WARNING: recursion!
    }
  }

  // sift down, aka swap down, aka bubble down
  // used when a node has been deleted; the last node in the heap is moved into its place and then bubbled down (O(log N) swaps)
  private void _siftDown(int index) {
    // starting at index, move a value down until the heap property is satisfied
    // * if it is greater than both of its children, no change is required
    // * otherwise, swap it with whichever child is greater (so the new parent
    //   upholds the heap property) and then repeat

    int leftChildIndex = _leftChildIndex(index);
    int rightChildIndex = _rightChildIndex(index);
    int size = _values.size();
    V parentVal = _values.get(index);

    if (rightChildIndex < size) {
      V leftChildVal = _values.get(leftChildIndex);
      V rightChildVal = _values.get(rightChildIndex);

      if (!_valid(parentVal, leftChildVal) || !_valid(parentVal, rightChildVal)) {
        int swapIndex;
        if (_type == Type.MAX) {
          swapIndex = (leftChildVal.compareTo(rightChildVal) >= 0 ? leftChildIndex : rightChildIndex);
        } else {
          swapIndex = (leftChildVal.compareTo(rightChildVal) <= 0 ? leftChildIndex : rightChildIndex);
        }
        _swap(index, swapIndex);
        _siftDown(swapIndex);
      } else {
        // Heap property upheld vs both children; nothing to do
      }
    } else if (leftChildIndex < size) {
      V leftChildVal = _values.get(leftChildIndex);
      if (!_valid(parentVal, leftChildVal)) {
        _swap(index, leftChildIndex);
        _siftDown(leftChildIndex);
      } else {
        // Heap property upheld vs only child; nothing to do
      }
    } else {
      // No children, heap property trivially upheld; nothing to do
    }
  }

  private int _parentIndex(int index) {
    return ((index + 1) >> 1) - 1;
  }

  private int _leftChildIndex(int index) {
    return ((index + 1) << 1) - 1;
  }

  private int _rightChildIndex(int index) {
    return ((index + 1) << 1);
  }

  private void _swap(int index1, int index2) {
    V val = _values.get(index2);
    _values.set(index2, _values.get(index1));
    _values.set(index1, val);
  }

  private boolean _valid(V parentVal, V childVal) {
    int cmp = parentVal.compareTo(childVal);
    return (_type == Type.MAX) ? cmp >= 0 : cmp <= 0;
  }

  private boolean _checkInvariant() {
    // Note: stop when we reach the root element (i == 0); a one-element heap trivially fulfills the heap property
    for (int i = size() - 1; i > 0; i--) {
      V val = _values.get(i);
      int parentIndex = _parentIndex(i);
      V parentVal = _values.get(parentIndex);
      if (!_valid(parentVal, val)) return false;
    }
    return true;
  }

  private void _assertInvariant() {
    if (!_checkInvariant()) throw new IllegalStateException("Invariant violation");
  }

  @Override public Heap<V> clone() {
    try {
      Heap<V> clone = (Heap<V>)super.clone();
      clone._values = (ArrayList<V>)_values.clone();
      clone._type = _type;
      return clone;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError(); // can't happen
    }
  }

  @Override public String toString() {
    StringJoiner sj = new StringJoiner(", ", "[", "] (size = " + size() + ", valid = " + valid() + ")");
    for (V val : this) {
      sj.add(val.toString());
    }
    return sj.toString();
  }
}
