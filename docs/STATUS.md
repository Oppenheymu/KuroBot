# KuroBot 现状与路线（STATUS）

> 借鉴 NapukettoQQ 的 `STATUS.md` 形态：现状 + 关键决策点 + 下一步。开始任何工作前先读本文。

## 当前状态（2026-08-10）

**设计已定稿**（见 `architecture.md` + `DECISIONS.md`），仓库为空骨架，尚未开始代码。

- 已建：`readme.md`、`AGENTS.md`、`docs/architecture.md`、`docs/DECISIONS.md`、`docs/STATUS.md`、`docs/protocol/draft-v0.1.md`、仓库骨架（biome/tsconfig/package.json 等）、`bridge/protocol` 包。

## 关键决策点（已拍板，勿再翻烧饼）

| # | 决策 | 要点 |
|---|---|---|
| 1 | 单仓 monorepo | ADR-001 |
| 2 | Java 21 | ADR-002 |
| 3 | 协议 `kurobot-ws` + 双层版本 | ADR-003 |
| 4 | 绑定频道随 hello + bindingsUpdated | ADR-004 |
| 5 | 业务核心在 Node（TS），Java 薄壳 | ADR-005 |
| 6 | external 也拉 Node | ADR-006 |
| 7 | core 平台无关（零 Node API） | ADR-007 |
| 8 | SSOT 用 zod（非 TypeBox） | ADR-008 |
| 9 | 不引入 nx/turbo | ADR-009 |
| 10 | IPC = stdin/stdout JSON-lines | ADR-010 |
| 11 | Java 第一版不上 Error Prone/NullAway | ADR-011 |
| 12 | LSE TS 化（@levimc-lse/types） | ADR-012 |
| 13 | 测试栈 vitest + JUnit 5；fast-check 二期 | ADR-013 |
| 14 | 嵌入式打包沿用 Napuketto 许可证方案 | ADR-014 |
| 15 | 工具链升级：Node 26 + Java 25（target 21） | ADR-015 |
| 16 | 运行时不用 Bun | ADR-016 |
| 17 | 剔除 CI/CD，本地门禁（lefthook + pnpm check） | ADR-017 |
| 18 | koishi 插件独立仓库（不在本仓库内） | ADR-018 |
| 19 | platforms/je 多模块（:core + 各服务端模块，含 fabric/velocity 预留） | ADR-019 |

## 待定事项

- 协议 `kurobot-ws` 具体消息 schema 逐字段定稿（`bridge/protocol` 下一步细化，含 zod 源）。
- 独立仓库 koishi-plugin-kurobot 的建立时间与 Koishi 版本基线（v4 稳定版）——JE 闭环后启动。
- `platforms/be` 的启动时间表（JE 闭环后再排）。

## 下一步实现顺序（推荐）

```
0. 仓库骨架：biome.json / tsconfig.json / package.json / pnpm-workspace.yaml /
   vitest.config.ts / .editorconfig / mise.toml / lefthook.yml / CI 空跑
   （直接借鉴 NapukettoQQ 的配置体系）
1. bridge/protocol：zod schema SSOT（@kurobot/protocol 包）+ draft 说明同步
2. bridge/core 最小闭环：connect → hello 握手 → 心跳 → chat 收发 → 重连
   （传输层抽象，先写 Node 实现，QuickJS 适配后续）
3. platforms/je 薄壳：IPC 客户端 + 进程管理 + 事件/命令/权限桥接
4. tools/embed 嵌入式打包 + sandbox 沙盒联调
5. koishi-plugin-kurobot：独立仓库（ADR-018），复用 `@kurobot/protocol` 发布版本
6. platforms/be：LSE TS 适配（复用 bridge/core）
```
