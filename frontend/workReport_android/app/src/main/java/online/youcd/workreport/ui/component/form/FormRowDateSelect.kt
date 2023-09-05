package online.youcd.workreport.ui.component.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import online.youcd.workreport.tools.getCurrentDateInChina
import online.youcd.workreport.ui.component.NewDatePicker


@Composable
fun FormRowDateSelect(
    labelWidth: Float = 0.3f,
    onSelect: (String) -> Unit,
) {

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent, // 背景透明
        unfocusedContainerColor = Color.Transparent, // 背景透明
        disabledContainerColor = Color.Transparent, // 背景透明
        disabledTextColor = MaterialTheme.colorScheme.onBackground
    )

    var showDatePicker by remember { mutableStateOf(false) }

    var dateStr by remember { mutableStateOf(getCurrentDateInChina()) }
    FormRowLayout(
        icon = Icons.Default.CalendarMonth,
        label = "日期",
        onClickIcon = {
            showDatePicker = true
        },
        labelWidth = labelWidth,
    )
    {
        NewDatePicker(isShow = showDatePicker, onSelect = {
            dateStr = it
            onSelect(it)
        }, onShow = {
            showDatePicker = false
        })


        TextField(
            value = dateStr,
            onValueChange = {},
            enabled = false,
            placeholder = { Text(text = "选择日期") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            colors = textFieldColors,
            textStyle = TextStyle(fontSize = 12.sp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showDatePicker = true
                }
        )


    }

}

@Preview(showBackground = true)
@Composable
fun PreviewFormRowDateSelect() {
    FormRowDateSelect(onSelect = {})
}
