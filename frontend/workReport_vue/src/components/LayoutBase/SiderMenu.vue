<template>
    <a-menu theme="dark" mode="inline" :inline-collapsed="collapsed" class="sider-menu" @click="onMenuClick">
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
</template>

<script setup lang="ts">
import { UnorderedListOutlined } from '@ant-design/icons-vue'
import '@/assets/iconfont/iconfont.css'

defineProps<{ collapsed?: boolean }>()

const emit = defineEmits<{ (e: 'navigate'): void }>()

function onMenuClick() {
    emit('navigate')
}
</script>

<style scoped>
.sider-menu {
    width: 100%;
}
</style>
