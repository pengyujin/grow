package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server01 {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        ServerThread serverThread;
        Socket you =null;
        while (true){
            try{
                serverSocket = new ServerSocket(2020);
            }catch (IOException e){
                System.out.println("正在监听");
            }

            try{
                System.out.println("等待客户呼叫:");
                you = serverSocket.accept();
                System.out.println("客户的地址："+you.getInetAddress());
            }catch (IOException e){
                System.out.println("正在等待客户");
            }

            if(you!= null){
                new ServerThread(you).start();
            }
        }
    }
    //内部线程类
    static class ServerThread extends Thread {
        Socket socket = null;
        DataInputStream in = null;
        DataOutputStream out = null;
        String s = null;

        ServerThread(Socket t) {
            socket = t;
            try {
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
            }

        }
        //重写run方法
        public void run() {
            while (true) {
                try {
                    double r = in.readDouble();
                    double area = Math.PI * r * r;
                    out.writeDouble(area);
                } catch (IOException e) {
                    System.out.println("客户离开");
                    return ;
                }
            }
        }

    }
}
