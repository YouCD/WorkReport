GOCMD			:=$(shell which go)
NPMCMD			:=$(shell which npm)
GOBUILD			:=$(GOCMD) build

IMPORT_PATH		:=WorkReport/common
BUILD_TIME		:=$(shell date "+%F %T")
COMMIT_ID       :=$(shell git rev-parse HEAD)
GO_VERSION      :=$(shell $(GOCMD) version)
VERSION			:=$(shell git describe --tags)
BUILD_USER		:=$(shell whoami)

FLAG			:="-w -s -X '${IMPORT_PATH}.BuildTime=${BUILD_TIME}' -X '${IMPORT_PATH}.CommitID=${COMMIT_ID}' -X '${IMPORT_PATH}.GoVersion=${GO_VERSION}'  -X '${IMPORT_PATH}.Version=${VERSION}' -X '${IMPORT_PATH}.BuildUser=${BUILD_USER}'"

BINARY_DIR=bin
BINARY_NAME:=WorkReport

DIST_DIR :=$(shell git clone https://github.com/YouCD/WorkReportFrontend.git&&cd WorkReportFrontend&&${NPMCMD} install &&${NPMCMD} run build&&cp -r dist/* ../web/dist/ )
UPX_CMD  :=$(shell wget https://github.com/upx/upx/releases/download/v3.96/upx-3.96-amd64_linux.tar.xz&&tar xf upx-3.96-amd64_linux.tar.xz&&mv upx-3.96-amd64_linux/upx ./)
# linux
build-linux:
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 $(GOBUILD) -ldflags $(FLAG) -o $(BINARY_DIR)/$(BINARY_NAME)-linux
	./upx $(BINARY_DIR)/$(BINARY_NAME)-linux

#mac
build-darwin:
	CGO_ENABLED=0 GOOS=darwin $(GOBUILD) -ldflags $(FLAG) -o $(BINARY_DIR)/$(BINARY_NAME)-darwin
	./upx $(BINARY_DIR)/$(BINARY_NAME)-linux

# windows
build-win:
	CGO_ENABLED=0 GOOS=windows GOARCH=amd64 $(GOBUILD) -ldflags $(FLAG) -o $(BINARY_DIR)/$(BINARY_NAME)-win.exe
	./upx $(BINARY_DIR)/$(BINARY_NAME)-linux
# 全平台
build-all:
	make build-linux
	make build-darwin
	make build-win