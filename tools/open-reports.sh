#!/bin/bash

# Opens code reports.
#
# Author: Dariusz Stefanski
# Date:   11.06.2014

REPORTS_DIR=`pwd`/"../build/reports"
BROWSER="firefox"

for report in /jacoco/test/html/index.html /tests/index.html /cucumber/index.html; do
    ${BROWSER} ${REPORTS_DIR}/${report}
done

