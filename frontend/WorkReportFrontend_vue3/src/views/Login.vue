<template>
  <div class="outer-wrap">
    <div class="loginBox">
      <div class="login-panel">
        <a-form
          :model="data.loginFrom"
          name="basic"
          :label-col="{ span: 8 }"
          :wrapper-col="{ span: 16 }"
          autocomplete="off"
          @finish="onFinish"
          @finish-failed="onFinishFailed"
        >
          <a-form-item
            label="账户"
            name="username"
            :rules="[{ required: true, message: '请输入账户' }]"
          >
            <a-input v-model:value="data.loginFrom.username">
              <template #prefix>
                <UserOutlined style="color: rgba(0, 0, 0, 0.25)" />
              </template>
            </a-input>
          </a-form-item>

          <a-form-item
            label="密码"
            name="password"
            :rules="[{ required: true, message: '请输入密码!' }]"
          >
            <a-input-password v-model:value="data.loginFrom.password">
              <template #prefix>
                <LockOutlined style="color: rgba(0, 0, 0, 0.25)" />
              </template>
            </a-input-password>
          </a-form-item>
          <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
            <a-button
              class="login-form-button"
              type="primary"
              html-type="submit"
            >
              登入
            </a-button>
          </a-form-item>
        </a-form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { LoginHandler, LoginRequestData } from '@/types/login'
import { useRouter } from 'vue-router'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'

let data = reactive({
  labelCol: { span: 4 },
  wrapperCol: { span: 20 },
  loginFrom: {
    username: '',
    password: '',
  } as LoginRequestData,
})

const router = useRouter()

const onFinish = async () => {
  const result = await LoginHandler(data.loginFrom)
  if (result.flag) {
    localStorage.setItem('jwt', result.data.token)
    await router.push('/')
  }
}
const onFinishFailed = (errorInfo: any) => {
  console.log('Failed:', errorInfo)
}
</script>

<style scoped>
.outer-wrap {
  /*只有同时为html和body设置height: 100%时，这里才生效，
    并且随浏览器窗口变化始终保持和浏览器视窗等高*/
  height: 100vh;
  background-image: url('/src/assets/bg.png');
  background-size: cover;
  background-position: center center;
}

.loginBox {
  width: 500px;
  height: 300px;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  margin: auto;
  border-radius: 10px;
  position: absolute;
  background-color: rgba(255, 255, 255, 0.9);
}

.login-panel {
  width: 400px;
  height: 200px;
  top: 25%;
  position: absolute;
}

.login-form-button {
  width: 100%;
}
</style>
