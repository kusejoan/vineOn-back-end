import pytest
from subprocess import *
import time
import argparse

parser = argparse.ArgumentParser()

parser.add_argument('--setup','-s',action='store_true')

args = parser.parse_args()

if args.setup:
    with open('log.txt', 'w') as output:
        spring = Popen(['java', '-jar', 'target/VineON-0.0.1-SNAPSHOT.jar'], stdout=output, stderr=output)
    print("Waiting for spring boot to start", end='')
    for i in range(40):
        print(".", end='', flush=True)
        time.sleep(0.4)
    print("Spring boot (hopefully) ready")
pytest.main(["./tests"])
if args.setup:
    Popen.kill(spring)
