
import java.awt.image.BufferedImage;
//any entity will use this as a superclass
public class Entity {

   public int x, y, speed;          //all entities need a speed and location
   public Display db;               //all entities need the display to draw on
   
   public BufferedImage plain, charging, shooting, hit, blownUp, gone, respawn, charging1, charging2, charging3;
   BufferedImage image = null;      //these images are used by a lot of the entities and image is generally used to be selected as one of the images
   
   public String statis;            //what the player uses to detect if it is shooting getting hit or anything like that
   public int damage = 0;           //it is what the player uses to figure out what state the explosion is in //don't ask why I didn't ise explosion like the aliens
   
   public int maxDelay = 5;         //this is the delay for when the image of a character changes
   public int delayTime = 0;
   
   public boolean alive, dying;     //determines if something should be drawn base off these two boolean expressions    
   
   public int laserDelay;           //shooting delay
   public double scale = 1.25;      //scales the player and alien images
   
   
   //hitBox
   public int hitBoxX, hitBoxY, hitBoxXr, hitBoxYb;
   
   
   //lasers values
   public int maxLasers;            //the max amount of lasers the player can shoot
   public String direction;         //what way the laser is facing
  
   
   //player and alien life values
   public int life;
   public int maxLife;
   
   public int respawnTimer = 0;        //the counter for the respawning animation
   public int immunity = 10;           //time the player is immune after respawn (10 player statis changes)
   
  
   
}