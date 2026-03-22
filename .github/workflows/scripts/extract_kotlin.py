import re
import sys

with open(sys.argv[1], 'r') as f:
    content = f.read()

match = re.search(r'```(?:kotlin)?\n(.*?)```', content, re.DOTALL)
if match:
    with open(sys.argv[2], 'w') as f:
        f.write(match.group(1))
else:
    with open(sys.argv[2], 'w') as f:
        f.write(content)