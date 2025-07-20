package supertrunfoobj2.xml;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class LoggerXML {

    public static void salvarResultado(String vencedor, List<String> logRodadas) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element raiz = doc.createElement("partida");
            raiz.setAttribute("dataHora", LocalDateTime.now().toString());
            raiz.setAttribute("vencedor", vencedor);
            doc.appendChild(raiz);

            for (String log : logRodadas) {
                Element rodada = doc.createElement("rodada");
                rodada.appendChild(doc.createTextNode(log));
                raiz.appendChild(rodada);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(new File("log_partidas.xml")));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
