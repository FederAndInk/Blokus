#!/usr/bin/env bash
shopt -s globstar

rm Blokus.jar
res=$(echo resources/ default.cfg)
javac -Xlint:deprecation **/*.java
jar -cfe Blokus.jar controller.Main src/{model,view,controller,utils}/*.class $(echo $res)
rm **/*.class
