package online.youcd.workreport.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import online.youcd.workreport.api.RetrofitUtil
import online.youcd.workreport.types.WorkContentItem

class WorkLogFromDateViewModel : ViewModel() {
    var api = RetrofitUtil.create()

    private val _workContentItemList = MutableStateFlow<List<WorkContentItem>?>(null)
    val workContentItemList: StateFlow<List<WorkContentItem>?> = _workContentItemList

    fun getWorkLogFromDate(date: Int) {
        viewModelScope.launch {
            val response = api.getWorkLogFromDate(date)
            if (response.flag == true) {
                _workContentItemList.value = response.data?.work_content_resp_list
            }
        }
    }
}
