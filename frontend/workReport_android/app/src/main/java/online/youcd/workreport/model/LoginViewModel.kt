package online.youcd.workreport.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import online.youcd.workreport.api.RetrofitUtil
import online.youcd.workreport.types.User


class LoginViewModel : ViewModel() {
    var api = RetrofitUtil.create()

    private val _loginMsg = MutableStateFlow<String?>(null)
    val loginMsg: StateFlow<String?> = _loginMsg

    private val _loginFlag = MutableStateFlow(false)
    val loginFlag: StateFlow<Boolean> = _loginFlag


    val isJwtExpired: Boolean
        get() = RetrofitUtil.jwtIsExpired()

    private val _jwt = MutableStateFlow("")
    val jwt: StateFlow<String> = _jwt


    val dataStoreViewModel = DataStoreViewModel()

    fun login(data: User) {
        viewModelScope.launch {
            val response = api.login(data)
            _loginMsg.value = response.msg


            if (response.flag == true) {
                _jwt.value = response.data!!.token
                dataStoreViewModel.setJwt(response.data.token)
                _loginFlag.value = response.flag
            }
        }
    }
}
