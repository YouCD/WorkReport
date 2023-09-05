package online.youcd.workreport.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DefaultCard(modifier: Modifier, content: @Composable () -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = defaultPadding, vertical = defaultPadding),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
//                if (isSelected) MaterialTheme.colorScheme.primaryContainer
//                else
//                    MaterialTheme.colorScheme.background,
//                contentColor = MaterialTheme.colorScheme.background,
        ), // 颜色

        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        content = {
            content()
        }

    )
}
