package tile;

import java.awt.Color;
import java.awt.Graphics;

import game.Handler;
import game.Id;


public class Pipe extends Tile {
	public Pipe(int x,int y,int width,int height,boolean solid,Id id,Handler handler,int facing){
		super(x,y,width,height,solid,id,handler);
		this.facing = facing;
	}
	
	// draw pipe
	public void render(Graphics g) {
		g.setColor(new Color(0,128,0)); // color
		g.fillRect(x,y,width,height); // shape
	}

	public void tick() {
		
	}
	
}
