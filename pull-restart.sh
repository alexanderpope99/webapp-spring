#!/bin/bash
fuser -k 5000/tcp

git pull

cd ~/Salarizare/server; mvn spring-boot:run
