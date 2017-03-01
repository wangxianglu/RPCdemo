package demo.main;

import demo.client.RpcImporter;
import demo.rpc.server.RpcExporter;
import demo.service.EchoService;
import demo.service.impl.EchoServiceImpl;

import java.net.InetSocketAddress;

/**
 * Created by lout on 2017/2/28.
 */
public class Main {

    public static void main(String[] args) throws Exception{
        // 创建一个异步 发布服务端的线程并 启动
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RpcExporter.exporter("localhost", 8888);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        // 发起RPC 客户端请求
        RpcImporter<EchoService> importer = new RpcImporter<EchoService>();
        EchoService echoService = importer.importer(EchoServiceImpl.class, new InetSocketAddress("localhost", 8888));
        System.out.print(echoService.echo("Are ou ok?"));
    }
}
