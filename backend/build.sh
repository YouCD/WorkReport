#!/bin/bash


download_url=`curl  -s https://api.github.com/repos/youcd/WorkReportFrontend_vue3/releases/latest|grep browser_download_url|awk -F"\"" '{print $4}'`
wget $download_url -O dist.txz
tar xf dist.txz -C backend/web
rm -rf dist.txz


wget https://github.com/upx/upx/releases/download/v3.96/upx-3.96-amd64_linux.tar.xz
tar xf upx-3.96-amd64_linux.tar.xz
mv upx-3.96-amd64_linux/upx backend
