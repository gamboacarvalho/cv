package bubbles;


public class BubblesBreaker {

	public  static  final  int x0 =  5, y0 =  5;
	public  static  final  int LINES =  12, COLS =  9;
	
	public static void main(String[] args){
		
		Point origin = new Point(x0, y0);
		BubblesGui gui = new BubblesGui(2, origin, LINES, COLS);
		gui.start();
	}
}
