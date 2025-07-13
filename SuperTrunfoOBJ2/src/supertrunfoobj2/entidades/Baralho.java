package supertrunfoobj2.entidades;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Baralho {
    private Queue<Carta> filaJogador1;
    private Queue<Carta> filaJogador2;

    public Baralho(List<Carta> cartas) {
        if (cartas == null || cartas.size() < 2) {
            throw new IllegalArgumentException("Deve haver pelo menos duas cartas.");
        }

        Collections.shuffle(cartas);

        filaJogador1 = new LinkedList<>();
        filaJogador2 = new LinkedList<>();

        for (int i = 0; i < cartas.size(); i++) {
            if (i % 2 == 0) {
                filaJogador1.add(cartas.get(i));
            } else {
                filaJogador2.add(cartas.get(i));
            }
        }
    }

    public Carta puxarCartaJogador1() {
        return filaJogador1.poll();
    }

    public Carta puxarCartaJogador2() {
        return filaJogador2.poll();
    }

    public void devolverCartaJogador1(Carta carta) {
        filaJogador1.add(carta);
    }

    public void devolverCartaJogador2(Carta carta) {
        filaJogador2.add(carta);
    }

    public boolean jogador1TemCartas() {
        return !filaJogador1.isEmpty();
    }

    public boolean jogador2TemCartas() {
        return !filaJogador2.isEmpty();
    }

    public int tamanhoJogador1() {
        return filaJogador1.size();
    }

    public int tamanhoJogador2() {
        return filaJogador2.size();
    }
}
