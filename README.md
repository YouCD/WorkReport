## WorkReport

[![Build Status](https://travis-ci.com/YouCD/WorkReport.svg?branch=master)](https://travis-ci.com/YouCD/WorkReport)

`WorkReport` 是记录工作日志的简易系统，主要用于以事件的形式记录工作日志，采用前后端分离架构

## 项目地址

* 前端：https://github.com/YouCD/WorkReport.git

* 后端：https://github.com/YouCD/WorkReportFrontend.git

## 功能

* 支持数据字典
* 支持数据导出
* 支持简单图表
* 支持搜索
* 支持本周周报生成

## 部署

* 下载 `WorkReport`

可以在项目的[releases页面](https://github.com/YouCD/WorkReport/releases)下载

* 部署`MySQL`

```sh
DATA=${PWD}/mysql
docker run -d --restart=always  --name mysql1 -v ${DATA}:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=P@ssw0rd -p 3306:3306 mysql:5.7.18
```

* 初始化数据库

```sh
# mysql -uroot -p'P@ssw0rd' -h127.0.0.1
MySQL [(none)]> create database worklog charset utf8mb4;
Query OK, 1 row affected (0.001 sec)


# ./WorkReport init              
2022/07/14 19:23:22 The default username is admin password is P@ssw0rd

```

> 默认账户 `admin`  密码：`P@ssw0rd`

* 运行

```sh
./WorkRepor run 
  
     m               #      mmmmm                                m   
#  #  #  mmm    m mm  #   m  #   "#  mmm   mmmm    mmm    m mm  mm#mm 
" #"# # #" "#   #"  " # m"   #mmmm" #"  #  #" "#  #" "#   #"  "   #   
 ## ##" #   #   #     #"#    #   "m #""""  #   #  #   #   #       #   
 #   #  "#m#"   #     #  "m  #    " "#mm"  ##m#"  "#m#"   #       "mm 
                                           #                          
                                           "                          
  
web server listen on 8080
[GIN-debug] [WARNING] Creating an Engine instance with the Logger and Recovery middleware already attached.

```

## FAQ：

* A：为什么会写这个软件：

​ 答：运维的工作相对分散，有的工作不能按照项目制的形式，所以大多数按照事件的形式去记录相关的工作

* B：和Execl有什么区别

​ 答：execl可以满足所有需求，但是在下班的时候去记录工作日志可能不是很方便，WorkReport B/S
架构，可以方便工作了一天的你，不想打开电脑的你，随时随地去记录日志(公网可以访问的情况下)

