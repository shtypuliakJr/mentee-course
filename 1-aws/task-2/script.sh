#! /bin/bash
filename_variable=$1
date_variable=$2
version_value=$(aws s3api list-object-versions --bucket mentee-artur-shtypuliak-task2 --query "Versions[?Key == '${1}' && LastModified < '${2}' && ].[VersionId]")
aws s3