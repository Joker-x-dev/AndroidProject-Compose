# UI 组件

## 定位

`core/ui` 是页面级公共组件层，建立在 Miuix 和[设计系统](./designsystem.md)之上。它提供页面骨架、返回按钮、滚动列表、网络状态、空态、加载态、刷新和自适应断点。

与 `core/designsystem` 的区别：设计系统提供颜色、文字、尺寸、形状和基础布局；`core/ui` 理解“页面标题”“加载中”“网络错误”“空数据”等页面语义，但不获取 ViewModel、Repository 或数据源。

## 组件清单

| 目录 | 组件 | 职责 |
| --- | --- | --- |
| `component/scaffold` | `AppScaffold`、`MiuixPageLazyColumn` | Miuix 页面骨架、顶部栏、背景、Insets 和统一页面滚动 |
| `component/snackbar` | `AppSnackbarHost` | 消费全局 `AppSnackbar` 消息的 Miuix Snackbar 宿主 |
| `component/dialog` | `AppWindowDialog`、`AppOverlayDialog`、`AppDialogHost`、`AppDialogActionRow` | 窗口级弹窗、浮层弹窗和全局 Dialog 宿主 |
| `component/appbar` | `BackButton` | Miuix 返回按钮封装 |
| `component/network` | `BaseNetWorkView`、`BaseNetWorkListView` | 单对象三态和分页列表状态容器 |
| `component/refresh` | `RefreshLayout`、`RefreshContent` | Miuix 下拉刷新、加载更多和列表内容组合 |
| `component/empty` | `Empty`、`EmptyData`、`EmptyError`、`EmptyNetwork` | 无数据、业务错误和网络错误反馈 |
| `component/loading` | `PageLoading` | 页面级加载反馈 |
| `component/skeleton` | `Skeleton` | 内容加载前的结构占位 |
| `adaptive` | `BreakpointInfo`、`BreakpointModel` | 根据窗口宽度选择响应式布局值 |
| `component/fps` | `FpsMonitor` | 开发和设置页面中的帧率信息展示 |

Button、Text、Card、TopAppBar、Preference、Navigation、Divider 等交互组件直接使用 Miuix 官方实现；项目只在页面语义或布局边界上提供封装。

`AppSnackbarHost` 与 `AppDialogHost` 由 `MainActivity` 在 `AppTheme` 内统一挂载，页面不需要声明弹窗状态，也不需要重复接入宿主。`AppWindowDialog` 基于窗口级 `WindowDialog`，不依赖 `Scaffold`；`AppOverlayDialog` 基于 `OverlayDialog`，必须位于 `AppScaffold` 提供的 `Scaffold` 作用域内。

## 页面骨架：`AppScaffold`

`AppScaffold` 使用 Miuix `Scaffold`，统一处理页面背景、Miuix `TopAppBar`、Snackbar、底部栏、FAB、系统栏 Insets、顶部栏嵌套滚动和可选的顶部栏模糊效果。

| 参数 | 默认值 | 说明 |
| --- | --- | --- |
| `title` / `titleText` | `null` | 资源 ID 或运行时标题；两者同时传入时优先使用 `titleText` |
| `showBackIcon` | `true` | 是否显示 `BackButton` |
| `onBackClick` | 空回调 | 返回按钮回调，普通页面传入 `navigateBack()` |
| `useLargeTopBar` | `true` | 是否启用 Miuix 大标题和默认滚动行为 |
| `blurEnabled` | `true` | 是否允许顶部栏模糊；仍受应用设置和运行时 Shader 能力影响 |
| `contentShouldConsumePadding` | `false` | `false` 时骨架自动消费 `PaddingValues`；`true` 时由内容自行处理 |
| `topBarActions` / `bottomBar` / `floatingActionButton` | 空内容 | 顶部操作、底部导航和浮动操作区域 |

```kotlin
/**
 * 设置页面骨架，统一处理 Miuix 顶部栏、返回操作和页面安全区。
 */
@Composable
internal fun SettingsScreen() {
    AppScaffold(
        titleText = "设置",
        onBackClick = { navigateBack() },
    ) { _ ->
        // contentShouldConsumePadding=false 时，AppScaffold 已自动消费页面内边距。
        SettingsContent()
    }
}

/**
 * 设置页面成功状态的可滚动内容。
 */
@Composable
private fun SettingsContent() {
    MiuixPageLazyColumn {
        item {
            Text(
                text = "设置内容",
                color = AppColors.onBackground,
                style = AppTextStyles.body1,
            )
        }
    }
}
```

默认模式下，`AppScaffold` 已对根内容应用 Scaffold 产生的内边距，内容不要再次对根容器调用 `padding(paddingValues)`。需要自行合并 Insets 时才设置 `contentShouldConsumePadding = true`。

## 页面列表：`MiuixPageLazyColumn`

复杂页面优先使用 `MiuixPageLazyColumn`。它在 `LazyColumn` 之上统一提供：

- `contentPadding` 与 `pagePadding` 的四方向合并。
- `fillMaxSize` 页面滚动视口。
- Miuix 越界回弹和滚动结束触感反馈（由应用设置控制）。
- `LazyListState`、`verticalArrangement` 和 `LazyListScope` 的直接传递。

```kotlin
AppScaffold(titleText = "个人资料") { contentPadding ->
    MiuixPageLazyColumn(
        contentPadding = contentPadding,
        pagePadding = PaddingValues(horizontal = SpaceHorizontalLarge),
        verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium),
    ) {
        item { ProfileHeader() }
        items(items = rows) { row ->
            ProfileRow(row)
        }
    }
}
```

页面间距使用设计系统令牌，不在每个列表页面重复声明裸 `dp`。

## 网络状态容器

网络页面使用已有状态容器，不在 Screen 中重复编写多组 `when`：

| 页面类型 | 状态 | 组件 |
| --- | --- | --- |
| 单对象请求 | Loading、Success、Error | `BaseNetWorkView<T>` |
| 分页列表 | Loading、Success、Error、Empty | `BaseNetWorkListView` |

成功分支只接收可渲染数据和回调；请求、重试、分页和 Repository 调用由 ViewModel 负责。单对象基类不自动推断 `data == null` 为 Empty，详情页需要空态时应在业务状态中显式表达。

## 刷新、加载和空态

`RefreshLayout` 基于 Miuix `PullToRefresh`，负责刷新手势、加载更多和列表内容组合。普通列表由 `RefreshContent` 内部复用 `MiuixPageLazyColumn`，列表状态和顶部栏滚动行为默认由刷新布局管理，调用方只需传入 Scaffold 的 `contentPadding`；瀑布流继续使用 `LazyVerticalStaggeredGrid`。组件接收来自分页 ViewModel 的刷新和加载状态。

| 场景 | 组件 | 说明 |
| --- | --- | --- |
| 首次进入页面 | `PageLoading` 或 Feature Skeleton | Skeleton 尽量接近最终内容结构 |
| 请求失败且无旧数据 | `EmptyNetwork` / `EmptyError` | 通过重试回调重新请求 |
| 第一页成功但无列表项 | `EmptyData` | 仅在分页列表的 Empty 状态展示 |
| 复杂内容占位 | `Skeleton` | 只绘制占位，不触发请求 |

空态和加载组件只负责绘制与回调，不决定业务状态何时切换。

## 设计和实现边界

- 公共组件通过参数接收数据和事件，不在内部获取 Hilt ViewModel、Repository 或导航实例。
- 颜色和文字使用 `AppColors`、`AppTextStyles` 或 `MiuixTheme`；间距使用 `Size.kt`；圆角优先使用 Miuix `Defaults` 或 `Shape.kt`。
- 业务专用卡片、Preference 组合和列表项留在对应 Feature，不为单个页面扩展 `core/ui`。
- 新增公共组件前确认至少有两个 Feature 的稳定复用场景，并补充浅色、深色和必要的窄屏 Preview。
- 自适应断点规则统一参见[屏幕适配](./screen-adaptation.md)，不要在组件内部假设固定设备型号。

## 相关链接

- [设计系统](./designsystem.md)
- [主题系统](./theme.md)
- [非分页网络基类](./network-base.md)
- [分页列表](./pagination.md)
- [屏幕适配](./screen-adaptation.md)
- [Miuix 中文组件文档](https://compose-miuix-ui.github.io/miuix/zh_CN/components/)
