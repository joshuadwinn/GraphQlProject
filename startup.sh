#!/bin/sh
java -javaagent:dd-java-agent.jar \
     -Ddd.logs.injection=true \
     -Dspring.profiles.active="$SPRING_PROFILES_ACTIVE" \
     -jar application.jar

