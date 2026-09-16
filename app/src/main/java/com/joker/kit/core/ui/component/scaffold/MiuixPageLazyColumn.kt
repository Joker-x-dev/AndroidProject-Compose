package com.joker.kit.core.ui.component.scaffold

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic
import com.joker.kit.core.state.LocalAppSettings

/**
 * 复杂页面统一懒加载滚动布局。
 *
 * @param modifier 页面滚动容器修饰符
 * @param state LazyColumn 滚动状态
 * @param contentPadding Scaffold 提供的安全区边距
 * @param pagePadding 页面统一额外边距
 * @param verticalArrangement 条目垂直排列方式
 * @param content 页面条目内容
 */
@Composable
fun MiuixPageLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(),
    pagePadding: PaddingValues = PaddingValues(),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: LazyListScope.() -> Unit,
) {
    val settings = LocalAppSettings.current
    val layoutDirection = LocalLayoutDirection.current
    // 合并 Scaffold 安全区边距与页面统一边距。
    val resolvedContentPadding = PaddingValues(
        top = contentPadding.calculateTopPadding() + pagePadding.calculateTopPadding(),
        bottom = contentPadding.calculateBottomPadding() + pagePadding.calculateBottomPadding(),
        start = contentPadding.calculateLeftPadding(layoutDirection) + pagePadding.calculateLeftPadding(layoutDirection),
        end = contentPadding.calculateRightPadding(layoutDirection) + pagePadding.calculateRightPadding(layoutDirection),
    )
    LazyColumn(
        modifier = modifier
            // 固定页面滚动视口，保证短内容也能参与顶部栏嵌套滚动和越界回弹。
            .fillMaxSize()
            .then(if (settings.enableScrollEndHaptic) Modifier.scrollEndHaptic() else Modifier)
            .overScrollVertical(),
        state = state,
        userScrollEnabled = true,
        contentPadding = resolvedContentPadding,
        verticalArrangement = verticalArrangement,
        content = content,
    )
}
