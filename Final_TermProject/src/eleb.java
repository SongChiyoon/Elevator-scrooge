import javax.swing.JLabel;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class eleb {
   public int name; // The name of elevator
   public int destination = -1;
   public int location; // y point. 
   public int X; // x point. 
   public int usernum; // The number of persons in the elevator. 
   public int state; // 1 : up -1 : down, 0 : stop, 2 : stop and close ,  open :  2 -> 3
   public int opencount = 0, closecount = 0; // -direction
   public int prvState = 0;// variable shows the previous movement direction when elevator is arrived special place. 
   int desup[] = new int[100];// destination array which goes toward upper floor. 
   int desdown[] = new int[100];// destination array which goes toward lower floor. 
   int des[] = new int[200];// The merge array which goes toward upper and lower floor. 
   int descount = 0;//The count od des[]. 
   int descup = 0; //The cound of desup[]. 
   int descdown = 0;// The cound of desdown[]
   int hereWait[] = {0,0,0,0,0,0,0,0};
   int temp = 0;
   int chch = 0;
   int MySettingFloor;// The point of expected floor
   double setFloorIndex;// The point of expected floor index. 
   
   ArrayList<person2> myperson = new ArrayList<person2>();
   private static final int floor_height = 88;
   private static final int first_floor = 592;
   public int floor[] = { first_floor, // first floor. 
         first_floor - floor_height, // 2
         first_floor - floor_height * 2, // 3
         first_floor - floor_height * 3 + 8, // 4
         first_floor - floor_height * 4 + 8, first_floor - floor_height * 5 + 8, first_floor - floor_height * 6 + 10,
         first_floor - floor_height * 7 + 14 };
   JLabel me;
   JLabel movme;
   public boolean close = true;

   public eleb(double index, int i, JLabel jLabel, int j, int i2) {
      me = jLabel;
      location = i;
      X = j;
      name = i2;
      location = i;
      setFloorIndex = index;
      MySettingFloor = floor[(int)setFloorIndex -1];

      
   }
   public void update(int Fl) {//expected floor update
      double a = 0.3;
      double Floor = setFloorIndex;
      Floor = Floor * (1 - a) + a * Fl;
      setFloorIndex = Floor;
      MySettingFloor = floor[(int)setFloorIndex-1];
   }

   // Delete the index of person when person is allocated to new elevator by scheduler 
   // Determine deleting the some floor by using checking method. 
   public void deletionindex(int hf, person2 ppp) {
      int deletekey = floor[hf];
      int callstate;
      if (location > deletekey) {
         callstate = 1;
         for (int i = 0; i < descup; i++) {
            if (deletekey == desup[i]) {
               for (int j = i; j < descup - 1; j++)
                  desup[j] = desup[j + 1];
               break;
            }
         }
      } else {
         callstate = -1;
         for (int i = 0; i < descdown; i++) {
            if (deletekey == desdown[i]) {
               for (int j = i; j < descdown - 1; j++)
                  desdown[j] = desdown[j + 1];
               break;
            }
         }
      }

      if (state == 2 || state == 3) {
         merge(prvState);
      } else {
         merge(state);
      }
   }

   public void insertPlist(person2 ppp) {
      myperson.add(ppp);
   }

   public void deletePlist(person2 ppp) {
      myperson.remove(ppp);
   }

   public boolean checking(int num) {
      boolean checkresult = false;
      for (int i = 0; i < myperson.size(); i++) {
         if (myperson.get(i).in == false) {
            if (myperson.get(i).hf == num) {
               checkresult = true;
               break;
            }
         } else {
            if (myperson.get(i).f == num) {
               checkresult = true;
               break;
            }
         }
      }
      return checkresult;

   }
   // When destination points go down, it orders the desdown array by ascending order. 
   public void insertDown(int newd) {
      if (descdown == 0) {
         desdown[0] = newd;
         descdown++;
         return;
      }

      if (newd < desdown[0]) {
         for (int i = descdown; i > 0; i--)
            desdown[i] = desdown[i - 1];
         desdown[0] = newd;
         descdown++;
         return;
      }
      if (newd > desdown[descdown - 1]) {
         desdown[descdown] = newd;
         descdown++;
         return;
      }
      for (int i = 0; i < descdown; i++) {
         if (desdown[i] == newd)
            break;
         if (desdown[i] > newd) {

            for (int j = descdown; j > i; j--) {
               desdown[j] = desdown[j - 1];
            }
            desdown[i] = newd;
            descdown++;
            return;
         }
      }
   }

   // order the desup array by descending order because the higher floor has a lower y value. 
   public void insertup(int newd) {
      if (descup == 0) {
         desup[0] = newd;
         descup++;
         return;
      }

      if (newd > desup[0]) {
         for (int i = descup; i > 0; i--)
            desup[i] = desup[i - 1];
         desup[0] = newd;
         descup++;
         return;
      }
      if (desup[descup - 1] > newd) {
         desup[descup] = newd;
         descup++;
         return;
      }
      for (int i = 0; i < descup; i++) {
         if (desup[i] == newd) //Duplicated value is not inserted.
            break;
         if (desup[i] < newd) {

            for (int j = descup; j > i; j--) {
               desup[j] = desup[j - 1];
            }
            desup[i] = newd;
            descup++;
            return;
         }
      }
   }
// Delete the floor when elevator is arrived at destination. 
   public void deletion(int st) {
      if (st == 1) {
         descup--;
         for (int i = 0; i < descup; i++)
            desup[i] = desup[i + 1];

         if (descup != 0)
            merge(st);
         else if (descdown != 0)
            merge(-1);
         else
            merge(0);
      } else {
         descdown--;
         for (int i = 0; i < descdown; i++)
            desdown[i] = desdown[i + 1];
         if (descdown != 0)
            merge(st);
         else if (descup != 0)
            merge(1);
         else
            merge(0);
      }

   }

   public void merge(int st) {
// It merges destination which go up and down direction. 
      if (st == 1) {
// If the elevator go up,the element of desdown array is merged to desup array in the rear of desup array.
// The other case is executed diversely.  
         for (int i = 0; i < descup; i++)
            des[i] = desup[i];
         for (int j = descup; j < descdown + descup; j++)
            des[j] = desdown[j];
      } else if (st == -1) {//The opposite case. 
         for (int i = 0; i < descdown; i++)
            des[i] = desdown[i];
         for (int j = descdown; j < descup + descdown; j++)
            des[j] = desup[j];
      }

      descount = descup + descdown;
   }

}