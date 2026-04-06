package com.joker.kit.core.ui.adaptive

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowDpSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass

/**
 * 断点顺序
 */
private val BREAKPOINT_ORDER: List<BreakpointType> = listOf(
    BreakpointType.XS,
    BreakpointType.SM,
    BreakpointType.MD,
    BreakpointType.LG
)

/**
 * 超小断点上限
 */
private val XS_MAX_WIDTH = 320.dp

/**
 * 断点信息
 *
 * @param current 当前断点
 * @author Joker.X
 */
@Immutable
private data class BreakpointInfo(
    val current: BreakpointType = BreakpointType.SM
) {
    /**
     * 是否为超小断点
     *
     * @return 是否超小断点
     * @author Joker.X
     */
    fun isXS(): Boolean = current == BreakpointType.XS

    /**
     * 是否为小断点
     *
     * @return 是否小断点
     * @author Joker.X
     */
    fun isSM(): Boolean = current == BreakpointType.SM

    /**
     * 是否为中断点
     *
     * @return 是否中断点
     * @author Joker.X
     */
    fun isMD(): Boolean = current == BreakpointType.MD

    /**
     * 是否为大断点
     *
     * @return 是否大断点
     * @author Joker.X
     */
    fun isLG(): Boolean = current == BreakpointType.LG

    /**
     * 根据当前断点返回适配值
     *
     * @param options 断点值配置
     * @param defaultValue 默认值
     * @return 适配值
     * @author Joker.X
     */
    fun <T> getValue(options: BreakpointValueOptions<T>, defaultValue: T? = null): T {
        val index = BREAKPOINT_ORDER.indexOf(current)
        val fallbackValue = getFallbackValue(options, defaultValue)
        if (index < 0) {
            return fallbackValue
        }

        val currentValue = getValueByType(options, BREAKPOINT_ORDER[index])
        if (currentValue != null) {
            return currentValue
        }

        for (i in index - 1 downTo 0) {
            val value = getValueByType(options, BREAKPOINT_ORDER[i])
            if (value != null) {
                return value
            }
        }

        for (i in index + 1 until BREAKPOINT_ORDER.size) {
            val value = getValueByType(options, BREAKPOINT_ORDER[i])
            if (value != null) {
                return value
            }
        }

        return fallbackValue
    }

    /**
     * 获取兜底值
     *
     * @param options 断点值配置
     * @param defaultValue 默认值
     * @return 兜底值
     * @author Joker.X
     */
    private fun <T> getFallbackValue(options: BreakpointValueOptions<T>, defaultValue: T?): T {
        if (defaultValue != null) {
            return defaultValue
        }

        BREAKPOINT_ORDER.forEach { type ->
            val value = getValueByType(options, type)
            if (value != null) {
                return value
            }
        }

        throw IllegalArgumentException("BreakpointValueOptions 不能为空，请至少提供一个断点值。")
    }
}

/**
 * 记住当前断点信息
 *
 * @return 当前断点信息
 * @author Joker.X
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun rememberBreakpointInfo(): BreakpointInfo {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val windowDpSize = currentWindowDpSize()
    return remember(adaptiveInfo, windowDpSize) {
        adaptiveInfo.toBreakpointInfo(windowDpSize = windowDpSize)
    }
}

/**
 * 获取当前断点信息
 *
 * @return 当前断点信息
 * @author Joker.X
 */
@Composable
private fun breakpointInfo(): BreakpointInfo = rememberBreakpointInfo()

/**
 * 是否为超小断点
 *
 * @return 是否超小断点
 * @author Joker.X
 */
@Composable
fun isXS(): Boolean = breakpointInfo().isXS()

/**
 * 是否为小断点
 *
 * @return 是否小断点
 * @author Joker.X
 */
@Composable
fun isSM(): Boolean = breakpointInfo().isSM()

/**
 * 是否为中断点
 *
 * @return 是否中断点
 * @author Joker.X
 */
@Composable
fun isMD(): Boolean = breakpointInfo().isMD()

/**
 * 是否为大断点
 *
 * @return 是否大断点
 * @author Joker.X
 */
@Composable
fun isLG(): Boolean = breakpointInfo().isLG()

/**
 * 根据断点返回适配值
 *
 * @param options 断点值配置
 * @param defaultValue 默认值
 * @return 适配值
 * @author Joker.X
 */
@Composable
fun <T> bp(options: BreakpointValueOptions<T>, defaultValue: T? = null): T {
    return breakpointInfo().getValue(options, defaultValue)
}

/**
 * 根据断点返回适配值
 *
 * @param xs 超小断点值
 * @param sm 小断点值
 * @param md 中断点值
 * @param lg 大断点值
 * @param defaultValue 默认值
 * @return 适配值
 * @author Joker.X
 */
@Composable
fun <T> bp(
    xs: T? = null,
    sm: T? = null,
    md: T? = null,
    lg: T? = null,
    defaultValue: T? = null
): T {
    return bp(
        options = BreakpointValueOptions(xs = xs, sm = sm, md = md, lg = lg),
        defaultValue = defaultValue
    )
}

/**
 * 官方窗口信息转换为项目断点信息
 *
 * @param windowDpSize 当前窗口尺寸
 * @return 项目断点信息
 * @author Joker.X
 */
private fun WindowAdaptiveInfo.toBreakpointInfo(
    windowDpSize: DpSize
): BreakpointInfo {
    return BreakpointInfo(
        current = resolveBreakpoint(windowSizeClass = windowSizeClass, windowDpSize = windowDpSize)
    )
}

/**
 * 根据官方窗口等级解析断点
 *
 * @param windowSizeClass 官方窗口尺寸等级
 * @param windowDpSize 当前窗口尺寸
 * @return 当前断点
 * @author Joker.X
 */
private fun resolveBreakpoint(
    windowSizeClass: WindowSizeClass,
    windowDpSize: DpSize
): BreakpointType {
    if (windowDpSize.width < XS_MAX_WIDTH) {
        return BreakpointType.XS
    }

    return when {
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> BreakpointType.LG
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> BreakpointType.MD
        else -> BreakpointType.SM
    }
}

/**
 * 根据断点名称获取值
 *
 * @param options 断点值配置
 * @param type 断点类型
 * @return 断点值
 * @author Joker.X
 */
private fun <T> getValueByType(
    options: BreakpointValueOptions<T>,
    type: BreakpointType
): T? {
    return when (type) {
        BreakpointType.XS -> options.xs
        BreakpointType.SM -> options.sm
        BreakpointType.MD -> options.md
        BreakpointType.LG -> options.lg
    }
}
