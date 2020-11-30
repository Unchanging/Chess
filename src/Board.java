import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Board extends JFrame{
	
	private Cell[][] cellBoard;
	private TreeMap<String, Image> imageMap;
	private TreeMap<String, Icon> iconMap;
	private ArrayList<String> imageList;
	private Game game;
	private Unit selected;
	private int[][] highlightedCells = new int[8][8];
	private int[][] lastMoveCell = new int[8][8];
	private String[][] cellText;
	private JPanel playField;
	private ControlPanel controlPanel;
	private boolean showControlPanel;
	private PromotionPanel promotionPanel;
	Move avaliblePromotion;

	public Board(Game game) {

		this.game = game; // sparar referens till spelet

		imageMap = new TreeMap<String, Image>(); // en karta bilderna. Görs inte om 
		loadImages(); // fyller kartan
		iconMap = new TreeMap<String, Icon>(); // en karta över iconerna. Görs om vid varje reDraw() för att storleken kan behöva ändras.
		generateIcons(40);	// gör en uppsättning av ikoner, kan vara fel storlek.

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Chess");	

		this.setLayout(new GridBagLayout()); // put the field in a gridbag so we can resize with the same aspect ratios and possbly add controls and information in a side panel		
		playField = new JPanel();		
		playField.setLayout(new GridLayout(game.getRows(),game.getCols()));		
		playField.setPreferredSize(new Dimension(600, 600));
		this.add(playField);

		controlPanel = new ControlPanel(game, this);
		showControlPanel = true;

		promotionPanel = new PromotionPanel(game, this, iconMap);	

		ControlToggle controlToggle = new ControlToggle(this);		
		this.add(controlToggle);	

		cellBoard = new Cell[game.getRows()][game.getCols()];
		cellText = getEmptyTextArray();

		int shiftingColor = 1;

		for (int i = 0; i < cellBoard.length; i++) {
			shiftingColor *= -1;
			for (int j = 0; j < cellBoard[0].length; j++) {
				shiftingColor *= -1;
				Cell nextCell = new Cell(new Location(i, j), game, this, shiftingColor);
				cellBoard[i][j] = nextCell;
				playField.add(nextCell);
			}
		}
		
		toggleControlPanel(showControlPanel);
	}

	public Unit getSelected() {
		return selected;
	}

	public void setSelected(Unit selected) {
		this.selected = selected;
		setHighLights(selected);
	}

	public void setHighLights(Unit selected) {

		ArrayList<Move> selectedUnitMoves = new ArrayList<Move>();
		if (selected != null) 
			selectedUnitMoves = selected.getMoves();		

		Move promotion = null;
		highlightedCells = new int[8][8];		
		for (Move move : selectedUnitMoves) {			
			if (move.getApplication() == 5)				
				promotion = move;
			else if (move.getApplication() != 3) 				
				highlightedCells[move.getTargetLoc().getRow()][move.getTargetLoc().getCol()] = 1;
		}

		showPromotionChoice(promotion);
	}

	public boolean isHighLighted(Location loc) { // so that we can click on the rook when we castle
		if (highlightedCells[loc.getRow()][loc.getCol()] == 1)
			return true;		
		return false;
	}

	private void showPromotionChoice(Move promotion)  {
		avaliblePromotion = promotion;		
		if (promotion != null) {
			toggleControlPanel(false);
			togglePromotionPanel(true);
			this.pack();
		}
		else {
			togglePromotionPanel(false);
			toggleControlPanel(showControlPanel); // showControlPanel is a boolean with the players preferences
			this.pack();
		}
	}

	public void choosenPromotion(String unit) {		
		game.setPromotionPreference(unit);
		avaliblePromotion.executeMove();
		setSelected(null); // this was moved down a line, look here if there's an issue with highlighted fields 
		reDraw();
		showPromotionChoice(null);
	}

	public void reDraw(){

		if (game.getLastMove(game.getOtherPlayer()) != null) {			
			Move move = game.getLastMove(game.getOtherPlayer());
			Location targetLoc = move.getTargetLoc();
			Location originLoc = move.getOriginLoc();
			lastMoveCell[targetLoc.getRow()][targetLoc.getCol()] = 1; 
			lastMoveCell[originLoc.getRow()][originLoc.getCol()] = 2;
		}

		generateIcons(cellBoard[0][0].getHeight());
		for(int i = 0; i < cellBoard.length; i++) {
			for(int j= 0; j < cellBoard[0].length; j++) {
				paint(cellBoard[i][j], new Location(i, j));
			}
		}		
		cellText = getEmptyTextArray();
		lastMoveCell = new int[8][8];

		controlPanel.reDraw();
		promotionPanel.reDraw();
	}

	private String[][] getEmptyTextArray() {
		String[][] res = new String[8][8];
		for (int i = 0; i < res.length; i++) {
			for (int j = 0; j < res[0].length; j++) {
				res[i][j] = "";
			}			
		}
		return res;
	}

	private void paint(Cell cell, Location loc) {
		Unit unit = game.getUnit(loc);

		cell.setCellColor(); // return to the checkered board		
		cell.setIcon(getIcon(unit));
		cell.textField.setText(cellText[loc.getRow()][loc.getCol()]); // To visualise the move-value of a unit

		if (highlightedCells[loc.getRow()][loc.getCol()] == 1) 
			cell.setCellColor(Color.yellow);
		else if(lastMoveCell[loc.getRow()][loc.getCol()] == 1) 
			if (cell.getOriginalTileColor().equals(Color.BLACK)) 
				cell.setCellColor(new Color(51, 0, 0));
			else 
				cell.setCellColor(new Color(255, 204, 204));
		else if(lastMoveCell[loc.getRow()][loc.getCol()] == 2) {
			if (cell.getOriginalTileColor().equals(Color.BLACK)) 
				cell.setCellColor(new Color(51, 0, 0));
			else 
				cell.setCellColor(new Color(255, 204, 204));
		}
		cell.setBorder(BorderFactory.createLineBorder(Color.black));

		//		game.generateThreatMap(game.getOtherPlayer());
		//		if (square.getThreat() == 1) // to see the threat level of each tile.
		//			cell.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5, true));
	}

	public void visulizeMoveValues(Location loc) {		
		Unit unit;
		if ((unit = game.getUnit(loc)) != null) {
			game.generateThreatMap(game.getOtherPlayer(unit.getOwner()));
			for (Move move : unit.getMoves()) {				
				Location targetLoc = move.getTargetLoc();
				cellText[targetLoc.getRow()][targetLoc.getCol()] += move.getValue(false);
			}					
		}		
	}	


	public Cell getCellByLoc(Location loc) {
		return cellBoard[loc.getRow()][loc.getCol()];
	}


	private Icon getIcon(Unit unit) {
		if (unit == null)
			return null;
		String owner;
		String backGround;
		if (unit.getOwner() == 1)
			owner = "WHITE_";
		else 
			owner = "BLACK_";
		if (getCellByLoc(unit.getLocation()).getOriginalTileColor().equals(Color.BLACK))
			backGround = "_DARK";
		else 
			backGround = "_LIGHT";		
		return iconMap.get(owner + unit.getType() + backGround);
	}


	private void generateIcons(int size) {	

		for(String imageName : imageList) {
			Icon nextIcon;
			nextIcon = new ImageIcon(imageMap.get(imageName).getScaledInstance(size, size, Image.SCALE_SMOOTH));
			iconMap.put(imageName, nextIcon);
		}
	}

	private void loadImages() {			
		String[] playerColor = {"WHITE_", "BLACK_"}; 
		String[] backGroundColor = {"_DARK", "_LIGHT"};
		imageList = new ArrayList<String>();	

		for(String backGround : backGroundColor) {
			for (String player : playerColor) {
				String imageName;
				for (String unitType : game.getLoadOut()) {	
					imageName = player + unitType + backGround;
					Image nextImage;
					nextImage = new ImageIcon(getClass().getResource(imageName + ".png")).getImage();
					imageList.add(imageName);
					imageMap.put(imageName, nextImage);
				}
				imageName = player + "PAWN" + backGround;
				imageList.add(imageName);
				imageMap.put(imageName, new ImageIcon(getClass().getResource(imageName + ".png")).getImage());
			}	
		}
	}
	
	public void togglePromotionPanel(boolean show) {
		if (show) {
			this.add(promotionPanel, 1);
		}
		else {
			this.remove(promotionPanel);	
		}
		pack();
	}
	
	public void toggleControlPanel(boolean show) {
		if (show) {
			this.add(controlPanel, 1);
			togglePromotionPanel(false);
		}
		else 
			this.remove(controlPanel);	
		pack();
	}

	public void toggleControlPanel() {	
		
		if(showControlPanel) 
			showControlPanel = false;			
		else 
			showControlPanel = true;
		
		toggleControlPanel(showControlPanel);
	}
}


