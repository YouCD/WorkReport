# WorkReport 构建脚本
# 用法:
#   make all            编译前端 + 编译后端(当前平台)
#   make frontend       仅编译前端
#   make backend        仅编译后端(当前平台, 使用 backend/Makefile 的 ldflags)
#   make backend-linux  编译后端 linux amd64
#   make backend-darwin 编译后端 darwin
#   make backend-win    编译后端 windows amd64
#   make clean          清理产物

FRONTEND_DIR := frontend/workReport_vue
BACKEND_DIR  := backend

.PHONY: all frontend backend backend-linux backend-darwin backend-win clean

all: frontend backend

# 编译前端(输出到 backend/web/dist)
frontend:
	@echo ">>> 编译前端..."
	cd $(FRONTEND_DIR) && npm run build
	@echo ">>> 前端编译完成, 产物: backend/web/dist"

# 编译后端(复用 backend/Makefile, 含全部 go ldflags 版本注入, 产物: backend/bin/WorkReport)
backend:
	@echo ">>> 编译后端..."
	cd $(BACKEND_DIR) && make build
	@echo ">>> 后端编译完成, 产物: backend/bin/WorkReport"

backend-linux:
	@echo ">>> 编译后端(linux amd64)..."
	cd $(BACKEND_DIR) && make build-linux

backend-darwin:
	@echo ">>> 编译后端(darwin)..."
	cd $(BACKEND_DIR) && make build-darwin

backend-win:
	@echo ">>> 编译后端(windows amd64)..."
	cd $(BACKEND_DIR) && make build-win

clean:
	cd $(BACKEND_DIR) && make clean
	@echo ">>> 清理完成"