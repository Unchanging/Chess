import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class ControlToggle extends JLabel implements MouseListener{

	private Board board;
	
	public ControlToggle(Board board) {
		super("<html>C<br/>O<br/>N<br/>T<br/>R<br/>O<br/>L<br/>S</html>", SwingConstants.CENTER);
		this.board = board;
		this.setPreferredSize(new Dimension(20, 600));
		this.setOpaque(true);
		this.setBackground(Color.LIGHT_GRAY);
		this.addMouseListener(this);
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		board.toggleControlPanel();		
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
		setBackground(Color.GRAY);
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		setBackground(Color.LIGHT_GRAY);		
	}

}
