
//this listens to the keypresses of the user
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyController implements KeyListener {
   
   public boolean upHold, downHold, rightHold, leftHold, spaceGo, reset;
   
   
   public void keyTyped(KeyEvent e) {
      //this is forced to be here but I don't use it
   }
   
   public void keyPressed(KeyEvent e) {      //when keys are pressed it will detect what key was pressed
      
      int code = e.getKeyCode();
      
      if(code == KeyEvent.VK_W) {            //w moves the player up
         upHold = true; 
      }  
      if(code == KeyEvent.VK_S) {            //s moves the player down
         downHold = true;
      }
      if(code == KeyEvent.VK_D) {            //d moves the player to the right
         rightHold = true;
      }
      if(code == KeyEvent.VK_A) {            //a moves the player to the left
         leftHold = true;
      }
      if(code == 32) {                       //spacebar the player shoots
         spaceGo = true;
      }
      if(code == KeyEvent.VK_R) {            //r resets the game
         reset = true;
      }
      
      
      
   }
   
   public void keyReleased(KeyEvent e) {     //when keys are released it detects what key was released
      
      int code = e.getKeyCode();
      
      if(code == KeyEvent.VK_W) {            //w player stops going up
         upHold = false;
      }
      if(code == KeyEvent.VK_S) {            //s the player stops going down
         downHold = false;
      }
      if(code == KeyEvent.VK_D) {            //d the player stops going to the right
         rightHold = false;
      }
      if(code == KeyEvent.VK_A) {            //a the player stops going to the left
         leftHold = false;
      }
         
      if(code == 32) {                       //spacebar the player stops shooting
         spaceGo = false;
      }
      if(code == KeyEvent.VK_R) {            //r stops reseting the game
         reset = false;
      }
   }
   
}