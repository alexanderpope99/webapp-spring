#!/bin/bash
fuser -k 5000/tcp

git pull

cd $(pwd)/server; mvn spring-boot:run
