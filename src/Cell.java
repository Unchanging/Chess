import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;


public class Cell extends JLabel implements MouseListener{

	private Location loc;
	private Game game;
	private Board board;
	protected JLabel textField;
	private Color tileColor;

	public Cell(Location loc, Game game, Board board, int shiftingColor) {		

		this.loc = loc;
		this.game = game;
		this.board = board;
		this.tileColor = generateTileColor(shiftingColor);

		textField = new JLabel();		
		textField.setFont(this.getFont().deriveFont(24.0f));
		textField.setForeground(Color.RED);

		setLayout(new GridLayout(3, 3));		
		this.add(textField);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		//		this.setPreferredSize(new Dimension(65, 65)); 
		this.setOpaque(true);
		this.addMouseListener(this);

	}

	private Color generateTileColor(int shiftingColor) {
		if (shiftingColor == 1)
			return Color.WHITE;
		return Color.BLACK;
	}

	public void setImage(Icon icon) {
		this.setIcon(icon);
	}

	public void setCellColor() {
		setBackground(tileColor);
	}

	public void setCellColor(Color color) {
		setBackground(color);
	}

	public Color getOriginalTileColor() {
		return tileColor;

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {

		if (!game.isGameOver()) {

			if (e.getButton() == 1) {
				leftMouse();
			}
			else if (e.getButton() == 3) {
				rightMouse();
			}
			else if (e.getButton() == 2) {
				middleMouse();
			}
		}

		board.reDraw();	
	}

	private void leftMouse() {		

		if (board.getSelected() != null && board.getSelected().getType().equals("KING") && board.isHighLighted(loc)) {
			if (game.move(board.getSelected(), loc))  //This checking if we're trying to castle. it's first so we don't deselect the Rook. if the move could be made it will unselect the unit
				board.setSelected(null);
		}		
		else if (game.getUnit(loc) == board.getSelected()) {
			board.setSelected(null);
		}
		else if (game.getUnit(loc) != null && game.getUnit(loc).getOwner() == game.getCurrentPlayer()) {
			board.setSelected(game.getUnit(loc));
		}
		else if(board.getSelected() != null) {			
			if (game.move(board.getSelected(), loc)) { // This is the main input of the game, if the move could be made it will unselect the unit
				board.setSelected(null);
			}			
		}
	}

	private void rightMouse() {

//		if (board.getSelected() != null) {
//			if (game.getUnit(loc) != null)
//				game.getUnit(loc).kill();
//			board.getSelected().move(loc);
//			board.setSelected(null);
//		}
//		else if (game.getUnit(loc) != null)
//			game.getUnit(loc).kill();
	}

	private void middleMouse() {	

//		if (game.getUnit(loc) != null) {
//			board.setSelected(game.getUnit(loc));
//			board.visulizeMoveValues(loc);
//		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
