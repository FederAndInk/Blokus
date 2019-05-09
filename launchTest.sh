#/bin/bash

export TESTNG_HOME="`pwd`/test/testNG"
export CLASSPATH="$CLASSPATH:$TESTNG_HOME/testng-7.0.0-SNAPSHOT.jar"

javac test/test/*.java
