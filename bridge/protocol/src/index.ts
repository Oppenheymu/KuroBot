/**
 * @kurobot/protocol —— kurobot-ws 协议 zod schema SSOT
 *
 * 这里是消息类型的唯一来源（硬约束，见 AGENTS.md）：
 * 任何文件禁止手写消息类型，必须 `import { ... } from "@kurobot/protocol"`。
 *
 * schema 定义详见各子模块；本文件只做聚合导出。
 * 具体消息 schema 在 STATUS.md 第 1 步细化。
 */

export const PROTOCOL_NAME = "kurobot-ws" as const;

/** 语义化版本（ADR-003：hello.protocolVersion 用它做小版本/能力协商） */
export const PROTOCOL_VERSION = "0.1.0" as const;

/** WS 子协议（ADR-003：大版本，握手期拒绝不兼容对端） */
export const WS_SUBPROTOCOL = "kurobot-ws.v1" as const;
