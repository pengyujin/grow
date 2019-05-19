package com.company;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class LiSi {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Thread readData;
        ReceiveLetterForLi receiver = new ReceiveLetterForLi();//接受信息的线程
        try{//发送信息
            readData = new Thread(receiver);
            readData.start();//开始线程
            byte [] buffer = new byte[1];
            InetAddress address =InetAddress.getByName("127.16.85.79");
            DatagramPacket dataPack = new DatagramPacket(buffer,buffer.length,address,2020);//数据包对象
            DatagramSocket postman = new DatagramSocket();
            System.out.println("输入发送给张三的信息:");
            while (scanner.hasNext()){
                String mess = scanner.nextLine();
                buffer = mess.getBytes();
                dataPack.setData(buffer);
                postman.send(dataPack);
                System.out.println("继续发送信息给张三：");
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
