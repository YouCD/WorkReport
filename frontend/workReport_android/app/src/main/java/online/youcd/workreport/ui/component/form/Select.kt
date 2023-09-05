package online.youcd.workreport.ui.component.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.unit.dp

@Composable
fun <T> Select(
    label: String,
    items: List<T>,
    dropdownMenuItem: @Composable (T) -> Unit,
    onSelect: (T) -> Unit,
    selectedItem: String
) {
//    var selectedValue by remember { mutableStateOf<T?>(null) }
    var isSimpleDropDownExpanded by remember { mutableStateOf(false) }
    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent, // 背景透明
        unfocusedContainerColor = Color.Transparent, // 背景透明
        unfocusedIndicatorColor = Color.Transparent, // 下划线透明
//        focusedIndicatorColor = Color.Transparent, // 下划线透明
        disabledContainerColor = Color.Transparent, // 背景透明
        disabledTextColor = MaterialTheme.colorScheme.onBackground
    )
    BoxWithConstraints {
        val with = maxWidth.value
        TextField(
            value = selectedItem,
            onValueChange = { },
            placeholder = { Text(text = label) },
            enabled = false,
            modifier = Modifier
                .clickable {
                    isSimpleDropDownExpanded = true
                }
                .fillMaxWidth(1f),
//            textStyle = TextStyle(color = Color.Black),
            colors = textFieldColors,
        )

        DropdownMenu(
            expanded = isSimpleDropDownExpanded,
            onDismissRequest = { isSimpleDropDownExpanded = false },
            modifier = Modifier.width(with.dp)
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = {
                        dropdownMenuItem(it)
                    },
                    onClick = {
//                        selectedValue = it
                        onSelect(it)
                        isSimpleDropDownExpanded = false
//                        expanded.value = false
//                        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}
