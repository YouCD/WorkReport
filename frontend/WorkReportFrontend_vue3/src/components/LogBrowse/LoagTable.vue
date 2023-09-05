<template>
    <div style="background: white;margin-bottom: 10px;padding: 10px">
        <a-range-picker @change="onRangeChange" style="width: 75%;"/>
        <a-button type="primary" style="float: right;width: 20%;" @click="ExportLogHandler">导出日志</a-button>
    </div>
    <div style="background: white;margin-bottom: 10px;padding: 10px">
        <a-input-search style="width: 75%;" v-model:value="data.content" placeholder="输入工作内容" enter-button
                        @search="SearchLogHandler"/>
        <a-button type="primary" style="float: right;width: 20%;" @click="ResetSearchLogHandler">重置</a-button>
    </div>


    <div style="  background: white;   padding: 10px;">
        <a-table
                :columns="data.columns"
                :data-source="LogData"
                :pagination="pagination"
                @change="handleTableChange"
                size="small"
                :loading="data.loading"
        >
            <template #bodyCell="{ column, record }">
                <template v-if="column.title === '操作'">
                    <a @click="EditHandler(record)">编辑</a>
                    <a-divider type="vertical"/>
                    <a @click="DeleteLogHandler(record.id)">删除</a>
                </template>
                <template v-if="column.title === '日期'">
                    {{ UnixTime2StrTime(record.date) }}
                </template>
            </template>
        </a-table>
        <!--  编辑日志 -->
        <a-modal v-model:visible="data.showEditModal" title="修改日志" @ok="handleOk">
            <template #footer>
                <a-button key="back" @click="handleCancel">取消</a-button>
                <a-button key="submit" type="primary" @click="handleOk">完成</a-button>
            </template>
            <a-form
                    :model="logItem"
                    name="basic"
                    :label-col="{ span: 4 }"
                    :wrapper-col="{ span: 20 }"
                    autocomplete="off"
            >
                <a-form-item label="日期" name="date">
                    <a-date-picker style="width: 100%" v-model:value="today" @select="onSelect"/>
                </a-form-item>

                <a-form-item label="工作大类" name="type1" :rules="[{ required: true, message: '请选择工作大类' }]">
                    <a-select ref="select" v-model:value="logItem.type1" style="width: 100%"
                              @change="type1handleChange">
                        <a-select-option v-model:value="item.id" v-for="(item,index) in type1Data" :key="index">
                            {{ item.description }}
                        </a-select-option>
                    </a-select>
                </a-form-item>

                <a-form-item label="工作子类" name="type2" :rules="[{ required: true, message: '请选择工作子类!' }]">
                    <a-select ref="select" style="width: 100%" v-model:value="logItem.type2">
                        <a-select-option :value="item.id" v-for="(item,index) in type2Data" :key="index">
                            {{ item.description }}
                        </a-select-option>
                    </a-select>
                </a-form-item>

                <a-form-item label="工作内容" name="content" :rules="[{ required: true, message: '请输入工作内容' }]">
                    <a-textarea :rows="5" v-model:value="logItem.content">
                    </a-textarea>
                </a-form-item>


            </a-form>

        </a-modal>
    </div>

</template>

<script setup lang="ts">
import {computed, onMounted, reactive,} from "vue"
import {DeleteLog, ExportLog, ExportLogRequest, IAddLogData, IWorkContent, ModifyLog} from "@/types/log";
import {logStore} from "@/store/log";
import {storeToRefs} from "pinia";

import {type2Store} from "@/store/type2";
import {type1Store} from "@/store/type1";


import dayjs, {Dayjs} from "dayjs";
import {IPageRequestData} from "@/types/commont";
import {Djs2UnixTime, UnixTime2StrTime} from "@/utils/time";


const columns = [
    {title: '日期', dataIndex: 'date', fixed: 'left', width: 100,},
    {title: '工作大类', dataIndex: 'type1', width: 100,},
    {title: '工作子类', dataIndex: 'type2', width: 100,},
    {title: '工作内容', dataIndex: 'content', ellipsis: true,},
    {title: '操作', key: 'operation', fixed: 'right', width: 100,}
];
let logItem: IAddLogData = reactive({} as IAddLogData)
const {LogData, sum} = storeToRefs(logStore())
const {getLogData, searchLog} = logStore()

const pagination = computed(() => ({
    total: sum.value,
    current: data.current,
    // pageSize: pageSize.value,
    // showTotal:  total => `总共 ${sum} 项`,
    defaultPageSize: 10,
    // pageSizeOptions: ['5', '10', '15', '20'], // 可不设置使用默认
    showSizeChanger: true, // 是否显示pagesize选择
    showQuickJumper: true, // 是否显示跳转窗
}));


const handleTableChange = async (pag: any,) => {
    data.current = pag.current
    if (!data.isSearch) {
        const a: IPageRequestData = {
            pageIndex: pag.current,
            pageSize: pag.pageSize,
        }
        data.loading = true
        await getLogData(a)
        data.loading = false
        return
    }
};


const data = reactive({
    columns,
    showEditModal: false,
    content: "",
    isSearch: false,
    current: 1,
    loading: false,
})


//  选择日期
const onSelect = (value: Dayjs,) => {
    logItem.date = Djs2UnixTime(value)
};

const defaultPageObj: IPageRequestData = {
    pageIndex: 1,
    pageSize: pagination.value.defaultPageSize
}
//  获取 工作日志
onMounted(async () => {
    await getLogData(defaultPageObj)
})

//  打开编辑 modal 框
const EditHandler = async (log: IWorkContent) => {
    data.showEditModal = true
    logItem.content = log.content
    logItem.type1 = log.type1_id
    logItem.date = log.date
    logItem.type2 = log.type2_id
    logItem.id = log.id
    await getType2List({pid: log.type1_id})
}

const today = computed(() => {
    return dayjs.unix(logItem.date)
})

//  modal编辑完成
const handleOk = () => {
    ModifyLog(logItem)
    data.showEditModal = false
    getLogData(defaultPageObj)
}
// 关闭 modal 框
const handleCancel = () => {
    data.showEditModal = false;
};


const {type1Data} = storeToRefs(type1Store())
const type1handleChange = async (value: number) => {
    await getType2List({pid: value})
    logItem.type2 = type2Data.value[0].id
};


//  工作子类
const {type2Data} = storeToRefs(type2Store())
const {getType2List,} = type2Store()


//  删除日志
const DeleteLogHandler = async (id: number) => {
    await DeleteLog({id: id})
    await getLogData(defaultPageObj)
}


// 导出日志
const ExportRequest: ExportLogRequest = {
    dateStart: 0,
    dateEnd: 0
}
const onRangeChange = (value: [Dayjs, Dayjs], dateString: [string, string]) => {
    ExportRequest.dateStart = Djs2UnixTime(value[0])
    ExportRequest.dateEnd = Djs2UnixTime(value[1])

};
const ExportLogHandler = () => {
    if (ExportRequest.dateStart === 0) {
        return
    }
    ExportLog(ExportRequest)
}


// 搜索日志
const SearchLogHandler = (value: string) => {
    searchLog({content: value})
    data.isSearch = true
}
const ResetSearchLogHandler = () => {
    if (data.content == "") {
        return
    }
    getLogData(defaultPageObj)
    data.content = ""
}


</script>

<style scoped>

</style>