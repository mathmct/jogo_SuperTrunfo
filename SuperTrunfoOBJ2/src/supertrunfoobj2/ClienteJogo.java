package supertrunfoobj2;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteJogo {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            String msg;
            while ((msg = in.readLine()) != null) {
                System.out.println(msg);
                if (msg.contains("Escolha o atributo")) {
                    System.out.print("> ");
                    String atributo = scanner.nextLine();
                    out.println(atributo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

