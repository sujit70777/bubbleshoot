package GameBubbleShoot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.text.StyledEditorKit.BoldAction;

public class PowerUp {
	
	private double x;
	private double y;
	private int type;
	private int r;
	
	private Color color1;

	private boolean Pause;
	
	
	public PowerUp(int type, double x, double y ){
		this.type = type;
		this.x= x;
		this.y = y;
		
		if(type == 1){
			color1 = Color.PINK;
			r = 3;
			
		}
		if(type == 2){
			color1 = Color.YELLOW;
			r = 3;
			
		}
		if(type == 3){
			color1 = Color.YELLOW;
			r = 5;
			
		}
                if(type == 4){
                    color1 = Color.WHITE;
                    r = 3;
                }
                if(type == 5){
                    color1 = new Color(0,191,252);
                    r = 5;
                }
	}
	
	public double getx(){ return x;}
	public double gety(){ return y;}
	public int getr(){ return r;}
	public int getType(){ return type;}
	public void setPause(boolean b) {Pause = b; }

	public boolean update(){
		if(Pause){
			y=y;
		}
		else{
			y+=2;
		}
		
		if(y>GamePanel.HEIGHT +r){
			return true;
		}
		return false;
	}
	
	public void draw(Graphics2D g){
		g.setColor(color1);
		g.fillRect((int)(x-r), (int)(y-r), 2*r, 2*r);
		
		g.setStroke(new BasicStroke(3));
		g.setColor(color1.darker());
		g.drawRect((int)(x-r), (int)(y-r), 2*r, 2*r);
		g.setStroke(new BasicStroke(3));

	}

}
