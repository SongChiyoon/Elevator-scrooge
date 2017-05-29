import java.awt.Image;
import java.util.*;

import javax.swing.*;
/* Use the other class and thread to implement movement of elevator. */
public class mover extends Thread {

   eleb[] eleList = new eleb[5];
   // variable representing 
   int hf = -1; // floor index when elevator is arrived at the predicted location. 
   scheduler mysch;
   private ImageIcon elevator[];

   ImageIcon outp = new ImageIcon("out.png");

   public mover(eleb[] obeleb, scheduler sc) {
      this.elevator = new ImageIcon[4];

      for (int j = 0; j < 5; j = j + 1) {
         init.i[j] = new JLabel(init.indicator, JLabel.CENTER);
         init.lp.add(init.i[j], new Integer(2));
      }

      elevator[0] = new ImageIcon(init.elevator_src + ".png");
      elevator[1] = new ImageIcon(init.elevator_src + "2.png");
      elevator[2] = new ImageIcon(init.elevator_src + "3.png");
      elevator[3] = new ImageIcon(init.elevator_src + "4.png");

      eleList[0] = obeleb[0];
      eleList[1] = obeleb[1];
      eleList[2] = obeleb[2];
      eleList[3] = obeleb[3];
      eleList[4] = obeleb[4];
      mysch = sc;

      for (int i = 0; i < 5; i = i + 1)
         init.i[i].setBounds(eleList[i].X - init.indicator_width / 5, eleList[i].location + init.indicator_width / 5,
               init.indicator_width, init.indicator_height);

   }

   public void run() {
      eleList[0].state = 0;
      eleList[1].state = 0;
      eleList[2].state = 0;
      eleList[3].state = 0;
      eleList[4].state = 0;
      while (true) {

         try {
            Thread.sleep(6);
         } catch (Exception e) {
         }
         elevatorMove();
         if (mysch.mode == 2)
            goSetFloor();
      }
   }
   public void insertP(person2 pp) {
      personMoveT pm = new personMoveT(pp, mysch);
      pm.start();

   }
   // The method to move each elevators. 
   public void elevatorMove() {
      int chch = 0;
      for (int i = 0; i <= 4; i++) {
// Store the index of floor to hf when the position of elevator is equal to floor position. 
         hf = -1;
         for (int k = 0; k < 8; k++) {
            if (eleList[i].location == eleList[i].floor[k])
               hf = k;

         }
         if (hf != -1)
            eleList[i].chch = eleList[i].hereWait[hf];

         if (eleList[i].chch == 0) {
// update the destination of each elevators. 
            if (eleList[i].state == 1)
               eleList[i].merge(1);
            else if (eleList[i].state == -1)
               eleList[i].merge(-1);
            else if (eleList[i].state == 0) {
               if (eleList[i].descdown == 0) {
                  eleList[i].merge(1);
               } else if (eleList[i].descup == 0) {
                  eleList[i].merge(-1);
               }
            } else {

               if (eleList[i].prvState == 1) {
                  if (eleList[i].descup == 0) {
                     eleList[i].merge(-1);
                  } else
                     eleList[i].merge(1);
               } else {
                  if (eleList[i].descdown == 0)
                     eleList[i].merge(1);
                  else
                     eleList[i].merge(-1);
               }
            }
         }
         if (eleList[i].descount != 0) {
            eleList[i].destination = eleList[i].des[0];
         }

         if (eleList[i].destination != -1) {

            if (eleList[i].closecount <= 2000 && eleList[i].close == false
                  && (hf != -1 && eleList[i].hereWait[hf] == 0)) {
// set the timer to show the moving door of elevator. 

               eleList[i].closecount += 20;
               if (eleList[i].closecount <= 350)
                  eleList[i].me.setBounds(eleList[i].X, eleList[i].location, 230, 120);
               else if (eleList[i].closecount > 350 && eleList[i].closecount <= 700) {
                  eleList[i].me.setIcon(elevator[3]);
                  eleList[i].me.setBounds(eleList[i].X, eleList[i].location, 230, 120); 

               } else if (eleList[i].closecount > 700 && eleList[i].closecount <= 1000) {
                  eleList[i].me.setIcon(elevator[2]);
                  eleList[i].me.setBounds(eleList[i].X, eleList[i].location, 230, 120);

               } else if (eleList[i].closecount > 1000 && eleList[i].closecount <= 1350) {
                  eleList[i].me.setIcon(elevator[1]);
                  eleList[i].me.setBounds(eleList[i].X, eleList[i].location, 230, 120);

               } else if (eleList[i].closecount > 1350 && eleList[i].closecount <= 1700) {
                  eleList[i].me.setIcon(elevator[1]);
                  eleList[i].me.setBounds(eleList[i].X, eleList[i].location, 230, 120);
               } else if (eleList[i].closecount > 1700 && eleList[i].closecount <= 2000) {
                  eleList[i].me.setIcon(elevator[0]);
                  eleList[i].me.setBounds(eleList[i].X, eleList[i].location, 230, 120);
                  if (eleList[i].closecount >= 2000) {
		// When the door of elevator is closed completely. 
                     eleList[i].me.setIcon(elevator[0]);
                     eleList[i].closecount = 0;
                     eleList[i].state = 0;
                     eleList[i].close = true;
                  }
               }

            } else if (eleList[i].state == 2 && (eleList[i].close == true)
                  && (eleList[i].usernum != 0 || mysch.totalhf(eleList[i]) != 0)) {
		// 
		// the part which repsents opening door when elevators is arrived at destination. 

               eleList[i].opencount += 20;
               if (eleList[i].opencount <= 2000) {
                  if (eleList[i].opencount <= 350)
                     eleList[i].me.setBounds(eleList[i].X, eleList[i].location, 230, 120);
                  else if (eleList[i].opencount > 350 && eleList[i].opencount <= 700) {
                     eleList[i].me.setIcon(elevator[0]);
                     eleList[i].me.setBounds(eleList[i].X, eleList[i].location, 230, 120);

                  } else if (eleList[i].opencount > 700 && eleList[i].opencount <= 1000) {
                     eleList[i].me.setIcon(elevator[0]);
                     eleList[i].me.setBounds(eleList[i].X, eleList[i].location, 230, 120); 

                  } else if (eleList[i].opencount > 1000 && eleList[i].opencount <= 1350) {
                     eleList[i].me.setIcon(elevator[1]);
                     eleList[i].me.setBounds(eleList[i].X, eleList[i].location, 230, 120); 

                  } else if (eleList[i].opencount > 1350 && eleList[i].opencount <= 1700) {
                     eleList[i].me.setIcon(elevator[2]);
                     eleList[i].me.setBounds(eleList[i].X, eleList[i].location, 230, 120);

                  } else if (eleList[i].opencount > 1700 && eleList[i].opencount <= 2000) {
                     eleList[i].me.setIcon(elevator[3]);
                     eleList[i].me.setBounds(eleList[i].X, eleList[i].location, 230, 120);
                     if (eleList[i].opencount >= 2000) {
                        eleList[i].state = 3;
                        eleList[i].close = false;
                        eleList[i].opencount = 0;
                     }

                  }
               }

            } else if (eleList[i].location < eleList[i].destination && eleList[i].close == true) {
		// If the position of elevator is less than the position of destination elvator, elevator go down. 
               eleList[i].state = -1;
               eleList[i].prvState = eleList[i].state;
               eleList[i].location = eleList[i].location + 1;
               eleList[i].me.setBounds(eleList[i].X, eleList[i].location, 230, 120);
               init.i[i].setBounds(eleList[i].X, eleList[i].location, 0, 0);
               if (eleList[i].location == eleList[i].destination) {
                  init.i[i].setBounds(eleList[i].X - init.indicator_width / 5,
                        eleList[i].location + init.indicator_width / 5, init.indicator_width,
                        init.indicator_height);

                  eleList[i].prvState = eleList[i].state;
                  eleList[i].state = 2;
                  eleList[i].deletion(-1);
               }
            } else if (eleList[i].location > eleList[i].destination && eleList[i].close == true) {
              // If the position of elevator is larger than the position of destination elvator, elevator go up. 
               eleList[i].state = 1;
               eleList[i].prvState = eleList[i].state;
               eleList[i].location = eleList[i].location - 1;
               eleList[i].me.setBounds(eleList[i].X, eleList[i].location, 230, 120);
               init.i[i].setBounds(eleList[i].X, eleList[i].location, 0, 0);
               if (eleList[i].location == eleList[i].destination) {
                  init.i[i].setBounds(eleList[i].X - init.indicator_width / 5,
                        eleList[i].location + init.indicator_width / 5, init.indicator_width,
                        init.indicator_height);

                  eleList[i].state = 2;
                  // eleList[i].chch = 0;
                  eleList[i].deletion(1);
                  eleList[i].merge(1);
               }
            } else
            {// If the position of destination is equal to the position of elevator, the state of elevator is stopped or opening door (closing door)  
               if (eleList[i].descount == 0 && hf != -1 && eleList[i].hereWait[hf] != 0
                     && eleList[i].close == true)
                  eleList[i].state = 2;
               if (hf != -1 && eleList[i].usernum == 0 && eleList[i].descount == 0 && mysch.totalhf(eleList[i]) == 0
                     && eleList[i].close == true) {

                  eleList[i].state = 0;

                  eleList[i].destination = -1;
                  eleList[i].prvState = 0;
                  eleList[i].descount = 0;
                  eleList[i].descup = 0;
                  eleList[i].descdown = 0;
                  eleList[i].chch = 0;
                  eleList[i].close = true;

               } else if (eleList[i].state == 2) {
                  if (eleList[i].prvState == 1) {

                     if (eleList[i].descup == 0)
                        eleList[i].merge(-1);
                     else
                        eleList[i].merge(1);

                  } else if (eleList[i].prvState == -1) {
                     if (eleList[i].descdown == 0)
                        eleList[i].merge(1);
                     else
                        eleList[i].merge(-1);
                  }
               }
            }
         }

      }
   }
   // All of the elevators are moved to the expected floor(optimal) by referencing the information of database when the state of elevators are idle. 
   public void goSetFloor() {
      int count = 0;
      for (int i = 0; i <= 4; i++)
         if (eleList[i].usernum ==0 && mysch.totalhf(eleList[i])==0)
            count++;
      if (count == 5) {
         for (int i = 0; i < 5; i++) {
            if (eleList[i].location != eleList[i].MySettingFloor) {
               if (eleList[i].location < eleList[i].MySettingFloor) {
                  eleList[i].insertDown(eleList[i].MySettingFloor);
                  eleList[i].merge(-1);
               } else {
                  eleList[i].insertup(eleList[i].MySettingFloor);
                  eleList[i].merge(1);

               }
            }
         }
      }
   }

}