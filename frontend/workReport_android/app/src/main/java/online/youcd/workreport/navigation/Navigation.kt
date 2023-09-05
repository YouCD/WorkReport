package online.youcd.workreport.navigation

import AllDestinations
import AppDrawer
import AppNavigationActions
import ChartScreen
import HomeScreen
import SettingsScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import online.youcd.workreport.api.RetrofitUtil
import online.youcd.workreport.model.WorkLogViewModel
import online.youcd.workreport.store.Store
import online.youcd.workreport.ui.component.form.AddWorkLogForm
import online.youcd.workreport.ui.screen.login.LoginScreen
import online.youcd.workreport.ui.screen.search.SearchScreen
import online.youcd.workreport.ui.screen.welcome.WelcomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
) {


    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    //  路由配置
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: AllDestinations.HOME
    val navigationActions = remember(navController) {
        AppNavigationActions(navController)
    }
    val screenWidth = LocalContext.current.resources.configuration.screenWidthDp.dp
    val quarterScreenWidth = screenWidth / 2

    var showSearch by remember { mutableStateOf(false) } // 显示搜索
    var searchContent by remember { mutableStateOf("") } // 搜索关键字

    Store.initialize(LocalContext.current) // 初始化本地存储


    val workLogViewModel = WorkLogViewModel()


    // 判断是否需要进行登录检查
    ModalNavigationDrawer(
        drawerContent = {
            if (!excludeRouter(currentRoute)) {
                Box(modifier = Modifier.width(quarterScreenWidth)) {
                    AppDrawer(
                        route = currentRoute,
                        navigateToHome = { navigationActions.navigateToHome() },
                        navigateToSettings = { navigationActions.navigateToSettings() },
                        navigateToChart = { navigationActions.navigateToChart() },
                        closeDrawer = { coroutineScope.launch { drawerState.close() } },
                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    )
                }
            }
        },
        drawerState = drawerState,
    ) {
        Scaffold(
            topBar = {
                if (!excludeRouter(currentRoute)) {
                    TopAppBar(
                        title = { Text(text = currentRoute) },
                        modifier = Modifier.fillMaxWidth(),
                        navigationIcon = {
                            IconButton(
                                onClick = { coroutineScope.launch { drawerState.open() } },
                                content = {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = null
                                    )
                                }
                            )
                        },
                        colors = topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            scrolledContainerColor = MaterialTheme.colorScheme.background
                        ),
                        actions = {
                            when (currentRoute) {
                                AllDestinations.HOME -> {
                                    HomeTopABar(
                                        showSearch = showSearch,
                                        str = searchContent,
                                        onClickSearchIcon = { showSearch = !showSearch },
                                        onInputChange = { searchContent = it },
                                        onSearch = {
                                            workLogViewModel.searchWorkLog(searchContent)
                                            navigationActions.navigateToSearch()//  进入搜索页
                                        },
                                        onClickAddIcon = { navigationActions.navigateToAddWorkLog() }
                                    )
                                }
                            }
                        }
                    )
                }
            },
            modifier = Modifier
        ) {
            NavHost(
                navController = navController,
                startDestination = when (RetrofitUtil.firstScreen()) {
                    AllDestinations.Welcome -> AllDestinations.Welcome
                    AllDestinations.LOGIN -> if (RetrofitUtil.jwtIsExpired()) {
                        // 如果需要登录检查，则将起始目标设置为登录页
                        AllDestinations.LOGIN
                    } else {
                        // 否则将其设置为首页
                        AllDestinations.HOME
                    }

                    else -> AllDestinations.HOME

                },

                modifier = modifier
                    .padding(it)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                composable(AllDestinations.HOME) {
                    HomeScreen()
                }

                composable(AllDestinations.SETTINGS) {
                    SettingsScreen()
                }

                composable(AllDestinations.CHART) {
                    ChartScreen()
                }
                composable(AllDestinations.AddWorkLog) {
                    AddWorkLogForm()
                }

                composable(AllDestinations.Search) {
                    SearchScreen(workLogViewModel)
                }
                composable(AllDestinations.LOGIN) {
                    LoginScreen(navController)
                }
                composable(AllDestinations.Welcome) {
                    WelcomeScreen(navController)
                }

            }
        }
    }
}

@Composable
private fun excludeRouter(currentRoute: String): Boolean {
    return (currentRoute == AllDestinations.LOGIN) || (currentRoute == AllDestinations.Welcome)
}
