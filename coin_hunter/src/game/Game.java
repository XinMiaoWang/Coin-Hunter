package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import entity.Entity.Entity;
import entity.mob.Player;
import gfx.Sprite;
import gfx.SpriteSheet;
import gul.Button;
import gul.Launcher;
import input.Keyinput;
import input.MouseInput;

public class Game extends Canvas implements Runnable{
	public static final int WIDTH = 270; // screen width
	public static final int HEIGHT = WIDTH/14*10; // screen height
	public static final int SCALE = 4;
	public static final String TITLE ="Coin hunter";
	
	private Thread thread;
	private boolean running = false;
	private static BufferedImage[] levels;
	private static BufferedImage background;
	
	public static int level = 0;
	public static int coins = 0;
	
	public static int targets = 5;
	public static int lives = 2;
	public static int add = 5;
	public static int deathScreenTime = 0;
	
	public static boolean showDeathScreen = true;
	public static boolean gameOver = false;
	public static boolean gameFinish = false;
	public static boolean playing = false;
	public static boolean treasures = false;

	public static Handler handler;
	public static SpriteSheet sheet;
	public static Camera cam;
	public static Launcher launcher;
	public static MouseInput mouse;
	public static Sprite wall;
	public static Sprite powerUp;
	public static Sprite usedPowerUp;
	
	public static Sprite player[] = new Sprite[8];
	public static Sprite goomba[] = new Sprite[8];
	public static Sprite flag[]= new Sprite[3];
	public static Sprite fireplayer[] = new Sprite[8];
	
	public static Sprite mushroom;
	public static Sprite coin;
	public static Sprite treasure;
	public static Sprite fireball;
	public static Sprite flower;
	
	/*Sounds*/
	public static Sound jump;
	public static Sound die;
	public static Sound getCoin;
	public static Sound gameover;
	public static Sound enemyDie;
	public static Sound pipe;
	public static Sound powerup;
	public static Sound failure;
	public static Sound success;
	
	public Game(){
		Dimension size = new Dimension(WIDTH*SCALE,HEIGHT*SCALE);
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
	}
	
	private void init(){ 		
		
		sheet = new SpriteSheet("/image.png"); // 圖
		wall = new Sprite(sheet,4,1); // 磚塊  
		powerUp = new Sprite(sheet,3,1);
		usedPowerUp = new Sprite(sheet,4,1);
		coin = new Sprite(sheet,5,1); // 金幣
		treasure = new Sprite(sheet,7,1); // 寶箱
		mushroom = new Sprite(sheet,2,1); // 香菇
		
		fireball = new Sprite(sheet,9,1);
		flower = new Sprite(sheet,9,1);
		
		handler = new Handler();
		cam = new Camera();
		launcher = new Launcher();	// menu
		mouse = new MouseInput();
		
		levels = new BufferedImage[3];//關卡
		
		addKeyListener(new Keyinput());
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		
		//角色
		for(int i=0;i<player.length;i++){
			player[i] = new Sprite(sheet,i+1,16);
		}
		for(int i=0;i<fireplayer.length;i++){
			fireplayer[i] = new Sprite(sheet,i+1,16);
		}
		
		//怪物
		for(int i=0;i<goomba.length;i++){
			goomba[i] = new Sprite(sheet,i+1,15);
		}
		
		//旗子
		for(int i=0;i<flag.length;i++){
			flag[i] = new Sprite(sheet,i+1,2);
		}
		
		//關卡1~3
		try{
			levels[0] = ImageIO.read(getClass().getResource("/Map1.png"));
			levels[1] = ImageIO.read(getClass().getResource("/Map2.png"));
			levels[2] = ImageIO.read(getClass().getResource("/Map3.png"));
			background = ImageIO.read(getClass().getResource("/bg1.gif"));
		} catch(IOException e){
			e.printStackTrace();
		}
		
		//建立第一關
		if(level==0)
			handler.createLevel(levels[level]);
		
		//player position
		handler.addEntity(new Player(300,512,64,64/*,true*/,Id.player,handler));
		
		
		/*Sounds*/
		jump = new Sound("/Sound/jump.wav");
		die = new Sound("/Sound/die.wav");
		getCoin = new Sound("/Sound/coin.wav");
		pipe = new Sound("/Sound/pipe.wav");
		powerup = new Sound("/Sound/mushroom.wav");
		enemyDie = new Sound("/Sound/goomba.wav");
		failure = new Sound("/Sound/gameover.wav");
		success = new Sound("/Sound/finish.wav");
	}
	
	public synchronized void start(){
		if(running) return;
		running = true;
		thread = new Thread(this,"Thread");
		thread.start();
	}
	
	public synchronized void stop(){
		if(!running) return;
		running = false;
		try{
			thread.join();
		} catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public void run(){
		init();
		requestFocus();
		
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double delta = 0.0;
		double ns = 1000000000.0/60.0;
		int frames = 0;
		int ticks = 0;
		
		while(running){
			long now = System.nanoTime();
			delta+=(now-lastTime)/ns;
			lastTime = now;
			while(delta>=1){
				tick();
				ticks++;
				delta--;
			}
			render();
			frames++;
			if(System.currentTimeMillis()-timer>1000){
				timer+=1000;
				System.out.println(frames + " Fromes Per Second " + ticks + " Updates Per Second ");
				frames = 0;
				ticks = 0;
			}
		}
		
		stop();
	}
	
	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs==null){
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(background, 0, 0, getWidth(), getHeight(),null);
		
		//左上角顯示金幣&數量
		if(!showDeathScreen){
			g.drawImage(Game.coin.getBufferedImage(), 20, 20, 75, 75,null);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier",Font.BOLD,20));
			g.drawString("x" + coins, 100, 95);
		}
		
		//life、gameover畫面
		if(showDeathScreen){
			if(!gameOver){
				g.setColor(Color.WHITE);
				g.setFont(new Font("Courier",Font.BOLD,50));
				g.drawImage(Game.player[0].getBufferedImage(), 500, 300, 100, 100,null);
				g.drawString("x" + lives, 610, 400);
			} else{
				g.setColor(Color.RED);
				g.setFont(new Font("Courier",Font.BOLD,80));
				g.drawString("Game over", 610, 400);
				//failure.play();
				
				//延遲
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//launcher.render(g);
			}
		}
		
		// 過關
		if(gameFinish && !gameOver){
				g.setColor(Color.RED);
				g.setFont(new Font("Courier",Font.BOLD,80));
				g.drawString("Finish", 610, 400);
				//success.play();
				handler.clearLevel();
				
				//延遲
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//launcher.render(g);跳回 menu
		}
		
		// Camera ，screen move with player
		if(playing) g.translate(cam.getX(),cam.getY());
		
		if(!showDeathScreen&&playing) handler.render(g);
		else if(!playing) launcher.render(g);
		
		g.dispose();
		bs.show();
	}
	
	public void tick(){
		if(playing) handler.tick();
		
		// Camera
		for(Entity e:handler.entity){
			if(e.getId()==Id.player){
				if(!e.goingDownPipe) cam.tick(e);
			}
		}
		
		if(showDeathScreen&&!gameOver) deathScreenTime++;
		
		// 破關超時
		if(deathScreenTime>=180){
			showDeathScreen = false;
			deathScreenTime = 0;
			handler.clearLevel();
			handler.createLevel(levels[level]);
			handler.addEntity(new Player(300,512,64,64/*,true*/,Id.player,handler));
		}
	}
	
	// Camera
	public static int getFrameWidth(){
		return WIDTH*SCALE;
	}
	public static int getFrameHeight(){
		return HEIGHT*SCALE;
	}
	
	
	/* go to next level */
	public static void switchLevel(){
		if(level<2) {
			Game.level++;			//進到下一關
			Game.targets = Game.targets + add;
			handler.clearLevel();	//清除地圖、角色
			lives++;				//過關加一條命
			treasures = false;
			handler.createLevel(levels[level]);//放新地圖
			handler.addEntity(new Player(300,512,64,64/*,true*/,Id.player,handler));//放角色
		}
		else {
			gameFinish = true;
		}
		
	}
	
	public static void main(String[] args){
		Game game = new Game();
		JFrame frame = new JFrame(TITLE);
		frame.add(game);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		game.start();
	}
}
