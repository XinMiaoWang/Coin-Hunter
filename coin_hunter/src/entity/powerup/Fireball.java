package entity.powerup;

import java.awt.Graphics;

import entity.Entity.Entity;
import game.Game;
import game.Handler;
import game.Id;
import tile.Tile;

public class Fireball extends Entity{

	public Fireball(int x, int y, int width, int height, Id id, Handler handler) {
		super(x, y, width, height, id, handler);
		
		switch(facing){
			case 0:
				setVelX(-2);
				break;
			case 1:
				setVelX(2);
				break;
		}
	}
	
	// draw fireball
	public void render(Graphics g) {
		g.drawImage(Game.fireball.getBufferedImage(),x,y,width,height,null);
	}
	
	// update screen
	public void tick() {
		x+=velX;
		y+=velY;
		
		for(int i=0;i<handler.tile.size();i++){
			Tile t = handler.tile.get(i);
			
			if(t.isSolid()){
				
				// fireball左邊OR右邊撞到物體會消失
				if(getBoundsLeft().intersects(t.getBounds()) || getBoundsRight().intersects(t.getBounds()))
					die();
				
				// 彈跳 & 下墜
				if(getBoundsBottom().intersects(t.getBounds())){
					jumping = true;
					falling = false;
					gravity = 0.4;
				} else if(!falling && !jumping){
					falling = true;
					gravity = 0.4;
				}
				
			}
			
		}
		
		for(int i=0;i<handler.entity.size();i++) {
			Entity e = handler.entity.get(i);
			
			// fireball打到小怪，小怪會死，fireball會消失
			if(e.getId()==Id.goomba) {
				if(getBounds().intersects(e.getBounds())) {
					die();
					e.die();
				}
			}
		}
		
		if(jumping){
			gravity-=0.1;
			setVelY((int)-gravity);
			if(gravity<=0.0){
				jumping = false;
				falling = true;
			}
		}
		
		if(falling&&!goingDownPipe){
			gravity+=0.1;
			setVelY((int)gravity);
		}
		
	}

}
