package guessANumberGUI;

import java.util.Random;
import java.util.Scanner;

public class GuessANumber {

	public static void main(String[] args) {
		Random r = new Random();
		int random = r.nextInt(101);
		
		Scanner scan = new Scanner(System.in);
		System.out.println("-----------------------Welcome to the Game-----------------------\n");
		int chance = startMessageDisplay();
		
		int ct = 0 ;
		boolean wantToPlayAgain = true ;
		while(wantToPlayAgain) {
			int temp = chance ;
			while(chance != 0) {
				System.out.print("Guess a number:  ");
				int guess = scan.nextInt();
				ct++ ;
				if(guess > random) {
					System.out.println("TOO HIGH...!!! YOU HAVE "+(temp-ct)+" attempt left.");
				}
				else if(guess < random) {
					System.out.println("TOO LOW...!!! YOU HAVE "+(temp-ct)+" attempt left.");
				}
				else {
					System.out.println("CONGRATULATION... YOU WON...!!!\nYou have guess in "+(ct)+" attempt.\n");
					break;
				}
				chance-- ;
				if(chance == 0) {
					System.out.println("SORRY... YOU LOOSE...!!!");
					System.out.println("The exact number was "+random);
				}
			}
			System.out.println("Do You want to play again (Y/N) ??? ");
			char asking = scan.next().charAt(0);
			if(asking=='Y'|| asking=='y') wantToPlayAgain = true;
			else {
				System.out.println("\n-----------------------Thank You-----------------------");
				System.exit(0);
			}
			System.out.println("\n\n");
			chance = startMessageDisplay();
			ct = 0;
		}
		scan.close();
	}
	
	public static int startMessageDisplay() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Press 1 to Choose Easy level (10 chance)");
		System.out.println("Press 2 to Choose Medium level (7 chance)");
		System.out.println("Press 3 to Choose Hard level (5 chance)");
		System.out.println("Press any other key to exit...!!!");
		int value = 0;
		try {
			value = scan.nextInt();
		}
		catch(NumberFormatException e) {
			
		}
		finally {
			if(value != 1 && value != 2 && value != 3 ) {
				System.out.println("\n-----------------------Thank You-----------------------");
				System.exit(0);
			}
		}
		int chance = 0;
		if(value == 1) chance = 10;
		else if(value == 2) chance = 7;
		else chance = 5;
		System.out.println("User had entered "+value+" as an input.\nSo User get "+chance+" chance to guess.");
		return chance;
	}
	
}
