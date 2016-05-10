#!/bin/bash

cd client
NODE_ENV=PRODUCTION webpack
cp -r dist/* gh-pages/
git add gh-pages
cd ..
git commit -m "Update gh-pages"
git subtree push --prefix client/gh-pages origin gh-pages
git push
