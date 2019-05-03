#!/usr/bin/env bash

javac **/*.java
if (($# == 1)); then
  java test/$1
else
  for test in test/*.class; do
    echo "$test"
    java ${test%.class}
  done
fi

rm -r **/*.class
