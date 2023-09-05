package online.youcd.workreport.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import online.youcd.workreport.api.RetrofitUtil
import online.youcd.workreport.types.AddTypeItem

class AddTypeViewModel : ViewModel() {
    var api = RetrofitUtil.create()

    private val _addFlag = MutableStateFlow(false)
    val addFlag: StateFlow<Boolean> = _addFlag
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage
    fun addType(data: AddTypeItem) {
        viewModelScope.launch {
            val response = api.addWorkType(data)
            if (response.flag == true) {
                if (data.type == 1) {
                    _toastMessage.value = "工作大类添加成功！"
                } else {
                    _toastMessage.value = "工作子类添加成功！"
                }
                _addFlag.value = true
            }
        }
    }


    fun setToastMessage(message: String?) {
        _toastMessage.value = message
    }
}
