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
	
	//���k���ʡA�ť�����D�A�W�U�i����
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		
		for(Entity en:Game.handler.entity){
			if(en.getId()==Id.player){
				if(en.goingDownPipe) return;
				
				switch(key){
					case KeyEvent.VK_SPACE:
						for(int q=0;q<Game.handler.tile.size();q++){
							Tile t = Game.handler.tile.get(q);
							
							// Ĳ�o�q�L���ު�����(�V�W)
							if(t.getId()==Id.pipe){
								if(en.getBoundsTop().intersects(t.getBounds())){
									if(!en.goingDownPipe) en.goingDownPipe = true;
								}
							}
						}
						if(!en.jumping) {
							en.jumping = true;	
							en.gravity = 8.0;	//���D����		
							Game.jump.play();	// music
						}
						break;
						
					case KeyEvent.VK_DOWN:
						for(int q=0;q<Game.handler.tile.size();q++){
							Tile t = Game.handler.tile.get(q);
							
							// Ĳ�o�q�L���ު�����(�V�U)
							if(t.getId()==Id.pipe){
								if(en.getBoundsBottom().intersects(t.getBounds())){
									if(!en.goingDownPipe) en.goingDownPipe = true;
								}
							}
						}
						break;
						
					case KeyEvent.VK_LEFT: // ������
						en.setVelX(-5);	// ���ʳt��
						en.facing = 0; // ��V(0:����)
						break;
						
					case KeyEvent.VK_RIGHT: // ���k��
						en.setVelX(5);
						en.facing = 1;
						break;
						
					case KeyEvent.VK_Z: // ����
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
	
	// �����}�N����H���ʧ@
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
	
