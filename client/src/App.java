import java.io.*;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class App {
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    private static Socket socket;
    public static void main(String[] args) throws Exception {
        try(Socket socket = new Socket("localhost",4000)) {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            CountDownLatch latch = new CountDownLatch(2);



            Thread senderThread = new Thread(() -> {
                sendFile("/Users/manaschougule/College/SJSU/CS-158A/project/stay-schemin.mp3");
                latch.countDown();
            });
            Thread readerThread = new Thread(() -> {
//                readStream();

                try {
                    dataInputStream.readLong();
                    Player player = new Player(dataInputStream);
                    player.play();
                } catch (IOException | JavaLayerException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            });

            senderThread.start();
            readerThread.start();

            latch.await();
            dataInputStream.close();
            dataInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void readStream() {
        try {
            System.out.println("in read stream");
            int bytes = 0;

            long size = dataInputStream.readLong();     // read file size
            System.out.println("Received file is "+size+" bytes large");
            byte[] buffer = new byte[4*1024];
            while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
                System.out.println("Read "+bytes+ " bytes");
                size -= bytes;      // read upto file size
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void sendFile(String path) {
        File file = new File(path);

        try(FileInputStream fileInputStream = new FileInputStream(file)){
            int bytes = 0;
            dataOutputStream.writeLong(file.length());
            // break file into chunks
            byte[] buffer = new byte[4*1024];
            while ((bytes=fileInputStream.read(buffer))!=-1){
                System.out.println("Writing "+bytes+" bytes");
                dataOutputStream.write(buffer,0,bytes);
                dataOutputStream.flush();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
