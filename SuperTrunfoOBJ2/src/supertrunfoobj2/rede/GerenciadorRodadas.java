package supertrunfoobj2.rede;

import supertrunfoobj2.entidades.*;

import java.io.*;
import java.util.*;

public class GerenciadorRodadas implements Runnable {

    private ObjectInputStream entradaJogador;
    private ObjectOutputStream saidaJogador;
    private Queue<Carta> cartasJogador;
    private boolean ehJogadorEscolhendo;
    private static final List<String> atributos = List.of("potencia", "velocidade", "rotacoes", "comprimento", "cilindrada");

    private static ObjectOutputStream saidaOponente;
    private static ObjectInputStream entradaOponente;
    private static Queue<Carta> cartasOponente;

    public GerenciadorRodadas(ObjectInputStream entrada, ObjectOutputStream saida, Queue<Carta> cartas, boolean escolhe) {
        this.entradaJogador = entrada;
        this.saidaJogador = saida;
        this.cartasJogador = cartas;
        this.ehJogadorEscolhendo = escolhe;
    }

    public static void setOponente(ObjectOutputStream saida, ObjectInputStream entrada, Queue<Carta> cartas) {
        saidaOponente = saida;
        entradaOponente = entrada;
        cartasOponente = cartas;
    }

    @Override
    public void run() {
        try {
            while (!cartasJogador.isEmpty() && !cartasOponente.isEmpty()) {
                Carta cartaJ = cartasJogador.poll();
                Carta cartaO = cartasOponente.poll();

                saidaJogador.writeObject("Sua carta: " + cartaJ);
                saidaOponente.writeObject("Sua carta: " + cartaO);

                String atributoEscolhido;

                if (ehJogadorEscolhendo) {
                    saidaJogador.writeObject("Escolha o atributo (potencia, velocidade, rotacoes, comprimento, cilindrada):");
                    atributoEscolhido = (String) entradaJogador.readObject();
                } else {
                    atributoEscolhido = (String) entradaOponente.readObject();
                }

                double valorJ = obterValor((CartaCarro) cartaJ, atributoEscolhido);
                double valorO = obterValor((CartaCarro) cartaO, atributoEscolhido);

                String resultado;

                if (valorJ > valorO) {
                    resultado = "Você venceu esta rodada!";
                    cartasJogador.add(cartaJ);
                    cartasJogador.add(cartaO);
                } else if (valorO > valorJ) {
                    resultado = "Você perdeu esta rodada.";
                    cartasOponente.add(cartaJ);
                    cartasOponente.add(cartaO);
                } else {
                    resultado = "Empate. Cada um mantém sua carta.";
                    cartasJogador.add(cartaJ);
                    cartasOponente.add(cartaO);
                }

                saidaJogador.writeObject(resultado);
                saidaOponente.writeObject(inverterResultado(resultado));

                ehJogadorEscolhendo = !ehJogadorEscolhendo;
            }

            if (cartasJogador.isEmpty()) {
                saidaJogador.writeObject("Você perdeu o jogo.");
                saidaOponente.writeObject("Você venceu o jogo!");
            } else {
                saidaJogador.writeObject("Você venceu o jogo!");
                saidaOponente.writeObject("Você perdeu o jogo.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double obterValor(CartaCarro carta, String atributo) {
        return switch (atributo.toLowerCase()) {
            case "potencia" -> carta.getPotencia();
            case "velocidade" -> carta.getVelocidade();
            case "rotacoes" -> carta.getRotacoes();
            case "comprimento" -> carta.getComprimento();
            case "cilindrada" -> carta.getCilindrada();
            default -> 0;
        };
    }

    private String inverterResultado(String msg) {
        return switch (msg) {
            case "Você venceu esta rodada!" -> "Você perdeu esta rodada.";
            case "Você perdeu esta rodada." -> "Você venceu esta rodada!";
            case "Você venceu o jogo!" -> "Você perdeu o jogo.";
            case "Você perdeu o jogo." -> "Você venceu o jogo!";
            default -> msg;
        };
    }
}
