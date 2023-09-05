package online.youcd.workreport.tools

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * unix时间戳格式日期
 * @param [unixTimestamp] unix时间戳
 * @return [String]
 */
fun unixTime2FormattedDate(unixTimestamp: Long): String {
    val instant = Instant.ofEpochSecond(unixTimestamp)
    val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

    val formatter = DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH)
    return dateTime.format(formatter)
}

/**
 * 日期str2unix时间
 * @param [dateString] 日期字符串
 * @return [Long]
 */
fun dateStr2unixTime(dateString: String): Long {
    val dateFormat = SimpleDateFormat("yyyy/M/d", Locale.getDefault())
    val date = dateFormat.parse(dateString)
    return date.time / 1000
}

fun getCurrentDateInChina(): String {
    val sdf = SimpleDateFormat("yyyy/M/d", Locale.getDefault())
    val timeZone = TimeZone.getTimeZone("Asia/Shanghai") // 设置时区为中国（上海）
    sdf.timeZone = timeZone
    val currentDate = Date()
    return sdf.format(currentDate)
}