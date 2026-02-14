package com.joker.kit.feature.demo.viewmodel

import androidx.lifecycle.viewModelScope
import com.joker.kit.core.base.viewmodel.BaseViewModel
import com.joker.kit.core.data.repository.DemoRepository
import com.joker.kit.core.database.entity.DemoEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 数据库示例页 ViewModel
 *
 * @param demoRepository Demo 仓库，封装 DemoDataSource 的增删改查
 * @author Joker.X
 */
@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val demoRepository: DemoRepository
) : BaseViewModel() {

    /** 标题输入 */
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    /** 描述输入 */
    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    /**
     * Demo 表数据流
     * UI 侧直接 collectAsState() 获取最新列表
     *
     * @return Demo 列表状态流
     * @author Joker.X
     */
    val items: StateFlow<List<DemoEntity>> = demoRepository
        .observeItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /**
     * 更新标题输入
     *
     * @param value 文本框中的标题
     * @author Joker.X
     */
    fun onTitleChange(value: String) {
        _title.value = value
    }

    /**
     * 更新描述输入
     *
     * @param value 文本框中的描述
     * @author Joker.X
     */
    fun onDescriptionChange(value: String) {
        _description.value = value
    }

    /**
     * 新增一条记录（仅当标题非空）
     *
     * @author Joker.X
     */
    fun addItem() {
        val title = _title.value.trim()
        if (title.isBlank()) return
        viewModelScope.launch {
            demoRepository.createItem(
                title = title,
                description = _description.value.trim()
            )
            // 重置输入框
            _title.value = ""
            _description.value = ""
        }
    }

    /**
     * 删除指定记录
     *
     * @param id 记录主键
     * @author Joker.X
     */
    fun deleteItem(id: Long) {
        viewModelScope.launch {
            demoRepository.deleteItem(id)
        }
    }

    /**
     * 清空全部记录
     *
     * @author Joker.X
     */
    fun clearAll() {
        viewModelScope.launch {
            demoRepository.clearAll()
        }
    }
}
