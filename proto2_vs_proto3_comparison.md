# proto2 vs proto3 对比示例

## 1. 语法对比

### proto2 语法（你的文件）
```protobuf
syntax="proto2";

message SetRequest {
    optional string key = 1;           // 明确声明 optional
    optional string value = 2;         // 明确声明 optional
    optional string column_family = 3; // 明确声明 optional
}
```

### proto3 语法（如果转换）
```protobuf
syntax="proto3";

message SetRequest {
    string key = 1;           // 没有 optional，所有字段都隐式存在
    string value = 2;         // 没有 optional，所有字段都隐式存在
    string column_family = 3; // 没有 optional，所有字段都隐式存在
}
```

## 2. 生成的 Java 代码对比

### proto2 生成的代码特征
```java
public static final class SetRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    SetRequestOrBuilder {
    
    private int bitField0_;  // proto2 特有的位字段
    
    // proto2 特有的 hasXxx() 方法
    public boolean hasKey() {
        return ((bitField0_ & 0x00000001) != 0);
    }
    
    public boolean hasValue() {
        return ((bitField0_ & 0x00000002) != 0);
    }
    
    public boolean hasColumnFamily() {
        return ((bitField0_ & 0x00000004) != 0);
    }
    
    // 字段注释显示 optional
    /**
     * <code>optional string key = 1;</code>
     */
    public java.lang.String getKey() {
        // ...
    }
}
```

### proto3 生成的代码特征
```java
public static final class SetRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    SetRequestOrBuilder {
    
    // 没有 bitField0_，没有 hasXxx() 方法
    
    // 字段注释不显示 optional
    /**
     * <code>string key = 1;</code>
     */
    public java.lang.String getKey() {
        // ...
    }
}
```

## 3. 运行时行为对比

### proto2 行为
```java
SetRequest request = SetRequest.newBuilder()
    .setKey("test")
    .build();

// proto2 可以检查字段是否设置
if (request.hasKey()) {
    System.out.println("Key is set: " + request.getKey());
} else {
    System.out.println("Key is not set");
}

// 未设置的字段返回默认值
if (!request.hasValue()) {
    System.out.println("Value is not set, returns empty string");
}
```

### proto3 行为
```java
SetRequest request = SetRequest.newBuilder()
    .setKey("test")
    .build();

// proto3 没有 hasXxx() 方法
// 所有字段都隐式存在，未设置时返回默认值
System.out.println("Key: " + request.getKey());  // "test"
System.out.println("Value: " + request.getValue());  // "" (空字符串)
```

## 4. 序列化对比

### proto2 序列化
- 只序列化已设置的字段
- 使用位字段跟踪哪些字段已设置
- 更紧凑的序列化格式

### proto3 序列化
- 序列化所有字段（包括默认值）
- 不跟踪字段是否设置
- 更简单的序列化格式

## 5. 你的具体情况

### 当前状态
```protobuf
syntax="proto2";  // 使用 proto2 语法
```

### 生成的代码
- 使用现代 protoc 3.25.5 编译器
- 生成使用 protobuf 3.x 运行时 API 的代码
- 保持 proto2 的语义（hasXxx() 方法、optional 字段等）

### 优势
1. **向后兼容**：保持现有的 proto2 语义
2. **现代性能**：使用最新的 protobuf 运行时
3. **渐进迁移**：可以逐步迁移到 proto3

## 6. 总结

**"基于 proto2 语法生成，但使用现代 protobuf 3.x 运行时"** 意味着：

1. **语法层面**：使用 proto2 的语法规则（optional 字段、hasXxx() 方法等）
2. **编译器层面**：使用现代 protoc 3.x 编译器
3. **运行时层面**：使用现代 protobuf 3.x 运行时库
4. **兼容性**：完全兼容，享受现代性能的同时保持 proto2 语义

这是一个很好的组合，既保持了 proto2 的灵活性，又享受了现代 protobuf 的性能优势。 