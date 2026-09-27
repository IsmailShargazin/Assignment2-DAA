import csv
from collections import defaultdict
from pathlib import Path

import matplotlib.pyplot as plt


TABLES = Path("results/tables")
PLOTS = Path("results/plots")

PLOTS.mkdir(
    parents=True,
    exist_ok=True
)


def read_csv(name):

    with open(
        TABLES / name,
        newline="",
        encoding="utf-8"
    ) as file:

        return list(
            csv.DictReader(file)
        )


def create_plot(
    rows,
    group_function,
    y_field,
    title,
    y_label,
    file_name
):

    groups = defaultdict(list)

    for row in rows:

        label = group_function(row)

        groups[label].append(
            (
                int(row["n"]),
                float(row[y_field])
            )
        )

    plt.figure()

    for label, points in groups.items():

        points.sort()

        x = [
            point[0]
            for point in points
        ]

        y = [
            point[1]
            for point in points
        ]

        plt.plot(
            x,
            y,
            marker="o",
            label=label
        )

    plt.xscale("log")

    plt.xlabel("Input size n")
    plt.ylabel(y_label)

    plt.title(title)

    plt.grid(True)
    plt.legend()

    plt.tight_layout()

    plt.savefig(
        PLOTS / file_name,
        dpi=200
    )

    plt.close()


# ====================================================
# WORKLOAD 1
# ====================================================

workload1 = read_csv(
    "workload1_random_access.csv"
)

create_plot(
    workload1,
    lambda row: row["structure"],
    "average_time_ns",
    "Workload 1: Random Access - Time vs n",
    "Average execution time (ns)",
    "workload1_time.png"
)

create_plot(
    workload1,
    lambda row: row["structure"],
    "average_accesses",
    "Workload 1: Random Access - Accesses vs n",
    "Average accesses",
    "workload1_accesses.png"
)


# ====================================================
# WORKLOAD 2
# ====================================================

workload2 = read_csv(
    "workload2_search.csv"
)

create_plot(
    workload2,
    lambda row: row["structure"],
    "average_time_ns",
    "Workload 2: Search - Time vs n",
    "Average execution time (ns)",
    "workload2_time.png"
)

create_plot(
    workload2,
    lambda row: row["structure"],
    "average_comparisons",
    "Workload 2: Search - Comparisons vs n",
    "Average comparisons",
    "workload2_comparisons.png"
)


# ====================================================
# WORKLOAD 3
# ====================================================

workload3 = read_csv(
    "workload3_updates.csv"
)

create_plot(
    workload3,
    lambda row:
        row["structure"] +
        " " +
        row["operation"],
    "average_time_ns",
    "Workload 3: Insert/Remove - Time vs n",
    "Average execution time (ns)",
    "workload3_time.png"
)

create_plot(
    workload3,
    lambda row:
        row["structure"] +
        " " +
        row["operation"],
    "average_movements_or_accesses",
    "Workload 3: Operations vs n",
    "Average movements/accesses",
    "workload3_operations.png"
)


# ====================================================
# WORKLOAD 4
# ====================================================

workload4 = read_csv(
    "workload4_heap.csv"
)

create_plot(
    workload4,
    lambda row: row["operation"],
    "average_time_ns",
    "Workload 4: Min-Heap - Time vs n",
    "Average execution time (ns)",
    "workload4_time.png"
)

create_plot(
    workload4,
    lambda row: row["operation"],
    "average_comparisons",
    "Workload 4: Min-Heap - Comparisons vs n",
    "Average comparisons",
    "workload4_comparisons.png"
)


print(
    "All plots were created in results/plots/"
)