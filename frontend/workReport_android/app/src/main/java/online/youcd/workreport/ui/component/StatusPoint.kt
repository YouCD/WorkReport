package online.youcd.workreport.ui.component

import NewCircle
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatusPoint(modifier: Modifier, count: Int, activeIndex: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = defaultPadding),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(count) {
            NewCircle(modifier.size(10.dp), isSystemInDarkTheme(), activeIndex == it)
            Spacer(Modifier.width(10.dp))
        }
    }
}
