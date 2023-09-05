package online.youcd.workreport.ui.component

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

val type1ColorMap = mapOf(
    "运维" to Color(0xFF2196F3),
    "开发" to Color(0xFFFFC300),
    "其他" to Color(0xFFFF683B),
    "技术支持" to Color(0xFF3BFF49),
    "项目管理" to Color(0xFF6E1BFF),
    "平台开发" to Color(0xFFE80054)
)

/**
 * 生成随机颜色
 */
fun randomColor(): Color {
    val red = Random.nextInt(256)
    val green = Random.nextInt(256)
    val blue = Random.nextInt(256)
    return Color(red, green, blue)
}
