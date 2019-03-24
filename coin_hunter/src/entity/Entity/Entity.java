package entity.Entity;

import java.awt.Graphics;
import java.awt.Rectangle;

import game.Game;
import game.Handler;
import game.Id;
import states.PlayerState;


public abstract class Entity {
	
	public int x,y;
	public int width,height;
	public int facing = 0; //0:left,1:right
	
	//public boolean solid = false;
	public boolean jumping = false;
	public boolean falling = true;
	public boolean goingDownPipe = false;
	
	public int velX,velY;
	public Id id;
	
	public double gravity =0.0;	//gravity
	
	public Handler handler;
	
	
	public Entity(int x,int y,int width,int height/*,boolean solid*/,Id id,Handler handler){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		//this.solid = solid;
		this.id = id;
		this.handler = handler;
	}
	public abstract void render(Graphics g);
	public abstract void tick();
	
	//玩家死掉會減命，命<=0就gameover
	public void die(){
		handler.removeEntity(this);
		if(getId()==Id.player){
			Game.lives--;
			Game.showDeathScreen = true;
	
			if(Game.lives<=0) Game.gameOver = true;
		}
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
	
	public Id getId(){
		return id;
	}
	
	/*public boolean isSolid() {
		return solid;
	}*/
	
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
	
	//磚塊固體範圍
	public Rectangle getBounds(){
		return new Rectangle(getX(),getY(),width,height);
	}
	public Rectangle getBoundsTop(){
		return new Rectangle(getX()+10,getY(),width-20,5);
	}
	public Rectangle getBoundsBottom(){
		return new Rectangle(getX()+10,getY()+height-5,width-20,5);
	}
	public Rectangle getBoundsLeft(){
		return new Rectangle(getX(),getY()+10,5,height-20);
	}
	public Rectangle getBoundsRight(){
		return new Rectangle(getX()+width-5,getY()+10,5,height-20);
	}
	
}
