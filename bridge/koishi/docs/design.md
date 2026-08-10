# bridge/koishi 设计（koishi-plugin-kurobot）

> 本文件是包级设计文档（AGENTS.md：写代码前先更新对应包的 `docs/design.md`，设计先行）。

## 职责

KuroBot 的 Koishi 插件：作为 `kurobot-ws` 的**对端**连接 kurobot 服务器，把游戏事件转发到 Koishi 生态（QQ/TG/Discord…），把群消息发回服务器。

- **平台渲染只出现在这里**（AGENTS.md 硬约束：富文本/颜色码/长度收敛只在壳层）。
- Koishi 宽类型在壳层收敛（同 Napuketto 排除 koishi-plugin-adapter 的道理）。

## 硬性约束

- 依赖方向：core + protocol（只允许向下依赖）。
- 平台渲染（MC 颜色码 → 平台富文本、长度收敛）只在此包实现，不回流 core。

## 目录规划

```
src/
├── index.ts           # 插件入口（Koishi Context 注册）
├── client.ts          # 连接 kurobot 的 WS 客户端（复用 core 框架）
├── render/            # 平台渲染：MC 颜色码 → 平台格式 / 长度收敛
└── handlers/          # 事件↔消息映射（game→session / session→game）
```

## 实现顺序（STATUS.md 第 5 步细化）

1. 连接管理（复用 core 的客户端框架）。
2. 游戏事件 → 群消息渲染。
3. 群消息/指令 → 服务器（含转发规则校验在 core 侧）。
4. 平台渲染收敛。

## 依赖

- `@kurobot/bridge-core`（workspace:*）——客户端框架。
- `@kurobot/protocol`（workspace:*）——消息 schema。
- `koishi`（peerDependency）。
