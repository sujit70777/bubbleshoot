package GameBubbleShoot;

import java.awt.*;
public class Bullet {
	
	private double x;
	private double y;
	private int r;
	
	private double rad;
	private double dx;
	private double dy;
	private double speed;
	private Color color1;
	
	private boolean Pause;
	
	
	public Bullet(double angle, int x, int y, Color color3){
		this.x = x;
		this.y = y;
		r=2;
		rad = Math.toRadians(angle+180);
		speed =10;

		color1 = color3.brighter();

		dx = Math.cos(rad) * speed;
		dy = Math.sin(rad) * speed;
		
	}
	public double getx(){ return x;}
	public double gety(){ return y;}
	public double getr(){ return r;}
	public void setPause(boolean b) {Pause = b; }
	public boolean update(){
		if(Pause){
			x += dx*0;
			y += dy*0;
		}
		else{
		x += dx;
		y += dy;
		}
		if(x<-r || x>GamePanel.WIDTH+r || y<-r || y> GamePanel.HEIGHT+r){
			return true;
		}
		return false;
		
	}
	
	public void draw (Graphics2D g){
		g.setColor(color1.brighter());
		g.fillOval((int)x-r,(int)y-r, 2*r, 2*r);
	}

}
