package com.joker.kit.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/**
 * 圆角规范
 */

// 标准圆角数值定义

/**
 * 超小圆角数值：4dp
 */
val RadiusXSmall = 4.dp

/**
 * 小圆角数值：8dp
 */
val RadiusSmall = 8.dp

/**
 * 中圆角数值：12dp
 */
val RadiusMedium = 12.dp

/**
 * 大圆角数值：16dp
 */
val RadiusLarge = 16.dp

/**
 * 超大圆角数值：24dp
 */
val RadiusExtraLarge = 24.dp

/**
 * 超小圆角：4dp
 * 适用场景：极小的UI元素，如图标按钮
 */
val ShapeXSmall = RoundedCornerShape(RadiusXSmall)

/**
 * 小圆角：8dp
 * 适用场景：常规卡片、按钮等小型UI元素的圆角
 */
val ShapeSmall = RoundedCornerShape(RadiusSmall)

/**
 * 中圆角：12dp
 * 适用场景：中型容器、对话框等组件的圆角
 */
val ShapeMedium = RoundedCornerShape(RadiusMedium)

/**
 * 大圆角：16dp
 * 适用场景：分类列表等组件
 */
val ShapeLarge = RoundedCornerShape(RadiusLarge)

/**
 * 超大圆角：24dp
 * 适用场景：大型卡片、底部弹窗等较大UI元素的圆角
 */
val ShapeExtraLarge = RoundedCornerShape(RadiusExtraLarge)

/**
 * 圆形
 * 适用场景：头像、图标按钮等需要完全圆形的组件
 */
val ShapeCircle = RoundedCornerShape(percent = 50)
