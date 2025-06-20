package com.github.raftimpl.raft.example.server.service;

import com.google.protobuf.ByteString;

/**
 * 测试 ExampleProto.java 与 proto2 的兼容性
 */
public class Proto2CompatibilityTest {
    
    public static void main(String[] args) {
        testProto2Compatibility();
    }
    
    public static void testProto2Compatibility() {
        System.out.println("=== Proto2 兼容性测试 ===");
        
        // 测试 SetRequest
        testSetRequest();
        
        // 测试 GetRequest
        testGetRequest();
        
        // 测试序列化/反序列化
        testSerialization();
        
        System.out.println("=== 兼容性测试完成 ===");
    }
    
    private static void testSetRequest() {
        System.out.println("\n--- SetRequest 测试 ---");
        
        // 创建空的 SetRequest
        ExampleProto.SetRequest emptyRequest = ExampleProto.SetRequest.newBuilder().build();
        
        // 测试 proto2 的 hasXxx() 方法
        System.out.println("Empty request - hasKey(): " + emptyRequest.hasKey());
        System.out.println("Empty request - hasValue(): " + emptyRequest.hasValue());
        System.out.println("Empty request - hasColumnFamily(): " + emptyRequest.hasColumnFamily());
        
        // 测试默认值
        System.out.println("Empty request - getKey(): '" + emptyRequest.getKey() + "'");
        System.out.println("Empty request - getValue(): '" + emptyRequest.getValue() + "'");
        System.out.println("Empty request - getColumnFamily(): '" + emptyRequest.getColumnFamily() + "'");
        
        // 创建部分设置的 SetRequest
        ExampleProto.SetRequest partialRequest = ExampleProto.SetRequest.newBuilder()
            .setKey("test-key")
            .setColumnFamily("default")
            .build();
        
        System.out.println("\nPartial request - hasKey(): " + partialRequest.hasKey());
        System.out.println("Partial request - hasValue(): " + partialRequest.hasValue());
        System.out.println("Partial request - hasColumnFamily(): " + partialRequest.hasColumnFamily());
        
        System.out.println("Partial request - getKey(): '" + partialRequest.getKey() + "'");
        System.out.println("Partial request - getValue(): '" + partialRequest.getValue() + "'");
        System.out.println("Partial request - getColumnFamily(): '" + partialRequest.getColumnFamily() + "'");
        
        // 测试 Builder 的 clear 方法
        ExampleProto.SetRequest.Builder builder = ExampleProto.SetRequest.newBuilder()
            .setKey("test-key")
            .setValue("test-value")
            .setColumnFamily("default");
        
        System.out.println("\nBefore clear - hasKey(): " + builder.hasKey());
        builder.clearKey();
        System.out.println("After clearKey() - hasKey(): " + builder.hasKey());
        
        // 测试 getXxxBytes() 方法
        ExampleProto.SetRequest bytesRequest = ExampleProto.SetRequest.newBuilder()
            .setKey("test-key")
            .build();
        
        ByteString keyBytes = bytesRequest.getKeyBytes();
        System.out.println("\nKey bytes: " + keyBytes.toStringUtf8());
    }
    
    private static void testGetRequest() {
        System.out.println("\n--- GetRequest 测试 ---");
        
        // 创建 GetRequest
        ExampleProto.GetRequest request = ExampleProto.GetRequest.newBuilder()
            .setKey("test-key")
            .setColumnFamily("default")
            .build();
        
        // 测试 proto2 的 hasXxx() 方法
        System.out.println("GetRequest - hasKey(): " + request.hasKey());
        System.out.println("GetRequest - hasColumnFamily(): " + request.hasColumnFamily());
        
        System.out.println("GetRequest - getKey(): '" + request.getKey() + "'");
        System.out.println("GetRequest - getColumnFamily(): '" + request.getColumnFamily() + "'");
    }
    
    private static void testSerialization() {
        System.out.println("\n--- 序列化/反序列化测试 ---");
        
        // 创建原始请求
        ExampleProto.SetRequest original = ExampleProto.SetRequest.newBuilder()
            .setKey("test-key")
            .setColumnFamily("default")
            .build();
        
        System.out.println("Original - hasKey(): " + original.hasKey());
        System.out.println("Original - hasColumnFamily(): " + original.hasColumnFamily());
        
        try {
            // 序列化
            byte[] serialized = original.toByteArray();
            System.out.println("Serialized size: " + serialized.length + " bytes");
            
            // 反序列化
            ExampleProto.SetRequest deserialized = ExampleProto.SetRequest.parseFrom(serialized);
            
            System.out.println("Deserialized - hasKey(): " + deserialized.hasKey());
            System.out.println("Deserialized - hasColumnFamily(): " + deserialized.hasColumnFamily());
            System.out.println("Deserialized - getKey(): '" + deserialized.getKey() + "'");
            System.out.println("Deserialized - getColumnFamily(): '" + deserialized.getColumnFamily() + "'");
            
            // 验证相等性
            System.out.println("Original equals deserialized: " + original.equals(deserialized));
            
        } catch (Exception e) {
            System.err.println("序列化/反序列化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 