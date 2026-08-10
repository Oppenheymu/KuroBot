import { describe, expect, it } from "vitest";

import { PROTOCOL_NAME, PROTOCOL_VERSION, WS_SUBPROTOCOL } from "./index.js";

describe("protocol 元信息", () => {
    it("协议名固定为 kurobot-ws", () => {
        expect(PROTOCOL_NAME).toBe("kurobot-ws");
    });

    it("版本号与 WS 子协议格式正确（ADR-003：两个独立概念）", () => {
        expect(PROTOCOL_VERSION).toMatch(/^\d+\.\d+\.\d+$/);
        expect(WS_SUBPROTOCOL).toMatch(/^kurobot-ws\.v\d+$/);
    });
});
