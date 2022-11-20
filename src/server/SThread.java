/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;

/**
 *
 * @author DuongPC
 */
class SThread extends Thread {

    //Sentence Thread
    public static String clientSentence;
    int srcid;
    BufferedReader inFromClient = Server.inFromClient[srcid];
    DataOutputStream outToClient[] = Server.outToClient;

    public SThread(int a) {
        srcid = a;
        // start();
        // fowl fl = new fowl(inFromClient, srcid);
        // fl.start();
    }

    public void run() {
        while (true) {
            try {

                clientSentence = inFromClient.readLine();
                // clientSentence = inFromClient.readLine();
                if(clientSentence.equals("144p") || clientSentence.equals("240p")|| clientSentence.equals("360p")
                        || clientSentence.equals("480p")|| clientSentence.equals("720p")
                        || clientSentence.equals("1080p")){
                    continue;
                }
                System.out.println("From Client " + srcid + ": " + clientSentence);
                FrmMain.jTextArea_displayMessage.append("From Client " + srcid + ": " + clientSentence + "\n");

                for (int i = 0; i < Server.i; i++) {
                    
                    if (i != srcid) {
                        outToClient[i].writeBytes("Client " + srcid + ": " + clientSentence + '\n');	//'\n' is necessary
                    }
                }

                FrmMain.jPanel_textMessage.revalidate();
                FrmMain.jPanel_textMessage.repaint();

            } catch (Exception e) {
            }

        }
    }
}
