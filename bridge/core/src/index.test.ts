import { describe, expect, it } from "vitest";

import { CORE_NAME } from "./index.js";

describe("bridge-core 占位", () => {
    it("包名常量正确", () => {
        expect(CORE_NAME).toBe("kurobot-bridge-core");
    });
});
