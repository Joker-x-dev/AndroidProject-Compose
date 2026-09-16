# 数据层

## 介绍

`core/data` 同时组织运行时数据访问和设计期预览数据。`repository/` 向 ViewModel 提供业务数据入口，`preview/` 集中保存 `PreviewParameterProvider` 和稳定的样例对象。

这两类代码都与“数据”有关，但用途不同：Repository 在应用运行时连接网络、Room 或 MMKV，预览数据只在 Android Studio 的设计期预览中提供输入，不能访问真实数据源。

::: info 当前实现
当前 AndroidProject-Compose 同时包含 `core/data/repository/` 和 `core/data/preview/`。`NetworkListDemoScreen` 已通过 `GoodsPreviewParameterProvider` 消费多组商品预览数据。
:::

## 目录结构

当前数据层包含运行时 Repository 与设计期预览数据：

```text
core/data/
├── repository/                         # 当前已有：运行时数据访问入口
│   ├── GoodsRepository.kt
│   ├── UserInfoRepository.kt
│   ├── AuthRepository.kt
│   ├── DemoRepository.kt
│   ├── AuthStoreRepository.kt
│   └── UserInfoStoreRepository.kt
└── preview/                            # 当前已有：跨页面复用的设计期预览数据
    └── GoodsPreviewParameterProvider.kt
```

## 数据内容分类

| 内容 | 使用阶段 | 负责什么 | 不负责什么 |
| --- | --- | --- | --- |
| `repository/` | 应用运行时 | 聚合 DataSource，对 ViewModel 暴露业务数据入口 | 不绘制 UI，不处理导航 |
| `preview/` | Compose 设计期 | 提供稳定、可重复的正常、空数据等预览输入 | 不请求网络，不读取 Room、MMKV 或 Hilt |

只被一个页面使用的预览数据可以留在页面同文件或相邻文件；同一 Feature 的多个页面复用时放入 `feature/<domain>/data/`；面向 Core 共享模型或需要跨 Feature 复用时，放入 `core/data/preview/`。不要为了追求目录完整而提前创建空包。

## Repository 数据链路

业务数据按以下边界从 ViewModel 流向具体存储或网络实现。

```text
ViewModel
  → Repository
  → NetworkDataSource / DatabaseDataSource / StoreDataSource
  → Retrofit Service / Room DAO / MMKVUtils
```

Repository 不是把 DataSource 方法换个名字。它决定业务层看到的返回类型、线程边界和多个数据源的组合方式。当前示例大多保持单一数据源直通；缓存优先、网络回写等策略需要在具体业务中明确实现，不能从示例推断为默认能力。

| Repository 类型 | 示例 | 对外形式 |
| --- | --- | --- |
| 网络仓库 | `GoodsRepository` | `Flow<NetworkResponse<T>>` |
| 数据库仓库 | `DemoRepository` | `Flow<List<DemoEntity>>` 与挂起 CRUD |
| 本地认证仓库 | `AuthStoreRepository` | 挂起读写、登录判断和 Token 刷新判断 |
| 本地用户仓库 | `UserInfoStoreRepository` | 挂起用户信息读写与字段读取 |

## 网络仓库示例

文件位置：`core/data/repository/GoodsRepository.kt`

```kotlin
package com.joker.kit.core.data.repository

import com.joker.kit.core.model.entity.Goods
import com.joker.kit.core.model.network.NetworkResponse
import com.joker.kit.core.network.datasource.goods.GoodsNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * 商品仓库
 *
 * @param goodsNetworkDataSource 商品网络数据源
 */
class GoodsRepository @Inject constructor(
    private val goodsNetworkDataSource: GoodsNetworkDataSource,
) {

    /**
     * 请求商品详情
     *
     * @param id 商品 ID
     * @return 商品详情响应流
     */
    fun getGoodsInfo(id: String): Flow<NetworkResponse<Goods>> = flow {
        // Repository 负责把挂起数据源转换为 Flow。
        emit(goodsNetworkDataSource.getGoodsInfo(id))
    }.flowOn(Dispatchers.IO)
}
```

分页方法 `getGoodsPage(params)` 的真实返回类型为 `Flow<NetworkResponse<NetworkPageData<Goods>>>`，实现方式相同，详见[网络请求](./network.md)。

## 数据库仓库示例

文件位置：`core/data/repository/DemoRepository.kt`

```kotlin
package com.joker.kit.core.data.repository

import com.joker.kit.core.database.datasource.demo.DemoDataSource
import com.joker.kit.core.database.entity.DemoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Demo 数据库仓库
 *
 * @param demoDataSource Demo 数据源
 */
@Singleton
class DemoRepository @Inject constructor(
    private val demoDataSource: DemoDataSource,
) {

    /**
     * 监听全部 Demo 记录
     *
     * @return 按更新时间倒序的记录流
     */
    fun observeItems(): Flow<List<DemoEntity>> {
        return demoDataSource.observeItems().flowOn(Dispatchers.IO)
    }

    /**
     * 新增一条记录
     *
     * @param title 标题
     * @param description 描述
     * @return 插入后的行 ID
     */
    suspend fun createItem(title: String, description: String = ""): Long {
        return demoDataSource.createItem(title, description)
    }
}
```

`DemoRepository` 还提供 `updateItem`、`deleteItem`、`clearAll` 和 `getItem`。这些方法保持为挂起函数，不把 DAO 暴露给 UI。

## 本地仓库示例

`AuthStoreRepository` 和 `UserInfoStoreRepository` 代理对应 Store DataSource，并在认证场景增加 `shouldRefreshToken()`。Store DataSource 当前使用 MMKV 保存 JSON 字符串，而不是 Jetpack DataStore。需要缓存用户资料时，ViewModel 注入仓库即可：

```kotlin
package com.joker.kit.core.data.repository

import com.joker.kit.core.datastore.datasource.userinfo.UserInfoStoreDataSource
import com.joker.kit.core.model.entity.User
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 用户信息本地存储仓库
 *
 * @param userInfoStoreDataSource 用户信息本地数据源
 */
@Singleton
class UserInfoStoreRepository @Inject constructor(
    private val userInfoStoreDataSource: UserInfoStoreDataSource,
) {

    /**
     * 保存用户信息
     *
     * @param user 用户信息
     */
    suspend fun saveUserInfo(user: User) {
        userInfoStoreDataSource.saveUserInfo(user)
    }

    /**
     * 读取用户信息
     *
     * @return 用户信息，不存在或解析失败时返回 null
     */
    suspend fun getUserInfo(): User? {
        return userInfoStoreDataSource.getUserInfo()
    }
}
```

Repository 只暴露“保存用户资料”和“读取用户资料”等业务动作，不暴露具体存储库的 key 与序列化细节。ViewModel 如何把读取结果写入页面状态，由[本地存储](./datastore.md)单独说明。

## 预览数据

当一个页面有正常列表、单条数据和空列表等多种输入时，不要为每种输入复制一份 Preview。`PreviewParameterProvider<T>` 可以把多组稳定数据依次传给同一个预览函数，Android Studio 会为序列中的每个值生成一份预览。

下面是当前源码中的实现，使用真实的 `Goods` 模型，并为列表页提供正常列表、单条数据和空列表三组输入。

文件位置：`core/data/preview/GoodsPreviewParameterProvider.kt`

```kotlin
package com.joker.kit.core.data.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.joker.kit.core.model.entity.Goods

/**
 * 商品列表预览数据
 */
val previewGoods = listOf(
    Goods(
        id = 1,
        title = "小米手机 14",
        subTitle = "直屏旗舰",
        price = 3999,
        sold = 5000,
    ),
    Goods(
        id = 2,
        title = "Apple AirPods",
        subTitle = "二代",
        price = 1299,
        sold = 8000,
    ),
    Goods(
        id = 3,
        title = "Switch OLED",
        subTitle = "游戏机",
        price = 2599,
        sold = 3000,
    ),
)

/**
 * 商品列表预览参数提供者
 */
class GoodsPreviewParameterProvider : PreviewParameterProvider<List<Goods>> {

    /** 商品列表的正常、单条和空数据预览序列 */
    override val values: Sequence<List<Goods>>
        get() = sequenceOf(
            previewGoods, // 正常列表
            listOf(previewGoods.first()), // 单条数据
            emptyList(), // 空列表
        )
}
```

Provider 的泛型必须与预览函数参数类型一致。本例返回 `List<Goods>`，因此消费它的参数也必须声明为 `List<Goods>`。预览函数如何组合 `@ScreenPreview` 与 `@PreviewParameter`，见[注解](./annotation.md#结合-previewparameter-预览多组数据)。

### 预览数据边界

- 数据必须固定、可重复，不依赖系统时间、随机数或线上接口。
- 至少覆盖页面真正处理的代表性状态，例如正常、单条、空数据；超长文本、缺图或异常字段按页面风险补充。
- Provider 不注入 Repository，不调用网络、Room、MMKV 或 Hilt。
- Provider 只提供输入；Loading、Error 等不携带业务数据的状态可直接在预览函数中构造。
- 不要把预览对象用作生产环境的默认返回值或兜底数据。

## 新增 Repository

1. 确定数据源边界：网络、Room 或 MMKV 分别放在对应 `core` 目录。
2. 为业务语义命名 Repository 方法，不向 Feature 暴露 `getData` 等无上下文名称。
3. 网络读操作通常返回 `Flow<NetworkResponse<T>>`，Room 观察方法返回 Room 的 `Flow`，写操作使用 `suspend`。
4. 在构造函数中注入 DataSource，借助 Hilt 提供依赖。
5. 在 ViewModel 中消费 Repository，并将结果转换为页面状态或 `StateFlow`。

## 新增预览数据

1. 先判断复用范围：单页数据可以紧贴页面，功能域内共享数据放入 `feature/<domain>/data/`，面向 Core 共享模型或跨 Feature 复用的数据放入 `core/data/preview/`。
2. 创建实现 `PreviewParameterProvider<T>` 的类，并让 `values` 返回有限、稳定的 `Sequence<T>`。
3. 用真实页面模型构造数据，但不要填入真实用户信息、Token 或线上隐私数据。
4. 在 Screen Preview 参数上添加 `@PreviewParameter(Provider::class)`。
5. 打开 Android Studio Preview，逐一确认每组输入都能独立渲染。

## 边界规则

- ViewModel 不直接调用 `GoodsService`、`DemoDao` 或 `MMKVUtils`。
- Repository 不创建 Compose 状态、不调用 `AppSnackbar`、不处理导航。
- DataSource 不读取 UI 参数或修改页面状态。
- Preview Provider 不参与应用运行时的数据链路。
- 网络错误统一交给 `ResultHandler` 或上层明确处理，Repository 不吞掉异常。

## 验证运行时链路

以 `GoodsRepository.getGoodsInfo("1")` 为例：

1. ViewModel 创建并收集返回的 `Flow`。
2. Repository 在 `Dispatchers.IO` 上调用 `GoodsNetworkDataSource`。
3. DataSource 调用 Retrofit Service，返回 `NetworkResponse<Goods>`。
4. ViewModel 通过 `asResult()` 和 `ResultHandler` 更新 Loading、Success 或 Error。
5. Screen 只根据状态渲染，不知道 Service、DAO 或 MMKV 的实现。

建议使用成功、业务失败和断网三组输入验证上述分支；Room 与 MMKV 还应验证首次读取为空、保存后可读、清除后恢复为空。

## 验证预览数据

1. 不启动应用，直接打开使用 Provider 的 Preview。
2. 确认正常列表、单条数据和空列表分别生成预览。
3. 断开网络后重新刷新 Preview，页面仍应可以渲染。
4. 修改 Provider 中的一组数据，确认对应预览同步变化且其他组不受影响。

## 相关实现

- [注解](./annotation.md)：项目级通用注解，以及当前已有的组件、页面和多设备预览注解。
- [网络请求](./network.md)：Service 与 NetworkDataSource 的定义和 Hilt 注册。
- [Room 数据库](./database.md)：`DemoRepository` 使用的数据库 DataSource。
- [本地存储](./datastore.md)：Store Repository 的边界与当前 MMKV 实现。
- [全局状态](./state.md)：跨页面共享数据的状态容器，不替代 Repository。

## 官方文档

- [Android 应用架构：数据层](https://developer.android.com/topic/architecture/data-layer)
- [Compose Preview 参数数据](https://developer.android.com/develop/ui/compose/tooling/previews#preview-data)
- [Kotlin Flow](https://kotlinlang.org/docs/flow.html)
- [Hilt 依赖注入](https://developer.android.com/training/dependency-injection/hilt-android)
