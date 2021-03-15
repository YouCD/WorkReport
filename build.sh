#!/bin/bash

git clone https://github.com/YouCD/WorkReportFrontend.git
cd WorkReportFrontend
npm install
npm run build
cp -r dist/* ../web/dist/
cd ..

wget https://github.com/upx/upx/releases/download/v3.96/upx-3.96-amd64_linux.tar.xz
tar xf upx-3.96-amd64_linux.tar.xz
mv upx-3.96-amd64_linux/upx ./
