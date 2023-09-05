package online.youcd.workreport.ui.component.swipeAction

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import online.youcd.workreport.ui.component.button.NewIconButton
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun OnChangedEffect(key: Any?, block: suspend () -> Unit) {
    var launched by remember { mutableStateOf(false) }
    LaunchedEffect(key) {
        if (!launched) {
            launched = true
        } else {
            block()
        }
    }
}

/**
 * 滑动组件
 * @param [onEditClick] 编辑事件
 * @param [onDeleteClicked] 删除事件
 * @param [onClickRow] 行点击事件
 * @param [swipeDirection] 滑动方向
 * @param [content] 内容
 */
@ExperimentalMaterialApi
@Composable
fun SwipeAbleItemCell(
    onEditClick: () -> (Unit),
    onDeleteClicked: () -> (Unit),
    onClickRow: () -> Unit = {},
    swipeDirection: SwipeDirection,
    isEditing: Boolean,
    content: @Composable () -> Unit

) {
    val squareSize = if (swipeDirection == SwipeDirection.BOTH) 60.dp else 120.dp
    val swipeAbleState = rememberSwipeableState(initialValue = 0)
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = when (swipeDirection) {
        SwipeDirection.LEFT -> mapOf(0f to 0, -sizePx to 1)
        SwipeDirection.RIGHT -> mapOf(0f to 0, sizePx to 1)
        else -> mapOf(0f to 0, sizePx to 1, -sizePx to 2)
    }
    val cornerShape = RoundedCornerShape(10.dp)
    val defaultSize = 40

    // 点击编辑按钮后 要重置 滑动状态
    var recoverSwipe by remember { mutableStateOf(false) }

    OnChangedEffect(recoverSwipe) {
        if (recoverSwipe) {
            // 点击编辑按钮后 要重置 滑动状态
            swipeAbleState.animateTo(0)
            recoverSwipe = false
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .swipeable(
                        state = swipeAbleState,
                        anchors = anchors,
                        thresholds = { _, _ ->
                            FractionalThreshold(0.3f)
                        },
                        orientation = Orientation.Horizontal,
                        enabled = !isEditing // 如果正在编辑 不允许滑动
                    )
            ) {
                Box(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Row(
                        horizontalArrangement = when (swipeDirection) {
                            SwipeDirection.BOTH -> Arrangement.SpaceBetween
                            SwipeDirection.LEFT -> Arrangement.End
                            else -> Arrangement.Start
                        },
                        modifier = Modifier.fillMaxWidth(),

                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NewIconButton(
                            onClick = {
                                recoverSwipe = true // 点击编辑后
                                onEditClick()
                            },
                            iconSize = defaultSize,
                            desc = "编辑",
                            iconColor = MaterialTheme.colorScheme.primary,
                            imageVector = Icons.Filled.Edit,
                            background = MaterialTheme.colorScheme.primaryContainer
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        NewIconButton(
                            onClick = {
                                recoverSwipe = true
                                onDeleteClicked()
                            },
                            iconSize = defaultSize,
                            desc = "删除",
                            iconColor = MaterialTheme.colorScheme.error,
                            imageVector = Icons.Filled.Delete,
                            background = MaterialTheme.colorScheme.errorContainer
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .offset {
                            IntOffset(swipeAbleState.offset.value.roundToInt(), 0)
                        }
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background, shape = cornerShape)
                        .clickable(onClick = { onClickRow() }) // 添加点击事件
                ) {
                    content()
                }
            }
        }
    }
}

enum class SwipeDirection {
    LEFT, RIGHT, BOTH
}

private fun Modifier.swipeToDismiss(
    onDismissed: () -> Unit
): Modifier = composed {
    // This `Animatable` stores the horizontal offset for the element.
    val offsetX = remember { Animatable(0f) }
    pointerInput(Unit) {
        // Used to calculate a settling position of a fling animation.
        val decay = splineBasedDecay<Float>(this)
        // Wrap in a coroutine scope to use suspend functions for touch events and animation.
        coroutineScope {
            while (true) {
                // Wait for a touch down event.
                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                // Interrupt any ongoing animation.
                offsetX.stop()
                // Prepare for drag events and record velocity of a fling.
                val velocityTracker = VelocityTracker()
                // Wait for drag events.
                awaitPointerEventScope {
                    horizontalDrag(pointerId) { change ->
                        // Record the position after offset
                        val horizontalDragOffset = offsetX.value + change.positionChange().x
                        launch {
                            // Overwrite the `Animatable` value while the element is dragged.
                            offsetX.snapTo(horizontalDragOffset)
                        }
                        // Record the velocity of the drag.
                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                        // Consume the gesture event, not passed to external
                        if (change.positionChange() != Offset.Zero) change.consume()
                    }
                }
                // Dragging finished. Calculate the velocity of the fling.
                val velocity = velocityTracker.calculateVelocity().x
                // Calculate where the element eventually settles after the fling animation.
                val targetOffsetX = decay.calculateTargetValue(offsetX.value, velocity)
                // The animation should end as soon as it reaches these bounds.
                offsetX.updateBounds(
                    lowerBound = -size.width.toFloat(),
                    upperBound = size.width.toFloat()
                )
                launch {
                    if (targetOffsetX.absoluteValue <= size.width) {
                        // Not enough velocity; Slide back to the default position.
                        offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                    } else {
                        // Enough velocity to slide away the element to the edge.
                        offsetX.animateDecay(velocity, decay)
                        // The element was swiped away.
                        onDismissed()
                    }
                }
            }
        }
    }
        // Apply the horizontal offset to the element.
        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
}
