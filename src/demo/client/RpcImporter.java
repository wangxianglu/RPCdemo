package demo.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by lout on 2017/2/28.
 */
public class RpcImporter<S> {

    public S importer(final Class<?> serviceClass, final InetSocketAddress address)
    {
        return (S) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class<?>[]{serviceClass.getInterfaces()[0]},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Socket socket = null;
                        ObjectOutput output = null;
                        ObjectInputStream inputStream = null;
                        try{
                            socket = new Socket();
                            socket.connect(address);
                            output = new ObjectOutputStream(socket.getOutputStream());
                            output.writeUTF(serviceClass.getName());
                            output.writeUTF(method.getName());
                            output.writeObject(method.getParameterTypes());
                            output.writeObject(args);
                            inputStream = new ObjectInputStream(socket.getInputStream());
                            return inputStream.readObject();
                        }
                        finally {
                            if(socket != null){
                                socket.close();
                            }
                            if (output != null){
                                output.close();
                            }
                            if(inputStream != null){
                                inputStream.close();
                            }

                        }
                    }
                });
    }
}
