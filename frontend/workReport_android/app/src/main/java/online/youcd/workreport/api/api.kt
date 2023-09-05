package online.youcd.workreport.api

import online.youcd.workreport.types.AddTypeItem
import online.youcd.workreport.types.CountType1DataList
import online.youcd.workreport.types.CountType2DataList
import online.youcd.workreport.types.LoginResp
import online.youcd.workreport.types.ModifyItem
import online.youcd.workreport.types.Response
import online.youcd.workreport.types.TypeList
import online.youcd.workreport.types.User
import online.youcd.workreport.types.WorkLog
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface WorkReportApi {
    /**
     * 通过工作type2 pid获取type列表
     * @param [pid] pid
     * @return [Call<Response<TypeList>>]
     */
    @GET(value = Url.workType2)
    suspend fun getWorkType2ByPid(
        @Query("pid") pid: Int
    ): Response<TypeList>

    /**
     * 获取type1的总数
     * @return [Call<Response<CountType1DataList>>]
     */
    @GET(value = Url.type1Count)
    suspend fun getType1Count(): Response<CountType1DataList>

    /**
     * 通过pid获取type1的列表
     * @param [pid] pid
     * @return [Call<Response<TypeList>>]
     */
    @GET(value = Url.workType1)
    suspend fun getWorkType1ByPid(
    ): Response<TypeList>

    /**
     * 通过id获取type2总数
     * @param [pid] pid
     * @return [Call<Response<CountType2DataList>>]
     */
    @GET(value = Url.type2Count)
    suspend fun getType2CountByID(
        @Query("id") pid: String
    ): Response<CountType2DataList>

    /**
     * 添加工作类型
     * @param [data] 数据
     * @return [Call<Response<Any>>]
     */ // 添加 工作类型
    @POST(value = Url.workType)
    suspend fun addWorkType(
        @Body data: AddTypeItem
    ): Response<Any>

    /**
     * 获取工作日志
     * @param [pageIndex] 页面索引
     * @param [pageSize] 页面大小
     * @return [Call<Response<WorkLog>>]
     */
    @GET(value = Url.workLog)
    suspend fun getWorkLog(
        @Query("pageIndex") pageIndex: Int,
        @Query(
            "pageSize",
        ) pageSize: Int
    ): Response<WorkLog>

    /**
     * 通过日期获取工作日志
     * @param [date] 日期
     * @return [Call<Response<WorkLog>>]
     */
    @GET(value = Url.workLogFromDate)
    suspend fun getWorkLogFromDate(
        @Query("date") date: Int
    ): Response<WorkLog>

    /**
     * 修改工作日志
     * @param [data] 数据
     * @return [Call<Response<Any>>]
     */
    @PUT(value = Url.workLog)
    suspend fun modifyWorkLog(
        @Body data: ModifyItem
    ): Response<Any>

    /**
     * 添加工作日志
     * @param [data] 数据
     * @return [Call<Response<Any>>]
     */
    @POST(value = Url.workLog)
    suspend fun addWorkLog(
        @Body data: ModifyItem
    ): Response<Any>

    /**
     * 删除工作日志
     * @param [id] id
     * @return [Call<Response<Any>>]
     */
    @DELETE(value = Url.workLog)
    suspend fun deleteWorkLog(
        @Query("id") id: Int
    ): Response<Any>

    /**
     * 搜索日志
     * @param [content]
     * @return [Response<WorkLog>]
     */
    @GET(value = Url.workLogFromContent)
    suspend fun searchWorkLog(
        @Query(value = "content", encoded = true) content: String
    ): Response<WorkLog>

    @POST(value = Url.login)
    suspend fun login(
        @Body data: User
    ): Response<LoginResp>

}
