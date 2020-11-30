import java.util.ArrayList;

public class Game {

	private Square[][] squares; // all the spots in the game, only used to keep track of theatlevels
	private ArrayList<Unit> Player1Units; // stores all the units each player has
	private ArrayList<Unit> Player2Units; 
	private ArrayList<Move> preformedMoves; // remembers all the preformed moves this round
	private boolean playerOneIsHuman;	
	private boolean playerTwoIsHuman;
	private boolean gameOver = false;
	private int currentPlayer = 1;	
	private String[] loudOut = {"ROOK", "KNIGHT", "BISHOP", "QUEEN", "KING", "BISHOP", "KNIGHT", "ROOK"}; // original loadout
	int turn = 1; // this int is increased each time the game progresses, to keep track of turns on the current round
	private String promotionPreferencePlayer1; // Pawns can be promoted at the last rank of their movement, this variable remembers which unit they want
	private String promotionPreferencePlayer2; 
	private ArrayList<Integer> winners = new ArrayList<Integer>(); // used to store all the wining player. With 3 for draws
	private Location enPassantNode = null; // used if a pawn doublemoves the first move
	private Unit enPassantUnit = null; // used to store which pawn left the node
	private Unit temporaryDead = null; // used to store a temporarily dead unit when prforming ghostmoves used to controll if the king would be checked by a move. 
	private ArrayList<ArrayList<Move>> setOfMoveSetsPlayer1; // used to store all the possible moves each player had at the start of their turn each turn. In order to implment the threefold repetition rule
	private ArrayList<ArrayList<Move>> setOfMoveSetsPlayer2;

	public Game(boolean playerOneIsHuman, boolean playerTwoIsHuman) {

		this.playerOneIsHuman = playerOneIsHuman;
		this.playerTwoIsHuman = playerTwoIsHuman;
		squares = new Square[8][8];
		Player1Units = loadUnits(1);
		Player2Units = loadUnits(2);
		preformedMoves = new ArrayList<Move>();
		setOfMoveSetsPlayer1 = new ArrayList<ArrayList<Move>>();
		setOfMoveSetsPlayer2 = new ArrayList<ArrayList<Move>>();

		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares[0].length; j++) {
				squares[i][j] = new Square();
			}
		}
	}

	private ArrayList<Unit> loadUnits(int player) {		
		ArrayList<Unit> NewUnitList = new ArrayList<>();		
		int UnitRow, PawnRow;		
		if (player == 1) {
			UnitRow = 7;
			PawnRow = 6;
		}
		else {
			UnitRow = 0;
			PawnRow = 1;
		}		
		for(int i = 0; i < 8; i++) {			
			NewUnitList.add(Unit.spawnUnit(loudOut[i], new Location(UnitRow, i), player, this));
		}

		for(int i = 0; i < 8; i++) {
			NewUnitList.add(Unit.spawnUnit("PAWN", new Location(PawnRow, i), player, this));
		}		
		return NewUnitList;
	}

	public boolean move(Unit unit, Location targetLoc) {				
		ArrayList<Move> moves = unit.getMoves(); // get all possible moves and their applications		
		for (Move move : moves) { //check all moves
			if (move.getTargetLoc().equals(targetLoc) && move.getApplication() != 3) { // if we find a move to the targetLoc and it's not a threat-projecting move
				move.executeMove();	//move
				return true; // the move was preformed
			}
		}
		return false; // the move was not preformed 
	}

	public void addPreformedMove(Move move) {
		preformedMoves.add(move);
	}

	public Move getLastMove(int whichPlayer) {		
		for (int i = preformedMoves.size() -1; i >= 0; i--) {
			if (preformedMoves.get(i).getUnit().getOwner() == whichPlayer) {
				return preformedMoves.get(i);
			}
		}		
		return null;
	}

	public int getRows() {
		return squares.length;
	}

	public int getCols() {
		return squares[0].length;
	}

	public Square getSquare(Location loc) {
		return squares[loc.getRow()][loc.getCol()];
	}

	public Unit getUnit(Location loc) {

		Unit res = null;		
		for(Unit unit : getUnitsList(3)) {
			if (unit.getLocation().equals(loc))
				res = unit;
		}			
		return res;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public int getOtherPlayer() {
		if (currentPlayer == 1)
			return 2;
		return 1;
	}

	public int getOtherPlayer(int player) {
		if (player == 1)
			return 2;
		return 1;
	}

	public void changePlayer() {			
		if (currentPlayer == 1) 
			currentPlayer = 2;
		else {
			currentPlayer = 1;
			turn++;
		}
	}

	public String getPromotionPreference() {
		String type;
		if (currentPlayer == 1)
			type = promotionPreferencePlayer1;
		else
			type = promotionPreferencePlayer2;

		for(String eachType : loudOut) {
			if (eachType.equals(type) && !type.equals("KING"))
				return type;
		}
		System.out.println("Attempted to promote to a unit which is not in the loadout, get a Queen instead");
		return "QUEEN";
	}

	public void setPromotionPreference(String type) {
		if (currentPlayer == 1)
			promotionPreferencePlayer1 = type;
		else
			promotionPreferencePlayer2 = type;
	} 

	public void setNodeAndUnit(Location loc, Unit unit) {
		enPassantNode = loc;
		enPassantUnit = unit;
	}

	public Location getNode() {
		return enPassantNode;
	}

	public Unit getEnPassUnit() {
		return enPassantUnit;
	}

	public void setSinglePlayer(boolean singlePlayer) {
		playerTwoIsHuman = !playerTwoIsHuman;		
		if (currentPlayer == 2 && singlePlayer == true) {
			computerPlay();
		}
	}

	public boolean getSinglePlayer() {
		return !playerTwoIsHuman;
	}

	public void progressGame() {	
		if (!gameOver) {
			changePlayer();
			generateThreatMap(getOtherPlayer());

			if (currentPlayer == 1 && !playerOneIsHuman)
				computerPlay();

			if (currentPlayer == 2 && !playerTwoIsHuman)
				computerPlay();
		}
	}

	public void computerPlay() {

		//Check if the other player left his king in check. if so win without playing.

		if(isKingChecked(getOtherPlayer())) {
			winners.add(currentPlayer);
			gameOver = true;
		}
		
		else { // if the enemy king wasn't in in check at the start of the turn
			generateThreatMap(getOtherPlayer());
			setPromotionPreference("QUEEN");

			ArrayList<Move> allMovesThisTurn = new ArrayList<Move>(); // stores all possible moves 
			for (Unit unit : getUnitsList(currentPlayer))  // gets all the units
				for (Move move : unit.getMoves())  // gets all the moves
					allMovesThisTurn.add(move); 

			ArrayList<Move> bestMoves = new ArrayList<Move>(); // stores a collection of the best moves this turn
			int highestMoveValue = -1000; // gets overwritten by any valid move

			for (Move move : allMovesThisTurn) { // checks all the moves
				int moveValue = move.getValue(false); // gets the value for each move. One level of recursion where necesary
				boolean leavesOwnKingCheck = true; // true is just a default to get the "may not have been initized" error to go away. Shouldn't matter
				if(move.getApplication() != 3) {	// if it's a move which will be considered 
					leavesOwnKingCheck = leavesTheKingCheck(move); // see if it would leave the king in check. This is both valid if the king is already in check or if this move would make it so.
				} 
				if (moveValue > highestMoveValue && move.getApplication() != 3 && !leavesOwnKingCheck) { // resets the list of best moves if it finds a move of higher value which doesn't leave the king in check
					highestMoveValue = moveValue;
					bestMoves = new ArrayList<Move>();
				}
				if (moveValue == highestMoveValue && move.getApplication() != 3 && !leavesOwnKingCheck) { // adds the move to the list of best moves if it has the highest value, is not a threat projecting move and doesn't leave the king in check
					bestMoves.add(move);
				}
			}

			if (setAndCompareMoveSets(allMovesThisTurn)) { // checks for the threefold repetition by comparing the set of all moves with all the previous sets from that player
				winners.add(3); // this is a threefold repetition draw
				gameOver = true;
			}		
			else if (bestMoves.size() != 0) { // If there are valid moves and the threefold repetition hasn't happened 
				int ChoosenAttack = (int) (Math.random() * bestMoves.size());		
				bestMoves.get(ChoosenAttack).executeMove();
			}
			else { // if there are no valid moves
				if (!isKingChecked(currentPlayer)) { // if the king is _NOT_ checked. 
					winners.add(3); // this is a stalemate draw
				}
				else { 
					winners.add(getOtherPlayer());	// a victory for the other player
				}
				gameOver = true;
			}
		}
	}

	private boolean setAndCompareMoveSets(ArrayList<Move> allMovesThisTurn) {

		getMoveSet(currentPlayer).add(allMovesThisTurn); // adds this set of moves to the set of sets

		int identicalsets = 0;
		for (int i = 0; i < getMoveSet(currentPlayer).size(); i++) // check all the sets
			if (getMoveSet(currentPlayer).get(i).equals(allMovesThisTurn))  // if an identical set is found add to the number of idential sets
				identicalsets++;

		if(identicalsets == 3) {
			return true;
		}			
		return false;
	}

	private ArrayList<ArrayList<Move>> getMoveSet(int player) {
		if (player == 1)
			return setOfMoveSetsPlayer1;
		return setOfMoveSetsPlayer2;
	}

	public void kingHasDied(Unit unit) {
		winners.add(getOtherPlayer(unit.getOwner()));		
		gameOver = true;
	}

	public void yield() {
		winners.add(getOtherPlayer());
		gameOver = true;
	}

	public void setTempDead(Unit unit) {
		temporaryDead = unit;
	}

	public Unit getTempDead() {
		return temporaryDead;
	}

	private boolean leavesTheKingCheck(Move move) {
		move.ghostMove(true);
		boolean res = isKingChecked(currentPlayer);	
		move.ghostMove(false);
		generateThreatMap(getOtherPlayer());
		return res;
	}

	public boolean isKingChecked(int player) {	
		generateThreatMap(getOtherPlayer(player));
		for (Unit unit : getUnitsList(player)) 
			if(unit.getType().equals("KING")) 
				if (getSquare(unit.getLocation()).getThreat() == 1) 
					return true;				
		return false;
	}

	public int getNumberOfTurns() {
		return turn;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void restart() {

		for (Unit unit : getUnitsList(3))  // detta ger ett poäng till fel sida
			unit.killQuetly();
		currentPlayer = 1;	
		gameOver = false;
		Player1Units = loadUnits(1);
		Player2Units = loadUnits(2);
		setOfMoveSetsPlayer1 = new ArrayList<ArrayList<Move>>();
		setOfMoveSetsPlayer2 = new ArrayList<ArrayList<Move>>();
		preformedMoves = new ArrayList<Move>();
		turn = 0;		
		generateThreatMap(getOtherPlayer());


	}

	public ArrayList<Integer> getWinners() {
		return winners;
	}

	public void generateThreatMap (int player) {		
		for (Square[] squareArray : squares) { // remove threat from all tiles
			for (Square square : squareArray) {
				square.removeThreat();
			}			
		}

		for (Unit unit : getUnitsList(player)) {	// get all units
			ArrayList<Move> moves = unit.getMoves(); // get all the moves of each unit
			for (Move move : moves) { 
				if (move.getApplication() != 4 && move.getApplication() != 5 && move.getApplication() != 6 && move.getApplication() != 7) { // if the move is not a pawn's forward move, or the promotion-move, or the castling move, or the pawn's en-passant-move  set the threat of that tile to 1
					getSquare(move.getTargetLoc()).increaseThreatTo(1); // only got 0 or 1 right now
				}
			}
		}
	}

	public ArrayList<Unit> getUnitsList(int whichPlayer) {
		if (whichPlayer == 1)
			return Player1Units;
		if (whichPlayer == 2)
			return Player2Units;
		else {
			ArrayList<Unit> res = new ArrayList<Unit>();
			res.addAll(Player1Units);
			res.addAll(Player2Units);		
			return res;
		}
	}

	public String[] getLoadOut() {
		return loudOut;
	}

	public boolean isInBounds(Location loc){      
		return loc.getCol() < squares[0].length && loc.getCol() >= 0 && loc.getRow() < squares.length && loc.getRow() >= 0;
	}

	public int validateMove(Unit unit, Location targetLoc) { // the int returned judges the applications of this move. 0 is unviable 1 is moving 2 is attacking 3 is just projecting threat
		// Do not ask for the unit's location, this is used to validate moves from where the unit isen't yet
		if(!isInBounds(targetLoc))  // the loc is not valid
			return 0;		
		if (getUnit(targetLoc) != null && unit.getOwner() == getUnit(targetLoc).getOwner()) // If there's a friendly on the square
			return 3;
		if (getUnit(targetLoc) != null && unit.getOwner() != getUnit(targetLoc).getOwner()) // if there's a hostile on the spot
			return 2;
		else {
			return 1; // you can move here but there's nothing to attack
		}
	}
}
