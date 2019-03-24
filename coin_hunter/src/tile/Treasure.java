package tile;

import java.awt.Graphics;

import game.Game;
import game.Handler;
import game.Id;


public class Treasure extends Tile {

	public Treasure(int x, int y, int width, int height, boolean solid, Id id, Handler handler) {
		super(x, y, width, height, solid, id, handler);
	}
	
	// draw treasure
	public void render(Graphics g) {
		g.drawImage(Game.treasure.getBufferedImage(), x, y, width, height, null);
	}

	public void tick() {
		
	}

}
