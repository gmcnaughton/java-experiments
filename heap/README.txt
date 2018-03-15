# Heap

`Heap` implements a standard heap data structure in Java.

A heap is a tree which satisfies the *heap property*: the value of a parent
is greater than or equal to (for a max-heap) or less than or equal to (for a min-
heap) the values of its children.

No other relationship is guaranteed between nodes. Siblings and cousins are not
ordered with respect to each other. Only the relationship between parents and
children is enforced.

Values in a heap must be comparable (naturally ordered) so that the heap
property can be maintained.

Operations in this library are not thread-safe.

### Installation

    git clone https://github.com/gmcnaughton/java-experiments.git
    gradle test

### Usage

```java
Heap<Integer> h = new Heap<>(Heap.Type.MIN);
h.push(100);
h.push(2);
h.push(1);

h.peek(); // => 1

h.pop(); // => 1
h.pop(); // => 2
h.pop(); // => 100
h.pop(); // => IllegalStateException
```

## Contributing

Bug reports and pull requests are welcome on GitHub at https://github.com/gmcnaughton/java-experiments.

## License

The package is available as open source under the terms of the [MIT License](http://opensource.org/licenses/MIT).
