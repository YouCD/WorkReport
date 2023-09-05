import android.widget.Toast
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import online.youcd.workreport.model.ActionDesc
import online.youcd.workreport.model.WorkLogFromDateViewModel
import online.youcd.workreport.model.WorkLogViewModel
import online.youcd.workreport.tools.dateStr2unixTime
import online.youcd.workreport.ui.component.DefaultCard
import online.youcd.workreport.ui.component.calendar.Calendar
import online.youcd.workreport.ui.screen.home.WorkLogListView
import java.time.ZoneId

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen() {
    val workLogViewModel: WorkLogViewModel = viewModel()
    val collectAsLazyPagingItems = workLogViewModel.workLog.collectAsLazyPagingItems()

    val workLogFromDateViewModel: WorkLogFromDateViewModel = viewModel()
    val workContentItemList by workLogFromDateViewModel.workContentItemList.collectAsState()

    //  点击日期的事件
    var onClickDay by remember { mutableStateOf(false) }

    var isRefreshing by remember { mutableStateOf(false) }
    var isWeekMode by remember { mutableStateOf(true) }


    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            collectAsLazyPagingItems.refresh()
        }
    )
    //  如果已经加载完成，则不显示刷新控件
    var showCircularProgressIndicator by remember { mutableStateOf(true) }
    LaunchedEffect(collectAsLazyPagingItems.loadState) {
        if (collectAsLazyPagingItems.loadState.refresh is LoadState.NotLoading) {
            showCircularProgressIndicator = false
        }
    }

    // 修改完成
    val actionMsg by workLogViewModel.actionMsg.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(actionMsg) {// 使用 LaunchedEffect 来确保只在 showModifySuccessToast 发生变化时触发一次
        if (actionMsg != null) {
            when (actionMsg!!.desc) {
                ActionDesc.ADD -> Toast.makeText(context, actionMsg!!.msg, Toast.LENGTH_SHORT)
                    .show()

                ActionDesc.MODIFY -> Toast.makeText(context, actionMsg!!.msg, Toast.LENGTH_SHORT)
                    .show()

                ActionDesc.DELETE -> Toast.makeText(context, actionMsg!!.msg, Toast.LENGTH_SHORT)
                    .show()
            }
            isRefreshing = true
            collectAsLazyPagingItems.refresh()
            isRefreshing = false
        }
    }


    // 日历视图切换
    val calendarModeState = rememberDraggableState { dy ->
        if (dy > 0) { //   在这里处理滑动事件  Log.e("向下拖动", "$dy")
            isWeekMode = false
        } else if (dy < 0) { //            Log.e("向上拖动", "$dy")
            isWeekMode = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.outlineVariant)
            .pullRefresh(pullRefreshState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(5.dp)) // 添加垂直间距
        DefaultCard(
            modifier = Modifier
                .weight(if (isWeekMode) 0.4f else 1.8f)
                .draggable(
                    orientation = Orientation.Vertical,
                    state = calendarModeState
                )
        ) {
            DisposableEffect(calendarModeState) {
                onDispose {}// 清理工作，如果需要的话
            }
            // 默认 周视图
            Calendar(
                isWeekMode,
                onClickCalendarDay = {
                    val unixTimestamp =
                        it.date.atStartOfDay(ZoneId.of("Asia/Shanghai")).toEpochSecond() // 设置时区
                    onClickDay = true
                    isRefreshing = true
                    workLogFromDateViewModel.getWorkLogFromDate(unixTimestamp.toInt()) // 获取当天的数据
                    isRefreshing = false
                },
                onClickLocalDate = {
                    val unixTimestamp =
                        dateStr2unixTime("${it.year}/${it.monthValue}/${it.dayOfMonth}")
                    onClickDay = true
                    isRefreshing = true
                    workLogFromDateViewModel.getWorkLogFromDate(unixTimestamp.toInt()) // 获取当天的数据
                    isRefreshing = false
                }
            )
        }
        DefaultCard(
            modifier = Modifier.weight(1f),
            content = {
                Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    if (showCircularProgressIndicator) { //  如果已经加载完成，则不显示刷新控件
                        Box(modifier = Modifier.align(Alignment.Center)) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }

                    // 工作日志列表视图
                    WorkLogListView(
                        pullRefreshState,
                        onClickDay,
                        workContentItemList,
                        collectAsLazyPagingItems,
                        isRefreshing
                    )
                }

            }
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
