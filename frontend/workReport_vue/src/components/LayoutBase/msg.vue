<template>
    <div class="wrap">
        <div ref="box" class="box">
            <div ref="marquee" class="marquee">{{text}}</div>
            <div ref="copy" class="copy"></div>
        </div>
        <div ref="node" class="node">{{text}}</div>
    </div>
</template>
<script setup lang="ts">
    import { ref, onMounted, onUpdated } from 'vue'

    const props = defineProps<{ lists: string[] }>()

    const text = ref('')
    const box = ref<HTMLElement>()
    const marquee = ref<HTMLElement>()
    const copy = ref<HTMLElement>()
    const node = ref<HTMLElement>()

    function move() {
        let width = node.value!.getBoundingClientRect().width
        copy.value!.innerText = text.value
        let distance = 0
        setInterval(() => {
            distance = distance - 1
            if (-distance >= width) {
                distance = 16
            }
            box.value!.style.transform = 'translateX(' + distance + 'px)'
        }, 20)
    }

    onMounted(() => {
        console.log(props.lists)
        for (let i = 0; i < props.lists.length; i++) {
            console.log(props.lists[i])
            text.value += ' ' + props.lists[i]
        }
    })

    onUpdated(() => {
        move()
    })
</script>
<style scoped>

    .wrap {
        overflow: hidden;
    }

    .box {
        width: 4000px;
    }

    .box div {
        float: left;
    }

    .marquee {
        margin: 0 16px 0 0;
    }

    .node {
        position: absolute;
        z-index: -999;
        top: -999999px;
    }
</style>
