import javax.swing.*;
import javax.swing.border.MatteBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/* Class that provides effective elevator to Objects of person */
public class scheduler {
      int mode = 1;//Normal mode : 1, Scrooge mode : 2
      mover mov;
      Main_GUI gui = new Main_GUI(this);
      Setting set = new Setting();
      remoteServer re = new remoteServer(this);
      public void nomalscheduling(int input, int des, int x) {	// Using only Shortest Distance
      int min = 10000;
      int minIndex = -1;
      int wantState = 0;
      int hereState = 0;

      int t = 0;
      for (int i = 0; i <= 4; i++) {
      
      
         int compare = Math.abs(gui.floor[input - 1] - gui.obele[i].location);




         if (i == 3) {
            if (input > 4 || des > 4)
               wantState = -3;

         }
         if (i == 4) {
            if (input < 5 || des < 5)
               wantState = -3;
         }
         if (wantState != -3) {
            if (min > compare) {	// select the nearest Elevator
               min = compare;
               minIndex = i;
            }
   
         }
      }
      //create person2 objects
      person2 p = new person2(gui.floor[des - 1], gui.floor[input - 1], gui.content, gui.lp, gui.obele[minIndex], des - 1, input - 1, x);

      mov.insertP(p);
      changeMyele change = new changeMyele();
      change.changeWait(1, input-1, gui.obele[minIndex]);


      if (gui.floor[input - 1] < gui.obele[minIndex].location)
         hereState = 1;
      else if (gui.floor[input - 1] > gui.obele[minIndex].location)
         hereState = -1;
      else if (gui.floor[input - 1] == gui.obele[minIndex].location)
         hereState = 0;
      // considering relative location between elevator and person, set floor into elevator destination array
      if (hereState == 1) {
         gui.obele[minIndex].insertup(gui.floor[input - 1]);
      } else if (hereState == -1) {
         gui.obele[minIndex].insertDown(gui.floor[input - 1]);
      } else {
         if (gui.obele[minIndex].state == 0)
            gui.obele[minIndex].destination = gui.floor[input - 1];
      }
   }
   public void scheduling(int input, int des, int x) {
      int min = 10000;
      int minIndex = -1;

      int wantState = 0;
      int hereState = 0;

      int t = 0;
      for (int i = 0; i <= 4; i++) {

         int compare = Math.abs(gui.floor[input - 1] - gui.obele[i].location);
      
         // If person wants to move just short floor, give more priority to 4th and 5th elevator
         if (i == 3) {
            if (input < 5 && des < 5)
               compare = compare - 250;

         }
         if (i == 4) {
            if (input > 4 && des > 4)
               compare = compare - 250;
         }
         int totalwait = totalhf(gui.obele[i]);
         int usability; 
         // Estimate usibility of each elavator, give more priority to relatively idle elevator
         if (gui.obele[i].usernum + totalwait > 12)
            usability = 1000;
         else if(gui.obele[i].usernum + totalwait > 8)
            usability = 600;
         else if (gui.obele[i].usernum + totalwait > 6)
            usability = 300;
         else if (gui.obele[i].usernum + totalwait > 4)
            usability = 200;
         else if(gui.obele[i].usernum + totalwait > 2)
            usability = 130;
         else
            usability =  gui.obele[i].usernum + totalwait;
         compare = this.calcul(hereState, wantState, compare, usability, t);

         if (i == 3) {// If Person wants to move 5th ~ 8th floor, except 4th Elevator using setting wantstate by -3
            if (input > 4 || des > 4)
               wantState = -3;

         }
         if (i == 4) {// If Person wants to move 1st ~ 4th floor, except 4th Elevator using setting wantstate by -3
            if (input < 5 || des < 5)
               wantState = -3;
         }
         if (wantState != -3) {
            if (min > compare) {//Allocate elavator that has the least output value of calcul method
               min = compare;
               minIndex = i;
            }

         }
      }
      // Creates person2 objects; parameter is current location, destination location and allocated elevator of each objects
      person2 p = new person2(gui.floor[des - 1], gui.floor[input - 1], gui.content, gui.lp, gui.obele[minIndex], des - 1, input - 1, x);
      mov.insertP(p);
      
      gui.jdbc.updateToServer(gui.obele[minIndex].name, input,gui.obele[minIndex].setFloorIndex);
      gui.obele[minIndex].update(input);

      gui.obele[minIndex].hereWait[input - 1]++;
      if (gui.floor[input - 1] < gui.obele[minIndex].location)// Larger Y point than elevator means elevator is under the person
         hereState = 1;
      else if (gui.floor[input - 1] > gui.obele[minIndex].location)
         hereState = -1;
      else if (gui.floor[input - 1] == gui.obele[minIndex].location)
         hereState = 0;
      
      if (hereState == 1) {// insert floor of person into upward or downward floor array
         gui.obele[minIndex].insertup(gui.floor[input - 1]);
      } else if (hereState == -1) {
         gui.obele[minIndex].insertDown(gui.floor[input - 1]);
      } else {
         if (gui.obele[minIndex].state == 0)
            gui.obele[minIndex].destination = gui.floor[input - 1];
      }
   }

   /* returns calculated value using direction, distance and usability */
   public int calcul(int here, int want, int Distance, int numOfUser, int eleState) {
      int result;
      int D = Distance;

      if (here == eleState || eleState == 0) {// If elevator's movement is the same with direction to person
         if (want == eleState)// If person wants the direction of the elevator
            result = 95 + D + numOfUser;
         else {// If person wants other direction of the elevator
            result = 100 + D + numOfUser;
         }
      } else // If elevator's movement isn't forward direction to person
         result = 250 + D + numOfUser;

      return result;

   }
   /* calculating waiting people in total floors that allocated to input elevator */
   public int totalhf(eleb el) {
      int result = 0;
      for (int i = 0; i < 8; i++)
         result = result + el.hereWait[i];
      return result;
   }

   public static void main(String args[]) {
      scheduler e = new scheduler();
      
   }

}