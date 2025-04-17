
//This class should take objects and then compare to see if the x and y intersects.

public class Compare {

   Display db;
   int l1, r1, b1, t1, l2, r2, b2, t2; //the positions of each corner of the two objects
   
   public Compare(Display db) {
      this.db = db;                    //this just makes a comparer constructor so it can be an object
   }
   
   
   public boolean compare(Entity one, Entity two) {
   
      //this compares to objects
      l1 = one.hitBoxX;                //left
      r1 = one.hitBoxXr;               //right
      //one's dimensions
      b1 = one.hitBoxYb;               //bottom
      t1 = one.hitBoxY;                //top   
      
      l2 = two.hitBoxX;                //left
      r2 = two.hitBoxXr;               //right
      //two's dimensions
      t2 = two.hitBoxY;                //top
      b2 = two.hitBoxYb;               //bottom
      
      if(one.alive == true && two.alive == true) {
      
         if((((l1 <= l2) && (l2 <= r1)) || ((l1 <= r2) && (r2 <= r1))) || (((l2 <= l1) && (l1 <= r2)) || ((l2 <= r1) && (r1 <= r2)))) {    // left < right
         //the two characters have at least one pixel of eachothers x
           
            if(((t2 <= b1 && b1 <= b2) || (t2 <= t1 && t1 <= b2)) || ((t1 <= b2 && b2 <= b1) || (t1 <= t2 && t2 <= b1))) { // top < bottom  
               //at least one part of the two y's have to be within eachother
               return true;
               
            }
            return false;
         
         }
         
      }
      
      return false;
      
   }
  
   


}