package server;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Graphics2D;
import java.io.IOException;

/**
 *
 * @author DuongPC
 */
class VideoThread extends Thread {
// Video Thread

    // Create a sub screen on server for checking screen on client
    JFrame jfSubScreen = new JFrame("screenshot before sending");
    JLabel jlbSubScreen = new JLabel();
    //

    DatagramSocket socket;

    Robot robot = new Robot();

    byte[] outbuff = new byte[62000];

    BufferedImage bufferedImage;
    ImageIcon imageIcon;
    Rectangle rectangle;

    public VideoThread(DatagramSocket socket) throws Exception {
        this.socket = socket;

        System.out.println(socket.getPort());
        jfSubScreen.setSize(500, 400);
        jfSubScreen.setVisible(true);
    }

    @Override
    public void run() {
        while (true) {
            try {

                // Rectangle(x, y, width, height) - size of screen 
//                rectangle = new Rectangle(Canvas_Demo.frame.getX() + 8, Canvas_Demo.frame.getY() + 27,
//                        Canvas_Demo.frame.getWidth(), Canvas_Demo.frame.getHeight() / 2);
                if(FrmMain.isRuningWeb == 0) {
                    rectangle = new Rectangle(MainView.frame.getX()+9, MainView.frame.getY() + 37,
                        MainView.frame.getWidth()-17, MainView.frame.getHeight() / 2);
                }
                else {
                    rectangle = new Rectangle(FrmMain.frameVidWeb.getX()+9, FrmMain.frameVidWeb.getY()+37,
                        FrmMain.frameVidWeb.getWidth()-20, FrmMain.frameVidWeb.getHeight()-68);
                }
                

                // Take screenshot
                bufferedImage = robot.createScreenCapture(rectangle);

                // Load image from data pixel of bufferedImage
                imageIcon = new ImageIcon(bufferedImage);

                // Display and update image on sub screen
                jlbSubScreen.setIcon(imageIcon);
                jfSubScreen.add(jlbSubScreen);
                jfSubScreen.repaint();
                jfSubScreen.revalidate();
                //

                // Write data pixel of image to byte
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if(SThread.clientSentence.equals("144p")){
                    bufferedImage = resizeImage(bufferedImage, 81, 108);
                }else if(SThread.clientSentence.equals("240p")){
                    bufferedImage = resizeImage(bufferedImage, 162, 218);
                }
                else if(SThread.clientSentence.equals("360p")){
                    bufferedImage = resizeImage(bufferedImage, 243, 327);
                }
                else if(SThread.clientSentence.equals("480p")){
                    bufferedImage = resizeImage(bufferedImage, 324, 436);
                }else if(SThread.clientSentence.equals("720p")){
                    bufferedImage = resizeImage(bufferedImage, 405, 545);
                }
                ImageIO.write(bufferedImage, "jpg", baos);

                outbuff = baos.toByteArray();
                //

                // Number of client connect
                int num = Server.i;

                for (int j = 0; j < num; j++) {
                    // Create packet for transfer
                    DatagramPacket packet = new DatagramPacket(outbuff, outbuff.length, Server.inet[j], Server.port[j]);

                    socket.send(packet);
                    baos.flush();
                }
                Thread.sleep(15);

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
}
