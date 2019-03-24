package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import entity.Entity.Entity;
import entity.mob.Goomba;
import entity.mob.Player;
import entity.powerup.Flower;
import entity.powerup.Mushroom;
import tile.Coin;
import tile.Flag;
import tile.Pipe;
import tile.PowerUpBlock;
import tile.Tile;
import tile.Treasure;
import tile.Wall;



public class Handler {
	public LinkedList<Entity> entity = new LinkedList<Entity>();
	public LinkedList<Tile> tile = new LinkedList<Tile>();
	
	public Handler(){
		//createLevel();
	}
	
	// draw level
	public void render(Graphics g){
		for(Entity en:entity){
			en.render(g);
		}
		for(Tile ti:tile){
			ti.render(g);
		}
	}
	
	// update screen
	public void tick(){
		for(Entity en:entity){
			en.tick();
		}
		for(Tile ti:tile){
			ti.tick();
		}
	}
	
	//新增、移除物件
	public void addEntity(Entity en){
		entity.add(en);
	}
	public void removeEntity(Entity en){
		entity.remove(en);
	}
	public void addTile(Tile ti){
		tile.add(ti);
	}
	public void removeTile(Tile ti){
		tile.remove(ti);
	}
	
	//建立新地圖
	public void createLevel(BufferedImage level){
		int width = level.getWidth();
		int height = level.getHeight();
		
		for(int y=0;y<height;y++){
			for(int x=0;x<width;x++){
				int pixel = level.getRGB(x,y);
				
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				//讀取相對應顏色來建立地圖內容
				if(red==0 && green==0 && blue==0) addTile(new Wall(x*64,y*64,64,64,true,Id.wall,this));
				if(red==0 && green==0 && blue==255) addEntity(new Player(x*64,y*64,48,48/*,false*/,Id.player,this));
				if(red==255 && green==119 && blue==0) addEntity(new Goomba(x*64,y*64,64,64/*,true*/,Id.goomba,this));
				
				//if(red==255 && green==0 && blue==0) addEntity(new Mushroom(x*64,y*64,64,64/*,true*/,Id.mushroom,this));
				if(red==255 && green==255 && blue==0) addTile(new PowerUpBlock(x*64,y*64,64,64,true,Id.powerUp,this,Game.mushroom));
				if(red==153 && green==0 && blue==255) addTile(new PowerUpBlock(x*64,y*64,64,64,true,Id.powerUp,this,Game.flower));
				if(red==255 && green==0 && blue==136) addTile(new PowerUpBlock(x*64,y*64,64,64,true,Id.powerUp,this,Game.flower));
				
				if(red==0 && (green>123&&green<129) &&blue==0) addTile(new Pipe(x*64,y*64,64,64*17,true,Id.pipe,this,128-green));
				
				if(red==255 && green==250 && blue==0) addTile(new Coin(x*64,y*64,64,64,true,Id.coin,this));
				if(red==0 && green==255 && blue==0) addTile(new Flag(x*64,y*64,64,64*5,true,Id.flag,this));
				if(red==0 && green==245 && blue==255) addTile(new Treasure(x*64,y*64,64,64,true,Id.treasure,this));
				//153,0,255
				//255,0,136
			}
		}
	}
	
	//清除地圖
	public void clearLevel(){
		entity.clear();
		tile.clear();
	}
}
