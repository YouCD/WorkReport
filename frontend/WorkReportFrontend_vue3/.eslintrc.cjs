module.exports = {
    "env": {
        "browser": true,
        "es2021": true,
        "node": true,
    },
    "extends": [
        'eslint-config-prettier',
        "eslint:recommended",
        "plugin:vue/vue3-recommended",
        "plugin:@typescript-eslint/recommended",
        'plugin:prettier/recommended',
    ],
    "overrides": [
    ],
    "parser": 'vue-eslint-parser',
    "parserOptions": {
        "ecmaVersion": "latest",
        "sourceType": "module",
        "parser": '@typescript-eslint/parser',
    },
    "plugins": [
        "vue",
        "@typescript-eslint"
    ],
    "rules": {
        "vue/multi-word-component-names": "off", // 禁用vue文件强制多个单词命名
        "@typescript-eslint/no-explicit-any": ["off"], //允许使用any
        // 'vue/script-setup-uses-vars': 'error', // 检查setup中使用的变量是否声明
        "vue/prop-name-casing": ["off"],  // 关闭命名检查
        "vue/require-default-prop":["off"],// 关闭 prop 默认值检查
    }
}
