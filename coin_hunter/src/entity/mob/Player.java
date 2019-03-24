package entity.mob;

import java.awt.Graphics;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


import entity.Entity.Entity;
import states.PlayerState;
import tile.Tile;
import game.Game;
import game.Handler;
import game.Id;


public class Player extends Entity {

	private PlayerState state;
	
	private int pixelsTravelled = 0;
	
	private int frame = 0;
	private int frameDelay = 0;
	
	private boolean animate = false;
	
	public Player(int x, int y, int width, int height/*, boolean solid*/, Id id, Handler handler) {
		super(x, y, width, height/*, solid*/, id, handler);
		
		state = PlayerState.SMALL; // Player初始狀態
	}

	// draw player
	public void render(Graphics g) {
		if(state==PlayerState.FIRE) {
			if(facing==0){
				g.drawImage(Game.fireplayer[frame+4].getBufferedImage(), x, y, width,height,null);
			} else if(facing==1){
				g.drawImage(Game.fireplayer[frame].getBufferedImage(), x, y, width,height,null);
			}
		}else {
			//角色行走圖
			if(facing==0){
				g.drawImage(Game.player[frame+4].getBufferedImage(), x, y, width,height,null);
			} else if(facing==1){
				g.drawImage(Game.player[frame].getBufferedImage(), x, y, width,height,null);
			}
		}
		
	}
	
	// updata screen
	public void tick() {
		x+=velX;
		y+=velY; 
		if(x<=0) x = 0;//左邊邊界
		
		if(velX!=0) animate = true;
		else animate =false;
		
		for(int i = 0; i < handler.tile.size(); i++){
            Tile t = handler.tile.get(i);
            
			if(t.isSolid()&&!goingDownPipe) {
				if(getBoundsTop().intersects(t.getBounds())&&t.getId()!=Id.coin){
					setVelY(0);
					if(jumping&&!goingDownPipe){			
						jumping = false;
						gravity =0.8;	//gravity
						falling = true;
					}
					if(t.getId()==Id.powerUp){
						if(getBoundsTop().intersects(t.getBounds())) t.activated = true;
					}
				}
				if(getBoundsBottom().intersects(t.getBounds())&&t.getId()!=Id.coin){
					setVelY(0);
					if(falling) falling = false; //gravity
				} else{
					if(!falling&&!jumping){
						gravity = 0.5;
						falling =true;
					}
				}
				if(getBoundsLeft().intersects(t.getBounds())&&t.getId()!=Id.coin){
					setVelX(0);
					x = t.getX()+t.width;
				}
				if(getBoundsRight().intersects(t.getBounds())&&t.getId()!=Id.coin){
					setVelX(0);
					x = t.getX()-t.width;
				}
				
				//吃到金幣
				if(getBounds().intersects(t.getBounds())&&t.getId()==Id.coin){
					Game.coins++; // add coin
					t.die(); // remove coin
					Game.getCoin.play(); // music
					
					// 過關條件
					if( (Game.coins == Game.targets) /*&& Game.treasures*/)
						Game.switchLevel();
				}
				
				//碰到寶箱
				if(getBounds().intersects(t.getBounds())&&t.getId()==Id.treasure){
					t.die();
					Game.treasures = true;
					//if(Game.treasures) Game.switchLevel();
				}
			
			}
		}
		
		for(int i=0;i<handler.entity.size();i++){
			Entity e = handler.entity.get(i);
			
			//吃到香菇player變大
			if(e.getId()==Id.mushroom){
				if(getBounds().intersects(e.getBounds())){
					if(state == PlayerState.SMALL) {
						int tpX = getX();
						int tpY = getY();
						
						width*=2; // 變大2倍
						height*=2;
						setX(tpX-width);
						setY(tpY-height);
						
						state = PlayerState.BIG;
						Game.powerup.play();
					}
					e.die(); // 香菇消失
				}
			} else if(e.getId()==Id.goomba){
				//從上面採怪物怪物會死掉
				if(getBoundsBottom().intersects(e.getBoundsTop())){
					e.die();
					Game.enemyDie.play();//音效
				} else if(getBounds().intersects(e.getBounds())){
					//被怪物咬到 大--->小--->死亡
					takeDamage();
					/*if(state == PlayerState.BIG){
						state = PlayerState.SMALL;
						width/=2;
						height/=2;
						x-=width;
						y-=height;
					}
					else if(state == PlayerState.SMALL){
						die();
						Game.die.play();//音效
						//延遲2秒
						try {
							TimeUnit.SECONDS.sleep(2);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						
					}*/
				}
			}else if(e.getId()==Id.flower) { // 吃到花
				if(getBounds().intersects(e.getBounds())) {
					if(state==PlayerState.SMALL) {
						int tpX = getX();
						int tpY = getY();
						width*=2;
						height*=2;
						setX(tpX-width);
						setY(tpY-height);
					}
					state = PlayerState.FIRE;
					e.die();
				}
			}
			
		}
		
		if(jumping&&!goingDownPipe){
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
		
		//行走圖
		if(animate){
			frameDelay++;
			if(frameDelay>=3){
				frame++;
				if(frame>=4){
					frame = 0;
				}
				frameDelay = 0;
			}
		}
		
		//進水管
		if(goingDownPipe){
			for(int i=0;i<Game.handler.tile.size();i++){
				Tile t = Game.handler.tile.get(i);
				
				if(t.getId()==Id.pipe){
					if(getBounds().intersects(t.getBounds())){
						switch(t.facing){
							case 0:
								setVelY(-5);
								setVelX(0);
								pixelsTravelled+=-velY;
								Game.pipe.play();
								break;
							case 2:
								setVelY(5);
								setVelX(0);
								pixelsTravelled+=velY;
								Game.pipe.play();
								break;
						}
						
						if(pixelsTravelled>t.height){
							goingDownPipe = false;
							pixelsTravelled = 0;
						}
					}
					
				}
			}
		}
	}
	
	// Player狀態轉換
	public void takeDamage() {
		if(state==PlayerState.SMALL) {
			die();
			
			Game.die.play();//音效
			//延遲2秒
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			return;
		}else if(state==PlayerState.BIG) {
			width/=2;
			height/=2;
			x-=width;
			y-=height;
			
			state = PlayerState.SMALL;
			return;
		}else if(state==PlayerState.FIRE) {
			state = PlayerState.BIG;
			return;
		}
	}
	
}
