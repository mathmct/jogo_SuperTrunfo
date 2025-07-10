package supertrunfoobj2;

import supertrunfoobj2.entidades.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorJogo {
    private static Baralho baralho;
    private static BufferedReader in1, in2;
    private static PrintWriter out1, out2;

    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(12345)) {
            System.out.println("Servidor aguardando jogadores...");

            Socket jogador1 = servidor.accept();
            System.out.println("Jogador 1 conectado.");
            Socket jogador2 = servidor.accept();
            System.out.println("Jogador 2 conectado.");

            in1 = new BufferedReader(new InputStreamReader(jogador1.getInputStream()));
            out1 = new PrintWriter(jogador1.getOutputStream(), true);

            in2 = new BufferedReader(new InputStreamReader(jogador2.getInputStream()));
            out2 = new PrintWriter(jogador2.getOutputStream(), true);

            iniciarJogo();

            jogador1.close();
            jogador2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void iniciarJogo() throws IOException {
        List<Carta> cartas = new ArrayList<>();
        cartas.add(new CartaCarro("Fusca", "001", 1300, 45, 4000, 140, 3.8, false));
        cartas.add(new CartaCarro("Ferrari", "002", 4000, 600, 8000, 340, 4.5, true));
        cartas.add(new CartaCarro("Gol", "003", 1600, 100, 5000, 180, 4.2, false));
        cartas.add(new CartaCarro("Camaro", "004", 6200, 426, 6000, 300, 4.7, false));
        cartas.add(new CartaCarro("Civic", "005", 2000, 155, 7000, 210, 4.3, false));
        cartas.add(new CartaCarro("Uno com escada", "006", 1000, 70, 4500, 160, 3.6, false));

        baralho = new Baralho(cartas);

        while (baralho.jogador1TemCartas() && baralho.jogador2TemCartas()) {
            Carta carta1 = baralho.puxarCartaJogador1();
            Carta carta2 = baralho.puxarCartaJogador2();

            out1.println("Sua carta: " + carta1);
            out2.println("Sua carta: " + carta2);

            out1.println("Escolha o atributo para jogar (potencia, velocidade, rotacoes, comprimento, cilindrada):");
            String atributo = in1.readLine().toLowerCase();

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

            out1.println(resultado);
            out2.println(resultado);
            out1.println("------------------------------------");
            out2.println("------------------------------------");
        }

        if (!baralho.jogador1TemCartas()) {
            out1.println("Você perdeu! Fim de jogo.");
            out2.println("Você venceu! Fim de jogo.");
        } else {
            out1.println("Você venceu! Fim de jogo.");
            out2.println("Você perdeu! Fim de jogo.");
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
