package online.youcd.workreport.ui.component.calendar

import CircleWithText
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/**
 * 日历
 * @param [isWeekMode] 周模式
 * @param [onClickCalendarDay] 点击事件
 */
@Composable
fun Calendar(
    isWeekMode: Boolean,
    onClickCalendarDay: (CalendarDay) -> Unit,
    onClickLocalDate: (LocalDate) -> Unit,
) {
    //    指定星期的开始
    val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY)

    val currentDate = remember { LocalDate.now() }

    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(30) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    var selection by remember { mutableStateOf(currentDate) }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )

    val weekState = rememberWeekCalendarState(
        startDate = startMonth.atStartOfMonth(),
        endDate = endMonth.atEndOfMonth(),
        firstVisibleWeekDate = currentDate,
        firstDayOfWeek = daysOfWeek.first()
    )
    val selections = remember { mutableStateListOf<CalendarDay>() }
    AnimatedVisibility(
        visible = !isWeekMode,
        exit = shrinkVertically(animationSpec = tween(700, easing = FastOutSlowInEasing)),
        enter = expandVertically(animationSpec = tween(700, easing = FastOutSlowInEasing))
    ) {
        HorizontalCalendar(
            modifier = Modifier,
            state = state,
            dayContent = { day ->
                DayContentOfCalendar(
                    day,
                    isSelected = selections.contains(day)
                ) { clicked ->
                    selections.clear() //  清空之前的选择
                    selections.add(clicked)
                    onClickCalendarDay(day)
                }
            },
            monthHeader = { month ->
                val daysOfWeek = month.weekDays.first().map { it.date.dayOfWeek }
                MonthHeader(daysOfWeek = daysOfWeek, state = state)
            }
        )
    }

    AnimatedVisibility(
        visible = isWeekMode,
        exit = shrinkVertically(animationSpec = tween(700, easing = FastOutSlowInEasing)),
        enter = expandVertically(animationSpec = tween(700, easing = FastOutSlowInEasing))

    ) {
        WeekCalendar(
            state = weekState,
            userScrollEnabled = false,
            dayContent = { day ->
                DayContentOfWeek(day.date, isSelected = selection == day.date) { clicked ->
                    if (selection != clicked) {
                        selection = clicked
                        onClickLocalDate(clicked)
                    }
                }
            },
            weekHeader = {
                WeekHeader(state = state, week = it)
            }
        )
    }
}

private val dateFormatter = DateTimeFormatter.ofPattern("dd")

/**
 * 月日历的 表头
 * @param [modifier] 修饰符
 * @param [daysOfWeek]
 * @param [state] 状态
 */
@Composable
private fun MonthHeader(
    modifier: Modifier = Modifier,
    daysOfWeek: List<DayOfWeek> = emptyList(),
    state: CalendarState
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        //            Log.e("上一月","${state.firstVisibleMonth.yearMonth.previousMonth}")
        //            Log.e("下一月","${state.firstVisibleMonth.yearMonth.nextMonth}")
        //            Log.e("当前月","${state.firstVisibleMonth.yearMonth}")
        // 这里要随滑动变更 当前月
        Text(
            text = state.firstVisibleMonth.yearMonth.toString(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
    Row(modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                text = dayOfWeek.getDisplayName(
                    TextStyle.SHORT,
                    Locale.getDefault()
                ),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * 星期日历的 表头
 * @param [modifier] 修饰符
 * @param [week] 周
 * @param [state] 状态
 */
@Composable
private fun WeekHeader(
    modifier: Modifier = Modifier,
    week: Week,
    state: CalendarState
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        //            Log.e("上一月","${state.firstVisibleMonth.yearMonth.previousMonth}")
        //            Log.e("下一月","${state.firstVisibleMonth.yearMonth.nextMonth}")
        //            Log.e("当前月","${state.firstVisibleMonth.yearMonth}")
        Text(
            text = state.firstVisibleMonth.yearMonth.toString(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp

        )
    }
    Row(modifier.fillMaxWidth()) {
        for (dayOfWeek in week.days) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                text = dayOfWeek.date.dayOfWeek.getDisplayName(
                    TextStyle.SHORT,
                    Locale.getDefault()
                ),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * 周 日历的 内容
 * @param [date] 日期
 * @param [isSelected] 被选中
 * @param [onClick] 点击事件
 */
@Composable
private fun DayContentOfWeek(date: LocalDate, isSelected: Boolean, onClick: (LocalDate) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick(date) },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = dateFormatter.format(date),
                fontSize = 14.sp,
                color = if (isSelected) MaterialTheme.colorScheme.onBackground else Color.Gray,
                fontWeight = FontWeight.Bold
            )
        }
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(5.dp)
                    .background(MaterialTheme.colorScheme.surfaceTint)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

/**
 * 月日历 的内容
 * @param [day] 一天
 * @param [isSelected] 被选中
 * @param [onClick] 点击事件
 */
@Composable
fun DayContentOfCalendar(
    day: CalendarDay,
    isSelected: Boolean,
    onClick: (CalendarDay) -> Unit
) {
    val currentDate = LocalDate.now()
    val isToday = day.date.year == currentDate.year &&
            day.date.month == currentDate.month &&
            day.date.dayOfMonth == currentDate.dayOfMonth
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .testTag("MonthDay")
            .padding(6.dp)
            .clip(CircleShape)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.onBackground else Color.Transparent
            )
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) }
            ),
        contentAlignment = Alignment.Center
    ) {
        val textColor = when (day.position) {
            DayPosition.MonthDate -> {
                if (day.position == DayPosition.MonthDate) {
                    if (!isSelected) {
                        MaterialTheme.colorScheme.onBackground
                    } else {
                        Color.White
                    }
                } else {
                    Color.Gray
                }
            }

            DayPosition.InDate, DayPosition.OutDate -> Color.Gray
        }

        if (isToday) {
            CircleWithText(modifier = Modifier, day.date.dayOfMonth.toString())
        } else {
            Text(
                text = day.date.dayOfMonth.toString(),
                color = textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
