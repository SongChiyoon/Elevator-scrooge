import java.awt.Container;
import java.awt.Font;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

//  It stores the information about person. 
public class person2 {
  public int destination;   // Destination Floor
   public int floor;   // Departure Floor
   public boolean in;   // Whether Person get in or not
   public JLabel p;
   public eleb myele;
   public int hereState = 0;
   public int px;   // X point of person
   public int f;
   public int hf;
   public int out = 0;
   public int checkTime = 1000;
   public int speed;
   public Random ran = new Random();
   public boolean stop = false, constop = false;
   public Container body;
   public JLayeredPane back;
   public ImageIcon imageIcon = new ImageIcon("icon.png");

   /* Character ImageIcon */
   private ImageIcon charset[][] = new ImageIcon[4][4]; // Character Image Set[Direction][Movement]
   private int direction;
   private int movement;
   private int movetimer;

   public person2(int des, int floor, Container body, JLayeredPane lp, eleb ele, int desf, int hef, int x) {
      this.myele = ele;
      this.floor = floor;
      this.destination = des;
      this.body = body;
      back = lp;
      f = desf;
      hf = hef;
      in = false;
      px = x;
      this.getSpeed();
      this.init();
   }

   private void init() {
      this.set_char("human");
      this.direction = 0;
      this.movement = 0;
      this.movetimer = 0;

      p = new JLabel(charset[0][0], JLabel.LEFT);
      p.setLayout(null);
      p.setBounds(px, floor, 120, 120);
      back.add(p, new Integer(4));
   
   }

  // Initialize the image of person. 
   public void set_char(String filename)
   {
      String direction[] = new String[4];
      direction[0] = "_up";
      direction[1] = "_down";
      direction[2] = "_left";
      direction[3] = "_right";
      String movement[] = new String[4];
      movement[0] = "";
      movement[1] = "2";
      movement[2] = "3";
      movement[3] = "4";

      String extend = ".png";

      for (int i = 0; i < 4; i = i + 1)
         for (int j = 0; j < 4; j = j + 1)
            this.charset[i][j] = new ImageIcon(filename + direction[i] + movement[j] + extend);
   }

   // change the image of person when the person waits elevators. 
   public void stop() {
      this.movement = 0;
      this.movetimer = 0;
      p.setIcon(this.charset[direction][movement]);
      this.p.setBounds(this.px, floor, 100, 100);
   }

   public void stop_wait() {
      this.direction = 1;
      this.movement = 0;
      this.movetimer = 0;
      p.setIcon(this.charset[direction][movement]);
      this.p.setBounds(this.px, floor, 100, 100);
   }
   // moves the person to the left direction. 
   public void move_left() {
      if (this.direction == 2) {
         if (this.movetimer > 20) {
            this.movetimer = 0;
            this.movement = (this.movement + 1) % 4;
         } else
            this.movetimer = this.movetimer + 1;
      } else {
         this.direction = 2;
         this.movement = 1;
         this.movetimer = 0;
      }
      p.setIcon(this.charset[direction][movement]);

      this.px = this.px - 1;
      this.setVisible(true);
      this.p.setBounds(this.px, floor, 100, 100);
   }
   // moves the person to the right direction. 
   public void move_right() {
      if (this.direction == 3) {
         if (this.movetimer > 20) {
            this.movetimer = 0;
            this.movement = (this.movement + 1) % 4;
         } else
            this.movetimer = this.movetimer + 1;
      } else {
         this.direction = 3;
         this.movement = 1;
         this.movetimer = 0;
      }
      p.setIcon(this.charset[direction][movement]);

      this.px = this.px + 1;
      this.setVisible(true);
      this.p.setBounds(this.px, floor, 100, 100);
   }

   public void setVisible(boolean aFlag) {
      p.setVisible(aFlag);
   }

   public void getSpeed() {
      Random ran;
      ran = new Random();
      speed = ran.nextInt(3);
   } 
}