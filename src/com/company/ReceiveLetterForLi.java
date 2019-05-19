package com.company;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReceiveLetterForLi implements Runnable{
    public void run() {
        DatagramPacket pack = null;//创建数据包
        DatagramSocket postman = null;//负责发送数据包
        byte data[]= new byte[8192];
        try{
            pack = new DatagramPacket(data,data.length);
            postman = new DatagramSocket(2010);
        }catch (Exception e){
        }
        while (true){
            if(postman==null){
                break;
            }else{
                try{
                    postman.receive(pack);
                    String message = new String(pack.getData(),0,pack.getLength());
                    System.out.printf("收到:"+message);
                }catch (Exception e){}
            }

        }
    }
}
