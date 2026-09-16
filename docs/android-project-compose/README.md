# AndroidProject-Compose 框架文档

> 在线文档：[AndroidProject-Compose 在线文档](https://compose.dusksnow.top)
>
> 文档来源：AndroidProject-Compose 文档站 `docs/help/`
>
> 收录范围：简介、Core、Navigation、Feature

本目录保存与当前工程配套的本地框架文档，供开发者在 IDE、GitHub 和 AI 编码工具中直接查阅。章节顺序与在线文档保持一致；示例涉及的类型、路径和调用关系应以当前源码为准。

## 按任务查阅

| 开发任务 | 建议阅读顺序 |
| --- | --- |
| 初次运行项目 | [快速开始](简介/quick-start.md) → [环境要求](简介/environment.md) → [工程组织与模块边界](简介/modularization.md) → [项目架构与职责](简介/architecture.md) |
| 编写主题与通用 UI | [Miuix Compose](框架核心/miuix.md) → [设计系统](框架核心/designsystem.md) → [主题系统](框架核心/theme.md) → [UI 组件](框架核心/ui.md) |
| 编写 Route、Screen 与 Content | [Feature 概览](业务功能/index.md) → [View 规范](业务功能/view.md) → [ViewModel 规范](业务功能/viewmodel.md) |
| 接入接口或分页列表 | [数据模型](框架核心/model.md) → [请求结果处理](框架核心/result.md) → [网络请求](框架核心/network.md) → [数据层](框架核心/data.md) → [非分页网络基类](框架核心/network-base.md)或[分页列表](框架核心/pagination.md) |
| 使用数据库、本地存储或全局状态 | [数据层](框架核心/data.md) → [Room 数据库](框架核心/database.md)、[本地存储](框架核心/datastore.md)或[全局状态](框架核心/state.md) |
| 配置路由、传参与结果回传 | [导航概览](导航/index.md) → [路由配置](导航/router.md) → [导航流程](导航/flow.md) → [登录与路由拦截](导航/guard.md) → [参数传递与结果回传](导航/result.md) |
| 创建完整 Feature 页面 | [目录与命名规范](业务功能/structure.md) → [View 规范](业务功能/view.md) → [ViewModel 规范](业务功能/viewmodel.md) → [创建页面流程](业务功能/create-page.md) → [页面模板](业务功能/templates.md) |
| 配置预览与屏幕适配 | [注解](框架核心/annotation.md) → [数据层的预览数据](框架核心/data.md#预览数据) → [屏幕适配](框架核心/screen-adaptation.md) → [View 预览规范](业务功能/view.md#预览规范) |
| 使用 AI 工具开发 | [AI 辅助开发](简介/ai-coding.md) → 按任务选择对应的 `apc-*` Skill → 阅读专项文档并核对源码和测试 |

## 简介

- [AndroidProject-Compose 是什么](简介/about.md)
- [快速开始](简介/quick-start.md)
- [环境要求](简介/environment.md)
- [工程组织与模块边界](简介/modularization.md)
- [项目架构与职责](简介/architecture.md)
- [AI 辅助开发](简介/ai-coding.md)
- [社区与反馈](简介/community.md)

## Core / 框架核心

- [Core 核心能力](框架核心/index.md)
- [Miuix Compose](框架核心/miuix.md)
- [设计系统](框架核心/designsystem.md)
- [主题系统](框架核心/theme.md)
- [UI 组件](框架核心/ui.md)
- [注解](框架核心/annotation.md)
- [屏幕适配](框架核心/screen-adaptation.md)
- [ViewModel 基类](框架核心/base.md)
- [数据模型](框架核心/model.md)
- [请求结果处理](框架核心/result.md)
- [网络请求](框架核心/network.md)
- [数据层](框架核心/data.md)
- [Room 数据库](框架核心/database.md)
- [本地存储](框架核心/datastore.md)
- [全局状态](框架核心/state.md)
- [工具类](框架核心/util.md)
- [非分页网络基类](框架核心/network-base.md)
- [分页列表](框架核心/pagination.md)

## Navigation / 导航

- [导航概览](导航/index.md)
- [路由配置](导航/router.md)
- [导航流程](导航/flow.md)
- [登录与路由拦截](导航/guard.md)
- [参数传递与结果回传](导航/result.md)

## Feature / 业务功能

- [Feature 模块概览](业务功能/index.md)
- [目录与命名规范](业务功能/structure.md)
- [View 规范](业务功能/view.md)
- [ViewModel 规范](业务功能/viewmodel.md)
- [创建页面流程](业务功能/create-page.md)
- [页面模板](业务功能/templates.md)

## 使用约定

1. 修改代码前按任务范围阅读对应章节，再核对真实源码、调用方和测试。
2. 文档与源码不一致时，以可运行实现为依据，并同步修正文档站与本地副本。
3. 新增、删除或重命名网站章节时，同步更新本目录结构、内部链接和本页索引。
4. 项目级高频约束维护在仓库根目录 `AGENTS.md`，完整概念、流程和示例由本目录统一说明。
