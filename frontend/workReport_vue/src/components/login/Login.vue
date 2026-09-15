<template>
    <div class="login-page">
        <div class="login-card">
            <a-form
                :model="formState"
                :rules="rules"
                ref="loginForm"
                @finish="handleFinish"
                class="login-form"
            >
                <a-form-item label="账户" name="username">
                    <a-input v-model:value="formState.username">
                        <template #prefix>
                            <UserOutlined style="color: rgba(0,0,0,.25)"/>
                        </template>
                    </a-input>
                </a-form-item>
                <a-form-item label="密码" name="password">
                    <a-input-password v-model:value="formState.password">
                        <template #prefix>
                            <LockOutlined style="color: rgba(0,0,0,.25)"/>
                        </template>
                    </a-input-password>
                </a-form-item>
                <a-form-item>
                    <a-button type="primary" html-type="submit" class="login-form-button">
                        登入
                    </a-button>
                </a-form-item>
            </a-form>
        </div>
    </div>
</template>

<script setup lang="ts">
    import { reactive, ref, onBeforeMount } from 'vue'
    import { useRouter, useRoute } from 'vue-router'
    import { message } from 'ant-design-vue'
    import { Login } from '../api/user'
    import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'

    const router = useRouter()
    const route = useRoute()

    const loginForm = ref()

    const formState = reactive<{ username: string | undefined, password: string | undefined }>({
        username: undefined,
        password: undefined,
    })

    const rules = {
        username: [{ required: true, message: '请输入用户名' }],
        password: [{ required: true, message: '请输入密码' }],
    }

    onBeforeMount(() => {
        localStorage.clear()
    })

    const handleFinish = (values: any) => {
        Login(values).then(res => {
            if (res.data.flag) {
                message.success(res.data.msg);
                localStorage.setItem('jwt', res.data.data.token);
                localStorage.setItem('uid', res.data.data.uid);
                const redirect = route.query.redirect as string;
                if (redirect) {
                    router.push(redirect)
                } else {
                    router.push('/')
                }
            } else if (!res.data.flag) {
                message.success(res.data.msg);
            }
        });
    }
</script>
<style scoped>
    .login-page {
        min-height: 100vh;
        background: url('../../assets/background.png') no-repeat center center / cover;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .login-card {
        width: 420px;
        max-width: 90vw;
        box-sizing: border-box;
        padding: 48px 32px;
        background: rgba(255, 255, 255, 0.72);
        backdrop-filter: blur(10px);
        -webkit-backdrop-filter: blur(10px);
        border-radius: 16px;
        box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
    }

    .login-form {
        width: 100%;
    }

    .login-form-button {
        width: 100%;
        height: 48px;
        font-size: 16px;
        border-radius: 8px;
    }
</style>
