#!/bin/bash

# Extract version number from GITHUB_REF
TAG=${GITHUB_REF##*/}

# Check if version matches format major.minor.patch+build
if [[ $TAG =~ ^[0-9]+\.[0-9]+\.[0-9]+(\+[0-9]+)?$ ]]
then
  export CREATE_TYPE="release_tag"
  echo "::set-output name=ref_type::release_tag"
else
  export CREATE_TYPE="other"
  echo "::set-output name=ref_type::other"
fi
