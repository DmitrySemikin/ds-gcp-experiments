#!/bin/bash
podman run --name hello-nginx -d -p 8080:80 hello-docker-nginx
