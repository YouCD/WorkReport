#!/bin/bash


download_url=`curl  -s  https://api.github.com/repos/youcd/WorkReportFrontend/releases/latest|grep browser_download_url|awk -F"\"" '{print $4}'`
wget $download_url -O web/dist.txz
cd web
tar Jxf dist.txz
cd ..

wget https://github.com/upx/upx/releases/download/v3.96/upx-3.96-amd64_linux.tar.xz
tar xf upx-3.96-amd64_linux.tar.xz
mv upx-3.96-amd64_linux/upx ./
