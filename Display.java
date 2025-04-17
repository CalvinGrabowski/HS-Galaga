
import java.awt.*;
import javax.swing.JPanel;
import java.util.*;


public class Display extends JPanel implements Runnable {
   
   //screen settings
   final int firstTileSize = 16;             //16x16 pixels (character size)
   final int resDif = 3;                     //resolution difference
   public final int tileSize = firstTileSize * resDif;
   final int fPS = 60;                       //game speed
   public final int tilesWidth = 12;         //width of tiles
   public final int tilesDepth = 15;         //depth of tiles
   
   public final int screenWidth = tileSize * tilesWidth; //total pixel width
   public final int screenDepth = tileSize * tilesDepth; //total pixel depth
   
   Graphics2D g2;                            //creates the graphics that will be used to draw all the things on the display
   
   public int laserDelay;                    //this makes sure the player has a limit to how fast it can shoot
   
   int waveCounter, aliensSpawned;           //this creates a delay between each alien spawning by counting up to 20
   int waveSpawnDelay = 20;                  //this is the delay that is between each alien spawning
   int waveNumber;                           //this is what wave the player is currently on
   
   
   
   //things all the classes will look at
   public int score, hits, shoots, blockedShots;                     //random stats
   public boolean gameOver, gameOverDone, gameStarted;               //game statis
   
  
     
   KeyController keyC = new KeyController();                         //key listener
   Thread gameThread;                                                //a game runner that the game's updating is ran from
   
   Player player = new Player(this, keyC);                           //creates the player with the display board and the key listener
   public ArrayList<Heart> lives = new ArrayList<Heart>();           //all player lives
   public LinkedList<Laser> lasers = new LinkedList<Laser>();        //all player lasers
   public LinkedList<Alien> aliens = new LinkedList<Alien>();        //all aliens
   public LinkedList<Laser> aLasers = new LinkedList<Laser>();       //all alien lasers
   public LinkedList<Button> buttons = new LinkedList<Button>();     //all buttons (I only created one though)
   
   public Compare comp;                                              //makes the compare class as an object so I can use collisions
   public AlienTiles at;                                             //alien tiles is made into an object and it holds a 2D array of aliens finding the locations the aliens should spawn into based on that
   
   public Display() {
   
      at = new AlienTiles(this);                                     //creates the alienTiles with the display
      comp = new Compare(this);                                      //creates the compare with the display
      this.setPreferredSize(new Dimension(screenWidth, screenDepth));//creates the display screen size
      this.setBackground(Color.BLACK);                               
      this.setDoubleBuffered(true);                                  //protects the display class
      this.addKeyListener(keyC);                                     //integrates the key listener into the display
      this.setFocusable(true);                                       //looks for key press
      gameOver = true;
      gameOverDone = true;
      
      buttons.add(new Button(this));                                 //creates the start button
      
      player.life = player.maxLife;                                  //gives the player their max life and then adds it to an array to make them appear visually
      
      
      for(int i = 0; i < player.life; i++) {
         lives.add(new Heart(this, i));
      }
     
   }
   
   public void gameStart() {
   
      
      for(int i = player.life; i < player.maxLife; i++) {
         lives.add(new Heart(this, i+1));       //lives don't have to be reset like everything else because it won't be moving much
      }
      
      player.life = player.maxLife;
      
      //I reset every thing that could kill the player
      aliens = new LinkedList<Alien>();
      aLasers = new LinkedList<Laser>();        //you don't want to lose a life when you reseting while an alien laser is coming at you
      buttons = new LinkedList<Button>();       //if you don't reset the buttons the game breaks because the buttons will never disappear
      
      buttons.add(new Button(this));            //adds the start and reset button to appear before and after the game
      waveNumber = 0;                           //resets the wave the player was on
      waveCounter = 0;                          //resets the timer to make the aliens spawn
      aliensSpawned = 0;                        //elminates the progress the player made on the waves
      score = 0;                                //resets the score
      hits = 0;                                 //resets the amount of hits the player got
      shoots = 0;                               //resets the shoots the player shoot
      blockedShots = 0;                         //resets the amount of player lasers blocked by alien lasers
      gameOver = true;                          //gameOver means no collisions should be running
      gameOverDone = true;                      //gameOverDone means all of the aliens are done exploding and the game is fully reset
      gameStarted = false;                      //the game hasn't started until the player shoots the start button
      
      
   }
   
   public void start() {                       //this makes the game start after the start button is hit
    
      waveNumber = 1;
                                                
      gameOver = false;
      gameOverDone = false;
      gameStarted = true;
      
   }
   
   public void startGameThread() {
      
      gameThread = new Thread(this);
      gameThread.start();
      //starts the run method that starts the game
   }
   
   public void run() {
      //this has everything that happens and is the game loop
      double drawInterval = 1000000000/fPS;
      double delta = 0;
      long lastTime = System.nanoTime();
      long currentTime;
      
      while(gameThread != null) {
         
         currentTime = System.nanoTime(); 
         delta += (currentTime - lastTime)/drawInterval; 
         lastTime = currentTime;
         //we want to update information
         
         if(delta >= 1) {  //currently has it stop when the player runs out of lives but Idk if this is the best way to do this
            update();
            repaint();
            delta = 0;
         }
         //and we want to draw the screen with the new information
      }
      
   }
   
   
   public void update() {
      
    
      if(keyC.reset) {
         gameStart();         //if r is pressed the game is reset
      }
      
      buttonUpdate();         //updates if the button is being hit and worries about game statis
      waveUpdate();           //controls the aliens spawning in
      alienUpdate();          //controls the aliens' movement and images
      updateAlienJumping();   //controls the aliens zig zag spawn animation
      laserUpdate();          //controls the lasers movement and images
      shootingUpdate();       //controls alien and player firing
      updateCollisions();     //controls collisions    
      player.update(this.g2); //controls the player's movement and images
      
      
   }
   
   public void paintComponent(Graphics g) {
      
      super.paintComponent(g);//calls the javax.swing frame to keep the graphics
      g2 = (Graphics2D)g;     //apparently graphics 2d is better than graphics
      drawAliens(g2);         //draws the aliens
      drawLives(g2);          //draws the player lives
      drawLasers(g2);         //draws all lasers
      player.draw(g2);        //draws the player
      drawButton(g2);         //draws the button
      drawInfo(g2);           //draws information like score and wave number
      g2.dispose();           //saves storage
   }
   
   public void updateAlienJumping() {
      int size = aliens.size();
      for(int i = 0; i < size; i++) {
         Alien alien = aliens.remove();
         if(aliens.peek() != null) {
            alien.moveRTL();        //this makes the aliens reroute to have an indirect way of traveling to their final destination
         }
         aliens.add(alien);
         
      }
                                    //this is the zig zag pattern that the aliens travel in
   }
  
   //button update
   public void buttonUpdate() {
      if(!gameOver && !gameOverDone) {
         for(int i = 0; i < buttons.size(); i++) {
            Button b = buttons.remove();
            b.show = false;
            buttons.add(b);
         }                          //if the game is over or hasn't started the button will appear as a play button
      }
      else if(gameOverDone) {       //the game finishes and the player dies the button will turn to a reset button
         for(int i = 0; i < buttons.size(); i++) {
            Button b = buttons.remove();
            b.show = true;
            if(gameStarted) {
               b.reset();
            }
            buttons.add(b);
         }
      }
   }
   //currently a wave counter 
   public void waveUpdate() {
      
      if(!gameOver) {
         waveCounter++;
         
         if(waveNumber == 1) {
            
            //spawns blue aliens
            if(waveCounter == waveSpawnDelay && aliensSpawned < tilesWidth) {
               aliens.add(at.add(0, 0, 1));
               aliensSpawned++;                   //spawns 12 blue aliens and at a rate of 20 frames or 3 every second (20 f /60 fps)
               waveCounter = 0;
            }
            
            else if(waveCounter >= waveSpawnDelay && aliens.size() == 0) {
               waveNumber++;                      //this spawns the next wave when all the aliens have died
               aliensSpawned = 0;
               waveCounter = 0;
            }
            
         }
         if(waveNumber == 2) {
            //spawns blue and red aliens
            if(waveCounter == waveSpawnDelay && aliensSpawned < tilesWidth * 2) {
               aliens.add(at.add(0, 0, 2));        //zero delay, blue alien, spawnpoint 2
               aliensSpawned++;                    //this adds two rows of blue aliens from spawnpoint 2
               waveCounter = 0;
            }
            else if(waveCounter == waveSpawnDelay && aliensSpawned < tilesWidth * 4) {
               aliens.add(at.add(0, 1, 3));
               aliensSpawned++;                    //this adds two rows of red aliens from spawnpoint 3
               waveCounter = 0;
            }
            else if(waveCounter >= waveSpawnDelay && aliens.size() == 0) {
               waveNumber++;
               aliensSpawned = 0;                  //when all the aliens die the next wave is summoned
               waveCounter = 0;
            }
           
         }
         if(waveNumber == 3) {
            
            if(waveCounter == waveSpawnDelay && aliensSpawned < tilesWidth) {
               aliens.add(at.add(0, 0, 5));
               aliensSpawned++;                    //this adds one row of blue aliens at spawnpoint 5
               waveCounter = 0;
            }
            else if(waveCounter == waveSpawnDelay && aliensSpawned < tilesWidth * 3) {
               aliens.add(at.add(0, 1, 6));
               aliensSpawned++;
               waveCounter = 0;                    //this spawns 2 rows of red aliens at spawnpoint 6
            }
            else if(waveCounter == waveSpawnDelay && aliensSpawned < tilesWidth * 4) {
               aliens.add(at.add(0, 2, 0));
               aliensSpawned++;                    //this spawns one row of green aliens from the top
               waveCounter = 0;
            }
            else if(waveCounter >= waveSpawnDelay && aliens.size() == 0) {
               g2.drawString("You Won!!!", screenWidth/2 - tileSize, tileSize*8);
               System.out.println("you won");      //supposedly this should show if you win but I've never made it this far
            }
         }
      }
      
   }
   
   //this updates the aliens
   public void alienUpdate() {
      
      
      int size = aliens.size();
      for(int i = 0; i < size; i++) {
         if(aliens.peek() != null) {
            aliens.peek().update();
            
            if(aliens.peek() != null) {
               Alien alien = aliens.remove();         //it has to check again if the alien is null because the update could make the alien become null
            
               if(gameOver && !gameOverDone) { 
                  alien.gameOver();                   //when the game ends the aliens explode
               }  
            
            
               aliens.add(alien);
            }
         }
        
      }
      if(gameOver) {
         gameOverDone = true;                          //when all the aliens explode the gameover has finished
      }
      
   }
   
   //the player lasers are cycled through and shoot when clicking space 
   public void laserUpdate() {
      
      //player lasers
      for(int i = 0; i < lasers.size(); i++) {
         
         if(lasers.peek() != null) {               //if I made lasers go in different directions or if I combined this laser.list with the player's lasers I could do seperate things by checking the direction of the lasers
            if(lasers.peek().y < 0) {
               lasers.remove();                    //if the laser goes off the screen it gets deleted
            }
            else {
               Laser laser = lasers.remove().update();
               if(laser != null) {
                  lasers.add(laser);               //if the laser is on the screen it gets updated
               }
            }
         }
      }
      
      //alien lasers this removes them if they are below the y area
      for(int i = 0; i < aLasers.size(); i++) {
         if(aLasers.peek().direction.equals("down")) {               //if I made lasers go in different directions or if I combined this laser.list with the player's lasers I could do seperate things by checking the direction of the lasers
            if(aLasers.peek().y > screenDepth) {
               aLasers.remove();                                     //the lasers get removed from the list when they are outside the screen
            }
            else {
               aLasers.add(aLasers.remove().update());               //the lasers on screen get updated and cycled through the list
            }
         }
      }
      
      
   }
   
   public void shootingUpdate() {
        
      //this is how the player shoots
      if(player.respawnTimer == 0 && player.life > 0) {
         
         if(laserDelay >= 30) { 
            if(keyC.spaceGo == true) {
            //trying to make the lasers shoot if this is clicked
               if(lasers.size() < player.maxLasers) {
                  lasers.add(new Laser(this, "up", 0, player.x, player.y));
                  
                  laserDelay = 0;
                  player.shoot();
                  keyC.spaceGo = false;
               }
            }
         }  
         laserDelay++;              //this is the part that allows the player to shoot only once every 20 frames
         
         //this is how the alien shoots
         int size = aliens.size();
         for(int i = 0; i < size; i++) {
            Alien alien = aliens.remove();
           
            if(alien != null && alien.alive && alien.fire == true) {
            
               if(alien.color.equals("blue")) {                //blue aliens fire blue lasers which are harder to see but they can be destroyed by shooting it
                  aLasers.add(new Laser(this, "down", 1, alien.x, alien.y + tileSize*3/2));
               }
               else {                                          //all other aliens shoot the red lasers which block player lasers and keep going
                  aLasers.add(new Laser(this, "down", 2, alien.x, alien.y + tileSize*3/2));
               }
               alien.fire = false;
            }
            
            aliens.add(alien);                                 //alien is cycled back
         }
         
      }
      
      
      
   }
   
   public void drawInfo(Graphics2D g2) {
      int realAccuracy = (int)((hits + blockedShots) * 1.0/shoots * 100);        //calculates real accuracy by using blocked shoots and counting them as hits
      int accuracy = (int)(hits * 1.0/shoots * 100);                             //accuracy is all the hits the player got
      if(g2 != null) {
         g2.setColor(Color.WHITE);
         g2.drawString("Score: " + score, screenWidth - tileSize*2, tileSize);   //draws the score the player has
         g2.drawString("Wave: " + waveNumber, screenWidth - tileSize * 2, tileSize*3/2);  //draws the player's current wave
         if(gameOverDone) {
            if(gameStarted) {                                                    //if the game is over it will pring out the final results
               String results = "Score: " + score + ", Accuracy: " + accuracy + "%, Real Accuracy: " + realAccuracy + "%";
               g2.drawString(results, screenWidth/4 + tileSize *1/2, tileSize*6);
            }                                                                    //gives a short instruction of the game
            g2.drawString("press r any time to reset", screenWidth/2 - tileSize*3/2, tileSize*8);
            g2.drawString("press space to shoot with wasd to move", screenWidth/2 - tileSize/5*12, tileSize * 9);
            g2.drawString("beat three waves of enemies to win", screenWidth/2 - tileSize*2, tileSize * 10);
         }
      }
   }
   
   public void drawButton(Graphics2D g2) {
      for(int i = 0; i < buttons.size(); i++) {
         Button b = buttons.remove();
         b.draw(g2);
         buttons.add(b);
      }                                                  //draws the button on the screen
   }
   
   public void drawAliens(Graphics2D g2) {
   
      int size = aliens.size();
      for(int i = 0; i < size; i++) {
         if(aliens.size() > 0) {                        //draws all the aliens on the screen even if they are dying but not if they are dead
            Alien alien = aliens.remove();
         
            if(alien != null && (alien.alive || alien.dying)) {
               alien.draw(g2);
               aliens.add(alien);
            }
         }
      }
   }
   
   public void drawLasers(Graphics2D g2) {
      
      int size = lasers.size();
      for(int i = 0; i < size; i++) {
         
         lasers.peek().draw(g2);          
         lasers.add(lasers.remove());                 //all lasers are drawn
         
      }
      for(int i = 0; i < aLasers.size(); i++) {
         
         aLasers.peek().draw(g2);
         aLasers.add(aLasers.remove());               //all lasers are drawn
         
      }
   }
   
   public void drawLives(Graphics2D g2) {
      for(int i = 0; i < player.life; i++) {
         lives.get(i).draw(g2);                       //draws the players lives
      }
   }
   
   public void updateCollisions() {
      
      if(!gameOverDone && !gameOver || aliens.size() == 0) {      //only has to check the button collisions when the game is over
         playerAlienCollision();       //Alien vs Player
         alienHit();                   //Alien vs Laser
         laserCollision();             //Laser vs Laser
         playerHit();                  //Player vs Laser
      }
      updateButtons();                 //Laser vs Button  
      
   }
   
   //alien vs laser
   public void alienHit() {
      
      int aSize = aliens.size();
      int lSize = lasers.size();
      
      for(int i = 0; i < aSize; i++) {
         for(int j = 0; j < lSize; j++) {
            if(aliens.size() > 0 && aliens.peek() != null) {
               
               Alien cAlien = aliens.remove();
               Laser cLaser = lasers.remove();
               
               if(cLaser != null && cAlien != null) {
               
                  if(comp.compare(cLaser, cAlien)) {
                     cLaser.hit();                      //checks for the laser alien collisions
                     cAlien.hit();
                     hits++;
                  } 
                  
                  
                  
               }
               
               lasers.add(cLaser);                      //cycles the lasers
               
               if(cAlien.alive || cAlien.dying) {
                  aliens.add(cAlien);                   //unless the alien is dead keep checking for collisions
               }
               
            }
         }  
      
      }
   }
   
   //laser vs laser
   public void laserCollision() {
      
      for(int i = 0; i < aLasers.size(); i++) {
         for(int j = 0; j < lasers.size(); j++) {
         
            Laser pLaser = lasers.remove();
            Laser aLaser = aLasers.remove();
            
            if(aLaser != null && pLaser != null) {
               if(comp.compare(pLaser,aLaser)) {
                  if(aLaser.color > 1) {
                     pLaser.hit();           //red lasers block player's lasers and keep going
                  }
                  else {                     // .hit makes the laser disappear
                     pLaser.hit();
                     aLaser.hit();           //blue lasers block the player's lasers but they are also blocked in the process 
                  }
                  blockedShots++;            //updates random stats
               }
               lasers.add(pLaser);
               aLasers.add(aLaser);
            }
         }
      }
      
   }
   //player vs Laser
   public void playerHit() {
      
      for(int i = 0; i < aLasers.size(); i++) {
         Laser aLaser = aLasers.remove();
         if(aLaser != null) {
            if(comp.compare(aLaser, player) ) {          //checks to see if the player is running into any alien lasers
               player.hit();
               aLaser.hit();
            }
         }
         aLasers.add(aLaser);                            //cycles lasers
      }  
     
      
   }

   //player vs Alien
   public void playerAlienCollision() {
      
      int size = aliens.size();
      for(int i = 0; i < size; i++) {
         
         if(aliens.size() > 0 && aliens.peek() != null) {
            Alien alien = aliens.remove();
         
            if(alien.alive && player.alive) {
            
               if(comp.compare(player, alien)) {
                  if(!alien.dying) {
                     alien.hit();
                  }                                         //checks to see if the player and the aliens are touching
                  player.hit();
               }
            
               aliens.add(alien);                           //cycles aliens
            }
            if(alien.dying) {
               aliens.add(alien);                           //if the alien is dying keep the alien but don't let it kill the player
            }
         }
      }
      
   }
   
   //buttons
   public void updateButtons() {
     
      if(gameOver && gameOverDone) {                                 //the game has to be over for the button to appear and so it shouldn't be able to get hit otherwise
         
         int bSize = buttons.size();
         int lSize = lasers.size();                                  //checks to see if the player lasers are hitting the start button
         
         for(int i = 0; i < bSize; i++) {
         
            Button button = buttons.remove();
            
            for(int j = 0; j < lSize; j++) {
            
               Laser laser = lasers.remove();
               
               if(comp.compare(laser, button)) {
                  laser.hit();
                  start();                                           //this is the only way to start the game
               }
               
               if(gameOver) {
                  lasers.add(laser);
               }
            }
            buttons.add(button);                                     //cycles the button
         }
      }
   }
}
