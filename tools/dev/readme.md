# tools/dev —— 开发沙盒脚本

> 本目录是开发工具（非交付物）。运行产物一律放 `sandbox/`（已 gitignore）。

## 用途（规划）

- 本地起 Paper 服务端（sandbox/je）联调。
- 本地起 Koishi 实例（sandbox/koishi）连 kurobot。
- 协议对端模拟（写测试时的最小 WS 客户端）。

## 说明

- 骨架阶段：暂无脚本。建包是为了让 `tools/*` 在 pnpm workspace 中有身份。
