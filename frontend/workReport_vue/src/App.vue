<template>
    <ProgressProvider color="#29d" :options="progressOptions">
        <RouterProgress />
        <div id="app">
            <router-view/>
        </div>
    </ProgressProvider>
</template>

<script setup lang="ts">
import { defineComponent } from 'vue';
import { ProgressProvider, useProgress } from '@bprogress/vue';
import router from '@/router';

// bprogress 配置(替代原 NProgress.configure)
const progressOptions = {
    easing: 'ease',  // 动画方式
    speed: 500,  // 递增进度条的速度
    showSpinner: false, // 是否显示加载ico
    trickleSpeed: 200, // 自动递增间隔
    minimum: 0.3 // 初始化时的最小百分比
}

// useProgress 依赖 ProgressProvider 的注入, 必须在 provider 的子组件内调用
const RouterProgress = defineComponent({
    setup() {
        const { start, stop } = useProgress()
        // 每次切换页面时，调用进度条
        router.beforeEach(() => start())
        // 在即将进入新的页面组件前，关闭掉进度条
        router.afterEach(() => stop())
        return () => null
    },
})
</script>

<style>
html,
body {
    -webkit-tap-highlight-color: transparent;
}
</style>
