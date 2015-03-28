#!/bin/bash


tmp='./src'
tmp='./target/classes':$tmp
tmp='./target/transfertknowledeandtagmatch-0.0.1-SNAPSHOT-jar-with-dependencies-without-resources/*':$tmp

CLASSPATH=$tmp:$CLASSPATH


echo $CLASSPATH
JVM_ARGS="-Xmn164m -Xmx600m -Xms600m -XX:NewRatio=4 -XX:SurvivorRatio=4 -XX:MaxTenuringThreshold=2"
#echo JVM_ARGS=$JVM_ARGS
#ulimit -n 400000
#echo "" > nohup.out
java $JVM_ARGS -classpath $CLASSPATH keyword.KeywordSort >>log/info.log 2>&1 &