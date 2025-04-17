
//life system

import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.awt.*;

public class Heart extends Entity{
   
   Display db;                
   int number;
   BufferedImage heart;
   
   
   public Heart(Display db, int number) {
      
      this.db = db;                          //keeps the display
      this.number = number;                  //keeps the number slot it will be in
      
      try {                                  //loads the heart image
         heart = ImageIO.read(getClass().getResourceAsStream("res/Heart.png"));
      }
      catch(IOException e) {
         e.printStackTrace();
      }
   }
                                              //Doesn't need an update method because if I lose a life I jsut remove the life
  
   public void draw(Graphics g2) {           //draws the heart image
   
      g2.drawImage(heart, db.tileSize/2, number * db.tileSize, db.tileSize, db.tileSize, null);
      
   }
   
   

}