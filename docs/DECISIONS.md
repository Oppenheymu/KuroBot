# KuroBot 决策记录（DECISIONS）

> 借鉴 NapukettoQQ 的 `DECISIONS.md` 形态。每项决策记录背景 → 选项 → 结论 → 理由。编号按时间序，永不改写历史。

---

## ADR-001 单仓 monorepo（2026-08-10）

- **背景**：协议 SSOT 需要 Java/TS 两端同步；嵌入式打包跨 `bridge/embedded` 与 `platforms/je`。
- **选项**：单仓 / 双仓（je 与 bridge 分离）。
- **结论**：**单仓**。
- **理由**：改协议源 + 两端消费方一个 PR 原子完成；跨仓 CI 联动成本高；协议防漂移门禁单仓内一行命令。代价是权限粒度粗，对个人开源项目可接受。

## ADR-002 Java 版本 21（2026-08-10）

- **结论**：Java 21 LTS。Paper 1.20.5+ 已是 Java 21 时代；虚拟线程对 WS 长连接有价值。

## ADR-003 协议命名与双层版本（2026-08-10）

- **结论**：协议代号 `kurobot-ws`。
- **版本机制**：WS 子协议 `Sec-WebSocket-Protocol: kurobot-ws.v1` 声明大版本（不兼容变化，握手期直接拒绝对端）；`hello` 消息内 `protocolVersion`（语义化 `0.1.0`）做小版本/能力协商。
- **理由**：比单纯握手协商更早暴露不兼容；对端（Koishi 侧）报错更友好。

## ADR-004 绑定频道列表随 hello 上报 + bindingsUpdated（2026-08-10）

- **结论**：`hello` 携带 `channelBindings`（对端无需读 kurobot 配置即可自动发现频道）；协议保留 `bindingsUpdated` 增量事件，服主运行期改配置时通知对端。

## ADR-005 业务核心移入 Node，Java 薄壳化（2026-08-10，架构关键变更）

- **背景**：开发者是 TS 开发者、未写过 Java；Paper 插件硬约束是 JVM 语言，但 kurobot 反正要内嵌 Node。
- **结论**：绑定/白名单/权限/转发规则等业务全部移入 `bridge/core`（TS）；Java 退化为 Bukkit 桥接薄壳（事件/命令/权限/IPC/进程管理，~几百行模板代码）。
- **理由**：开发量 90% 落在 TS；早期「业务在 Java config」结论作废。

## ADR-006 external 模式也拉 Node（2026-08-10）

- **结论**：两种模式都拉起内嵌 Node 跑 `bridge/core`（WS 服务端在 Node 里）；external 仅表示「不附带协议端」。
- **理由**：业务核心在 Node，external 也必须拉；两种模式的差异只剩协议端是否随 JAR 附带，架构复杂度反而下降。

## ADR-007 bridge/core 平台无关化（2026-08-10）

- **结论**：`bridge/core` 禁止 Node API（`ws`/`process`/`fs`/`pino`），传输层与 logger 为可注入接口，target ES2020 → Node / Koishi / LSE(QuickJS) 三端可跑。
- **理由**：JE/BE 双端复用同一套协议层；QuickJS 无 tslib/装饰器，`erasableSyntaxOnly` 保证产物可直接跑。

## ADR-008 协议 SSOT 用 zod（TypeBox → zod）（2026-08-10）

- **背景**：TypeBox 的核心价值是「schema → JSON Schema → 其他语言代码生成」。架构演进后 Java 走 JSON-lines IPC，不再需要协议 POJO。
- **结论**：SSOT 用 **zod**。schema 即产物（`z.infer`），无生成步骤；`safeParse()` 直接做协议层运行时验证；零依赖纯 JS，QuickJS 可跑；防漂移门禁从「生成检查」变为 lint（禁止手写消息类型）。
- **连带**：`gen-protocol` 只出 TS 类型，不再生成 Java POJO。

## ADR-009 多语言单仓不引入 nx/turbo（2026-08-10）

- **结论**：pnpm workspace 管 TS、Gradle 独立构建、根 `package.json` 只做编排；不引入 nx/turbo。
- **理由**：仅 5-6 个 TS 包，turbo 缓存收益是负资产；唯一跨语言桥 `tools/embed`（已删待重建）用构建顺序硬约束保证。

## ADR-010 Java↔Node IPC 用 stdin/stdout JSON-lines（2026-08-10）

- **结论**：Java 薄壳 ↔ Node 子进程走 stdin/stdout JSON-lines，零端口零配置；UUID 请求-响应 + 事件推送。
- **理由**：消灭本地端口冲突；Node 对外 WS 用动态端口 `listen(0)`，僵尸进程不再占端口。

## ADR-011 Java 第一版不上 Error Prone / NullAway（2026-08-10）

- **结论**：Java 薄壳第一版只上 `-Xlint:all -Werror` + Spotless(Palantir) + JUnit 5 + JaCoCo 存在性门禁（≥60%）。
- **理由**：几百行模板代码配 NullAway 性价比低；规范重心全押 TS 侧。薄壳定型演进后再评估。

## ADR-012 LSE TS 化（2026-08-10）

- **结论**：`platforms/be`（LeviLamina）用 TS 开发，经官方 `@levimc-lse/types` + `@levimc-lse/scaffold` 编译为 JS 后加载；复用 `bridge/core`。
- **理由**：LSE 原生 JS（QuickJS）不跑 TS，但官方把「TS → 编译 JS → LSE 加载」做成标准开发方式；开发者是 TS 开发者。

## ADR-013 测试栈（2026-08-10）

- **结论**：TS 侧 **vitest** 一统（protocol/core/embedded），coverage 用 v8 provider（core 设阈值，壳层放宽）；Java 侧 JUnit 5。**fast-check（property-based）列为二期 backlog**。
- **理由**：开发者熟练 vitest；property-based 对协议编解码/握手状态机有价值但不阻塞一期。

## ADR-014 嵌入式打包沿用 Napuketto 许可证方案（2026-08-10）

- **结论**：`node.exe`（MIT）进 JAR；`wrapper.node`（腾讯闭源）不进 JAR，运行期从 QQ 安装目录发现拷贝；stub 闭源件走 release 附带；动态端口；stdin EOF 自杀 + PID 文件 + Watchdog + 崩溃兜底。

## ADR-015 工具链升级：Node 26 + Java 25（2026-08-11）

- **背景**：Node 26 已是 Current（2026-10 进 LTS，@types/node 26 已在用）；Java 25 为 LTS（2025-09 发布）。
- **结论**：
  - Node 升 **26**（开发/CI 运行时）。
  - Java 工具链升 **25**，但 **Gradle 编译字节码 target 保持 21**（`-release 21`）——Paper 服务端运行时以 Java 21 为基线，target 21 保证插件在大多数服务端可加载，同时享受 25 工具链的编译期检查。
- **不引入 Bun 运行时**（ADR-016）。

## ADR-016 运行时不用 Bun（2026-08-11）

- **结论**：**Bun 不能替换 Node 作为 kurobot 的运行时**。
- **理由**：
  1. **napukettoqq 协议端**依赖 `wrapper.node`（腾讯闭源 NAPI 模块），必须由 Node 进程 `process.dlopen` 加载——Bun 的 Node-API 兼容层未经验证，闭源模块是高风险赌注。
  2. **Koishi 生态**绑定 Node（adapter 全家桶的 fs/net/worker 等边缘 API 在 Bun 下有差异，插件生态未验证）。
  3. **嵌入式运行时**打进 JAR 分发，Node 体积/兼容性经过验证；Bun 的 Windows 支持仍非一等公民。
  4. LSE 是 **QuickJS**——`bridge/core` 平台无关（ADR-007）已经保证了跨端，Bun 改变不了这一点，也没有收益。
- **Bun 的合理用途**（本项目不需要）：纯工具脚本 / dev 服务器——pnpm + Node 已满足。

## ADR-017 剔除 CI/CD（2026-08-11）

- **背景**：开发者从未使用过 CI/CD；单人开发、本地命令（`pnpm check` / `pnpm test`）与 CI 内容完全一致。
- **结论**：**删除 `.github/workflows/ci.yml`**，暂不引入 CI/CD；质量门禁由本地 lefthook（pre-commit）+ `pnpm check` 承担。
- **理由**：单人单机开发，CI 只是"多一道自动化保险"；协议防漂移门禁（ADR-008 的 lint 规则）本地同样生效。成本不为零（写一次不用管，但出了故障要排），收益当前不明显。
- **回退条件**：多人协作 / 开源贡献者介入 / 需要干净机器构建验证时，按 ADR-001 的构建顺序恢复（TS 构建 → 嵌入式打包 → gradle :paper:shadowJar）。

## ADR-018 koishi 插件独立仓库（2026-08-11）

- **背景**：koishi-plugin-kurobot 作为 `kurobot-ws` 的官方参考对端（external 形态），需要**独立发布 npm** 供任意 Koishi 实例 load；它是公开契约的对外实现，外部团队可对照协议实现自己的对端，我们无需关心其实现。
- **结论**：**`bridge/koishi` 从本仓库剔除**；koishi-plugin-kurobot 在**独立仓库**开发，依赖 `@kurobot/protocol` 的**发布版本**（非 workspace link）。
- **理由**：与协议「公开契约、对端只认协议」的设计（ADR-003）一致；独立版本节奏（不随 kurobot 主仓库发版）；`@kurobot/protocol` 本身也需独立发布 npm 作为公开契约。
- **连带**：本仓库 `bridge/` 只含 protocol/core/embedded；平台渲染（富文本/颜色码/长度收敛）全部发生在独立仓库，本仓库不涉及。
- **回退条件**：如独立仓库维护成本过高（协议同步频繁），可改回子模块/workspace 方式，但协议包仍须独立发布。

## ADR-019 platforms/je 多模块化（2026-08-11）

- **背景**：MC 服务端多样（Paper / Fabric / Velocity 等），每个都需要一个适配插件；薄壳的核心逻辑（IPC 客户端 / Node 子进程管理 / JSON-lines 解析）与具体服务端 API 无关。
- **结论**：`platforms/je` 改为 **Gradle 多模块**：`:core`（纯逻辑，零 Bukkit API，可独立测试）+ `:paper`（Paper 适配，依赖 `:core`）+ 预留服务端模块骨架（`fabric`/`velocity` 等）。**一个服务端 = 一个模块，共享 `:core`**，命名不带 kurobot 前缀（目录已处于 kurobot 项目内，冗余）。
- **理由**：未来新增服务端只是加模块，不动 `:core`；`:core` 平台无关可独立测试（对齐 TS 侧 `bridge/core` 平台无关的设计 ADR-007）；共享薄壳核心避免多服务端重复实现。
- **实施**：2026-08-11 完成 `:core` + `:paper` 拆分，shadowJar 产物 `kurobot-0.1.0.jar`。

## ADR-020 BE 服务端家族：LSE + Endstone C++ 薄壳，剔除 Nukkit（2026-08-11）

- **背景**：2026 年 BE 服务端生态实况——LeviLamina（LSE 脚本）是主流插件路线，Endstone（C++/Python）是另一支活跃生态；Nukkit 确认非主流（Wiki 插件加载器列表无它）。开发者会 C++。
- **结论**：
  - `platforms/be` 重组为 **BE 服务端家族**：`lse/`（LeviLamina，TS，维持 ADR-012）+ `endstone/`（Endstone，**C++ 薄壳**，预留骨架）。
  - **Endstone 走 C++ 薄壳**（非 Python）：C++ 只做事件桥接 + 内嵌 Node 子进程管理 + JSON-lines IPC，业务仍走 `bridge/core`——与 Java 薄壳完全同构，复用全部业务。
  - **剔除 Nukkit 模块**（非主流，避免误导）。
- **理由**：Endstone 生态存在且有价值（C++ 技术栈匹配开发者能力）；薄壳模式（ADR-005 同款）让 C++/Java 薄壳共享同一 `bridge/core` 业务核心，无需为各服务端重复实现业务。
- **连带**：`platforms/` 划分标准 = 技术栈 + 客户端（je=Java 服务端 / be=BE 服务端家族，家族内按具体平台分）。
- **回退条件**：如 Endstone 生态萎缩，可删除 `endstone/` 骨架（成本为零）。
