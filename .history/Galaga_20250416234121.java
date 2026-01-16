
//this is the class that runs the game

import javax.swing.JFrame;
import java.awt.*;

public class Galaga {

   public static void main(String[] args) {

      JFrame window = new JFrame(); // makes a window that will hold the display panel
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      window.setResizable(false); // makes the display usable
      window.setTitle("Galaga");

      Display panel = new Display(); // creates the display panel

      window.add(panel); // makes the display panel a window
      window.pack();

      window.setLocationRelativeTo(null); // the window is created in the middle of the screen
      window.setVisible(true); // this makes the window and does everything that makes it the size and look
                               // that we want

      panel.startGameThread(); // starts the game
   }

}