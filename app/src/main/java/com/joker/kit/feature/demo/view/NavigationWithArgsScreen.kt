package com.joker.kit.feature.demo.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.navigation.demo.DemoRoutes
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.core.ui.component.text.AppText
import com.joker.kit.feature.demo.viewmodel.NavigationWithArgsViewModel

/**
 * 带参跳转示例路由
 *
 * @param navKey 导航参数
 * @param viewModel Hilt 注入的 NavigationWithArgsViewModel
 * @author Joker.X
 */
@Composable
internal fun NavigationWithArgsRoute(
    navKey: DemoRoutes.NavigationWithArgs,
    viewModel: NavigationWithArgsViewModel = hiltViewModel<NavigationWithArgsViewModel, NavigationWithArgsViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(navKey)
        }
    )
) {
    NavigationWithArgsScreen(
        goodsId = viewModel.goodsId,
        onBackClick = ::navigateBack
    )
}

/**
 * 带参跳转示例界面
 *
 * @param goodsId 传入的商品 ID
 * @param onBackClick 返回按钮回调
 * @author Joker.X
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NavigationWithArgsScreen(
    goodsId: Long = 0,
    onBackClick: () -> Unit = {},
) {
    AppScaffold(
        titleText = "带参跳转",
        onBackClick = onBackClick
    ) {
        NavigationWithArgsContent(goodsId = goodsId)
    }
}

/**
 * 带参跳转内容视图
 *
 * @param goodsId 传入的商品 ID
 * @author Joker.X
 */
@Composable
private fun NavigationWithArgsContent(goodsId: Long) {
    AppText(text = "传递的商品ID：$goodsId")
}

/**
 * 带参跳转界面浅色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun NavigationWithArgsPreview() {
    AppTheme {
        NavigationWithArgsScreen(goodsId = 1)
    }
}

/**
 * 带参跳转界面深色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun NavigationWithArgsPreviewDark() {
    AppTheme(darkTheme = true) {
        NavigationWithArgsScreen(goodsId = 1)
    }
}
