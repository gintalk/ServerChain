#!/bin/sh

xrpcgen-0.9.1.1 --gen java resources.thrift

cp gen-java/* ../src/ -rf
rm gen-java -rf

