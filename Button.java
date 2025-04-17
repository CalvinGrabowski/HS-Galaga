
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Button extends Entity{
   
   BufferedImage red, green, reset, image;
   boolean show;
   int color;                                //color 0 is green, 1 is red, 2 or higher is reset
   Display db;
   int size;                                 //size is the side length of the button
   
   public Button(Display db) {
      
      buttonImages();                        //loads the images
      this.db = db;
      show = true;                           //makes the button visible and alive so it can be hit
      alive = true;
            
      size = 3 * db.tileSize;
      x = db.screenWidth/2 - size/2;
      y = 100;                               //creates the placement of the button
      
      hitBoxX = x;                           //creates the hitbox of the button
      hitBoxY = y;
      hitBoxXr = x + size;
      hitBoxYb = y + (5 * size / 6);
      
   }
   
   public void buttonImages() {              //it has the images the button can look like
      try {
         red = ImageIO.read(getClass().getResourceAsStream("res/RedPlayButton.png"));
         green = ImageIO.read(getClass().getResourceAsStream("res/GreenPlayButton.png"));
         reset = ImageIO.read(getClass().getResourceAsStream("res/RestartButton.png"));
      }
      catch(IOException e) {
         e.printStackTrace();
      }
   }
   
   public void color(int c) {                //this would take in the color variable
      color = c;
   }
   
   public void update() {                    //this switches if the button is visible to invisible and vice versa
      if(show) {
         show = false;
      }
      else {
         show = true;
      }
   }
   
   public void reset() {                     //the reset button is color 3
      color = 3;
   }
      
   public void draw(Graphics2D g2) {         //based on the color that was selected it will appear as that color
      if(show) {
         if(color == 0) {
            image = green;
         }
         else if(color == 1) {
            image = red;
         }
         else {
            image = reset;
         }
            
         g2.drawImage(image, x, y, size, size, null);       //prints the button
      }
   }
}