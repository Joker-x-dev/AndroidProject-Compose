# 页面模板

## 介绍

为方便快速创建标准化的 Feature 页面，可使用以下模板。将代码复制到 Android Studio 的 **File and Code Templates** 中，即可通过模板快速生成 Screen、ViewModel、Routes、Graph 和 Navigator 文件。

页面模板遵循项目统一结构：Route 收集状态并转发事件，Screen 负责页面框架与缺省状态，Content 负责成功后的业务布局，ViewModel 处理页面状态与业务动作，模块级 Navigator 统一封装跳转。

::: tip 模板适用范围
Screen 与 ViewModel 模板用于创建每个页面。Routes、Graph 和 Navigator 是模块级文件，只在创建新功能域时生成一次；向已有功能域增加页面时，应在现有文件中追加 Route、`entry` 和跳转方法，不能重新生成文件覆盖原有内容。
:::

## 配置 Android Studio

在 macOS 中打开 `Android Studio > Settings > Editor > File and Code Templates > Files`，然后按以下步骤保存模板：

1. 点击 `+` 创建文件模板。
2. 填写下表中的模板名称，扩展名统一填写 `kt`。
3. 将对应章节的 Kotlin 代码粘贴到模板编辑区。
4. 点击 `Apply` 保存。
5. 在目标目录执行 `New > <模板名称>`，填写文件名和模板变量后生成文件。

建议保存以下模板：

| 模板名称 | 生成位置 | 使用时机 |
| --- | --- | --- |
| `Compose Feature Screen` | `feature/<domain>/view/` | 每个页面创建一次 |
| `Compose Feature ViewModel` | `feature/<domain>/viewmodel/` | 每个页面创建一次 |
| `Navigation Routes` | `core/navigation/<domain>/` | 新建功能域时创建 |
| `Feature Graph` | `feature/<domain>/navigation/` | 新建功能域时创建 |
| `Module Navigator` | `core/navigation/<domain>/` | 新建功能域时创建 |

Android Studio 会在创建文件时要求填写未定义的自定义变量。五个模板按需使用以下变量：

| 变量 | 示例 | 说明 |
| --- | --- | --- |
| `FEATURE_NAME` | `demo` | 功能域包名，使用小写形式 |
| `FEATURE_CLASS_NAME` | `Demo` | 功能域类型名前缀，使用 PascalCase |
| `PAGE_NAME` | `GoodsDetail` | 页面类型名前缀，使用 PascalCase |
| `CN_NAME` | `商品详情` | 页面中文名称，用于注释和初始标题 |

## Screen 模板

- 模板名称：`Compose Feature Screen`
- 生成文件：`${PAGE_NAME}Screen.kt`

该模板一次生成 Route、Screen、Content 和浅色/深色预览。创建文件时输入 `GoodsDetailScreen` 作为文件名，并将 `PAGE_NAME` 填写为 `GoodsDetail`。

```kotlin
package com.joker.kit.feature.${FEATURE_NAME}.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.annotation.ScreenPreview
import com.joker.kit.core.annotation.ScreenPreviewDark
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import top.yukonga.miuix.kmp.basic.Text
import com.joker.kit.feature.${FEATURE_NAME}.viewmodel.${PAGE_NAME}ViewModel

/**
 * ${CN_NAME}页面路由入口
 *
 * @param viewModel 页面 ViewModel
 */
@Composable
internal fun ${PAGE_NAME}Route(
    viewModel: ${PAGE_NAME}ViewModel = hiltViewModel(),
) {
    // 页面暂无业务状态，Route 只负责创建 ViewModel 并进入 Screen。
    ${PAGE_NAME}Screen()
}

/**
 * ${CN_NAME}页面骨架
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ${PAGE_NAME}Screen() {
    AppScaffold(
        titleText = "${CN_NAME}",
        onBackClick = { navigateBack() },
    ) {
        ${PAGE_NAME}Content()
    }
}

/**
 * ${CN_NAME}页面内容
 */
@Composable
private fun ${PAGE_NAME}Content() {
    // Content 只负责成功后的业务布局。
    Text(text = "${CN_NAME}")
}

/**
 * ${CN_NAME}页面浅色预览
 */
@ScreenPreview
@Composable
private fun ${PAGE_NAME}Preview() {
    AppTheme {
        ${PAGE_NAME}Screen()
    }
}

/**
 * ${CN_NAME}页面深色预览
 */
@ScreenPreviewDark
@Composable
private fun ${PAGE_NAME}PreviewDark() {
    AppTheme(darkTheme = true) {
        ${PAGE_NAME}Screen()
    }
}
```

生成网络页面时，Route 负责收集 `uiState` 等状态并添加对应注释；Screen 使用 `BaseNetWorkView` 或 `BaseNetWorkListView` 处理 Loading、Empty 和 Error，成功分支再调用 Content。完整分层规则见 [View 规范](./view.md)。

## ViewModel 模板

- 模板名称：`Compose Feature ViewModel`
- 生成文件：`${PAGE_NAME}ViewModel.kt`

该模板生成无网络页面使用的最小 ViewModel：

```kotlin
package com.joker.kit.feature.${FEATURE_NAME}.viewmodel

import com.joker.kit.core.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ${CN_NAME}页面 ViewModel
 */
@HiltViewModel
class ${PAGE_NAME}ViewModel @Inject constructor() : BaseViewModel()
```

网络页面根据数据形态改为 `BaseNetWorkViewModel<T>` 或 `BaseNetWorkListViewModel<T>`，并注入真实 Repository。带参数页面使用 Navigation 3 的 `NavKey` 与 Hilt Assisted Factory，具体写法见 [ViewModel 规范](./viewmodel.md#路由参数与-assisted-inject)。

## Routes 模板

- 模板名称：`Navigation Routes`
- 生成文件：`${FEATURE_CLASS_NAME}Routes.kt`

Routes 文件集中定义功能域内的类型安全路由：

```kotlin
package com.joker.kit.core.navigation.${FEATURE_NAME}

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * ${CN_NAME}模块路由
 */
object ${FEATURE_CLASS_NAME}Routes {
    /**
     * ${CN_NAME}页面路由
     */
    @Serializable
    data object ${PAGE_NAME} : NavKey
}
```

已有功能域只需把新的 `@Serializable data object` 或 `data class` 追加到现有 Routes 对象。带参数 Route 的字段就是页面输入契约，参数类型必须支持 Kotlin Serialization。

## Graph 模板

- 模板名称：`Feature Graph`
- 生成文件：`${FEATURE_CLASS_NAME}Graph.kt`

Graph 只负责把 Route 映射到页面入口：

```kotlin
package com.joker.kit.feature.${FEATURE_NAME}.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.joker.kit.core.navigation.${FEATURE_NAME}.${FEATURE_CLASS_NAME}Routes
import com.joker.kit.feature.${FEATURE_NAME}.view.${PAGE_NAME}Route

/**
 * ${CN_NAME}模块导航图
 */
fun EntryProviderScope<NavKey>.${FEATURE_NAME}Graph() {
    entry<${FEATURE_CLASS_NAME}Routes.${PAGE_NAME}> {
        ${PAGE_NAME}Route()
    }
}
```

新功能域生成 Graph 后，需要在 `AppNavHost.appEntryProvider` 中调用 `${FEATURE_NAME}Graph()`。已有功能域只在现有 Graph 中追加 `entry`，不重复创建聚合函数。

## Navigator 模板

- 模板名称：`Module Navigator`
- 生成文件：`${FEATURE_CLASS_NAME}Navigator.kt`

Navigator 为业务层提供具有业务语义的跳转方法，具体 Route 只在模块导航目录中构造：

```kotlin
package com.joker.kit.core.navigation.${FEATURE_NAME}

import com.joker.kit.core.navigation.navigate

/**
 * ${CN_NAME}模块导航封装
 */
object ${FEATURE_CLASS_NAME}Navigator {
    /**
     * 打开${CN_NAME}页面
     */
    fun to${PAGE_NAME}() {
        // 将业务动作转换为类型安全路由。
        navigate(${FEATURE_CLASS_NAME}Routes.${PAGE_NAME})
    }
}
```

带参页面由 `to${PAGE_NAME}(...)` 接收明确类型的参数，再构造对应 Route。需要结果回传时，在同一业务域目录增加结果模型和 `NavigationResultKey<T>`；完整链路见 [参数传递与结果回传](../导航/result.md)。

## 使用模板创建页面

以 `demo` 功能域的 `GoodsDetail` 页面为例：

1. 在 `feature/demo/view/` 中选择 `New > Compose Feature Screen`，文件名填写 `GoodsDetailScreen`。
2. 在 `feature/demo/viewmodel/` 中选择 `New > Compose Feature ViewModel`，文件名填写 `GoodsDetailViewModel`。
3. 两次生成时都填写 `FEATURE_NAME = demo`、`FEATURE_CLASS_NAME = Demo`、`PAGE_NAME = GoodsDetail`、`CN_NAME = 商品详情`。
4. 在 `DemoRoutes` 中增加 `GoodsDetail`，在 `DemoGraph` 中增加对应 `entry`，在 `DemoNavigator` 中增加 `toGoodsDetail()`。
5. 从来源页面把点击事件交给 ViewModel，再由 ViewModel 调用 `DemoNavigator.toGoodsDetail()`。

新建功能域时，先使用 Routes、Graph 和 Navigator 模板生成三个模块级文件，再使用 Screen 和 ViewModel 模板创建页面。目录拆分和完整接入顺序见 [创建页面流程](./create-page.md)。

## 生成后的处理

- 把模板中的标题字符串迁移到资源文件。
- 为 Route 中每个状态收集变量添加用途注释。
- 为 Screen 和 Content 的新增参数补齐 KDoc。
- 根据页面类型接入 Repository、网络状态、分页或本地数据。
- 运行 `./gradlew :app:assembleDebug`，确认生成文件、Hilt 和 Navigation 3 注册能够编译。

## 相关链接

- [创建页面流程](./create-page.md)：按 Route、ViewModel、页面、Graph 和 Navigator 顺序接入新页面。
- [功能域目录规范](./structure.md)：确定模板文件应放入的目录。
- [JetBrains File and Code Templates](https://www.jetbrains.com/help/idea/using-file-and-code-templates.html)：了解 Android Studio 所基于 IDE 平台的文件模板机制。
