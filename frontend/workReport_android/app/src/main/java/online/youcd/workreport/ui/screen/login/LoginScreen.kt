package online.youcd.workreport.ui.screen.login

import AllDestinations
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import online.youcd.workreport.model.DataStoreViewModel
import online.youcd.workreport.model.LoginViewModel
import online.youcd.workreport.types.User


@Composable
fun LoginScreen(navController: NavController) {
    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }


    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent, // 背景透明
        unfocusedContainerColor = Color.Transparent, // 背景透明
        disabledContainerColor = Color.Transparent, // 背景透明
        disabledTextColor = MaterialTheme.colorScheme.onBackground
    )


    val dataStoreViewModel = DataStoreViewModel()
    dataStoreViewModel.getServer()

    val server by dataStoreViewModel.server.collectAsState()

    val loginViewModel: LoginViewModel = viewModel()

    val loginMsg by loginViewModel.loginMsg.collectAsState()

    val loginFlag by loginViewModel.loginFlag.collectAsState()
    var serverHost by remember { mutableStateOf("") }

    LaunchedEffect(server) {
        if (server.isNotEmpty()) {
            serverHost = server
        }
    }

    LaunchedEffect(loginFlag) {
        if (loginFlag) {
            navController.navigate(AllDestinations.HOME)
        }
    }
    val context = LocalContext.current
    LaunchedEffect(loginMsg) {
        if (loginMsg != null) {
            Toast.makeText(context, loginMsg, Toast.LENGTH_SHORT).show()
        }
    }

    val submit = {
        dataStoreViewModel.setServer(serverHost)
        loginViewModel.login(User(user, password))
    }



    Surface(
        modifier = Modifier
            .background(color = Color(0xFF2A2A2A))
            .fillMaxSize(),
    ) {
        Box(modifier = Modifier.padding(10.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {

                Text(
                    text = "WorkReport",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                // 服务器地址
                TextField(
                    value = serverHost,
                    onValueChange = { serverHost = it },
                    leadingIcon = { Icon(Icons.Default.Api, "服务器地址") },
                    label = { Text(text = "服务器地址") },
                    placeholder = { Text(text = "服务器地址") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    colors = textFieldColors,
                    textStyle = TextStyle(fontSize = 12.sp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
                // 用户名
                TextField(
                    value = user,
                    onValueChange = {
                        user = it
                    },
                    label = {
                        Text(text = "用户名")
                    },
                    leadingIcon = { Icon(Icons.Default.Person, "用户名") },
                    placeholder = { Text(text = "用户名") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    colors = textFieldColors,
                    textStyle = TextStyle(fontSize = 12.sp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
                // 密码

                TextField(
                    leadingIcon = { Icon(Icons.Default.Lock, "密码") },
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = { newText ->
                        password = newText
                    },
                    label = { Text(text = "密码") },
                    placeholder = { Text(text = "密码") },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = textFieldColors,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            showPassword = false
                            submit()
                        }
                    ),
                    trailingIcon = {
                        if (showPassword) {
                            IconButton(onClick = { showPassword = false }) {
                                Icon(
                                    imageVector = Icons.Filled.Visibility,
                                    contentDescription = "hide_password"
                                )
                            }
                        } else {
                            IconButton(
                                onClick = { showPassword = true }) {
                                Icon(
                                    imageVector = Icons.Filled.VisibilityOff,
                                    contentDescription = "hide_password"
                                )
                            }
                        }
                    })

                Spacer(modifier = Modifier.height(10.dp))

                // 登录按钮
                Button(
                    onClick = {
                        submit()
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Login, null)
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = "登录")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
//    LoginScreen()
}
