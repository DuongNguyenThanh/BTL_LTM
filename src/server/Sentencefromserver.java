/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author DuongPC
 */
class Sentencefromserver extends Thread {

    public static String sendingSentence;

    public Sentencefromserver() {

    }

    public void run() {

        while (true) {

            try {

                if (sendingSentence.length() > 0) {
                    for (int i = 0; i < Server.i; i++) {
                        Server.outToClient[i].writeBytes("From Server: " + sendingSentence + '\n');

                    }
                    sendingSentence = null;
                }

            } catch (Exception e) {

            }
        }
    }
}
