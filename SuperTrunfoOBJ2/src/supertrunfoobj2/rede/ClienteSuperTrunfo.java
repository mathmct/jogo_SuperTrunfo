package supertrunfoobj2.rede;

import java.io.*;
import java.net.Socket;
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
                        out.flush();
                    } else if (mensagem.toLowerCase().contains("deseja jogar novamente")) {
                        System.out.print("> ");
                        String resposta = scanner.nextLine();
                        out.writeObject(resposta);
                        out.flush();

                        if (!resposta.trim().equalsIgnoreCase("s")) {
                            break;
                        }
                    } else if (mensagem.toLowerCase().contains("jogo finalizado")) {
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
