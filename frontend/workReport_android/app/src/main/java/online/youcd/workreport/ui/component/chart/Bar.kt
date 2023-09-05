package online.youcd.workreport.ui.component.chart

import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.fastMap
import com.himanshoe.charty.bar.model.BarData
import com.himanshoe.charty.common.ChartDataCollection
import com.himanshoe.charty.common.ComposeList
import com.himanshoe.charty.common.config.AxisConfig
import com.himanshoe.charty.common.config.ChartDefaults
import com.himanshoe.charty.common.maxYValue
import com.himanshoe.charty.common.minYValue
import com.himanshoe.charty.common.ui.drawGridLines
import com.himanshoe.charty.common.ui.drawXAxis
import com.himanshoe.charty.common.ui.drawYAxis

@Composable
fun NewBarChart(
    dataCollection: ChartDataCollection,
    modifier: Modifier = Modifier,
    barSpacing: Dp = 8.dp,
    padding: Dp = 16.dp,
    axisConfig: AxisConfig = ChartDefaults.axisConfigDefaults(),
) {

    val points = dataCollection.data

    val screenWidth = with(LocalContext.current) { resources.displayMetrics.widthPixels.toFloat() }
    var chartBound by remember { mutableStateOf(0F) }
    var chartWidth by remember { mutableStateOf(0F) }
    var chartHeight by remember { mutableStateOf(0F) }
    val textColor = MaterialTheme.colorScheme.primary

    // 原项目 ChartSurface 有一个默认的 drawYAxisLabels 要执行，不支持修改颜色
    ChartSurface(
        padding = padding,
        modifier = modifier,
    ) {
        val data = dataCollection.data
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { size ->
                    chartWidth = size.width.toFloat()
                    chartHeight = size.height.toFloat()
                    chartBound = chartWidth.div(
                        points
                            .count()
                            .times(1.2F)
                    )
                }
                .background(MaterialTheme.colorScheme.background)
                .drawBehind {
                    drawYAxisLabels(
                        values =
                        data.fastMap { it.yValue },
                        spacing = padding.toPx(),
                        textColor = textColor
                    )
                    if (data.count() >= 14 && axisConfig.showGridLabel) {
                        drawXAxisLabels(
                            data = data.fastMap { it.xValue },
                            count = data.count(),
                            padding = padding.toPx(),
                            minLabelCount = axisConfig.minLabelCount,
                            textColor = textColor
                        )
                    }
                    if (axisConfig.showAxes) {
                        drawYAxis(axisConfig.axisColor, axisConfig.axisStroke)
                        drawXAxis(axisConfig.axisColor, axisConfig.axisStroke)
                    }
                    if (axisConfig.showGridLines) {
                        drawGridLines(chartWidth, chartHeight, padding.toPx())
                    }
                }
        ) {
            val maxValue = dataCollection.maxYValue()
            val barCount = data.size
            val minValue = if (dataCollection.minYValue() < 0) {
                dataCollection.minYValue()
            } else {
                0F
            }
            val range = maxValue - minValue
            val availableWidth = chartWidth - (barSpacing.toPx() * (barCount - 1))
            val maxBarWidth = availableWidth / barCount

            data.fastForEachIndexed { index, barData ->
                val barWidth = maxBarWidth.coerceAtMost(screenWidth / barCount)
                val barHeight =
                    (barData.yValue - minValue) / range * (chartHeight - axisConfig.axisStroke)

                val startX = index * (barWidth + barSpacing.toPx())
                val startY = chartHeight - barHeight

                if (barData is BarData) {
                    if (barData.color != null) {
                        drawRect(
                            color = barData.color!!,
                            topLeft = Offset(startX, startY),
                            size = Size(barWidth, barHeight)
                        )
                    } else {
                        throw NoSuchFieldException("$barData should have color")
                    }
                    if (data.count() < 14) {
                        drawXAxisLabels(
                            data = barData.xValue,
                            center = Offset(startX + barWidth / 2, startY),
                            count = data.count(),
                            padding = padding.toPx(),
                            minLabelCount = axisConfig.minLabelCount,
                            // 颜色设置
                            textColor = textColor
                        )
                    }
                }
            }
        }
    }
}

