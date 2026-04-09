package com.joker.kit.core.util.time

/**
 * 时间工具类
 *
 * @author Joker.X
 */
object TimeUtils {
    /**
     * 计算距离最短展示时长还需等待的毫秒数
     *
     * @param startTime 开始时间戳
     * @param minDurationMillis 最短展示时长
     * @param currentTime 当前时间戳，默认使用系统时间
     * @return 剩余等待时长；若已达到最短展示时长则返回 0
     * @author Joker.X
     */
    fun calculateRemainingDuration(
        startTime: Long,
        minDurationMillis: Long,
        currentTime: Long = System.currentTimeMillis()
    ): Long {
        val elapsedTime = currentTime - startTime
        return (minDurationMillis - elapsedTime).coerceAtLeast(0L)
    }
}
