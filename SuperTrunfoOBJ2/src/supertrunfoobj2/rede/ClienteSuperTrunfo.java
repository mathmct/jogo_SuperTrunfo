package supertrunfoobj2.rede;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteSuperTrunfo {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket(Config.getIp(), Config.getPorta());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in);

            System.out.println("Conectado ao servidor Super Trunfo.");

            while (true) {
                Object obj = in.readObject();
                if (obj instanceof String mensagem) {
                    System.out.println(mensagem);

                    if (mensagem.toLowerCase().contains("escolha o atributo")) {
                        System.out.print("> ");
                        String atributo = scanner.nextLine();
                        out.writeObject(atributo);
                    }

                    if (mensagem.toLowerCase().contains("fim de jogo") ||
                        mensagem.toLowerCase().contains("você venceu o jogo") ||
                        mensagem.toLowerCase().contains("você perdeu o jogo")) {
                        break;
                    }
                }
            }

            in.close();
            out.close();
            socket.close();
            System.out.println("Desconectado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
