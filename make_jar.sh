#!/usr/bin/env bash

javac **/*.java
res=$(echo)
jar -cfe Blokus.jar Blokus {model,view,controller,utils}/*.class $(echo $res)
rm -r **/*.class
