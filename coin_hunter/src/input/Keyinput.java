package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import entity.Entity.Entity;
import entity.mob.Player;
import entity.powerup.Fireball;
import game.Game;
import game.Id;
import states.PlayerState;
import tile.Tile;



public class Keyinput implements KeyListener{
	
	private boolean fire;
	
	//左右移動，空白鍵跳躍，上下進水管
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		
		for(Entity en:Game.handler.entity){
			if(en.getId()==Id.player){
				if(en.goingDownPipe) return;
				
				switch(key){
					case KeyEvent.VK_SPACE:
						for(int q=0;q<Game.handler.tile.size();q++){
							Tile t = Game.handler.tile.get(q);
							
							// 觸發通過水管的條件(向上)
							if(t.getId()==Id.pipe){
								if(en.getBoundsTop().intersects(t.getBounds())){
									if(!en.goingDownPipe) en.goingDownPipe = true;
								}
							}
						}
						if(!en.jumping) {
							en.jumping = true;	
							en.gravity = 8.0;	//跳躍高度		
							Game.jump.play();	// music
						}
						break;
						
					case KeyEvent.VK_DOWN:
						for(int q=0;q<Game.handler.tile.size();q++){
							Tile t = Game.handler.tile.get(q);
							
							// 觸發通過水管的條件(向下)
							if(t.getId()==Id.pipe){
								if(en.getBoundsBottom().intersects(t.getBounds())){
									if(!en.goingDownPipe) en.goingDownPipe = true;
								}
							}
						}
						break;
						
					case KeyEvent.VK_LEFT: // 往左走
						en.setVelX(-5);	// 移動速度
						en.facing = 0; // 方向(0:左邊)
						break;
						
					case KeyEvent.VK_RIGHT: // 往右走
						en.setVelX(5);
						en.facing = 1;
						break;
						
					case KeyEvent.VK_Z: // 攻擊
						if(/*Player.state==PlayerState.FIRE &&*/ !fire) {
							switch(en.facing) {
								case 0:
									Game.handler.addEntity(new Fireball(en.getX()-24, en.getY()+12, 24, 24, Id.fireball, Game.handler));
									fire = true;
									break;
								case 1:
									Game.handler.addEntity(new Fireball(en.getX()+en.width, en.getY()+12, 24, 24, Id.fireball, Game.handler));
									fire = true;
									break;
							}
						}
						
					}
				}
			
		}
		
	}
	
	// 按鍵放開就停止人物動作
	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		
		for(Entity en:Game.handler.entity){
			if(en.getId()==Id.player){
				switch(key){
					case KeyEvent.VK_UP:
						en.setVelY(0);
						break;
					case KeyEvent.VK_DOWN:
						en.setVelY(0);
						break;
					case KeyEvent.VK_LEFT:
						en.setVelX(0);
						break;
					case KeyEvent.VK_RIGHT:
						en.setVelX(0);
						break;
					case KeyEvent.VK_Z:
						fire = false;
						break;
				}
			}
		}
	}
	
	public void keyTyped(KeyEvent arg0){
		
	}
}
	
