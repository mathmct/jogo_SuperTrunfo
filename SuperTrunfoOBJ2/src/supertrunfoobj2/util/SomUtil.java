package supertrunfoobj2.util;

import javax.sound.sampled.*;
import java.io.File;

public class SomUtil {

    public static void tocar(String nomeArquivo) {
        try {
            File file = new File("sounds/" + nomeArquivo + ".wav"); // ou ".mp3" se usar MP3SPI
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Erro ao tocar som: " + nomeArquivo);
            e.printStackTrace();
        }
    }
}
