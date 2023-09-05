package online.youcd.workreport.store

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first


const val MyDataStoreKeyJwt = "Jwt"
const val MyDataStoreKeyUser = "User"
const val MyDataStoreKeyServer = "Server"
const val MyDataStoreKeyWelcome = "Welcome"


class MyDataStore(val context: Context) {

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "WorkReport")
    }

    suspend fun saveString(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit {
            it[preferencesKey] = value
        }
    }

    suspend fun getString(key: String): String {
        val preferencesKey = stringPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[preferencesKey] ?: ""
    }


}


object Store {
    @SuppressLint("StaticFieldLeak")
    private lateinit var myDataStore: MyDataStore

    fun initialize(context: Context) {
        myDataStore = MyDataStore(context)
    }

    fun getInstance(): MyDataStore {
        if (!::myDataStore.isInitialized) {
            throw IllegalStateException("MyDataStore has not been initialized")
        }
        return myDataStore
    }
}