package online.youcd.workreport.ui.component.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun FormRowLayout(
    icon: ImageVector? = null,
    label: String,
    onClickIcon: () -> Unit,
    size: Int = 30,
    labelWidth: Float = 0.3f,
    content: @Composable () -> Unit
) {


    Row(
        modifier = Modifier.fillMaxWidth(),

        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(labelWidth)
                .padding(end = 10.dp),
            horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null)
                IconButton(
                    modifier = Modifier.size(size.dp),
                    onClick = {
                        onClickIcon()
                    }, content = {
                        // 图表
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(size.dp),
                        )
                    })
            // 标签
            Text(text = "$label:", textAlign = TextAlign.End)
        }
        Row(modifier = Modifier.fillMaxWidth()) {        // 内容
            content()
        }


    }
}