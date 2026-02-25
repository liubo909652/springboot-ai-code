# Java 代码审查

请对以下代码进行全面的代码审查：

$ARGUMENTS

---

## 审查维度

按以下顺序逐项检查，每项给出明确结论（✅ 通过 / ⚠️ 建议 / ❌ 必须修复）。

---

### 1. 分层与职责

- Controller 是否有业务逻辑（应只做参数绑定和响应封装）
- Service 是否直接操作 HTTP 相关对象（HttpServletRequest 等）
- Mapper 是否被 Controller 直接调用（跳层）
- 类是否承担了过多职责（God Class）

---

### 2. Spring 使用规范

- 是否使用字段注入 `@Autowired`（应改为构造器注入）
- `@Transactional` 是否加在了 Controller 上（不应该）
- 写操作的 Service 方法是否缺少 `@Transactional`
- 是否在同一个 Bean 内部调用 `@Transactional` 方法（代理失效）
- 返回类型是否统一使用 `ResponseEntity<T>`

---

### 3. MyBatis 规范

- Mapper 接口是否加了 `@Mapper`
- 多参数方法是否使用了 `@Param`
- XML 中是否使用 `${}` 拼接用户输入（❌ SQL 注入风险）
- 是否定义了 `<resultMap>`，而非依赖自动映射
- `<insert>` 是否配置了 `useGeneratedKeys="true" keyProperty="id"`
- 动态 SQL 是否使用了标签（`<where>` `<set>` `<foreach>`），而非字符串拼接
- 是否存在 N+1 查询（循环内调用 Mapper）

---

### 4. 安全性

- `@RequestBody` 是否配了 `@Valid`
- 异常处理是否会向客户端泄露堆栈信息或内部结构
- 日志中是否打印了密码、Token、手机号等敏感字段
- 配置中是否硬编码了密钥或数据库密码

---

### 5. 异常处理

- 是否有空的 `catch` 块（吞掉异常）
- 是否直接 `catch (Exception e)` 而不做区分处理
- 自定义异常是否被 `GlobalExceptionHandler` 统一处理
- 查询结果是否做了 null 检查（Mapper 返回 null 时是否会 NPE）

---

### 6. 代码质量

- 是否有重复代码可以抽取
- 方法是否过长（建议不超过 30 行）
- 命名是否清晰（camelCase 字段、PascalCase 类、UPPER_SNAKE 常量）
- DTO 是否使用了 Java `record`（不可变数据优先）
- 是否有不必要的注释（代码即文档，注释说"为什么"而非"做什么"）

---

### 7. 测试覆盖（如提供了测试代码）

- 是否覆盖了正常路径、边界值、异常路径
- Mapper 测试是否使用了真实数据库（`@MybatisTest`），而非 Mock
- 测试方法命名是否遵循 `方法名_期望结果_触发条件` 格式
- 是否存在只测试 Mock、不测试真实逻辑的空洞测试

---

## 输出格式

```
## 审查结论

### ❌ 必须修复（{N} 项）
1. [问题描述] — [位置]
   修复建议：[具体怎么改]

### ⚠️ 建议优化（{N} 项）
1. [问题描述] — [位置]
   建议：[具体怎么改]

### ✅ 做得好的地方
- [具体指出哪里写得规范]

### 💡 顺带发现
- [不在主要审查范围内，但值得关注的问题]
```

---

## 注意事项

- 遵循 @./constitution.md 中的行为准则
- 只评审提供的代码，不要重写整个文件
- 如果代码上下文不足（缺少关联的 Service/Mapper），先指出"需要查看 XX 才能完整评审"
- 给出的修复建议要具体，附上修改后的代码片段
