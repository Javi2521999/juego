package sounds;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
// implementar el sonido que se encuentra en la carpeta
public class GameSounds {
    private Clip clip;
    String fileName = "src/main/resources/sounds/sound.wav";
    public GameSounds() {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                AudioInputStream sound = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(sound);
            }
            else {
                throw new RuntimeException("Sound: file not found: " + fileName);
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Malformed URL: " + e);
        }
        catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Unsupported Audio File: " + e);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Input/Output Error: " + e);
        }
        catch (LineUnavailableException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Line Unavailable Exception Error: " + e);
        }
    }
    // sonido al emmpezar
    public void play(){
        clip.setFramePosition(0);
        clip.start();
    }
    //sonidos cada vez que interaccionemos en alguna clase loop
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    //sonidos al acabar
    public void stop(){
        clip.stop();
    }
}
