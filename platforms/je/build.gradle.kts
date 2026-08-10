plugins {
    java
    id("com.gradleup.shadow") version "9.0.0"
    id("com.diffplug.spotless") version "7.0.2"
}

group = "com.kurobot"
version = "0.1.0"
description = "KuroBot MC 服务器 ↔ 社交平台群服互通插件（Java 薄壳）"

repositories {
    // Paper API（compileOnly）
    maven("https://repo.papermc.io/repository/maven-public/")
    mavenCentral()
}

dependencies {
    // Paper API 只编译期使用（薄壳不依赖运行时服务端 API 之外的东西）
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    // JSON-lines IPC 解析（Java ↔ Node 子进程）
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.0")

    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    // 工具链 JDK 25（mise 统一），字节码 target 21（Paper 服务端运行时基线，ADR-015）
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
    withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 21 // ADR-015：target 21 兼容 Paper 运行时
    options.compilerArgs.addAll(listOf("-Xlint:all", "-Werror")) // 苛刻度（ADR-011）
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

// 苛刻度：Spotless(Palantir)（ADR-011）
spotless {
    java {
        palantirJavaFormat()
        target("src/**/*.java")
    }
}

// 全链路构建（AGENTS.md 构建顺序）：TS 构建 → tools/embed → shadowJar
tasks.shadowJar {
    archiveFileName.set("kurobot-${project.version}.jar")
    manifest {
        attributes("Implementation-Title" to "KuroBot", "Implementation-Version" to project.version)
    }
}

// 本地全链路构建（与根 pnpm build:jar 对应）
tasks.register("kurobotBuild") {
    dependsOn(tasks.shadowJar)
}
