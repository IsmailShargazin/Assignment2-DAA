public class LinkedList {

    private static class Node {

        int value;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }

    private Node head;
    private Node tail;

    private int size;

    private long nodeAccesses;
    private long comparisons;

    // Add to the end
    public void add(int value) {

        Node newNode = new Node(value);

        if (head == null) {

            head = newNode;
            tail = newNode;

        } else {

            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    // Add at index
    public void add(int index, int value) {

        checkPositionIndex(index);

        if (index == size) {

            add(value);
            return;
        }

        Node newNode = new Node(value);

        if (index == 0) {

            newNode.next = head;
            head = newNode;

            if (tail == null) {
                tail = newNode;
            }

            size++;
            return;
        }

        Node previous = getNode(index - 1);

        newNode.next = previous.next;
        previous.next = newNode;

        size++;
    }

    // Remove by index
    public int remove(int index) {

        checkElementIndex(index);

        if (index == 0) {

            nodeAccesses++;

            int removed = head.value;

            head = head.next;

            size--;

            if (size == 0) {
                tail = null;
            }

            return removed;
        }

        Node previous = getNode(index - 1);

        Node removedNode = previous.next;

        nodeAccesses++;

        previous.next = removedNode.next;

        size--;

        if (removedNode == tail) {
            tail = previous;
        }

        return removedNode.value;
    }

    // Get by index
    public int get(int index) {

        checkElementIndex(index);

        return getNode(index).value;
    }

    // Search
    public boolean contains(int value) {

        Node current = head;

        while (current != null) {

            nodeAccesses++;
            comparisons++;

            if (current.value == value) {
                return true;
            }

            current = current.next;
        }

        return false;
    }

    private Node getNode(int index) {

        Node current = head;

        for (int i = 0; i < index; i++) {

            nodeAccesses++;

            current = current.next;
        }

        nodeAccesses++;

        return current;
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

        nodeAccesses = 0;
        comparisons = 0;
    }

    public long getNodeAccesses() {
        return nodeAccesses;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getUpdateOperations() {
        return nodeAccesses;
    }
}