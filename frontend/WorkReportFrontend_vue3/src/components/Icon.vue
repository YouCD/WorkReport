<template>
  <component :is="icon" :style="$attrs as StyleValue "/>
</template>

<script setup lang="ts">
/* eslint-disable vue/no-setup-props-destructure */
import {StyleValue} from 'vue'

const {icon} = defineProps<{
  icon: any
}>()
</script>

<style scoped></style>
