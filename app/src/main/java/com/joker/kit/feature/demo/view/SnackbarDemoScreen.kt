package com.joker.kit.feature.demo.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.R
import com.joker.kit.core.annotation.ScreenPreview
import com.joker.kit.core.annotation.ScreenPreviewDark
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.SpaceHorizontalMedium
import com.joker.kit.core.designsystem.theme.SpaceHorizontalSmall
import com.joker.kit.core.designsystem.theme.SpacePaddingMedium
import com.joker.kit.core.designsystem.theme.SpaceVerticalMedium
import com.joker.kit.core.designsystem.theme.SpacePaddingSmall
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.core.ui.component.scaffold.MiuixPageLazyColumn
import com.joker.kit.feature.demo.viewmodel.SnackbarDemoViewModel
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.TextButton

/**
 * Snackbar 示例路由。
 *
 * @param viewModel Hilt 注入的 SnackbarDemoViewModel
 */
@Composable
internal fun SnackbarDemoRoute(
    viewModel: SnackbarDemoViewModel = hiltViewModel()
) {
    // 收集操作按钮的运行态文案资源 ID。
    val actionStateResId by viewModel.actionStateResId.collectAsState()

    SnackbarDemoScreen(
        actionStateResId = actionStateResId,
        onDismissOldest = viewModel::dismissOldest,
        onDismissNewest = viewModel::dismissNewest,
        onShowShort = viewModel::showShort,
        onShowLong = viewModel::showLong,
        onShowCustom = viewModel::showCustom,
        onShowAction = viewModel::showAction,
        onShowDismissible = viewModel::showDismissible,
        onShowIndefinite = viewModel::showIndefinite,
        onShowStacked = viewModel::showStacked,
    )
}

/**
 * Snackbar 示例界面。
 *
 * 与 Miuix 官方 Snackbar 示例保持一致的按钮布局与呈现效果，
 * 展示行为替换为项目封装后的 [com.joker.kit.core.state.AppSnackbar]。
 *
 * @param actionStateResId 操作按钮运行态文案资源 ID
 * @param onDismissOldest 关闭最旧提示
 * @param onDismissNewest 关闭最新提示
 * @param onShowShort 展示短提示
 * @param onShowLong 展示长提示
 * @param onShowCustom 展示自定义时长提示
 * @param onShowAction 展示带操作按钮提示
 * @param onShowDismissible 展示可关闭提示
 * @param onShowIndefinite 展示常驻提示
 * @param onShowStacked 连续触发堆叠提示
 */
@Composable
internal fun SnackbarDemoScreen(
    actionStateResId: Int? = null,
    onDismissOldest: () -> Unit = {},
    onDismissNewest: () -> Unit = {},
    onShowShort: () -> Unit = {},
    onShowLong: () -> Unit = {},
    onShowCustom: () -> Unit = {},
    onShowAction: () -> Unit = {},
    onShowDismissible: () -> Unit = {},
    onShowIndefinite: () -> Unit = {},
    onShowStacked: () -> Unit = {},
) {
    AppScaffold(
        titleText = stringResource(R.string.snackbar_demo_title),
        onBackClick = { navigateBack() },
        contentShouldConsumePadding = true,
    ) { paddingValues ->
        SnackbarDemoContent(
            contentPadding = paddingValues,
            actionStateResId = actionStateResId,
            onDismissOldest = onDismissOldest,
            onDismissNewest = onDismissNewest,
            onShowShort = onShowShort,
            onShowLong = onShowLong,
            onShowCustom = onShowCustom,
            onShowAction = onShowAction,
            onShowDismissible = onShowDismissible,
            onShowIndefinite = onShowIndefinite,
            onShowStacked = onShowStacked,
        )
    }
}

/**
 * Snackbar 示例内容。
 *
 * @param contentPadding 页面 Scaffold 内容边距
 * @param actionStateResId 操作按钮运行态文案资源 ID
 * @param onDismissOldest 关闭最旧提示
 * @param onDismissNewest 关闭最新提示
 * @param onShowShort 展示短提示
 * @param onShowLong 展示长提示
 * @param onShowCustom 展示自定义时长提示
 * @param onShowAction 展示带操作按钮提示
 * @param onShowDismissible 展示可关闭提示
 * @param onShowIndefinite 展示常驻提示
 * @param onShowStacked 连续触发堆叠提示
 */
@Composable
private fun SnackbarDemoContent(
    contentPadding: PaddingValues,
    actionStateResId: Int?,
    onDismissOldest: () -> Unit,
    onDismissNewest: () -> Unit,
    onShowShort: () -> Unit,
    onShowLong: () -> Unit,
    onShowCustom: () -> Unit,
    onShowAction: () -> Unit,
    onShowDismissible: () -> Unit,
    onShowIndefinite: () -> Unit,
    onShowStacked: () -> Unit,
) {
    MiuixPageLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        // 卡片单独保留 Miuix 12dp 边距，页面仅保留上下留白。
        pagePadding = PaddingValues(
            top = SpacePaddingMedium,
            bottom = SpacePaddingMedium,
        ),
    ) {
        item {
            Card(
                modifier = Modifier.padding(horizontal = SpaceHorizontalMedium),
            ) {
                Column(
                    modifier = Modifier.padding(SpacePaddingSmall),
                    verticalArrangement = Arrangement.spacedBy(SpaceHorizontalSmall),
                ) {
                    SnackbarButtonRow(
                        leftText = stringResource(R.string.snackbar_demo_dismiss_oldest),
                        rightText = stringResource(R.string.snackbar_demo_dismiss_newest),
                        onLeftClick = onDismissOldest,
                        onRightClick = onDismissNewest,
                    )
                    SnackbarButtonRow(
                        leftText = stringResource(R.string.snackbar_demo_show_short),
                        rightText = stringResource(R.string.snackbar_demo_show_long),
                        onLeftClick = onShowShort,
                        onRightClick = onShowLong,
                    )
                    SnackbarButtonRow(
                        leftText = stringResource(R.string.snackbar_demo_show_custom),
                        rightText = actionStateResId
                            ?.let { stringResource(it) }
                            ?: stringResource(R.string.snackbar_demo_show_action),
                        onLeftClick = onShowCustom,
                        onRightClick = onShowAction,
                        highlightRight = true,
                    )
                    SnackbarButtonRow(
                        leftText = stringResource(R.string.snackbar_demo_show_dismissible),
                        rightText = stringResource(R.string.snackbar_demo_show_indefinite),
                        onLeftClick = onShowDismissible,
                        onRightClick = onShowIndefinite,
                    )
                    TextButton(
                        text = stringResource(R.string.snackbar_demo_show_stack),
                        onClick = onShowStacked,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

/**
 * Snackbar 示例按钮行。
 *
 * @param leftText 左侧按钮文字
 * @param rightText 右侧按钮文字
 * @param onLeftClick 左侧按钮点击回调
 * @param onRightClick 右侧按钮点击回调
 * @param highlightRight 是否将右侧按钮渲染为主色按钮
 */
@Composable
private fun SnackbarButtonRow(
    leftText: String,
    rightText: String,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    highlightRight: Boolean = false,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(SpaceHorizontalSmall),
    ) {
        TextButton(
            text = leftText,
            onClick = onLeftClick,
            modifier = Modifier.weight(1f),
        )
        TextButton(
            text = rightText,
            onClick = onRightClick,
            modifier = Modifier.weight(1f),
            colors = if (highlightRight) {
                ButtonDefaults.textButtonColorsPrimary()
            } else {
                ButtonDefaults.textButtonColors()
            },
        )
    }
}

/** Snackbar 示例浅色预览。 */
@ScreenPreview
@Composable
private fun SnackbarDemoPreview() {
    AppTheme { SnackbarDemoScreen() }
}

/** Snackbar 示例深色预览。 */
@ScreenPreviewDark
@Composable
private fun SnackbarDemoPreviewDark() {
    AppTheme(darkTheme = true) { SnackbarDemoScreen() }
}
