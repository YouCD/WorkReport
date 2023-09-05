<script setup lang="ts">
//  汉化
import {ref} from "vue";
import zhCN from "ant-design-vue/es/locale/zh_CN";

const locale = ref(zhCN);
</script>

<template>
  <!--   汉化-->
  <a-config-provider :locale="locale">
    <router-view/>
  </a-config-provider>
</template>

<style scoped>

</style>
