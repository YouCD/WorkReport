package online.youcd.workreport.model

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import online.youcd.workreport.api.RetrofitUtil
import online.youcd.workreport.types.WorkContentItem


class WorkLogSource : PagingSource<Int, WorkContentItem>() {

    var api = RetrofitUtil.create()

    private val TAG = "WorkLog"

    override fun getRefreshKey(state: PagingState<Int, WorkContentItem>): Int? {
        Log.e("getRefreshKey", "${state.anchorPosition}")
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, WorkContentItem> {
        return try {
            val currentPage = params.key ?: 1
            val pageSize = params.loadSize
            Log.d(TAG, "currentPage: $currentPage")
            Log.d(TAG, "pageSize: $pageSize")
            // 传入当前页码，每页大小，然后请求数据。网络请求封装在repository
            val responseList = api.getWorkLog(currentPage, pageSize = pageSize)
                .data?.work_content_resp_list ?: emptyList()

            // 加载分页
            val everyPageSize = 4
            val initPageSize = 8
            // 前一页
            val preKey = if (currentPage == 1) null else currentPage.minus(1)
            // 下一页
            var nextKey: Int? = if (currentPage == 1) {
                initPageSize / everyPageSize
            } else {
                currentPage.plus(1)
            }
            Log.d(TAG, "preKey: $preKey")
            Log.d(TAG, "nextKey: $nextKey")
            if (responseList.isEmpty()) {
                nextKey = null
            }
            Log.d(TAG, "final nextKey: $nextKey")

            LoadResult.Page(
                data = responseList,
                prevKey = preKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

}