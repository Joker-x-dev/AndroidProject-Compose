package com.joker.kit.feature.demo.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.ShapeMedium
import com.joker.kit.core.designsystem.theme.SpaceHorizontalSmall
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.designsystem.theme.SpacePaddingMedium
import com.joker.kit.core.designsystem.theme.SpaceVerticalLarge
import com.joker.kit.core.designsystem.theme.SpaceVerticalMedium
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.core.ui.component.text.AppText
import com.joker.kit.core.ui.component.text.TextSize
import com.joker.kit.core.ui.component.text.TextType
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
    // 从 ViewModel 中收集 count 状态
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StateManagementScreen(
    count: Int = 0,
    onIncrease: () -> Unit = {},
    onDecrease: () -> Unit = {},
    onReset: () -> Unit = {},
) {
    AppScaffold(
        titleText = "状态管理",
        onBackClick = { navigateBack() },
    ) {
        StateManagementContent(
            count = count,
            onIncrease = onIncrease,
            onDecrease = onDecrease,
            onReset = onReset
        )
    }
}

/**
 * 状态管理内容视图
 *
 * @param count 当前计数
 * @param onIncrease 递增回调
 * @param onDecrease 递减回调
 * @param onReset 重置回调
 * @author Joker.X
 */
@Composable
private fun StateManagementContent(
    count: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(SpacePaddingMedium),
        verticalArrangement = Arrangement.spacedBy(SpaceVerticalLarge)
    ) {
        IntroCard()
        CounterCard(
            count = count,
            onIncrease = onIncrease,
            onDecrease = onDecrease,
            onReset = onReset
        )
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
        shape = ShapeMedium
    ) {
        Column(
            modifier = Modifier.padding(SpacePaddingLarge),
            verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium)
        ) {
            AppText(
                text = "为什么要有 DemoCounterState?",
                size = TextSize.TITLE_LARGE,
                type = TextType.PRIMARY
            )
            AppText(
                text = "它是一个 @Singleton + ApplicationScope 的 StateFlow，任意页面都能订阅同一份 count 并保持同步。这里的计数器示例演示了“状态放在状态持有者里，UI 只收集 StateFlow”。",
                type = TextType.SECONDARY,
                size = TextSize.BODY_MEDIUM
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
        Column(
            modifier = Modifier.padding(SpacePaddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium)
        ) {
            AppText(
                text = "全局计数器",
                size = TextSize.TITLE_LARGE
            )
            AppText(
                text = count.toString(),
                size = TextSize.DISPLAY_LARGE,
                type = TextType.PRIMARY
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    SpaceHorizontalSmall,
                    Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onDecrease,
                    enabled = count > 0
                ) {
                    Text(text = "-1")
                }
                TextButton(onClick = onReset) {
                    Text(text = "重置")
                }
                Button(onClick = onIncrease) {
                    Text(text = "+1")
                }
            }
            AppText(
                text = "操作委托给 DemoCounterState，UI 不直接改值，这样多个页面共享同一份状态也能保持一致。",
                type = TextType.TERTIARY,
                size = TextSize.BODY_SMALL,
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
