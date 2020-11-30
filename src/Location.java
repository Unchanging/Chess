public class Location implements Comparable<Location>{ 
	private int row; 
	private int col; 

	public Location(int row, int col) { 
		this.row = row; 
		this.col = col; 
	}

	public Location(Location loc, int y, int x) { 
		this(loc.getRow() + y, loc.getCol() + x);
	}

	public int getRow() { 
		return row; 
	}

	public int getCol() { 
		return col; 
	}

	public boolean equals(Location otherLoc) { 
		return row == otherLoc.getRow() && col == otherLoc.getCol(); 
	}

	public String toString() { 
		return "(" + row + ", " + col + ")"; 
	}
	
    public int compareTo(Location otherLoc) {
    	int res = 0;
    	
    	if (this.getRow() < otherLoc.getRow()) {
    		return -1;
    	}
    	if (this.getRow() > otherLoc.getRow()) {
    		return 1;
    	}
    	if (this.getCol() < otherLoc.getCol()) {
    		return -1;
    	}
    	if (this.getCol() > otherLoc.getCol()) {
    		return 1;
    	}    	
        return res;
    }
}
