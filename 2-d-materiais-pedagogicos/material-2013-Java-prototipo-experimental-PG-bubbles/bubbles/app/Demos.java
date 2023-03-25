package app;

import static isel.leic.pg.Console.color;
import static isel.leic.pg.Console.cursor;
import static isel.leic.pg.Console.print;
import static isel.leic.pg.Console.WHITE;
import static isel.leic.pg.Console.DARK_GRAY;
import static isel.leic.pg.Console.RED;
import static isel.leic.pg.Console.BLACK;
import static isel.leic.pg.Console.echo;
import static isel.leic.pg.Console.waitKeyPressed;
import static isel.leic.pg.Console.isKeyPressed;
import static isel.leic.pg.Console.waitChar;

import isel.leic.pg.Console;

import java.awt.event.KeyEvent;

public class Demos {

	public static void test1(String[] args) {
		Console.open("Consola de PG", 10, 50);
		Console.cursor(5,0);
		Console.println("Ola Mundo!");

		Console.color(Console.LIGHT_GRAY, Console.BLACK); 
		String name = Console.nextLine(12); 
		Console.println(); 
		Console.color(Console.BLACK, Console.CYAN); 
		Console.print("name = "+name);
	}


	private static final int LINES=10, COLS=20; 
	static int lin=LINES/2, col=COLS/2; 

	private static void writeMsg(String msg) { 
		cursor(LINES/2,(COLS-msg.length())/2); 
		color(WHITE, DARK_GRAY); 
		print(msg); 
		waitChar(3000);  
	} 

	public static void test2(String[] args) { 
		boolean[] path = new boolean[LINES*COLS]; 
		int key;  
		boolean move; 
		Console.open("Snake", LINES, COLS); 
		color(BLACK, RED); 
		echo(false); 
		for(;;) { 
			cursor(lin,col);  
			print("#"); 
			path[lin*COLS+col]=true; 
			if ((key=waitKeyPressed(5000))==-1) { 
				writeMsg("Timeout"); break; 
			} 
			move = true; 
			switch (key) { 
			case KeyEvent.VK_UP:    if (lin>0) --lin;       break;  
			case KeyEvent.VK_DOWN:  if (lin<LINES-1) ++lin; break;  
			case KeyEvent.VK_LEFT:  if (col>0) --col;       break; 
			case KeyEvent.VK_RIGHT: if (col<COLS-1) ++col;  break; 
			default: move = false; 
			} 
			if (move && path[lin*COLS+col]) { 
				writeMsg("Game over"); break; 
			} 
			while(isKeyPressed()) ; 
		} 
		Console.close(); 
	} 

}
