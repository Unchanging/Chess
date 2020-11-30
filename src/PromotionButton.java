import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class PromotionButton extends JLabel implements MouseListener{
	
	private Game game;
	private Board board;
	private PromotionPanel panel;
	private String unit;
	
	public PromotionButton(Game game, Board board, Dimension dimentions, PromotionPanel panel, String unit) {
		
		super("", SwingConstants.CENTER);
		this.game = game;
		this.board = board;
		this.panel = panel;
		this.unit = unit;
		
		this.setPreferredSize(dimentions);
		this.setOpaque(true);
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.addMouseListener(this);
	}	
	
	public String getUnit() {
		return unit;
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e) {
		panel.input(this, unit);
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
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
