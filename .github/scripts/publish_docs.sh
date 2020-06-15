#!/bin/bash

MAJOR=$(echo $VERSION | grep -Eo "^[0-9]+")
echo "Detected major version $MAJOR"

# Clone komposten.github.io
git config --global user.email $GITHUB_EMAIL
git config --global user.name $GITHUB_USER
git clone --depth 1 https://${GITHUB_USER}:${GITHUB_TOKEN}@github.com/komposten/komposten.github.io.git

# Copy docs into the komposten.github.io repo
TARGET="komposten.github.io/leapjna/v$MAJOR"
echo "Copying javadocs to $TARGET" 
cp -r LeapJna/target/site/apidocs/. $TARGET

# Create a new branch, commit and push
echo "Creating a new branch and committing the docs"
cd komposten.github.io
git checkout -b docs/leapjna-release-$VERSION
git add .
git commit -m ":pencil: Update LeapJna javadocs to ${VERSION}"
git push -u origin docs/leapjna-release-$VERSION

# Create a pull request
echo "Creating pull request"
hub pull-request -m "Update LeapJna javadocs to ${VERSION}"