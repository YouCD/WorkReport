package online.youcd.workreport.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import online.youcd.workreport.api.RetrofitUtil
import online.youcd.workreport.types.Typ

class TypeListViewModel : ViewModel() {
    var api = RetrofitUtil.create()

    private val _type1ListData = MutableStateFlow<List<Typ>?>(null)
    val type1ListData: StateFlow<List<Typ>?> = _type1ListData


    private val _type2ListData = MutableStateFlow<Map<Int, List<Typ>>?>(null)
    val type2ListData: StateFlow<Map<Int, List<Typ>>?> = _type2ListData


    fun getWorkType1ByPid() {
        viewModelScope.launch {
            val response = api.getWorkType1ByPid()
            if (response.flag == true) {
                _type1ListData.value = response.data?.type_list
            }
        }

    }


    fun getType2List(pid: Int) {
        if (type2ListData.value?.contains(pid) == true) {
            return
        }
        viewModelScope.launch {
            val response = api.getWorkType2ByPid(pid)
            if (response.flag == true) {
                if (response.data != null) {
                    Log.e("type_list", "${mapOf(pid to response.data.type_list)}")

                    val mergedMap = _type2ListData.value?.toMutableMap()
                        ?: mutableMapOf() // 创建一个可变的 Map，将原始 Map 的内容复制到其中
                    mergedMap[pid] = response.data.type_list // 将新的键值对添加到 Map 中
                    _type2ListData.value = mergedMap // 更新 LiveData 的值为合并后的 Map


                    Log.e("_type2ListData", "${_type2ListData.value}")

                }
            }
        }
    }
}
