package supertrunfoobj2.rede;

import supertrunfoobj2.rede.GerenciadorJogo;

import java.net.ServerSocket;
import java.net.Socket;

public class ServidorSuperTrunfo {

    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(12345)) {
            System.out.println("Servidor aguardando jogadores...");

            while (true) {
                Socket jogador1 = servidor.accept();
                System.out.println("Jogador 1 conectado.");

                Socket jogador2 = servidor.accept();
                System.out.println("Jogador 2 conectado.");

                Thread jogo = new Thread(new GerenciadorJogo(jogador1, jogador2));
                jogo.start();

                System.out.println("Jogo iniciado para dois jogadores.");                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
