package com.joker.kit.core.navigation

/**
 * 导航返回结果的类型安全 Key。
 *
 * 将一个唯一的字符串 key 与返回结果的数据类型 [T] 在编译期绑定
 *
 * 使用示例（定义 Key）：
 * ```kotlin
 * object SelectAddressResultKey : NavigationResultKey<Address>
 * ```
 *
 * 使用示例（发送结果）：
 * ```kotlin
 * popBackStackWithResult(SelectAddressResultKey, address)
 * ```
 *
 * 使用示例（接收结果）：
 * ```kotlin
 * navigator.resultEvents(SelectAddressResultKey).collect { address ->
 *     viewModel.onAddressSelected(address)
 * }
 * ```
 *
 * @param T 返回结果的数据类型
 * @author Joker.X
 */
interface NavigationResultKey<T> {
    /**
     * 底层用于结果分发的字符串 key。
     *
     * 默认实现使用 Key 对象自身的完全限定类名，保证全局唯一且无需手写字符串。
     */
    val key: String
        get() = this::class.java.name

    /**
     * 将结果对象序列化为可分发的底层类型。
     *
     * 默认实现为透传（即直接存储原始对象），适用于 Boolean、Int、String 等基础类型。
     * 复杂类型可以在具体的 Key 中重写此方法，例如序列化为 JSON 字符串。
     *
     * @param value 待序列化的结果对象
     * @return 可在导航结果流中传递的底层值
     */
    fun serialize(value: T): Any = value as Any

    /**
     * 从结果分发流中还原结果对象。
     *
     * 默认实现为简单强转，复杂类型的 Key 需要重写以配合 [serialize]。
     *
     * @param raw 结果流中的底层值
     * @return 还原后的结果对象
     */
    fun deserialize(raw: Any): T = raw as T
}
