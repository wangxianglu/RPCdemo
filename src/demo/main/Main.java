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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RpcExporter.exporter("localhost", 8080);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        RpcImporter<EchoService> importer = new RpcImporter<EchoService>();
        EchoService echoService = importer.importer(EchoServiceImpl.class, new InetSocketAddress("localhost", 8080));
        System.out.print(echoService.echo("Are ou ok?"));
    }
}
