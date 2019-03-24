package entity.powerup;

import java.awt.Graphics;

import entity.Entity.Entity;
import game.Game;
import game.Handler;
import game.Id;

public class Flower extends Entity{

	public Flower(int x, int y, int width, int height, Id id, Handler handler) {
		super(x, y, width, height, id, handler);
	}
	
	// draw flower
	public void render(Graphics g) {
		g.drawImage(Game.flower.getBufferedImage(),x,y,width,height,null);
	}

	public void tick() {
		
	}

}
