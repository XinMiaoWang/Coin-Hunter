package tile;

import java.awt.Graphics;
import java.awt.Rectangle;

import game.Handler;
import game.Id;

public abstract class Tile {

	public int x,y;
	public int width,height;
	
	public boolean solid = false;
	public boolean activated = false;
	
	public int velX,velY;
	public int facing;
	
	public Id id;
	public Handler handler;

	public Tile(int x,int y,int width,int height,boolean solid,Id id,Handler handler){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.solid = solid;
		this.id = id;
		this.handler = handler;

	}
	public abstract void render(Graphics g);
	public abstract void tick();
	
	public void die(){
		handler.removeTile(this);
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public Id getId(){
		return id;
	}
	
	public int getVelX() {
		return velX;
	}
	
	public void setVelX(int velX) {
		this.velX = velX;
	}
	
	public int getVelY() {
		return velY;
	}
	
	public void setVelY(int velY) {
		this.velY = velY;
	}
	
	public Rectangle getBounds(){
		return new Rectangle(getX(),getY(),width,height);
	}
}
