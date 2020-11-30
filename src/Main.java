
public class Main {

	public static void main(String[] args) {
		
		Game Chess = new Game(true, false);
		
		Board board = new Board(Chess);
		
		board.pack();
		board.setVisible(true);
		board.reDraw();
	}
}
