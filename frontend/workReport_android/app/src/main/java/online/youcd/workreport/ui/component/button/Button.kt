package online.youcd.workreport.ui.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * 新图标按钮
 * @param [onClick] 在点击
 * @param [iconSize] 图标大小
 * @param [background] 背景
 * @param [desc] 描述
 * @param [iconColor] 图标颜色
 */
@Composable
fun NewIconButton(
    onClick: () -> Unit,
    iconSize: Int = 40,
    background: Color = Color.Transparent,
    desc: String,
    iconColor: Color,
    imageVector: ImageVector
) {
    IconButton(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .clip(CircleShape)
            .background(background)
            .size(iconSize.dp)
    ) {
        Icon(
            imageVector,
            contentDescription = desc,
            tint = iconColor
        )
    }
}
