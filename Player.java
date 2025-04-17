//this is the player
import java.awt.*;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Entity {

   public boolean shoot;            //checks to see if the player if firing
   
   KeyController keyC;              //checks to see if the user is putting any key inputs
   
   
   public Player(Display db, KeyController keyC) {
   
      this.db = db;                 //keps the display so it can draw on it
      this.keyC = keyC;             //keeps the key listener to make sure the user's inputs align with the players movements
     
      setDefault();                 //gets the players default stats
      getPlayerImage();             //gets the players images
      
   }
   
   public void setDefault() {
      x = db.screenWidth/2 - db.tileSize/2;     //base location
      y = db.screenDepth - 2*db.tileSize;
      speed = 6;                    //speed of the player movement
      statis = "plain";             //standard image of the player
      maxLife = 3;                  //max life of the player 
      life = maxLife;               //current life of the player
      maxLasers = 7;                //max amount of laser that can be on screen at once        
      alive = true;                 //makes sure the player is alive so it can be hit with collisions and can be drawn
      
   }
   
   public void getPlayerImage() { //loads all the players images
      
      try {
         plain = ImageIO.read(getClass().getResourceAsStream("res/PlayerPlain.png"));
         charging = ImageIO.read(getClass().getResourceAsStream("res/PlayerCharging.png"));
         shooting = ImageIO.read(getClass().getResourceAsStream("res/PlayerShooting.png"));
         hit = ImageIO.read(getClass().getResourceAsStream("res/PlayerHit.png"));
         blownUp = ImageIO.read(getClass().getResourceAsStream("res/PlayerBlownUp.png"));
         gone = ImageIO.read(getClass().getResourceAsStream("res/PlayerGone.png"));
         respawn = ImageIO.read(getClass().getResourceAsStream("res/PlayerRespawn.png"));
      }
      catch(IOException e) {
         e.printStackTrace();
      }
   }
   
   public void update(Graphics2D g2) {
      
      updateMovement();                //updates the movement 
      updateHitBox();                  //updates the hitbox with the player's movement
      
      if(delayTime > maxDelay) {       //makes the animations slower than every frame but keeps producing a 60 fps game
         updateStatis();
         delayTime = 0;
      }
      else {
         delayTime++;
      }
      
   } //end of update
   
  
   
   public void draw(Graphics2D g2) {   //chooses the best image to display what the player is currently doing
      
      if(statis.equals("plain")) {
         image = plain;                
      }
      else if(statis.equals("shooting")) {
         image = shooting;
      }
      else if(statis.equals("hit")) {
         image = hit;  
      }
      else if(statis.equals("blownUp")) {
         image = blownUp;
      }
      else if(statis.equals("respawn")) {
         image = respawn;
      }
      else if(statis.equals("gone")) {
         image = gone;
      }
      else {
         statis = "plain";                //resets the players statis if it isn't doing anything
      }
      
         
      if(life > 0 || dying) {             //draws the player if it isn't completely dead
         g2.drawImage(image, x, y, (int)(db.tileSize*scale), (int)(db.tileSize*scale), null);  
      }
      
   }
   
   public void updateMovement() {
      
      if(!dying) {                        //this makes it so you can't move once you are dying   
         if(keyC.upHold == true) {        //the player can move up until it reaches 550 y
            if(y > 550) {
               y -= speed;
            }
         }
         if(keyC.downHold == true) {      //the player can move to the right until it hits the side of the screen
            if(y < db.screenDepth - 2*db.tileSize) {
               y += speed;
            }
         }
         if(keyC.rightHold == true) {     //the player can move down until it hits the bottome of the screen
            if(x < db.screenWidth - db.tileSize) {
               x += speed;
            }
         }
         if(keyC.leftHold == true) {      //the player can move to the left until it hits the side of the screen
            if(x > 0) { 
               if(x > 0) {
                  x -= speed;
               }
            }
         }
      }
   
   }
   
   
   public void updateHitBox() {          //tracks where the player is and keeps a hitbox loaded at that position
    
      hitBoxX = x + db.tileSize/8;           //left
      hitBoxY = y;                           //top
      hitBoxXr = x + db.tileSize * 7 / 8;    //right
      hitBoxYb = y + db.tileSize * 3 / 4;    //bottom
      
   }
   
   public void updateStatis() {
      
      //updates the player's statis
        
      if(life == 0 && !db.gameOverDone) {    
         db.gameOver = true;                       //if the player is completely dead the game will end.  
      }
           
      if(dying && damage == 0) {
         statis = "hit";                           //if the player is dying it will go through this loop to give it an animation
         damage++;
         life--;
      }
      else if(dying && damage < 2) {
         statis = "blownUp";
         damage++;
      }
      else if(dying && damage < 4) {
         statis = "gone";
         damage++;
      }
      else if (dying || (respawnTimer > 0 &&  respawnTimer < immunity)) { // respawning
            
         dying = false;                            //the player is no longer dying and recieves spawn immunity
         respawnTimer++;
         if(respawnTimer%2 == 0) {
            statis = "plain";
         }
         else {
            statis = "respawn";                    //switches between looking plain and a blue bubble to show it is immune
         }
         if(respawnTimer == immunity) {
            damage = 0;
            respawnTimer = 0;
            statis = "plain";                      //immunity ends
         }
            
      }
      if(shoot && !dying) {
         
         statis = "shooting";                      //the player can's shoot if the player is dead
         image = shooting;
         shoot = false;
               
      } 
      else if(!dying && !statis.equals("respawn")) {
         statis = "plain";                         //the player completes the respawn
            
      }
         
         
         
   }
   
   public void shoot() {
      shoot = true;  
      db.shoots++;                                  //when the player wants to shoot
   }
   public void hit() {                             //when the player gets hit by a laser
      dying = true;
   }
   
}