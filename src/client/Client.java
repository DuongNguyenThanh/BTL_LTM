/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;


public class Client {
    public static DatagramSocket ds;


    public static void main(String[] args) throws Exception {
        ds = new DatagramSocket();
        
        byte[] init = new byte[62000];
        init = "givedata".getBytes();
        //26.10.210.69
        InetAddress addr = InetAddress.getByName("localhost");
        
        DatagramPacket dp = new DatagramPacket(init,init.length,addr,4321);
        
        ds.send(dp);
        
        DatagramPacket rcv = new DatagramPacket(init, init.length);
        
        ds.receive(rcv);
        System.out.println(new String(rcv.getData()));
        
        System.out.println(ds.getPort());
        ClientForm cf = new ClientForm();
        cf.setVisible(true);
        Thread t1 = new Thread(cf);
        t1.start();
        
        String modifiedSentence;

        InetAddress inetAddress = InetAddress.getByName("localhost");
        //.getByName(String hostname); "CL11"
        System.out.println(inetAddress);

        Socket clientSocket = new Socket(inetAddress, 6782);
        DataOutputStream outToServer =
                new DataOutputStream(clientSocket.getOutputStream());

        BufferedReader inFromServer =
                new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outToServer.writeBytes("Thanks man\n");

        CThread write = new CThread(inFromServer, outToServer, 0);
        CThread read = new CThread(inFromServer, outToServer, 1);
        Thread t2 = new Thread(write);
        Thread t3 = new Thread(read);
        t2.start();
        t3.start();

        t2.join();
        t3.join();
        clientSocket.close();
    }
}
