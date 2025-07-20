package supertrunfoobj2.rede;

import supertrunfoobj2.entidades.CartaCarro;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TelaJogo extends JFrame {

    private JTextArea areaMensagens;
    private JPanel painelCarta;
    private JPanel painelAtributos;
    private JButton botaoJogar;
    private JButton botaoJogarNovamente;

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final Map<String, JButton> botoesAtributos = new HashMap<>();

    public TelaJogo() {
        setTitle("Super Trunfo - Jogo");
        setSize(1200, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        inicializarInterface();
        configurarAcoes();
    }

    private void inicializarInterface() {
        areaMensagens = new JTextArea();
        areaMensagens.setEditable(false);
        areaMensagens.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(areaMensagens), BorderLayout.CENTER);

        painelAtributos = new JPanel(new GridLayout(1, 5, 10, 10));
        String[] atributos = {"potencia", "velocidade", "rotacoes", "comprimento", "cilindrada"};
        for (String atr : atributos) {
            JButton botao = new JButton(atr.substring(0, 1).toUpperCase() + atr.substring(1));
            botao.setEnabled(false);
            botao.addActionListener(e -> enviarAtributo(atr));
            painelAtributos.add(botao);
            botoesAtributos.put(atr, botao);
        }
        add(painelAtributos, BorderLayout.NORTH);

        painelCarta = new JPanel();
        painelCarta.setBorder(BorderFactory.createTitledBorder("Sua Carta"));
        painelCarta.setPreferredSize(new Dimension(220, 160));
        painelCarta.add(new JLabel("Dados da carta aparecerão aqui"));
        add(painelCarta, BorderLayout.WEST);

        JPanel painelControle = new JPanel();
        botaoJogar = new JButton("Jogar");
        botaoJogarNovamente = new JButton("Jogar de Novo");
        botaoJogarNovamente.setVisible(false);
        painelControle.add(botaoJogar);
        painelControle.add(botaoJogarNovamente);
        add(painelControle, BorderLayout.SOUTH);
    }

    private void configurarAcoes() {
        botaoJogar.addActionListener(e -> {
            botaoJogar.setEnabled(false);
            conectarAoServidor();
        });

        botaoJogarNovamente.addActionListener(e -> {
            try {
                out.writeObject("s");
                out.flush();
                botaoJogarNovamente.setVisible(false);
                areaMensagens.append("Jogando novamente...\n");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void conectarAoServidor() {
        try {
            Socket socket = new Socket(Config.getIp(), Config.getPorta());
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            new Thread(() -> {
                try {
                    while (true) {
                        Object obj = in.readObject();
                        if (obj instanceof String mensagem) {
                            processarMensagem(mensagem);
                            if (mensagem.toLowerCase().contains("jogo finalizado")) break;
                        }
                    }
                    encerrarJogo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao servidor!");
            e.printStackTrace();
        }
    }

    private void processarMensagem(String mensagem) {
        SwingUtilities.invokeLater(() -> {
            areaMensagens.append(mensagem + "\n");
            pedirAtributo(mensagem);
            mostrarBotaoJogarNovamente(mensagem);
            exibirCarta(mensagem);
        });
    }

    private void pedirAtributo(String mensagem) {
        boolean precisaEscolher = mensagem.toLowerCase().contains("escolha o atributo");
        botoesAtributos.values().forEach(botao -> botao.setEnabled(precisaEscolher));
    }

    private void mostrarBotaoJogarNovamente(String mensagem) {
        botaoJogarNovamente.setVisible(mensagem.toLowerCase().contains("deseja jogar novamente"));
    }

    private void exibirCarta(String mensagem) {
        if (mensagem.toLowerCase().startsWith("sua carta:")) {
            String dadosCarta = mensagem.replace("Sua carta: ", "").trim();
            painelCarta.removeAll();
            painelCarta.add(new JLabel("<html>" + dadosCarta.replace("|", "<br>") + "</html>"));
            painelCarta.revalidate();
            painelCarta.repaint();
        }
    }

    private void encerrarJogo() {
        try {
            in.close();
            out.close();
            areaMensagens.append("Desconectado.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enviarAtributo(String atributo) {
        try {
            out.writeObject(atributo);
            out.flush();
            botoesAtributos.values().forEach(botao -> botao.setEnabled(false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaJogo tela = new TelaJogo();
            tela.setVisible(true);
        });
    }
}
