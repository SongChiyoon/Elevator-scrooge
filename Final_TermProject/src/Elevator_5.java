import javax.swing.*;
import java.awt.*;

/* Class for 5th Elvator */
public class Elevator_5 extends JFrame
{
   public Elevator_5(JPanel pane)
   {
      super("Elevator 5");
      setResizable(false);
      setSize(260,280);
      setLocation(1000,300);
      setVisible(true);
      
      this.add(pane);
   }
}