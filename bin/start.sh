#!/bin/bash

tmp='./lib':$tmp
tmp='./bin':$tmp
tmp='./target/keyword-0.0.1-SNAPSHOT-jar-with-dependencies-without-resources/*':$tmp
tmp='./target/classes':$tmp

CLASSPATH=$tmp:$CLASSPATH


echo $CLASSPATH
JVM_ARGS="-Xmn164m -Xmx600m -Xms600m -XX:NewRatio=4 -XX:SurvivorRatio=4 -XX:MaxTenuringThreshold=2"
java $JVM_ARGS -classpath $CLASSPATH keyword.KeywordSort >>log/info.log 2>&1 &