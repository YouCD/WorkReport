package online.youcd.workreport.model

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import cafe.adriel.bonsai.core.node.Branch
import cafe.adriel.bonsai.core.node.Leaf
import cafe.adriel.bonsai.core.tree.Tree
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import online.youcd.workreport.api.RetrofitUtil
import online.youcd.workreport.types.Typ
import online.youcd.workreport.ui.component.EmojiIconList

@Composable
private fun EmojiIcon(emoji: String) {
    Text(emoji, color = Color.White, fontSize = 20.sp)
}

class TreeDataViewModel : ViewModel() {
    var api = RetrofitUtil.create()

    private val _treeData = MutableStateFlow<Tree<Typ>?>(null)
    val treeData: StateFlow<Tree<Typ>?> = _treeData

    @Composable
    fun genData(l: List<Typ>) {
        val treeData = Tree<Typ> {
            l.mapNotNull { item ->
                var leafData: List<Typ> by remember { mutableStateOf(emptyList()) }

                // 使用 LaunchedEffect 在 item 发生变化时获取数据并更新状态
                val newData = getType2List(item.id)
                leafData = newData

                Branch(item.description) {
                    leafData.mapNotNull { item1 ->
                        Leaf(item1.description, customIcon = { EmojiIcon(EmojiIconList[item1.id]) })
                    }
                }
            }
        }
        updateTreeData(treeData)
    }

    //    TODO 在genData中调用updateTreeData 方法，获取数据并更新状态
    fun updateTreeData(newTreeData: Tree<Typ>) {
        _treeData.value = newTreeData
    }

    @Composable
    fun getType2List(pid: Int): List<Typ> {
        // 使用 State 来存储网络请求结果
        val typeListState = remember(pid) {
            mutableStateOf<List<Typ>>(emptyList())
        }

        LaunchedEffect(pid) {
            // 在协程中执行网络请求
            val response = withContext(Dispatchers.IO) {
                api.getWorkType2ByPid(pid)
            }
            if (response.flag == true) {
                val typeList = response.data?.type_list ?: emptyList()
                // 更新 State 中的数据
                typeListState.value = typeList
            } else {
                // Handle error
            }
        }

        return typeListState.value
    }
}
