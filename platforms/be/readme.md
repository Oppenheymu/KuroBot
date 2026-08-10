# platforms/be —— LeviLamina（LSE）平台适配（占位）

> 排期在 JE 闭环之后（见 docs/STATUS.md 待定事项）。

## 形态（已拍板，ADR-012）

- **LSE TS 化**：TS 源码 → 编译为 JS → 由 LeviLamina 加载（LSE 内核是 QuickJS，不原生跑 TS）。
- 使用官方 TS 声明 `@levimc-lse/types` + 脚手架 `@levimc-lse/scaffold`。
- **复用 `bridge/core`**（平台无关，ADR-007）：核心协议逻辑零改动，只做 QuickJS 环境的传输层/logger 适配。

## 骨架阶段说明

本目录暂无构建配置——`bridge/core` 平台无关性设计（target ES2020 + 零 Node API）已为其铺路；
待 JE 闭环后按 STATUS.md 第 6 步搭建。
