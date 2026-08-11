/**
 * tools/embed —— 嵌入式打包（占位）
 *
 * 职责（规划，STATUS.md 第 4 步）：
 *   1. 拉 npm tarball（napukettoqq 协议端）+ Node 运行时（node.exe，MIT）
 *   2. 将 bridge/embedded 产物 + node.exe 打包进 platforms/je/src/main/resources/embedded/
 *   3. wrapper.node（腾讯闭源）不进入 JAR，运行期从 QQ 安装目录发现拷贝
 *
 * 尚未实现，调用会失败以提示。
 */
console.error("[tools/embed] 未实现：请先完成嵌入式打包工具（见 STATUS.md 第 4 步）");
process.exit(1);
