<template>
  <a-config-provider :locale="zhCN">
    <div class="wlog-wrap">
    <div class="wlog-left">
      <a-card :bordered="false" class="card-box wlog-add-card">
        <a-form :layout="'vertical'" :model="form" :rules="rules" ref="ruleForm">
          <div class="wlog-form-row">
          <a-form-item name="date" class="wlog-form-col">
            <a-date-picker style="width: 100%" :defaultValue="dayjs(getCurrentData(), 'YYYY-MM-DD')" v-model:value="form.date"/>
          </a-form-item>

          <a-form-item name="type1" class="wlog-form-col">
            <a-select style="width: 100%" placeholder="请选择工作大类" v-model:value="form.type1"
                      @change="addWorkLogType1Change">
              <a-select-option v-for="(item,index) in type1List" :value="item.id" :key="index">
                {{ item.description }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item name="type2" class="wlog-form-col">
            <a-select style="width: 100%" placeholder="请选择工作子类" v-model:value="form.type2">
              <a-select-option v-for="(item,index) in type2List" :value="item.id" :key="index">
                {{ item.description }}
              </a-select-option>
            </a-select>
          </a-form-item>
          </div>
          <div class="wlog-btn-row">
            <a-button @click="backToToday">
              回到今日
            </a-button>
            <a-button @click="getWeekChange">
              本周日志
            </a-button>
            <a-button type="primary" @click="addWorkLogHandler">
              添加
            </a-button>
          </div>
          <a-form-item name="content">
            <a-textarea style="width: 100%" @keydown="Keydown" v-model:value="form.content" :show-count="true" class="wlog-content-textarea"/>
          </a-form-item>
<div class="wlog-btn-row">
            <a-button type="primary" :icon="h(AiIcon)" @click="getAiWorkLogFromWeekHandler">
              本周日志
            </a-button>
            <a-button @click="clearContentHandler">
              清空
            </a-button>
            <a-button danger :icon="h(AiIcon)" @click="sendEmailHandler">
              邮件发送
            </a-button>
            <a-button :icon="h(AiIcon)" @click="addContentHandler">
              添加
            </a-button>
          </div>
        </a-form>
      </a-card>
    </div>

    <div class="wlog-right">
      <a-card :bordered="false" class="card-box search-card">
        <a-form class="search-form" :layout="'inline'">
          <a-form-item class="search-flex">
            <a-range-picker :locale="locale" v-model:value="exportFormData.dateRange"
                            @change="dateRangeChange"/>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="showExportWorkLogHandler">
              导出日志
            </a-button>
          </a-form-item>
        </a-form>
      </a-card>

      <a-card :bordered="false" class="card-box search-card">
        <a-form class="search-form" :layout="'inline'" :model="searchForm">
          <a-form-item class="search-flex">
            <a-input placeholder="请输入工作内容" v-model:value="searchForm.content"/>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="searchFormHandler">
              搜索
            </a-button>
          </a-form-item>
        </a-form>
      </a-card>

      <a-card :bordered="false" class="card-box">
        <a-table v-if="showTable" @change="handleTableChange" :columns="columns" :data-source="data"
                 :rowKey="record => record.id" :loading="loading" size="small" :pagination="pagination"
                 :scroll="{ x: 800 }">
          <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'action'">
              <a @click="showWorkLogEdit(record)">编辑</a>
              <a-divider type="vertical"/>

              <a-popconfirm title="确认删除该记录"
                            ok-text="确认"
                            cancel-text="取消"
                            @confirm="confirmDelete(record)"
              >
                        <a href="#">删除</a>
              </a-popconfirm>
              </template>
          </template>
        </a-table>
        <div v-if="!showTable">
          <div v-for="(item,i) in weekData" :key="i">
            {{ i }}:<br/>
            <div v-for="(item1,i1) in item" :key="i1">{{ kg }}{{ i1 + 1 }}.{{ item1 }}</div>
          </div>
        </div>
      </a-card>
    </div>

    <a-modal v-model:open="editWorkLog" title="编辑工作日志" @ok="editWorkLogHandler" cancelText="取消" okText="确认">
          <a-form :model="editForm" :label-col="labelCol" :wrapper-col="wrapperCol" :rules="rules"
                        ref="editModalRuleForm">
            <a-form-item label="日期" name="date">
              <a-date-picker :defaultValue="dayjs(getCurrentData(), 'YYYY-MM-DD')"
                             v-model:value="editForm.date"
                             :locale="locale"/>
            </a-form-item>

            <a-form-item label="工作大类" name="type1">
              <a-select placeholder="请选择工作大类" v-model:value="editForm.type1" @change="editWorkLogType1Change">
                <a-select-option v-for="(item,index) in type1List" :value="item.id" :key="index">
                  {{ item.description }}
                </a-select-option>
              </a-select>
            </a-form-item>

            <a-form-item label="工作子类" name="type2">
              <a-select placeholder="请选择工作子类" v-model:value="editForm.type2">
                <a-select-option v-for="(item,index) in type2List" :value="item.id" :key="index">
                  {{ item.description }}
                </a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item label="工作内容" name="content">
              <a-textarea v-model:value="editForm.content"/>
            </a-form-item>
          </a-form>
        </a-modal>


        </div>
  </a-config-provider>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch, h } from 'vue'
import { message } from 'ant-design-vue'
import dayjs from "dayjs"
import {
  addWorkLog as addWorkLogApi,
  addContent as addContentApi,
  delWorkLog,
  getWorkLog,
  getWorkLogFromContent,
  getWorkLogFromDate,
  getWorkLogFromType,
  getWorkLogFromWeek,
  getAiWorkLogFromWeek as getAiWorkLogFromWeekApi,
  getWorkType1,
  getWorkType2,
  modifyWorkLog,
  sendEmail as sendEmailApi
} from "../api/worklog"
import locale from "ant-design-vue/lib/date-picker/locale/zh_CN";
import zhCN from "ant-design-vue/es/locale/zh_CN";
import 'dayjs/locale/zh-cn';
import AiIcon from "@/components/iconfont/AiIcon.vue";

dayjs.locale('zh-cn');

const columns: any[] = [
  {
    dataIndex: 'date',
    key: 'date',
    title: '日期',
    customRender: function ({ text }: { text: any }) {
      return dayjs.unix(text).format("YYYY-MM-DD");
    },
    width: 120,
    fixed: 'left'
  },
  {
    title: '工作大类',
    key: 'type1',
    dataIndex: 'type1',
    width: 120,
  },
  {
    title: '工作子类',
    key: 'type2',
    dataIndex: 'type2',
    width: 120,
  },
  {
    title: '工作内容',
    dataIndex: 'content',
    key: 'content',
    ellipsis: true,
  },
  {
    title: '操作', key: 'action', width: 100, fixed: 'right'
  },
];

const ruleForm = ref()
const editModalRuleForm = ref()
const exportForm = ref()

function getCurrentData() {
  return dayjs().format('YYYY-MM-DD');
}

function backToToday() {
  form.date = dayjs(getCurrentData(), 'YYYY-MM-DD')
}

const data = ref([] as any[])
const loading = ref(false)
const labelCol = { span: 5 }
const wrapperCol = { span: 19 }
const pagination = reactive({
  size: "small",
  showSizeChanger: true,
  pageSize: 15,
  total: 0,
  pageSizeOptions: ["10", "15", "20", "50", "100"],
  showTotal: (total: number) => `共有 ${total} 条工作记录`,
})
const pageInfo = reactive({
  pageIndex: 1,
  pageSize: 15,
})

const type1List = ref([] as any[])
const type2List = ref([] as any[])
const form = reactive<any>({
  type1: undefined,
  type2: undefined,
  content: undefined,
  date: dayjs(getCurrentData(), 'YYYY-MM-DD'),
})
const rules = {
  type1: [{ required: true, message: '请选择工作大类', trigger: 'change' }],
  type2: [{ required: true, message: '请选择工作子类', trigger: 'change' }],
  content: [{ required: true, message: '请输入工作内容', trigger: 'change' }],
  date: [{ required: true, message: '请选择日期', trigger: 'change' }],
}

const editWorkLog = ref(false)
const editForm = reactive<any>({
  id: undefined,
  type1: undefined,
  type2: undefined,
  content: undefined,
  date: undefined,
})

const searchForm = reactive<any>({
  content: "",
  date: undefined,
})

const weekData = ref([] as any[])
const showTable = ref(true)
const kg = "　　"

const visible = ref(false)
const layout = ref<unknown>(undefined)
const is_Mobile = ref(false)
const showWorkType = ref(false)
const showSearch = ref(false)
const showAddWorkLog = ref(false)

const exportFormData = reactive<any>({
  dateRange: undefined,
})
const dateRange = reactive<any>({
  dateStart: undefined,
  dateEnd: undefined
})
const backUrl = ref("/w/downloadWorklog")

const showSearchDate = ref(false)

const isSelectDrawer = ref(false)
const DrawerKey = ref<unknown>(undefined)
const queryParam = ref<unknown>(undefined)

watch(searchForm, (newName) => {
  if (newName.content == "") {
    showSearchDate.value = false;
  } else if (newName.content !== "") {
    showSearchDate.value = true;
  }
}, { deep: true })

function getWorkType1Handler() {
  getWorkType1().then(res => {
    if (res.data.flag) {
      type1List.value = res.data.data.type_list
    }
  })
}
function getWorkType2Handler(params: any) {
  getWorkType2(params).then(res => {
    if (res.data.flag) {
      type2List.value = res.data.data.type_list
    }
  })
}

function addWorkLogType1Change() {
  form.type2 = undefined
  let params = {
    pid: form.type1
  }
  getWorkType2Handler(params)
}

function Keydown(e: any) {
  if (!e.shiftKey && e.keyCode == 13) {
    e.cancelBubble = true;
    e.stopPropagation();
    e.preventDefault();
    addWorkLogHandler()
  }
}
function addWorkLogHandler() {
  showTable.value = true
  ruleForm.value.validate().then(() => {
    let params = {
      type1: form.type1,
      type2: form.type2,
      content: form.content.substr(0, form.content.length - 1),
      date: form.date.startOf('day').unix(),
    }
    addWorkLogApi(params).then(res => {
      if (res.data.flag) {
        form.content = undefined
        message.success(res.data.msg)
        getWorkLogHandler()
      } else if (res.data.flag !== true) {
        message.error(res.data.msg)
      }
    });
  }).catch(() => {
  });
}
function addContentHandler() {
  let params = {
    content: form.content,
    date: form.date.startOf('day').unix(),
  }
  addContentApi(params).then(res => {
    if (res.data.flag) {
      form.content = undefined
      message.success("添加成功")
      getWorkLogHandler()
    } else {
      message.error(res.data.msg || "添加失败")
    }
  }).catch(() => {
    message.error("添加失败")
  })
}

function getAiWorkLogFromWeekHandler() {
  getAiWorkLogFromWeekApi().then(res => {
    if (res.data.flag) {
      form.content = res.data.data
    } else {
      message.error(res.data.msg || "获取失败")
    }
  }).catch(() => {
    message.error("获取失败")
  })
}

function sendEmailHandler() {
  ruleForm.value.validate().then(() => {
    let params = {
      content: form.content,
      date: form.date.startOf('day').unix(),
    }
    sendEmailApi(params).then(res => {
      if (res.data.flag) {
        message.success(res.data.msg)
      } else {
        message.error(res.data.msg || "发送失败")
      }
    }).catch(() => {
      message.error("发送失败")
    })
  }).catch(() => {
  })
}

function clearContentHandler() {
  form.content = undefined
}

function getWorkLogHandler() {
  getWorkLog(pageInfo).then(res => {
    if (res.data.flag) {
      data.value = res.data.data.work_content_resp_list
      pagination.total = res.data.data.sum
    } else if (res.data.flag !== true) {
      message.error(res.data.msg)
    }
  });
}

function showWorkLogEdit(record: any) {
  editWorkLog.value = true
  editForm.id = record.id
  editForm.type1 = record.type1_id
  editForm.type2 = record.type2_id
  editForm.content = record.content
  editForm.date = dayjs.unix(record.date)
  let params = {
    pid: record.type1_id
  }
  getWorkType2Handler(params)
}
function editWorkLogType1Change() {
  let params = {
    pid: editForm.type1
  }
  editForm.type2 = undefined
  getWorkType2Handler(params)
}

function editWorkLogHandler() {
  editModalRuleForm.value.validate().then(() => {
    let params = {
      id: editForm.id,
      type1: editForm.type1,
      type2: editForm.type2,
      content: editForm.content,
      date: editForm.date.startOf('day').unix(),
    }
    modifyWorkLog(params).then(res => {
      if (res.data.flag) {
        message.success(res.data.msg)
        getWorkLogHandler()
        editWorkLog.value = false
      } else if (res.data.flag !== true) {
        message.error(res.data.msg)
        editWorkLog.value = false
      }
    });
  }).catch(() => {
  });
}

function handleTableChange(page: any) {
  if (searchForm.content == "") {
    pageInfo.pageSize = page.pageSize;
    pageInfo.pageIndex = page.current;
    getWorkLogHandler()
  } else if (searchForm.content !== "") {
    searchFormHandler()
  }
  if (isSelectDrawer.value) {
    getWorkLogFromTypeHandler(DrawerKey.value)
  }
}

function confirmDelete(record: any) {
  let params = {
    id: record.id
  }
  delWorkLog(params).then(res => {
    if (res.data.flag) {
      getWorkLogHandler()
      message.success(res.data.msg)
    } else if (res.data.flag !== true) {
      message.error(res.data.msg)
    }
  });
}

function getWorkLogFromTypeHandler(id: any) {
  let params = {
    typeID: id,
    pageIndex: pageInfo.pageIndex,
    pageSize: pageInfo.pageSize,
  }
  getWorkLogFromType(params).then(res => {
    showTable.value = true
    if (res.data.flag) {
      data.value = []
      data.value = res.data.data.work_content_resp_list
      pagination.total = res.data.data.sum
    } else if (res.data.flag !== true) {
      message.error(res.data.msg)
    }
  })
}

function getWeekChange() {
  getWorkLogFromWeek().then(res => {
    if (res.data.flag) {
      const weekMap = res.data.data
      let text = ""
      for (const type1 in weekMap) {
        text += "【" + type1 + "】\n"
        const list = weekMap[type1]
        if (Array.isArray(list)) {
          list.forEach((item: any, i: number) => {
            text += (i + 1) + ". " + item + "\n"
          })
        }
      }
      form.content = text
    } else if (res.data.flag !== true) {
      message.error(res.data.msg)
    }
  })
}
function MobileGetWeekChange() {
  showTable.value = !showTable.value
  showSearch.value = false
  showAddWorkLog.value = false
  getWorkLogFromWeek().then(res => {
    if (res.data.flag) {
      weekData.value = []
      weekData.value = res.data.data
    } else if (res.data.flag !== true) {
      message.error(res.data.msg)
    }
  })
}
function searchFormHandler() {
  if (searchForm.date === undefined || searchForm.content !== "") {
    showTable.value = true
    getWorkLogFromContent(searchForm).then(res => {
      if (res.data.flag) {
        data.value = res.data.data.work_content_resp_list
        pagination.total = res.data.data.sum
      } else if (res.data.flag !== true) {
        message.error(res.data.msg)
      }
    })
  } else if (searchForm.content == "") {
    let params = {
      date: searchForm.date.startOf('day').unix(),
    }
    getWorkLogFromDate(params).then(res => {
      if (res.data.flag) {
        data.value = res.data.data.work_content_resp_list
      } else if (res.data.flag !== true) {
        message.error(res.data.msg)
      }
    })
  }
}

function afterVisibleChange(_val: any) {
  // console.log('visible', val);
}
function showDrawer() {
  visible.value = true;
}
function onClose() {
  visible.value = false;
}

function isMobile() {
  let flag = navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i);
  return flag;
}
function layoutHandler() {
  if (!isMobile()) {
    layout.value = "inline"
    is_Mobile.value = false
    showSearch.value = true
    showAddWorkLog.value = true
  } else if (isMobile()) {
    layout.value = "horizontal"
    is_Mobile.value = true
    showWorkType.value = true
  }
}

function MobileSearch() {
  showAddWorkLog.value = false
  showSearch.value = !showSearch.value
}
function MobileAddWorkLog() {
  showSearch.value = false
  showAddWorkLog.value = !showAddWorkLog.value
}

function dateRangeChange(date: any) {
  if (!date || date.length < 2) {
    dateRange.dateStart = undefined
    dateRange.dateEnd = undefined
    return
  }
  dateRange.dateStart = date[0].unix()
  dateRange.dateEnd = date[1].unix()
}
function exportWorkLogHandler() {
  if (dateRange.dateStart === undefined || dateRange.dateEnd === undefined) {
    message.warning("请先选择导出日志的时间范围")
    return
  }
  if (import.meta.env.VUE_APP_API_ROOT) {
    backUrl.value = import.meta.env.VUE_APP_API_ROOT + backUrl.value
  }
  let url = backUrl.value + "?dateStart=" + dateRange.dateStart + "&dateEnd=" + dateRange.dateEnd
  downLoadByUrl(url)
}

function downLoadByUrl(url: string) {
  var xhr = new XMLHttpRequest();
  xhr.open('GET', url, true);
  xhr.setRequestHeader("jwt", localStorage.getItem('jwt') || '');
  xhr.responseType = 'blob';
  xhr.onload = function (e: any) {
    if (this.status == 200) {
      let blob = this.response;
      let filename = "WorkLog.xlsx";
      let a = document.createElement('a');
      let url = URL.createObjectURL(blob);
      a.href = url;
      a.download = filename;
      a.click();
      window.URL.revokeObjectURL(url);
    }
  };
  xhr.send();
}

function showExportWorkLogHandler() {
  exportWorkLogHandler()
}

onMounted(() => {
  layoutHandler()
  getWorkLogHandler()
  getWorkType1Handler()
})
</script>

<style scoped>
.wlog-wrap {
    display: flex;
    align-items: flex-start;
    gap: 24px;
    padding: 4px;
}

.wlog-left {
    flex: 1 1 0;
    display: flex;
    flex-direction: column;
    gap: 24px;
}

.wlog-right {
    flex: 1 1 0;
    min-width: 0;
    display: flex;
    flex-direction: column;
    gap: 24px;
}

.card-box {
    width: 100%;
}

.card-box :deep(.ant-card-body) {
    padding: 10px;
}

.search-card :deep(.ant-card-body) {
    padding: 10px;
}

.wlog-add-card :deep(.ant-card-body) {
    padding: 10px;
}

.search-form {
    display: flex;
    flex-wrap: nowrap;
    align-items: center;
    width: 100%;
}

.search-form .search-flex {
    flex: 1 1 0;
    min-width: 0;
}

.search-form .search-flex .ant-input,
.search-form .search-flex .ant-picker {
    width: 100%;
}

.menu-item {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    padding: 6px 8px;
    cursor: pointer;
    background: #fff;
    border: 1px solid #f0f0f0;
    border-radius: 4px;
}

.wlog-form-row {
    display: flex;
    gap: 8px;
    width: 100%;
}

.wlog-btn-row {
    display: flex;
    gap: 8px;
    margin-bottom: 16px;
    width: 100%;
}

.wlog-btn-row > :deep(.ant-btn) {
    flex: 1 1 0;
}

.wlog-form-col {
    flex: 1 1 0;
    min-width: 0;
}

.wlog-form-col .ant-form-item-control {
    width: 100%;
}

.wlog-content-textarea :deep(.ant-input) {
    resize: vertical;
    height: 350px;
}

@media (max-width: 768px) {
    .wlog-wrap {
        flex-direction: column;
    }

    .wlog-left {
        flex: 0 0 auto;
        width: 100%;
    }

    .wlog-right {
        width: 100%;
    }
}
</style>

<style>
.wlog-btn-row .ant-btn > .iconfont {
    margin-right: 6px;
}
</style>