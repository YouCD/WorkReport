package online.youcd.workreport.ui.screen.search

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import online.youcd.workreport.model.WorkLogViewModel
import online.youcd.workreport.ui.component.DefaultCard
import online.youcd.workreport.ui.screen.home.SwipeAbleItemCellWithWorkContent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchScreen(
    workLogViewModel: WorkLogViewModel
) {

    val searchWorkLogData by workLogViewModel.searchWorkLogData.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {

        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.outlineVariant)
            .pullRefresh(pullRefreshState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefaultCard(
            modifier = Modifier.weight(1f),
            content = {
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
                            items(searchWorkLogData?.size ?: 0) {
                                // 列表动画
                                val animatedProgress = remember { Animatable(initialValue = 0f) }
                                LaunchedEffect(Unit) {
                                    animatedProgress.animateTo(
                                        targetValue = 1f,
                                        animationSpec = tween(700, easing = FastOutSlowInEasing)
                                    )
                                }
                                val content = searchWorkLogData?.get(it)
                                if (content != null) {
                                    SwipeAbleItemCellWithWorkContent(
                                        Modifier.graphicsLayer(alpha = animatedProgress.value),
                                        content
                                    )
                                }
                            }
                        })
                }

            })
    }
}