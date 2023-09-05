package online.youcd.workreport.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import online.youcd.workreport.api.RetrofitUtil
import online.youcd.workreport.types.CountType1DataX

class CountType1ViewModel : ViewModel() {
    var api = RetrofitUtil.create()

    private val _countType1Data = MutableStateFlow<List<CountType1DataX>?>(null)
    val countType1Data: StateFlow<List<CountType1DataX>?> = _countType1Data

    fun fetchCountType1Data() {

        viewModelScope.launch {
            val response = api.getType1Count()
            if (response.flag == true) {
                _countType1Data.value = response.data?.countType1Data
            }
        }

    }
}
