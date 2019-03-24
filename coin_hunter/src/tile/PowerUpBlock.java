package tile;

import java.awt.Graphics;

import entity.powerup.Flower;
import entity.powerup.Mushroom;
import game.Game;
import game.Handler;
import game.Id;
import gfx.Sprite;

public class PowerUpBlock extends Tile{
	
	private Sprite powerUp;
	
	private boolean poppedUp = false;
	
	private int spriteY = getY();
	
	public PowerUpBlock(int x,int y,int width,int height,boolean solid,Id id,Handler handler,Sprite powerUp){
		super(x,y,width,height,solid,id,handler);
		this.powerUp = powerUp;
	}
	
	// draw ? block
	public void render(Graphics g){
		// Animation of hit ? block
		if(!poppedUp) g.drawImage(Game.powerUp.getBufferedImage(), x, spriteY, width, height,null);
		
		// block status
		if(!activated) // (before hit it)
			g.drawImage(Game.powerUp.getBufferedImage(), x, y, width, height,null);
		else  // (after hit it)
			g.drawImage(Game.usedPowerUp.getBufferedImage(), x, y, width, height,null);
	}
	
	
	// Update screen
	public void tick(){
		if(activated&&!poppedUp){
			spriteY--; // Adjustment coordinates
			
			if(spriteY<=y-height){
				// There is mushroom on ? block
				if(powerUp == Game.mushroom)
					handler.addEntity(new Mushroom(x,spriteY,width,height,Id.mushroom,handler));
				else if(powerUp == Game.flower) // There is flower on ? block
					handler.addEntity(new Flower(x,spriteY,width,height,Id.flower,handler));
				
				poppedUp = true;
			}
		}
	}
}
