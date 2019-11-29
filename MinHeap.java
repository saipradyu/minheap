public class MinHeap {
    private final Building[] heap;
    private int size = 0;

    public MinHeap(int size) {
        heap = new Building[size];
    }

    // insert building to heap
    public void add(Building value) {
        if (isFull()) throw new IllegalStateException();
        heap[size] = new Building(value);
        size += 1;
        heapifyUp(); // call heapify up to maintain min heap invariant
    }

    // remove the min element from heap
    public Building remove() {
        Building result = peek();

        // replace the root with the last entry
        heap[0] = new Building(heap[size - 1]);
        heap[size - 1] = null;
        size -= 1;
        heapifyDown(); // call heapify down to maintain min heap invariant
        return result;
    }

    // get min element from heap
    public Building peek() {
        if (this.isEmpty()) {
            return null;
        }
        return heap[0];
    }

    // percolate downwards from root until heap invariant is maintained
    public void heapifyDown() {
        int index = 0;
        while (hasLeftChild(index)) {
            int leftChild = leftIndex(index);
            int smallerChild = leftChild;

            if (hasRightChild(index)) {
                int rightChild = rightIndex(index);
                if (isSmaller(rightChild, leftChild)) {
                    smallerChild = rightChild;
                }
            }
            if (isSmaller(smallerChild, index)) {
                swap(index, smallerChild);
                index = smallerChild;
            } else {
                // heap invariant is maintained
                return;
            }
        }
    }

    // percolate upwards until heap invariant is maintained
    private void heapifyUp() {
        int index = this.size - 1;

        while (hasParent(index)) {
            int parent = parentIndex(index);
            if (isSmaller(index, parent)) {
                swap(index, parent);
                index = parent;
            } else {
                // heap invariant is maintained
                return;
            }
        }
    }

    // check if heap is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // check if heap is full
    public boolean isFull() {
        return size == heap.length;
    }

    private boolean isSmaller(int left, int right) {
        return heap[left].compareTo(heap[right]) < 0;
    }

    private boolean hasParent(int i) {
        return i > 0;
    }

    private int leftIndex(int i) {
        return (i * 2) + 1;
    }

    private int rightIndex(int i) {
        return (i * 2) + 2;
    }

    private boolean hasLeftChild(int i) {
        return leftIndex(i) < size;
    }

    private boolean hasRightChild(int i) {
        return rightIndex(i) < size;
    }

    private int parentIndex(int i) {
        return (i - 1) / 2;
    }

    //swap objects at indexes
    private void swap(int j, int k) {
        Building tmp = new Building(heap[j]);
        heap[j] = new Building(heap[k]);
        heap[k] = tmp;
    }
}
