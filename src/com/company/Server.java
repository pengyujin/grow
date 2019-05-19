package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        String [] answer = {"南非","进入世界杯了","哈哈...问题真逗!"};
        ServerSocket serverForClient = null;
        Socket socketOnServer = null;
        DataInputStream in = null;
        DataOutputStream out =null;

        try{
            serverForClient =new ServerSocket(2010);
        }catch (IOException e){
            System.out.println(e);
        }
        try {
            System.out.println("等待客户呼叫");
            socketOnServer = serverForClient.accept();
            out = new DataOutputStream(socketOnServer.getOutputStream());
            in = new DataInputStream(socketOnServer.getInputStream());
            for(int i=0;i<answer.length;i++){
                String s =in.readUTF();
                System.out.println("服务气受客服的提问:"+s);
                out.writeUTF(answer[i]);
                Thread.sleep(500);
            }
        }catch (Exception e){
            System.out.println("客户已断开"+e);
        }

    }

}
