package supertrunfoobj2;

import supertrunfoobj2.entidades.*;
import java.util.*;

public class SuperTrunfoOBJ2 {

    public static void main(String[] args) {
        List<Carta> cartas = new ArrayList<>();

        cartas.add(new CartaCarro("Fusca", "001", 1300, 45, 4000, 140, 3.8, false));
        cartas.add(new CartaCarro("Ferrari", "002", 4000, 600, 8000, 340, 4.5, true));
        cartas.add(new CartaCarro("Gol", "003", 1600, 100, 5000, 180, 4.2, false));
        cartas.add(new CartaCarro("Camaro", "004", 6200, 426, 6000, 300, 4.7, false));
        cartas.add(new CartaCarro("Civic", "005", 2000, 155, 7000, 210, 4.3, false));
        cartas.add(new CartaCarro("Uno com escada", "006", 1000, 70, 4500, 160, 3.6, false));

        Baralho baralho = new Baralho(cartas);

        System.out.println("Cartas iniciais:");
        System.out.println("Jogador 1: " + baralho.tamanhoJogador1() + " cartas");
        System.out.println("Jogador 2: " + baralho.tamanhoJogador2() + " cartas");

        System.out.println("\n--- RODADA ---");

        Carta carta1 = baralho.puxarCartaJogador1();
        Carta carta2 = baralho.puxarCartaJogador2();

        System.out.println("Jogador 1 jogou: " + carta1);
        System.out.println("Jogador 2 jogou: " + carta2);

        baralho.devolverCartaJogador1(carta1);
        baralho.devolverCartaJogador2(carta2);

        System.out.println("\nCartas após a rodada:");
        System.out.println("Jogador 1: " + baralho.tamanhoJogador1() + " cartas");
        System.out.println("Jogador 2: " + baralho.tamanhoJogador2() + " cartas");
    }
}
