package online.youcd.workreport.ui.component.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.util.fastMap
import com.himanshoe.charty.common.ChartDataCollection
import com.himanshoe.charty.common.ComposeList
import com.himanshoe.charty.common.config.AxisConfig
import com.himanshoe.charty.common.config.ChartDefaults
import com.himanshoe.charty.common.minYValue


@Composable
fun ChartSurface(
    padding: Dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    BoxWithConstraints(
        modifier = modifier
            .padding(
                start = padding.times(2),
                bottom = padding.times(2),
                top = padding.times(2),
                end = padding
            ),
            // 原项目 ChartSurface 有一个默认的 drawYAxisLabels 要执行，不支持修改颜色
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
