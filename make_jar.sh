#!/usr/bin/env bash

javac **/*.java
res=$(echo)
jar -cfe Blokus.jar Blokus **/*.class $(echo $res)
rm -r **/*.class
