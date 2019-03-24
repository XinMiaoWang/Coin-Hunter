package entity.mob;

import java.awt.Graphics;
import java.util.Random;

import entity.Entity.Entity;
import game.Game;
import game.Handler;
import game.Id;
import tile.Tile;


public class Goomba extends Entity {
	
	private Random random = new Random();

	private int frame = 0;
	private int frameDelay = 0;
	
	public Goomba(int x,int y,int width,int height/*,boolean solid*/,Id id,Handler handler){
		super(x,y,width,height/*,solid*/,id,handler);
		
		int dir = random.nextInt(2); //�H������ 0 �� 1
		
		// 0:�����A1:���k
		switch(dir){
			case 0:
				setVelX(-2); // ���ʤ�V&�t��
				facing = 0;
				break;
			case 1:
				setVelX(2);
				facing = 1;
				break;
		}
	}
	
	//�樫��
	public void render(Graphics g){
		if(facing==0){
			g.drawImage(Game.goomba[frame+4].getBufferedImage(), x, y, width,height,null); //����
		} else if(facing==1){
			g.drawImage(Game.goomba[frame].getBufferedImage(), x, y, width,height,null); //����
		}
	}
	
	// update screen
	public void tick(){
		x+=velX;
		y+=velY;
		
		for(int i=0;i<handler.tile.size();i++){
			Tile t = handler.tile.get(i);
			
			if(t.isSolid()){
				// �I��a�O
				if(getBoundsBottom().intersects(t.getBounds())){
					setVelY(0);
					if(falling) falling = false;
				} else if(!falling){
					falling = true;
					gravity = 0.4;
				}
				
				// ���쥪��-->��V�~��
				if(getBoundsLeft().intersects(t.getBounds())){
					setVelX(2);
					facing =1;
				}
				
				// ����k��-->��V�~��
				if(getBoundsRight().intersects(t.getBounds())){
					setVelX(-2);
					facing =0;
				}

			}
		}
		
		if(falling){
			gravity+=0.1;
			setVelY((int)gravity);
		}
		
		//�樫��
		if(velX!=0){
			frameDelay++;
			if(frameDelay>=3){
				frame++;
				if(frame>=4){
					frame = 0;
				}
				frameDelay = 0;
			}
		}
		
	}
}
