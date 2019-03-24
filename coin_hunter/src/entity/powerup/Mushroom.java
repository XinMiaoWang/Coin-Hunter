package entity.powerup;

import java.awt.Graphics;
import java.util.Random;

import entity.Entity.Entity;
import tile.Tile;
import game.Game;
import game.Handler;
import game.Id;


public class Mushroom extends Entity {
	
	private Random random = new Random();
	
	public Mushroom(int x,int y,int width,int height/*,boolean solid*/,Id id,Handler handler){
		super(x,y,width,height/*,solid*/,id,handler);
	
		int dir = random.nextInt(2); // 隨機產生數字 0 或 1
		
		// 0 :往左邊，1:往右邊
		switch(dir){
			case 0:
				setVelX(-2); // 移動速度 & 方向
				break;
			case 1:
				setVelX(2);
				break;
		}
	}
	
	// draw mushroom
	public void render(Graphics g){
		g.drawImage(Game.mushroom.getBufferedImage(),x,y,width,height,null);
	}
	
	// update screen
	public void tick(){
		x+=velX;
		y+=velY;
		
		for(int i=0;i<handler.tile.size();i++){
			Tile t = handler.tile.get(i);
			
			if(t.isSolid()){
				// 香菇底部接觸到地板
				if(getBoundsBottom().intersects(t.getBounds())){
					setVelY(0);
					if(falling) falling = false; // 禁止往下掉
				} else if(!falling){
					falling = true; // 可往下掉
					gravity = 0.4; // 往下掉的起始速度
				}
				
				// 香菇撞到牆壁後的移動方向&速度
				if(getBoundsLeft().intersects(t.getBounds())){
					setVelX(2);
				}
				
				if(getBoundsRight().intersects(t.getBounds())){
					setVelX(-2);
				}

			}
		}
		
		// 往下掉
		if(falling){
			gravity+=0.1;
			setVelY((int)gravity);
		}
		
	}
	 
}
