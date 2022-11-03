import os
import subprocess
import itertools
import re
from datetime import datetime
from generate_plots import generate_plot


def save_results(filepath, results, used_options):
    with open(filepath, 'a+') as file:
        file.write(f'------ {datetime.now().strftime("%d/%m/%Y %H:%M:%S")} ------{os.linesep}')
        file.write(f'OPTIONS: {used_options}{os.linesep}')
        file.write(results)


def run_philosophers():
    dir_path = os.path.dirname(os.path.abspath(__file__))
    js_path = os.path.join(dir_path, 'philosophers.js')
    data_by_philosophers_by_iterations = {
        5: {
            50: dict(),
            100: dict(),
            150: dict(),
        },
        10: {
            50: dict(),
            100: dict(),
            150: dict(),
        },
        15: {
            50: dict(),
            100: dict(),
            150: dict(),
        },
    }

    philosophers = (5, 10, 15)
    iterations = (50, 100, 150)
    versions = ('asym', 'bothForks', 'conductor')  # without naive version -> might block everything
    for p, i, v in itertools.product(philosophers, iterations, versions):
        result = subprocess.run(['node', js_path, str(p), str(i), v], stdout=subprocess.PIPE)
        output = result.stdout.decode('utf-8')

        c = re.compile(r'.*: (?P<time>\d+\.?\d*)')
        times = [float(c.match(line).group('time')) for line in filter(lambda l: l != '', output.split(sep='\n'))]
        data_by_philosophers_by_iterations[p][i][v] = times

        used_options = f'philosophers={p} iterations={i} version={v}'
        save_results(os.path.join(dir_path, f'{v}_results.txt'), output, used_options)
        print(f'DONE WITH PARAMS: {used_options}')

    for p in philosophers:
        for i in iterations:
            data = data_by_philosophers_by_iterations[p][i]
            generate_plot(**data, filename=f'js_{p}_{i}.png', title=f'Java with {p} Philosophers and {i} iterations')
            print(f'GENERATED PLOT WITH PARAMS: philosophers={p} iterations={i}')


if __name__ == '__main__':
    run_philosophers()
