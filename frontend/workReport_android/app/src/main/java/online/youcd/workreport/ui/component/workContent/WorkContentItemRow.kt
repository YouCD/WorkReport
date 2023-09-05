package online.youcd.workreport.ui.component.workContent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import online.youcd.workreport.tools.dateStr2unixTime
import online.youcd.workreport.tools.unixTime2FormattedDate
import online.youcd.workreport.types.WorkContentItem
import online.youcd.workreport.ui.component.NewDatePicker
import online.youcd.workreport.ui.component.button.NewIconButton

/**
 * 工作内容项行
 * @param [modifier] 修饰符
 * @param [workContentItem] 工作内容项
 * @param [isExpand] 是否展开
 * @param [isEditing] 编辑状态
 */
@Composable
fun WorkContentItemRow(
    modifier: Modifier = Modifier,
    workContentItem: WorkContentItem,
    isExpand: Boolean = false,
    isEditing: Boolean,
    onEditComplete: (WorkContentItem) -> Unit,
    onDateChange: (Int) -> Unit
) {
    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent, // 背景透明
        unfocusedContainerColor = Color.Transparent, // 背景透明
        unfocusedIndicatorColor = Color.Transparent, // 下划线透明
        disabledContainerColor = Color.Transparent, // 背景透明
        disabledTextColor = MaterialTheme.colorScheme.onBackground
    )
    val defaultSize = 40

    val focusRequester = remember { FocusRequester() }
    var workLogContent by remember { mutableStateOf("") }

    var textFieldValueState by remember { mutableStateOf(TextFieldValue(workLogContent)) }

    LaunchedEffect(isEditing) {
        if (isEditing) {
            focusRequester.requestFocus()
            // 点击编辑后 将光标移动到最后
            workLogContent = workContentItem.content
            textFieldValueState = TextFieldValue(workLogContent, TextRange(workLogContent.length))
        }
    }
    var showDatePicker by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.clickable(
                    enabled = isEditing,
                    onClick = { showDatePicker = true }
                ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(12.dp),
                    imageVector = Icons.Default.WatchLater,
                    contentDescription = null
                )
                Text(
                    text = unixTime2FormattedDate(workContentItem.date.toLong()),
                    fontSize = 5.sp
                )
                NewDatePicker(isShow = showDatePicker, onSelect = {
                    onDateChange(dateStr2unixTime(it).toInt())
                }, onShow = {
                    showDatePicker = false
                })
            }

            Spacer(modifier = Modifier.size(10.dp))
            if (!isEditing) {
                Text(
                    text = workContentItem.content,
                    fontSize = 12.sp,
                    maxLines = if (isExpand) 5 else 1, // 如果是展开就将函数设置为5行
                    overflow = if (isExpand) {
                        TextOverflow.Visible
                    } else {
                        TextOverflow.Ellipsis
                    }, // 设置 截断
                    softWrap = isExpand
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = textFieldValueState,
                        onValueChange = {
                            textFieldValueState = it
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        colors = textFieldColors,
                        maxLines = if (isExpand) 5 else 1,
                        enabled = isEditing,
                        textStyle = TextStyle(fontSize = 12.sp),
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .fillMaxWidth(0.8f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    NewIconButton(
                        onClick = {// 编辑后回调
                            val newWorkContentItem = workContentItem.copy(
                                content = textFieldValueState.text
                            )
                            onEditComplete(newWorkContentItem)
                        },
                        iconSize = defaultSize,
                        desc = "完成",
                        iconColor = Color.Green,
                        imageVector = Icons.Filled.Check
                    )
                }
            }
        }
    }
}
