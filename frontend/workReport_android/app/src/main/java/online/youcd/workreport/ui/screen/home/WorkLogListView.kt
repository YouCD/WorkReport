package online.youcd.workreport.ui.screen.home

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import online.youcd.workreport.model.WorkLogViewModel
import online.youcd.workreport.types.ModifyItem
import online.youcd.workreport.types.WorkContentItem
import online.youcd.workreport.ui.component.swipeAction.SwipeAbleItemCell
import online.youcd.workreport.ui.component.swipeAction.SwipeDirection
import online.youcd.workreport.ui.component.workContent.WorkContentItemRow

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun WorkLogListView(
    pullRefreshState: PullRefreshState,
    onClickDay: Boolean,
    workContentItemList: List<WorkContentItem>?,
    collectAsLazyPagingItems: LazyPagingItems<WorkContentItem>,
    isRefreshing: Boolean
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
            content = {

                if (onClickDay) {
                    items(workContentItemList?.size ?: 0) {
                        // 列表动画
                        val animatedProgress = remember { Animatable(initialValue = 0f) }
                        LaunchedEffect(Unit) {
                            animatedProgress.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(700, easing = FastOutSlowInEasing)
                            )
                        }
                        val content = workContentItemList?.get(it)
                        if (content != null) {
                            SwipeAbleItemCellWithWorkContent(
                                Modifier.graphicsLayer(alpha = animatedProgress.value),
                                content
                            )
                        }
                    }
                } else {
                    items(collectAsLazyPagingItems.itemCount) {
                        val animatedProgress = remember { Animatable(initialValue = 0f) }
                        LaunchedEffect(Unit) {
                            animatedProgress.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(700, easing = FastOutSlowInEasing)
                            )
                        }
                        val content = collectAsLazyPagingItems[it]
                        if (content != null) {
                            SwipeAbleItemCellWithWorkContent(
                                Modifier.graphicsLayer(alpha = animatedProgress.value),
                                content
                            )
                        }
                    }
                }

            }
        )
        PullRefreshIndicator(
            isRefreshing, pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
        )
    }
}

/**
 * 可以滑动的 工作内容组件
 * @param [workLog] 工作日志
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeAbleItemCellWithWorkContent(
    modifier: Modifier = Modifier,
    workLog: WorkContentItem
) {
    var isClickRow by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }
    var dateInt by remember { mutableStateOf(0) }
//    val modifier by remember { mutableStateOf(Modifier.padding(5.dp)) }
    val workLogViewModel: WorkLogViewModel = viewModel()
    var w by remember {
        mutableStateOf(
            ModifyItem(
                workLog.content,
                workLog.type1_id,
                workLog.type2_id,
                workLog.id,
                workLog.date
            )
        )
    }

    SwipeAbleItemCell(
        swipeDirection = SwipeDirection.RIGHT,
        onEditClick = {
            isEditing = true // 置为编辑状态
            isClickRow = true // 设置行点击的标志位
        },
        onDeleteClicked = {
            //  删除数据
            Log.e("删除", "${workLog.id}")
            workLogViewModel.deleteWorkLog(workLog.id)
        },
        onClickRow = {
            // 当前是 编辑状态则 不允许切换状态
            if (!isEditing) {
                isClickRow = !isClickRow
            }
        },
        isEditing = isEditing
    ) {
        WorkContentItemRow(
            modifier = modifier.padding(5.dp),
            workContentItem = workLog,
            isExpand = isClickRow,
            isEditing = isEditing,
            onEditComplete = {
                isEditing = false
                //  修改提交数据
                w.content = it.content
                w.type1 = it.type1_id
                w.type2 = it.type2_id
                w.id = it.id
                if (dateInt == 0) w.date=it.date else w.date=dateInt
                workLogViewModel.modifyWorkLog(w)
            },
            onDateChange = {
                dateInt = it
            }
        )
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onBackground)
        )
    }
}
