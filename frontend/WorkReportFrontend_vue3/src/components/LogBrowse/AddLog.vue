<template>
    <div class="BoxClass">
        <div class="LeftClass">
            <div class="CalendarClass">
                <a-calendar v-model:value="today" :fullscreen="false" valueFormat="YYYY-MM-DD" @select="onSelect"/>
            </div>
            <a-button type="primary" style="width: 100%;margin-top: 8px" @click="goToday">今日</a-button>
            <a-button type="primary" style="width: 100%;margin-top: 8px" @click="GetWeekLog">本周日志</a-button>
        </div>
        <div class="RightClass">
            <div v-if="!data.showWeekLog">
                <a-form
                        :model="logData"
                        name="basic"
                        :label-col="{ span: 8 }"
                        :wrapper-col="{ span: 16 }"
                        autocomplete="off"
                >
                    <a-form-item label="工作大类" name="type1" :rules="[{ required: true, message: '请选择工作大类' }]">
                        <a-select ref="select" v-model:value="logData.type1" style="width: 100%"
                                  @change="type1handleChange">
                            <a-select-option v-model:value="item.id" v-for="(item,index) in type1Data" :key="index">
                                {{ item.description }}
                            </a-select-option>
                        </a-select>
                    </a-form-item>

                    <a-form-item label="工作子类" name="type2"
                                 :rules="[{ required: true, message: '请选择工作子类!' }]">
                        <a-select ref="select" style="width: 100%" v-model:value="logData.type2"
                                  @change="type2handleChange">
                            <a-select-option :value="item.id" v-for="(item,index) in type2Data" :key="index">
                                {{ item.description }}
                            </a-select-option>
                        </a-select>
                    </a-form-item>
                </a-form>
                <div>
                    <a-textarea :rows="11" v-model:value="logData.content">
                    </a-textarea>
                </div>
                <a-button type="primary" @click="AddLogHandler" style="margin-top: 10px;width: 100%;">添加</a-button>
            </div>

            <div v-if="data.showWeekLog">
                <div v-for="(item,i) in data.weekData" :key="i">
                    {{ i }}:<br/>
                    <div v-for="(item1,i1) in item" :key="i1">{{ kg }}{{ i1 + 1 }}.{{ item1 }}</div>
                </div>
            </div>
        </div>

    </div>


</template>

<script setup lang="ts">
import {onMounted, reactive, ref} from 'vue';

import dayjs, {Dayjs} from 'dayjs';

import moment from "moment";
import {AddLog, FetchWeekLog, IAddLogData} from "@/types/log";
import {storeToRefs} from "pinia";
import {type1Store} from "@/store/type1";
import {type2Store} from "@/store/type2";
import {logStore} from "@/store/log";


const today = ref<Dayjs>();

const logData: IAddLogData = reactive(
    {
        id: 0,
        type1: 0,
        type2: 0,
        content: "",
        date: moment(dayjs().format("YYYY-MM-DD")).unix()
    }
)


//  选择日期
const onSelect = (value: Dayjs, mode: string) => {
    // 选择日期后 就直接转为 unix 时间戳
    logData.date = moment(value.toString()).unix()
};

//
// tion goToday
//  @Description: 回到今天
//
function goToday() {
    today.value = dayjs()
    logData.date = moment(today.value?.format("YYYY-MM-DD")).unix()
}

//
// tion GetWeekLog
//  @Description: 获取本周日志
//
function GetWeekLog() {
    data.showWeekLog = !data.showWeekLog
    if (data.showWeekLog) {
        FetchWeekLog().then(res => {
            data.weekData = res.data
        })
    }
}

const kg = "\u3000\u3000\u3000"

const type1handleChange = async (value: number) => {

    await getType2List({pid: value})
    logData.type1 = value
    logData.type2 = type2Data.value[0].id
};

const type2handleChange = (value: number) => {
    logData.type2 = value
};


let data = reactive({
    showWeekLog: false,
    weekData: {},
});

// 工作大类
const {type1Data} = storeToRefs(type1Store())
const {getType1List} = type1Store()
onMounted(async () => {
        await getType1List()

        logData.type1 = type1Data.value[0].id
        await getType2List({pid: logData.type1})
        logData.type2 = type2Data.value[0].id
    }
)


//  工作子类
const {type2Data} = storeToRefs(type2Store())
const {getType2List} = type2Store()

//  添加日志
const {getLogData} = logStore()
const AddLogHandler = async () => {
    if (logData.content === "") {
        return
    }
    await AddLog(logData)
    // 添加完毕后要更新数据
    await getLogData({
        pageIndex: 1,
        pageSize: 10
    })
    logData.content = ""
}


</script>

<style scoped>

.BoxClass {
    overflow: hidden;
    width: 100%;
}

.LeftClass {
    padding: 10px;
    width: 300px;
    height: 425px;
    background: white;
    float: left;
    border-radius: 4px;
    margin-right: 10px;
}

.CalendarClass {
    border: 1px solid #F0F2F5;
}


.RightClass {
    float: left;
    padding: 10px;
    width: 300px;
    background: white;
    border-radius: 4px;
    height: 425px;
}

</style>