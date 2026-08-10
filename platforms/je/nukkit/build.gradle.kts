// :nukkit —— Nukkit / PowerNukkitX 服务端适配（预留骨架，尚未实现）
// 依赖 :core（纯逻辑）+ Nukkit API（compileOnly）
// 说明：Nukkit 虽服务 BE 客户端，但它是 **Java** 服务端（非 LSE 脚本世界，
//       ADR-019 技术栈划分），插件形态为 Nukkit 插件（主类 extends PluginBase），
//       注册监听器用 Nukkit 的 PluginManager，业务仍走 :core。

plugins {
    id("com.gradleup.shadow")
}

dependencies {
    implementation(project(":core"))
    // TODO(nukkit)：接入 Nukkit API 后启用
    // compileOnly("cn.nukkit:plugin-api:1.0.0")
    // repositories 需加 NukkitX Maven：maven("https://repo.nukkitx.com/main/")
}

// 苛刻度：Spotless(Palantir)（ADR-011）
spotless {
    java {
        palantirJavaFormat()
        target("src/**/*.java")
    }
}
