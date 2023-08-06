#!/bin/bash
#podman run --name hello-nginx --cpus=0.08 --memory=128m -d -p 8080:80 hello-docker-nginx
podman run --name hello-nginx --cpus=0.08 --memory=128m -d -p 8080:8080 hello-docker-nginx
