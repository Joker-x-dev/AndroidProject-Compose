package com.joker.kit.feature.demo.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.core.database.entity.DemoEntity
import com.joker.kit.core.designsystem.component.CenterColumn
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.ShapeMedium
import com.joker.kit.core.designsystem.theme.SpaceHorizontalSmall
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.designsystem.theme.SpacePaddingMedium
import com.joker.kit.core.designsystem.theme.SpaceVerticalLarge
import com.joker.kit.core.designsystem.theme.SpaceVerticalMedium
import com.joker.kit.core.designsystem.theme.SpaceVerticalSmall
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.component.divider.Divider
import com.joker.kit.core.ui.component.scaffold.AppScaffold
import com.joker.kit.core.ui.component.text.AppText
import com.joker.kit.core.ui.component.text.TextSize
import com.joker.kit.core.ui.component.text.TextType
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
    // 列表数据（Demo 表 Flow -> State）
    val items by viewModel.items.collectAsState()
    // 标题输入状态
    val title by viewModel.title.collectAsState()
    // 描述输入状态
    val description by viewModel.description.collectAsState()

    DatabaseScreen(
        title = title,
        description = description,
        items = items,
        onTitleChange = viewModel::onTitleChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onAddClick = viewModel::addItem,
        onDeleteItem = viewModel::deleteItem,
        onClearAll = viewModel::clearAll,
        onBackClick = ::navigateBack
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
 * @param onBackClick 返回按钮回调
 * @author Joker.X
 */
@OptIn(ExperimentalMaterial3Api::class)
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
    onBackClick: () -> Unit = {},
) {
    AppScaffold(
        titleText = "数据库",
        onBackClick = onBackClick
    ) {
        DatabaseContent(
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
 * 数据库内容视图
 *
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
    title: String,
    description: String,
    items: List<DemoEntity>,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onDeleteItem: (Long) -> Unit,
    onClearAll: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(SpacePaddingMedium),
        verticalArrangement = Arrangement.spacedBy(SpaceVerticalLarge)
    ) {
        InputCard(
            title = title,
            description = description,
            onTitleChange = onTitleChange,
            onDescriptionChange = onDescriptionChange,
            onAddClick = onAddClick,
            onClearAll = onClearAll,
            canClear = items.isNotEmpty()
        )

        DemoListCard(
            items = items,
            onDeleteItem = onDeleteItem
        )
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
    Card {
        Column(
            modifier = Modifier.padding(SpacePaddingLarge),
            verticalArrangement = Arrangement.spacedBy(SpaceVerticalMedium)
        ) {
            AppText(
                text = "用 DemoRepository 做增删改查",
                size = TextSize.TITLE_MEDIUM
            )
            AppText(
                text = "输入标题即可新增一条记录，描述可选；列表来自 DemoRepository.observeItems() 的 Flow。",
                type = TextType.TERTIARY,
                size = TextSize.BODY_MEDIUM
            )

            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text("标题（必填）") },
                singleLine = true,
                shape = ShapeMedium,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("描述（可选）") },
                shape = ShapeMedium,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SpaceHorizontalSmall, Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onClearAll,
                    enabled = canClear
                ) {
                    Text("清空全部")
                }
                Button(
                    onClick = onAddClick,
                    enabled = title.isNotBlank()
                ) {
                    Text("新增记录")
                }
            }
        }
    }
}

/**
 * Demo 列表卡片
 *
 * @param items Demo 数据列表
 * @param onDeleteItem 删除单条记录回调
 * @author Joker.X
 */
@Composable
private fun DemoListCard(
    items: List<DemoEntity>,
    onDeleteItem: (Long) -> Unit
) {
    Card {
        AppText(
            text = "Demo 表当前 ${items.size} 条",
            size = TextSize.TITLE_MEDIUM,
            modifier = Modifier.padding(
                horizontal = SpacePaddingLarge,
                vertical = SpacePaddingMedium
            )
        )

        Divider()

        if (items.isEmpty()) {
            CenterColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(SpacePaddingLarge),
            ) {
                AppText(text = "列表为空，先添加一条吧", type = TextType.SECONDARY)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(SpacePaddingMedium),
                verticalArrangement = Arrangement.spacedBy(SpaceVerticalSmall)
            ) {
                items(items, key = { it.id }) { item ->
                    DemoListItem(
                        item = item,
                        onDeleteItem = onDeleteItem
                    )
                }
                item { Spacer(modifier = Modifier.height(SpaceVerticalLarge)) }
            }
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
    val formattedTime = remember(item.updatedAt) {
        SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(Date(item.updatedAt))
    }

    ListItem(
        headlineContent = {
            AppText(
                text = item.title.ifBlank { "未命名记录" },
                size = TextSize.TITLE_MEDIUM,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            val desc = item.description.ifBlank { "暂无描述" }
            AppText(
                text = "$desc · 更新于 $formattedTime",
                type = TextType.SECONDARY,
                size = TextSize.BODY_MEDIUM,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            TextButton(onClick = { onDeleteItem(item.id) }) {
                Text("删除")
            }
        }
    )
}

/**
 * 数据库界面浅色主题预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
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
@Preview(showBackground = true)
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
