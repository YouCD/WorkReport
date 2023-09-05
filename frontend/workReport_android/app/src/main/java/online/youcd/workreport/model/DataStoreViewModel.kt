package online.youcd.workreport.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import online.youcd.workreport.store.MyDataStoreKeyJwt
import online.youcd.workreport.store.MyDataStoreKeyServer
import online.youcd.workreport.store.MyDataStoreKeyWelcome
import online.youcd.workreport.store.Store


class DataStoreViewModel : ViewModel() {

    private val _jwt = MutableStateFlow("")
    val jwt: StateFlow<String> = _jwt

    private val _welcome = MutableStateFlow("")
    val welcome: StateFlow<String> = _welcome


    private val _server = MutableStateFlow("")
    val server: StateFlow<String> = _server


    fun setJwt(jwt: String) {
        _jwt.value = jwt
        viewModelScope.launch(Dispatchers.IO) {
            Store.getInstance().saveString(MyDataStoreKeyJwt, jwt)
        }
    }

    fun setWelcome(welcome: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Store.getInstance().saveString(MyDataStoreKeyWelcome, welcome)
        }
    }

    fun getWelcome() {
        viewModelScope.launch(Dispatchers.IO) {
            val jwtValue = Store.getInstance().getString(MyDataStoreKeyWelcome)
            _welcome.value = jwtValue
        }
    }


    // 修改 getJwt 函数，以更新 _jwt 的 MutableStateFlow
    fun getJwt() {
        viewModelScope.launch(Dispatchers.IO) {
            val jwtValue = Store.getInstance().getString(MyDataStoreKeyJwt)
            _jwt.value = jwtValue // 更新 MutableStateFlow 的值
        }
    }


    fun setServer(server: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Store.getInstance().saveString(MyDataStoreKeyServer, server)
        }
    }

    fun getServer() {
        viewModelScope.launch(Dispatchers.IO) {
            val value = Store.getInstance().getString(MyDataStoreKeyServer)
            _server.value = value

        }
    }


}
