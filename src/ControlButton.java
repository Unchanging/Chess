import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ControlButton extends JLabel implements MouseListener{
	
	private Game game;
	private Board board;
	private ControlPanel panel;
	private String function;
	
	public ControlButton(Game game, Board board, Dimension dimentions, ControlPanel panel, String function, String text) {
		
		super(text, SwingConstants.CENTER);
		this.game = game;
		this.board = board;
		this.panel = panel;
		this.function = function;
		
		this.setPreferredSize(dimentions);
		this.setOpaque(true);
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.addMouseListener(this);
	}	
	
	
	@Override
	public void mouseReleased(MouseEvent e) {
		panel.input(this, function);
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
		setBackground(Color.LIGHT_GRAY);		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		setBackground(Color.WHITE);
		
	}

}
