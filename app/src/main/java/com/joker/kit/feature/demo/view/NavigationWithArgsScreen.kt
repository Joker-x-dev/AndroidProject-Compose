package com.joker.kit.feature.demo.view

import androidx.compose.ui.res.stringResource
import com.joker.kit.R

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.ui.component.scaffold.MiuixPageLazyColumn
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.navigation.demo.DemoRoutes
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.scaffold.AppScaffold
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
        goodsId = viewModel.goodsId
    )
}

/**
 * 带参跳转示例界面
 *
 * @param goodsId 传入的商品 ID
 * @author Joker.X
 */
@Composable
internal fun NavigationWithArgsScreen(
    goodsId: Long = 0,
) {
    AppScaffold(
        titleText = stringResource(R.string.navigation_args_title),
        onBackClick = { navigateBack() },
        contentShouldConsumePadding = true,
    ) { paddingValues ->
        NavigationWithArgsContent(contentPadding = paddingValues, goodsId = goodsId)
    }
}

/**
 * 带参跳转页面内容
 *
 * @param goodsId 传入的商品 ID
 * @author Joker.X
 */
@Composable
private fun NavigationWithArgsContent(contentPadding: PaddingValues, goodsId: Long) {
    MiuixPageLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        pagePadding = PaddingValues(SpacePaddingLarge),
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                BasicComponent(
                    title = stringResource(R.string.navigation_goods_id),
                    summary = goodsId.toString(),
                )
            }
        }
    }
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
