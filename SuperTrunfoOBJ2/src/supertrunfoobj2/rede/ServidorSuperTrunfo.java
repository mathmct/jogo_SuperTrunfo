package supertrunfoobj2;

import supertrunfoobj2.entidades.*;
import supertrunfoobj2.xml.XMLHandler;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorSuperTrunfo {

    private static Baralho baralho;
    private static ObjectInputStream entradaJogador1;
    private static ObjectOutputStream saidaJogador1;
    private static ObjectInputStream entradaJogador2;
    private static ObjectOutputStream saidaJogador2;

    public static void main(String[] args) {
        try {
            ServerSocket servidor = new ServerSocket(12345);
            System.out.println("Servidor aguardando jogadores...");

            Socket jogador1 = servidor.accept();
            System.out.println("Jogador 1 conectado.");

            saidaJogador1 = new ObjectOutputStream(jogador1.getOutputStream());
            saidaJogador1.flush();
            entradaJogador1 = new ObjectInputStream(jogador1.getInputStream());
            saidaJogador1.writeObject("J1;true");

            Socket jogador2 = servidor.accept();
            System.out.println("Jogador 2 conectado.");

            saidaJogador2 = new ObjectOutputStream(jogador2.getOutputStream());
            saidaJogador2.flush();
            entradaJogador2 = new ObjectInputStream(jogador2.getInputStream());
            saidaJogador2.writeObject("J2;false");

            iniciarJogo();

            jogador1.close();
            jogador2.close();
            servidor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void iniciarJogo() throws Exception {
        File arquivo = new File("cartas.xml");
        List<CartaCarro> cartas = XMLHandler.lerCartas(arquivo);

        Collections.shuffle(cartas);
        baralho = new Baralho(new ArrayList<>(cartas));

        while (baralho.jogador1TemCartas() && baralho.jogador2TemCartas()) {
            Carta carta1 = baralho.puxarCartaJogador1();
            Carta carta2 = baralho.puxarCartaJogador2();

            saidaJogador1.writeObject("Sua carta: " + carta1);
            saidaJogador2.writeObject("Sua carta: " + carta2);

            saidaJogador1.writeObject("Escolha o atributo para jogar (potencia, velocidade, rotacoes, comprimento, cilindrada):");

            String atributo = (String) entradaJogador1.readObject();
            atributo = atributo.trim().toLowerCase();

            double valor1 = obterValor(carta1, atributo);
            double valor2 = obterValor(carta2, atributo);

            String resultado;
            if (valor1 > valor2) {
                resultado = "Jogador 1 venceu a rodada!";
                baralho.devolverCartaJogador1(carta1);
                baralho.devolverCartaJogador1(carta2);
            } else if (valor2 > valor1) {
                resultado = "Jogador 2 venceu a rodada!";
                baralho.devolverCartaJogador2(carta1);
                baralho.devolverCartaJogador2(carta2);
            } else {
                resultado = "Empate! Cada jogador mantém sua carta.";
                baralho.devolverCartaJogador1(carta1);
                baralho.devolverCartaJogador2(carta2);
            }

            saidaJogador1.writeObject(resultado);
            saidaJogador2.writeObject(resultado);

            saidaJogador1.writeObject("------------------------------------");
            saidaJogador2.writeObject("------------------------------------");
        }

        if (!baralho.jogador1TemCartas()) {
            saidaJogador1.writeObject("Você perdeu! Fim de jogo.");
            saidaJogador2.writeObject("Você venceu! Fim de jogo.");
        } else {
            saidaJogador1.writeObject("Você venceu! Fim de jogo.");
            saidaJogador2.writeObject("Você perdeu! Fim de jogo.");
        }
    }

    private static double obterValor(Carta carta, String atributo) {
        if (!(carta instanceof CartaCarro)) return 0;
        CartaCarro c = (CartaCarro) carta;
        return switch (atributo) {
            case "potencia" -> c.getPotencia();
            case "velocidade" -> c.getVelocidade();
            case "rotacoes" -> c.getRotacoes();
            case "comprimento" -> c.getComprimento();
            case "cilindrada" -> c.getCilindrada();
            default -> 0;
        };
    }
}
