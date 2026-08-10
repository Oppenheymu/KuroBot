# bridge/embedded 设计（@kurobot/bridge-embedded）

> 本文件是包级设计文档（AGENTS.md：写代码前先更新对应包的 `docs/design.md`，设计先行）。

## 职责

嵌入式瘦身对端（`mode=embedded` 默认形态）：

- 复用 `bridge/core` 框架，作为对端连接 kurobot 的 WS 服务端。
- 内嵌 **napukettoqq** 协议端（QQ 连接，控制台扫码）。
- **无 Koishi**：esbuild 单文件产物，随 JAR 分发（`tools/embed` 打包）。

## 与架构的关系（ADR-005 / ADR-006）

- embedded / external 只是打包差异：本包 = "协议端在 JAR 里"的形态；
  external 形态由 koishi-plugin-kurobot 承担，两者复用同一 core。
- `wrapper.node`（腾讯闭源）不进 JAR，运行期从 QQ 安装目录发现拷贝（ADR-014）。

## 目录规划

```
src/
├── index.ts       # 入口：拉起 core 客户端 + napukettoqq（嵌入引导）
└── bootstrap.ts   # 子进程引导（stdin EOF 自杀 / Watchdog / 崩溃兜底）
```

## 实现顺序（STATUS.md 第 4 步细化）

1. core 客户端接入（对端角色：连接/握手/心跳/重连）。
2. napukettoqq 嵌入引导。
3. 子进程生命周期（stdin EOF 自杀 + PID + Watchdog）。

## 依赖

- `@kurobot/bridge-core`（workspace:*）。
- `@kurobot/protocol`（workspace:*）。
- esbuild（devDep，单文件打包）。
