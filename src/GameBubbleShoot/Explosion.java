package GameBubbleShoot;

import java.awt.*; 

public class Explosion {
	
	private double x;
	private double y;
	private int r;
	private int MaxRadeus;

	private boolean Pause;
        
        Music ME = new Music();
	
	
	public Explosion(double x, double y, int r, int Max) throws Exception{
		this.x = x;
		this.y = y;
		this.r = r;
		MaxRadeus = Max;
                ME.ExplosionEnemy();
	}
	public void setPause(boolean b) {Pause = b; }

	public boolean update(){
		if(Pause){
			y+=0;	
		}
		else{
		r +=2;
		}
		if(r>=MaxRadeus){
			return true;
		}
		return false;
	}
	public void draw(Graphics2D g){
		g.setColor(new Color(255,255,255,128));
		g.setStroke(new BasicStroke(3));
		g.drawOval((int)(x-r), (int)(y-r), 2*r, 2*r);
		g.setStroke(new BasicStroke(1));

	}

}
