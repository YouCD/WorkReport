package online.youcd.workreport.ui.component

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun NewToast(context: Context = LocalContext.current, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}
