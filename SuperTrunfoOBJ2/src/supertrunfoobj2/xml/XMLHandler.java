package supertrunfoobj2.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import java.io.File;
import java.util.List;
import supertrunfoobj2.entidades.CartaCarro;

public class XMLHandler {

    public static void salvarCartas(List<CartaCarro> cartas, File arquivo) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();

        Element root = doc.createElement("cartas");
        doc.appendChild(root);

        for (CartaCarro c : cartas) {
            Element elem = doc.createElement("carta");
            elem.setAttribute("nome", c.getNome());
            elem.setAttribute("codigo", c.getCodigo());
            elem.setAttribute("cilindrada", String.valueOf(c.getCilindrada()));
            elem.setAttribute("potencia", String.valueOf(c.getPotencia()));
            elem.setAttribute("rotacoes", String.valueOf(c.getRotacoes()));
            elem.setAttribute("velocidade", String.valueOf(c.getVelocidade()));
            elem.setAttribute("comprimento", String.valueOf(c.getComprimento()));
            elem.setAttribute("superTrunfo", String.valueOf(c.isSuperTrunfo()));
            root.appendChild(elem);
        }

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(arquivo);
        transformer.transform(source, result);
    }
}
