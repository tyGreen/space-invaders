import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

public class BgMusic {
	
	// Constructor
	   public BgMusic() {
	   
	      try {
	         // Open audio input stream:
	         URL url = this.getClass().getClassLoader().getResource("music_bg.wav");
	         AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
	         
	         // Retrieve sound clip resource:
	         Clip bgMusic = AudioSystem.getClip();
	         
	         // Open clip and load sample from input stream:
	         bgMusic.open(audioIn);
	         bgMusic.start();
	         
	         // Play clip on loop:
	         bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
	         
	      } catch (UnsupportedAudioFileException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (LineUnavailableException e) {
	         e.printStackTrace();
	      }
	   }
	
}
