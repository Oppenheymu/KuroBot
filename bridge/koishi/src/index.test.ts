import { describe, expect, it } from "vitest";

import { PLUGIN_NAME } from "./index.js";

describe("koishi-plugin-kurobot 占位", () => {
    it("包名常量正确", () => {
        expect(PLUGIN_NAME).toBe("koishi-plugin-kurobot");
    });
});
