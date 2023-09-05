import AllDestinations.AddWorkLog
import AllDestinations.CHART
import AllDestinations.HOME
import AllDestinations.LOGIN
import AllDestinations.SETTINGS
import AllDestinations.Search
import AllDestinations.Welcome
import androidx.navigation.NavHostController

object AllDestinations {
    const val CHART = "图表"
    const val HOME = "日志浏览"
    const val SETTINGS = "字典设置"
    const val AddWorkLog = "添加工作日志"
    const val Search = "搜索结果"
    const val LOGIN = "登入"
    const val Welcome = "Welcome"
}

class AppNavigationActions(private val navController: NavHostController) {

    fun navigateToHome() {
        navController.navigate(HOME) {
            popUpTo(HOME)
        }
    }

    fun navigateToSettings() {
        navController.navigate(SETTINGS) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToChart() {
        navController.navigate(CHART) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToAddWorkLog() {
        navController.navigate(AddWorkLog) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToSearch() {
        navController.navigate(Search) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToLogin() {
        navController.navigate(LOGIN) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToWelcome() {
        navController.navigate(Welcome) {
            launchSingleTop = true
            restoreState = true
        }
    }


}
