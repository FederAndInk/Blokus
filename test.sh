#!/usr/bin/env bash

javac **/*.java
for test in test/*.class; do
  echo "$test"
  java ${test%.class}
done

rm -r **/*.class
