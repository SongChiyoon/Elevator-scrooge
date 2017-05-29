import javax.swing.*;

public class init 
{
   /* frame */
   public static final int frame_width = 850;
   public static final int frame_height = 700;
   
   /* background */
   public static final int background_width = 850;
   public static final int background_height = 680;
   public static final String background_src = "background";
   
   /* elevator object */
   public static final String elevator_src = "elevator";
   
   /* elevator inner */
   public static final String sub_background_src = "inner";
   public static final int inner_width = 230;
   public static final int inner_height = 250;
   
   /* banner */
   public static final String scrooge = "Scrooge";
   public static final String normal = "Normal";
   
   /* indicator */
   public static final String indicator_src = "indicator_t2";
   public static JLayeredPane lp;
   public static ImageIcon indicator = new ImageIcon(indicator_src+".png");
   public static JLabel i[] = new JLabel[5];
   public static final int indicator_width = indicator.getIconWidth();
   public static final int indicator_height = indicator.getIconHeight();
}