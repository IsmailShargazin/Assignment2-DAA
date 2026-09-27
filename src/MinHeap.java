public class MinHeap {

    private int[] heap;

    private int size;

    private long comparisons;

    public MinHeap() {
        this(10);
    }

    public MinHeap(int initialCapacity) {

        if (initialCapacity < 1) {
            initialCapacity = 1;
        }

        heap = new int[initialCapacity];
    }

    // Insert value into heap
    public void insert(int value) {

        ensureCapacity();

        heap[size] = value;

        siftUp(size);

        size++;
    }

    // Return minimum element
    public int peekMin() {

        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }

        return heap[0];
    }

    // Remove and return minimum
    public int extractMin() {

        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }

        int minimum = heap[0];

        size--;

        if (size > 0) {

            heap[0] = heap[size];

            siftDown(0);
        }

        return minimum;
    }

    private void siftUp(int index) {

        while (index > 0) {

            int parent = (index - 1) / 2;

            comparisons++;

            if (heap[parent] <= heap[index]) {
                break;
            }

            swap(parent, index);

            index = parent;
        }
    }

    private void siftDown(int index) {

        while (true) {

            int left = 2 * index + 1;
            int right = left + 1;

            if (left >= size) {
                break;
            }

            int smallerChild = left;

            if (right < size) {

                comparisons++;

                if (heap[right] < heap[left]) {
                    smallerChild = right;
                }
            }

            comparisons++;

            if (heap[index] <= heap[smallerChild]) {
                break;
            }

            swap(index, smallerChild);

            index = smallerChild;
        }
    }

    private void swap(int first, int second) {

        int temp = heap[first];

        heap[first] = heap[second];
        heap[second] = temp;
    }

    private void ensureCapacity() {

        if (size < heap.length) {
            return;
        }

        int[] newHeap = new int[heap.length * 2];

        for (int i = 0; i < heap.length; i++) {
            newHeap[i] = heap[i];
        }

        heap = newHeap;
    }

    public boolean isValidHeap() {

        for (int i = 0; i < size; i++) {

            int left = 2 * i + 1;
            int right = left + 1;

            if (left < size && heap[i] > heap[left]) {
                return false;
            }

            if (right < size && heap[i] > heap[right]) {
                return false;
            }
        }

        return true;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // ---------------- Metrics ----------------

    public void resetMetrics() {
        comparisons = 0;
    }

    public long getComparisons() {
        return comparisons;
    }
}