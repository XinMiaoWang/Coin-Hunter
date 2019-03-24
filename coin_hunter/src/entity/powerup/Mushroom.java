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
	
		int dir = random.nextInt(2); // �H�����ͼƦr 0 �� 1
		
		// 0 :������A1:���k��
		switch(dir){
			case 0:
				setVelX(-2); // ���ʳt�� & ��V
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
				// ��ۣ������Ĳ��a�O
				if(getBoundsBottom().intersects(t.getBounds())){
					setVelY(0);
					if(falling) falling = false; // �T��U��
				} else if(!falling){
					falling = true; // �i���U��
					gravity = 0.4; // ���U�����_�l�t��
				}
				
				// ��ۣ��������᪺���ʤ�V&�t��
				if(getBoundsLeft().intersects(t.getBounds())){
					setVelX(2);
				}
				
				if(getBoundsRight().intersects(t.getBounds())){
					setVelX(-2);
				}

			}
		}
		
		// ���U��
		if(falling){
			gravity+=0.1;
			setVelY((int)gravity);
		}
		
	}
	 
}
