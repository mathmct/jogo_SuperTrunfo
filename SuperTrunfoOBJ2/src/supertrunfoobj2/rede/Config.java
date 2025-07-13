package supertrunfoobj2.rede;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;

public class Config {
    private static String ip;
    private static int porta = -1;

    private Config() {}

    private static void readConfig() {
        try {
            File file = new File("config.xml");
            if (!file.exists()) throw new RuntimeException("Arquivo config.xml não encontrado.");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();
            Node servidor = root.getElementsByTagName("servidor").item(0);

            ip = servidor.getAttributes().getNamedItem("ip").getNodeValue();
            porta = Integer.parseInt(servidor.getAttributes().getNamedItem("porta").getNodeValue());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler config.xml", e);
        }
    }

    public static String getIp() {
        if (ip == null) readConfig();
        return ip;
    }

    public static int getPorta() {
        if (porta == -1) readConfig();
        return porta;
    }
}
