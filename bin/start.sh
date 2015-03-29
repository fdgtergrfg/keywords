#!/bin/bash

tmp='./lib/*':$tmp
tmp='./bin':$tmp
tmp='./target/*':$tmp

CLASSPATH=$tmp:$CLASSPATH


echo $CLASSPATH
JVM_ARGS="-Xmn164m -Xmx600m -Xms600m -XX:NewRatio=4 -XX:SurvivorRatio=4 -XX:Max
#echo JVM_ARGS=$JVM_ARGS
#ulimit -n 400000
#echo "" > nohup.out
java $JVM_ARGS -classpath $CLASSPATH keyword.KeywordSort >>log/info.log 2>&1 &