import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int port = 6379;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while(true){

                // Since the tester restarts your program quite often, setting SO_REUSEADDR
                // ensures that we don't run into 'Address already in use' errors
                serverSocket.setReuseAddress(true);
                // Wait for connection from client.
                Socket clientSocket = serverSocket.accept();

                Thread worker = new Thread(
                        () -> handleClient(clientSocket)
                );
                worker.start();

            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private static void handleClient(Socket client){
        try (
                client;
                Scanner sc = new Scanner(
                        client.getInputStream(),
                        StandardCharsets.UTF_8
                        );
                ) {
            OutputStream output = client.getOutputStream();


            while(sc.hasNextLine()){
                String nextLine = sc.nextLine();
                if(nextLine.contains("PING")){
                    output.write(
                            "+PONG\r\n".getBytes(StandardCharsets.UTF_8)
                    );
                }
                output.flush();
            }
        }catch (IOException e){
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
