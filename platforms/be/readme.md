# platforms/be —— BE 服务端家族（基岩版）

> BE 服务端生态：LeviLamina（LSE 脚本，TS 路线）+ Endstone（C++/Python 薄壳）。

## 子目录

| 目录 | 服务端 | 技术栈 | 状态 |
|---|---|---|---|
| `lse/` | LeviLamina（LSE） | TS → JS（QuickJS） | ✅ 骨架已建（ADR-012） |
| `endstone/` | Endstone | **C++ 薄壳** + 内嵌 Node | 🚧 预留骨架 |

## 设计原则

- **LSE**：复用 `bridge/core`（平台无关，ADR-007），TS 直接跑。
- **Endstone**：C++ 薄壳（事件桥接 + 内嵌 Node 子进程 + JSON-lines IPC），与 Java 薄壳**同构**（ADR-020），业务仍走 `bridge/core`。

## 排期

- `lse/` 实现排在 JE 闭环后（见 docs/STATUS.md 待定事项）。
- `endstone/` 为预留骨架，启动时间另行评估。
