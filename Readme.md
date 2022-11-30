本项目来源于https://github.com/Yin-Hongwei/music-website。

一切版权归原作者@[Yin-Hongwei](https://github.com/Yin-Hongwei)所有

[教学视频](https://www.bilibili.com/video/BV1Ck4y127cg?p=1&vd_source=7bbb95f0a4a6f4f051cb193b8898b1a4)

本仓库仅作为一个练习项目使用。

------

## 技术栈

### 后端

**SpringBoot + MyBatis**

### 前端

**Vue2.0 + javaScript + Vue-Router + Vuex + Axios + Element UI**





## 开发环境

macos13.0.1 

JDK： jdk-8u281

Maven 3.6.1

mysql：mysql-5.7.39

Navicat：16.1.2

node：8.16.0

IDE：IntelliJ IDEA 2020、VSCode

------

## 项目介绍

后端：music-server 

前端：music-manage（后台登录管理页面）music-client（前端网页展示页面）

## 项目开发记录

1 - 环境配置

2 - 设计数据库

3 - 开发

4 - 登录功能

5 - 后台：Home框架

6 - 后台：歌手管理Page1

7 - 后台：歌曲管理Page2

8 - 后台：歌单管理Page3歌单歌曲管理Page4

9 - 后台：用户管理Page5

10 - 后台：系统首页Page6

11 - 前台：首页

12 - 前台：搜索

13 - 前台：播放歌曲（有一个bug）

14 - 前台：注册登录

15 - 前台：歌单详情页+歌手详情页

16 - 前台：收藏

17 - 后台：管理收藏

18 - 项目部署

## 待解决问题

### 问题一

- 在后台播放音乐的图标，点击播放音乐后变为可暂停状态后无法再次改变，并且列表里的所有歌曲图标都会改变，但播放和暂停音乐的功能完好，只有图标状态不正确

### 问题二：

- 在前台播放列表点击播放❌**没实现**

### 问题三：

- **发现歌曲突然不能播放了，报错：**

  Uncaught (in promise) DOMException: The element has no supported sources. #2

- 报错原因：

  external sources are restricted by Cross-origin reason.

- 解决方法：

  - https://github.com/wayou/audio-visualizer-with-controls/issues/2
  - https://stackoverflow.com/questions/37674223/domexception-failed-to-load-because-no-supported-source-was-found

- 后续：不报错了，但也不能播放

