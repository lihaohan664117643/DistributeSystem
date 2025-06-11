package com.github.raftimpl.raft.example.client;

import java.io.Console;

import com.baidu.brpc.client.BrpcProxy;
import com.baidu.brpc.client.RpcClient;
import com.github.raftimpl.raft.example.server.service.ExampleProto;
import com.github.raftimpl.raft.example.server.service.ExampleService;
import com.googlecode.protobuf.format.JsonFormat;

public class ClientMain_Test {
        public static void main(String[] args) {

                // String ipPorts =
                // "list://192.168.91.134:8051,192.168.91.134:8052,192.168.91.134:8053";
                String ipport = "list://127.0.0.1:8051";
                String key = "username123";
                String value = null;

                // init rpc client
                RpcClient rpcClient = new RpcClient(ipport);

                ExampleService exampleService = BrpcProxy.getProxy(rpcClient, ExampleService.class);
                final JsonFormat jsonFormat = new JsonFormat();

                // set
                System.out.print("I am here");
                if (value != null) {
                        ExampleProto.SetRequest setRequest = ExampleProto.SetRequest.newBuilder()
                                        .setKey(key).setValue(value).build();
                        ExampleProto.SetResponse setResponse = exampleService.set(setRequest);
                        System.out.printf("set request, key=%s value=%s response=%s\n",
                                        key, value, jsonFormat.printToString(setResponse));
                } else {
                        // get
                        System.out.println("*************");
                        ExampleProto.GetRequest getRequest = ExampleProto.GetRequest.newBuilder()
                                        .setKey(key).build();
                        System.out.println("*************");
                        ExampleProto.GetResponse getResponse = exampleService.get(getRequest);
                        System.out.printf("get request, key=%s, response=%s\n",
                                        key, jsonFormat.printToString(getResponse));
                }

                rpcClient.stop();
        }
}
