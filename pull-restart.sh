#!/bin/bash
fuser -k 5000/tcp

app_path="/home/developer/Salarizare"
echo $app_path

cd $app_path

git pull

cd $app_path/server

mvn spring-boot:run

