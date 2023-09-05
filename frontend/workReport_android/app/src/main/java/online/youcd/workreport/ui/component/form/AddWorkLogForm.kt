package online.youcd.workreport.ui.component.form

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import online.youcd.workreport.model.ActionDesc
import online.youcd.workreport.model.TypeListViewModel
import online.youcd.workreport.model.WorkLogViewModel
import online.youcd.workreport.tools.dateStr2unixTime
import online.youcd.workreport.tools.getCurrentDateInChina
import online.youcd.workreport.types.ModifyItem
import online.youcd.workreport.types.Typ

@Composable
fun AddWorkLogForm() {

    val type1viewModel: TypeListViewModel = viewModel()
    val typ1List by type1viewModel.type1ListData.collectAsState()
    type1viewModel.getWorkType1ByPid()

    var type1Obj by remember { mutableStateOf<Typ?>(null) }

    val typ2ListMap by type1viewModel.type2ListData.collectAsState()
    var type2Obj by remember { mutableStateOf<Typ?>(null) }


    var workLogContent by remember { mutableStateOf("") }
    var dateInt by remember { mutableStateOf(dateStr2unixTime(getCurrentDateInChina()).toInt()) }


    val workLogViewModel: WorkLogViewModel = viewModel()
    val actionMsg by workLogViewModel.actionMsg.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(actionMsg) {// 使用 LaunchedEffect 来确保只在 showModifySuccessToast 发生变化时触发一次
        if (actionMsg != null) {
            if (actionMsg?.desc == ActionDesc.ADD) {
                Toast.makeText(context, actionMsg!!.msg, Toast.LENGTH_SHORT)
                    .show()
                workLogContent = ""
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        // 选择日期
        FormRowDateSelect {
            dateInt = dateStr2unixTime(it).toInt()
        }

        FormRowSelect(
            label = "工作大类",
            items = typ1List ?: emptyList(),
            dropdownMenuItem = {
                Text(text = it.description)
            },
            selectedItem = type1Obj?.description ?: "",
            onSelect = { item ->
                type1Obj = item
                type1viewModel.getType2List(item.id)
            }
        )
        // 选择子类
        FormRowSelect(
            label = "工作子类",
            items = typ2ListMap?.get(type1Obj?.id ?: 0) ?: emptyList(),
            dropdownMenuItem = {
                Text(text = it.description)
            },
            selectedItem = type2Obj?.description ?: "",
            onSelect = { item ->
                type2Obj = item
            })


        // 工作内容
        FormRowInput("工作内容", onInputChange = {
            workLogContent = it
        }, str = workLogContent)
        Button(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(5.dp),
            onClick = {
                if (type1Obj != null && type2Obj != null) {
                    val data = ModifyItem(
                        content = workLogContent,
                        type1 = type1Obj!!.id,
                        type2 = type2Obj!!.id,
                        date = dateInt,
                        id = null
                    )
                    workLogViewModel.addWorkLog(data)
                }
            }, content = {
                Text(text = "添加")
            })
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddWorkLogForm() {
    AddWorkLogForm()
}
