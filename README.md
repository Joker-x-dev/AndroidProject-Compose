<div align="center">

<img src="docs/images/graphs/logo.svg" width="120" alt="Logo"/>

# AndroidProject-Compose

_一个 Android 快速开发框架_

<!-- 语言切换按钮 -->
<div align="center">
  <a href="README_EN.md">🌍 English</a>
</div>

[![GitHub](https://img.shields.io/badge/GitHub-AndroidProjectCompose-blue?style=flat-square&logo=github)](https://github.com/Joker-x-dev/AndroidProject-Compose)
[![Gitee](https://img.shields.io/badge/Gitee-AndroidProjectCompose-red?style=flat-square&logo=gitee)](https://gitee.com/Joker-x-dev/AndroidProject-Compose)
[![Demo](https://img.shields.io/badge/Demo-蒲公英下载-green?style=flat-square&logo=android)](https://www.pgyer.com/AndroidProject-Compose)
[![API](https://img.shields.io/badge/Docs-compose.dusksnow.top-orange?style=flat-square&logo=readthedocs)](https://compose.dusksnow.top)
[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/Joker-x-dev/AndroidProject-Compose)

</div>

## 📖 项目简介

AndroidProject-Compose 是一个包含网络、状态、导航、主题、数据存储、数据库等基础能力的 **Jetpack Compose 单模块脚手架**，并提供少量功能示例页面，方便你“拉仓库 → 运行 → 填业务”地快速搭建或学习现代 Compose 应用。

> 如果项目对您有帮助，请给个 Star 支持 ⭐ 这对我来说很重要，能给我带来长期更新维护的动力！

## 📱 项目预览

<img src="docs/images/preview/page.png" alt="page"/>

### 📍 项目地址

- **GitHub 地址**：[https://github.com/Joker-x-dev/AndroidProject-Compose](https://github.com/Joker-x-dev/AndroidProject-Compose)
- **Gitee 地址**：[https://gitee.com/Joker-x-dev/AndroidProject-Compose](https://gitee.com/Joker-x-dev/AndroidProject-Compose)

> 本脚手架源于青商城的实践，仅保留基础能力与功能示例；完整电商业务与全量 UI/动效/完整业务示例请查看青商城：GitHub https://github.com/Joker-x-dev/CoolMallKotlin ｜ Gitee https://gitee.com/Joker-x-dev/CoolMallKotlin

### Demo 下载

**Release 版本（2MB）**：[点击下载体验](https://www.pgyer.com/AndroidProject-Compose)
- **支持系统**：Android 6.0 及以上
- **更新说明**：预览版本会不定时更新，可能不会完全同步最新的代码变更

### 说明文档

- **说明文档**：[在线查看](https://compose.dusksnow.top)
- **说明**：与代码同步的在线文档，包含快速开始、架构说明、示例路由、常见定制点等，便于理解项目与学习。

## 🛠️ 技术栈

### 核心技术

| 类别    | 技术选型                      | 说明                |
|-------|---------------------------|-------------------|
| 编程语言  | Kotlin                    | 100% Kotlin 开发    |
| UI 框架 | Jetpack Compose           | 声明式 UI 框架         |
| 依赖注入  | Hilt                      | 基于 Dagger 的依赖注入框架 |

### 功能模块

| 类别    | 技术选型                  | 说明               |
|-------|-----------------------|------------------|
| 导航    | Navigation Compose    | Compose 导航组件     |
| 网络请求  | Retrofit + OkHttp     | HTTP 客户端         |

### 数据存储

| 类别   | 技术选型 | 说明         |
|------|------|------------|
| 数据库  | Room | SQLite 数据库 |
| 本地存储 | MMKV | 高性能键值存储    |

### 开发工具

| 类别   | 技术选型          | 说明          |
|------|---------------|-------------|
| 日志框架 | Timber        | 日志管理        |
| 网络调试 | Chucker       | 网络请求监控      |
| 内存检测 | LeakCanary    | 内存泄漏检测      |

## 📱 功能模块目录

- **主模块 (main)**
  - 主页面 (main)
  - 基础能力示例 (core-demo)
  - 导航示例 (navigation-demo)
  

- **认证模块 (auth)**
  - 登录页 (login)


- **用户模块 (user)**
  - 用户信息 (info)


- **示例模块 (demo)**
  - 通用网络请求示例 (network-demo)
  - 通用分页列表示例 (network-list-demo)
  - 数据库示例 (database)
  - 本地存储示例 (local-storage)
  - 状态管理示例 (state-management)
  - 网络请求示例 (network-request)
  - 带参跳转示例 (navigation-with-args)
  - 结果回传示例 (navigation-result)

## 项目结构

```
├── core/                 # 核心
│   ├── base/             # 基础抽象
│   ├── data/             # 数据层
│   ├── database/         # 数据库
│   ├── datastore/        # 数据存储
│   ├── designsystem/     # 设计系统
│   ├── model/            # 数据模型
│   ├── network/          # 网络层
│   ├── result/           # 结果处理
│   ├── state/            # 状态管理
│   ├── ui/               # UI 组件
│   └── util/             # 工具类
├── navigation/           # 导航模块
│   ├── routes/           # 路由定义
│   ├── results/          # 路由返回结果
│   └── extension/        # 导航扩展
├── feature/              # 功能模块
│   ├── main/             # 主模块
│   ├── auth/             # 认证模块
│   ├── user/             # 用户模块
│   └── demo/             # 示例模块
└── MainActivityViewModel.kt # 宿主级共享 ViewModel
```

## 👥 加入群聊

欢迎加入开发者交流群，一起分享学习心得，讨论技术问题！

<div align="left">
  <img src="docs/images/group/qq.jpg" width="200" alt="QQ群二维码"/>
  <p>扫码或搜索群号加入 QQ 群</p>
</div>

## 🤝 参与贡献

这是一个开放的学习项目，欢迎所有对 Android 开发感兴趣的开发者参与贡献！

### 🎯 贡献方式

- **代码贡献**: 提交 Pull Request，完善功能实现或修复问题
- **问题反馈**: 通过 Issue 报告 Bug 或提出功能建议
- **文档优化**: 完善项目文档、添加使用说明或开发指南
- **设计支持**: 提供 UI/UX 设计建议或素材资源
- **测试协助**: 参与功能测试，提供使用反馈和改进建议

### 📋 贡献指南

- 提交代码前请确保遵循项目的编码规范
- 新功能开发建议先创建 Issue 讨论可行性
- 欢迎分享学习心得和技术总结
