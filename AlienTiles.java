
public class AlienTiles extends Entity {                          //this contains all aliens in a 2D array to ease aliens to go to their correct final destination

   Alien[][] alienTiles;
   Display db;
   int rows;
   int columns;
   
   public AlienTiles(Display db) {
      
      this.db = db;                                               //keeps the display
      rows = 5;                                                   //I cap out the amount of rows at 5
      columns = db.tilesWidth;
      alienTiles = new Alien[rows][db.tilesWidth];                //I basically make the 2D array an object for simplification
      
   }
   
   public Alien add(int delay, int aType, int spawnpoint) {       //alien type is blue 0, red 1, and green 2
      
      for(int r = 0; r < rows; r++) {
         for(int c = 0; c < columns; c++) {
         
            if(empty(r,c)) {
               Alien alien = new Alien(db, spawnpoint, delay, aType);         //checks all the spots in the 2D array and the first empty spot it finds it creates an alien 
            
               alien.finalDestination(c*db.tileSize, (r + 2) * db.tileSize);
               alienTiles[r][c] = alien;                                      //sets its final destination and then returns the alien in the list
               return alienTiles[r][c]; 
               
            }
            
         }
         
      }
      return new Alien(false);                                                //if it can't find a spot for the alien to go it will create a false alien which gets deleted quickly after
      
   }
   
   public boolean empty(int r, int c) {                                       //checks to see if there is a live alien in the 2D array at the spot specified
      if(alienTiles[r][c] != null) {
         return !alienTiles[r][c].alive;
      }
      return true;
   }
   
   public boolean emptyRow(int r) {                                           //didn't end up using this but this just checks if a certain row is empty
      
      for(int c = 0; c < alienTiles[r].length; c++) {                         //I was going to use it create a harder wave of enemies that would require you to kill them row at a time to prevent collumn focusing
         if(alienTiles[r][c].alive) {
            return false;
         }
      }
      return true;
   }
   
}