
//enemy



import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

//contains all three aliens
public class Alien extends Entity {


   int desX;
   int desY;
                                          //original destination and other planned destinations
   int oDesX;
   int oDesY;
   
   Display db;
   int delay;                             //the variable that controls how long the alien will wait before moving
   int exploding = 0;                     //this variable shows what stage the alien is in an explosion after it dies
   boolean shooting;                      //is it shooting
   int shootingDelay = 2;                 //controls the time it takes for the aliens to charge up their shoots
   int chargeStage = 0;                   //this is what stage in the charge the alien is at
   boolean fire = false;                  //only active for a frame that activates the laser to spawn in
   public boolean moving;                 //the alien only moves when the alien is moving
   public double laserChance;             //this is the chance that the alien fires randomly every frame        
   public int movementStage = 0;          //this shows what part of the right to left movement animation is at
   public String color;                   //holds what color the alien is
   
   private BufferedImage charging1H, charging2H, charging3H, charging1D, charging2D, charging3D, hit2;         //this is the charging animations when the alien is damaged or hit
   
   //this is for when there are too many aliens that are getting created that they shouldn't make more than the tiles can hold
   Alien(boolean alive) {
      this.alive = alive;
   }
   
   Alien(Display db, int spawnpoint, int delay, int alienColor) {
     
      this.db = db;                    //holds the display
      spawnpoint(spawnpoint);          //this gets the x and y for the alien based on the 
      commonAlien(x, y, delay);        //this sets the alien's priorities that all three aliens share
      
      if(alienColor == 0)              //chooses the alien based on the number passed through
         blueAlien();
      if(alienColor == 1)
         redAlien();
      if(alienColor > 1)
         greenAlien(); 
   }
   //this takes in a number that is a standad spawnpoint and gives the x and y for that
      
   public void spawnpoint(int spawnpoint) {  
      if(spawnpoint == 1){
         this.x = -db.tileSize - db.resDif - 1;
         this.y = db.screenDepth - db.tileSize*2;
      }                          //bottom left
      else if(spawnpoint == 2) {
         this.x = -db.tileSize - db.resDif - 1;
         this.y = db.screenDepth/2;
      }                          //middle left
      else if(spawnpoint == 3) {
         this.x = -db.tileSize - db.resDif - 1;
         this.y = db.tileSize;
      }                          //top left
      else if(spawnpoint == 0) {
         this.x = db.screenWidth/2;
         this.y = -db.tileSize;
      }                          //top middle
      
      else if(spawnpoint == 4){
         this.x = db.screenWidth;
         this.y = db.screenDepth - db.tileSize*2;
      }                          //bottom right
      else if(spawnpoint == 5) {
         this.x = db.screenWidth;
         this.y = db.screenDepth/2;
      }                          //middle right
      else if(spawnpoint == 6) {
         this.x = db.screenWidth;
         this.y = db.tileSize;
      }                          //top right
      else {
         this.x = 0;             //shouldn't ever happen but it will give 0 0 as coordinates so the game doesn't break
         this.y = 0;
      }
   }  
   
   public void commonAlien(int x, int y, int delay) {
      oDesX = x;                 //this makes the alien realize that the end goal is to go to the original x and y
      oDesY = y;
      this.speed = 4;            //sets speed, direction and delay
      direction = "down";
      this.delay = delay;
      alive = true;              //tells the alien it is alive and is moving
      moving = true;
   }
   
   public void blueAlien() {     //the blue alien's standard stats
      color = "blue";
      laserChance = 0.05;
      life = 1;
      getBlueAlienImage();
   }
   
   public void redAlien() {      //the red alien's standard stats
      color = "red";
      laserChance = 0.03;
      life = 2;
      getRedAlienImage();
   }
   
   public void greenAlien() {    //the green alien's standard stats
      color = "green";
      life = 3;
      laserChance = 0.05;
      getGreenAlienImage();
   }
   
   public void getBlueAlienImage() {   //loads all the images the blue alien can look like
      
      try {
         plain = ImageIO.read(getClass().getResourceAsStream("res/BluePlain.png"));
         charging1 = ImageIO.read(getClass().getResourceAsStream("res/BlueCharging1.png"));
         charging2 = ImageIO.read(getClass().getResourceAsStream("res/BlueCharging2.png"));
         charging3 = ImageIO.read(getClass().getResourceAsStream("res/BlueCharging3.png"));
         hit = ImageIO.read(getClass().getResourceAsStream("res/BlueHit.png"));
         blownUp = ImageIO.read(getClass().getResourceAsStream("res/BlueBlownUp.png"));
         gone = ImageIO.read(getClass().getResourceAsStream("res/BlueGone.png"));
      }
      catch(IOException e) {
         e.printStackTrace();
      }
   }
   
   public void getRedAlienImage() {    //loads all the images the red alien can look like
      
      try {
         plain = ImageIO.read(getClass().getResourceAsStream("res/RedPlain.png"));
         charging1 = ImageIO.read(getClass().getResourceAsStream("res/RedCharging1.png"));
         charging1H = ImageIO.read(getClass().getResourceAsStream("res/RedCharging1Hit.png"));
         charging2 = ImageIO.read(getClass().getResourceAsStream("res/RedCharging2.png"));
         charging2H = ImageIO.read(getClass().getResourceAsStream("res/RedCharging2Hit.png"));
         charging3 = ImageIO.read(getClass().getResourceAsStream("res/RedCharging3.png"));
         charging3H = ImageIO.read(getClass().getResourceAsStream("res/RedCharging3Hit.png"));
         hit = ImageIO.read(getClass().getResourceAsStream("res/RedHit.png"));
         blownUp = ImageIO.read(getClass().getResourceAsStream("res/RedBlownUp.png"));
         gone = ImageIO.read(getClass().getResourceAsStream("res/RedGone.png"));
      }
      catch(IOException e) {
         e.printStackTrace();
      }
   }
   
   public void getGreenAlienImage() {  //loads all the images the green alien can look like
      
      try {
         plain = ImageIO.read(getClass().getResourceAsStream("res/GreenPlain.png"));
         charging1 = ImageIO.read(getClass().getResourceAsStream("res/GreenCharging1.png"));
         charging1H = ImageIO.read(getClass().getResourceAsStream("res/GreenCharging1Hit.png"));
         charging1D = ImageIO.read(getClass().getResourceAsStream("res/GreenCharging1Hit2.png"));
         charging2 = ImageIO.read(getClass().getResourceAsStream("res/GreenCharging2.png"));
         charging2H = ImageIO.read(getClass().getResourceAsStream("res/GreenCharging2Hit.png"));
         charging2D = ImageIO.read(getClass().getResourceAsStream("res/GreenCharging2Hit2.png"));
         charging3 = ImageIO.read(getClass().getResourceAsStream("res/GreenCharging3.png"));
         charging3H = ImageIO.read(getClass().getResourceAsStream("res/GreenCharging3Hit.png"));
         charging3D = ImageIO.read(getClass().getResourceAsStream("res/GreenCharging3Hit2.png"));
         hit = ImageIO.read(getClass().getResourceAsStream("res/GreenHit.png"));
         hit2 = ImageIO.read(getClass().getResourceAsStream("res/GreenHit2.png"));
         blownUp = ImageIO.read(getClass().getResourceAsStream("res/GreenBlownUp.png"));
         gone = ImageIO.read(getClass().getResourceAsStream("res/GreenGone.png"));
      }
      catch(IOException e) {
         e.printStackTrace();
      }
   }
   
   public void finalDestination(int oDesX, int oDesY) {     //sets the alien's end position
      this.oDesX = oDesX;
      this.oDesY = oDesY;   
   }                                               
   
   public void move(int locX, int locY) {                   //changes the current alien's direction by rerouting the alien's current target position
      
      desX = locX;
      desY = locY;
      moving = true;
      
   }
   
   public void moveRTL() {                                  //this reroutes the aliens to go to target locations instead of going to its final position
      if(movementStage == 0) {
         desX = db.screenWidth - db.tileSize;                //this makes the aliens run to the desX and desY    
         desY = 300;
         if(x == db.screenWidth - db.tileSize && y == 300) { //once it makes it to that position it will go to the next target position
            movementStage++;
         }
      }
      else if(movementStage == 1) {                          //new target position
         desX = 100;
         desY = 200;
         if(x == 100 && y == 200) {
            movementStage++;                                 //switches to the next target position
         }
         
      }
      else if(movementStage == 2) {                          //switches the target location to the original target location
         desX = oDesX;
         desY = oDesY;
         if(x == oDesX && y == oDesY) {                      //once it gets to the final location it stops moving and doesn't activate anymore movement stages by changing to the next stage which doesn't exist
            moving = false;
            movementStage++;
         }
      }
      
   
   }

   
   public void update() {
   
      
      if(delay == 0) {
          
         if(delayTime > maxDelay*2) {
            updateStatis();
            delayTime = 0;
         }
         else {                                       //once the delay is done it updates the alien images every delayTime so the images switch slowly
            delayTime++;
         }
         
      }
                                                      //waits the delay out by waiting delay amount of frames and doesn't do anything else in the waiting time
      if(delay > 0) {
         delay--;
      }
      
      
      else if((x != oDesX || y != oDesY) && (x != desX || y != desY) && (alive && !dying) && moving)  {        
         updateMovement();
         updateHitBox();                               //if the alien isn't at it's final position it should move and the hitbox should update to the new position
      }
       
   }
   
   public void updateHitBox() {                      //the alien can only collide with things within it's hitbox
    
      hitBoxY = y;                                    //top
      hitBoxX = x + db.tileSize / 16;                 //left
      hitBoxXr = x + db.tileSize * 15 / 16;           //right
      hitBoxYb = y + db.tileSize;                     //bottom
      
   }
   
   public void gameOver() {                          //when the game ends the aliens all die and explode
      life = 0;
      dying = true;
      exploding++;
   }
   
   public void hit() {                               //everytime an alien collides with something it gets hit and once it runs out of life it dies and starts exploding
      life--; 
      if(life == 0) {
         dying = true;
         exploding++;
         if(color.equals("blue")) {                   //each alien is worth a different amount of score when it dies
            db.score+=100;
         }
         else if(color.equals("red")) {
            db.score+=250;
         }
         else if(color.equals("green")) {
            db.score+=500;
         }
      }
   }
   
   public void draw(Graphics g2) {
      
      if(alive || dying) {                            //the alien will be drawn until it dies and completely explodes
         g2.drawImage(image, x, y, (int)(db.tileSize*scale), (int)(db.tileSize*scale), null);
      }
   
   }
   
   public void updateMovement() {
      
      
      int changeX = Math.abs(desX - x);               //finds if the alien is to the left or right of the target
      int changeY = Math.abs(desY - y);               //finds if the alien is above or below the target
      if(changeX >= changeY) {                        //it only moves the alien to the farther location x or y if they are equal distance it goes sideways
         if(x < desX) {                               //if the alien is to the left of the destination it moves to the right
            x += speed;
         }
         else if(x > desX) {                          //if the alien is to the right of the destination it moves to the left
            x -= speed;
         }
      }
      if(changeY >= changeX) {                        //if the alien is farther above or below than it is to the left or the right
         if(y < desY) {
            y += speed;                               //if the alien is above the target it will go down
         }
         else if(y > desY) {
            y -= speed;                               //if the alien is below the target it will go up
         }
      }
      
      
   }

   public void updateStatis() {                       //choose the picture that the alien should look like
      
      if(color.equals("red") && life == 1) {
         image = hit;                                  //when the red alien gets hit it becomes hit
      }
      else if(color.equals("green") && life != 3) {
         if(life == 1)
            image = hit2;                              //when the green alien gets hit it becomes hit
         if(life == 2)
            image = hit;                               //then it becomes damaged
      }
      else {
         image = plain;                                //otherwise it is basic
      }
      
      if(dying) {                                      //dying animation
      
         if(exploding > 0 && exploding < 2) {          //becomes hit
            image = hit;
            exploding++;
         }
         else if(exploding < 3) {                      //looks blown up
            image = blownUp;
            exploding++;
         }
         else if(exploding < 4) {                      //and then it is just pieces
            image = gone;
            exploding++;
         }
         else {
            exploding = 0;                             //then the alien gets deleted after it is both not alive and not dying
            alive = false;
            dying = false;
         }
         
      }
      else {
         if(!shooting) {
            double random = Math.random();            //makes a random chance to shoot based on the alien's laserChance
            if(random < laserChance) {
               shooting = true;
            }
         }
         else {
         
            if(chargeStage == 0) {
            
               if(color.equals("red") && life == 1)   //this makes the alien in charge statis one still have the damage it would have previously obtained
                  image = charging1H;
               else if(color.equals("green") && life != 3) {
                  if(life == 1)
                     image = charging1D;
                  if(life == 2)
                     image = charging1H;
               }
               else
                  image = charging1;                           //if undamaged it will just have the base charging animation
                  
               if(shootingDelay == 2) {                        //this controls the switching of what stage the alien's charge is in
                  chargeStage++;
                  shootingDelay = 0;
               }                                               //slows the animation down
               shootingDelay++;
            } 
            else if(chargeStage == 1) {                        //if the alien has recieved damage it will still be visible in the charge animation
            
               if(color.equals("red") && life == 1) 
                  image = charging2H;
               else if(color.equals("green") && life != 3) {
                  if(life == 1)
                     image = charging2D;
                  if(life == 2)
                     image = charging2H;
               }
               else 
                  image = charging2;                           //if undamaged it will have the base charging animation
                  
               if(shootingDelay == 2) {
                  chargeStage++;                               //controls the charging animation speed
                  shootingDelay = 0;
               }                                               //slows the animation down
               shootingDelay++;
            } 
            else if(chargeStage == 2) {                        //final charge of the alien
            
               if(color.equals("red") && life == 1) 
                  image = charging3H;
               else if(color.equals("green") && life != 3) {
                  if(life == 1)
                     image = charging3D;                       //keeps the damage in the charge animation
                  if(life == 2)
                     image = charging3H;
               }
               else
                  image = charging3;
                  
               if(shootingDelay == 1) {
                  chargeStage++;                               //once the alien is done charging it will shoot
                  shootingDelay = 0;
                  fire = true;
               }                                               //fires the laser
               shootingDelay++;
            } 
            else {
               if(color.equals("red") && life == 1) {
                  image = hit;                                 //when the red alien takes damage
               }
               else if(color.equals("green") && life != 3) {
                  if(life == 1)
                     image = hit2;                             //when the green alien takes damage
                  if(life == 2)
                     image = hit;
               }
               else {
                  image = plain;                               //the base alien
               }  
               shootingDelay = 0;                              //allows the alien to charge up its next shoot or its first shoot
               chargeStage = 0;
               shooting = false;
            }
         } 
         
      }
      
      
      
      
   }


}