package gul;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Launcher {
	
	public Button[] buttons;
	public Button[] restartbuttons;
	private BufferedImage bg;

	public Launcher(){
		buttons = new Button[2];
		
		//button position¡Bword size
		buttons[0] = new Button(500,400,100,100,"Start Game");
		buttons[1] = new Button(500,500,100,100,"Exit Game");
		
		try {
			// load background picture
			bg = ImageIO.read(getClass().getResource("/menu1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g){
		g.drawImage(bg, 0, 0,1090,800, null); // background 

		for(int i=0;i<buttons.length;i++){
			buttons[i].render(g);
		}
	}
	
}
