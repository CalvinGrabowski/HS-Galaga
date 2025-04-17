
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

//this will make all the lasers and it will be an object which can be created
public class Laser extends Entity{

   Entity player;
   
   BufferedImage purple, red, blue;
   String direction;                   //either up or down
   
   
   int x, y;                           //location
   int speed;                          //speed
   Graphics g3;                        //graphics for drawing
   Display db;
   int color;                          //the color of laser
   boolean wasAlive;                   //this makes the laser load before it shoots so it prevents a glitch where the aliens would get hit by lasers that player spawned
   
   Laser(Display db, String direction, int color, int x, int y) {
      this.db = db;                    //holds the display
      speed = 8;                       //all lasers currently go at this speed
      this.direction = direction;      //holds the direction
      this.color = color;              //purple is 0, blue is 1, red is anything else
      getDefaultLaser();               //sets the images
      this.x = x + db.tileSize/2+3;    //takes the player of alien location and plants the laser in the middle of that
      this.y = y - db.tileSize;
      this.alive = false;              //makes the laser unalive for a frame
   }
   
   public void hit() {
      alive = false;                   //when the laser hits something it disappears and is no longer alive
   }
   
   
         
   public void getDefaultLaser() {     //loads the images
      try {
         purple = ImageIO.read(getClass().getResourceAsStream("res/PurpleLaser.png"));
         red = ImageIO.read(getClass().getResourceAsStream("res/RedLaser.png"));
         blue = ImageIO.read(getClass().getResourceAsStream("res/BlueLaser.png"));
      }
      catch(IOException e) {
         e.printStackTrace();
      }
   }
   
   //update the laser by putting it into position
   public Laser update() {
      
      //this stops a glitch where the laser would spawn on the aliens and the alien would die before the laser was positioned properly
      if(wasAlive == false) {
         alive = true;
         wasAlive = true;
      }
      
      updateHitBox();         //does the hitbox first so the user can see the laser for longer and actually see the collision before the computer detects it
      
      if(direction.equals("up")) {
         y -= speed;          //goes in the direction specified
      }
      else if(direction.equals("down")) {
         y += speed;
      }
      
      
      return this;            //the laser is returned
     
   
   }
   
   public void draw(Graphics g2) {
      BufferedImage laser;    //picks the color of the laser based on the information given
      if(color == 0) {
         laser = purple;
      }
      else if(color == 1) {
         laser = blue;
      }
      else {
         laser = red;
      }
      if(alive == true) {      //if the laser is alive it will be drawn
         g2.drawImage(laser, x, y, db.tileSize/8, db.tileSize, null);
      }
   }
   
   public void updateHitBox() {       //the laser's hitbox used for collision
    
      hitBoxX = x;                     //left
      hitBoxY = y;                     //top
      hitBoxXr = x + db.tileSize/8;    //right
      hitBoxYb = y + db.tileSize;      //bottom
      
   }
   
}