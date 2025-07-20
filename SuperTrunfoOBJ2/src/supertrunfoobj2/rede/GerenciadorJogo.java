package supertrunfoobj2.rede;

import supertrunfoobj2.entidades.*;
import supertrunfoobj2.xml.XMLHandler;
import supertrunfoobj2.xml.LoggerXML;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GerenciadorJogo implements Runnable {
    private final ObjectInputStream entradaJogador1;
    private final ObjectOutputStream saidaJogador1;
    private final ObjectInputStream entradaJogador2;
    private final ObjectOutputStream saidaJogador2;
    private boolean vezJogador1 = true;
    private Baralho baralho;
    private final List<String> logRodadas = new ArrayList<>();

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

                saidaJogador1.writeObject("SOM:end");
                saidaJogador2.writeObject("SOM:end");
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
    logRodadas.clear();

    while (baralho.jogador1TemCartas() && baralho.jogador2TemCartas()) {
        Carta carta1 = baralho.puxarCartaJogador1();
        Carta carta2 = baralho.puxarCartaJogador2();

        saidaJogador1.writeObject("Sua carta: " + carta1);
        saidaJogador2.writeObject("Sua carta: " + carta2);

        int totalJogador1 = baralho.tamanhoJogador1() + 1;
        int totalJogador2 = baralho.tamanhoJogador2() + 1;

        saidaJogador1.writeObject("Voce tem " + totalJogador1 + " cartas. Oponente tem " + totalJogador2 + " cartas.");
        saidaJogador2.writeObject("Voce tem " + totalJogador2 + " cartas. Oponente tem " + totalJogador1 + " cartas.");

        String atributo;
        if (vezJogador1) {
            saidaJogador1.writeObject("Escolha o atributo para jogar (potencia, velocidade, comprimento, rotacoes, cilindrada):");
            atributo = ((String) entradaJogador1.readObject()).trim().toLowerCase();
        } else {
            saidaJogador2.writeObject("Escolha o atributo para jogar (potencia, velocidade, comprimento, rotacoes, cilindrada):");
            atributo = ((String) entradaJogador2.readObject()).trim().toLowerCase();
        }

        saidaJogador1.writeObject("Carta do oponente: " + carta2);
        saidaJogador2.writeObject("Carta do oponente: " + carta1);

        boolean carta1Super = carta1 instanceof CartaCarro && ((CartaCarro) carta1).isSuperTrunfo();
        boolean carta2Super = carta2 instanceof CartaCarro && ((CartaCarro) carta2).isSuperTrunfo();

        String codigo1 = carta1.getCodigo().toUpperCase();
        String codigo2 = carta2.getCodigo().toUpperCase();

        String resultado;

        if (carta1Super && !carta2Super) {
            if (codigo2.length() >= 2 && codigo2.charAt(1) == 'A') {
                resultado = "Jogador 2 venceu a rodada! Carta comum com código A venceu o SUPER TRUNFO.";
                baralho.devolverCartaJogador2(carta1);
                baralho.devolverCartaJogador2(carta2);
                vezJogador1 = false;
            } else {
                resultado = "Jogador 1 venceu a rodada com SUPER TRUNFO!";
                baralho.devolverCartaJogador1(carta1);
                baralho.devolverCartaJogador1(carta2);
                vezJogador1 = true;
            }
        } else if (carta2Super && !carta1Super) {
            if (codigo1.length() >= 2 && codigo1.charAt(1) == 'A') {
                resultado = "Jogador 1 venceu a rodada! Carta comum com código A venceu o SUPER TRUNFO.";
                baralho.devolverCartaJogador1(carta1);
                baralho.devolverCartaJogador1(carta2);
                vezJogador1 = true;
            } else {
                resultado = "Jogador 2 venceu a rodada com SUPER TRUNFO!";
                baralho.devolverCartaJogador2(carta1);
                baralho.devolverCartaJogador2(carta2);
                vezJogador1 = false;
            }
        } else {
            double valor1 = obterValor(carta1, atributo);
            double valor2 = obterValor(carta2, atributo);

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
        }

        if (resultado.contains("Jogador 1 venceu")) {
            saidaJogador1.writeObject("SOM:win");
            saidaJogador2.writeObject("SOM:lose");
        } else if (resultado.contains("Jogador 2 venceu")) {
            saidaJogador1.writeObject("SOM:lose");
            saidaJogador2.writeObject("SOM:win");
        } else if (resultado.contains("Empate")) {
            saidaJogador1.writeObject("SOM:draw");
            saidaJogador2.writeObject("SOM:draw");
        }

        double valor1 = obterValor(carta1, atributo);
        double valor2 = obterValor(carta2, atributo);

        String log = String.format("Rodada - J1: %s [%s: %.2f] | J2: %s [%s: %.2f] | Resultado: %s%s",
                carta1.getNome(), atributo, valor1,
                carta2.getNome(), atributo, valor2,
                resultado,
                resultado.toLowerCase().contains("super trunfo") ? " ⭐"
                    : resultado.toLowerCase().contains("código a venceu") ? " ✖️"
                    : ""
        );

        logRodadas.add(log);

        saidaJogador1.writeObject("Comparação: " + atributo.toUpperCase() + " → Jogador 1: " + valor1 + " | Jogador 2: " + valor2);
        saidaJogador2.writeObject("Comparação: " + atributo.toUpperCase() + " → Jogador 1: " + valor1 + " | Jogador 2: " + valor2);

        saidaJogador1.writeObject(resultado);
        saidaJogador2.writeObject(resultado);
        saidaJogador1.writeObject("------------------------------------");
        saidaJogador2.writeObject("------------------------------------");
    }

    String vencedor;
    if (!baralho.jogador1TemCartas()) {
        saidaJogador1.writeObject("Voce perdeu! Fim de jogo.");
        saidaJogador2.writeObject("Voce venceu! Fim de jogo.");
        vencedor = "Jogador 2";
    } else {
        saidaJogador1.writeObject("Voce venceu! Fim de jogo.");
        saidaJogador2.writeObject("Voce perdeu! Fim de jogo.");
        vencedor = "Jogador 1";
    }

    LoggerXML.salvarResultado(vencedor, logRodadas);
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
