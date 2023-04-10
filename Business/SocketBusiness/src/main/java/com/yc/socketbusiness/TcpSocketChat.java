package com.yc.socketbusiness;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpSocketChat {

    //客户端
    public class TCPClient {
        // 首先创建一个Socket和InetSocketAddress ，然后通过Socket的connect()方法进行连接
        // 连接成功后可以获取到输出流，通过该输出流就可以向服务端传输数据。
        public void createClient(){
            //1.创建TCP客户端Socket服务
            Socket client = new Socket();
            //2.与服务端进行连接
            InetSocketAddress address = new InetSocketAddress("192.168.31.137",10000);
            try {
                client.connect(address);
                //3.连接成功后获取客户端Socket输出流
                OutputStream outputStream = client.getOutputStream();
                //4.通过输出流往服务端写入数据
                outputStream.write("hello server".getBytes());
                //5.关闭流
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class TCPServer {
        public void createServer() throws IOException {
            //1.创建服务端Socket并明确端口号
            ServerSocket serverSocket = new ServerSocket(10000);
            //2.获取到客户端的Socket
            Socket socket = serverSocket.accept();
            //3.通过客户端的Socket获取到输入流
            InputStream inputStream = socket.getInputStream();
            //4.通过输入流获取到客户端传递的数据
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = bufferedReader.readLine())!=null){
                System.out.println(line);
            }
            //5.关闭流
            socket.close();
            serverSocket.close();
        }
    }


    //客户端1
    public class TCPClient1 {
        public void createClient1() throws IOException {
            System.out.println("TCPClient1 Start...");
            //1.创建TCP客户端Socket服务
            Socket client = new Socket();
            //2.与服务端进行连接
            InetSocketAddress address = new InetSocketAddress("192.168.31.137",10004);
            client.connect(address);

            //3.连接成功后获取客户端Socket输出流
            OutputStream outputStream = client.getOutputStream();

            //4.通过输出流往服务端写入数据
            outputStream.write("Hello my name is Client1".getBytes());

            //5.告诉服务端发送完毕
            client.shutdownOutput();

            //6.读取服务端返回数据
            InputStream is = client.getInputStream();
            byte[] bytes = new byte[1024];
            int len = is.read(bytes);
            System.out.println(new String(bytes,0,len));

            //7.关闭流
            client.close();
        }
    }

    //客户端2
    public class TCPClient2 {
        public void createClient2() throws IOException {
            System.out.println("TCPClient2 Start...");

            //1.创建TCP客户端Socket服务
            Socket client = new Socket();

            //2.与服务端进行连接
            InetSocketAddress address = new InetSocketAddress("192.168.31.137",10004);
            client.connect(address);

            //3.连接成功后获取客户端Socket输出流
            OutputStream outputStream = client.getOutputStream();

            //4.通过输出流往服务端写入数据
            outputStream.write("Hello my name is Client2".getBytes());

            //5.告诉服务端发送完毕
            client.shutdownOutput();

            //6.读取服务端返回数据
            InputStream is = client.getInputStream();
            byte[] bytes = new byte[1024];
            int len = is.read(bytes);
            System.out.println(new String(bytes,0,len));

            //7.关闭流
            client.close();
        }
    }

    //服务端
    public class TCPServer2 {
        private void receive() throws IOException {
            System.out.println("Server Start...");
            //创建服务端Socket并明确端口号
            ServerSocket serverSocket = new ServerSocket(10004);
            while (true){
                //获取到客户端的Socket
                Socket socket = serverSocket.accept();
                //通过客户端的Socket获取到输入流
                InputStream is = socket.getInputStream();

                //通过输入流获取到客户端传递的数据
                byte[] bytes = new byte[1024];
                int len = is.read(bytes);
                System.out.println(new String(bytes,0,len));

                //将客户端发来的数据原封不动返回
                OutputStream os = socket.getOutputStream();
                os.write(new String(bytes,0,len).getBytes());
                //关闭连接
                socket.close();
            }
        }


        private void receive2() throws IOException {
            System.out.println("Server Start...");
            //创建服务端Socket并明确端口号
            ServerSocket serverSocket = new ServerSocket(10004);
            while (true){
                //获取到客户端的Socket
                final Socket socket = serverSocket.accept();
                //通过线程执行客户端请求
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //通过客户端的Socket获取到输入流
                            InputStream is = socket.getInputStream();

                            //通过输入流获取到客户端传递的数据
                            byte[] bytes = new byte[1024];
                            int len = is.read(bytes);
                            System.out.println(new String(bytes,0,len));

                            //将客户端发来的数据原封不动返回
                            OutputStream os = socket.getOutputStream();
                            os.write(new String(bytes,0,len).getBytes());
                            //关闭连接
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        }
    }


}
