import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.util.Random;

public class Benchmark {

    private static final int[] SIZES = {
            100,
            1_000,
            10_000,
            100_000
    };

    private static final int REPEATS = 5;
    private static final int RANDOM_ACCESS_OPERATIONS = 10_000;
    private static final int SEARCH_OPERATIONS = 1_000;
    private static final int UPDATE_OPERATIONS = 1_000;

    private static volatile long blackhole;

    public static void main(String[] args) throws IOException {

        Files.createDirectories(
                Paths.get("results", "tables")
        );

        Files.createDirectories(
                Paths.get("results", "plots")
        );

        warmUp();

        workload1RandomAccess();
        workload2Search();
        workload3InsertionRemoval();
        workload4Heap();

        System.out.println();
        System.out.println("All benchmarks finished.");
        System.out.println("Results: results/tables/");
        System.out.println("blackhole = " + blackhole);
    }

    private static void workload1RandomAccess()
            throws IOException {

        StringBuilder csv = new StringBuilder();

        csv.append(
                "structure,n,operations," +
                        "average_time_ns,average_accesses," +
                        "theoretical_complexity\n"
        );

        for (int n : SIZES) {

            int[] data =
                    generateData(n, 42);

            int[] indices =
                    generateIndices(
                            RANDOM_ACCESS_OPERATIONS,
                            n,
                            42
                    );

            Measurement arrayResult =
                    measureArrayRandomAccess(
                            data,
                            indices
                    );

            Measurement listResult =
                    measureListRandomAccess(
                            data,
                            indices
                    );

            csv.append(
                    createRow(
                            "DynamicArray",
                            n,
                            RANDOM_ACCESS_OPERATIONS,
                            arrayResult.averageTime,
                            arrayResult.averageMetric,
                            "Theta(1)"
                    )
            );

            csv.append(
                    createRow(
                            "LinkedList",
                            n,
                            RANDOM_ACCESS_OPERATIONS,
                            listResult.averageTime,
                            listResult.averageMetric,
                            "Theta(n)"
                    )
            );

            System.out.println(
                    "Workload 1 finished for n = " + n
            );
        }

        writeCSV(
                "workload1_random_access.csv",
                csv.toString()
        );
    }

    private static Measurement measureArrayRandomAccess(
            int[] data,
            int[] indices
    ) {

        long totalTime = 0;
        long totalAccesses = 0;

        for (int repeat = 0;
             repeat < REPEATS;
             repeat++) {

            DynamicArray array =
                    buildDynamicArray(data);

            array.resetMetrics();

            long checksum = 0;

            long start =
                    System.nanoTime();

            for (int index : indices) {
                checksum += array.get(index);
            }

            long end =
                    System.nanoTime();

            blackhole ^= checksum;

            totalTime += end - start;

            totalAccesses +=
                    array.getAccesses();
        }

        return new Measurement(
                totalTime / (double) REPEATS,
                totalAccesses / (double) REPEATS
        );
    }

    private static Measurement measureListRandomAccess(
            int[] data,
            int[] indices
    ) {

        long totalTime = 0;
        long totalAccesses = 0;

        for (int repeat = 0;
             repeat < REPEATS;
             repeat++) {

            LinkedList list =
                    buildLinkedList(data);

            list.resetMetrics();

            long checksum = 0;

            long start =
                    System.nanoTime();

            for (int index : indices) {
                checksum += list.get(index);
            }

            long end =
                    System.nanoTime();

            blackhole ^= checksum;

            totalTime += end - start;

            totalAccesses +=
                    list.getNodeAccesses();
        }

        return new Measurement(
                totalTime / (double) REPEATS,
                totalAccesses / (double) REPEATS
        );
    }

    private static void workload2Search()
            throws IOException {

        StringBuilder csv =
                new StringBuilder();

        csv.append(
                "structure,n,operations," +
                        "average_time_ns," +
                        "average_comparisons," +
                        "theoretical_complexity\n"
        );

        for (int n : SIZES) {

            int[] data =
                    generateData(n, 42);

            int[] searchValues =
                    generateSearchValues(
                            SEARCH_OPERATIONS,
                            n,
                            42
                    );

            Measurement arrayResult =
                    measureArraySearch(
                            data,
                            searchValues
                    );

            Measurement listResult =
                    measureListSearch(
                            data,
                            searchValues
                    );

            csv.append(
                    createRow(
                            "DynamicArray",
                            n,
                            SEARCH_OPERATIONS,
                            arrayResult.averageTime,
                            arrayResult.averageMetric,
                            "Theta(n)"
                    )
            );

            csv.append(
                    createRow(
                            "LinkedList",
                            n,
                            SEARCH_OPERATIONS,
                            listResult.averageTime,
                            listResult.averageMetric,
                            "Theta(n)"
                    )
            );

            System.out.println(
                    "Workload 2 finished for n = " + n
            );
        }

        writeCSV(
                "workload2_search.csv",
                csv.toString()
        );
    }

    private static Measurement measureArraySearch(
            int[] data,
            int[] values
    ) {

        long totalTime = 0;
        long totalComparisons = 0;

        for (int repeat = 0;
             repeat < REPEATS;
             repeat++) {

            DynamicArray array =
                    buildDynamicArray(data);

            array.resetMetrics();

            long found = 0;

            long start =
                    System.nanoTime();

            for (int value : values) {

                if (array.contains(value)) {
                    found++;
                }
            }

            long end =
                    System.nanoTime();

            blackhole ^= found;

            totalTime += end - start;

            totalComparisons +=
                    array.getComparisons();
        }

        return new Measurement(
                totalTime / (double) REPEATS,
                totalComparisons / (double) REPEATS
        );
    }

    private static Measurement measureListSearch(
            int[] data,
            int[] values
    ) {

        long totalTime = 0;
        long totalComparisons = 0;

        for (int repeat = 0;
             repeat < REPEATS;
             repeat++) {

            LinkedList list =
                    buildLinkedList(data);

            list.resetMetrics();

            long found = 0;

            long start =
                    System.nanoTime();

            for (int value : values) {

                if (list.contains(value)) {
                    found++;
                }
            }

            long end =
                    System.nanoTime();

            blackhole ^= found;

            totalTime += end - start;

            totalComparisons +=
                    list.getComparisons();
        }

        return new Measurement(
                totalTime / (double) REPEATS,
                totalComparisons / (double) REPEATS
        );
    }

    private static void workload3InsertionRemoval()
            throws IOException {

        StringBuilder csv =
                new StringBuilder();

        csv.append(
                "structure,operation,n,operations," +
                        "average_time_ns," +
                        "average_movements_or_accesses," +
                        "theoretical_complexity\n"
        );

        for (int n : SIZES) {

            int[] data =
                    generateData(n, 42);

            int[] insertValues =
                    generateData(
                            UPDATE_OPERATIONS,
                            99
                    );

            addUpdateRow(
                    csv,
                    "DynamicArray",
                    "insert_beginning",
                    n,
                    UPDATE_OPERATIONS,
                    measureArrayInsertion(
                            data,
                            insertValues,
                            0
                    ),
                    "Theta(n)"
            );

            addUpdateRow(
                    csv,
                    "LinkedList",
                    "insert_beginning",
                    n,
                    UPDATE_OPERATIONS,
                    measureListInsertion(
                            data,
                            insertValues,
                            0
                    ),
                    "Theta(1)"
            );

            int beginningRemovalOperations =
                    Math.min(
                            UPDATE_OPERATIONS,
                            n
                    );

            addUpdateRow(
                    csv,
                    "DynamicArray",
                    "remove_beginning",
                    n,
                    beginningRemovalOperations,
                    measureArrayRemoval(
                            data,
                            0,
                            beginningRemovalOperations
                    ),
                    "Theta(n)"
            );

            addUpdateRow(
                    csv,
                    "LinkedList",
                    "remove_beginning",
                    n,
                    beginningRemovalOperations,
                    measureListRemoval(
                            data,
                            0,
                            beginningRemovalOperations
                    ),
                    "Theta(1)"
            );

            int middle =
                    n / 2;

            addUpdateRow(
                    csv,
                    "DynamicArray",
                    "insert_middle",
                    n,
                    UPDATE_OPERATIONS,
                    measureArrayInsertion(
                            data,
                            insertValues,
                            middle
                    ),
                    "Theta(n)"
            );

            addUpdateRow(
                    csv,
                    "LinkedList",
                    "insert_middle",
                    n,
                    UPDATE_OPERATIONS,
                    measureListInsertion(
                            data,
                            insertValues,
                            middle
                    ),
                    "Theta(n)"
            );

            int middleRemovalOperations =
                    Math.min(
                            UPDATE_OPERATIONS,
                            n - middle
                    );

            addUpdateRow(
                    csv,
                    "DynamicArray",
                    "remove_middle",
                    n,
                    middleRemovalOperations,
                    measureArrayRemoval(
                            data,
                            middle,
                            middleRemovalOperations
                    ),
                    "Theta(n)"
            );

            addUpdateRow(
                    csv,
                    "LinkedList",
                    "remove_middle",
                    n,
                    middleRemovalOperations,
                    measureListRemoval(
                            data,
                            middle,
                            middleRemovalOperations
                    ),
                    "Theta(n)"
            );

            System.out.println(
                    "Workload 3 finished for n = " + n
            );
        }

        writeCSV(
                "workload3_updates.csv",
                csv.toString()
        );
    }

    private static Measurement measureArrayInsertion(
            int[] data,
            int[] values,
            int index
    ) {

        long totalTime = 0;
        long totalOperations = 0;

        for (int repeat = 0;
             repeat < REPEATS;
             repeat++) {

            DynamicArray array =
                    buildDynamicArray(data);

            array.resetMetrics();

            long start =
                    System.nanoTime();

            for (int value : values) {
                array.add(index, value);
            }

            long end =
                    System.nanoTime();

            blackhole ^= array.size();

            totalTime += end - start;

            totalOperations +=
                    array.getUpdateOperations();
        }

        return new Measurement(
                totalTime / (double) REPEATS,
                totalOperations / (double) REPEATS
        );
    }

    private static Measurement measureListInsertion(
            int[] data,
            int[] values,
            int index
    ) {

        long totalTime = 0;
        long totalOperations = 0;

        for (int repeat = 0;
             repeat < REPEATS;
             repeat++) {

            LinkedList list =
                    buildLinkedList(data);

            list.resetMetrics();

            long start =
                    System.nanoTime();

            for (int value : values) {
                list.add(index, value);
            }

            long end =
                    System.nanoTime();

            blackhole ^= list.size();

            totalTime += end - start;

            totalOperations +=
                    list.getUpdateOperations();
        }

        return new Measurement(
                totalTime / (double) REPEATS,
                totalOperations / (double) REPEATS
        );
    }

    private static Measurement measureArrayRemoval(
            int[] data,
            int index,
            int operations
    ) {

        long totalTime = 0;
        long totalOperations = 0;

        for (int repeat = 0;
             repeat < REPEATS;
             repeat++) {

            DynamicArray array =
                    buildDynamicArray(data);

            array.resetMetrics();

            long checksum = 0;

            long start =
                    System.nanoTime();

            for (int i = 0;
                 i < operations;
                 i++) {

                checksum += array.remove(index);
            }

            long end =
                    System.nanoTime();

            blackhole ^= checksum;

            totalTime += end - start;

            totalOperations +=
                    array.getUpdateOperations();
        }

        return new Measurement(
                totalTime / (double) REPEATS,
                totalOperations / (double) REPEATS
        );
    }

    private static Measurement measureListRemoval(
            int[] data,
            int index,
            int operations
    ) {

        long totalTime = 0;
        long totalOperations = 0;

        for (int repeat = 0;
             repeat < REPEATS;
             repeat++) {

            LinkedList list =
                    buildLinkedList(data);

            list.resetMetrics();

            long checksum = 0;

            long start =
                    System.nanoTime();

            for (int i = 0;
                 i < operations;
                 i++) {

                checksum += list.remove(index);
            }

            long end =
                    System.nanoTime();

            blackhole ^= checksum;

            totalTime += end - start;

            totalOperations +=
                    list.getUpdateOperations();
        }

        return new Measurement(
                totalTime / (double) REPEATS,
                totalOperations / (double) REPEATS
        );
    }

    private static void workload4Heap()
            throws IOException {

        StringBuilder csv =
                new StringBuilder();

        csv.append(
                "operation,n,operations," +
                        "average_time_ns," +
                        "average_comparisons," +
                        "theoretical_complexity\n"
        );

        for (int n : SIZES) {

            int[] values =
                    generateData(n, 42);

            HeapMeasurement result =
                    measureHeap(values);

            csv.append("insert,");
            csv.append(n).append(",");
            csv.append(n).append(",");

            csv.append(
                    format(
                            result.averageInsertTime
                    )
            ).append(",");

            csv.append(
                    format(
                            result.averageInsertComparisons
                    )
            ).append(",");

            csv.append("O(log n)")
                    .append("\n");

            csv.append("extractMin,");
            csv.append(n).append(",");
            csv.append(n).append(",");

            csv.append(
                    format(
                            result.averageExtractTime
                    )
            ).append(",");

            csv.append(
                    format(
                            result.averageExtractComparisons
                    )
            ).append(",");

            csv.append("O(log n)")
                    .append("\n");

            System.out.println(
                    "Workload 4 finished for n = " + n
            );
        }

        writeCSV(
                "workload4_heap.csv",
                csv.toString()
        );
    }

    private static HeapMeasurement measureHeap(
            int[] values
    ) {

        long totalInsertTime = 0;
        long totalExtractTime = 0;

        long totalInsertComparisons = 0;
        long totalExtractComparisons = 0;

        for (int repeat = 0;
             repeat < REPEATS;
             repeat++) {

            MinHeap heap =
                    new MinHeap();

            heap.resetMetrics();

            long insertStart =
                    System.nanoTime();

            for (int value : values) {
                heap.insert(value);
            }

            long insertEnd =
                    System.nanoTime();

            totalInsertTime +=
                    insertEnd - insertStart;

            totalInsertComparisons +=
                    heap.getComparisons();

            int[] extracted =
                    new int[values.length];

            heap.resetMetrics();

            long extractStart =
                    System.nanoTime();

            for (int i = 0;
                 i < extracted.length;
                 i++) {

                extracted[i] =
                        heap.extractMin();
            }

            long extractEnd =
                    System.nanoTime();

            totalExtractTime +=
                    extractEnd - extractStart;

            totalExtractComparisons +=
                    heap.getComparisons();

            verifyNonDecreasing(extracted);

            blackhole ^=
                    extracted[
                            extracted.length - 1
                            ];
        }

        return new HeapMeasurement(
                totalInsertTime /
                        (double) REPEATS,

                totalExtractTime /
                        (double) REPEATS,

                totalInsertComparisons /
                        (double) REPEATS,

                totalExtractComparisons /
                        (double) REPEATS
        );
    }

    private static int[] generateData(
            int n,
            long seed
    ) {

        Random random =
                new Random(seed);

        int[] result =
                new int[n];

        int bound =
                Math.max(
                        10,
                        n * 10
                );

        for (int i = 0;
             i < n;
             i++) {

            result[i] =
                    random.nextInt(bound);
        }

        return result;
    }

    private static int[] generateIndices(
            int count,
            int n,
            long seed
    ) {

        Random random =
                new Random(seed);

        int[] result =
                new int[count];

        for (int i = 0;
             i < count;
             i++) {

            result[i] =
                    random.nextInt(n);
        }

        return result;
    }

    private static int[] generateSearchValues(
            int count,
            int n,
            long seed
    ) {

        Random random =
                new Random(seed);

        int bound =
                Math.max(
                        10,
                        n * 10
                );

        for (int i = 0;
             i < n;
             i++) {

            random.nextInt(bound);
        }

        int[] result =
                new int[count];

        for (int i = 0;
             i < count;
             i++) {

            result[i] =
                    random.nextInt(bound);
        }

        return result;
    }

    private static DynamicArray buildDynamicArray(
            int[] values
    ) {

        DynamicArray array =
                new DynamicArray();

        for (int value : values) {
            array.add(value);
        }

        return array;
    }

    private static LinkedList buildLinkedList(
            int[] values
    ) {

        LinkedList list =
                new LinkedList();

        for (int value : values) {
            list.add(value);
        }

        return list;
    }

    private static void verifyNonDecreasing(
            int[] values
    ) {

        for (int i = 1;
             i < values.length;
             i++) {

            if (values[i] <
                    values[i - 1]) {

                throw new IllegalStateException(
                        "Heap extraction order is incorrect."
                );
            }
        }
    }

    private static void warmUp() {

        int[] data =
                generateData(
                        2_000,
                        123
                );

        int[] indices =
                generateIndices(
                        2_000,
                        data.length,
                        123
                );

        DynamicArray array =
                buildDynamicArray(data);

        LinkedList list =
                buildLinkedList(data);

        MinHeap heap =
                new MinHeap();

        long sum = 0;

        for (int index : indices) {

            sum += array.get(index);
            sum += list.get(index);
        }

        for (int value : data) {
            heap.insert(value);
        }

        while (!heap.isEmpty()) {
            sum += heap.extractMin();
        }

        blackhole ^= sum;
    }

    private static void addUpdateRow(
            StringBuilder csv,
            String structure,
            String operation,
            int n,
            int operations,
            Measurement result,
            String complexity
    ) {

        csv.append(structure).append(",");
        csv.append(operation).append(",");
        csv.append(n).append(",");
        csv.append(operations).append(",");

        csv.append(
                format(
                        result.averageTime
                )
        ).append(",");

        csv.append(
                format(
                        result.averageMetric
                )
        ).append(",");

        csv.append(complexity)
                .append("\n");
    }

    private static String createRow(
            String structure,
            int n,
            int operations,
            double averageTime,
            double averageMetric,
            String complexity
    ) {

        return structure + "," +
                n + "," +
                operations + "," +
                format(averageTime) + "," +
                format(averageMetric) + "," +
                complexity + "\n";
    }

    private static String format(
            double value
    ) {

        return String.format(
                Locale.US,
                "%.2f",
                value
        );
    }

    private static void writeCSV(
            String fileName,
            String content
    ) throws IOException {

        Path path =
                Paths.get(
                        "results",
                        "tables",
                        fileName
                );

        Files.writeString(
                path,
                content,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    private static class Measurement {

        double averageTime;
        double averageMetric;

        Measurement(
                double averageTime,
                double averageMetric
        ) {

            this.averageTime =
                    averageTime;

            this.averageMetric =
                    averageMetric;
        }
    }

    private static class HeapMeasurement {

        double averageInsertTime;
        double averageExtractTime;

        double averageInsertComparisons;
        double averageExtractComparisons;

        HeapMeasurement(
                double averageInsertTime,
                double averageExtractTime,
                double averageInsertComparisons,
                double averageExtractComparisons
        ) {

            this.averageInsertTime =
                    averageInsertTime;

            this.averageExtractTime =
                    averageExtractTime;

            this.averageInsertComparisons =
                    averageInsertComparisons;

            this.averageExtractComparisons =
                    averageExtractComparisons;
        }
    }
}