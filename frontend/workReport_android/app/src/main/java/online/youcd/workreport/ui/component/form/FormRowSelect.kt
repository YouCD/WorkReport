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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@Composable
fun <T> FormRowSelect(
    label: String = "",
    placeholder: String = label,
    labelWidth: Float = 0.3f,
    icon: ImageVector? = null,
    onClickIcon: () -> Unit = {},
    items: List<T>,
    dropdownMenuItem: @Composable (T) -> Unit,
    onSelect: (T) -> Unit,
    selectedItem: String
) {

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent, // 背景透明
        unfocusedContainerColor = Color.Transparent, // 背景透明
        disabledContainerColor = Color.Transparent, // 背景透明
        disabledTextColor = MaterialTheme.colorScheme.onBackground
    )
    var isSimpleDropDownExpanded by remember { mutableStateOf(false) }

    FormRowLayout(
        icon = icon,
        label = label,
        labelWidth = labelWidth,
        onClickIcon = {
            onClickIcon()
        },
    )
    {

        BoxWithConstraints {
            val with = maxWidth.value
            TextField(
                value = selectedItem,
                onValueChange = { },
                placeholder = { Text(text = placeholder) },
                enabled = false,
                modifier = Modifier
                    .clickable {
                        isSimpleDropDownExpanded = true
                    }
                    .fillMaxWidth(1f),
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
                            onSelect(it)
                            isSimpleDropDownExpanded = false
                        }
                    )
                }
            }
        }
    }

}



