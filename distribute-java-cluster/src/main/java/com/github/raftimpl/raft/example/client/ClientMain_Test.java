package com.github.raftimpl.raft.example.client;

import java.io.Console;

import com.baidu.brpc.client.BrpcProxy;
import com.baidu.brpc.client.RpcClient;
import com.github.raftimpl.raft.example.server.service.ExampleProto;
import com.github.raftimpl.raft.example.server.service.ExampleService;
import com.googlecode.protobuf.format.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMain_Test {
        private static final Logger LOG = LoggerFactory.getLogger(ClientMain_Test.class);
        
        public static void main(String[] args) {

                // String ipPorts =
                // "list://192.168.91.134:8051,192.168.91.134:8052,192.168.91.134:8053";
                String ipport = "list://127.0.0.1:8051";
                String key = "user";
                String value = "test1";
                String column_family = "default";

                LOG.info("Starting ClientMain_Test with ipport={}, key={}, value={}, column_family={}", 
                        ipport, key, value, column_family);

                // init rpc client
                RpcClient rpcClient = new RpcClient(ipport);
                LOG.info("RPC client initialized");

                ExampleService exampleService = BrpcProxy.getProxy(rpcClient, ExampleService.class);
                final JsonFormat jsonFormat = new JsonFormat();

                // set
                System.out.print("I am here");
                LOG.info("I am here - logging message");
                if (value != null) {
                        LOG.info("Performing SET operation");
                        ExampleProto.SetRequest setRequest = ExampleProto.SetRequest.newBuilder()
                                        .setKey(key).setValue(value).setColumnFamily(column_family).build();
                        LOG.info("SetRequest created: {}", jsonFormat.printToString(setRequest));
                        ExampleProto.SetResponse setResponse = exampleService.set(setRequest);
                        LOG.info("SetResponse received: {}", jsonFormat.printToString(setResponse));
                        System.out.printf("set request, key=%s value=%s response=%s\n",
                                        key, value, jsonFormat.printToString(setResponse));
                } else {
                        // get
                        LOG.info("Performing GET operation");
                        System.out.println("*************");
                        ExampleProto.GetRequest getRequest = ExampleProto.GetRequest.newBuilder()
                                        .setKey(key).setColumnFamily(column_family).build();
                        LOG.info("GetRequest created: {}", jsonFormat.printToString(getRequest));
                        System.out.println("*************");
                        ExampleProto.GetResponse getResponse = exampleService.get(getRequest);
                        LOG.info("GetResponse received: {}", jsonFormat.printToString(getResponse));
                        System.out.printf("get request, key=%s, response=%s\n",
                                        key, jsonFormat.printToString(getResponse));
                }

                LOG.info("Stopping RPC client");
                rpcClient.stop();
                LOG.info("ClientMain_Test completed");
        }
}
