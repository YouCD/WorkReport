<template>
    <a-layout-sider
            v-if="!isMobile"
            class="app-sider"
            v-model:collapsed="collapsed"
            breakpoint="lg"
            collapsible
            :trigger="null"
            width="220"
            @collapse="onCollapse"
            @breakpoint="onBreakpoint"
    >
        <div class="sider-inner">
            <div class="sider-menu-wrap">
                <SiderMenu :collapsed="collapsed"/>
            </div>
            <div class="sider-footer" @click="toggleCollapsed">
                <MenuFoldOutlined v-if="!collapsed" />
                <MenuUnfoldOutlined v-else />
                <span v-if="!collapsed" class="sider-footer-text">收起</span>
            </div>
        </div>
    </a-layout-sider>
    <a-drawer
            v-else
            v-model:open="drawerOpen"
            placement="left"
            :width="260"
            :closable="false"
            :body-style="{ padding: 0, background: '#001529' }"
    >
        <SiderMenu @navigate="drawerOpen = false"/>
    </a-drawer>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import {
    MenuFoldOutlined,
    MenuUnfoldOutlined,
} from '@ant-design/icons-vue'
import SiderMenu from '@/components/LayoutBase/SiderMenu.vue'
import { useIsMobile } from '@/utils/useIsMobile'

const { isMobile } = useIsMobile()

const collapsed = ref(false)
const drawerOpen = ref(false)
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
function toggleDrawer() {
    drawerOpen.value = !drawerOpen.value
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

defineExpose({ toggleDrawer })
</script>

<style scoped>
/* 整页滚动时让 sider 固定在视口, 收起按钮不随页面滚动 */
.app-sider {
    position: sticky;
    top: 0;
    height: 100vh;
    align-self: flex-start;
}
.sider-inner {
    display: flex;
    flex-direction: column;
    height: 100vh;
}
.sider-menu-wrap {
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
