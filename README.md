# WorkReport

`WorkReport` 是一款用于记录工作日志的简易系统，以**事件**的形式记录日常运维与项目工作内容。采用前后端分离架构：

- **前端**：`Vue 3 + TypeScript + Vite + ant-design-vue 4`
- **后端**：`Go + Gin + GORM + MySQL`

## 功能特性

- 工作日志的增删改查、分页浏览
- 工作类别（数据字典）管理：工作大类 / 工作子类
- 按内容、日期、工作类别搜索
- 按时间范围导出 Excel（`WorkLog.xlsx`）
- 本周日志一键汇总填入
- **AI 周报**：基于 LLM（通义千问 / GLM）智能整理、去重、生成进度与下周计划
- **邮件发送**：将整理后的周报通过 SMTP 发送
- 内嵌静态资源，单二进制部署

## 目录结构

```
WorkReport
├── backend                 # Go 后端
│   ├── cmd                 # cobra 命令行（init / run / reset / update / version）
│   ├── internal/config     # 配置解析（config.yaml）
│   ├── pkg                 # mcp / llm / email / tools 等能力封装
│   ├── web                 # Gin 路由、处理器、模型
│   │   └── dist            # 前端构建产物（go:embed 内嵌）
│   ├── bin                 # 后端编译产物
│   └── Makefile
├── frontend
│   └── workReport_vue      # Vue3 前端
│       └── src
└── Makefile                # 一键构建（前端 + 后端）
```

## 环境要求

- Go 1.24+
- Node.js 18+ / npm
- MySQL 5.7+

## 构建

项目根目录提供统一的 `Makefile`：

```sh
# 依次编译前端 + 后端
make all

# 仅编译前端（产物输出到 backend/web/dist）
make frontend

# 仅编译后端（产物输出到 backend/bin/WorkReport）
make backend

# 清理产物
make clean
```

后端也支持单独跨平台构建（在 `backend` 目录下）：

```sh
make build          # 当前平台
make build-linux    # linux amd64
make build-darwin   # macOS
make build-win      # windows amd64
```

## 配置

后端通过 `-f` 指定配置文件（示例见 `backend/internal/config/config.yaml`）：

```yaml
global:
  logLevel: "info"
  port: 8080
  token: youcd
db:
  host: "127.0.0.1"
  user: root
  pwd: "P@ssw0rd"
  port: 3306
  name: worklog
llm:
  apiKey: "xxxxxxxxx"
  baseURL: "https://dashscope.aliyuncs.com/compatible-mode/v1"
  model: "qwen3-235b-a22b-instruct-2507"
email:
  host: "smtp.qiye.aliyun.com"
  port: "465"
  user: "a@a.onaliyun.com"
  pwd: "sasa"
  to: ["a@a.onaliyun.com"]
```

## 数据库初始化

```sh
# 1. 创建数据库
# mysql -uroot -p'P@ssw0rd' -h127.0.0.1
# MySQL [(none)]> create database worklog charset utf8mb4;

# 2. 初始化表结构与默认账户
./WorkReport init -f config.yaml
```

> 默认账户 `admin`，密码 `P@ssw0rd`（可通过 `init -n` / `-w` 指定）

## 运行

```sh
./WorkReport run -f config.yaml
```

启动后自动打开浏览器：`http://127.0.0.1:8080/#/`

## FAQ

- **为什么会有这个软件**：运维的工作相对分散，有些工作不适合按项目制管理，因此以事件的形式记录日常工作。
- **和 Excel 有什么区别**：Excel 能满足需求，但下班时记录日志不方便。WorkReport 采用 B/S 架构，随时随地（公网可达时）即可记录。