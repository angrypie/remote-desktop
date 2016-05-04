#!/bin/bash

cd client
NODE_ENV=PRODUCTION webpack
cp -r dist/* gh-pages/
cd ..
git subtree push --prefix client/gh-pages origin gh-pages
