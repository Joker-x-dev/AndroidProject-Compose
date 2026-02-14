package com.joker.kit.core.navigation.demo

import com.joker.kit.core.navigation.NavigationResultKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Demo 结果回传 Key
 *
 * @author Joker.X
 */
object DemoResultKey : NavigationResultKey<DemoResult> {
    /**
     * 序列化结果
     *
     * @param value 待序列化对象
     * @return 序列化字符串
     * @author Joker.X
     */
    override fun serialize(value: DemoResult): Any = Json.encodeToString(value)

    /**
     * 反序列化结果
     *
     * @param raw 原始值
     * @return 反序列化后的结果
     * @author Joker.X
     */
    override fun deserialize(raw: Any): DemoResult = Json.decodeFromString(raw as String)
}

/**
 * Demo 结果数据
 *
 * @param id 结果 ID
 * @param message 结果信息
 * @author Joker.X
 */
@Serializable
data class DemoResult(
    val id: Long,
    val message: String,
)
