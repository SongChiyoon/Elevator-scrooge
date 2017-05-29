import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/* Remote Controller GUI */
public class remoteGUI extends JFrame
{
   private GridBagConstraints c = new GridBagConstraints();
   private GridBagLayout gridbag = new GridBagLayout();
   
   /* Floor Buttons */
   private JPanel jp[] = new JPanel[9];
   private JButton bt[] = new JButton[8];
   private ImageIcon dest_img[] = new ImageIcon[8];
   private ImageIcon src_img[] = new ImageIcon[8];
   
   private final String src_bt_src = "default";	// Not Clicked Image src
   private final String dest_bt_src = "dest";	// Clicked Image src
   
   private final int img_size = 150;	// Button Size
   private boolean flag;   // true: clicked, false: not clicked
   private int src;	// Departure Floor
   private int dest;	// Destination Floor
   remoteControl myremote;
   
   public remoteGUI(remoteControl rmc)
   {
      super("Remote control");
      myremote =rmc;
      init();

      setSize(img_size*3-50, img_size*5-50);
      setLocationRelativeTo(null); 
      setResizable(false);
      setVisible(true);
   }
   
   private void init()
   {
	   /* GridBag Layout */
      c.weightx = 0.0;
      c.weighty = 0.0;
      
      for (int i=0; i<8; i=i+1)
      {
         jp[i] = new JPanel();

         src_img[i] = new ImageIcon(src_bt_src+"_"+(i+1)+".png");
         dest_img[i] = new ImageIcon(dest_bt_src+"_"+(i+1)+".png");
         bt[i] = new JButton(src_img[i]);
      }
      jp[8] = new JPanel();
      
     setLayout(gridbag);
      jp[0].setPreferredSize(new Dimension(img_size,img_size));
      jp[1].setPreferredSize(new Dimension(img_size,img_size));
      jp[2].setPreferredSize(new Dimension(img_size,img_size));
      jp[3].setPreferredSize(new Dimension(img_size,img_size));
      jp[4].setPreferredSize(new Dimension(img_size,img_size));
      jp[5].setPreferredSize(new Dimension(img_size,img_size));
      jp[6].setPreferredSize(new Dimension(img_size,img_size));
      jp[7].setPreferredSize(new Dimension(img_size,img_size));
      
      jp[8].setPreferredSize(new Dimension(img_size*2,img_size));
      
      layout(jp[0],0,0,5,5);
      layout(jp[1],5,0,5,5);
      layout(jp[2],0,5,5,5);
      layout(jp[3],5,5,5,5);
      layout(jp[4],0,10,5,5);
      layout(jp[5],5,10,5,5);
      layout(jp[6],0,15,5,5);
      layout(jp[7],5,15,5,5);
      layout(jp[8],0,20,10,5);

      for (int i=0; i<8; i=i+1)
      {
          jp[i].add(bt[i]);
          bt[i].setBounds(0, 0, img_size, img_size);
          bt[i].addActionListener(new Clicked(i));
      }
      
      flag = false;
   }

   /* GridBag Layout */
   public void layout (Component obj, int x, int y, int width, int height) {
      c.gridx = x; // X point
      c.gridy = y; // Y point
      c.gridwidth = width; // Width
      c.gridheight = height; // Height

      add(obj, c);
   }
   
   private class Clicked implements ActionListener
   {
      int bt_num;
      
      public Clicked(int i)
      {
         bt_num = i;
      }
      public void actionPerformed(ActionEvent e)
      {
         if (!flag)
         {
             bt[bt_num].setIcon(dest_img[bt_num]);
             src = bt_num+1;
             flag = true;
         }
         else
         {
             dest = bt_num+1;
             bt[src-1].setIcon(src_img[src-1]);
             flag = false;
             myremote.getInput(src, dest);
         }
      }
   }
}