package online.youcd.workreport.navigation

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import online.youcd.workreport.ui.component.form.AnimatedRowSearch

@Composable
fun HomeTopABar(
    showSearch: Boolean,
    str: String,
    onInputChange: (String) -> Unit,
    onClickSearchIcon: () -> Unit,
    onSearch: () -> Unit,
    onClickAddIcon: () -> Unit
) {

    AnimatedRowSearch(
        onClickIcon = {
            onClickSearchIcon()
        },
        onInputChange = {
            onInputChange(it)
        },
        str = str,
        size = 25,
        onSearch = {
            onSearch()
        },
        showSearch = showSearch,
    )


    IconButton(
        onClick = {
            onClickAddIcon()
        }, content = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        })
}