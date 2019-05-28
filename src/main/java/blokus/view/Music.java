package blokus.view;

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import blokus.model.Config;

/**
 * Music
 */
public class Music {
  private ArrayList<String> musicFiles;
  private FloatControl gainControl;
  private int curMusicNo;
  private Clip clip;

  public Music() {
    musicFiles = Config.i().getMany(Config.MUSIC);
    curMusicNo = 0;
    try {
      clip = AudioSystem.getClip();
      clip.addLineListener((e) -> {
        if (e.getType() == LineEvent.Type.STOP) {
          next();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
    play();
  }
  
  void next() {
    curMusicNo = (curMusicNo + 1) % (musicFiles.size());
    clip.close();
    play();
  }

  void play() {
    try {
      System.out.println("Playing: " + musicFiles.get(curMusicNo));
      AudioInputStream inputStream = AudioSystem.getAudioInputStream(Config.load(musicFiles.get(curMusicNo)));
      clip.open(inputStream);
      gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(Config.i().getf(Config.VOLUME)); // Reduce volume by 10 decibels.
      clip.start();
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
      e.printStackTrace();
    }
  }

  public void setSound(float value) {
    gainControl.setValue(value);
    Config.i().set(Config.VOLUME, gainControl.getValue());
  }

  public void mute() {
    setSound(gainControl.getMinimum());
  }

  public double getSound() {
    return gainControl.getValue();
  }
}