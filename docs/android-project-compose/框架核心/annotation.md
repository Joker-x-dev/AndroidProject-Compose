# 注解

## 介绍

`core/annotation` 用于存放项目级通用注解。只有语义稳定、会被多个 Feature 或 Core 能力复用，并且不依赖具体业务页面的注解，才应该放入该目录；只服务单个业务域的注解应留在对应 Feature。

当前项目只在该目录实现了 Compose Preview 相关注解，用于复用设备、深色模式和背景参数。以后出现其他项目级通用注解时，也继续放在 `core/annotation`，不需要把该目录限制为 Preview 专用目录。

## 当前已有的预览注解

当前目录包含两个文件：

```text
core/annotation/
├── ComponentPreview.kt           # 组件级浅色、深色预览
└── ScreenPreview.kt              # 页面级手机、平板、折叠屏预览
```

这两个文件中的注解只决定“使用什么主题模式和设备尺寸生成预览”，不提供页面数据。固定样例对象和 `PreviewParameterProvider` 的放置规则由[数据层](./data.md#预览数据)说明。

## 组件预览注解

组件预览不显示设备外框，适合按钮、列表项、卡片等局部 UI。

| 注解 | 生成的预览 | 使用场景 |
| --- | --- | --- |
| `@ComponentPreview` | 浅色 | 只检查组件默认样式 |
| `@ComponentPreviewDark` | 深色 | 只检查组件深色样式 |
| `@ComponentPreviewLightAndDark` | 浅色 + 深色 | 同时比较两套主题 |

```kotlin
package com.joker.kit.feature.demo.component

import androidx.compose.runtime.Composable
import com.joker.kit.core.annotation.ComponentPreviewLightAndDark
import com.joker.kit.core.designsystem.theme.AppTheme

/**
 * 商品卡片预览
 */
@ComponentPreviewLightAndDark
@Composable
private fun GoodsCardPreview() {
    AppTheme {
        GoodsCard(
            title = "小米手机 14",
            price = 3999,
        )
    }
}
```

`@ComponentPreviewLightAndDark` 会生成两份 Preview，但 Composable 仍只写一次。组件内部应从 `AppColors`、`AppTextStyles` 或 `MiuixTheme` 读取主题值，不能把浅色颜色写死。

## 页面预览注解

页面预览指定设备尺寸，适合检查完整 Screen 的布局、系统栏留白和宽度变化。

| 注解 | 设备 | 主题 |
| --- | --- | --- |
| `@ScreenPreview` | Phone | 浅色 |
| `@ScreenPreviewDark` | Phone | 深色 |
| `@ScreenPreviewPhoneAndTablet` | Phone + Tablet | 浅色 |
| `@ScreenPreviewPhoneAndTabletDark` | Phone + Tablet | 深色 |
| `@ScreenPreviewMultiDevice` | Phone + Foldable + Tablet | 浅色 |
| `@ScreenPreviewMultiDeviceDark` | Phone + Foldable + Tablet | 深色 |

选择规则：

- 普通页面至少使用 `@ScreenPreview` 和 `@ScreenPreviewDark` 检查两套主题。
- 明确支持手机与平板的页面，使用 `PhoneAndTablet` 组合。
- 页面会在折叠屏运行或使用自适应分栏时，使用 `MultiDevice` 组合。
- 需要精确的自定义 `widthDp`、`heightDp` 时，可以继续直接使用原生 `@Preview`，详见[屏幕适配](./screen-adaptation.md#用-preview-验证断点)。

::: warning 深色预览需要两部分同时生效
`@ScreenPreviewDark` 设置的是预览环境的夜间模式；`AppTheme(darkTheme = true)` 决定页面实际使用深色主题。示例中应同时配置二者，避免预览标签显示为 Dark，页面却仍渲染浅色配色。
:::

## 结合 PreviewParameter 预览多组数据

`@ScreenPreview` 可以和 `@PreviewParameter` 同时使用。下面的页面只声明一个预览函数，`GoodsPreviewParameterProvider` 会依次传入正常列表、单条数据和空列表：

```kotlin
package com.joker.kit.feature.demo.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.joker.kit.core.annotation.ScreenPreview
import com.joker.kit.core.base.state.BaseNetWorkListUiState
import com.joker.kit.core.data.preview.GoodsPreviewParameterProvider
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.model.entity.Goods

/**
 * 网络列表页面预览
 *
 * @param goods 商品预览数据
 */
@ScreenPreview
@Composable
private fun NetworkListDemoScreenPreview(
    @PreviewParameter(GoodsPreviewParameterProvider::class)
    goods: List<Goods>,
) {
    AppTheme {
        NetworkListDemoScreen(
            // 空列表映射为空状态，其余数据映射为成功状态。
            uiState = if (goods.isEmpty()) {
                BaseNetWorkListUiState.Empty
            } else {
                BaseNetWorkListUiState.Success
            },
            list = goods,
            hasMoreData = goods.isNotEmpty(),
        )
    }
}
```

当前脚手架已在 `core/data/preview/GoodsPreviewParameterProvider.kt` 提供正常列表、单条数据和空列表。具体数据与放置边界见[数据层的预览数据](./data.md#预览数据)。

深色页面使用独立预览函数，让主题配置保持明确：

```kotlin
/**
 * 网络列表页面深色预览
 *
 * @param goods 商品预览数据
 */
@ScreenPreviewDark
@Composable
private fun NetworkListDemoScreenPreviewDark(
    @PreviewParameter(GoodsPreviewParameterProvider::class)
    goods: List<Goods>,
) {
    AppTheme(darkTheme = true) {
        NetworkListDemoScreen(
            uiState = if (goods.isEmpty()) {
                BaseNetWorkListUiState.Empty
            } else {
                BaseNetWorkListUiState.Success
            },
            list = goods,
            hasMoreData = goods.isNotEmpty(),
        )
    }
}
```

## Preview 应该调用 Screen

预览应直接调用 Screen 或 Content，而不是 Route：

```text
推荐：Preview → AppTheme → Screen → Content
避免：Preview → Route → hiltViewModel() → Repository
```

Route 依赖 Hilt、导航宿主和运行时状态，在设计期通常无法创建。Screen 通过参数接收数据和回调，因此可以独立验证 UI。

## 新增项目级注解

新增注解前先确认它属于项目级公共约定：

1. 注解会被多个 Feature 或 Core 目录复用，而不是只服务一个页面。
2. 注解名称表达稳定语义，不携带具体业务模块名称。
3. 注解声明不引用 Feature 的模型、Composable 或 ViewModel。
4. 根据实际用途评估 `@Target` 和 `@Retention`；需要限制使用位置或保留周期时显式声明。
5. 在至少一个真实调用位置验证注解行为，并在本页补充用途和限制。

只在单个 Feature 使用的业务标记注解应放在该 Feature 内部，不要因为“以后可能复用”提前上移到 Core。

### 新增 Preview 组合

只有同一组 `@Preview` 参数在多个页面重复出现时，才应在 `core/annotation` 增加注解：

1. 先确认现有 9 个注解不能表达目标组合。
2. 使用业务无关的名称描述设备或主题组合。
3. 只封装 Preview 元数据，不在注解中引入 Feature 类型。
4. 在浅色和深色命名上保持成对关系。
5. 用一个真实 Screen 验证生成数量、设备尺寸和主题模式。

一次性的特殊尺寸直接使用原生 `@Preview`，不必把每个宽高组合都沉淀为全局注解。

## 相关链接

- [数据层](./data.md#预览数据)：创建和放置 `PreviewParameterProvider`。
- [屏幕适配](./screen-adaptation.md)：根据窗口宽度切换结构，并用 Preview 验证断点。
- [View 规范](../业务功能/view.md#预览规范)：业务页面如何消费统一预览能力。
- [Compose Preview 官方文档](https://developer.android.com/develop/ui/compose/tooling/previews)
- [Preview 参数数据](https://developer.android.com/develop/ui/compose/tooling/previews#preview-data)
