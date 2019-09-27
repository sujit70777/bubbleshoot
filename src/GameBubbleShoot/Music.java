package GameBubbleShoot;

import java.applet.*;
import java.awt.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.*;
import sun.audio.*;
import sun.swing.text.CountingPrintable;

import java.io.*;
import java.net.URL;
public class Music {
	public  void SingleBullet()
	throws Exception
      {
		
        String gongFile = "C:\\MY BUBBLE SHOOT 2\\src\\GameBubbleShoot\\SS\\SingleBullet.wav";
        InputStream in = new FileInputStream(gongFile);
            AudioStream audioStream = new AudioStream(in);
            AudioPlayer.player.start(audioStream);
 
      }
	
	public  void ExplosionEnemy()
	throws Exception
      {
        
		 String gongFile = "C:\\MY BUBBLE SHOOT 2\\src\\GameBubbleShoot\\SS\\DeadEnemy.wav";
	        InputStream in = new FileInputStream(gongFile);
	            AudioStream audioStream = new AudioStream(in);
	            AudioPlayer.player.start(audioStream);
 
      }
        
        public  void HitEnemy()
	throws Exception
      {
        
        	 String gongFile = "C:\\MY BUBBLE SHOOT 2\\src\\GameBubbleShoot\\SS\\HitEnemy.wav";
             InputStream in = new FileInputStream(gongFile);
                 AudioStream audioStream = new AudioStream(in);
                 AudioPlayer.player.start(audioStream);
 
      }
        public  void GetKill()
	throws Exception
      {
        
        	 String gongFile = "C:\\MY BUBBLE SHOOT 2\\src\\GameBubbleShoot\\SS\\beep-01.wav";
             InputStream in = new FileInputStream(gongFile);
                 AudioStream audioStream = new AudioStream(in);
                 AudioPlayer.player.start(audioStream);
 
 
      }
        
      
		
	

}
