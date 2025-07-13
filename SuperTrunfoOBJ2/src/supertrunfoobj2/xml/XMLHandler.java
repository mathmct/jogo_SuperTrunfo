package supertrunfoobj2.xml;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import supertrunfoobj2.entidades.CartaCarro;

public class XMLHandler {

    public static List<CartaCarro> lerCartas(File arquivo) throws Exception {
        List<CartaCarro> cartas = new ArrayList<>();

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(arquivo);
        doc.getDocumentElement().normalize();

        NodeList listaCartas = doc.getElementsByTagName("carta");

        for (int i = 0; i < listaCartas.getLength(); i++) {
            Element e = (Element) listaCartas.item(i);

            String nome = e.getAttribute("nome");
            String codigo = e.getAttribute("codigo");
            int cilindrada = Integer.parseInt(e.getAttribute("cilindrada"));
            int potencia = Integer.parseInt(e.getAttribute("potencia"));
            int rotacoes = Integer.parseInt(e.getAttribute("rotacoes"));
            int velocidade = Integer.parseInt(e.getAttribute("velocidade"));
            double comprimento = Double.parseDouble(e.getAttribute("comprimento"));
            boolean superTrunfo = Boolean.parseBoolean(e.getAttribute("superTrunfo"));

            cartas.add(new CartaCarro(nome, codigo, cilindrada, potencia, rotacoes, velocidade, comprimento, superTrunfo));
        }

        return cartas;
    }
}
