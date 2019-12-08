import pytest
from subprocess import *
import time
with open('log.txt', 'w') as output:
    spring = Popen(['java', '-jar', 'target/VineON-0.0.1-SNAPSHOT.jar'], stdout=output, stderr=output)

time.sleep(10)
pytest.main(["./tests"])
Popen.kill(spring)
