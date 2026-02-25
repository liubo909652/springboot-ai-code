# 生成 Git Commit 信息并提交

分析当前暂存区的变更，生成符合规范的 commit 信息并执行提交。

---

## 执行步骤

### Step 1：检查暂存区

```bash
git diff --cached --stat
git diff --cached
```

如果暂存区为空，停止执行并提示：

```
⚠️ 暂存区没有文件。请先执行 git add <文件> 后再运行此命令。
```

---

### Step 2：分析变更内容

根据 diff 内容，识别：

- **变更类型**：新增功能 / 修复 Bug / 重构 / 测试 / 配置 / 文档
- **影响范围**：涉及哪个模块或层（controller / service / mapper / config 等）
- **变更摘要**：这次改动做了什么，用一句话描述

---

### Step 3：生成 Commit 信息

严格遵循 **Conventional Commits** 格式：

```
<type>(<scope>): <subject>

[body - 可选，说明为什么这样改，而不是改了什么]

[footer - 可选，关联 Issue]
```

**type 选择规则：**

| type | 适用场景 |
|---|---|
| `feat` | 新功能 |
| `fix` | Bug 修复 |
| `refactor` | 重构（不新增功能，不修复 Bug） |
| `test` | 新增或修改测试 |
| `docs` | 文档变更（含 CLAUDE.md、README） |
| `chore` | 构建配置、依赖升级、非业务杂项 |
| `perf` | 性能优化 |
| `style` | 代码格式（不影响逻辑） |
| `ci` | CI/CD 配置变更 |

**scope 选择规则（本项目）：**

优先使用以下 scope，保持全项目统一：

`user` `order` `inventory` `auth` `mapper` `service` `controller` `config` `test` `deps` `migration`

**subject 规则：**

- 使用祈使句（动词开头）：`add`、`fix`、`remove`、`update`
- 不超过 72 个字符
- 不加句号
- 中英文均可，与项目已有提交保持一致

**示例：**

```
feat(user): add pagination support to user list API

使用 MyBatis 的 RowBounds 实现物理分页，避免全表加载。

Closes #42
```

```
fix(mapper): replace ${} with #{} in UserMapper to prevent SQL injection
```

```
refactor(service): extract order validation logic to OrderValidator
```

---

### Step 4：执行前确认

输出生成的 commit 信息，等待用户确认：

```
📝 即将提交以下信息：

---
<生成的 commit message>
---

确认提交？(y/n)
```

- 用户输入 `y` → 执行 `git commit -m "..."`
- 用户输入 `n` → 输出修改建议，或让用户提供补充信息后重新生成
- 用户输入其他内容 → 视为对 commit 信息的修改意见，重新生成后再次确认

---

### Step 5：提交并反馈

```bash
git commit -m "<subject>" -m "<body（如有）>"
```

提交成功后输出：

```
✅ 提交成功

commit <hash>
<commit message>

变更文件：
  <git diff --cached --stat 的结果>

下一步建议：
- 推送到远程：git push origin <当前分支名>
- 查看日志：git log --oneline -5
```

---

## 注意事项

- 遵循 @constitution.md 的行为准则：不替用户做决定，提交前必须确认
- 一次 commit 只做一件事；如果暂存区混入了不相关的改动，提示用户拆分后再提交
- 不自动执行 `git push`，推送由用户决定
- 如果变更内容涉及密钥、密码、敏感配置，立即警告并终止：

```
❌ 检测到可能包含敏感信息的变更：
   [文件名] 中包含疑似密钥/密码的内容。
   请确认后再提交，或将该文件加入 .gitignore。
```
