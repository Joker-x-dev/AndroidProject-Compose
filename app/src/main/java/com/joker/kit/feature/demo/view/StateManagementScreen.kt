package com.joker.kit.feature.demo.view

import androidx.compose.ui.res.stringResource
import com.joker.kit.R

import com.joker.kit.core.designsystem.theme.AppColors
import com.joker.kit.core.designsystem.theme.AppTextStyles
import androidx.compose.foundation.layout.Arrangement
import com.joker.kit.core.designsystem.component.AppColumn
import com.joker.kit.core.designsystem.component.AppRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.RadiusMedium
import com.joker.kit.core.designsystem.theme.SpaceHorizontalSmall
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.designsystem.theme.SpacePaddingMedium
import com.joker.kit.core.designsystem.theme.SpaceVerticalLarge
import com.joker.kit.core.designsystem.theme.SpaceVerticalMedium
import com.joker.kit.core.ui.component.scaffold.MiuixPageLazyColumn
import androidx.compose.foundation.layout.PaddingValues
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.feature.demo.viewmodel.StateManagementViewModel

/**
 * 状态管理示例路由
 *
 * @param viewModel Hilt 注入的 StateManagementViewModel
 * @author Joker.X
 */
@Composable
internal fun StateManagementRoute(
    viewModel: StateManagementViewModel = hiltViewModel()
) {
    // 收集计数器状态
    val count by viewModel.count.collectAsState()

    StateManagementScreen(
        count = count,
        onIncrease = viewModel::increase,
        onDecrease = viewModel::decrease,
        onReset = viewModel::reset
    )
}

/**
 * 状态管理示例界面
 *
 * @param count 计数器值
 * @param onIncrease +1 回调
 * @param onDecrease -1 回调
 * @param onReset 重置回调
 * @author Joker.X
 */
@Composable
internal fun StateManagementScreen(
    count: Int = 0,
    onIncrease: () -> Unit = {},
    onDecrease: () -> Unit = {},
    onReset: () -> Unit = {},
) {
    AppScaffold(
        titleText = stringResource(R.string.state_management_title),
        onBackClick = { navigateBack() },
        contentShouldConsumePadding = true,
    ) { paddingValues ->
        StateManagementContent(
            contentPadding = paddingValues,
            count = count,
            onIncrease = onIncrease,
            onDecrease = onDecrease,
            onReset = onReset
        )
    }
}

/**
 * 状态管理页面内容
 *
 * @param count 当前计数
 * @param onIncrease 递增回调
 * @param onDecrease 递减回调
 * @param onReset 重置回调
 * @author Joker.X
 */
@Composable
private fun StateManagementContent(
    contentPadding: PaddingValues,
    count: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onReset: () -> Unit
) {
    MiuixPageLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        pagePadding = PaddingValues(SpacePaddingMedium),
        verticalArrangement = Arrangement.spacedBy(SpaceVerticalLarge)
    ) {
        item {
            IntroCard()
        }
        item {
            CounterCard(
                count = count,
                onIncrease = onIncrease,
                onDecrease = onDecrease,
                onReset = onReset
            )
        }
    }
}

/**
 * 状态管理介绍卡片
 *
 * @author Joker.X
 */
@Composable
private fun IntroCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = RadiusMedium
    ) {
        AppColumn(
            modifier = Modifier.padding(SpacePaddingLarge),
            verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium)
        ) {
            Text(
                text = stringResource(R.string.state_intro_title),
                style = AppTextStyles.title3,
            )
            Text(
                text = stringResource(R.string.state_intro_description),
                color = AppColors.onSurfaceSecondary,
                style = AppTextStyles.body2
            )
        }
    }
}

/**
 * 计数器卡片
 *
 * @param count 当前计数
 * @param onIncrease 递增回调
 * @param onDecrease 递减回调
 * @param onReset 重置回调
 * @author Joker.X
 */
@Composable
private fun CounterCard(
    count: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onReset: () -> Unit
) {
    Card {
        AppColumn(
            modifier = Modifier.padding(SpacePaddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium)
        ) {
            Text(
                text = stringResource(R.string.state_counter_title),
                style = AppTextStyles.title3
            )
            Text(
                text = count.toString(),
                style = AppTextStyles.title1,
            )
            AppRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    SpaceHorizontalSmall,
                    Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onDecrease,
                    enabled = count > 0
                ) {
                    Text(text = "-1")
                }
                TextButton(text = stringResource(R.string.state_reset), onClick = onReset)
                Button(onClick = onIncrease) {
                    Text(text = "+1")
                }
            }
            Text(
                text = stringResource(R.string.state_counter_description),
                color = AppColors.onSurfaceContainerVariant,
                style = AppTextStyles.footnote1,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * 状态管理界面浅色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun StateManagementPreview() {
    AppTheme {
        StateManagementScreen(count = 5)
    }
}

/**
 * 状态管理界面深色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun StateManagementPreviewDark() {
    AppTheme(darkTheme = true) {
        StateManagementScreen(count = 5)
    }
}
