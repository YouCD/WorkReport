package online.youcd.workreport.ui.screen.welcome

import AllDestinations
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import online.youcd.workreport.R
import online.youcd.workreport.model.DataStoreViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WelcomeScreen(navController: NavHostController) {

    val circleColor = Color(0x9922A640)
    val dataStoreViewModel: DataStoreViewModel = viewModel()


    var visible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        delay(1500)
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .offset(x = -20.dp, y = -60.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)

            ) {
                drawCircle(
                    color = circleColor,
                    radius = size.minDimension / 2,
                    center = Offset(size.width / 2, size.height / 2)
                )
            }
        }


        Box(
            modifier = Modifier
                .offset(x = -60.dp, y = 30.dp)
        ) {

            Canvas(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            ) {
                drawCircle(
                    color = circleColor,
                    radius = size.minDimension / 2,
                    center = Offset(size.width / 2, size.height / 2)
                )
            }
        }



        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(x = 150.dp, y = -20.dp)
                .rotate(30f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.docker),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }

        Box(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .offset(x = -80.dp, y = -50.dp)
                .rotate(-45f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.etcd),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }

        Box(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .offset(x = 80.dp, y = -50.dp)
                .rotate(45f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.istio),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
        }


        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(x = 100.dp, y = 70.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.jenkins),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        }


        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(x = 20.dp, y = 80.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.kubernetes),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }

        Box(modifier = Modifier.offset(x = 300.dp, y = 150.dp)) {
            Image(
                painter = painterResource(id = R.drawable.prometheus1),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
        }



        Box(modifier = Modifier.align(Alignment.Center)) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = visible,
                    enter = scaleIn(
                        animationSpec = tween(
                            2000,
                            easing = FastOutSlowInEasing
                        ),
                    )
                ) {
                    Text(
                        text = "WorkReport",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,

                        )
                }
                AnimatedVisibility(
                    visible = visible,
                    enter = scaleIn(
                        animationSpec = tween(
                            2000,
                            easing = FastOutSlowInEasing
                        ),
                    )

                ) {


                    Image(
                        painter = painterResource(id = R.drawable.devops1), // 替换成您的图片资源
                        contentDescription = null, // 添加图像描述，如果需要


                    )
                }
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInVertically(
                        animationSpec = tween(
                            2000,
                            easing = FastOutSlowInEasing
                        ),
                        initialOffsetY = {// 改变了方向
                            it * 3
                        },
                    )
                ) {
                    // 登录按钮
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF22A640),
                            contentColor = Color.White,
                            disabledContainerColor = Color.White,
                            disabledContentColor = Color.White
                        ),
                        onClick = {
                            dataStoreViewModel.setWelcome("isWelcome")
                            Log.e("登录按钮", "登录按钮")
                            navController.navigate(AllDestinations.LOGIN)
                        },
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

}
