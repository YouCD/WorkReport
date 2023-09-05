package online.youcd.workreport.ui.component.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp


@Composable
fun FormRowInput(
    label: String = "",
    placeholder: String = label,
    labelWidth: Float = 0.3f,
    icon: ImageVector? = null,
    singleLine: Boolean = false,
    onInputChange: (String) -> Unit = {},
    onClickIcon: () -> Unit = {},
    str: String,
) {

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent, // 背景透明
        unfocusedContainerColor = Color.Transparent, // 背景透明
        disabledContainerColor = Color.Transparent, // 背景透明
        disabledTextColor = MaterialTheme.colorScheme.onBackground
    )

    FormRowLayout(
        icon = icon,
        label = label,
        labelWidth = labelWidth,
        onClickIcon = {
            onClickIcon()
        },
    )
    {

        TextField(
            value = str,
            onValueChange = {
                onInputChange(it)
            },

            placeholder = { Text(text = placeholder) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            colors = textFieldColors,
            textStyle = TextStyle(fontSize = 12.sp),
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = singleLine,
        )
    }

}

