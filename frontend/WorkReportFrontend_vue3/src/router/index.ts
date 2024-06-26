import {createRouter, createWebHashHistory, RouteRecordNormalized, RouteRecordRaw,} from 'vue-router'
import Layout from '@/views/Layout.vue'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {PieChartOutlined, SettingFilled, SoundFilled, TabletFilled, ToolFilled,} from '@ant-design/icons-vue'

const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'home',
        component: Layout,
        redirect: {name: '图表'},
        meta: {id: 1,},
        children: [
            {
                path: 'log',
                name: '日志',
                meta: {
                    icon: TabletFilled,
                    show: true,
                    id: 2,
                },
                redirect: {name: '浏览日志'},
                children: [
                    {
                        name: '浏览日志',
                        path: 'LogBrowse',
                        meta: {icon: SoundFilled, show: true, id: 3,},
                        component: () => import('@/views/log/LogBrowse.vue'),
                    },
                ],
            },
            {
                path: 'setting',
                name: '设置',
                meta: {icon: SettingFilled, show: true, id: 4,},
                redirect: {name: '字典设置'},
                children: [
                    {
                        name: '字典设置',
                        path: 'DicConfig',
                        meta: {icon: ToolFilled, show: true, id: 5,},
                        component: () => import('@/views/setting/DicConfig.vue'),
                    },
                ],
            },
            {
                path: 'chart',
                name: 'Chart',
                meta: {menu: '报表', show: false, id: 6},
                redirect: {name: '图表'},
                children: [
                    {
                        name: '图表',
                        path: 'Chart',
                        meta: {icon: PieChartOutlined, show: false, id: 7},
                        component: () => import('@/views/chart/Chart.vue'),
                    },
                ],
            },
        ],
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue'),
    },
]

const router = createRouter({
    history: createWebHashHistory(),
    routes,
})

NProgress.configure({
    easing: 'ease', // 动画方式
    speed: 1000, // 递增进度条的速度
    showSpinner: false, // 是否显示加载ico
    trickleSpeed: 200, // 自动递增间隔
    minimum: 0.3, // 更改启动时使用的最小百分比
    parent: 'body', //指定进度条的父容器
})

// 每次跳转页面前显示进度条
router.beforeEach((to, from, next) => {
    //全局进度条的配置
    NProgress.configure({
        easing: 'ease', // 动画方式
        speed: 1000, // 递增进度条的速度
        showSpinner: false, // 是否显示加载ico
        trickleSpeed: 200, // 自动递增间隔
        minimum: 0.3, // 更改启动时使用的最小百分比
        parent: 'body', //指定进度条的父容器
    })
    NProgress.start()
    if (to.path === '/login') {
        next()
        return
    }

    //  避免进入不存在的路由 导致 白屏
    if (to.matched.length === 0) {
        //如果未匹配到路由
        from.name ? next({name: from.name}) : next('/') //如果上级也未匹配到路由则跳转登录页面，如果上级能匹配到则转上级路由
        return
    }
    next()
})

// 路由加载后隐藏进度条
router.afterEach(() => {
    NProgress.done()
})

// router.beforeEach((pre, next) => {})

// router.afterEach(() => {})

export default router

interface MenuItem {
    id: number
    path: string
    name: string
    icon?: any
    children?: MenuItem[]
    show?: boolean
}

const getMenuFromRouter = (router: Array<RouteRecordRaw>): MenuItem[] => {
    const menu: MenuItem[] = []

    router.forEach((route) => {
        const item: MenuItem = {
            path: route.path,
            name: route.name as string,
            icon: route.meta?.icon,
            children: [],
            id: route.meta?.id as number,
            show: route.meta?.show as boolean,
        }

        if (route.children && route.children.length > 0) {
            item.children = getMenuFromRouter(
                route.children as RouteRecordNormalized[],
            )
        }

        menu.push(item)
    })

    return menu
}

// 使用
const a = getMenuFromRouter(routes)
export const menu: MenuItem[] = []
a.forEach((item) => {
    if (item.path === '/') {
        const ccc: MenuItem[] = []
        item.children?.forEach((item) => {
            if (item.show) {
                ccc.push(item)
            }
        })

        menu.push(...ccc)

        // menu = item.children as MenuItem[]
    }
})
