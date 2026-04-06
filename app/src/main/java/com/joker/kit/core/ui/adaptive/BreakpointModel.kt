package com.joker.kit.core.ui.adaptive

/**
 * 断点类型
 *
 * @param code 断点编码
 * @author Joker.X
 */
enum class BreakpointType(
    val code: String
) {
    XS("xs"),
    SM("sm"),
    MD("md"),
    LG("lg")
}

/**
 * 断点值配置
 *
 * @param xs 超小断点值
 * @param sm 小断点值
 * @param md 中断点值
 * @param lg 大断点值
 * @author Joker.X
 */
data class BreakpointValueOptions<T>(
    val xs: T? = null,
    val sm: T? = null,
    val md: T? = null,
    val lg: T? = null
)
