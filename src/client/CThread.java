/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;


class CThread implements Runnable{
    BufferedReader inFromServer;
    Button sender = new Button("Send Text");
    public static DataOutputStream outToServer;
    public static String sentence;
    int RW_Flag;

    public CThread(BufferedReader in, DataOutputStream out, int rwFlag) {
        inFromServer = in;
        outToServer = out;
        RW_Flag = rwFlag;
        if(rwFlag == 0)
        {
            try{
                sentence = ClientForm.tb.getText();
                ClientForm.ta.append("From myself: "+sentence+"\n");
                outToServer.writeBytes(sentence + '\n');
                ClientForm.tb.setText(null);
            }
            catch(Exception E)
            {

            }
//            ClientForm.half.add(sender);
//            sender.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                sentence = ClientForm.tb.getText();
//                ClientForm.ta.append("From myself: "+sentence+"\n");
//                try{
//                outToServer.writeBytes(sentence + '\n');
//                }
//                catch(Exception E)
//                {
//                
//                }
//                ClientForm.tb.setText(null);
//            }
//        });
        }
    }

    @Override
    public void run() {
        String mysent;
        try {
            while (true) {
                if (RW_Flag == 0) {
                    if(sentence.length()>0)
                    {
                        System.out.println(sentence+"\n");
                        ClientForm.ta.append(sentence+"\n");
                        ClientForm.ta.setCaretPosition(ClientForm.ta.getDocument().getLength());
                        ClientForm.half.revalidate();
                        ClientForm.half.repaint();
                        ClientForm.jp.revalidate();
                        ClientForm.jp.repaint();
                        outToServer.writeBytes(sentence + '\n');
                        sentence = null;
                        ClientForm.tb.setText(null);
                    }
                } else if (RW_Flag == 1) {
                    mysent = inFromServer.readLine();
                    
                    ClientForm.ta.append(mysent+"\n");
                    ClientForm.ta.setCaretPosition(ClientForm.ta.getDocument().getLength());
                    ClientForm.half.revalidate();
                    ClientForm.half.repaint();
                    ClientForm.jp.revalidate();
                    ClientForm.jp.repaint();
                    
                    
                    
                    System.out.println("From : " + sentence);
                    sentence = null;
                    
                }
            }
        } catch (Exception e) {
        }
    }
}
