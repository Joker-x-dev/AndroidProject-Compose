# 屏幕适配

## 介绍

屏幕适配能力把 Material 3 Adaptive 提供的当前窗口等级转换为项目统一的 `XS / SM / MD / LG` 四档断点。Composable 可以通过 `bp()` 为列数、间距、字号和组件尺寸选择不同值，也可以通过 `isXS()`、`isSM()`、`isMD()`、`isLG()` 切换页面结构。

断点判断使用应用的**当前窗口宽度**，不是设备型号或物理屏幕尺寸。手机横屏、平板分屏、折叠屏窗口和桌面可调整窗口都可能在运行期间切换断点，因此适配逻辑应留在 Compose UI，不要把断点结果缓存到 ViewModel。

## 实现位置与依赖

当前实现和示例位于以下文件：

```text
core/ui/adaptive/
├── BreakpointInfo.kt       # 窗口等级转换、断点判断和 bp() 回退
└── BreakpointModel.kt      # BreakpointType 与 BreakpointValueOptions
feature/demo/
├── view/ScreenAdaptDemoScreen.kt
├── viewmodel/ScreenAdaptDemoViewModel.kt
└── navigation/DemoGraph.kt
core/navigation/demo/
├── DemoRoutes.kt
└── DemoNavigator.kt
```

`app` 通过版本目录引入 `androidx.compose.material3.adaptive:adaptive`，当前版本为 `1.3.0`。业务页面不需要重复添加依赖。

## 断点规则

| 断点 | 当前判断方式 | 典型窗口 |
| --- | --- | --- |
| `XS` | 宽度小于 320dp | 极窄分屏、小型浮动窗口 |
| `SM` | 宽度至少 320dp，但未达到官方 Medium 下限 | 普通手机竖屏 |
| `MD` | 达到官方 Medium 下限，但未达到 Expanded 下限 | 大屏手机横屏、平板窄窗 |
| `LG` | 达到官方 Expanded 下限 | 平板宽窗、桌面窗口 |

项目只把 320dp 作为自定义超小断点。`MD` 与 `LG` 不在项目中重复写死数值，而是调用 `WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND` 和 `WIDTH_DP_EXPANDED_LOWER_BOUND`，随 Material 3 Adaptive 的窗口等级定义保持一致。当前官方宽度等级的 Medium、Expanded 下限分别为 600dp、840dp。

高度和设备方向不会单独产生断点。同一个宽度下需要处理高度不足时，应再结合滚动容器、`WindowInsets` 或页面自身的高度约束，不能把宽度断点当成完整的设备分类。

## 核心 API

| API | 返回值 | 适用场景 |
| --- | --- | --- |
| `bp(xs, sm, md, lg, defaultValue)` | 泛型 `T` | 直接为四档窗口提供列数、尺寸或枚举值 |
| `bp(options, defaultValue)` | 泛型 `T` | 需要复用 `BreakpointValueOptions<T>` 配置时使用 |
| `isXS()` | `Boolean` | 只针对小于 320dp 的极窄窗口分支 |
| `isSM()` | `Boolean` | 手机宽度分支 |
| `isMD()` | `Boolean` | 中等窗口分支 |
| `isLG()` | `Boolean` | 宽窗口分支 |

这些 API 都是 `@Composable` 函数，因为它们读取 `currentWindowAdaptiveInfo()` 和 `currentWindowDpSize()`。窗口信息变化后，Compose 会重新计算调用位置使用的值。

## `bp()` 如何选择值

`bp()` 不要求每档都传值。当前断点没有对应值时，按以下顺序查找：

1. 使用当前断点值。
2. 从当前断点向较小断点查找，优先使用最近值。
3. 没有较小值时，再向较大断点查找。
4. 仍未找到时使用 `defaultValue`。
5. 没有默认值时，使用配置中从 `XS` 到 `LG` 的第一个非空值。
6. 所有断点和 `defaultValue` 都为空时抛出 `IllegalArgumentException`。

例如只提供 `sm = 2` 和 `lg = 4` 时，`MD` 会向较小断点回退并得到 2；`XS` 没有较小值，会向较大断点查找并得到 2。

::: warning 至少提供一个可用值
`bp<Int>()` 的全部断点和 `defaultValue` 都为 `null` 时，运行到该 Composable 会抛出“BreakpointValueOptions 不能为空，请至少提供一个断点值”。页面必须提供至少一个断点值或明确默认值。
:::

## 基础示例

文件位置：`feature/<domain>/view/<PageName>Screen.kt`。

下面的 Content 根据窗口宽度调整网格列数。适配只影响 UI 排版，数据列表仍由参数传入，不需要 ViewModel 感知断点。

```kotlin
package com.joker.kit.feature.catalog.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.joker.kit.core.ui.adaptive.bp

/**
 * 根据当前窗口宽度展示商品网格。
 *
 * @param goodsNames 商品名称列表
 */
@Composable
internal fun AdaptiveGoodsContent(
    goodsNames: List<String>,
) {
    // 当前窗口断点对应的网格列数
    val columns = bp(xs = 1, sm = 2, md = 3, lg = 4)
    // 当前窗口断点对应的商品间距
    val itemSpacing = bp(xs = 8.dp, sm = 8.dp, md = 12.dp, lg = 16.dp)

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        horizontalArrangement = Arrangement.spacedBy(itemSpacing),
        verticalArrangement = Arrangement.spacedBy(itemSpacing),
    ) {
        items(goodsNames) { goodsName ->
            Card(modifier = Modifier.fillMaxWidth()) {
                // 商品卡片只接收业务数据，不读取窗口信息。
                Text(
                    text = goodsName,
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
    }
}
```

预期结果：小窗口显示一到两列，中等窗口显示三列，宽窗口显示四列；调整分屏比例或窗口宽度时，列表会重新排版。

## 切换页面结构

列数、间距和字号变化使用 `bp()`。导航栏位置、主从栏等结构性变化可以使用断点判断函数，但应让每个分支继续接收相同状态和事件。

文件位置：`feature/<domain>/view/<PageName>Screen.kt`。Route 仍按 [View 规范](../业务功能/view.md)收集状态并绑定 ViewModel 事件；以下片段展示 Screen 与 Content 如何分工。Screen 保留页面骨架，Content 再根据窗口宽度选择单栏或主从栏。`CatalogListPane` 和 `CatalogListDetailPane` 代表同一 Feature 内已有的内容组件；它们接收相同状态和事件，不读取 ViewModel。

```kotlin
import androidx.compose.runtime.Composable
import com.joker.kit.core.ui.adaptive.isLG
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.scaffold.AppScaffold

/**
 * 根据窗口宽度选择单栏或主从栏结构。
 *
 * @param selectedId 当前选中项 ID
 * @param onSelect 选中项回调
 */
@Composable
internal fun CatalogScreen(
    selectedId: Long?,
    onSelect: (Long) -> Unit,
) {
    AppScaffold(
        titleText = "商品目录",
        onBackClick = { navigateBack() },
    ) {
        CatalogContent(
            selectedId = selectedId,
            onSelect = onSelect,
        )
    }
}

/**
 * 根据窗口宽度显示目录业务内容。
 *
 * @param selectedId 当前选中项 ID
 * @param onSelect 选中项回调
 */
@Composable
private fun CatalogContent(
    selectedId: Long?,
    onSelect: (Long) -> Unit,
) {
    if (isLG()) {
        // 宽窗口同时展示列表与详情，避免不必要的页面跳转。
        CatalogListDetailPane(
            selectedId = selectedId,
            onSelect = onSelect,
        )
    } else {
        CatalogListPane(onSelect = onSelect)
    }
}
```

`isLG()` 只表示当前宽度处于项目的宽窗口档位。需要 Material 3 标准的 List-Detail、Supporting Pane 或导航套件时，应直接评估对应 Adaptive 组件，不要在 `bp()` 上重复实现完整的窗格状态机。

## 当前 Demo

应用内的真实示例路径是：

```text
首页 → 扩展 → 屏幕适配
```

`DemoCardData.expandCards` 调用 `DemoNavigator.toScreenAdaptDemo()`，随后进入 `DemoRoutes.ScreenAdaptDemo`。`demoGraph()` 将该 Route 注册为 `ScreenAdaptDemoRoute()`。

示例页展示以下行为：

- 当前 `XS / SM / MD / LG` 断点编码。
- 网格列数从 2 列调整为 3 列或 4 列。
- 普通列表从一列调整为两列。
- `AppTextStyles` 的文字样式随断点变化。
- 示例方块尺寸从 80dp 调整为 96dp 或 120dp。

`ScreenAdaptDemoViewModel` 当前没有业务状态，Route 获取它只是为了保持 Demo 页面的统一结构。真实的纯展示页面无需为了使用 `bp()` 创建空 ViewModel。

## Preview 与验证

使用不同 `widthDp` 的 Preview 可以快速检查断点结果。Preview 只验证静态布局，分屏拖动和窗口连续变化仍需在运行环境验证。

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.joker.kit.core.designsystem.theme.AppTheme

/** 手机宽度下的页面预览。 */
@Preview(name = "Phone", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun CatalogPhonePreview() {
    AppTheme {
        CatalogScreen(
            selectedId = null,
            onSelect = {},
        )
    }
}

/** 平板宽度下的页面预览。 */
@Preview(name = "Tablet", widthDp = 840, heightDp = 900, showBackground = true)
@Composable
private fun CatalogTabletPreview() {
    AppTheme {
        CatalogScreen(
            selectedId = 1L,
            onSelect = {},
        )
    }
}
```

完整验证至少覆盖：

- 319dp 与 320dp，确认 `XS → SM` 边界。
- 官方 Medium 下限两侧，确认 `SM → MD`。
- 官方 Expanded 下限两侧，确认 `MD → LG`。
- 手机横竖屏、平板分屏和窗口拖动。
- 长文本、系统字体放大、深色主题和键盘弹出。
- 每个断点下的点击区域、滚动位置和内容可达性。

## 常见问题

### 在 ViewModel 中无法调用 `bp()`

`bp()` 是 Composable API，只能在 Compose UI 中读取当前窗口。ViewModel 应保存业务状态；Screen 或 Content 使用 `bp()` 把同一份状态映射成不同布局。

### 横屏后仍然使用手机布局

横屏不必然达到 `MD`。以当前窗口实际宽度为准，先在 Demo 中查看断点编码，再检查页面是否为目标断点提供了值。

### 只传 `lg`，手机为什么也得到宽屏值

当前断点没有较小值时，`bp()` 会向较大断点查找。只传 `lg` 等于让所有断点最终都使用 `lg`。需要手机和宽屏差异时，至少同时提供 `sm` 与 `lg`，或设置明确的 `defaultValue`。

### 分屏后内容被截断

断点只负责选择值，不会自动为页面添加滚动、安全区或最小尺寸。检查 `LazyColumn`、`LazyVerticalGrid`、`WindowInsets`、长文本换行以及固定高度组件。

## 相关链接

- [UI 组件](./ui.md)：查看公共 UI 和断点入口。
- [View 规范](../业务功能/view.md)：确认响应式逻辑与页面职责。
- [Material 3 Adaptive 布局](https://developer.android.com/develop/ui/compose/layouts/adaptive)
- [Android 窗口尺寸等级](https://developer.android.com/develop/ui/compose/layouts/adaptive/use-window-size-classes)
- [支持不同显示尺寸](https://developer.android.com/develop/ui/compose/layouts/adaptive/support-different-display-sizes)
