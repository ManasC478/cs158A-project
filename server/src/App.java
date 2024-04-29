import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class App {
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    private static Socket clientSocket;
    public static void main(String[] args) throws Exception {
        try(ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]))){
            System.out.println("listening to port: "+args[0]);
            clientSocket = serverSocket.accept();
            System.out.println(clientSocket+" connected.");
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

            receiveFile();
//            receiveFile("NewFile2.pdf");


        } catch (Exception e){
            e.printStackTrace();
            dataInputStream.close();
            dataOutputStream.close();
            clientSocket.close();
        }
    }

    private static void receiveFile() throws Exception{
        int bytes = 0;

        long size = dataInputStream.readLong();     // read file size
        dataOutputStream.writeLong(size);
        byte[] buffer = new byte[4*1024];
        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            dataOutputStream.write(buffer, 0, (int)Math.min(buffer.length, size));
//            System.out.println(new String(buffer));
            System.out.println("Reading "+bytes+ " bytes");
            size -= bytes;      // read upto file size
        }
    }
}
