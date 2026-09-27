public class DynamicArray {

    private int[] data;
    private int size;

    private long accesses;
    private long movements;
    private long comparisons;

    public DynamicArray() {
        this(10);
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity < 1) {
            initialCapacity = 1;
        }

        data = new int[initialCapacity];
    }

    // Add to the end
    public void add(int value) {
        ensureCapacity();

        data[size] = value;
        size++;

        movements++;
    }

    // Add at a specific index
    public void add(int index, int value) {

        checkPositionIndex(index);
        ensureCapacity();

        // Shift elements to the right
        for (int i = size; i > index; i--) {

            data[i] = data[i - 1];

            accesses++;
            movements++;
        }

        data[index] = value;

        movements++;
        size++;
    }

    // Remove element by index
    public int remove(int index) {

        checkElementIndex(index);

        int removed = data[index];
        accesses++;

        // Shift remaining elements to the left
        for (int i = index; i < size - 1; i++) {

            data[i] = data[i + 1];

            accesses++;
            movements++;
        }

        size--;

        return removed;
    }

    // Get element by index
    public int get(int index) {

        checkElementIndex(index);

        accesses++;

        return data[index];
    }

    // Search for value
    public boolean contains(int value) {

        for (int i = 0; i < size; i++) {

            accesses++;
            comparisons++;

            if (data[i] == value) {
                return true;
            }
        }

        return false;
    }

    private void ensureCapacity() {

        if (size < data.length) {
            return;
        }

        int newCapacity = data.length * 2;

        int[] newData = new int[newCapacity];

        for (int i = 0; i < size; i++) {

            newData[i] = data[i];

            accesses++;
            movements++;
        }

        data = newData;
    }

    private void checkElementIndex(int index) {

        if (index < 0 || index >= size) {

            throw new IndexOutOfBoundsException(
                    "Index: " + index +
                            ", size: " + size
            );
        }
    }

    private void checkPositionIndex(int index) {

        if (index < 0 || index > size) {

            throw new IndexOutOfBoundsException(
                    "Index: " + index +
                            ", size: " + size
            );
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // ---------------- Metrics ----------------

    public void resetMetrics() {

        accesses = 0;
        movements = 0;
        comparisons = 0;
    }

    public long getAccesses() {
        return accesses;
    }

    public long getMovements() {
        return movements;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getUpdateOperations() {
        return accesses + movements;
    }
}