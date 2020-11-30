import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PromotionPanel extends JPanel{
	
	private Game game;
	private Board board;
	private ArrayList<PromotionButton> theButtons;
	private InformationPanel informationPanel;
	private TreeMap<String, Icon> iconMap;

	public PromotionPanel(Game game, Board board, TreeMap<String, Icon> iconMap) {
		
		this.game = game;
		this.board = board;
		this.setOpaque(true);
		this.setBackground(Color.white);
		theButtons = new ArrayList<PromotionButton>();
		this.iconMap = iconMap;
		
		setLayout(new FlowLayout());
		setPreferredSize(new Dimension(100, 600));
		
		JLabel promotionInformation = new JLabel("<html><center>You may</center></br><center>promote</center></br><center>a pawn</center></html>", SwingConstants.CENTER);
		promotionInformation.setPreferredSize(new Dimension(100, 200));
		
		String[] units = {"Rook", "Bishop", "Knight", "Queen"};		
		JPanel buttonArea = new JPanel();
		buttonArea.setPreferredSize(new Dimension(100, 400));
		buttonArea.setLayout(new GridLayout(4, 1));			
		
		for (String unit : units) {		
			PromotionButton button = new PromotionButton(game, board, new Dimension(100, 100), this, unit.toUpperCase());
			theButtons.add(button);
			buttonArea.add(button);
		}
		this.add(promotionInformation);
		this.add(buttonArea);
		
	}
	
	public void reDraw() {		
		if(game.getCurrentPlayer() == 1) {
			for (PromotionButton button : theButtons) {
				button.setIcon(iconMap.get("WHITE_" + button.getUnit() +"_LIGHT"));
			}
		} 
		else {
			for (PromotionButton button : theButtons) {
				button.setIcon(iconMap.get("BLACK_" + button.getUnit() +"_LIGHT"));
			}			
		}		
	}

	public void input(PromotionButton button, String unit) {
		board.choosenPromotion(unit);
	}

}
