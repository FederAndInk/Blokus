#!/usr/bin/env bash

res=$(echo resources/ default.cfg)
javac -Xlint:deprecation **/*.java
jar -cfe Blokus.jar controller.Main {model,view,controller,utils}/*.class $(echo $res)
rm -r **/*.class
