# CLAUDE.md

你是一名资深java开发工程师，你的任务是辅助完成项目的研发工作。请根据以下规范进行代码编写、审查和优化：
---

## AI 行为准则

@./constitution.md

---

## 项目概览

| 项目 | 详情 |
|---|---|
| 框架 | Spring Boot 3.5.9 |
| 持久层 | MyBatis 3.x + MyBatis Spring Boot Starter 3.0.3 |
| Java 版本 | Java 21 (LTS) |
| 构建工具 | Maven |
| 数据库 | MySQL 8.x |
| 连接池 | HikariCP（Spring Boot 默认） |
| 数据库迁移 | Flyway |
| 测试框架 | JUnit 5 + Mockito + MyBatis Test |

---

## 目录结构

```
src/
├── main/
│   ├── java/com/example/app/
│   │   ├── Application.java              # 启动入口 @SpringBootApplication
│   │   ├── config/                       # 配置类
│   │   │   ├── MyBatisConfig.java
│   │   │   └── DataSourceConfig.java
│   │   ├── controller/                   # REST 控制器 @RestController
│   │   ├── service/                      # 业务逻辑 @Service
│   │   ├── mapper/                       # MyBatis Mapper 接口 @Mapper
│   │   ├── model/                        # 实体类 / 领域对象
│   │   ├── dto/                          # 请求/响应 DTO（用 Java record）
│   │   ├── exception/                    # 自定义异常 + 全局异常处理器
│   │   └── util/                         # 工具类
│   └── resources/
│       ├── application.properties               # 主配置
│       ├── application-dev.properties           # 开发环境
│       ├── application-prod.properties          # 生产环境（不含真实密钥）
│       ├── mapper/                       # MyBatis XML 映射文件
│       │   └── UserMapper.xml
│       └── db/migration/                 # Flyway SQL 迁移脚本
│           └── V1__init_schema.sql
└── test/
    ├── java/com/example/app/
    │   ├── controller/                   # MockMvc 控制器测试
    │   ├── service/                      # Service 单元测试
    │   └── mapper/                       # Mapper 集成测试
    └── resources/
        └── application-test.properties
```

---

## 核心依赖（pom.xml）

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.9</version>
</parent>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>3.0.3</version>
    </dependency>
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-core</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter-test</artifactId>
        <version>3.0.3</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## 配置规范（application.yml）

```properties
# 数据源
spring.datasource.url=jdbc:mysql://localhost:3306/mydb?useSSL=false&serverTimezone=UTC
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:}
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000

# MyBatis
mybatis.mapper-locations=classpath:mapper/**/*.xml
mybatis.type-aliases-package=com.example.app.model
mybatis.configuration.map-underscore-to-camel-case=true
mybatis.configuration.default-fetch-size=100
mybatis.configuration.default-statement-timeout=30
mybatis.configuration.log-impl=org.apache.ibatis.logging.slf4j.Slf4jImpl

# 日志（开发时打印 SQL）
logging.level.com.example.app.mapper=DEBUG
```

---

## MyBatis 编写规范

### Mapper 接口

```java
@Mapper
public interface UserMapper {

    User findById(@Param("id") Long id);

    List<User> findAll();

    List<User> findByCondition(@Param("query") UserQuery query);

    int insert(User user);

    int update(User user);

    int deleteById(@Param("id") Long id);
}
```

- 每个 Mapper 接口必须加 `@Mapper`
- 多参数方法必须使用 `@Param` 命名
- 查询方法前缀：`find*` / `count*` / `exists*`
- 写操作方法前缀：`insert` / `update` / `delete`

### XML Mapper 模板

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.example.app.mapper.UserMapper">

    <resultMap id="UserResultMap" type="com.example.model.User">
        <id     column="id"         property="id"/>
        <result column="username"   property="username"/>
        <result column="email"      property="email"/>
        <result column="created_at" property="createdAt"/>
    </resultMap>

    <sql id="Base_Column_List">
        id, username, email, created_at
    </sql>

    <select id="findById" resultMap="UserResultMap">
        SELECT <include refid="Base_Column_List"/>
        FROM users
        WHERE id = #{id}
    </select>

    <select id="findByCondition" resultMap="UserResultMap">
        SELECT <include refid="Base_Column_List"/>
        FROM users
        <where>
            <if test="query.username != null and query.username != ''">
                AND username LIKE CONCAT('%', #{query.username}, '%')
            </if>
            <if test="query.email != null">
                AND email = #{query.email}
            </if>
        </where>
        ORDER BY created_at DESC
    </select>

    <insert id="insert" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO users (username, email, created_at)
        VALUES (#{username}, #{email}, NOW())
    </insert>

    <update id="update">
        UPDATE users
        <set>
            <if test="username != null">username = #{username},</if>
            <if test="email != null">email = #{email},</if>
        </set>
        WHERE id = #{id}
    </update>

    <delete id="deleteById">
        DELETE FROM users WHERE id = #{id}
    </delete>

</mapper>
```

**XML Mapper 硬性规则：**

| 规则 | 说明 |
|---|---|
| 必须定义 `<resultMap>` | 不依赖自动映射，字段变更时有明确报错 |
| 用 `<sql>` 抽取列名 | 避免 `SELECT *`，便于复用 |
| 动态 SQL 用标签 | `<where>` `<set>` `<foreach>` `<choose>`，不拼字符串 |
| 参数用 `#{}` | `${}` 仅用于可信的代码控制值（如排序字段名） |
| INSERT 必须加自增配置 | `useGeneratedKeys="true" keyProperty="id"` |

---

## 分层规范

### Service 层

```java
@Service
@Transactional(readOnly = true)     // 类级别默认只读
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public User getById(Long id) {
        return Optional.ofNullable(userMapper.findById(id))
            .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }

    @Transactional                  // 写操作单独覆盖
    public User create(CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        userMapper.insert(user);
        return user;
    }
}
```

- 类级别 `@Transactional(readOnly = true)`，写方法单独加 `@Transactional`
- 使用构造器注入（`@RequiredArgsConstructor` + `final` 字段），禁止字段注入
- Service 只依赖 Mapper 接口，不依赖其他 Service 的内部实现

### Controller 层

```java
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(UserResponse.from(userService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @RequestBody @Valid CreateUserRequest request) {
        User user = userService.create(request);
        URI location = URI.create("/api/v1/users/" + user.getId());
        return ResponseEntity.created(location).body(UserResponse.from(user));
    }
}
```

- 返回类型统一用 `ResponseEntity<T>`
- `@RequestBody` 必须配 `@Valid`
- Controller 不写业务逻辑，只做参数绑定和响应封装
- API 路径统一前缀 `/api/v1/`

### 异常处理

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest()
            .body(new ErrorResponse("VALIDATION_ERROR", message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.internalServerError()
            .body(new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred"));
    }
}
```

---

## 测试规范

### Mapper 集成测试

```java
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void findById_returnsUser_whenExists() {
        User user = userMapper.findById(1L);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("testuser");
    }
}
```

### Service 单元测试

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserMapper userMapper;
    @InjectMocks UserService userService;

    @Test
    void getById_throwsNotFound_whenUserMissing() {
        when(userMapper.findById(99L)).thenReturn(null);
        assertThatThrownBy(() -> userService.getById(99L))
            .isInstanceOf(EntityNotFoundException.class);
    }
}
```

### Controller 测试

```java
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean UserService userService;

    @Test
    void getUser_returns200_whenFound() throws Exception {
        when(userService.getById(1L)).thenReturn(new User(1L, "alice", "alice@test.com"));

        mockMvc.perform(get("/api/v1/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("alice"));
    }
}
```

**测试方法命名：** `方法名_期望结果_触发条件`，例如 `findById_returnsNull_whenIdNotExists`

---

## Git 规范

### 分支策略

```
main        ← 仅存放生产就绪代码，禁止直接提交
develop     ← 集成分支，所有 feature 合并到这里
feature/*   ← 新功能
bugfix/*    ← 缺陷修复
hotfix/*    ← 紧急修复，从 main 拉出
release/*   ← 发版准备
```

### 提交信息格式（Conventional Commits）

```
feat(user): add pagination support to UserMapper
fix(auth): resolve NPE when token is missing
refactor(service): extract validation to UserValidator
test(mapper): add edge-case tests for findByCondition
chore(deps): upgrade mybatis-spring-boot-starter to 3.0.3
```

格式：`<type>(<scope>): <summary>`，summary 不超过 72 字符，使用祈使句。

### .gitignore 关键条目

```gitignore
target/
*.class
.idea/
*.iml
.env
application-local.yml
*.log
```

---

## 常用命令

```bash
# 构建（跳过测试）
mvn clean package -DskipTests

# 本地运行（dev profile）
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 运行所有测试
mvn test

# 运行指定测试类
mvn test -Dtest=UserServiceTest

# Flyway 迁移
mvn flyway:migrate

# 查看依赖树
mvn dependency:tree
```

---

## 编码风格

- **Java 特性**：积极使用 Java 21 特性：`record`（DTO）、文本块（测试 SQL）、pattern matching
- **命名**：字段/方法 `camelCase`，类 `PascalCase`，常量 `UPPER_SNAKE_CASE`
- **Null 处理**：公开 API 返回 `Optional`，禁止直接返回 `null`
- **日志**：用 `@Slf4j`（Lombok）；SQL 用 `DEBUG`，业务事件用 `INFO`，异常用 `ERROR`
- **包结构**：按层（controller/service/mapper）或按功能模块，选其一全项目统一
