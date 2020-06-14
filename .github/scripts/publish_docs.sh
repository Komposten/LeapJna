#!/bin/bash

# Extract version number from GITHUB_REF
VERSION=${GITHUB_REF##*/}

# Check if version matches format major.minor.patch+build
if [[ $VERSION =~ ^[0-9]+\.[0-9]+\.[0-9]+(\+[0-9]+)?$ ]]
then
  MAJOR=$(echo $VERSION | grep -Eo "^[0-9]+")
  echo "Detected version $VERSION"
else
  echo "Invalid version format: $VERSION"
  exit 1
fi

# Copy docs into the komposten.github.io repo
TARGET="komposten.github.io/leapjna/v$MAJOR"
echo "Copying javadocs to $TARGET" 
cp -r LeapJna/target/site/apidocs/. $TARGET

# Enter the repo folder
cd komposten.github.io

# Create a new branch, commit and push
echo "Creating a new branch and committing the docs"
git checkout -b docs/leapjna-release-$VERSION
git add .
git commit -m ":pencil: Update LeapJna javadocs to ${VERSION}"
git push -u origin docs/leapjna-release-$VERSION

# Create a pull request
echo "Creating pull request"
hub pull-request -m "Update LeapJna javadocs to ${VERSION}"