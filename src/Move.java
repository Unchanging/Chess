import java.util.ArrayList;

public class Move {

	private Game game;
	private Unit unit;
	private Location targetLoc;
	private Location originLoc;
	private int validated;

	public Move(Game game, Unit unit, Location targetLoc, int validated) {
		this.game = game;
		this.unit = unit;
		this.targetLoc = targetLoc;
		this.originLoc = unit.getLocation(); // save for later as the unit can move but the move will be stored
		this.validated = validated; // used to see if the move is valid, 0 is invalid, 1 is move, 2 is attack, 3 is project threat, 4 is pawn special, 5 is another pawn special: promotion, 6 is castling
	}

	public Location getTargetLoc() {
		return targetLoc;
	}

	public Location getOriginLoc() {
		return originLoc;
	}

	public Unit getUnit() {
		return unit;
	}

	public int getValue(boolean recursion) {

		int res = 0;

		if (validated == 5) { // promotion 
			return 50;
		}

		if (validated == 6) { //castling
			return 5; // not worth that much, but better than just advancing randomly
		}

		if (unit.getType().equals("PAWN") && game.getSquare(targetLoc).getThreat() == 0) { // adds additional value for Pawns if they can get closer to a promotion safely
			if(unit.getOwner() == 1 && targetLoc.getRow() < 3) 
				res += 2;
			else if(unit.getOwner() == 2 && targetLoc.getRow() > 4) 
				res += 2;
		}

		if (validated == 7) { // if this is a double step pawn move check so we dont risk an en-passant attack
			Unit nearbyUnit;
			if ((nearbyUnit = game.getUnit( new Location(targetLoc, 0, 1))) != null && nearbyUnit.getType().equals("PAWN") && nearbyUnit.getOwner() != unit.getOwner())  // check if there's a hostile Pawn to the right 
				res -= unit.getValue();			
			else if ((nearbyUnit = game.getUnit( new Location(targetLoc, 0, -1))) != null && nearbyUnit.getType().equals("PAWN") && nearbyUnit.getOwner() != unit.getOwner()) // check left
				res -= unit.getValue();
		}

		if (validated == 8) { //Capturing a En-passant Pawn
			res += game.getEnPassUnit().getValue(); 
		}

		if(game.getUnit(targetLoc) != null && game.getUnit(targetLoc).owner != unit.getOwner())
			res += game.getUnit(targetLoc).getValue(); // adds the value of the enemy unit
		if(game.getSquare(targetLoc).getThreat() != 0)
			res -= unit.getValue(); // subtracts the value of this unit if the square could be attacked
		if(game.getSquare(unit.getLocation()).getThreat() != 0)
			res += unit.getValue(); // adds the value of moving out of the way

		if (!recursion && game.getSquare(targetLoc).getThreat() == 0 && validated != 3) {
			ArrayList<Move> movesAfterThisMove = unit.getMoves(targetLoc);
			int bestValue = 0;
			for (Move move : movesAfterThisMove) {
				int nextMoveValue = move.getValue(true);
				if (nextMoveValue > bestValue)
					bestValue = nextMoveValue;					
			}
			res += bestValue / 3; // add the (possibly negative) value of the best move the unit could make next turn

		}
		return res;
	}

	public int getApplication( ) { // the rules are at the top. Information about what kind of move it is.
		return validated; 
	}

	public void executeMove() {	

		if (validated == 8) { // Pawn captures another who has does a doublestep past them
			game.getEnPassUnit().kill();
		}

		if (validated == 7) { // Pawn double-move which leaves an attackable node.
			Location NodeLoc = new Location((originLoc.getRow() + targetLoc.getRow())/2, originLoc.getCol()); //get the location for the square that the pawn jumped over
			game.setNodeAndUnit(NodeLoc, unit);
		} 
		else { // if we move a unit which doesn't leave a node. It removes the node if the player didn't use their opportunity
			game.setNodeAndUnit(null, null);
		}

		if(validated == 6) { // castling
			getUnit().castle(targetLoc);
		}
		else {
			if (validated == 5) { // promotion
				getUnit().promote();
			}
			else if(game.getUnit(targetLoc) != null) {
				game.getUnit(targetLoc).kill();
			}
			unit.move(targetLoc);			
		}

		game.addPreformedMove(this);
		game.progressGame();

	}

	public void ghostMove(boolean doOrUndo) {

		if (doOrUndo) { // do the test			
			if (validated == 8) { // en-passant move
				game.getEnPassUnit().ghostKill();
			}
			else if(validated == 2) { // attack move
				game.getUnit(targetLoc).ghostKill();
			}
			unit.ghostMove(targetLoc); // move the unit to the targetLoc
		}			
		else { // undo the test	
			if(validated == 2 || validated == 8) { // revive attacked unit
				game.getTempDead().ghostRevive();
			}
			unit.ghostMove(originLoc); // move the unit back to the originalLoc
		}		
	}

	public String toString() {		
		if (validated == 1)		
			return "A " + unit.getType().toLowerCase() + " at " + unit.getLocation() + " moving to " + targetLoc;
		if (validated == 2) {
			return "A " + unit.getType().toLowerCase() + " at " + unit.getLocation() + " attacking a " + 
					game.getUnit(targetLoc).getType().toLowerCase() +" at " + targetLoc;
		}
		if (validated == 3)
			return "A " + unit.getType().toLowerCase() + " at " + unit.getLocation() + " protecing the square at " + targetLoc;
		if (validated == 4)
			return "A " + unit.getType().toLowerCase() + " at " + unit.getLocation() + " can move to but not protect " + targetLoc;
		if (validated == 5)
			return "A " + unit.getType().toLowerCase() + " at " + unit.getLocation() + " can get a promotion";
		if (validated == 6)
			return "A " + unit.getType().toLowerCase() + " at " + unit.getLocation() + " can castle with the rook at " + targetLoc;
		if (validated == 7)
			return "The " + unit.getType().toLowerCase() + " at " + unit.getLocation() + " can move to but not protect " + targetLoc + " it will also leave an attackable node for the en-passant-rule";
		if (validated == 8)
			return "A " + unit.getType().toLowerCase() + " at " + unit.getLocation() + " can capture a pawn who moved two steps last turn at " + targetLoc;
		if (validated == 10)
			return "A " + unit.getType().toLowerCase() + " at " + unit.getLocation() + " moves to " + targetLoc;
		return "This move has a non valid validation, this should not happen, unit at " + unit.getLocation();		
	}

	@Override
	public boolean equals(Object otherObj) {

		if (!(otherObj instanceof Move)) 
			return false; 

		Move otherMove = (Move) otherObj;
		if (this.validated != otherMove.getApplication()) // a move-move can not be preformed as an attack-move or protect-move. Doesn't matter the other way around
			return false;
		if (this.unit != otherMove.getUnit()) 
			return false;
		if (!this.targetLoc.equals(otherMove.getTargetLoc()))
			return false;		
		return true;		
	}
}
