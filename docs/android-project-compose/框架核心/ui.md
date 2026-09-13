# UI 组件

## 介绍

`core/ui` 是页面级公共组件库。它建立在[设计系统](./designsystem.md)和[主题系统](./theme.md)之上，提供 Scaffold、AppBar、文本、列表项、空态、加载态、网络状态、刷新容器、骨架屏和响应式断点。

与 `core/designsystem` 的区别是：Design System 只统一颜色、尺寸和基础布局；`core/ui` 可以理解“页面标题”“网络错误”“加载中”“空数据”等 UI 语义，但仍然不直接访问 ViewModel、Repository 或数据源。

## 组件清单

| 目录 | 主要组件 | 解决的问题 |
| --- | --- | --- |
| `component/scaffold` | `AppScaffold` | 统一页面背景、AppBar、Snackbar、底部栏、FAB 和安全区内边距 |
| `component/appbar` | `CenterTopAppBar`、`LargeTopAppBar`、`BackButton` | 普通标题、大标题和返回操作 |
| `component/text` | Miuix `Text`、`AppColors`、`AppTextStyles` | 使用 Miuix 官方颜色和文字样式 |
| `component/list` | `AppListItem`、`GroupAppListItem`、`StaticAppListItem` | 设置项、分组列表和只读列表项 |
| `component/network` | `BaseNetWorkView`、`BaseNetWorkListView` | 单对象三态和分页列表四态 |
| `component/refresh` | `RefreshLayout`、`RefreshContent` | 下拉刷新、上拉加载、普通列表和瀑布流 |
| `component/empty` | `Empty`、`EmptyData`、`EmptyError`、`EmptyNetwork` | 无数据、业务错误和网络错误 |
| `component/loading` | `PageLoading`、`MiLoadingWeb`、`MiLoadingMobile` | 页面或局部加载反馈 |
| `component/skeleton` | `Skeleton`、`SkeletonShowcase` | 内容加载前的结构占位 |
| `component/title`、`divider` | `TitleWithLine`、`Divider` | 分组标题和统一分割线 |
| `adaptive` | `bp`、`isXS`、`isSM`、`isMD`、`isLG` | 根据当前窗口宽度选择布局值 |

## 页面骨架：`AppScaffold`

大多数带标题页面从 `AppScaffold` 开始。它内部使用 Material 3 `Scaffold`，可以选择居中标题或大标题，并统一处理页面背景、Snackbar、BottomBar 和 FAB。

| 关键参数 | 默认行为 | 什么时候修改 |
| --- | --- | --- |
| `title` / `titleText` | 标题为空时不显示文字 | 标题来自资源或运行时字符串 |
| `showBackIcon` | `true` | 顶级页面不需要返回按钮时设为 `false` |
| `onBackClick` | 空回调 | 普通页面传入 `{ navigateBack() }` |
| `useLargeTopBar` | `false` | 需要可折叠大标题时设为 `true` |
| `contentShouldConsumePadding` | `false` | 内容需要自己处理 Insets 时设为 `true` |
| `bottomBar` / `floatingActionButton` | 空内容 | 页面需要底部导航或 FAB 时传入 |

文件位置：`feature/<domain>/view/<PageName>Screen.kt`。下面的 Screen 不获取 ViewModel，顶部栏直接执行统一返回操作，并把最终业务布局交给 Content。

```kotlin
import androidx.compose.runtime.Composable
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import top.yukonga.miuix.kmp.basic.Text

/**
 * 设置页面骨架。
 */
@Composable
internal fun SettingsScreen() {
    AppScaffold(
        titleText = "设置",
        onBackClick = { navigateBack() },
    ) {
        // 默认模式下，AppScaffold 的根容器已经消费顶部栏产生的 PaddingValues。
        SettingsContent()
    }
}

/**
 * 设置页面内容。
 */
@Composable
private fun SettingsContent() {
    Text(text = "设置内容")
}
```

`contentShouldConsumePadding = false` 时不要再次对根内容调用 `Modifier.padding(paddingValues)`，否则顶部间距会叠加。只有显式设为 `true` 时，内容才需要消费传入的 `PaddingValues`。

## 文本：`Text`

Miuix `Text` 直接使用 Miuix 官方文字样式和颜色角色：

- `AppColors.onBackground`、`AppColors.onSurfaceSecondary`、`AppColors.onSurfaceContainerVariant`：正文层级。
- `AppColors.primary`、`AppColors.tertiaryContainer`、`AppColors.primaryVariant`、`AppColors.error`：操作与状态颜色。
- 页面文字直接使用 `AppTextStyles` 或 `MiuixTheme.textStyles` 中的官方样式。
- `onClick`：存在回调时文本才可点击。
- `selectable`：需要复制文本时启用选择容器。
- `style`：需要直接传入 `TextStyle` 时覆盖默认尺寸映射。

页面优先选择 `AppColors` 和 `AppTextStyles`，只有组件确实需要特殊样式时才传入 `color`、`fontWeight` 或完整 `style`。

## 网络状态容器

网络页面不要自己写多组 `when`。Core 已将两类页面状态分开：

| 页面类型 | 状态 | 组件 |
| --- | --- | --- |
| 单对象请求 | Loading、Success、Error | `BaseNetWorkView<T>` |
| 分页列表 | Loading、Success、Error、Empty | `BaseNetWorkListView` |

`BaseNetWorkView` 的成功分支会把 `T` 交给 `content`；`BaseNetWorkListView` 的 Success 分支通常继续组合 `RefreshLayout`。具体 ViewModel、Route 和 View 分层示例参见[非分页网络基类](./network-base.md)和[分页列表](./pagination.md)。

::: warning Empty 不是单对象网络状态
`BaseNetWorkUiState` 没有 Empty。按照当前响应模型，详情接口满足业务成功规则但 `data == null` 时，`ResultHandler` 也不会自动转换为空态。需要业务空态时应根据真实响应结构，在模型或页面状态中显式设计，不要假定基类已经处理。
:::

## 刷新和加载更多

`RefreshLayout` 基于 UltraSwipeRefresh，负责把刷新手势、加载更多状态和列表内容组合在一起：

- 默认使用 `LazyColumn`；`isGrid = true` 时使用 `LazyVerticalStaggeredGrid`。
- `isRefreshing` 和 `isLoadingMore` 来自分页 ViewModel，UI 不自行维护第二份状态。
- 列表非空时才启用加载更多，避免空页面继续请求下一页。
- `hasMoreData = false` 时底部进入无更多内容状态。
- 传入 `scrollBehavior` 时与大标题 AppBar 的嵌套滚动联动。

应用启动时，`Application.initRefresh()` 将 header 和 footer 的 `NestedScrollMode` 配置为 `Translate`。如果更换刷新库或动画，需要同步检查 Application 配置、分页基类和本页文档。

## 空态、加载态和骨架屏怎么选

| 场景 | 推荐组件 | 说明 |
| --- | --- | --- |
| 首次进入网络页 | `PageLoading` 或业务 Skeleton | Skeleton 应尽量接近最终内容结构 |
| 请求失败且没有旧数据 | `EmptyNetwork` 或 `EmptyError` | 通过 `onRetryClick` 连接重试 |
| 第一页成功但列表为空 | `EmptyData` | 仅分页四态使用 |
| 局部操作等待 | `MiLoadingWeb` / `MiLoadingMobile` | 不替换整页内容 |
| 复杂页面占位 | `Skeleton` | 骨架只负责绘制，不触发请求 |

空态组件不决定何时显示，它们只接收文案、图片和事件。状态判断仍由 Screen 的状态容器完成。

## 屏幕适配入口

`core/ui/adaptive` 位于 UI 包中，但它解决的是窗口断点和响应式布局，不属于本页的公共组件说明。断点规则、`bp()` 回退顺序、结构切换、Preview 和分屏验证统一参见[屏幕适配](./screen-adaptation.md)，本页不重复定义这些行为。

## 新增公共组件

1. 先确认组件在两个或更多 Feature 中重复；只服务一个业务域时留在 `feature/<domain>/component`。
2. 通过参数接收状态和回调，不在组件内部获取 Hilt ViewModel 或调用 Repository。
3. 颜色、文字、圆角和间距读取 Design System，不创建同义常量。
4. 为公开 Composable 和参数添加 KDoc，为非直观状态分支添加中文行内注释。
5. 提供浅色、深色以及必要的窄屏 Preview，再在至少一个真实 Screen 中验证。

## 相关链接

- [设计系统](./designsystem.md)：基础布局和视觉令牌。
- [主题系统](./theme.md)：颜色、Typography、Shapes 和四张规范图。
- [非分页网络基类](./network-base.md)：单对象三态页面。
- [分页列表](./pagination.md)：列表四态、刷新和加载更多。
- [屏幕适配](./screen-adaptation.md)：窗口断点、`bp()` 回退规则和响应式页面示例。
- [Compose 状态](https://developer.android.com/develop/ui/compose/state)
- [Material 3 Scaffold](https://developer.android.com/develop/ui/compose/components/scaffold)
- [Material 3 Adaptive](https://developer.android.com/develop/ui/compose/layouts/adaptive)
- [UltraSwipeRefresh 官方仓库](https://github.com/jenly1314/UltraSwipeRefresh)
