#!/usr/bin/env bash

javac -Xlint:deprecation **/*.java
res=$(echo default.cfg)
jar -cfe Blokus.jar controller.Main {model,view,controller,utils}/*.class $(echo $res)
rm -r **/*.class
