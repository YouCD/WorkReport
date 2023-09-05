import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//

@Composable
fun NewCircle(modifier: Modifier, inDarkTheme: Boolean = false, activeIndex: Boolean?) {
    Canvas(
        modifier = modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onBackground)
    ) {
        drawCircle(
            color = if (activeIndex != null && activeIndex) {
                Color.Red
            } else {
                if (inDarkTheme) {
                    online.youcd.workreport.ui.theme.md_theme_dark_primary
                } else {
                    online.youcd.workreport.ui.theme.md_theme_light_primary
                }
            },
            radius = size.minDimension / 2,
            center = Offset(size.width / 2, size.height / 2)
        )
    }
}

@Composable
fun CircleWithText(modifier: Modifier, text: String, fontSize: Int = 20) {
    Box(modifier = modifier.size((fontSize * 1.5).dp)) {
        Canvas(modifier = Modifier.matchParentSize()) {
//            drawCircle(color = Color.Blue, radius = size.minDimension / 2)
            drawCircle(
                color = Color.Red, // Transparent fill color
                style = Stroke(width = 1.dp.toPx(), cap = StrokeCap.Round),
                radius = size.minDimension / 2
            )
        }
        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            color = Color.Red,
            fontSize = fontSize.sp
        )
    }
}
