package com.joker.kit.feature.demo.view

import androidx.compose.ui.res.stringResource
import com.joker.kit.R

import com.joker.kit.core.designsystem.theme.AppColors
import com.joker.kit.core.designsystem.theme.AppTextStyles
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.SmallTitle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.joker.kit.core.annotation.ScreenPreview
import com.joker.kit.core.annotation.ScreenPreviewDark
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.database.entity.DemoEntity
import com.joker.kit.core.designsystem.component.AppColumn
import com.joker.kit.core.designsystem.component.AppRow
import com.joker.kit.core.designsystem.component.CenterColumn
import com.joker.kit.core.designsystem.component.SpaceVerticalMedium
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.RadiusMedium
import com.joker.kit.core.designsystem.theme.SpaceHorizontalMedium
import com.joker.kit.core.designsystem.theme.SpaceHorizontalSmall
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.designsystem.theme.SpaceVerticalMedium
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.core.ui.component.scaffold.MiuixPageLazyColumn
import com.joker.kit.feature.demo.viewmodel.DatabaseViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 数据库示例路由
 *
 * @param viewModel Hilt 注入的 DatabaseViewModel
 * @author Joker.X
 */
@Composable
internal fun DatabaseRoute(
    viewModel: DatabaseViewModel = hiltViewModel()
) {
    // 收集数据库列表状态
    val items by viewModel.items.collectAsState()
    // 收集标题输入状态
    val title by viewModel.title.collectAsState()
    // 收集描述输入状态
    val description by viewModel.description.collectAsState()

    DatabaseScreen(
        title = title,
        description = description,
        items = items,
        onTitleChange = viewModel::onTitleChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onAddClick = viewModel::addItem,
        onDeleteItem = viewModel::deleteItem,
        onClearAll = viewModel::clearAll
    )
}

/**
 * 数据库示例界面
 *
 * @param title 标题输入框内容
 * @param description 描述输入框内容
 * @param items Demo 表数据
 * @param onTitleChange 标题输入变化回调
 * @param onDescriptionChange 描述输入变化回调
 * @param onAddClick 点击新增记录
 * @param onDeleteItem 删除指定记录
 * @param onClearAll 清空所有记录
 * @author Joker.X
 */
@Composable
internal fun DatabaseScreen(
    title: String = "",
    description: String = "",
    items: List<DemoEntity> = emptyList(),
    onTitleChange: (String) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onAddClick: () -> Unit = {},
    onDeleteItem: (Long) -> Unit = {},
    onClearAll: () -> Unit = {},
) {
    AppScaffold(
        titleText = stringResource(R.string.database_title),
        onBackClick = { navigateBack() },
        contentShouldConsumePadding = true,
    ) { paddingValues ->
        DatabaseContent(
            contentPadding = paddingValues,
            title = title,
            description = description,
            items = items,
            onTitleChange = onTitleChange,
            onDescriptionChange = onDescriptionChange,
            onAddClick = onAddClick,
            onDeleteItem = onDeleteItem,
            onClearAll = onClearAll
        )
    }
}

/**
 * 数据库页面内容
 *
 * @param contentPadding 页面脚手架提供的系统栏和顶部栏边距
 * @param title 标题输入
 * @param description 描述输入
 * @param items Demo 列表数据
 * @param onTitleChange 标题更新回调
 * @param onDescriptionChange 描述更新回调
 * @param onAddClick 新增记录回调
 * @param onDeleteItem 删除记录回调
 * @param onClearAll 清空列表回调
 * @author Joker.X
 */
@Composable
private fun DatabaseContent(
    contentPadding: PaddingValues,
    title: String,
    description: String,
    items: List<DemoEntity>,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onDeleteItem: (Long) -> Unit,
    onClearAll: () -> Unit
) {
    MiuixPageLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        pagePadding = PaddingValues(
            top = SpacePaddingLarge,
            bottom = SpacePaddingLarge,
        ),
    ) {
        item(key = "database-input") {
            InputCard(
                title = title,
                description = description,
                onTitleChange = onTitleChange,
                onDescriptionChange = onDescriptionChange,
                onAddClick = onAddClick,
                onClearAll = onClearAll,
                canClear = items.isNotEmpty(),
            )
        }
        item {
            SpaceVerticalMedium()
        }
        item(key = "database-list-title") {
            SmallTitle(
                text = stringResource(R.string.database_count_format, items.size),
            )
        }
        if (items.isEmpty()) {
            item(key = "database-empty") {
                EmptyDatabaseCard()
            }
        } else {
            items(items, key = { it.id }) { item ->
                DemoListItem(
                    item = item,
                    onDeleteItem = onDeleteItem,
                )
            }
        }
    }
}

/**
 * 新增/清空操作区域
 *
 * @param title 标题输入
 * @param description 描述输入
 * @param onTitleChange 标题变化回调
 * @param onDescriptionChange 描述变化回调
 * @param onAddClick 新增记录
 * @param onClearAll 清空全部
 * @param canClear 是否允许清空按钮启用
 * @author Joker.X
 */
@Composable
private fun InputCard(
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onClearAll: () -> Unit,
    canClear: Boolean
) {
    Card(
        modifier = Modifier.padding(horizontal = SpaceHorizontalMedium),
    ) {
        AppColumn(
            modifier = Modifier.padding(SpacePaddingLarge),
            verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium)
        ) {
            Text(
                text = stringResource(R.string.database_crud_title),
                style = AppTextStyles.headline2
            )
            Text(
                text = stringResource(R.string.database_crud_description),
                color = AppColors.onSurfaceContainerVariant,
                style = AppTextStyles.body2
            )

            TextField(
                value = title,
                onValueChange = onTitleChange,
                label = stringResource(R.string.database_title_required),
                singleLine = true,
                cornerRadius = RadiusMedium,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = stringResource(R.string.database_description_optional),
                cornerRadius = RadiusMedium,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )

            AppRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SpaceHorizontalSmall, Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(text = stringResource(R.string.database_clear_all), onClick = onClearAll, enabled = canClear)
                Button(
                    onClick = onAddClick,
                    enabled = title.isNotBlank()
                ) {
                    Text(stringResource(R.string.database_add))
                }
            }
        }
    }
}

/** 空数据库状态卡片。 */
@Composable
private fun EmptyDatabaseCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SpaceHorizontalMedium),
    ) {
        CenterColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacePaddingLarge),
        ) {
            Text(
                text = stringResource(R.string.database_empty),
                color = AppColors.onSurfaceSecondary,
            )
        }
    }
}

/**
 * 单条 Demo 展示
 *
 * @param item Demo 实体
 * @param onDeleteItem 删除该条记录回调
 * @author Joker.X
 */
@Composable
private fun DemoListItem(
    item: DemoEntity,
    onDeleteItem: (Long) -> Unit
) {
    // 数据记录更新时间展示文案
    val formattedTime = remember(item.updatedAt) {
        SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(Date(item.updatedAt))
    }

    val desc = item.description.ifBlank { stringResource(R.string.database_no_description) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SpaceHorizontalMedium),
    ) {
        BasicComponent(
            title = item.title.ifBlank { stringResource(R.string.database_unnamed_record) },
            summary = stringResource(R.string.database_updated_format, desc, formattedTime),
            endActions = { TextButton(text = stringResource(R.string.database_delete), onClick = { onDeleteItem(item.id) }) },
            onClick = null,
        )
    }
}

/**
 * 数据库界面浅色主题预览
 *
 * @author Joker.X
 */
@ScreenPreview
@Composable
private fun DatabasePreview() {
    AppTheme {
        DatabaseScreen(
            title = "标题",
            description = "描述",
            items = previewDemoItems()
        )
    }
}

/**
 * 数据库界面深色主题预览
 *
 * @author Joker.X
 */
@ScreenPreviewDark
@Composable
private fun DatabasePreviewDark() {
    AppTheme(darkTheme = true) {
        DatabaseScreen(
            title = "标题",
            description = "描述",
            items = previewDemoItems()
        )
    }
}

/**
 * 预览用 Demo 数据
 *
 * @return 示例数据列表
 * @author Joker.X
 */
private fun previewDemoItems() = listOf(
    DemoEntity(id = 1, title = "演示标题 A", description = "这是第一条记录"),
    DemoEntity(id = 2, title = "演示标题 B", description = "描述可以留空"),
    DemoEntity(id = 3, title = "演示标题 C", description = "点击右侧图标删除")
)
