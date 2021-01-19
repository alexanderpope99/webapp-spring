#!/bin/bash
fuser -k 3000/tcp

cd $(pwd)/client;
npm start
