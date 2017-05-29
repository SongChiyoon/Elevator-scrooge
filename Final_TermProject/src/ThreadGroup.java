import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.swing.ImageIcon;

public class ThreadGroup {
	Random ran;

	ThreadGroup() {
		ran = new Random();
	}

	public int speed() {
		return ran.nextInt(3);
	}
}

class personMoveT extends Thread {
	public person2 person;
	public scheduler mysch;
	public boolean stopped = false;	// Whether Person Stops or not
	public String outp = "human";	// Person Image Source
	public int speed;	// Speed of Person's Movement

	/* Initialize */
	personMoveT(person2 p, scheduler sch) {
		person = p;
		this.mysch = sch;
		this.speed = new ThreadGroup().speed();
	}

	public void sstop() {
		stopped = true;	// Make person stop
	}

	public void run() {
		changeMyele change = new changeMyele();
		int checkTime = 2500;	// Limited Time of Person Waiting
		int inTime = 3500;	// For error detection
		Random generator = new Random();
		while (!stopped) {

			try {
				Thread.sleep(5);
			} catch (Exception e) {
			}
			if (person.in == false)	// When person is waiting
			{
				checkTime--;
				if (checkTime == 0 && person.floor != person.myele.location)	// Recall
				{
					change.changeWait(-1, person.hf, person.myele);
					person.p.setBounds(person.px, person.floor, 0, 0);
					if (mysch.mode == 2)
						mysch.scheduling(person.hf + 1, person.f + 1, person.px);
					else
						mysch.nomalscheduling(person.hf + 1, person.f + 1, person.px);
					sstop();
					continue;

				} else {
					if (person.floor != person.myele.location)	// Person goes to get in elevator
						;

					// Elevator Arrived and Door is Opened
					if (person.myele.location == person.floor && person.myele.close == false)
					{
						// Elevator isn't for Person
						if (person.myele.location == person.floor && person.myele.close == false) {
							// People can't get in the elevator
							if (person.in != true) {
								// Person's Elevator comes
								if (person.px == person.myele.X) {
									// Elevator has Over people
									if (person.myele.usernum > 9) {
										change.changeWait(-1, person.hf, person.myele);
										person.p.setBounds(person.px, person.floor, 0, 0);
										// If Scrooge Mode
										if (mysch.mode == 2)
											mysch.scheduling(person.hf + 1, person.f + 1, person.px);
										// If Normal Mode
										else
											mysch.nomalscheduling(person.hf + 1, person.f + 1, person.px);
										sstop();
										continue;
									}
							
									// People get in elevator
									elevator_inner.in(person.myele.name);
									change.In(1, person.myele);
									change.changeWait(-1, person.hf, person.myele);
									person.in = true;
									person.p.setBounds(person.px, person.floor, 0, 0);
									change.insertUpDown(person.myele, person.destination);

								}

								// Person walks to his Elevator
								int ran2 = generator.nextInt(1) + 1;
								for (int k = 0; k < ran2; k++) {
									if (person.px != person.myele.X) {
										if (person.px > person.myele.X)
											person.move_left();
										else if (person.px < person.myele.X)
											person.move_right();
										else
											person.stop_wait();
									}
								}
							}
						}
					}
					// If the elevator isn't for person, wait
					if (person.myele.location != person.floor) {
						person.stop_wait();
					}
				}
			} else	// When elevator arrived its destination
			{
				inTime--;
				/* Error Detection */
				if(inTime == 0)
				{
					System.out.println("timeout");
					person.out = 1;
					change.In(-1, person.myele);
					change.Inner(person.myele);
					person.myele.deletePlist(person);
					break;
				}
				/* Door is opened, So people get out of Elevator */
				if (person.myele.location == person.destination && person.myele.close == false && person.out != 1)
				{
					person.out = 1;
					change.In(-1, person.myele);
					change.Inner(person.myele);
					person.myele.deletePlist(person);
					person.px++;
					person.set_char(outp);
					person.p.setBounds(person.px, person.destination, 100, 100);
					new PersonOut(person, 700, person.destination);
					sstop();
				}

			}

		}
	}
}

/*Class of synchronizing threads*/
class changeMyele {
   public synchronized void Inner(eleb ele){
      elevator_inner.out(ele.name);
   }
   public synchronized void In(int input, eleb myele) {
      myele.usernum += input;
   }

   public synchronized void changeWait(int input, int floor, eleb myele) {
      myele.hereWait[floor] += input;
   }

   public synchronized void insertUpDown(eleb myele, int destination) {
      if (myele.location > destination) // want

      {
         myele.insertup(destination);
      } else // want down
      {
         myele.insertDown(destination);
      }

   }

}
/*Class of moving part for getting out , when elevator is reached to destination floor of person*/
class PersonOut extends Thread {
   person2 p;
   int des;
   boolean stop = false;
   int floor;
   Random generator;

   public PersonOut(person2 pp, int destination, int ffloor) {
      generator = new Random();
      p = pp;
      des = destination;
      start();
      floor = ffloor;
   }

   public void run() {
      int ran = generator.nextInt(2);

      int ran2 = generator.nextInt(2) + 1;
      if (ran == 0) {
         while (true) {
            try {
               Thread.sleep(10);
            } catch (Exception e) {
            }
            p.floor = floor;
            for (int i = 0; i < ran2; i++)
               p.move_right();

            if (p.px >= des)
               break;
         }
      } else {
         while (true) {
            try {
               Thread.sleep(10);
            } catch (Exception e) {
            }
            p.floor = floor;
            for (int i = 0; i < ran2; i++)
               p.move_left();
            if (p.px <= 10)
               break;
         }
      }
      p.p.setBounds(p.px, floor, 0, 0);
   }
}
/*Class of autometical input by using thread*/
class autoInput extends Thread {
   int auto = 0;
   scheduler mysch;

   autoInput(scheduler sc) {
      mysch = sc;
   }

   public void run() {
      Random generator = new Random();
      while (auto < 10) {
         try {
            Thread.sleep(2000);
         } catch (Exception e) {
         }
         randomInput r;
         r = new randomInput();

         int input = r.input;
         int des = r.des;

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

         auto++;
      }
   }
}
/*Class of input using textfile by using thread */
class FileInput extends Thread {

   scheduler mysch;

   FileInput(scheduler sc) {
      mysch = sc;
   }

   public void run() {
      BufferedReader inputStream = null;
      int input;
      int des;
      Random generator = new Random();
      try {
         inputStream = new BufferedReader(new FileReader("fileinput.txt"));
      } catch (FileNotFoundException e2) {
         // TODO Auto-generated catch block
         e2.printStackTrace();
      }
      try {
         while (true) {
            try {
               Thread.sleep(1000);
            } catch (Exception e) {
            }
            String FileInput = null;
            String FileInput2 = null;
            FileInput = inputStream.readLine();
            if (FileInput == null)
               break;
            input = Integer.parseInt(FileInput);
            FileInput2 = inputStream.readLine();
            des = Integer.parseInt(FileInput2);
   
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
      } catch (NumberFormatException | IOException e1) {
         // TODO Auto-generated catch block
         e1.printStackTrace();
      }

   }
}