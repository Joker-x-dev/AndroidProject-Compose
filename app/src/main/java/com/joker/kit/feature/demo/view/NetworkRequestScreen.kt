package com.joker.kit.feature.demo.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.designsystem.theme.SpacePaddingMedium
import com.joker.kit.core.model.entity.Goods
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.core.ui.component.text.AppText
import com.joker.kit.feature.demo.viewmodel.NetworkRequestViewModel

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
    // 商品信息
    val goods by viewModel.goods.collectAsState()

    NetworkRequestScreen(
        goods = goods,
        onBackClick = ::navigateBack,
        onRequestClick = viewModel::onRequestClick
    )
}

/**
 * 网络请求示例界面
 *
 * @param goods 商品信息
 * @param onBackClick 返回按钮回调
 * @param onRequestClick 请求按钮回调
 * @author Joker.X
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NetworkRequestScreen(
    goods: Goods? = null,
    onBackClick: () -> Unit = {},
    onRequestClick: () -> Unit = {},
) {
    AppScaffold(
        titleText = "网络请求",
        onBackClick = onBackClick
    ) {
        NetworkRequestContent(
            goods = goods,
            onRequestClick = onRequestClick
        )
    }
}

/**
 * 网络请求内容视图
 *
 * @param goods 商品信息
 * @param onRequestClick 请求按钮回调
 * @author Joker.X
 */
@Composable
private fun NetworkRequestContent(
    goods: Goods?,
    onRequestClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SpacePaddingLarge),
        verticalArrangement = Arrangement.spacedBy(SpacePaddingMedium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onRequestClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            AppText(text = "发起网络请求")
        }

        if (goods != null) {
            CardResult(goods = goods)
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
        Column(modifier = Modifier.padding(SpacePaddingMedium)) {
            AppText(text = "商品名称：${goods.title}")
            AppText(text = "副标题：${goods.subTitle ?: "暂无"}")
            AppText(text = "价格：¥${goods.price}")
            AppText(text = "已售：${goods.sold} 件")
        }
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
