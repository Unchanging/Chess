import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.TreeMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ControlPanel extends JPanel{
	
	private Game game;
	private Board board;
	private TreeMap<String, ControlButton> theButtons;
	private InformationPanel informationPanel;
	private JLabel currentScore;

	public ControlPanel(Game game, Board board) {
		
		this.game = game;
		this.board = board;
		this.setOpaque(true);
		this.setBackground(Color.white);
		theButtons = new TreeMap<String, ControlButton>();
		
		setLayout(new GridLayout(3, 1));
		setPreferredSize(new Dimension(100, 600));
		
		informationPanel = new InformationPanel(game, board, this);
		currentScore = new JLabel("", SwingConstants.CENTER);
		
		
		JPanel buttonArea = new JPanel();
		buttonArea.setPreferredSize(new Dimension(100, 300));
		buttonArea.setLayout(new GridLayout(3, 1));		
		String[] buttonFunction = {"restart", "singlePlayer", "yield"};
		String[] buttonLable = {"Restart", "Singleplayer", "Yield"};	
		for (int i = 0; i < buttonFunction.length; i++) {
			ControlButton button = new ControlButton(game, board, new Dimension(100, 100), this, buttonFunction[i], buttonLable[i]);
			theButtons.put(buttonFunction[i], button);
			buttonArea.add(button);
		}
		
		this.add(informationPanel);
		this.add(currentScore);
		this.add(buttonArea);
	}
	
	public void reDraw() {		
		informationPanel.reDraw();
		
		int black = 0;
		int white = 0;
		
		for(int winner : game.getWinners()) {
			if(winner == 1)
				white++;
			else if(winner == 2)
				black++;			
		}
		
		currentScore.setText("<html><center>Current score</center></br><center>Black: " + black + "</center></br><center>White: " + white + "</center></html>");
		
	}

	public void input(ControlButton button, String function) {		
		if (function.equals("restart")) {
			game.restart();
		} 
		else if(function.equals("singlePlayer")) {
			if (game.getSinglePlayer()) {
				game.setSinglePlayer(false);
				button.setText("Two players");
			}
			else  {
				game.setSinglePlayer(true);
				button.setText("Singleplayer");
			}
		}
		else if(function.equals("yield")) {
			game.yield();
		}		
		board.reDraw();
	}
}
