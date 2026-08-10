# bridge/protocol 设计（@kurobot/protocol）

> 本文件是包级设计文档（AGENTS.md：写代码前先更新对应包的 `docs/design.md`，设计先行）。

## 职责

`kurobot-ws` 协议消息类型的 **zod schema SSOT**（ADR-008）。全项目唯一的消息类型来源，任何文件禁止手写消息类型。

## 约束

- 零框架依赖（仅 zod），零 Node API —— QuickJS（LSE）可跑。
- schema 即产物（`z.infer`），无生成步骤。
- 消费方用 `schema.safeParse()` 做协议层运行时验证（对端数据不可信）。

## 组成（规划）

- `src/meta.ts`：协议名 / 版本 / WS 子协议常量。
- `src/frame.ts`：帧格式 `{ header: { type, id? }, body }`。
- `src/messages/`：各消息 schema（hello / hello_ack / chat / join / leave / death / status / bindings_updated / command / query / ping / pong / msgContinue / msgEnd…）。
- `src/index.ts`：聚合导出。

## 实现顺序

1. 根骨架就绪（本文件所在阶段）。
2. 按 `docs/protocol/draft-v0.1.md` 逐消息细化 schema（STATUS.md 第 1 步）。
3. 每个 schema 配 vitest 单测（`safeParse` 合法/非法载荷）。
