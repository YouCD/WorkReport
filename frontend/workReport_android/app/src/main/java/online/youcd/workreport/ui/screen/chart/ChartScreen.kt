import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import online.youcd.workreport.model.CountType1ViewModel
import online.youcd.workreport.types.CountType1DataX
import online.youcd.workreport.ui.component.DefaultCard
import online.youcd.workreport.ui.screen.chart.PieChart
import online.youcd.workreport.ui.screen.chart.Type1BarChart
import online.youcd.workreport.ui.screen.chart.Type2BarChart

@Composable
fun ChartScreen(data: List<CountType1DataX>?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.outlineVariant),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (data != null) {
            // type1 饼图

            DefaultCard(
                modifier = Modifier.weight(1f),
                content = {
                    PieChart(data)
                }
            )

            // type1 柱状图
            DefaultCard(
                modifier = Modifier.weight(1f),
                content = {
                    Type1BarChart(data)
                }
            )

            // type2 柱状图
            DefaultCard(
                modifier = Modifier.weight(1f),
                content = {
                    Type2BarChart("1")
                }
            )
        } else {
            Text(text = "加载中")
        }
    }
}

@Composable
fun ChartScreen() {
    val viewModel: CountType1ViewModel = viewModel()
    val countType1Data by viewModel.countType1Data.collectAsState()
    // 在协程中调用 fetchCountType1Data
    LaunchedEffect(viewModel) {
        viewModel.fetchCountType1Data()
    }
    ChartScreen(data = countType1Data)
}

@Preview
@Composable
fun ChartScreenPreview() {
    ChartScreen()
}
