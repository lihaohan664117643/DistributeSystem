# Maven 依赖冲突解决机制演示

## 当前项目依赖情况

### 1. 没有显式 protobuf 依赖时
```
distribute-java-cluster
└── distribute-java-core (1.9.0)
    └── brpc-java (2.5.9)
        └── protobuf-java (版本X)  ← 使用 brpc-java 中的版本
```

**结果：** 使用 `brpc-java` 中内置的 protobuf 版本

### 2. 添加显式 protobuf 依赖时
```
distribute-java-cluster
├── distribute-java-core (1.9.0)
│   └── brpc-java (2.5.9)
│       └── protobuf-java (版本X)  ← 传递依赖
└── protobuf-java (3.25.1)        ← 直接依赖（优先级更高）
```

**结果：** 使用 `3.25.1` 版本（直接依赖优先）

## Maven 依赖冲突解决规则

### 1. 最近优先原则（Nearest Definition Wins）
- **直接依赖** > **传递依赖**
- 路径短的传递依赖 > 路径长的传递依赖
- 相同路径长度时，按声明顺序选择

### 2. 依赖排除（Dependency Exclusion）
```xml
<dependency>
    <groupId>com.baidu</groupId>
    <artifactId>brpc-java</artifactId>
    <version>2.5.9</version>
    <exclusions>
        <exclusion>
            <groupId>com.google.protobuf</groupId>
            <artifactId>protobuf-java</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

### 3. 依赖管理（Dependency Management）
在父 pom 中统一管理版本：
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.google.protobuf</groupId>
            <artifactId>protobuf-java</artifactId>
            <version>3.25.1</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## 实际测试方法

### 1. 查看依赖树
```bash
mvn dependency:tree
```

### 2. 查看依赖冲突
```bash
mvn dependency:analyze
```

### 3. 强制使用特定版本
```xml
<dependency>
    <groupId>com.google.protobuf</groupId>
    <artifactId>protobuf-java</artifactId>
    <version>3.25.1</version>
    <scope>runtime</scope>
</dependency>
```

## 潜在问题

### 1. 版本不兼容
- `brpc-java` 可能依赖较旧的 protobuf 版本
- 新版本 protobuf 可能移除了一些 API
- 运行时可能出现 `NoSuchMethodError` 或 `ClassNotFoundException`

### 2. 类加载器问题
- 同一个类可能被加载了多个版本
- 可能导致序列化/反序列化问题

### 3. 性能影响
- 多个版本的库会增加内存占用
- 可能影响启动时间

## 最佳实践

### 1. 统一版本管理
```xml
<properties>
    <protobuf.version>3.25.1</protobuf.version>
</properties>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.google.protobuf</groupId>
            <artifactId>protobuf-java</artifactId>
            <version>${protobuf.version}</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 2. 排除冲突依赖
```xml
<dependency>
    <groupId>com.baidu</groupId>
    <artifactId>brpc-java</artifactId>
    <version>2.5.9</version>
    <exclusions>
        <exclusion>
            <groupId>com.google.protobuf</groupId>
            <artifactId>protobuf-java</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

### 3. 测试验证
- 运行完整的测试套件
- 检查运行时日志
- 验证序列化/反序列化功能 