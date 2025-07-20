package supertrunfoobj2.rede;

import supertrunfoobj2.entidades.*;
import supertrunfoobj2.xml.XMLHandler;

import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

public class GerenciadorJogo implements Runnable {

    private final ObjectInputStream entradaJogador1;
    private final ObjectOutputStream saidaJogador1;
    private final ObjectInputStream entradaJogador2;
    private final ObjectOutputStream saidaJogador2;

    private boolean vezJogador1 = true;
    private Baralho baralho;

    public GerenciadorJogo(Socket jogador1, Socket jogador2) throws IOException {
        saidaJogador1 = new ObjectOutputStream(jogador1.getOutputStream());
        saidaJogador1.flush();
        entradaJogador1 = new ObjectInputStream(jogador1.getInputStream());

        saidaJogador2 = new ObjectOutputStream(jogador2.getOutputStream());
        saidaJogador2.flush();
        entradaJogador2 = new ObjectInputStream(jogador2.getInputStream());
    }

    @Override
    public void run() {
        try {
            saidaJogador1.writeObject("J1 começa");
            saidaJogador2.writeObject("J2 espera");

            boolean continuar = true;

            while (continuar) {
                iniciarJogo();

                
                saidaJogador1.writeObject("Deseja jogar novamente? (s/n)");
                saidaJogador2.writeObject("Deseja jogar novamente? (s/n)");
                saidaJogador1.flush();
                saidaJogador2.flush();

                String resposta1 = ((String) entradaJogador1.readObject()).trim().toLowerCase();
                String resposta2 = ((String) entradaJogador2.readObject()).trim().toLowerCase();

                if (!resposta1.equals("s") || !resposta2.equals("s")) {
                    continuar = false;
                    saidaJogador1.writeObject("Jogo finalizado. Obrigado por jogar!");
                    saidaJogador2.writeObject("Jogo finalizado. Obrigado por jogar!");
                    saidaJogador1.flush();
                    saidaJogador2.flush();
                } else {
                    vezJogador1 = true;
                }
            }

            
            entradaJogador1.close();
            saidaJogador1.close();
            entradaJogador2.close();
            saidaJogador2.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void iniciarJogo() throws Exception {
        List<CartaCarro> cartas = XMLHandler.lerCartas(new File("cartas.xml"));
        Collections.shuffle(cartas);
        baralho = new Baralho(cartas);

        while (baralho.jogador1TemCartas() && baralho.jogador2TemCartas()) {
            Carta carta1 = baralho.puxarCartaJogador1();
            Carta carta2 = baralho.puxarCartaJogador2();

            saidaJogador1.writeObject("Sua carta: " + carta1);
            saidaJogador2.writeObject("Sua carta: " + carta2);

            saidaJogador1.writeObject("Voce tem " + baralho.tamanhoJogador1() +
                    " cartas. Oponente tem " + baralho.tamanhoJogador2() + " cartas.");
            saidaJogador2.writeObject("Voce tem " + baralho.tamanhoJogador2() +
                    " cartas. Oponente tem " + baralho.tamanhoJogador1() + " cartas.");

            String atributo;
            if (vezJogador1) {
                saidaJogador1.writeObject("Escolha o atributo para jogar (potencia, velocidade, rotacoes, comprimento, cilindrada):");
                atributo = ((String) entradaJogador1.readObject()).trim().toLowerCase();
            } else {
                saidaJogador2.writeObject("Escolha o atributo para jogar (potencia, velocidade, rotacoes, comprimento, cilindrada):");
                atributo = ((String) entradaJogador2.readObject()).trim().toLowerCase();
            }

            double valor1 = obterValor(carta1, atributo);
            double valor2 = obterValor(carta2, atributo);

            String resultado;
            if (valor1 > valor2) {
                resultado = "Jogador 1 venceu a rodada!";
                baralho.devolverCartaJogador1(carta1);
                baralho.devolverCartaJogador1(carta2);
                vezJogador1 = true;
            } else if (valor2 > valor1) {
                resultado = "Jogador 2 venceu a rodada!";
                baralho.devolverCartaJogador2(carta1);
                baralho.devolverCartaJogador2(carta2);
                vezJogador1 = false;
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
            saidaJogador1.writeObject("Voce perdeu! Fim de jogo.");
            saidaJogador2.writeObject("Voce venceu! Fim de jogo.");
        } else {
            saidaJogador1.writeObject("Voce venceu! Fim de jogo.");
            saidaJogador2.writeObject("Voce perdeu! Fim de jogo.");
        }
    }

    private double obterValor(Carta carta, String atributo) {
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
