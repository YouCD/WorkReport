GOCMD			:=$(shell which go)
NPMCMD			:=$(shell which npm)
GOBUILD			:=$(GOCMD) build

IMPORT_PATH		:=WorkReport/cmd
BUILD_TIME		:=$(shell date "+%F %T")
COMMIT_ID       :=$(shell git rev-parse HEAD)
GO_VERSION      :=$(shell $(GOCMD) version)
VERSION			:=$(shell git describe --tags)
BUILD_USER		:=$(shell whoami)

FLAG			:="-X '${IMPORT_PATH}.buildTime=${BUILD_TIME}' -X '${IMPORT_PATH}.commitID=${COMMIT_ID}' -X '${IMPORT_PATH}.goVersion=${GO_VERSION}' -X '${IMPORT_PATH}.goVersion=${GO_VERSION}' -X '${IMPORT_PATH}.Version=${VERSION}' -X '${IMPORT_PATH}.buildUser=${BUILD_USER}'"

BINARY_DIR=bin
BINARY_NAME:=WorkReport

DIST_DIR :=$(shell git clone https://github.com/YouCD/WorkReportFrontend.git&&cd WorkReportFrontend&&${NPMCMD} install &&${NPMCMD} run build&&cp -r dist ../web )
GOPATH   :=$(shell go env GOPATH)
PACKR2   :=$(GOPATH)/bin/packr2
# linux
build-linux:
	${GOCMD} get -u github.com/gobuffalo/packr/v2/packr2
	$(GOCMD) generate
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 $(GOBUILD) -ldflags $(FLAG) -o $(BINARY_DIR)/$(BINARY_NAME)-linux

#mac
build-darwin:
	${GOCMD} get -u github.com/gobuffalo/packr/v2/packr2
	$(GOCMD) generate
	CGO_ENABLED=0 GOOS=darwin $(GOBUILD) -ldflags $(FLAG) -o $(BINARY_DIR)/$(BINARY_NAME)-darwin
# windows
build-win:
	${GOCMD} get -u github.com/gobuffalo/packr/v2/packr2
	$(GOCMD) generate
	CGO_ENABLED=0 GOOS=windows GOARCH=amd64 $(GOBUILD) -ldflags $(FLAG) -o $(BINARY_DIR)/$(BINARY_NAME)-win.exe

# 全平台
build-all:
	make build-linux
	make build-darwin
	make build-win