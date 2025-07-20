package supertrunfoobj2.rede;

import supertrunfoobj2.entidades.CartaCarro;
import supertrunfoobj2.util.SomUtil;

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
    private JButton botaoVerLog;

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final Map<String, JButton> botoesAtributos = new HashMap<>();

    public TelaJogo() {
        UIManager.put("Panel.background", new Color(30, 33, 38));
        UIManager.put("Button.background", new Color(53, 57, 64));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("TextArea.background", new Color(30, 30, 30));
        UIManager.put("TextArea.foreground", Color.WHITE);
        UIManager.put("TitledBorder.titleColor", Color.WHITE);

        setTitle("Super Trunfo - Jogo");
        setSize(1200, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(20, 22, 28));

        inicializarInterface();
        configurarAcoes();
    }

    private void inicializarInterface() {
        Font fonte = new Font("Segoe UI", Font.PLAIN, 14);

        areaMensagens = new JTextArea();
        areaMensagens.setEditable(false);
        areaMensagens.setFont(fonte);
        add(new JScrollPane(areaMensagens), BorderLayout.CENTER);

        painelAtributos = new JPanel(new GridLayout(1, 5, 10, 10));
        String[] atributos = {"potencia", "velocidade", "comprimento", "rotacoes", "cilindrada"};
        for (String atr : atributos) {
            JButton botao = new JButton(atr.substring(0, 1).toUpperCase() + atr.substring(1));
            botao.setFont(fonte);
            botao.setEnabled(false);
            botao.addActionListener(e -> enviarAtributo(atr));
            painelAtributos.add(botao);
            botoesAtributos.put(atr, botao);
        }
        add(painelAtributos, BorderLayout.NORTH);

        painelCarta = new JPanel();
        painelCarta.setBorder(BorderFactory.createTitledBorder("Sua Carta"));
        painelCarta.setPreferredSize(new Dimension(400, 300));
        painelCarta.setLayout(new BoxLayout(painelCarta, BoxLayout.Y_AXIS));
        painelCarta.add(new JLabel("Dados da carta aparecerão aqui"));
        add(painelCarta, BorderLayout.WEST);

        JPanel painelControle = new JPanel();
        botaoJogar = new JButton("Jogar");
        botaoJogarNovamente = new JButton("Jogar de Novo");
        botaoJogarNovamente.setVisible(false);
        botaoVerLog = new JButton("Ver Log");

        botaoJogar.setFont(fonte);
        botaoJogarNovamente.setFont(fonte);
        botaoVerLog.setFont(fonte);

        painelControle.add(botaoJogar);
        painelControle.add(botaoJogarNovamente);
        painelControle.add(botaoVerLog);
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

        botaoVerLog.addActionListener(e -> {
            try {
                File file = new File("log_partidas.xml");
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(this, "Nenhum log de partida encontrado.");
                    return;
                }

                var doc = javax.xml.parsers.DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder().parse(file);
                doc.getDocumentElement().normalize();
                var rodadas = doc.getElementsByTagName("rodada");

                StringBuilder sb = new StringBuilder("📜 Log da Última Partida:\n\n");
                for (int i = 0; i < rodadas.getLength(); i++) {
                    sb.append("- ").append(rodadas.item(i).getTextContent()).append("\n");
                }

                JTextArea logArea = new JTextArea(sb.toString());
                logArea.setEditable(false);
                logArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                logArea.setBackground(new Color(40, 42, 48));
                logArea.setForeground(Color.WHITE);

                JScrollPane scroll = new JScrollPane(logArea);
                scroll.setPreferredSize(new Dimension(500, 300));

                JOptionPane.showMessageDialog(this, scroll, "Log de Partida", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
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
            if (mensagem.startsWith("SOM:")) {
                String nomeSom = mensagem.replace("SOM:", "").trim();
                SomUtil.tocar(nomeSom);
                return;
            }

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

            JLabel textoCarta = new JLabel("<html>" + dadosCarta.replace("|", "<br>") + "</html>");
            textoCarta.setAlignmentX(Component.CENTER_ALIGNMENT);
            textoCarta.setForeground(Color.WHITE);
            textoCarta.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            String codigo = "";
            int ini = dadosCarta.indexOf("(");
            int fim = dadosCarta.indexOf(")");
            if (ini != -1 && fim != -1 && fim > ini) {
                codigo = dadosCarta.substring(ini + 1, fim).toLowerCase();
            }

            try {
                ImageIcon iconeCarta = new ImageIcon("imagens/" + codigo + ".png");
                Image imagem = iconeCarta.getImage().getScaledInstance(400, 500, Image.SCALE_SMOOTH);
                JLabel imagemCarta = new JLabel(new ImageIcon(imagem));
                imagemCarta.setAlignmentX(Component.CENTER_ALIGNMENT);
                painelCarta.add(imagemCarta);
            } catch (Exception e) {
                painelCarta.add(new JLabel("Imagem não encontrada."));
            }

            painelCarta.add(textoCarta);
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
