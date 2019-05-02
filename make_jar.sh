#!/usr/bin/env bash

javac **/*.java
res=$(echo resources/)
jar -cfe Blokus.jar controller.Main {model,view,controller,utils}/*.class $(echo $res)
rm -r **/*.class
