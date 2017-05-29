import java.awt.Dimension;

import javax.swing.*;

/* Inner of Elevators */
public class elevator_inner
{
   private static ImageIcon bg[] = new ImageIcon[11];	// Images for Inner Elevators
   private static JLayeredPane lp[] = new JLayeredPane[5];
   private static JPanel elevator[] = new JPanel[5];	// Panels
   private static JLabel inner[] = new JLabel[5];

   private static int number_people[] = new int[5];	// Number of peoples in Nth Elevator
   
   /* Initialize 5 Panels */
   public static void init(JPanel a, JPanel b, JPanel c, JPanel d, JPanel e)
   {   
      elevator[0] = c;
      elevator[1] = d;
      elevator[2] = a;
      elevator[3] = b;
      elevator[4] = e;
      
      for (int i=0; i<5; i=i+1)
      {
         lp[i] = new JLayeredPane();
         lp[i].setPreferredSize(new Dimension(init.inner_width, init.inner_height));
         lp[i].setVisible(true);
         elevator[i].add(lp[i], "TOP");
      }
      
      for (int i=0; i<11; i=i+1)
      {
         switch (i)
         {
            case 0:
               bg[0] = new ImageIcon(init.sub_background_src+"_off.png");
               break;
            default:
               bg[i] = new ImageIcon(init.sub_background_src+"_"+i+".png");
               break;
         }
      }

      for (int i=0; i<5; i=i+1)
      {
         number_people[i] = 0;
         inner[i] = new JLabel(bg[0]);	//,JLabel.LEFT);
         lp[i].add(inner[i],"TOP", new Integer(0));
         inner[i].setBounds(0, 0, init.inner_width, init.inner_height);
      }

   }
   
   /* When a person get out */
   public static void out(int i)
   {
      number_people[i] = number_people[i] - 1;
      inner[i].setIcon(bg[number_people[i]]);
      inner[i].setBounds(0, 0, init.inner_width, init.inner_height);
   }

   /* When a person get in */
   public static void in(int i)
   {
      number_people[i] = number_people[i] + 1;
      inner[i].setIcon(bg[number_people[i]]);
      inner[i].setBounds(0, 0, init.inner_width, init.inner_height);
   }
   
   /* getter method for 5th Panel */
   public static JPanel get5th()
   {
      return elevator[4];
   }
}