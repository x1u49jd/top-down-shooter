package main;

import javax.sound.sampled.*;
import java.io.File;

public class Sound {
    public static void play(String path) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
