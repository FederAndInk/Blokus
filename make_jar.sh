#!/usr/bin/env bash

javac **/*.java
res=$(echo resources/ default.cfg)
javac -Xlint:deprecation **/*.java
jar -cfe Blokus.jar controller.Main {model,view,controller,utils}/*.class $(echo $res)
rm -r **/*.class
