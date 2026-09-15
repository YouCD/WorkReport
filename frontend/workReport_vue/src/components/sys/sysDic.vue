<template>
    <div class="dic-wrap">
        <div class="dic-left">
            <a-card :bordered="false" @click="clearMenu" class="card-box" title="工作分类">
                <a-tree
                        v-if="treeData.length>0"
                        :treeData="treeData"
                        :defaultExpandAll="true"
                        @select="onSelect"
                        @rightClick="onRightClick"
                ></a-tree>
                <div :style="tmpStyle" v-if="NodeTreeItem">
                    <div class="menu-item" @click="orgAdd">
                        <a-tooltip placement="bottom" title="新增子节点">
                            <PlusCircleOutlined/>
                        </a-tooltip>
                    </div>
                    <div class="menu-item" @click="orgEdit">
                        <a-tooltip placement="bottom" title="修改">
                            <EditOutlined/>
                        </a-tooltip>
                    </div>
                    <div class="menu-item" @click="orgDelete" v-if="NodeTreeItem.parentOrgId">
                        <a-tooltip placement="bottom" title="删除">
                            <MinusCircleOutlined/>
                        </a-tooltip>
                    </div>
                </div>
            </a-card>
        </div>
        <div class="dic-right">
            <a-card :bordered="false" class="card-box">
                <a-form class="dic-form-inline" :layout="'inline'" :model="Type1Data" :rules="rules" ref="ruleForm">
                    <a-form-item name="Type1" class="dic-flex-item">
                        <a-input placeholder="请输入工作大类" v-model:value="Type1Data.Type1" @change="onType1Change"/>
                    </a-form-item>
                    <a-form-item>
                        <a-button @click="Type1AddHandleOk" type="primary">
                            添加
                        </a-button>
                    </a-form-item>
                </a-form>
            </a-card>
            <a-card :bordered="false" class="card-box">
                <a-form class="dic-form-inline" :layout="'inline'" :model="Type2Data" :rules="rules1" ref="ruleForm2">
                    <a-form-item name="pid" class="dic-flex-item">
                        <a-select style="width: 100%" placeholder="请选择工作大类" v-model:value="Type2Data.pid">
                            <a-select-option v-for="(item,index) in type1List" :value="item.id" :key="index">
                                {{ item.description }}
                            </a-select-option>
                        </a-select>
                    </a-form-item>
                    <a-form-item name="description" class="dic-flex-item">
                        <a-input placeholder="请输入工作子类" v-model:value="Type2Data.description"/>
                    </a-form-item>
                    <a-form-item>
                        <a-button @click="Type2AddHandleOk" type="primary">
                            添加
                        </a-button>
                    </a-form-item>
                </a-form>
            </a-card>
        </div>

        <a-modal v-model:open="editTypeModal" title="修改工作类别" cancelText="取消" okText="确认" @ok="editTypeHandleOk">
            <a-form :model="TypeData" :label-col="labelCol" :wrapper-col="wrapperCol" :rules="rules1"
                          ref="editRuleForm">
                <a-form-item label="类别名称" name="description">
                    <a-input v-model:value="TypeData.description" placeholder="请输入类别名称"/>
                </a-form-item>

                <a-form-item v-if="TypeData.pid >0" label="工作大类" name="pid">
                    <a-select placeholder="请选择工作大类" v-model:value="TypeData.pid">
                        <a-select-option v-for="(item,index) in type1List" :value="item.id" :key="index">
                            {{ item.description }}
                        </a-select-option>
                    </a-select>
                </a-form-item>

            </a-form>
        </a-modal>
    </div>
</template>

<script setup lang="ts">
    import { ref, reactive, onMounted } from 'vue'
    import { message } from 'ant-design-vue'
    import { addWorkType } from '../api/sysdic'
    import { getWorkType1, getWorkType2 } from "@/components/api/worklog";
    import { getWorkType, editWorkType } from "@/components/api/sysdic";
    import { PlusCircleOutlined, EditOutlined, MinusCircleOutlined } from '@ant-design/icons-vue'

    const ruleForm = ref()
    const ruleForm2 = ref()
    const editRuleForm = ref()

    const rules = {
        Type1: [{ required: true, message: '请输入工作大类', trigger: 'change' }],
    }
    const rules1 = {
        pid: [{ required: true, message: '请选择工作大类', trigger: 'change' }],
        description: [{ required: true, message: '请输入工作子类', trigger: 'change' }],
    }

    const showType1 = ref(false)
    const Type1Data = reactive<{ Type1: string | undefined }>({ Type1: undefined })

    const Type2Data = reactive<{ pid: number | undefined, description: string | undefined }>({
        pid: undefined,
        description: undefined,
    })
    const type1List = ref([] as any[])

    const NodeTreeItem = ref<any>(null)
    const tmpStyle = ref<any>('')
    const treeData = ref([] as any[])

    const selectID = ref<number | string | undefined>(undefined)
    const editTypeModal = ref(false)
    const TypeData = reactive<{ id: number, description: string | undefined, pid: number | undefined, type: number | undefined }>({
        id: 1, description: undefined, pid: undefined, type: undefined,
    })
    const labelCol = { span: 5 }
    const wrapperCol = { span: 18 }

    const queryParam = ref<unknown>(undefined)

    //添加type1
    const onType1Change = () => {
        ruleForm.value.validateFields(['Type1']).catch(() => {
        });
    }

    const Type1AddHandleOk = () => {
        ruleForm.value.validate().then((values: any) => {
            let params = {
                description: values.Type1,
                type: 1,
                pid: 0
            }
            addWorkType(params).then(res => {
                if (res.data.flag) {
                    message.success(res.data.msg)
                    showType1.value = false;
                    ruleForm.value.resetFields()
                    getWorkType1Handler()
                } else if (res.data.flag !== true) {
                    message.error(res.data.msg)
                }
            })
        }).catch(() => {
        });
    }

    const Type2AddHandleOk = () => {
        ruleForm2.value.validate().then((values: any) => {
            let params = {
                description: values.description,
                type: 2,
                pid: values.pid
            }
            addWorkType(params).then(res => {
                if (res.data.flag) {
                    message.success(res.data.msg)
                    showType1.value = false;
                    ruleForm2.value.resetFields()
                    getWorkType1Handler()
                } else if (res.data.flag !== true) {
                    message.error(res.data.msg)
                }
            })
        }).catch(() => {
        });
    }

    const getWorkType1Handler = () => {
        getWorkType1().then(res => {
            if (res.data.flag) {
                treeData.value = []
                type1List.value = res.data.data.type_list
                for (let i = 0; i < res.data.data.type_list.length; i++) {
                    let params = {
                        pid: res.data.data.type_list[i].id
                    }
                    treeData.value.push({
                        title: res.data.data.type_list[i].description,
                        key: res.data.data.type_list[i].id,
                        children: []
                    })
                    getWorkType2(params).then(resc => {
                        if (resc.data.flag) {
                            for (let c = 0; c < resc.data.data.type_list.length; c++) {
                                if (res.data.data.type_list[i].id === resc.data.data.type_list[c].pid) {
                                    treeData.value[i].children.push({
                                        title: resc.data.data.type_list[c].description,
                                        key: resc.data.data.type_list[c].id,
                                    })
                                }
                            }
                        }
                    })
                }
            }
        })
    }

    const onSelect = (selectedKeys: any, info: any) => {
        console.log(selectedKeys)
        queryParam.value = {
            orgId: selectedKeys[0]
        };
    }

    const onRightClick = (e: any, node: any) => {
        const x = e.currentTarget.offsetLeft + e.currentTarget.clientWidth;
        const y = e.currentTarget.offsetTop;
        NodeTreeItem.value = {
            pageX: x,
            pageY: y,
            id: node.eventKey,
            title: node.title,
            parentOrgId: node.parentOrgId || null
        };
        selectID.value = node.eventKey
        tmpStyle.value = {
            position: 'absolute',
            maxHeight: 40,
            textAlign: 'center',
            left: `${x + 10 - 0}px`,
            top: `${y + 6 - 0}px`,
            display: 'flex',
            flexDirection: 'row'
        };
    }

    const clearMenu = () => {
        // NodeTreeItem.value = null;
    }

    const orgAdd = () => {
        // 写自己的业务逻辑
    }

    const orgEdit = () => {
        editTypeModal.value = true
        let params = {
            id: selectID.value
        }
        getWorkType(params).then(res => {
            if (res.data.flag) {
                Object.assign(TypeData, res.data.data)
            } else if (res.data.flag !== true) {
                message.error(res.data.msg)
            }
        })
    }

    const orgDelete = () => {
        // 写自己的业务逻辑
    }

    const editTypeHandleOk = () => {
        editRuleForm.value.validate().then((values: any) => {
            let payload = {
                ...TypeData,
                description: values.description,
                pid: values.pid !== undefined ? values.pid : TypeData.pid
            }
            editWorkType(payload).then(res => {
                if (res.data.flag) {
                    message.success(res.data.msg)
                    treeData.value = []
                    getWorkType1Handler()
                    editTypeModal.value = false;
                } else if (res.data.flag !== true) {
                    message.error(res.data.msg)
                    editTypeModal.value = false;
                }
            })
        }).catch(() => {
        });
    }

    const isMobile = () => {
        let flag = navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i);
        return flag;
    }

    onMounted(() => {
        getWorkType1Handler()
        isMobile();
    })
</script>

<style scoped>
.dic-wrap {
    display: flex;
    align-items: flex-start;
    gap: 24px;
    padding: 4px;
}

.dic-left {
    flex: 0 0 280px;
    height: 100vh;
    overflow: hidden;
}

.dic-right {
    flex: 1 1 auto;
    min-width: 0;
    display: flex;
    flex-direction: column;
    gap: 24px;
    max-width: 480px;
}

.dic-form-inline {
    display: flex;
    flex-wrap: nowrap;
    align-items: center;
    width: 100%;
}

.dic-form-inline .dic-flex-item {
    flex: 1 1 0;
    min-width: 0;
}

.dic-form-inline .dic-flex-item .ant-input,
.dic-form-inline .dic-flex-item .ant-select {
    width: 100% !important;
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

@media (max-width: 768px) {
    .dic-wrap {
        flex-direction: column;
    }

    .dic-left {
        flex: 0 0 auto;
        width: 100%;
    }

    .dic-right {
        width: 100%;
        max-width: none;
    }
}
</style>
