#!/bin/bash

docker run -it --rm -p 8888:8080 -v $PWD/config:/mnt/config:rw -v $PWD/files:/mnt/files:rw -e "DEBUG_LEVEL=1" webfilesys

