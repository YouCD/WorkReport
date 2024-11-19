import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.bonsai.core.Bonsai
import cafe.adriel.bonsai.core.BonsaiStyle
import cafe.adriel.bonsai.core.tree.Tree
import kotlinx.coroutines.delay
import online.youcd.workreport.model.AddTypeViewModel
import online.youcd.workreport.model.TreeDataViewModel
import online.youcd.workreport.model.TypeListViewModel
import online.youcd.workreport.types.AddTypeItem
import online.youcd.workreport.types.Typ
import online.youcd.workreport.ui.component.DefaultCard
import online.youcd.workreport.ui.component.NewToast
import online.youcd.workreport.ui.component.StatusPoint
import online.youcd.workreport.ui.component.defaultPadding
import online.youcd.workreport.ui.component.form.Select

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}

@Composable
fun SettingsScreen() {
    val viewModel: TypeListViewModel = viewModel()
    val data by viewModel.type1ListData.collectAsState()
    val treeDataViewModel: TreeDataViewModel = viewModel()
    val treeData by treeDataViewModel.treeData.collectAsState()
    var pageIndex by remember { mutableStateOf(0) }
    if (data != null) {
        treeDataViewModel.genData(data!!)
    }

    // 使用 LaunchedEffect 在数据变化时发起网络请求
    viewModel.getWorkType1ByPid()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.outlineVariant), // 颜色
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefaultCard(
            modifier = Modifier.weight(1f),
            content = {
                Box(modifier = Modifier.padding(defaultPadding)) {
                    if (treeData != null) {
                        TypeTree(treeData!!)
                    }
                }
            }
        )

        DefaultCard(
            modifier = Modifier.weight(1f),
            content = {
                Column {
                    StatusPoint(
                        modifier = Modifier,
                        count = 2,
                        activeIndex = pageIndex
                    )
                    AddTypePage { index ->
                        pageIndex = index
                    }
                }
            }
        )
    }
}

@Composable
fun <T> TypeTree(tree: Tree<T>) {
    Bonsai(
        tree = tree,
        onClick = { node ->
            tree.clearSelection()
            tree.toggleExpansion(node)
        },
        style = BonsaiStyle(
            nodeNameTextStyle = TextStyle(MaterialTheme.colorScheme.onBackground), // 颜色
            toggleIconColorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
        )
    )
}

/**
 * 添加 type 的输入组
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddTypePage(onChange: (page: Int) -> Unit) {
    val viewModel: TypeListViewModel = viewModel()
    val data by viewModel.type1ListData.collectAsState()

    // 使用 LaunchedEffect 在数据变化时发起网络请求
    viewModel.getWorkType1ByPid()
    val pagerState = rememberPagerState{ 2 }
//    val animatedPage by animateIntAsState(pagerState.currentPage, label = "")

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

    ) {
        onChange(pagerState.currentPage)
        InputBoxGroup(pagerState.currentPage, selectedData = data ?: emptyList())
    }
}

/**
 * 输入框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputBoxGroup(
    id: Int,
    selectedData: List<Typ>,
) {
    var type1Str by remember { mutableStateOf("") }
    var type1Obj by remember { mutableStateOf<Typ?>(null) }
    var type2Str by remember { mutableStateOf("") }

    val viewModel: AddTypeViewModel = viewModel()
    val type1ListViewModel: TypeListViewModel = viewModel()
    val treeDataViewModel: TreeDataViewModel = viewModel()

    val toastMessage by viewModel.toastMessage.collectAsState()

    if (toastMessage != null) {
        NewToast(msg = toastMessage!!)
        viewModel.setToastMessage(null)
        type1Str = ""
        type2Str = ""
        type1ListViewModel.getWorkType1ByPid()
        treeDataViewModel.genData(type1ListViewModel.type1ListData.value ?: emptyList())
    }
    val textFieldcolors = TextFieldDefaults.colors(
        unfocusedContainerColor = Color.Transparent, // 背景透明
        focusedContainerColor = Color.Transparent // 背景透明
    )

    var visible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        delay(300)
        visible = true
    }



    when (id) {
        0 -> {
            AnimatedVisibility(
                visible = visible,
                enter = expandHorizontally(
                    animationSpec = tween(
                        700,
                        easing = FastOutSlowInEasing
                    ),
                ),
                exit = shrinkHorizontally(
                    animationSpec = tween(
                        700,
                        easing = FastOutSlowInEasing
                    ),
                ),
            ) {
                Column {
                    var showError: Boolean by remember { mutableStateOf(false) }
                    Select(
                        label = "工作大类",
                        items = selectedData,
                        dropdownMenuItem = {
                            Text(text = it.description)
                        },
                        selectedItem = type1Obj?.description ?: "",
                        onSelect = { item ->
                            type1Obj = item
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(
                        value = type2Str,
                        onValueChange = { type2Str = it },
                        label = { Text("工作子类") },
                        placeholder = { Text("工作子类") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        colors = textFieldcolors,
                        isError = showError,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            showError = true
                            viewModel.addType(
                                AddTypeItem(
                                    description = type2Str,
                                    pid = type1Obj!!.id,
                                    type = 2
                                )
                            )
                        },
                        Modifier
                            .wrapContentSize(Alignment.Center)
                            .padding(5.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = "添加")
                    }
                }
            }
        }

        1 -> {
            AnimatedVisibility(
                visible = visible,
                enter = expandHorizontally(
                    animationSpec = tween(
                        700,
                        easing = LinearOutSlowInEasing
                    ),
                ),
                exit = shrinkHorizontally(
                    animationSpec = tween(
                        700,
                        easing = LinearOutSlowInEasing
                    ),
                ),
            ) {
                Column {
                    var showError: Boolean by remember { mutableStateOf(false) }

                    TextField(
                        value = type1Str,
                        onValueChange = { type1Str = it },
                        label = { Text("工作大类") }, // It will be shown on the top when focussed
                        placeholder = {
                            Text("工作大类")
                        }, // It will be shown as hint when nothing is typed
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        colors = textFieldcolors,
                        isError = showError,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            showError = true
                            viewModel.addType(
                                AddTypeItem(
                                    description = type1Str,
                                    pid = 0,
                                    type = 1
                                )
                            )
                        },
                        Modifier
                            .wrapContentSize(Alignment.Center)
                            .padding(5.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = "添加")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewViewPagerExample() {
    NewBox(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AddTypePage {}
        }
    }
}
