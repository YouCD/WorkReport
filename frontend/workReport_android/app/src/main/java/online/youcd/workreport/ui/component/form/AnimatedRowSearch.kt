package online.youcd.workreport.ui.component.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun AnimatedRowSearch(
    placeholder: String = "输入搜索内容",
    onInputChange: (String) -> Unit = {},
    onClickIcon: () -> Unit = {},
    onSearch: () -> Unit = {},
    showSearch: Boolean = true,
    str: String,
    size: Int = 30,
) {

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent, // 背景透明
        unfocusedContainerColor = Color.Transparent, // 背景透明
        disabledContainerColor = Color.Transparent, // 背景透明
        disabledTextColor = MaterialTheme.colorScheme.onBackground
    )
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(showSearch) {
        if (showSearch) {
            focusRequester.requestFocus()
        }
    }

    Row(
        modifier = if (showSearch) Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 10.dp
            ) else Modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    ) {


        AnimatedVisibility(visible = showSearch) {
            TextField(
                value = str,
                onValueChange = {
                    onInputChange(it)
                },

                placeholder = { Text(text = placeholder) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearch()
                    }
                ),
                colors = textFieldColors,
                textStyle = TextStyle(fontSize = 12.sp),
                modifier = Modifier
                    .focusRequester(focusRequester),
                singleLine = true,
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
        IconButton(
            modifier = Modifier
                .size(size.dp)
                .align(alignment = Alignment.CenterVertically),
            onClick = {
                onClickIcon()
            }, content = {
                Icon(
                    imageVector = if (!showSearch) Icons.Default.Search else Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier.size(size.dp),
                )
            })
    }
}
