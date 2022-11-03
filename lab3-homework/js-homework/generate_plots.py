import numpy as np
import matplotlib.pyplot as plt


def generate_plot(filename, title, **kwargs):
    # set width of bar
    bar_width = 0.25
    fig = plt.subplots(figsize=(12, 8))

    # Set position of bar on X axis
    versions = list(kwargs.keys())
    colors = ('r', 'g', 'b')  # for this case max 3 colors will be used
    brs = [np.arange(len(kwargs[versions[0]]))]
    for _ in range(len(versions) - 1):
        brs.append([x + bar_width for x in brs[-1]])

    for i in range(len(versions)):
        plt.bar(brs[i], kwargs[versions[i]], color=colors[i], width=bar_width, edgecolor='grey', label=versions[i])

    # Adding Xticks
    plt.ylabel('Waiting time [ms]', fontweight='bold', fontsize=15)
    plt.xticks([r + bar_width for r in range(len(kwargs[versions[0]]))],
               list(range(len(kwargs[versions[0]]))))

    plt.legend()
    plt.title = title
    plt.savefig(filename)
