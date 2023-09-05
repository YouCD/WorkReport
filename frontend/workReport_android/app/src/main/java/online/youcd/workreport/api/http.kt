package online.youcd.workreport.api

import AllDestinations
import android.util.Log
import com.auth0.android.jwt.JWT
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import online.youcd.workreport.store.MyDataStoreKeyJwt
import online.youcd.workreport.store.MyDataStoreKeyServer
import online.youcd.workreport.store.MyDataStoreKeyWelcome
import online.youcd.workreport.store.Store
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitUtil {
    var jwt: String? = null

    //    var baseUrl: String? = null
    var baseUrl = "https://work.youcd.online"

    // 创建 BaseHeaderInterceptor 自定义的拦截器
    val BaseHeaderInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder()
            .addHeader("Jwt", jwt ?: "")
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .build()
        chain.proceed(modifiedRequest)
    }

    // 使用okhttp自带的日志输出
    private var clientWithHttpLoggingInterceptor = OkHttpClient()

    init {
        clientWithHttpLoggingInterceptor = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS) // 连接超时时间
            .readTimeout(10, TimeUnit.SECONDS) // 读取超时
            .writeTimeout(10, TimeUnit.SECONDS) // 请求超时
            .addInterceptor(LogInterceptor())
            .addInterceptor(BaseHeaderInterceptor)
            .build()
    }

    // 创建出来 retrofit的对象
    private var retrofit = Retrofit.Builder()
        // 这里可以使用自己定义的client
        .client(clientWithHttpLoggingInterceptor)
        // 注意，这里必须以 / 结尾，否则会报错
        .baseUrl(baseUrl!!)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun jwtIsExpired(): Boolean {
        val jwtValue = runBlocking {
            Store.getInstance().getString(MyDataStoreKeyJwt)
        }
        if (jwtValue.isNullOrEmpty()) {
            return true
        }

        val j = JWT(jwtValue)
        if (j.isExpired(60 * 60 * 24)) {  // 最后一天要求重新登入
            return true
        }
        return false
    }

    fun firstScreen(): String {
//        val jwtValue = runBlocking {
//            Store.getInstance().getString(MyDataStoreKeyJwt)
//        }
        val welcomeValue = runBlocking {
            Store.getInstance().getString(MyDataStoreKeyWelcome)
        }
        // 如果是刚安装 就进入 欢迎页面
        return if (welcomeValue.isNullOrEmpty()) {
            AllDestinations.Welcome
        } else {
            AllDestinations.LOGIN
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun create(): WorkReportApi {
        val jwtValue = runBlocking {
            Store.getInstance().getString(MyDataStoreKeyJwt)
        }
        val baseUrlValue = runBlocking {
            Store.getInstance().getString(MyDataStoreKeyServer)
        }
        if (!baseUrlValue.isNullOrEmpty()) {
            baseUrl = baseUrlValue
        }
        GlobalScope.launch(context = Dispatchers.IO) {
            Store.getInstance().saveString(MyDataStoreKeyServer, baseUrl)
        }
        jwt = jwtValue
        return retrofit.create(WorkReportApi::class.java)
    }
}

// 自定义拦截器需要实现 okhttp中的接口
class LogInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 获取请求执行时的时间戳
        val timeStart = System.nanoTime()
        // 获取调用链中的request对象
        val request = chain.request()
        var buffer = Buffer()
        request.body?.writeTo(buffer)
        val requestBodyStr = buffer.readUtf8()
        Log.e(
            "OkHttp",
            String.format("Sending request %s with params 5s", request.url, requestBodyStr)
        )
        val response = chain.proceed(request)
        // response 只能读取一次，后续再次读取body的时候就会报错
        val responseData = response.body?.string() ?: "response body null"
        // 构建新的responseBody
        val responseBody = responseData.toResponseBody(response.body?.contentType())
        val endTime = System.nanoTime()
        Log.e(
            "OkHttp",
            "接口请求地址为 ${request.url},接口返回的数据是 $responseData,用时${(endTime - timeStart) / 1e6}ms "
        )
        // 返回新的 response
        return response.newBuilder().body(responseBody).build()
    }
}
