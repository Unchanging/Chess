import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class InformationPanel extends JPanel{
	
	private Game game;
	private Board board;
	private ControlPanel panel;
	JLabel currentPlayer;
	JLabel checkLabel;
	JLabel currentScore;
	
	public InformationPanel(Game game, Board board, ControlPanel panel) {
		
		
		this.game = game;
		this.board = board;
		this.panel = panel;
		this.setOpaque(true);
		this.setBackground(Color.WHITE);		
		this.setLayout(new GridLayout(2, 1));
		
		currentPlayer = new JLabel("", SwingConstants.CENTER);
		currentPlayer.setFont(getFont().deriveFont(20.0f));	
		currentPlayer.setPreferredSize(new Dimension(100, 200));
		currentPlayer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		currentPlayer.setOpaque(true);
		this.add(currentPlayer);
		
		checkLabel = new JLabel("", SwingConstants.CENTER);
		checkLabel.setFont(getFont().deriveFont(20.0f));
		checkLabel.setPreferredSize(new Dimension(100, 200));
		checkLabel.setForeground(Color.WHITE);
		checkLabel.setOpaque(true);
		this.add(checkLabel);
	}
	
	public void reDraw() {
		
		if (game.isKingChecked(game.getCurrentPlayer())) {
			checkLabel.setText("<html><center>The King</center></br><center>Is</center></br><center>Checked!</center></html>");
			checkLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			checkLabel.setBackground(Color.RED);
		}
		else {
			checkLabel.setText("");
			checkLabel.setBorder(null);
			checkLabel.setBackground(Color.WHITE);
		}		
		
		if (game.isGameOver() == true) {
			board.toggleControlPanel(true);
			currentPlayer.setText("<html><center>Player" + game.getWinners().get(game.getWinners().size() - 1) + "</center></br><center>won this game</center></html>");
			currentPlayer.setBackground(Color.BLUE);
		}
		else if (game.getCurrentPlayer() == 1) {
			currentPlayer.setText("<html><center>White</center></br><center>Player's</center></br><center>Turn</center></html>");	
			currentPlayer.setBackground(Color.WHITE);
		}
		else {
			currentPlayer.setText("<html><center>Black</center></br><center>Player's</center></br><center>Turn</center></html>");
			currentPlayer.setBackground(Color.WHITE);
		}
	}

}
