<template>
    <a-layout-sider
            v-model:collapsed="collapsed"
            breakpoint="lg"
            collapsible
            :trigger="null"
            width="220"
            @collapse="onCollapse"
            @breakpoint="onBreakpoint"
    >
        <div class="sider-inner">
            <a-menu theme="dark" mode="inline" :inline-collapsed="collapsed" class="sider-menu">
                <a-menu-item key="1">
                    <template #icon><span class="iconfont icon-tubiao"></span></template>
                    <router-link to='/home'><span>Home</span></router-link>
                </a-menu-item>
                <a-sub-menu key="log">
                    <template #icon><span class="iconfont icon-rizhi"></span></template>
                    <template #title><span>日志</span></template>
                    <a-menu-item key="/workLogList">
                        <template #icon><UnorderedListOutlined /></template>
                        <router-link to='/workLogList'><span>日志浏览</span></router-link>
                    </a-menu-item>
                </a-sub-menu>
                <a-sub-menu key="sys">
                    <template #icon><span class="iconfont icon-shezhi"></span></template>
                    <template #title><span>系统设置</span></template>
                    <a-menu-item key="/sys/sysDic">
                        <template #icon><span class="iconfont icon-zidian"></span></template>
                        <router-link to='/sys/sysDic'><span>字典设置</span></router-link>
                    </a-menu-item>
                </a-sub-menu>
            </a-menu>
            <div class="sider-footer" @click="toggleCollapsed">
                <MenuFoldOutlined v-if="!collapsed" />
                <MenuUnfoldOutlined v-else />
                <span v-if="!collapsed" class="sider-footer-text">收起</span>
            </div>
        </div>
    </a-layout-sider>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import {
    UnorderedListOutlined,
    MenuFoldOutlined,
    MenuUnfoldOutlined,
} from '@ant-design/icons-vue'
import '@/assets/iconfont/iconfont.css'

const collapsed = ref(false)
const isSecretAdmin = ref(false)
const isSealAdmin = ref(false)

function onCollapse(_collapsed: boolean, _type: string) {
    // console.log(collapsed, type);
}
function onBreakpoint(_broken: boolean) {
    // console.log(broken);
}
function toggleCollapsed() {
    collapsed.value = !collapsed.value
}
function rolesHandler() {
    if (localStorage.getItem("role") === "secretadmin") {
        isSecretAdmin.value = true
    } else if (localStorage.getItem("role") === "sealadmin") {
        isSealAdmin.value = true
    }
}

onMounted(() => {
    rolesHandler()
})
</script>

<style scoped>
.sider-inner {
    display: flex;
    flex-direction: column;
    height: 100vh;
}
.sider-menu {
    flex: 1;
    overflow-y: auto;
}
.sider-footer {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    color: #fff;
    padding: 14px 0;
    cursor: pointer;
    border-top: 1px solid rgba(255, 255, 255, 0.1);
    transition: background 0.2s;
}
.sider-footer:hover {
    background: rgba(255, 255, 255, 0.08);
}
.sider-footer-text {
    white-space: nowrap;
}
</style>