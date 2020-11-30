import java.util.ArrayList;

public class Unit {

	protected Location loc;
	protected int owner;
	protected Game game;
	protected int numberOfMoves = 0; // used to determine if Castling is allowed
	protected int value; // how much is the piece worth? King > Queen > ... > Pawn

	public Unit() {
		System.out.println("placeholder constructor");
	}

	public Unit(Location loc, int owner, Game game, int value) {
		this.loc = loc;
		this.owner = owner;
		this.game = game;
		this.value = value;
	}

	public static Unit spawnUnit(String type, Location loc, int player, Game game) {
		Unit res;

		switch (type) {
		case "ROOK": {
			res = new ROOK(loc, player, game, 15);
			break;
		}
		case "KNIGHT": {
			res = new KNIGHT(loc, player, game, 15);
			break;
		}
		case "BISHOP": {
			res = new BISHOP(loc, player, game, 15);
			break;
		}
		case "QUEEN": {
			res = new QUEEN(loc, player, game, 25);
			break;
		}
		case "KING": {
			res = new KING(loc, player, game, 100);
			break;
		}
		case "PAWN": {
			res = new PAWN(loc, player, game, 5);
			break;
		}
		default:
			res = null;
			System.out.println("Unit spawner not implemented!");
		}
		return res;
	}

	public Location getLocation( ) {
		return loc;
	}

	public int getOwner( ) {
		return owner;
	}

	public String getType() {
		System.out.println("Fel i unit's getType");
		return("getType default i Unit klassen, ska skrivas över");
	}

	public int getValue() {
		return value;
	}

	public int getNumberOfMoves() {
		return numberOfMoves;
	}

	public ArrayList<Move> getMoves() {		
		System.err.println("This method should have been overshadowed");
		return new ArrayList<Move>();
	}

	public ArrayList<Move> getMoves(Location loc) {		
		System.err.println("This method should have been overshadowed");
		return new ArrayList<Move>();
	}

	public void move(Location targetLoc) {
		numberOfMoves++;
		loc = targetLoc;
	}
	
	public void ghostMove(Location targetLoc) {
		loc = targetLoc;
	}

	public void kill() {
		game.getUnitsList(owner).remove(this);
	}

	public void killQuetly() { // so we can restart the game without the king's death triggering a gameOver
		game.getUnitsList(owner).remove(this);
	}
	
	public void ghostKill() {
		game.getUnitsList(owner).remove(this);
		game.setTempDead(this);
	}
	
	public void ghostRevive() {		
		game.getUnitsList(owner).add(game.getTempDead());
		game.setTempDead(null);
	}

	public void promote() {
		System.out.println("this should be overshadowed");
	}

	public void castle(Location targetLoc) {
		System.out.println("this should be overshadowed");
	}
}






class ROOK extends Unit{
	public ROOK(Location loc, int player, Game game, int value) {
		super(loc, player, game, value);
	}

	public String getType() {
		return "ROOK";
	}

	public ArrayList<Move> getMoves() {
		return getMoves(loc);
	}	

	public ArrayList<Move> getMoves(Location startLoc) {
		int[] y = {-1, 0, 1, 0}; //how the rook moves
		int[] x = {0, 1, 0, -1};
		ArrayList<Move> res = new ArrayList<Move>();		
		for(int i = 0; i < 4; i++) { // four directions			
			Location nextMoveLoc;
			int validated;
			int step = 0;			
			do {	
				step++;				
				nextMoveLoc = new Location(startLoc, step*y[i], step*x[i]);	
				validated = game.validateMove(this, nextMoveLoc);

				if (validated > 0) {
					res.add(new Move(game, this, nextMoveLoc, validated));
				}				
			} while(validated == 1);
		}
		return res;
	}
}

class KNIGHT extends Unit{
	public KNIGHT(Location loc, int player, Game game, int value) {
		super(loc, player, game, value);
	}	

	public String getType() {
		return "KNIGHT";
	}

	public ArrayList<Move> getMoves() {
		return getMoves(loc);
	}	

	public ArrayList<Move> getMoves(Location startLoc) {
		int[] y = {-2, -1, 1, 2, 2, 1, -1, -2}; // how the knight can move 
		int[] x = {1, 2, 2, 1, -1, -2, -2, -1};
		ArrayList<Move> res = new ArrayList<Move>();
		for(int i = 0; i < 8; i++) {				
			Location nextMoveLoc = new Location(startLoc, y[i], x[i]);
			int validated = game.validateMove(this, nextMoveLoc); // determines the use off the move, can this be an attacking move, is it valid at all and so on....
			if (validated > 0) {
				res.add(new Move(game, this, nextMoveLoc, validated));
			}
		}
		return res;
	}
}

class BISHOP extends Unit{
	public BISHOP(Location loc, int player, Game game, int value) {
		super(loc, player, game, value);
	}

	public String getType() {
		return "BISHOP";
	}

	public ArrayList<Move> getMoves() {
		return getMoves(loc);
	}	

	public ArrayList<Move> getMoves(Location startLoc) {
		int[] y = {-1, 1, 1, -1}; //how the bishop moves
		int[] x = {1, 1, -1, -1};
		ArrayList<Move> res = new ArrayList<Move>();		
		for(int i = 0; i < 4; i++) { // four directions			
			Location nextMoveLoc;
			int validated;
			int step = 0;			
			do {	
				step++;				
				nextMoveLoc = new Location(startLoc, step*y[i], step*x[i]);	
				validated = game.validateMove(this, nextMoveLoc);

				if (validated > 0) {
					res.add(new Move(game, this, nextMoveLoc, validated));
				}				
			} while(validated == 1);
		}
		return res;
	}
}

class QUEEN extends Unit{
	public QUEEN(Location loc, int player, Game game, int value) {
		super(loc, player, game, value);
	}

	public String getType() {
		return "QUEEN";
	}

	public ArrayList<Move> getMoves() {
		return getMoves(loc);
	}	

	public ArrayList<Move> getMoves(Location startLoc) {
		int[] y = {-1, -1, 0, 1, 1, 1, 0, -1}; //how the queen moves
		int[] x = {0, 1, 1, 1, 0, -1,-1, -1};
		ArrayList<Move> res = new ArrayList<Move>();		
		for(int i = 0; i < 8; i++) { // eight directions			
			Location nextMoveLoc;
			int validated;
			int step = 0;			
			do {	
				step++;				
				nextMoveLoc = new Location(startLoc, step*y[i], step*x[i]);	
				validated = game.validateMove(this, nextMoveLoc);

				if (validated > 0) {
					res.add(new Move(game, this, nextMoveLoc, validated));
				}				
			} while(validated == 1);
		}
		return res;
	}
}

class KING extends Unit{
	public KING(Location loc, int player, Game game, int value) {
		super(loc, player, game, value);
	}

	public String getType() {
		return "KING";
	}

	public void kill() {
		game.getUnitsList(owner).remove(this);
		game.kingHasDied(this);
	}		
	
	public void killQuetly() {
		game.getUnitsList(owner).remove(this);
	}

	public void castle(Location targetLoc) {

		if (targetLoc.getCol() < loc.getCol()) {
			game.getUnit(targetLoc).move(new Location(loc, 0, -1));
			this.move( new Location(loc, 0, -2));
		}
		else {
			game.getUnit(targetLoc).move(new Location(loc, 0, 1));
			this.move( new Location(loc, 0, 2));
		}	
	}

	public ArrayList<Move> getMoves() {
		return getMoves(loc);
	}	

	public ArrayList<Move> getMoves(Location startLoc) {

		int[] y = {-1, -1, 0, 1, 1, 1, 0, -1}; //how the king moves
		int[] x = {0, 1, 1, 1, 0, -1,-1, -1};
		ArrayList<Move> res = new ArrayList<Move>();		
		for(int i = 0; i < 8; i++) { // eight directions			
			Location nextMoveLoc = new Location(startLoc, y[i], x[i]);	
			int validated = game.validateMove(this, nextMoveLoc);
			if (validated > 0 && game.getSquare(nextMoveLoc).getThreat() == 0) { // The king may not put himself in check
				res.add(new Move(game, this, nextMoveLoc, validated));
			}				
		}

		if(numberOfMoves == 0 && game.getSquare(loc).getThreat() == 0) { // castling and the king is not in check

			int[] steps = {-4, -3, -2, -1, 1, 2, 3}; // steps to the right and left to consider 	
			Location[] places= new Location[7];				
			for (int i = 0; i < steps.length; i++) // gets a list of all the locs to consider				
				places[i] = new Location(loc, 0, steps[i]);			

			if (game.getUnit(places[0]) != null && game.getUnit(places[0]).getType().equals("ROOK") && game.getUnit(places[0]).getNumberOfMoves() == 0) { // Checks if there's any unit to the far left, checks if it's a rook and check's if it has moved
				if (game.getUnit(places[1])== null && game.getUnit(places[2]) == null && game.getUnit(places[3]) == null) { // checks if the three spaces between the king and the rook are free
					if(game.getSquare(places[2]).getThreat() == 0) { // only move if it doesn't put the king in check
						res.add(new Move(game, this, places[0], 6));
					}
				}
			}
			if (game.getUnit(places[6]) != null && game.getUnit(places[6]).getType().equals("ROOK") && game.getUnit(places[6]).getNumberOfMoves() == 0) { // Checks if there's any unit to the far right, checks if it's a rook and check's if it has moved
				if (game.getUnit(places[4])== null && game.getUnit(places[5]) == null) { // checks if the two spaces between the king and the rook are free
					if(game.getSquare(places[5]).getThreat() == 0) { // only move if it doesn't put the king in check
						res.add(new Move(game, this, places[6], 6));
					}
				}
			}				
		}	
		return res;
	}
}

class PAWN extends Unit{
	public PAWN(Location loc, int player, Game game, int value) {
		super(loc, player, game, value);
	}

	public String getType() {
		return "PAWN";
	}

	public ArrayList<Move> getMoves() {
		return getMoves(loc);
	}

	public void promote() {
		this.kill();
		Unit thePromotedUnit = Unit.spawnUnit(game.getPromotionPreference(), loc, owner, game);		
		game.getUnitsList(owner).add(thePromotedUnit);
	}

	public int getValue() { // the Pawn should have a slightly increasing value the further is has moved as it gets closer to being promoted
		if (owner == 1)
			return value + (loc.getRow() - 6) * -1;
		return value + (loc.getRow() - 1);
	}

	public ArrayList<Move> getMoves(Location startLoc) {

		int[] y = {-1, -1, -1, -2}; //how the pawn moves
		int[] x = {-1, 1, 0, 0};
		ArrayList<Move> res = new ArrayList<Move>();
		int direction, promotionRow, startRow;
		if (this.getOwner() == 1) { 
			direction = 1;
			promotionRow = 0;
			startRow = 6;
		}
		else {
			direction = -1;
			promotionRow = 7;
			startRow = 1;
		}

		if (startLoc.getRow() == promotionRow) {
			res.add(new Move(game, this, startLoc, 5)); // promotion-move is avalible. Skips all validations and checks
		}

		Location nextMoveLoc;
		for(int i = 0; i < 2; i++) { // to the left and then the right
			nextMoveLoc = new Location(startLoc, direction*y[i], x[i]); //next ordinary moveLoc
			if(game.isInBounds(nextMoveLoc)) { //basic validitycheck
				
				
				if(game.getUnit(nextMoveLoc) != null) { // if there's another unit on the targetLoc 
					if (game.getUnit(nextMoveLoc).getOwner() != owner)   // if the unit is hostile
						res.add(new Move(game, this, nextMoveLoc, 2)); // add an attacking move

					else // if the unit is friendly
						res.add(new Move(game, this, nextMoveLoc, 3)); // add a threat projecting move
				}
				else if(game.getNode() != null && nextMoveLoc.equals(game.getNode())) {
					res.add(new Move(game, this, nextMoveLoc, 8));
				}
				else // if the square is empty
					res.add(new Move(game, this, nextMoveLoc, 3));
			}
		}
		
		boolean blocked = false; // can we make a double step? (which is the forth step)
		if (loc.getRow() != startRow) // if we're not on the first row, It should be blocked
			blocked = true;
		
		
		nextMoveLoc = new Location(startLoc, direction*y[2], x[2]); //next ordinary moveLoc. one step ahead
		if(game.isInBounds(nextMoveLoc)) { //basic validitycheck
			if(game.getUnit(nextMoveLoc) != null)  // if there's another unit on the targetLoc 
				blocked = true; // if the pawn could make this move it can't make the next either
			else // if the square is empty
				res.add(new Move(game, this, nextMoveLoc, 4)); // add a peasent-special-move: it can move, but not attack or project theat
		}
		
		nextMoveLoc = new Location(startLoc, direction*y[3], x[3]); //next ordinary moveLoc. Two steps ahead
		if(game.isInBounds(nextMoveLoc) && !blocked) { //basic validitycheck and a check to see if the last move was blocked
			if(game.getUnit(nextMoveLoc) == null)  // if the square is empty
				res.add(new Move(game, this, nextMoveLoc, 7)); // add a peasent-special-move it's like 4 but it leaves an attackable node there to enable the en-passant rule
		}
		return res;
	}
}

