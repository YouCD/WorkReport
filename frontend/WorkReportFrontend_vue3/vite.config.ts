import {ConfigEnv, UserConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import qiniu from 'qiniu'
import {qiniuPlugin} from '@puhaha/vite-plugin-upload-oss'
import fs from "fs";


// https://vitejs.dev/config/
// export default defineConfig({
//     resolve: {
//         // 别名配置
//         alias: {
//           "@":path.resolve(__dirname,"src")
//         }
//     },
//     plugins: [vue()],
// })


// 将静态文件上传到七牛云CDN   不需要可以使用上面默认的配置，qiniuKey.json 是配置文件
const realFile = path.resolve(__dirname, './qiniuKey.json')

function qiniuConfig(callback) {
    fs.readFile(realFile, (err, buffer) => {
        if (err) {
            callback(err, null);
        } else {
            let d = buffer.toString();
            let config = JSON.parse(d);
            callback(null, config);
        }
    });
}

export default async ({command, mode}: ConfigEnv): Promise<UserConfig> => {
    const isBuild = command === 'build' && mode === "pro";
    let config = null;
    await new Promise<void>((resolve, reject) => {
        qiniuConfig((err, result) => {
            if (err) {
                console.error(err);
                reject(err);
            } else {
                config = result;
                resolve();
            }
        });
    });

    return {
        resolve: {
            // 别名配置
            alias: {
                "@": path.resolve(__dirname, "src")
            }
        },
        base: isBuild ? 'https://cdn.youcd.online' : "",
        plugins: [
            isBuild && qiniuPlugin({
                sdk: qiniu,
                accessKey: config.accessKey,
                secretKey: config.secretKey,
                bucket: config.bucket,
                publicCdnPath: config.publicCdnPath,
                filePath: './dist/assets',
                remoteFilePath: 'assets/',
                openConfirm: false,
                enabledRefresh: true,
                uploadTarget:path.resolve(__dirname, './dist'),
                appName:"WorkReport"
            }),
            vue()
        ]
    }
}