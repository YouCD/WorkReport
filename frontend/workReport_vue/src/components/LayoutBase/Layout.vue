<template>
  <a-layout id="components-layout-demo-responsive">
    <Sider ref="siderRef"></Sider>
    <a-layout>
      <a-layout-header class="layout-header">
        <div class="header-inner">
          <a-button v-if="isMobile" type="text" class="menu-toggle" aria-label="打开菜单" @click="siderRef?.toggleDrawer()">
            <MenuUnfoldOutlined/>
          </a-button>
          <!--                    <a v-if="msg.flag" style="float:right;padding-right: 20px" :href="msg.data.download_url">{{msg.msg}}</a>-->
          <a v-if="showUpdate" style="float:right;padding-right: 20px" @click="UpdateHandler">{{ msg }}</a>
          <a v-if="!showUpdate" style="float:right;padding-right: 20px">{{ msg }}</a>
          <!--                    <msg :lists="msg" style="float:right;padding-right: 20px"></msg>-->
        </div>
      </a-layout-header>
      <a-layout-content class="layout-content">
        <div class="layout-content-inner">
          <router-view :key="$route.fullPath"/>
        </div>

      </a-layout-content>
      <a-layout-footer class="layout-footer">
        WorkLogSystem ©2021 Created by YouCD
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>
<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import Sider from '@/components/LayoutBase/Sider.vue'
import { UserOutlined, MenuUnfoldOutlined } from '@ant-design/icons-vue'
import { useIsMobile } from '@/utils/useIsMobile'

import { UpdateCheck } from "@/components/api/worklog";

const router = useRouter()
const route = useRoute()

const { isMobile } = useIsMobile()
const siderRef = ref<InstanceType<typeof Sider> | null>(null)

const uid = ref("我是谁？")
const breadList = ref<unknown[]>([])
const updateUrl = ref("")
const msg = ref<unknown>(undefined)
const showUpdate = ref(false)
const websock = ref<WebSocket | undefined>(undefined)

function onCollapse(_collapsed: boolean, _type: string) {
  // console.log(collapsed, type);
}
function onBreakpoint(_broken: boolean) {
  // console.log(broken);
}
function localStorageUID() {
  uid.value = localStorage.getItem("uid") || ""
}
function logout() {
  router.push('/login')
}

function UpdateCheckHandler() {
  UpdateCheck().then(res => {
    if (res.data.flag) {
      msg.value = res.data.msg
      showUpdate.value = res.data.flag
    } else if (!res.data.flag) {
      msg.value = res.data.msg
      showUpdate.value = res.data.flag
    }
  })
}
function UpdateHandler() {
  initWebSocket();
}

function getBreadcrumb() {
  breadList.value = []
  route.path.split("/").splice(1).forEach(item => {
  })
  route.matched.forEach(item => {
  })
}

function initWebSocket() {
  let wsUrl: string | undefined
  let protocol: string | undefined

  if (window.location.protocol === "http:") {
    protocol = "ws://"
  } else if (window.location.protocol === "https:") {
    protocol = "wss://"
  }

  if (import.meta.env.VUE_APP_API_ROOT) {
    wsUrl = protocol + import.meta.env.VUE_APP_API_ROOT.split("//")[1] + "/w/update";
  } else {
    wsUrl = protocol + window.location.host + "/w/update"
  }

  websock.value = new WebSocket(wsUrl);
  websock.value.onmessage = websocketOnMessage;
  websock.value.onopen = websocketOnOpen;
  websock.value.onerror = websocketOnError;
  websock.value.onclose = websocketClose;
}
function websocketOnOpen() {
  let actions = { "test": "12345" };
  websocketSend(JSON.stringify(actions));
}
function websocketOnError() {
  initWebSocket();
}
function websocketOnMessage(e: MessageEvent) {
  msg.value = e.data
}
function websocketSend(Data: string) {
  websock.value!.send(Data);
}
function websocketClose(e: CloseEvent) {
  console.log('断开连接', e);
}

onMounted(() => {
  localStorageUID()
  getBreadcrumb()
  UpdateCheckHandler()
})

watch(route, () => {
  getBreadcrumb()
})
</script>

<style scoped>
#components-layout-demo-responsive {
    min-height: 100vh;
}

.layout-header {
    background: #fff;
    padding: 0;
}

.header-inner {
    padding: 0 23px;
}

.menu-toggle {
    margin-right: 12px;
    font-size: 18px;
}

.layout-content {
    margin: 24px 16px 0;
}

.layout-content-inner {
    padding: 12px;
    min-height: 790px;
    height: 100%;
}

.layout-footer {
    text-align: center;
}

@media (max-width: 768px) {
    .header-inner {
        padding: 0 8px;
    }

    .layout-content {
        margin: 8px;
    }

    .layout-content-inner {
        padding: 8px;
        min-height: 0;
    }

    .layout-footer {
        padding: 12px 0;
        font-size: 12px;
    }
}
</style>
