package online.youcd.workreport.ui.screen.chart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.himanshoe.charty.common.config.ChartyLabelTextConfig
import com.himanshoe.charty.common.toChartDataCollection
import com.himanshoe.charty.pie.config.PieChartConfig
import com.himanshoe.charty.pie.model.PieData
import online.youcd.workreport.types.CountType1DataX
import online.youcd.workreport.ui.component.chart.NewPieChart
import online.youcd.workreport.ui.component.type1ColorMap

@Composable
fun PieChart(data: List<CountType1DataX>) {
    val pieData = data.mapNotNull { item ->
        val color = type1ColorMap[item.type1]
        color?.let { PieData(item.count.toFloat(), item.type1, it) }
    }
    NewPieChart(
        dataCollection = pieData.toChartDataCollection(),

        pieChartConfig = PieChartConfig(donut = true, showLabel = true),
        textLabelTextConfig = ChartyLabelTextConfig(
            textColor = MaterialTheme.colorScheme.primary,
            textSize = 12.sp,
            maxLine = 5
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}
