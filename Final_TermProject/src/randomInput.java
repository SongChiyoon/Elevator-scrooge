import java.util.Random;

public class randomInput extends Thread{
	int input, des;
	 Random generator;
	public randomInput(){
		generator = new Random();
		input = input();
		des = destination();
	}
	private int input(){
		int num = generator.nextInt(8) + 1;
		return num;
	}
	
	private int destination(){
		int num = generator.nextInt(8) + 1;
		while(num == input){
			num = generator.nextInt(8) + 1;
		}
		return num;
		
	}
}