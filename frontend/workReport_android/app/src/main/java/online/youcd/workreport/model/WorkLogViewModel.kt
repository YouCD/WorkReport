package online.youcd.workreport.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import online.youcd.workreport.api.RetrofitUtil
import online.youcd.workreport.types.ModifyItem
import online.youcd.workreport.types.WorkContentItem


enum class ActionDesc {
    ADD, MODIFY, DELETE
}

data class ActionMsg(
    var desc: ActionDesc,
    var msg: String,
    var flag: Boolean
)


class WorkLogViewModel : ViewModel() {
    var api = RetrofitUtil.create()


    private val _actionMsg = MutableStateFlow<ActionMsg?>(null)
    val actionMsg: StateFlow<ActionMsg?> = _actionMsg

    private val _workLog = MutableStateFlow<List<WorkContentItem>?>(null)
    val searchWorkLogData: StateFlow<List<WorkContentItem>?> = _workLog

    val workLog = Pager(
        config = PagingConfig(
            pageSize = 10,
            initialLoadSize = 10, // 第一次加载数量，如果不设置的话是 pageSize * 2
            prefetchDistance = 2
        )
    ) {
        WorkLogSource()
    }.flow.cachedIn(viewModelScope)

    /**
     * 修改工作日志
     * @param [data] 数据
     */
    fun modifyWorkLog(data: ModifyItem) {
        viewModelScope.launch {
            val response = api.modifyWorkLog(data)
            if (response.flag == true) {
                _actionMsg
                    .value = ActionMsg(ActionDesc.MODIFY, response.msg ?: "修改完成", true)
            }
        }

    }


    /**
     * 添加工作日志
     * @param [data] 数据
     */
    fun addWorkLog(data: ModifyItem) {

        viewModelScope.launch {
            val response = api.addWorkLog(data)
            if (response.flag == true) {
                _actionMsg
                    .value = ActionMsg(ActionDesc.ADD, response.msg ?: "", true)
            }
        }
    }

    /**
     * 删除工作日志
     * @param [id] id
     */
    fun deleteWorkLog(id: Int) {
        viewModelScope.launch {
            val response = api.deleteWorkLog(id)
            if (response.flag == true) {
                _actionMsg
                    .value = ActionMsg(ActionDesc.DELETE, response.msg ?: "", true)
            }
        }

    }


    fun searchWorkLog(content: String) {
        viewModelScope.launch {
            val response = api.searchWorkLog(content)
            if (response.flag == true) {
                _workLog.value = response.data?.work_content_resp_list
            }
        }

    }


}
