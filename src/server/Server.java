/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.sun.jna.NativeLibrary;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


/**
 *
 * @author DuongPC
 */
class Server {

    public static InetAddress[] inet;
    public static int[] port;
    public static int i;
    static int count = 0;
    public static BufferedReader[] inFromClient;
    public static DataOutputStream[] outToClient;

    public Server() throws Exception {

        NativeLibrary.addSearchPath("libvlc", "D:\\VideoLAN\\VLC");

        Server.inet = new InetAddress[30];
        port = new int[30];

        // TODO code application logic here
        ServerSocket welcomeSocket = new ServerSocket(6782);
        System.out.println(welcomeSocket.isClosed());
        Socket connectionSocket[] = new Socket[30];
        inFromClient = new BufferedReader[30];
        outToClient = new DataOutputStream[30];

        DatagramSocket serv = new DatagramSocket(4321);

        byte[] buf = new byte[62000];
        // Socket[] sc = new Socket[5];
        DatagramPacket dp = new DatagramPacket(buf, buf.length);

//        Canvas_Demo canv = new Canvas_Demo();
//        frmMain fmain = new frmMain();
//        fmain.setVisible(true);
        MainView view = new MainView();

        System.out.println("Gotcha");

        // OutputStream[] os = new OutputStream[5];
        i = 0;

        SThread[] st = new SThread[30];

        while (true) {

            System.out.println(serv.getPort());
            serv.receive(dp);
            System.out.println(new String(dp.getData()));
            buf = "starts".getBytes();

            inet[i] = dp.getAddress();
            port[i] = dp.getPort();

            DatagramPacket dsend = new DatagramPacket(buf, buf.length, inet[i], port[i]);
            serv.send(dsend);

            VideoThread sendvid = new VideoThread(serv);

            System.out.println("waiting\n ");
            connectionSocket[i] = welcomeSocket.accept();
            System.out.println("connected " + i);

            inFromClient[i] = new BufferedReader(new InputStreamReader(connectionSocket[i].getInputStream()));
            outToClient[i] = new DataOutputStream(connectionSocket[i].getOutputStream());
            outToClient[i].writeBytes("Connected: from Server\n");

            st[i] = new SThread(i);
            st[i].start();

            if (count == 0) {
                Sentencefromserver sen = new Sentencefromserver();
                sen.start();
                count++;
            }

            System.out.println(inet[i]);
            sendvid.start();

            i++;

            if (i == 30) {
                break;
            }
        }
    }
    
    // Run
    public static void main(String[] args) throws Exception {
        Server server = new Server();
    }
}
