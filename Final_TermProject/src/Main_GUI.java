import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.Border;

/* Class of Main Frame that shows Total GUI */
class Main_GUI extends JFrame {

	public static final int x[] = { 210, 330, 450, 570, 570 };	// X point of Nth Elevator

	private static final int floor_height = 88;	// interval between floors
	private static final int first_floor = 592;	// Y point of 1st floor
	public int floor[] =	// Y point of each floor
		{ first_floor, first_floor - floor_height, first_floor - floor_height*2,
		  first_floor - floor_height * 3 + 8, first_floor - floor_height * 4 + 8,
		  first_floor - floor_height * 5 + 8, first_floor - floor_height * 6 + 10,
		  first_floor - floor_height * 7 + 14 };
	
	public JLabel ele[] = new JLabel[5];	// for showing elevator image
	public int input;	// input value of departure floor
	public int des;	// input value of destination floor
	public int numC = 5;
	public int mode = 2;
	
	/* Objects of other classes */
	scheduler mysch;
	Container content;
	eleb obele[] = new eleb[5];
	mover mov;
	
	/* Image Sources */
	ImageIcon imageIcon = new ImageIcon(init.elevator_src + ".png");
	ImageIcon outp = new ImageIcon("out.png");
	ImageIcon b = new ImageIcon(init.background_src + ".png");
	ImageIcon Scrooge_mode = new ImageIcon(init.scrooge + ".png");
	ImageIcon Normal_mode = new ImageIcon(init.normal + ".png");

	GridBagConstraints c;	// Constraints for Gridbag Layout

	/* Panels */
	public JLayeredPane lp = new JLayeredPane();	// Main Panel Layered
	private JPanel p2 = new JPanel();	// Panel for customized input
	private JPanel p3 = new JPanel();	// Panel for inner of 1st Elevator
	private JPanel p4 = new JPanel();	// Panel for inner of 2nd Elevator
	private JPanel p5 = new JPanel();	// Panel for inner of 3rd Elevator
	private JPanel p6 = new JPanel();	// Panel for inner of 4th Elevator
	private JPanel p7 = new JPanel();	// Panel for Mode Changer
	private JPanel p8 = new JPanel();
	private JPanel p9 = new JPanel();	// Panel for inner of 5th Elevator

	JLabel b_title = new JLabel();	// Bottom Title
	JLabel floor_in = new JLabel();
	JLabel floor_out = new JLabel();
	Choice choice, choice_2;	// Normal Mode, Scrooge Mode

	JButton change;	// Mode Changer
	client jdbc = new client();	// for using Database MySQL

	public Main_GUI(scheduler obj)
	{
		/* initialize frame */
		super("Elevator_term");
		mysch = obj;
		setSize(1360, 720);
		setResizable(false);
		choice = new Choice();
		choice_2 = new Choice();
		init.lp = this.lp;
		change = new JButton(new ImageIcon(init.normal + ".png"));
		Button Enter_Button = new Button("     E n t e r     ");
		Button Random = new Button("     R a n d o m     ");
		Button auto = new Button("     A u t o     ");
		Button fileinput = new Button("     F i l e   ");
		
		MouseHandler handler = new MouseHandler();

		/* GridBag Layout */
		GridBagLayout gridbag = new GridBagLayout();

		setLayout(gridbag);
		c = new GridBagConstraints();
		c.weightx = 0.0;
		c.weighty = 0.0;
		
		lp.setPreferredSize(new Dimension(860, 690));
		p2.setPreferredSize(new Dimension(460, 100));
		p3.setPreferredSize(new Dimension(230, 250));
		p4.setPreferredSize(new Dimension(230, 250));
		p5.setPreferredSize(new Dimension(230, 250));
		p6.setPreferredSize(new Dimension(230, 250));
		p7.setPreferredSize(new Dimension(460, 90));
		p9.setPreferredSize(new Dimension(230, 250));
		
		layout(lp, 0, 0, 12, 10);
		layout(p2, 12, 8, 6, 2);
		layout(p3, 12, 5, 3, 3);
		layout(p4, 15, 5, 3, 3);
		layout(p5, 12, 2, 3, 3);
		layout(p6, 15, 2, 3, 3);
		layout(p7, 12, 0, 6, 2);

		/* for opening Inner of Elevator */
		p4.addMouseListener(handler);
		p4.addMouseMotionListener(handler);

		elevator_inner.init(p3, p4, p5, p6, p9);	// initialize 5 panels for inner of elevator

		b_title.setText("                           User can control the person's entrance and outdoor floor.                         ");
		floor_in.setText("Floor in :     ");
		floor_out.setText("            Floor out :      ");

		/* Checkbox for input */
		choice.add("1");
		choice.add("2");
		choice.add("3");
		choice.add("4");
		choice.add("5");
		choice.add("6");
		choice.add("7");
		choice.add("8");
		choice_2.add("1");
		choice_2.add("2");
		choice_2.add("3");
		choice_2.add("4");
		choice_2.add("5");
		choice_2.add("6");
		choice_2.add("7");
		choice_2.add("8");

		/* Mode Changer */
		change.setPreferredSize(new Dimension(470, 90));
		change.setBackground(Color.white);
		p7.add(change);
		change.addActionListener(new MyActionListener_3());

		/* Panel for Customized Input */
		p2.add(b_title);
		p2.add(floor_in);
		p2.add(choice);
		p2.add(floor_out);
		p2.add(choice_2);
		p2.add(Enter_Button);
		p2.add(Random);
		p2.add(auto);
		p2.add(fileinput);
		Enter_Button.addActionListener(new MyActionListener());
		Random.addActionListener(new MyActionListener_2());
		auto.addActionListener(new MyActionListener_4());
		fileinput.addActionListener(new MyActionListener_5());
		Scanner keyboard = new Scanner(System.in);
		JLabel a3 = new JLabel(b, JLabel.LEFT);
		a3.setBounds(0, 0, init.frame_width, init.frame_height);
		lp.add(a3, new Integer(0));
		JPanel back = new JPanel();
		back.setSize(init.background_width, init.background_height);

		/* Initialize Elevator Label */
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 8; j++) {
				JLabel backElv = new JLabel(new ImageIcon(init.elevator_src + "_temp.png"), JLabel.LEFT);
				backElv.setBounds(x[i], floor[j], 230, 120);
				backElv.setLayout(null);
				backElv.setOpaque(false);
				lp.add(backElv, new Integer(2));
				backElv.setBounds(x[i], floor[j], 230, 120);
			}
		}
		for (int i = 0; i <= 3; i++) {
			ele[i] = new JLabel(imageIcon, JLabel.LEFT);
			ele[i].setBounds(x[i], floor[0], 230, 120);
			ele[i].setLayout(null);
			ele[i].setOpaque(false);
			lp.add(ele[i], new Integer(3));
			ele[i].setBounds(x[i], floor[0], 230, 120);
		}

		ele[4] = new JLabel(imageIcon, JLabel.LEFT);
		ele[4].setBounds(x[3], floor[4], 230, 120);
		ele[4].setLayout(null);
		ele[4].setOpaque(false);
		lp.add(ele[4], new Integer(3));
		ele[4].setBounds(x[3], floor[4], 230, 120);

		JLabel trash = new JLabel();
		lp.add(trash);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		for (int i = 0; i < 4; i++) {
			obele[i] = new eleb(jdbc.callToServer(i), floor[0], ele[i], x[i], i);
		}
		obele[4] = new eleb(jdbc.callToServer(4), floor[4], ele[4], x[4], 4);
		mysch.mov = new mover(obele, mysch);
		mysch.mov.start();
		setVisible(true);
	}

	/* Getter method for Inner of 5th Elevator */
	private JPanel get5th() {
		return this.p9;
	}

	private class MyActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			input = (choice.getSelectedIndex() + 1);
			des = (choice_2.getSelectedIndex() + 1);

			Random generator = new Random();
			int ran = generator.nextInt(200);
			if (mysch.mode == 2) {
				if (ran % 2 == 0)
					mysch.scheduling(input, des, 350 + ran);
				else
					mysch.scheduling(input, des, 350 - ran);
			}
			else
			{
				if(ran%2 == 0)
		        	 mysch.nomalscheduling(input, des, 350 + ran);
		         else
		        	 mysch.nomalscheduling(input, des, 350 - ran);
			}
		}

	}

	/* Random Input Value */
	private class MyActionListener_2 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			randomInput r;
			r = new randomInput();

			Random generator = new Random();
			input = r.input;
			des = r.des;

			int ran = generator.nextInt(200);
			if (mysch.mode == 2) {
				if (ran % 2 == 0)
					mysch.scheduling(input, des, 350 + ran);
				else
					mysch.scheduling(input, des, 350 - ran);
			}
			else
			{
				if(ran%2 == 0)
		        	 mysch.nomalscheduling(input, des, 350 + ran);
		         else
		        	 mysch.nomalscheduling(input, des, 350 - ran);
			}
		}
	}

	/* Mode Changer */
	private class MyActionListener_3 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (mysch.mode == 1) {
				change.setIcon(Scrooge_mode);
				mysch.mode = 2;
			} else {
				change.setIcon(Normal_mode);
				mysch.mode = 1;
			}

		}
	}

	/* Auto Input Value */
	private class MyActionListener_4 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			autoInput autos = new autoInput(mysch);
			autos.start();

		}

	}

	/* File Input Value */
	private class MyActionListener_5 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// autoinput = 0;
			FileInput files = new FileInput(mysch);
			files.start();
		}

	}

	/* Shows Inner of 5th Elevator */
	private class MouseHandler implements MouseListener, MouseMotionListener {

		public void mouseClicked(MouseEvent event) {
			Elevator_5 e = new Elevator_5(elevator_inner.get5th());
		}

		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}
	}

	/* For Gridbag Layout */
	public void layout(Component obj, int x, int y, int width, int height) {
		c.gridx = x;	// X point
		c.gridy = y;	// Y point
		c.gridwidth = width;	// Width
		c.gridheight = height;	// Height

		add(obj, c);
	}
}