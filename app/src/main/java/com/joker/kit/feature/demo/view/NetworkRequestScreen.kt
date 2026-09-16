package com.joker.kit.feature.demo.view

import androidx.compose.ui.res.stringResource
import com.joker.kit.R

import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Button
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.ui.component.scaffold.MiuixPageLazyColumn
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.designsystem.theme.SpaceVerticalMedium
import com.joker.kit.core.model.entity.Goods
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.feature.demo.viewmodel.NetworkRequestViewModel
import top.yukonga.miuix.kmp.basic.BasicComponent

/**
 * 网络请求示例路由
 *
 * @param viewModel Hilt 注入的 NetworkRequestViewModel
 * @author Joker.X
 */
@Composable
internal fun NetworkRequestRoute(
    viewModel: NetworkRequestViewModel = hiltViewModel()
) {
    // 收集商品信息状态
    val goods by viewModel.goods.collectAsState()

    NetworkRequestScreen(
        goods = goods,
        onRequestClick = viewModel::onRequestClick
    )
}

/**
 * 网络请求示例界面
 *
 * @param goods 商品信息
 * @param onRequestClick 请求按钮回调
 * @author Joker.X
 */
@Composable
internal fun NetworkRequestScreen(
    goods: Goods? = null,
    onRequestClick: () -> Unit = {},
) {
    AppScaffold(
        titleText = stringResource(R.string.network_request_title),
        onBackClick = { navigateBack() },
        contentShouldConsumePadding = true,
    ) { paddingValues ->
        NetworkRequestContent(
            contentPadding = paddingValues,
            goods = goods,
            onRequestClick = onRequestClick
        )
    }
}

/**
 * 网络请求页面内容
 *
 * @param goods 商品信息
 * @param onRequestClick 请求按钮回调
 * @author Joker.X
 */
@Composable
private fun NetworkRequestContent(
    contentPadding: PaddingValues,
    goods: Goods?,
    onRequestClick: () -> Unit,
) {
    MiuixPageLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        pagePadding = PaddingValues(SpacePaddingLarge),
        verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium),
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                BasicComponent(
                    title = stringResource(R.string.network_request_operation),
                    summary = stringResource(R.string.network_request_operation_summary),
                    bottomAction = {
                        Button(
                            onClick = onRequestClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = SpaceVerticalMedium),
                        ) {
                            Text(text = stringResource(R.string.network_request_action))
                        }
                    },
                )
            }
        }

        if (goods != null) {
            item {
                CardResult(goods = goods)
            }
        }
    }
}

/**
 * 网络请求结果卡片视图
 *
 * @param goods 商品信息
 * @author Joker.X
 */
@Composable
private fun CardResult(goods: Goods) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        BasicComponent(title = stringResource(R.string.product_name), summary = goods.title)
        BasicComponent(title = stringResource(R.string.product_subtitle), summary = goods.subTitle ?: stringResource(R.string.no_content))
        BasicComponent(title = stringResource(R.string.product_price), summary = stringResource(R.string.product_price_format, goods.price))
        BasicComponent(title = stringResource(R.string.product_sold), summary = stringResource(R.string.product_sold_format, goods.sold))
    }
}

/**
 * 模拟商品信息
 *
 * @return 示例商品
 * @author Joker.X
 */
private fun mockGoods() = Goods(
    id = 1,
    title = "手机",
    subTitle = "示例副标题",
    mainPic = "",
    price = 199,
    sold = 88,
)

/**
 * 网络请求界面浅色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun NetworkRequestPreview() {
    AppTheme {
        NetworkRequestScreen(
            goods = mockGoods(),
        )
    }
}

/**
 * 网络请求界面深色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun NetworkRequestPreviewDark() {
    AppTheme(darkTheme = true) {
        NetworkRequestScreen(
            goods = mockGoods(),
        )
    }
}
