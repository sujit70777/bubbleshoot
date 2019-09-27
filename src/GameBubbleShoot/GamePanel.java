package GameBubbleShoot;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GamePanel extends JPanel implements Runnable, KeyListener{
	
	//------------- VARIBLEAS------------
	
	public static int WIDTH = 600;
	public static int HEIGHT = 550;
	
	private Thread thread;
	private boolean running ;
	
	private BufferedImage image;
	private Graphics2D g;
	
	private int FPS = 30;
	private double avarageFPS;
	public static ArrayList<Bullet> bullets;
	public static ArrayList<Enemy> enemies;
	public static ArrayList<PowerUp> powerUps;
	public static ArrayList<Explosion> explosions;
        public static ArrayList<Text> texts;

	public static Player player;
	
	private long RoundStartTime;
	private long RoundStartTimeDiff;
	private int RoundNumber;
	private boolean RoundStrat;
	private int RoundDelay = 2000;
//	private Color BackgroundColor = new Color(72,60,50);
	private Color BackgroundColor = Color.BLACK;
	
        private boolean Pause;
	private boolean Start;
	private boolean ReStart;
	private boolean GameOver;
        
        private long SlowDownTimer ;
        private long SlowDownTimeDiff;
        private int SlowDownlength=6000;
        
         
        private long PowerUpTimer ;
        private long PowerUpTimeDiff;
        private int PowerUplength=4000;
        
        private Music music ;
        
        
	
	//---------CONSTRUCTOR-----------------------------------------
	
	public GamePanel(){
		super();
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setFocusable(true);
		requestFocus();
	}
	
	//-----------FUNCTION--------------------------------------------
	
	public void addNotify(){
		super.addNotify();
		if(thread==null){
			thread = new Thread(this);
			thread.start();
		}
		addKeyListener(this);
	}

	
	
	// ---------------- RUN FUNCTION OF GEME ---------**************------
	
	
	
	@Override
	public void run() {
		running=true;
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_BGR);
		g = (Graphics2D) image.getGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

		
		bullets = new ArrayList<Bullet>();
		player = new Player();
		enemies = new ArrayList<Enemy>();
		powerUps = new ArrayList<PowerUp>();
		explosions = new ArrayList<Explosion>();
                texts = new ArrayList<Text>();
                music = new Music();
		
		
//		//----------------NUMBER OF ENEMIES -----******************------
//		for(int i=0;i<20;i++){
//			enemies.add(new Enemy(1, 1));
//			enemies.add(new Enemy(2, 2));
//		}
		
		RoundStartTime=0;
		RoundStartTimeDiff=0;
		RoundNumber = 0;
		RoundStrat= true;
		Start = false;
		
		
		//--------------GAME LOOP-----------------------------------------
		
		
		while(running){
			//startTime = System.nanoTime();
			if(GameOver){

				GameOver();
				gameDraw();

				}
			else if(!Start){

				GameStart();
				gameDraw();

				}
				else{
                 try {
                  gameUpadte();
                  gameRender();
                   } catch (Exception ex) {            
                  }
		
			gameDraw();
			

			
			try {
				thread.sleep(FPS);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		}
		}
	
	
	// ---------------- MAKING UPDATE OF GEME ---------**************------*********
	
	
	
	private void gameUpadte() throws Exception {
		
		
		//--------------PLAYER----*************************--------
		
		
		player.update();
		
		
		//--------------BULLETS----------******************----------
		
		
		for(int i = 0; i< bullets.size();i++){
			boolean remove = bullets.get(i).update();
			if(remove){
				bullets.remove(i);
				i--;
			}
		}
		
		
		
		// -----------------------ENEMYS-------------*******----
		
		
		
		for(int i=0;i<enemies.size();i++){
			enemies.get(i).update();
		}
		
		
		
		//---------------------POWER UP ICONS-------------**************
		
		
		
		for(int i=0;i<powerUps.size();i++){
			boolean remove = powerUps.get(i).update();
			
			if(remove){
				powerUps.remove(i);
				i--;
			}
		}
		
		//---------------EMEMY EXPLORSIONS UPDATE -------------*************
		
		for(int i=0;i<explosions.size();i++){
			boolean remove = explosions.get(i).update();
			
			if(remove){
				explosions.remove(i);
				i--;
			}
		}
		
                //---------------TEXT UPDATE -------------*************
		
		for(int i=0;i<texts.size();i++){
			boolean remove = texts.get(i).update();
			
			if(remove){
				texts.remove(i);
				i--;
			}
		}
		
		//-----------------------KILLING ENEMY HA HA HA :D --------********-----
		
		
		
		for(int i=0;i<bullets.size();i++){
			
			Bullet b = bullets.get(i);
			double bx = b.getx();
			double by = b.gety();
			double br = b.getr();
			
			for(int j=0;j<enemies.size();j++){
				Enemy e = enemies.get(j);
				double ex = e.getx();
				double ey = e.gety();
				double er = e.getr();
				
				double dx = bx-ex;
				double dy = by-ey;
				
				double dist= Math.sqrt(dx*dx + dy*dy);
				if(dist<br+er){
					e.hits();
					bullets.remove(i);
					i--;
					break;
				}
			}
		}
		

		
		
		// ---------------- GET THE POWER BY THE PLAYER :)  ---------**************------
		
		
		int px = player.getx();
		int py = player.gety();
		int pr = player.getr();
		
		for(int i = 0; i<powerUps.size();i++){
			PowerUp p = powerUps.get(i);
			
			double x = p.getx();
			double y = p.gety();
			double r = p.getr();
			
			double dx = px-x;
			double dy = py-y;
			
			double dist = Math.sqrt(dx * dx + dy * dy);
			
			if(dist < pr+r){
				
				int type = p.getType();
				if(type == 1){
					player.LifeUp();
                   texts.add(new Text(player.getx(), player.gety(), 2000, "Extra Life"));
                   texts.remove(texts);
				}
				if(type == 2){
					player.increasingPower(1);
                    texts.add(new Text(player.getx(), player.gety(), 2000, "Power"));
                    texts.remove(texts);
				}
                                if(type == 3){
					player.increasingPower(2);
                    texts.add(new Text(player.getx(), player.gety(), 2000, "Double Power"));
                    texts.remove(texts);
				}
                     if(type == 4){
                         SlowDownTimer = System.nanoTime();
                         for(int j =0;j<enemies.size();j++){
                        	 enemies.get(j).setSlow(true);
                                        }
                   texts.add(new Text(player.getx(), player.gety(), 2000, "Slow"));
                   texts.remove(texts);
				}
                                if(type == 5){
					PowerUpTimer = System.nanoTime();
                          player.setPowerfiring(true);
                          texts.add(new Text(player.getx(), player.gety(), 2000, "Super Power"));
				}
				
				powerUps.remove(i);
				i--;
			}
			
		}
                //-=--------------SLOW DOWN POWER UPDATE------***********************
                    if(SlowDownTimer!=0){
                        SlowDownTimeDiff = (System.nanoTime()-SlowDownTimer)/1000000;
                        
                       if(SlowDownTimeDiff>SlowDownlength){
                            SlowDownTimer =0;
                            for(int j =0;j<enemies.size();j++){
                                            enemies.get(j).setSlow(false);
                                        }
                        }
                       
                    }
		
                //-=--------------PowerUP POWER UPDATE------***********************
                    if(PowerUpTimer!=0){
                        PowerUpTimeDiff = (System.nanoTime()-PowerUpTimer)/1000000;
                        
                        if(PowerUpTimeDiff>PowerUplength){
                            PowerUpTimer =0;
                            player.setPowerfiring(false);
                        }
                    }
		
		
		//---------------------FINAL KILLING ENEMY LOL :D :D --- ********
		
		
		
		for(int i=0;i<enemies.size();i++){
			if(enemies.get(i).isDead()){
				Enemy e = enemies.get(i);
				
				
				
				// --------------------------POWER UP PLAYER.---------------------
				
				
				
				double rand = Math.random();
				
				if(rand < 0.001){
					powerUps.add(new PowerUp(1, e.getx(), e.gety()));
				}
				else if(rand < 0.020){
					powerUps.add(new PowerUp(3, e.getx(), e.gety()));
				}
				else if(rand < 0.120){
					powerUps.add(new PowerUp(2, e.getx(), e.gety()));
				}
                                else if(rand < 0.140){
					powerUps.add(new PowerUp(4, e.getx(), e.gety()));
				}
                                else if(rand < 0.155){
					powerUps.add(new PowerUp(5, e.getx(), e.gety()));
				}
//				
//				else 	{				
//					powerUps.add(new PowerUp(5, e.getx(), e.gety()));
//                                        
//                                }
				
				player.addScore(e.getType()+e.getRank());
				enemies.remove(i);
				i--;
				e.explode();
				explosions.add(new Explosion(e.getx(), e.gety(), e.getr(), e.getr()+30));
			}
		}
		
		
		
		//-----------------------DEAD OF PLAYER---------*******]
		
		
		
		if(!player.isrecovering()){
			
			int Dpx = player.getx();
			int Dpy = player.gety();
			int Dpr = player.getr();
			
			for(int i = 0; i<enemies.size();i++){
				Enemy e = enemies.get(i);
				
				double ex = e.getx();
				double ey = e.gety();
				double er = e.getr();
				
				double dx = Dpx-ex;
				double dy = Dpy-ey;
				
				double dist = Math.sqrt(dx*dx + dy*dy);
				
				if(dist<Dpr+er){
					player.livelost();
                    music.GetKill();
                    texts.add(new Text(player.getx(), player.gety(), 2000, "DEAD!!"));
                    texts.remove(texts);
				}
			}
		}
		
		
		
		//-------------ROUND'S UPDATING :)----**************************************
		
		
		
		if(RoundStartTime == 0 && enemies.size()==0){
			RoundNumber++;
			RoundStrat = false;
			RoundStartTime = System.nanoTime();
		}
		
		else{
			RoundStartTimeDiff = (System.nanoTime() - RoundStartTime)/1000000;
			if(RoundStartTimeDiff>RoundDelay){
				RoundStrat =  true;
				RoundStartTime = 0;
				RoundStartTimeDiff =0;
			}
		}
		
		
		//------------------ ENEMIES HA HA HA -----*********************************----
		
		
		if(RoundStrat && enemies.size() == 0){
			CreateNewEnemies();
		}
		
		if(player.IsDead()){
			GameOver = true;;
		}
		
	}
	
	
	

//-----------------------RENDERING-------***********-----*****8----********----*********-------
	
	
	
	private void gameRender() {
		
		// -------------- MY BACKGROUND ----**********----------*********
		
		if(SlowDownTimer!=0){
			g.setColor(new Color(30,30,30));
		}
		else if(PowerUpTimer!=0){
			g.setColor(new Color(25,25,93));
		}else{
			g.setColor(BackgroundColor);
		}
		
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		g.setColor(new Color(255,255,255));
		g.drawString("SCORE: "+player.getscore(), WIDTH - 120, 30);
		
		
		//-----------------MY PLAYER-----------************---------*** 
		
		player.draw(g);
		for(int i = 0; i< bullets.size();i++){
			bullets.get(i).draw(g);
		}
		
		
		//-----------------MY ENEMIES-----------************---------*** 
		
		
		for(int i=0;i<enemies.size();i++){
			enemies.get(i).draw(g);
		}
		
		
		//------------------MY PLAYER POWER UPS DRAW-------------***************
		
		
		for(int i=0;i<powerUps.size();i++){
			powerUps.get(i).draw(g);
		}
		
		//--------------------ENEMY EXPLOSION DRAWING-----------************
		
		for(int i=0;i<explosions.size();i++){
			explosions.get(i).draw(g);
		}
                
                //--------------------TEXT DRAWING-----------************
		
		for(int i=0;i<texts.size();i++){
			texts.get(i).draw(g);
		}
		
		//--------------DRAWING POWERLEVEL OF MY PLAYER :) -----******************
		
		

		g.setColor(Color.YELLOW);
		g.fillRect(20, 45, player.getPower()*8, 8);
		g.setColor(Color.YELLOW.darker());
		g.setStroke(new BasicStroke(2));
		for(int i = 0; i<player.getNeededPowerLevel();i++){
			g.drawRect(20+8*i, 45, 8, 8);
		}
		g.setStroke(new BasicStroke(1));

		
		
		//-----------------MY ROUNDS NUMBER DRAWING :) -----------************---------***
		
		
		if(RoundStartTime != 0){
			
			g.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
			String s = "- R O U N D "+RoundNumber+" - ";
			//int length = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();
			int alpha = (int)(255*Math.sin(3.14 * RoundStartTimeDiff/RoundDelay));
			if(alpha>255){
				alpha = 255;
			}
			g.setColor(new Color(255,255,255,alpha));
	
			g.drawString(s, WIDTH/2-150, HEIGHT/2);
			//g.drawString(s, WIDTH/2-length/2, HEIGHT/2);
			
		}
		
		// ------------ MY PLAYERS LIVES--------**********-------
		
		
		for(int i =0; i<player.getLives();i++){
			g.setColor(Color.GREEN.brighter());
			g.fillOval(20+(20*i),20, 2*player.getr(), 2*player.getr());
			
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.GREEN.darker());
			g.drawOval(20+(20*i),20, 2*player.getr(), 2*player.getr());
			g.setStroke(new BasicStroke(1));
		}
                
                //---------------DRAW SLOW DOWN POWER -----------------
                if(SlowDownTimer!=0){
                    g.setColor(Color.WHITE);
                    g.drawRect(20, 60, 100, 8);
                    g.fillRect(20, 60, (int)(100-100.0* SlowDownTimeDiff/SlowDownlength), 8);
                }
                //---------------DRAW POWER UP POWER -----------------
                if(PowerUpTimer!=0){
                    g.setColor(new Color(0,191,252));
                    g.drawRect(20, 70, 100, 8);
                    g.fillRect(20, 70, (int)(100-100.0* PowerUpTimeDiff/PowerUplength), 8);
                }
            	
            	// ---------------- DRAWING GAME OVER AT END OF GEME ---------**************------
            		
            		
            		if(Pause){
            		
            		g.setColor( new Color(255,255,255,255));
            		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 45));
            		String s1 = "P A U S E";
            		int length = (int)g.getFontMetrics().getStringBounds(s1, g).getWidth();
            		g.drawString(s1, WIDTH/2-length/2, HEIGHT/2);
            		}
            		else{
            			
                		g.setColor(new Color(255,255,255,0));
                		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 45));
                		String s1 = "G A M E  O V E R";
                		int length = (int)g.getFontMetrics().getStringBounds(s1, g).getWidth();
                		g.drawString(s1, WIDTH/2-length/2, HEIGHT/2);
                		}	
            		
            		
                
            
	}


	
	
	//--------------MY NUMBERS OF ENEMIES.. HA HA HA -----******************
	
	
	
	public void CreateNewEnemies(){
		
		enemies.clear();
		
		Enemy e;
		if(RoundNumber == 1){
			for(int i=0;i<5;i++){
				enemies.add(new Enemy(1, 1));
			}
		}
		if(RoundNumber == 2){
			for(int i=0;i<5;i++){
				enemies.add(new Enemy(1, 1));
			}
				enemies.add(new Enemy(1,2));
                                enemies.add(new Enemy(1,2));
                        
		}
		if(RoundNumber == 3){
			for(int i=0;i<5;i++){
				enemies.add(new Enemy(2, 1));
			}
                        enemies.add(new Enemy(1,4));
                        enemies.add(new Enemy(1,3));
		}
		if(RoundNumber == 4){
			
				enemies.add(new Enemy(1, 3));
				enemies.add(new Enemy(1, 4));
				enemies.add(new Enemy(2, 3));
                                enemies.add(new Enemy(2, 3));
			
		}
		if(RoundNumber == 5){
			for(int i=0;i<5;i++){
				enemies.add(new Enemy(2, 1));
				enemies.add(new Enemy(3, 1));
			}
                        enemies.add(new Enemy(2, 4));
		}
                if(RoundNumber == 6){
			for(int i=0;i<1;i++){
				enemies.add(new Enemy(2, 3));
				enemies.add(new Enemy(3, 3));
				enemies.add(new Enemy(1, 3));
			}
		}
		if(RoundNumber == 7){
			for(int i=0;i<1;i++){
				enemies.add(new Enemy(2, 4));
				enemies.add(new Enemy(3, 4));
				enemies.add(new Enemy(1, 4));
			}
		}
                if(RoundNumber == 8){
			for(int i=0;i<2;i++){
				enemies.add(new Enemy(2, 3));
				enemies.add(new Enemy(3, 3));
				enemies.add(new Enemy(1, 3));
			}
		}
		if(RoundNumber == 9){
			for(int i=0;i<2;i++){
				enemies.add(new Enemy(2, 4));
				enemies.add(new Enemy(3, 4));
				enemies.add(new Enemy(1, 4));
			}
		}
                
		if(RoundNumber == 10){
			GameOver = true;;
		}
		
	}
	
	
	//*****************************************************************************************************************************
	//----------------------------------------------------------- MY GAME START SCREEN----------- ******************------===-=-=-=-----=-=\-=\-\=
	//******************************************************************************************************************************
	
	private void GameStart(){
		
		int Space =100;
		int high = 100;
		
		g.setColor(Color.BLACK.darker());
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 85));
		
		String w1 = "B";
		String w2 = "U";
		String w3 = "B";
		String w4 = "B";
		String w5 = "L";
		String w6 = "E";
		String w7 = "S";
		String w8 = "H";
		String w9 = "O";
		String w10 = "O";
		String w11 = "T";
		
		String C = " : ";
		
		String T1 = "ENTER";
		String T11 = "START GAME";
		
		String T2 = "      S";
		String T21 = "SHOOT";

		String T3 = "LEFT ARROW / NUM 4";
		String T31 = "MOVE LEFT";

		String T4 = "RIGHT ARROW / NUM 6";
		String T41 = "MOVE RIGHT";

		String T5 = "UP ARROW / NUM 8";
		String T51 = "MOVE UP";

		String T6 = "DOWN ARROW / NUM 5";
		String T61 = "MOVE DOWN";

		String T7 = "SPACE ";
		String T71 = "PAUSE GAME";

		
		//---------------------------------------PRINTING BUBBLE TEXT ----------------********************---

		
		g.setColor(new Color(255,246,0).brighter());
		g.drawString(w1, WIDTH/5,high);
		
		

		g.setColor(Color.GREEN.brighter());
		g.drawString(w2, WIDTH/5 +60,high);
		
		

		g.setColor(new Color(255,1,0).brighter());
		g.drawString(w3, WIDTH/5 + 120, high);
		
		

		g.setColor(new Color(255,246,0).brighter());
		g.drawString(w4, WIDTH/5 +180, high);
		
		

		g.setColor(Color.PINK.brighter());
		g.drawString(w5, WIDTH/5 + 240, high);
		
		

		g.setColor(Color.GREEN.brighter());
		g.drawString(w6, WIDTH/5 + 300, high);
		
		//------------------------------PRINTING SHOOT TEXT----------************
		
		
		g.setColor(new Color(255,0,0).brighter());
		g.drawString(w7, WIDTH/5+20, high+80);
		
		
		g.setColor(new Color(150,255,49).brighter());
		g.drawString(w8, WIDTH/5+20+60, high+80);
		
		
		g.setColor(new Color(255,0,200).brighter());
		g.drawString(w9, WIDTH/5+20+120, high+80);
		
		
		g.setColor(new Color(99,255,50).brighter());
		g.drawString(w10, WIDTH/5+20+180, high+80);
		
		
		g.setColor(new Color(0,191,252).brighter());
		g.drawString(w11, WIDTH/5+20+240, high+80);
		
		//-------------------------------------PRINTING INSTRUCTIONS --------------***********
		
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
		
		g.setColor(Color.ORANGE.brighter());
		g.drawString(T1, WIDTH/4, high+180);
		
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		
		g.setColor(new Color(0,191,252).brighter());
		g.drawString(T2, WIDTH/3+50, high+225);
		g.drawString(T3, WIDTH/6-20, high+250);
		g.drawString(T4, WIDTH/6-38, high+275);
		g.drawString(T5, WIDTH/6+3, high+300);
		g.drawString(T6, WIDTH/6-38, high+325);
		g.drawString(T7, WIDTH/3+38, high+350);
		
		//--------------------PRINTING COLORNS ( : )-------------------------
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));


		g.setColor(Color.WHITE.brighter());
		g.drawString(C, WIDTH/2-50, high+180);
		
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		g.setColor(Color.WHITE.brighter());
		g.drawString(C, WIDTH/2, high+225);
		g.drawString(C, WIDTH/2, high+250);
		g.drawString(C, WIDTH/2, high+275);
		g.drawString(C, WIDTH/2, high+300);
		g.drawString(C, WIDTH/2, high+325);
		g.drawString(C, WIDTH/2, high+350);
		
		//--------------------PRINTING INSTRUCTION LIST ( : )-------------------------
				g.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
				g.setColor(Color.GREEN.brighter());
				g.drawString(T11, WIDTH/2-30, high+180);
				
				g.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));

				g.setColor(Color.GREEN.brighter());
				g.drawString(T21, WIDTH/2+20, high+225);
				g.drawString(T31, WIDTH/2+20, high+250);
				g.drawString(T41, WIDTH/2+20, high+275);
				g.drawString(T51, WIDTH/2+20, high+300);
				g.drawString(T61, WIDTH/2+20, high+325);
				g.drawString(T71, WIDTH/2+20, high+350);
				
		//****************************BUBBLES*******************
				Color color1= new Color(138,43,226,90);
				Color color2 = new Color(0,0,255,90);
				Color color3 = new Color(255,0,0,90);
				g.setColor(color1);
				g.fillOval( 110, 100, 50, 50);
				g.setStroke(new BasicStroke(3));
				g.setColor(color1.darker());
				g.drawOval(110, 100, 50, 50);
				g.setStroke(new BasicStroke(1));
				
				g.setColor(color2);
				g.fillOval( 50, 180, 50, 50);
				g.setStroke(new BasicStroke(3));
				g.setColor(color2.darker());
				g.drawOval(50, 180, 50, 50);
				g.setStroke(new BasicStroke(1));
				
				g.setColor(color3);
				g.fillOval( 450, 80, 50, 50);
				g.setStroke(new BasicStroke(3));
				g.setColor(color3.darker());
				g.drawOval(450, 80, 50, 50);
				g.setStroke(new BasicStroke(1));
				
				g.setColor(color2);
				g.fillOval( 300, 0, 50, 50);
				g.setStroke(new BasicStroke(3));
				g.setColor(color2.darker());
				g.drawOval(300, 0, 50, 50);
				g.setStroke(new BasicStroke(1));
				
				g.setColor(color2);
				g.fillOval( 500, 180, 50, 50);
				g.setStroke(new BasicStroke(3));
				g.setColor(color2.darker());
				g.drawOval(500, 180, 50, 50);
				g.setStroke(new BasicStroke(1));
				
				g.setColor(color3);
				g.fillOval( 110, 0, 50, 50);
				g.setStroke(new BasicStroke(3));
				g.setColor(color3.darker());
				g.drawOval(110, 0, 50, 50);
				g.setStroke(new BasicStroke(1));
				
		
	}
	
	
	
	
	//*****************************************************************************************************************************
	//----------------------------------------------------------- MY GAME OVER SCREEN----------- ******************------===-=-=-=-----=-=\-=\-\=
	//******************************************************************************************************************************
	private void GameOver(){
		
	// ---------------- DRAWING GAME OVER AT END OF GEME ---------**************------
		
		
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 45));
		String s1 = "G A M E  O V E R";
		int length = (int)g.getFontMetrics().getStringBounds(s1, g).getWidth();
		g.drawString(s1, WIDTH/2-length/2, HEIGHT/2);

		
		
		// ---------------- DRAWING LARGE PLAYER AT END OF GEME ---------**************------
		
		
		
		g.setColor(Color.RED.brighter());
		g.fillOval(250, 100, 100, 100);
		g.setStroke(new BasicStroke(15));
		g.setColor(Color.RED.darker());
		g.drawOval(250, 100, 100, 100);
		g.setStroke(new BasicStroke(20));

		
		
		// ---------------- DRAWING FINAL SCORE AT END OF GEME ---------**************------
		
		
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 22));
		String s2 = "FINAL SCORES: "+player.getscore();
		int length21 = (int)g.getFontMetrics().getStringBounds(s1, g).getWidth();
		g.drawString(s2, WIDTH/2-length21/2, HEIGHT/2+50);
		
	
		
		// ---------------- DRAWING FINAL SCORE AT END OF GEME ---------**************------
		
		
		
		g.setColor(Color.GREEN.brighter());
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
		String s3 = "PRESS ENTER TO RESTART..";
		int length22 = (int)g.getFontMetrics().getStringBounds(s1, g).getWidth();
		g.drawString(s3, WIDTH/2-length22/2-80, HEIGHT/2+100);
		
	
	
	}
	
	
	
	
	//-------------------- MY GAME DRAW ******************------===-=-=-=-----=-=\-=\-\=
	
	
	
	private void gameDraw() {
		Graphics g2=this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		
	}

	
	//----------------------- MY GAME KEY-----------******************---***********
	
	
	@Override
	public void keyPressed(KeyEvent key) {
		int keyCode = key.getKeyCode();
		if(keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_NUMPAD4){
			player.setLeft(true);
		}
		if(keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_NUMPAD6){
			player.setRight(true);
		}
		if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_NUMPAD8){
			player.setUp(true);
		}
		if(keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_NUMPAD5){
			player.setDown(true);
		}
		if(keyCode == KeyEvent.VK_S){
			player.setfiring(true);
		}
		if(keyCode == KeyEvent.VK_SPACE){
			Pause = true;
			player.setPause(true);
			for(int i = 0; i<enemies.size();i++){
				enemies.get(i).setPause(true);
			}
			for(int i = 0; i<bullets.size();i++){
				bullets.get(i).setPause(true);
			}
			for(int i = 0; i<powerUps.size();i++){
				powerUps.get(i).setPause(true);
			}
			for(int i = 0; i<explosions.size();i++){
				explosions.get(i).setPause(true);
			}
	} 
	if(keyCode == KeyEvent.VK_ENTER){
		Pause = false;
		player.setPause(false);
		for(int i = 0; i<enemies.size();i++){
			enemies.get(i).setPause(false);
		}
		for(int i = 0; i<bullets.size();i++){
			bullets.get(i).setPause(false);
		}
		for(int i = 0; i<powerUps.size();i++){
			powerUps.get(i).setPause(false);
		}
		for(int i = 0; i<explosions.size();i++){
			explosions.get(i).setPause(false);
		}
		Start = true;
		if((GameOver = true && RoundNumber>10)||(GameOver = true && player.IsDead()) ){
			GameOver = false;
			RoundStartTime=0;
			RoundStartTimeDiff=0;
			RoundNumber = 0;
			SlowDownTimer =0;
			PowerUpTimer=0;
			player.setPlayer();
			player.setRecoveringtimer(0);
			player.setPowerLevel(0);
			player.setPower(0);
			player.setLives(3);
			player.setScore(0);
			for(int i=0;i<enemies.size();i++){
				Enemy e = enemies.get(i);
				enemies.remove(i);
			}
			for(int i=0;i<enemies.size();i++){
				Enemy e = enemies.get(i);
				enemies.remove(i);
			}
			for(int i=0;i<enemies.size();i++){
				Enemy e = enemies.get(i);
				enemies.remove(i);
			}
			for(int i=0;i<enemies.size();i++){
				Enemy e = enemies.get(i);
				enemies.remove(i);
			}
			for(int i=0;i<enemies.size();i++){
				Enemy e = enemies.get(i);
				enemies.remove(i);
			}
			for(int i=0;i<enemies.size();i++){
				Enemy e = enemies.get(i);
				enemies.remove(i);
			}
			for(int i=0;i<enemies.size();i++){
				Enemy e = enemies.get(i);
				enemies.remove(i);
			}
			for(int i=0;i<enemies.size();i++){
				Enemy e = enemies.get(i);
				enemies.remove(i);
			}
			for(int i=0;i<enemies.size();i++){
				Enemy e = enemies.get(i);
				enemies.remove(i);
			}
			for(int i=0;i<enemies.size();i++){
				Enemy e = enemies.get(i);
				enemies.remove(i);
			}
			for(int i=0;i<enemies.size();i++){
				Enemy e = enemies.get(i);
				enemies.remove(i);
			}
			for(int i=0;i<enemies.size();i++){
				Enemy e = enemies.get(i);
				enemies.remove(i);
			}
			for(int i=0;i<bullets.size();i++){
				Bullet b = bullets.get(i);
				bullets.remove(i);
			}
			for(int i=0;i<bullets.size();i++){
				Bullet b = bullets.get(i);
				bullets.remove(i);
			}
			for(int i=0;i<texts.size();i++){
				Text t = texts.get(i);
				texts.remove(i);
			}
			for(int i=0;i<texts.size();i++){
				Text t = texts.get(i);
				texts.remove(i);
			}
			for(int i=0;i<powerUps.size();i++){
				powerUps.remove(i);
			}
		
		}
	
		}
	
	}

	@Override
	public void keyReleased(KeyEvent key) {
		int keyCode = key.getKeyCode();
		if(keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_NUMPAD4){
			player.setLeft(false);
		}
		if(keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_NUMPAD6){
			player.setRight(false);
		}
		if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_NUMPAD8){
			player.setUp(false);
		}
		if(keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_NUMPAD5){
			player.setDown(false);
		}
		if(keyCode == KeyEvent.VK_S){
			player.setfiring(false);
		}
		
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
