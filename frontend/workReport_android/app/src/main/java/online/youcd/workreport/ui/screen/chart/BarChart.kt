package online.youcd.workreport.ui.screen.chart

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.himanshoe.charty.bar.model.BarData
import com.himanshoe.charty.common.toChartDataCollection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import online.youcd.workreport.api.RetrofitUtil
import online.youcd.workreport.types.CountType1DataX
import online.youcd.workreport.types.CountType2DataX
import online.youcd.workreport.ui.component.chart.NewBarChart
import online.youcd.workreport.ui.component.randomColor
import online.youcd.workreport.ui.component.type1ColorMap

@Composable
fun Type1BarChart(data: List<CountType1DataX>) {
    val barData = data.mapNotNull { item ->
        val color = type1ColorMap[item.type1]
        color?.let { BarData(item.count.toFloat(), item.type1, it) }
    }
//    com.himanshoe.charty.bar.BarChart(dataCollection = barData.toChartDataCollection(),)
    NewBarChart(
        dataCollection = barData.toChartDataCollection(),
    )
}

/**
 * 获取 type2 统计 数据
 * @param id type1 id
 * @return List<CountType2DataX>
 */
@Composable  //todo 要做到 viewModel 里面
fun getType2Count(id: String): List<CountType2DataX> {
    val api = RetrofitUtil.create()

    // 使用 State 来存储网络请求结果
    val typeListState = remember(id) {
        mutableStateOf<List<CountType2DataX>>(emptyList())
    }

    LaunchedEffect(id) {
        // 在协程中执行网络请求
        val response = withContext(Dispatchers.IO) {
            api.getType2CountByID(id)
        }
        if (response.flag == true) {
            // 更新 State 中的数据
            typeListState.value = response.data?.countType2Data ?: emptyList()
        }

    }

    return typeListState.value
}

@Composable
fun Type2BarChart(id: String) {
    val data = getType2Count(id)
    if (data.isEmpty()) {
        return
    }

    val barData = data.mapNotNull { item ->
        BarData(item.count.toFloat(), item.type2, randomColor())
    }
    NewBarChart(
        dataCollection = barData.toChartDataCollection(),
    )
}
