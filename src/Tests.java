import java.util.ArrayList;
import java.util.PriorityQueue;

public class Tests {

    public static void main(String[] args) {

        testDynamicArray();
        testLinkedList();
        testMinHeap();

        testEmptyStructures();
        testDuplicates();
        testBoundaryIndices();
        testLargeInput();

        System.out.println();
        System.out.println("ALL TESTS PASSED");
    }

    private static void testDynamicArray() {

        System.out.println("Testing DynamicArray...");

        DynamicArray actual = new DynamicArray();

        ArrayList<Integer> expected = new ArrayList<>();

        actual.add(10);
        actual.add(20);
        actual.add(30);

        expected.add(10);
        expected.add(20);
        expected.add(30);

        actual.add(1, 15);
        expected.add(1, 15);

        check(
                actual.size() == expected.size(),
                "DynamicArray size"
        );

        for (int i = 0; i < expected.size(); i++) {

            check(
                    actual.get(i) == expected.get(i),
                    "DynamicArray get"
            );
        }

        check(
                actual.contains(15),
                "DynamicArray contains"
        );

        check(
                !actual.contains(999),
                "DynamicArray missing value"
        );

        int actualRemoved = actual.remove(1);
        int expectedRemoved = expected.remove(1);

        check(
                actualRemoved == expectedRemoved,
                "DynamicArray remove"
        );

        System.out.println("DynamicArray tests passed.");
    }

    private static void testLinkedList() {

        System.out.println("Testing LinkedList...");

        LinkedList actual = new LinkedList();

        java.util.LinkedList<Integer> expected =
                new java.util.LinkedList<>();

        actual.add(10);
        actual.add(20);
        actual.add(30);

        expected.add(10);
        expected.add(20);
        expected.add(30);

        actual.add(1, 15);
        expected.add(1, 15);

        check(
                actual.size() == expected.size(),
                "LinkedList size"
        );

        for (int i = 0; i < expected.size(); i++) {

            check(
                    actual.get(i) == expected.get(i),
                    "LinkedList get"
            );
        }

        check(
                actual.contains(15),
                "LinkedList contains"
        );

        check(
                !actual.contains(999),
                "LinkedList missing"
        );

        int actualRemoved = actual.remove(1);
        int expectedRemoved = expected.remove(1);

        check(
                actualRemoved == expectedRemoved,
                "LinkedList remove"
        );

        System.out.println("LinkedList tests passed.");
    }

    private static void testMinHeap() {

        System.out.println("Testing MinHeap...");

        int[] values = {
                5, 3, 8, 1, 1, 9, 2, 7
        };

        MinHeap heap = new MinHeap();

        PriorityQueue<Integer> expected =
                new PriorityQueue<>();

        for (int value : values) {

            heap.insert(value);
            expected.add(value);

            check(
                    heap.isValidHeap(),
                    "Heap property after insert"
            );

            check(
                    heap.peekMin() == expected.peek(),
                    "peekMin"
            );
        }

        int previous = Integer.MIN_VALUE;

        while (!expected.isEmpty()) {

            int actualValue = heap.extractMin();
            int expectedValue = expected.remove();

            check(
                    actualValue == expectedValue,
                    "extractMin"
            );

            check(
                    actualValue >= previous,
                    "Non-decreasing extraction order"
            );

            check(
                    heap.isValidHeap(),
                    "Heap property after extract"
            );

            previous = actualValue;
        }

        System.out.println("MinHeap tests passed.");
    }

    private static void testEmptyStructures() {

        System.out.println("Testing empty structures...");

        DynamicArray array = new DynamicArray();

        expectException(
                () -> array.get(0),
                "DynamicArray empty get"
        );

        expectException(
                () -> array.remove(0),
                "DynamicArray empty remove"
        );

        LinkedList list = new LinkedList();

        expectException(
                () -> list.get(0),
                "LinkedList empty get"
        );

        expectException(
                () -> list.remove(0),
                "LinkedList empty remove"
        );

        MinHeap heap = new MinHeap();

        expectException(
                heap::peekMin,
                "Heap empty peekMin"
        );

        expectException(
                heap::extractMin,
                "Heap empty extractMin"
        );

        System.out.println("Empty structure tests passed.");
    }

    private static void testDuplicates() {

        System.out.println("Testing duplicate values...");

        DynamicArray array = new DynamicArray();

        array.add(5);
        array.add(5);
        array.add(5);

        check(
                array.contains(5),
                "DynamicArray duplicates"
        );

        LinkedList list = new LinkedList();

        list.add(7);
        list.add(7);
        list.add(7);

        check(
                list.contains(7),
                "LinkedList duplicates"
        );

        MinHeap heap = new MinHeap();

        heap.insert(3);
        heap.insert(3);
        heap.insert(3);

        check(
                heap.extractMin() == 3,
                "Heap duplicates"
        );

        System.out.println("Duplicate tests passed.");
    }

    private static void testBoundaryIndices() {

        System.out.println("Testing boundary indices...");

        DynamicArray array = new DynamicArray();

        array.add(10);
        array.add(20);

        array.add(0, 5);
        array.add(array.size(), 30);

        check(
                array.get(0) == 5,
                "DynamicArray first index"
        );

        check(
                array.get(array.size() - 1) == 30,
                "DynamicArray last index"
        );

        LinkedList list = new LinkedList();

        list.add(10);
        list.add(20);

        list.add(0, 5);
        list.add(list.size(), 30);

        check(
                list.get(0) == 5,
                "LinkedList first index"
        );

        check(
                list.get(list.size() - 1) == 30,
                "LinkedList last index"
        );

        System.out.println("Boundary tests passed.");
    }

    private static void testLargeInput() {

        System.out.println("Testing large input...");

        DynamicArray array = new DynamicArray();

        LinkedList list = new LinkedList();

        MinHeap heap = new MinHeap();

        for (int i = 0; i < 100_000; i++) {

            array.add(i);
            list.add(i);
            heap.insert(i);
        }

        check(
                array.get(99_999) == 99_999,
                "DynamicArray large input"
        );

        check(
                list.get(99_999) == 99_999,
                "LinkedList large input"
        );

        check(
                heap.peekMin() == 0,
                "Heap large input"
        );

        System.out.println("Large input tests passed.");
    }

    private static void expectException(
            Runnable action,
            String message
    ) {

        boolean exceptionThrown = false;

        try {

            action.run();

        } catch (RuntimeException e) {

            exceptionThrown = true;
        }

        check(
                exceptionThrown,
                message
        );
    }

    private static void check(
            boolean condition,
            String message
    ) {

        if (!condition) {

            throw new AssertionError(
                    "TEST FAILED: " + message
            );
        }
    }
}