@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package online.youcd.workreport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import online.youcd.workreport.navigation.AppNavGraph
import online.youcd.workreport.ui.theme.WorkReportTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkReportTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {

                AppNavGraph()
//                }
            }
        }
    }
}
