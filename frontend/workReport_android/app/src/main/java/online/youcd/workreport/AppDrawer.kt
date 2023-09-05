import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import online.youcd.workreport.R

@Composable
fun AppDrawer(
    route: String,
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
    navigateToChart: () -> Unit = {},
    closeDrawer: () -> Unit = {}
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.background

    ) {
        DrawerHeader(modifier)
        Column(
            modifier = modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            NavigationDrawerItem(
                label = {
                    Text(
                        text = stringResource(id = R.string.home),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                selected = route == AllDestinations.HOME,
                onClick = {
                    navigateToHome()
                    closeDrawer()
                },
                icon = { Icon(imageVector = Icons.Default.Chat, contentDescription = null) },
                shape = MaterialTheme.shapes.small
            )
            Divider(color = MaterialTheme.colorScheme.onSecondary)
            NavigationDrawerItem(
                label = {
                    Text(
                        text = stringResource(id = R.string.settings),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                selected = route == AllDestinations.SETTINGS,
                onClick = {
                    navigateToSettings()
                    closeDrawer()
                },
                icon = { Icon(imageVector = Icons.Default.Settings, contentDescription = null) },
                shape = MaterialTheme.shapes.small
            )
            Divider(color = MaterialTheme.colorScheme.onSecondary)
            NavigationDrawerItem(
                label = {
                    Text(
                        text = stringResource(id = R.string.chart),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                selected = route == AllDestinations.CHART,
                onClick = {
                    navigateToChart()
                    closeDrawer()
                },
                icon = { Icon(imageVector = Icons.Default.Equalizer, contentDescription = null) },
                shape = MaterialTheme.shapes.small
            )
            Divider(color = MaterialTheme.colorScheme.onSecondary)
        }
    }
}

/**
 * 抽屉 头
 */
@Composable
fun DrawerHeader(modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.header_padding))
            .fillMaxWidth()
    ) {
        Image(
            painterResource(id = R.drawable.prometheus),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = modifier
                .size(90.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.spacer_padding)))

        Text(
            text = stringResource(id = R.string.app_name),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview
@Composable
fun DrawerHeaderPreview() {
    AppDrawer(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        route = AllDestinations.HOME
    )
}
