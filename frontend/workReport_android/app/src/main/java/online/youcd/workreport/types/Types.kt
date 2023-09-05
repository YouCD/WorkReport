package online.youcd.workreport.types

class Response<T> {
    val code = 0
    val msg: String? = null
    val flag: Boolean? = null
    val data: T? = null // 这里省略get、set方法
}

data class CountType2DataList(
    val countType2Data: List<CountType2DataX>
)

data class CountType2DataX(
    val count: Int,
    val type2: String
)

data class CountType1DataList(
    val countType1Data: List<CountType1DataX>
)

data class CountType1DataX(
    val count: Int,
    val type1: String
)

data class TypeList(
    val type_list: List<Typ>
)

data class Typ(
    val description: String,
    val id: Int,
    val pid: Int,
    val type: Int
)

data class AddTypeItem(
    val description: String,
    val pid: Int,
    val type: Int
)

data class WorkLog(
    val sum: Int,
    val work_content_resp_list: List<WorkContentItem>
)

data class ModifyItem(
    var content: String,
    var type1: Int,
    var type2: Int,
    var date: Int,
    var id: Int?
)

data class WorkContentItem(
    val content: String,
    val date: Int,
    val id: Int,
    val type1: String,
    val type1_id: Int,
    val type2: String,
    val type2_id: Int
)

data class User(
    val username: String,
    val password: String
)


data class LoginResp(
    val token: String,
    val uid: String
)