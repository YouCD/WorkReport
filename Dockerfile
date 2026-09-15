# syntax=docker/dockerfile:1

# ============ Stage 1: 编译前端 ============
FROM node:22-alpine AS frontend
WORKDIR /app/frontend/workReport_vue

# 先复制依赖清单, 利用 Docker 层缓存
COPY frontend/workReport_vue/package.json frontend/workReport_vue/package-lock.json ./
RUN npm ci

# 复制前端源码并构建(产物输出到 backend/web/dist)
COPY frontend/workReport_vue ./
RUN npm run build

# ============ Stage 2: 编译后端 ============
FROM golang:1.27.1-alpine AS backend
WORKDIR /app

# 复制后端源码(web/dist 已被 .dockerignore 排除, 不会带入本地产物)
COPY backend ./backend

# 复制前端构建产物(内嵌到后端静态资源, 覆盖占位目录)
COPY --from=frontend /app/backend/web/dist ./backend/web/dist

# 编译单二进制(使用与 backend/Makefile 一致的版本注入)
RUN cd backend && CGO_ENABLED=0 go build -ldflags "-w -s -X 'WorkReport/common.BuildTime=$(date "+%F %T")' -X 'WorkReport/common.CommitID=$(git rev-parse HEAD 2>/dev/null || echo unknown)' -X 'WorkReport/common.GoVersion=$(go version)' -X 'WorkReport/common.Version=$(git describe --tags 2>/dev/null || echo unknown)' -X 'WorkReport/common.BuildUser=docker'" -o /app/WorkReport .

# ============ Stage 3: 运行镜像 ============
FROM alpine:3.20
RUN apk add --no-cache ca-certificates tzdata && \
    adduser -D -u 1000 appuser
WORKDIR /app

COPY --from=backend /app/WorkReport /app/WorkReport
COPY --from=backend /app/backend/internal/config/config.yaml /app/config.yaml

USER appuser
EXPOSE 8080

# 默认命令: 运行服务(可用 -f 覆盖配置文件)
ENTRYPOINT ["/app/WorkReport"]
CMD ["run", "-f", "config.yaml"]