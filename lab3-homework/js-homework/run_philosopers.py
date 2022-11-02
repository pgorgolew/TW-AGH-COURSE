import os
import subprocess
import itertools
from datetime import datetime


def save_results(filepath, results, used_options):
    with open(filepath, 'a+') as file:
        file.write(f'------ {datetime.now().strftime("%d/%m/%Y %H:%M:%S")} ------{os.linesep}')
        file.write(f'OPTIONS: {used_options}{os.linesep}')
        file.write(results)


def run_philosophers():
    dir_path = os.path.dirname(os.path.abspath(__file__))
    js_path = os.path.join(dir_path, 'philosophers.js')

    philosophers = (5, 10, 15)
    iterations = (50, 100, 150)
    versions = ('asym', 'bothForks', 'conductor') # without naive version -> might block everything
    for p, i, v in itertools.product(philosophers, iterations, versions):
        result = subprocess.run(['node', js_path, str(p), str(i), v], stdout=subprocess.PIPE)
        used_options = f'philosophers={p} iterations={i} version={v}'
        save_results(os.path.join(dir_path, f'{v}_results.txt'), result.stdout.decode('utf-8'), used_options)
        print(f'DONE WITH PARAMS: {used_options}')


if __name__ == '__main__':
    run_philosophers()
