package GameBubbleShoot;

import java.awt.*;
public class Player {
	
	// ----------------------MY PRIVATE VARIABLES---------------
	
	private int x;
	private int y;
	private int r;
	private int dx;
	private int dy;
	private int speed;
	private int lives = 3;
	
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	
	private Color color1;
	private Color color2;
	
	private boolean firing;
	private boolean Powerfiring;

	private long firingTimer;
	private long firingDelay;
	
	private boolean recover;
	private long recoverTime;
	
	private int score;
	private Color BulletColor;
	private Color BulletColor2;
	private Color BulletColor3;

	
	private int powerLevel=0;
	private int power;
	private int[] NeededPowerLevel = { 1,2,3,4,5,6,7,8};
	
	private boolean Pause;
        
        Music B = new Music();
	
	//------------------PLAYER CONSTRUCTTOR-------------------******
	
	
	public Player(){
		x = GamePanel.WIDTH/2;
		y = GamePanel.HEIGHT/2;
		
		dx=0;
 		dy=0;
		speed = 5;
		r=7;
	
		color1 = Color.GREEN;
		color2 = Color.RED;
		firing = false;
		Powerfiring = false;
		firingTimer = System.nanoTime();
		firingDelay = 200;
		
		recover = false;
		recoverTime = 0;
		
		score = 0;
		
		
		
	}
	
	//----------------GETTING VALUES OF PLAYER----------------*******
	
	public int getx(){ return x;}
	public int gety(){ return y;}
	public int getr(){ return r;}
	public int getLives(){return lives;}
	public int getPower(){ return power;}
	public int getPowerLevel(){ return powerLevel;}
	public int getNeededPowerLevel(){ return NeededPowerLevel[powerLevel];}
	
	public void LifeUp(){
		lives++;
	}
	
	public void increasingPower(int i){
		power +=i;
		if(powerLevel==7){
                    if(power>NeededPowerLevel[powerLevel])
                            power=NeededPowerLevel[powerLevel];
                    return;
                }
		if(power >= NeededPowerLevel[powerLevel]){
			power -= NeededPowerLevel[powerLevel];
			powerLevel++;
		}
	}
	
	
	//-----RECOVERING PLAYER-------------*********
	
	public boolean isrecovering(){
		return recover;
	}
	
	public boolean IsDead(){
		return lives<=0;
	}
	
	// ------------ DEAD OF PLAYER-------------*******
	
	public void livelost(){
		lives--;
		recover = true;
		recoverTime = System.nanoTime();
	}
	public int getscore(){
		return score;
	}
	
	public void addScore(int i){
		score +=i;
	}
	
	public void update () throws Exception{
		
		if(left){
			dx = -speed;
		}
		if(right){
			dx= speed;
		}
		if(up){
			dy = -speed;
		}
		if(down){
			dy = speed;
		}
		
		if(Pause){
			x += dx*0;
			y += dy*0;
		}
		else{
		x += dx;
		y += dy;
		}
		if (x<r){ 
			x=r;
		}
		if(y<r){
			y=r;
		}
		if(x>GamePanel.WIDTH-r){
			x = GamePanel.WIDTH -r;
		}
		if(y>GamePanel.HEIGHT-r){
			y = GamePanel.HEIGHT-r;
		}
		dy=0;
		dx=0;
		// ---------------BULLETS FIRING OF PLAYERS-----------*******
		if (firing && !Pause){
			long elasped = (System.nanoTime()-firingTimer)/1000000;
                        if(Powerfiring){
                            if(elasped > firingDelay){
				BulletColor = new Color(0,191,252);
				B.SingleBullet();
				GamePanel.bullets.add(new Bullet(30, x, y,BulletColor));
				GamePanel.bullets.add(new Bullet(60, x, y,BulletColor));
				GamePanel.bullets.add(new Bullet(90, x, y,BulletColor));
				GamePanel.bullets.add(new Bullet(120, x, y,BulletColor));
				GamePanel.bullets.add(new Bullet(150, x, y,BulletColor));
				GamePanel.bullets.add(new Bullet(180, x, y,BulletColor));
				GamePanel.bullets.add(new Bullet(210, x, y,BulletColor));
				GamePanel.bullets.add(new Bullet(240, x, y,BulletColor));
				GamePanel.bullets.add(new Bullet(270, x, y,BulletColor));
				GamePanel.bullets.add(new Bullet(300, x, y,BulletColor));
				GamePanel.bullets.add(new Bullet(330, x, y,BulletColor));
				GamePanel.bullets.add(new Bullet(360, x, y,BulletColor));

				firingTimer = System.nanoTime();
			}
                        }
                        else{
			if(elasped > firingDelay){
				//GamePanel.bullets.add(new Bullet(270, x, y));
				B.SingleBullet();
				firingTimer = System.nanoTime();
				
				if(powerLevel<3){
				//	BulletColor = new Color(255,246,0);
					BulletColor = Color.YELLOW;
					GamePanel.bullets.add(new Bullet(90, x, y, BulletColor) );

				}
				else if(powerLevel<5){
					BulletColor = new Color(0,191,252);
					GamePanel.bullets.add(new Bullet(90, x+5, y,BulletColor));
					GamePanel.bullets.add(new Bullet(90, x-5, y,BulletColor));


				}
				else if(powerLevel<7){
					BulletColor2 = new Color(255,1,0);
					BulletColor = new Color(255,246,0);
					GamePanel.bullets.add(new Bullet(90, x, y,BulletColor2));
					GamePanel.bullets.add(new Bullet(95, x+5, y,BulletColor));
					GamePanel.bullets.add(new Bullet(85, x-5, y,BulletColor));

				}
				else {
					BulletColor2 = new Color(255,1,0);
					BulletColor = new Color(255,246,0);
					BulletColor3 = new Color(0,191,252);

					GamePanel.bullets.add(new Bullet(90, x, y,BulletColor2));
					GamePanel.bullets.add(new Bullet(95, x+5, y,BulletColor));
					GamePanel.bullets.add(new Bullet(85, x-5, y,BulletColor));
					GamePanel.bullets.add(new Bullet(102, x+5, y,BulletColor3));
					GamePanel.bullets.add(new Bullet(78, x-5, y,BulletColor3));

				}
			}
                        }
			
			//firingTimer = System.nanoTime();
		}
		
		// ----------------- POWER BULLET FIRING----------*************
		
		if(recover){
		long elapsed = (System.nanoTime()-recoverTime)/1000000;
		if(elapsed>2000){
			recover= false;
			recoverTime = 0;
		}
		}
	}
	
	public void setLeft(boolean b){ 
		left = b;
		}
	public void setRight(boolean b){
		right = b;
		}
	public void setUp(boolean b){ 
		up = b;
		}
	public void setDown(boolean b){
		down = b;
		}
	public void setfiring(boolean b) {
		firing = b;
	}
	public void setPowerfiring(boolean b) {
		Powerfiring = b;
	}
	public void setPause(boolean b) {
		Pause = b;
	}
	public void setPowerLevel(int b) {
		powerLevel = b;
	}
	public void setPower(int b) {
		power = b;
	}
	public void setLives(int b) {
		lives = b;
	}
	public void setScore(int b) {
		score = b;
	}
	public void setRecoveringtimer(int b) {
		recoverTime = b;
	}
	public void setPlayer() {
		x = GamePanel.WIDTH/2;
		y = GamePanel.HEIGHT/2;
	}


	
	
	public void draw ( Graphics2D g){
		if(recover){
			g.setColor(color2.brighter());
			g.fillOval(x-r, y-r, 2*r, 2*r);
			g.setStroke(new BasicStroke(3));
			g.setColor(color2.darker());
			g.drawOval(x-r, y-r, 2*r, 2*r);
			g.setStroke(new BasicStroke(1));
		}
		else{
		g.setColor(color1.brighter());
		g.fillOval(x-r, y-r, 2*r, 2*r);
		g.setStroke(new BasicStroke(3));
		g.setColor(color1.darker());
		g.drawOval(x-r, y-r, 2*r, 2*r);
		g.setStroke(new BasicStroke(1));
		}
	}
		

	
	

}
